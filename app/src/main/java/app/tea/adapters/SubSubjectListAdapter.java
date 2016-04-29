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
 * Created by PB5N0145 on 3/5/2016.
 */
public class SubSubjectListAdapter extends BaseAdapter{
    public ArrayList<HashMap<String,String>> list;
    Activity activity;
    TextView tv_subjcode;
    TextView tv_section;
    public SubSubjectListAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
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
            convertView=inflater.inflate(R.layout.adapter_studsubj, null);
            tv_subjcode=(TextView) convertView
                    .findViewById(R.id.tv_subjCode);
            tv_section=(TextView) convertView
                    .findViewById(R.id.tv_section);
        }
        HashMap<String, String> map=list.get(position);
        tv_subjcode.setText(map.get(AppConstants.SUBJCODE));
        tv_section.setText(map.get(AppConstants.SUBJDESC));
        return convertView;
    }
}
