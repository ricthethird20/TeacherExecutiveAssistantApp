package app.tea.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import app.tea.com.AppCompatPreferenceActivity;
import app.tea.com.R;
import app.tea.constants.AppConstants;

/**
 * Created by Ric on 3/10/2016.
 */
public class GradesStudentAdapter extends BaseAdapter {
    public ArrayList<HashMap<String,String>> list;
    Activity activity;
    TextView tv_name;
    TextView tv_id;
    TextView tv_grade;
    public GradesStudentAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
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
            convertView=inflater.inflate(R.layout.adapter_grades_student, null);
            tv_name =(TextView) convertView
                    .findViewById(R.id.tv_name);
            tv_id =(TextView) convertView
                    .findViewById(R.id.tv_id);
            tv_grade =(TextView) convertView
                    .findViewById(R.id.tv_grade);

        }
        HashMap<String, String> map=list.get(position);
        tv_name.setText(map.get(AppConstants.STUDNAME));
        tv_id.setText(map.get(AppConstants.STUDID));
        tv_grade.setText(map.get("GRADE"));
        return convertView;
    }
}
