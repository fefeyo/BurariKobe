package archetypenova.com.jphacksapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import archetypenova.com.jphacksapp.R;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 移動中
 * 投稿一覧画面
 */
public class MovingFragment extends Fragment {

    public MovingFragment() {}

    private OnPost mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_moving, container, false);
        ButterKnife.inject(this, v);

        return v;
    }

    public void setOnPostListener(OnPost listener){
        this.mListener = listener;
    }

    @Override
    public void onPause() {
        super.onPause();
        ButterKnife.reset(this);
    }

    interface OnPost{
        void onPost(String title, String content);
        void notPost();
    }
}
