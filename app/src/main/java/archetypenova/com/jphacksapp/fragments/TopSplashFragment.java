package archetypenova.com.jphacksapp.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import archetypenova.com.jphacksapp.MainActivity;
import archetypenova.com.jphacksapp.R;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 起動後スプラッシュ画面
 */
public class TopSplashFragment extends Fragment {

    private View v;

    public TopSplashFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_top_splash, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.top)
    void onClick(View view){
        ((MainActivity)getActivity()).fadeoutTop();
    }

}
