package archetypenova.com.jphacksapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import archetypenova.com.jphacksapp.R;
import archetypenova.com.jphacksapp.items.PlaceItem;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceDetailFragment extends Fragment {

    @InjectView(R.id.detail_title)
    TextView detailTitle;
    @InjectView(R.id.detail_genre)
    TextView detailGenre;
    @InjectView(R.id.detail_tel)
    TextView detailTel;
    @InjectView(R.id.detail_url)
    TextView detailUrl;
    @InjectView(R.id.detail_text)
    TextView detailText;
    @InjectView(R.id.place_negative)
    ImageButton placeNegative;
    @InjectView(R.id.place_positive)
    ImageButton placePositive;
    private DetailClickListener mListener;

    private String title;
    public PlaceDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_place_detail, container, false);
        ButterKnife.inject(this, v);

        Bundle b = getArguments();
        title = b.getString("spot");
        detailTitle.setText(title);
        detailText.setText(b.getString("text"));
        detailGenre.setText(b.getString("main"));
        detailTel.setText(b.getString("tel"));
        detailUrl.setText(b.getString("url"));
        Linkify.addLinks(detailUrl, Linkify.ALL);
        placePositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPositiveClick(title);
            }
        });
        placeNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNegativeClick();
            }
        });

        ButterKnife.inject(this, v);
        return v;
    }

    public void setOnDialogClickListener(DetailClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    interface DetailClickListener {
        void onPositiveClick(String title);

        void onNegativeClick();
    }

}
