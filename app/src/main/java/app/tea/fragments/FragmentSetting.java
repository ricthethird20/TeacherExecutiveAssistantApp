package app.tea.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.HashMap;

import app.tea.adapters.GradesRecordAdapter;
import app.tea.adapters.SettingListAdapter;
import app.tea.com.Dialog_Setting;
import app.tea.com.R;
import app.tea.db.AppDb;

/**
 * Created by Ric on 3/17/2016.
 */


public class FragmentSetting  extends Fragment{
    ListView lv_percentage;
    public static String record;
    public static String percentage;
    private static ArrayList<HashMap<String,String>> percentageList;
    AppDb appDb;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting,container,false);
        appDb = new AppDb(getActivity());
        lv_percentage = (ListView) rootView.findViewById(R.id.lv_percentages);
        percentageList = new ArrayList<HashMap<String,String>>();

        lv_percentage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map =
                        percentageList.get(position);
                record = map.get("part");
                percentage = map.get("perc");

                Intent i = new Intent(getActivity(), Dialog_Setting.class);
                startActivity(i);

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

    @Override
    public void onStop() {
//        appDb.open();
//        appDb.insertGradePercent(numPrelim.getValue(),numMidterm.getValue(),numSemis.getValue(),numClassStand.getValue(),
//                numQuiz.getValue(),numExam.getValue(),numAttendance.getValue(),numBehavior.getValue(),numSeatWork.getValue(),
//                numHomeWork.getValue());
//        appDb.close();
       super.onStop();
    }

    private void populateList(){
        String records[] = {"Prelim","Midterm","Semi-Finals",
                "Class Standing","Quiz","Major Exam","Attendance","Behavior","Seatwork","Homework"};
        ArrayList<Integer> arr_perc = new ArrayList<Integer>();
        appDb.open();
        arr_perc = appDb.getPercentage();
        appDb.close();
        percentageList = new ArrayList<HashMap<String, String>>();
        for(int i=0;i<records.length;i++){
            HashMap<String,String> hashMap = new HashMap<String,String>();
            hashMap.put("part", records[i]);
            if(arr_perc.get(i) != null)
                hashMap.put("perc",arr_perc.get(i).toString());
            else
                hashMap.put("perc","---");
            percentageList.add(hashMap);
        }
    }

    private void setListAdapter(){
        SettingListAdapter settingListAdapter =
                new SettingListAdapter(getActivity(),percentageList);
        lv_percentage.setAdapter(settingListAdapter);
    }


}
