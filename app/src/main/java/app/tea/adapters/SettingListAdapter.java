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
 * Created by PB5N0145 on 3/18/2016.
 */
public class SettingListAdapter extends BaseAdapter {
    public ArrayList<HashMap<String,String>> list;
    Activity activity;
    TextView tv_record;
    TextView tv_percent;

    public SettingListAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
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
            convertView=inflater.inflate(R.layout.adapter_setting, null);
            tv_record =(TextView) convertView
                    .findViewById(R.id.tv_record);
            tv_percent =(TextView) convertView
                    .findViewById(R.id.tv_percent);

        }
        HashMap<String, String> map=list.get(position);
        tv_record.setText(map.get("part"));
        tv_percent.setText(map.get("perc"));
        return convertView;
    }
}
