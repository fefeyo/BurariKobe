package archetypenova.com.jphacksapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ResultActivity extends AppCompatActivity {

    @InjectView(R.id.result)
    GridView mGridView;

    private ResultAdapter mAdapter;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.inject(this);
        mAdapter = new ResultAdapter(
                getApplicationContext(),
                0
        );
        mGridView.setAdapter(mAdapter);
        list = MainActivity.everList;
        for(String title : MainActivity.everList){
            mAdapter.add(title);
        }
    }

    @OnClick({
            R.id.share,
            R.id.back
    })
    void onClick(View v){
        switch (v.getId()){
            case R.id.share:
                StringBuilder builder = new StringBuilder();
                builder.append("今日ぶらぶらした場所は");
                for(String s : list){
                    builder.append(s+"\n");
                }
                builder.append("です！\n#JPHacks");
                final Intent i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, builder.toString());
                startActivity(i);
                break;
            case R.id.back:
                final Intent top = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(top);
                break;
        }
    }


    private class ResultAdapter extends ArrayAdapter<String> {

        private LayoutInflater inflater;

        public ResultAdapter(Context context, int resource) {
            super(context, resource);
            inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(null == convertView) convertView = inflater.inflate(R.layout.result_row, null);

            TextView title = (TextView)convertView.findViewById(R.id.result_title);
            title.setText(getItem(position));

            return convertView;
        }
    }
}
