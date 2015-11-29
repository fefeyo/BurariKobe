package archetypenova.com.jphacksapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import archetypenova.com.jphacksapp.MainActivity;
import archetypenova.com.jphacksapp.R;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * 距離選択画面
 */
public class SelectDistanceFragment extends Fragment {


    public SelectDistanceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_select_distance, container, false);
        ButterKnife.inject(this, v);
        ((MainActivity)getActivity()).checkMyLocation();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.distance_submit)
    void onClick(){
        if(0 != MainActivity.r) {
            ((MainActivity) getActivity()).nextPage(new PlaceListFragment());
        }else{
            Toast.makeText(getActivity(), "距離を選択してください", Toast.LENGTH_SHORT).show();
        }
    }



    @OnItemSelected(R.id.distance_time)
    void onItemSelected(AdapterView<?> parent, int position){
        if(0 != position) {
            MainActivity.r = Integer.parseInt(parent.getItemAtPosition(position).toString()) * 100;
        }
    }

}
