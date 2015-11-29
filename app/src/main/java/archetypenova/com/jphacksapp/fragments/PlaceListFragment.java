package archetypenova.com.jphacksapp.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.util.List;

import archetypenova.com.jphacksapp.MainActivity;
import archetypenova.com.jphacksapp.R;
import archetypenova.com.jphacksapp.items.PlaceItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import cz.msebera.android.httpclient.Header;

/**
 * 施設一覧画面
 */
public class PlaceListFragment extends Fragment implements PlaceDetailFragment.DetailClickListener {

    @InjectView(R.id.place_list)
    ListView mListView;

    private PlaceAdapter mAdapter;
    private PlaceDetailFragment fragment;

    public PlaceListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_place_list, container, false);
        ButterKnife.inject(this, v);
        mAdapter = new PlaceAdapter(
                getActivity().getApplicationContext(),
                R.layout.place_row
        );
        mListView.setAdapter(mAdapter);
        downloadPlaces();
        return v;
    }

    private void downloadPlaces() {
        final String URL = "http://archetypenova.sakura.ne.jp/jphacks/get_places.php?lat=" + MainActivity.myLat + "&lng=" + MainActivity.myLng + "&r=" + MainActivity.r;
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Now Loading ...");
        dialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(
                getActivity().getApplicationContext(),
                "http://archetypenova.sakura.ne.jp/jphacks/get_places.php",
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            final String result = new String(responseBody, "UTF-8");
                            final Gson gson = new Gson();
                            PlaceItem[] list = gson.fromJson(result, PlaceItem[].class);
                            List<String> everList = MainActivity.everList;
                            boolean flag = false;
                            for(PlaceItem item : list){
                                for(String place : everList){
                                    if(item.spot.equals(place)) flag = true;
                                }
                                if(!flag) mAdapter.add(item);
                                flag = false;
                            }
                            mAdapter.notifyDataSetChanged();
                            Log.d("placeitems", list + "");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("failed", "failed");
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnItemClick(R.id.place_list)
    void onItemClick(AdapterView<?> parent, int position) {
        if(!MainActivity.isFrontShown) {
            final PlaceItem item = (PlaceItem) parent.getItemAtPosition(position);
            fragment = new PlaceDetailFragment();
            Bundle b = new Bundle();
            b.putString("spot", item.spot);
            b.putString("text", item.text);
            b.putString("main", item.main);
            b.putString("tel", item.tel);
            b.putString("url", item.url);
            fragment.setArguments(b);
            fragment.setOnDialogClickListener(this);
            ((MainActivity) getActivity()).showDialogDetail(fragment);
        }
    }

    @Override
    public void onPositiveClick(String title) {
        ((MainActivity) getActivity()).dismissDialogDetail(fragment);
        PostFragment fragment = new PostFragment();
        Bundle b = new Bundle();
        b.putString("title", title);
        MainActivity.everList.add(title);
        fragment.setArguments(b);
        ((MainActivity) getActivity()).nextPage(fragment);
    }

    @Override
    public void onNegativeClick() {
        ((MainActivity) getActivity()).dismissDialogDetail(fragment);
    }

    private class PlaceAdapter extends ArrayAdapter<PlaceItem> {

        private LayoutInflater inflater;
        int resource;

        public PlaceAdapter(Context context, int resource) {
            super(context, resource);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.resource = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) convertView = inflater.inflate(resource, null);

            PlaceItem item = getItem(position);
            TextView title = (TextView) convertView.findViewById(R.id.place_title);
            title.setText(item.spot);
            TextView text = (TextView) convertView.findViewById(R.id.place_text);
            text.setText(item.text);

            return convertView;
        }
    }
}
