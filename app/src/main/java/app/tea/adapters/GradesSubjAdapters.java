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
 * Created by Ric on 3/10/2016.
 */
public class GradesSubjAdapters extends BaseAdapter {
    public ArrayList<HashMap<String,String>> list;
    Activity activity;
    TextView tv_subjcode;
    TextView tv_subjdesc;
    public GradesSubjAdapters(Activity activity, ArrayList<HashMap<String, String>> list) {
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
        if(convertView == null){
            convertView=inflater.inflate(R.layout.adapter_subjectlist, null);
            tv_subjcode=(TextView) convertView
                    .findViewById(R.id.tv_subjCode);
            tv_subjdesc=(TextView) convertView
                    .findViewById(R.id.tv_subjDesc);
        }
        HashMap<String, String> map=list.get(position);
        tv_subjcode.setText(map.get(AppConstants.SUBJCODE));
        tv_subjdesc.setText(map.get(AppConstants.SUBJDESC));
        return convertView;
    }
}
