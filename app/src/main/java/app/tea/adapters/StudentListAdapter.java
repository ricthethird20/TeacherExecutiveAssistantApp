package app.tea.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import app.tea.com.R;
import app.tea.constants.AppConstants;

/**
 * Created by PB5N0145 on 3/4/2016.
 */
public class StudentListAdapter extends BaseAdapter{
    public ArrayList<HashMap<String,String>> list;
    Activity activity;
    TextView tv_studentName;
    TextView tv_studentId;
    public StudentListAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
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
            convertView=inflater.inflate(R.layout.adapter_studentlist, null);
            tv_studentName=(TextView) convertView
                    .findViewById(R.id.tv_studName);
            tv_studentId=(TextView) convertView
                   .findViewById(R.id.tv_studId);
        }
        HashMap<String, String> map=list.get(position);
        tv_studentName.setText(map.get(AppConstants.STUDNAME));
        tv_studentId.setText(map.get(AppConstants.STUDID));
        return convertView;
    }
}
