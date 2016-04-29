package app.tea.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import app.tea.adapters.GradesRecordAdapter;
import app.tea.adapters.GradesSubjAdapters;
import app.tea.com.R;
import app.tea.com.StudentDetails;

/**
 * Created by Ric on 3/10/2016.
 */
public class FragmentGradesRecords extends Fragment {
    ListView lv_records;
    public static String record;
    private static ArrayList<HashMap<String,String>> recordList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView  = inflater.inflate(
                R.layout.fragment_grades2,container,false);
        lv_records = (ListView) rootView
                .findViewById(R.id.lv_records);
        TextView tv_header = (TextView) rootView.findViewById(R.id.tv_header);
        tv_header.setText(FragmentGradesSubjects.subjCode);
        lv_records.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map =
                        recordList.get(position);
                record = map.get("REC");
                Fragment fragment = null;
                fragment = new FragmentGradesSubRecord();
                if (fragment != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.rl_contentMain, fragment)
                            .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left)
                            .addToBackStack(null)
                            .commit();
                }

            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        populateList();
        setListAdapter();
        super.onResume();
    }

    private void populateList(){
        String records[] = {"Prelim","Midterm","Semi-Finals"};
        recordList = new ArrayList<HashMap<String, String>>();
        for(int i=0;i<records.length;i++){
            HashMap<String,String> hashMap = new HashMap<String,String>();
            hashMap.put("REC",records[i]);
            recordList.add(hashMap);
        }
    }

    private void setListAdapter(){
        GradesRecordAdapter studentListAdapter =
                new GradesRecordAdapter(getActivity(),recordList);
        lv_records.setAdapter(studentListAdapter);
    }
}
