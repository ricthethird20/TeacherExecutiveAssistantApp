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
import app.tea.adapters.GradesSubRecordAdapter;
import app.tea.com.R;

/**
 * Created by PB5N0145 on 3/17/2016.
 */
public class FragmentGradesSubRecord extends Fragment {
    ListView lv_subRecords;
    public static String subRecord;
    private static ArrayList<HashMap<String,String>> subRecordList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_record_select,container,false);
        lv_subRecords = (ListView) rootView
                .findViewById(R.id.lv_subrecord);
        TextView tv_subj = (TextView) rootView.findViewById(R.id.tv_subj);
        TextView tv_rec = (TextView) rootView.findViewById(R.id.tv_rec);
        tv_subj.setText(FragmentGradesSubjects.subjCode);
        tv_rec.setText(FragmentGradesRecords.record);
        lv_subRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map =
                        subRecordList.get(position);
                subRecord = map.get("SUB_REC");
                Fragment fragment = null;
                fragment = new FragmentGradesStudents();
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
        String records[] = {"Attendance","Behavior","Seatwork","Homework","Quizzes","Major Exam"};
        subRecordList = new ArrayList<HashMap<String, String>>();
        for(int i=0;i<records.length;i++){
            HashMap<String,String> hashMap = new HashMap<String,String>();
            hashMap.put("SUB_REC",records[i]);
            subRecordList.add(hashMap);
        }
    }
    private void setListAdapter(){
        GradesSubRecordAdapter gradesSubRecordAdapter =
                new GradesSubRecordAdapter(getActivity(),subRecordList);
        lv_subRecords.setAdapter(gradesSubRecordAdapter);
    }

}
