package app.tea.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import app.tea.com.R;

/**
 * Created by Ric on 3/10/2016.
 */
public class SettingItemsAdapter extends BaseAdapter {
    ArrayList<HashMap<String,String>> list;
    ArrayList<HashMap<String,Integer>>mIcons;
    ArrayList<String> subLabel;
    Activity activity;

    public SettingItemsAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
        mIcons = new ArrayList<HashMap<String, Integer>>();
        int[] icons = {R.drawable.settings,R.drawable.server};
        for(int i = 0;i<2;i++){
            HashMap<String,Integer> hashMap = new HashMap<>();
            hashMap.put("ICONS",icons[i]);
            mIcons.add(hashMap);
        }
        subLabel = new ArrayList<>();
        subLabel.add("Edit grade percentages preferences");
        subLabel.add("Modify server setup details");
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

            convertView=inflater.inflate(R.layout.adapter_studentlist, null);
        TextView tv_header=(TextView) convertView
                    .findViewById(R.id.tv_studName);
        TextView tv_subLabel = (TextView) convertView
                    .findViewById(R.id.tv_studId);
        ImageView iv_icon = (ImageView) convertView
                    .findViewById(R.id.imageView2);

        HashMap<String,Integer> imageList = mIcons.get(position);
        HashMap<String, String> map=list.get(position);
        tv_header.setText(map.get("SETTING_LABEL"));
        tv_subLabel.setText(subLabel.get(position));
        iv_icon.setImageResource(imageList.get("ICONS"));
        return convertView;
    }
}
