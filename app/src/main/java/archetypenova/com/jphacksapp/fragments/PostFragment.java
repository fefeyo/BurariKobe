package archetypenova.com.jphacksapp.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;

import archetypenova.com.jphacksapp.MainActivity;
import archetypenova.com.jphacksapp.R;
import archetypenova.com.jphacksapp.items.PlaceItem;
import archetypenova.com.jphacksapp.items.PostItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment implements MovingFragment.OnPost{

    @InjectView(R.id.post_list)
    ListView mListView;
    private PostAdapter mAdapter;
    private String next;
    private MovingFragment fragment;

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
        mListView.setAdapter(mAdapter);
        next = getArguments().getString("next");
        downloadPostItems();

        return v;
    }

    private void downloadPostItems() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Now Loading ...");
        dialog.show();
        final String URL = "http://archetypenova.sakura.ne.jp/jphacks/get_posts.php?next="+next;
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

    private void showPostDialog(){
        fragment = new MovingFragment();
        fragment.setOnPostListener(this);
        ((MainActivity) getActivity()).showDialogDetail(fragment);
    }

    @Override
    public void onPost(String title, String content) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Now Loading ...");
        dialog.show();
        final String URL = "http://archetypenova.sakura.ne.jp/jphacks/insert_post.php?title="+title+"&content="+content+"&next"+next;
        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(
                getActivity().getApplicationContext(),
                URL,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try{
                            final String result = new String(responseBody, "UTF-8");
                            Log.d("result", result);
                        }catch (UnsupportedEncodingException e){
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
                }
        );
    }

    @Override
    public void notPost() {
        ((MainActivity)getActivity()).dismissDialogDetail(fragment);
    }

    private class PostAdapter extends ArrayAdapter<PostItem> {

        private LayoutInflater inflater;

        public PostAdapter(Context context, int resource) {
            super(context, resource);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(null == convertView) convertView = inflater.inflate(R.layout.place_row, null);
            PostItem item = getItem(position);
            TextView title = (TextView)convertView.findViewById(R.id.place_title);
            title.setText(item.title);
            TextView text = (TextView)convertView.findViewById(R.id.place_text);
            text.setText(item.content);

            return convertView;
        }
    }

}
