package archetypenova.com.jphacksapp.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import archetypenova.com.jphacksapp.MainActivity;
import archetypenova.com.jphacksapp.R;
import archetypenova.com.jphacksapp.ResultActivity;
import archetypenova.com.jphacksapp.items.PostItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment implements MovingFragment.OnPost {

    @InjectView(R.id.post_list)
    ListView mListView;
    @InjectView(R.id.write_post)
    ImageButton writePost;
    @InjectView(R.id.target_place)
    TextView targetPlace;
    @InjectView(R.id.arrive)
    ImageButton arrive;
    private PostAdapter mAdapter;
    private MovingFragment fragment;
    private String mTitle;

    public PostFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post, container, false);
        ButterKnife.inject(this, v);
        mAdapter = new PostAdapter(
                getActivity().getApplicationContext(),
                R.layout.place_row
        );
        mTitle = getArguments().getString("title");
        targetPlace.setText(mTitle);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(v.findViewById(R.id.empty));
        downloadPostItems();

        return v;
    }

    private void downloadPostItems() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Now Loading ...");
        dialog.show();
        final String URL = "http://archetypenova.sakura.ne.jp/jphacks/get_posts.php?next=" + mTitle;
        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(
                getActivity().getApplicationContext(),
                URL,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            final String result = new String(responseBody, "UTF-8");
                            final Gson gson = new Gson();
                            PostItem[] list = gson.fromJson(result, PostItem[].class);
                            mAdapter.clear();
                            mAdapter.addAll(list);
                            mAdapter.notifyDataSetChanged();
                            Log.d("postitems", list + "");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dialog.dismiss();
                    }
                });
    }

    private void showPostDialog() {
        fragment = new MovingFragment();
        fragment.setOnPostListener(this);
        ((MainActivity) getActivity()).showDialogDetail(fragment);
    }

    @OnClick({
            R.id.write_post,
            R.id.arrive,
            R.id.end
    })
    void onClick(View v){
        switch (v.getId()){
            case R.id.write_post:
                showPostDialog();
                break;
            case R.id.arrive:
                ((MainActivity)getActivity()).nextPage(new PlaceListFragment());
                break;
            case R.id.end:
                final Intent i = new Intent(getActivity().getApplicationContext(), ResultActivity.class);
                getActivity().startActivity(i);
                break;
        }
    }

    @Override
    public void onPost(String title, String content) {
        ((MainActivity) getActivity()).dismissDialogDetail(fragment);
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Now Loading ...");
        dialog.show();
        final String URL = "http://archetypenova.sakura.ne.jp/jphacks/insert_post.php?title=" + title + "&content=" + content + "&next=" + mTitle;
        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(
                getActivity().getApplicationContext(),
                URL,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            final String result = new String(responseBody, "UTF-8");
                            downloadPostItems();
                            Log.d("result", result);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dialog.dismiss();
                        Snackbar.make(getView(), "投稿しませんでした", Snackbar.LENGTH_SHORT);
                    }
                }
        );
    }

    @Override
    public void notPost() {
        ((MainActivity) getActivity()).dismissDialogDetail(fragment);
        Snackbar.make(getView(), "投稿しませんでした", Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private class PostAdapter extends ArrayAdapter<PostItem> {

        private LayoutInflater inflater;

        public PostAdapter(Context context, int resource) {
            super(context, resource);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) convertView = inflater.inflate(R.layout.place_row, null);
            PostItem item = getItem(position);
            TextView title = (TextView) convertView.findViewById(R.id.place_title);
            title.setText(item.title);
            TextView text = (TextView) convertView.findViewById(R.id.place_text);
            text.setText(item.content);

            return convertView;
        }
    }

}
