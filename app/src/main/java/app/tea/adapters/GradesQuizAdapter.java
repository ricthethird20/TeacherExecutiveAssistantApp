package app.tea.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import app.tea.com.R;
import app.tea.constants.AppConstants;

/**
 * Created by Ric on 3/20/2016.
 */
public class GradesQuizAdapter extends BaseAdapter {
    public ArrayList<HashMap<String,String>> list;
    Activity activity;
    TextView tv_quiz;
    TextView tv_quizNo;
    public GradesQuizAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();
        convertView = null;
        if(convertView == null){
            convertView=inflater.inflate(R.layout.adapter_dual_item, null);
            tv_quiz=(TextView) convertView
                    .findViewById(R.id.tv_record);
            tv_quizNo=(TextView) convertView
                    .findViewById(R.id.tv_quizseatNo);
        }
        HashMap<String, String> map=list.get(position);
        tv_quiz.setText(map.get(AppConstants.HASHQUIZ));
        tv_quizNo.setText(map.get(AppConstants.HASHQUIZNO));
        return convertView;
    }
}
