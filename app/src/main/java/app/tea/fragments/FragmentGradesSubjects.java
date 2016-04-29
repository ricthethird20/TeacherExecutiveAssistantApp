package app.tea.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import app.tea.adapters.GradesSubjAdapters;
import app.tea.com.R;
import app.tea.constants.AppConstants;
import app.tea.db.AppDb;

/**
 * Created by Ric on 3/10/2016.
 */
public class FragmentGradesSubjects extends Fragment {
    ListView lv_subjects;
    public static String subjCode;
    private AppDb appDb;
    private static ArrayList<HashMap<String,String>> subjList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView  = inflater.inflate(
                R.layout.fragment_grades1,container,false);
        lv_subjects = (ListView) rootView
                .findViewById(R.id.lv_subjects);
        lv_subjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map =
                        subjList.get(position);
                subjCode = map.get(AppConstants.SUBJCODE);
                Fragment fragment;
                fragment = new FragmentGradesRecords();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.rl_contentMain, fragment)
                        .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left)
                        .addToBackStack(null)
                        .commit();
                //String studID = map.get(AppConstants.STUDID);

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
        subjList = new ArrayList<HashMap<String, String>>();
        appDb = new AppDb(getActivity().getApplicationContext());
        appDb.open();
        subjList = appDb.getSubjects();
        appDb.close();
    }
    private void setListAdapter(){
        GradesSubjAdapters studentListAdapter =
                new GradesSubjAdapters(getActivity(),subjList);
        lv_subjects.setAdapter(studentListAdapter);
    }


}
