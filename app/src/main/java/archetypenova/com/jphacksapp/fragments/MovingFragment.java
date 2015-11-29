package archetypenova.com.jphacksapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import archetypenova.com.jphacksapp.R;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 移動中
 * 投稿一覧画面
 */
public class MovingFragment extends Fragment {

    @InjectView(R.id.post_title)
    EditText postTitle;
    @InjectView(R.id.post_text)
    EditText postText;
    @InjectView(R.id.post_negative)
    ImageButton postNegative;
    @InjectView(R.id.post_positive)
    ImageButton postPositive;

    public MovingFragment() {
    }

    private OnPost mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_moving, container, false);
        ButterKnife.inject(this, v);

        return v;
    }

    @OnClick({
            R.id.post_positive,
            R.id.post_negative
    })
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_positive:
                mListener.onPost(
                        postTitle.getText().toString(),
                        postText.getText().toString()
                );
                break;
            case R.id.post_negative:
                mListener.notPost();
                break;
        }
    }

    public void setOnPostListener(OnPost listener) {
        this.mListener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    interface OnPost {
        void onPost(String title, String content);

        void notPost();
    }
}
