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

import java.util.ArrayList;
import java.util.HashMap;

import app.tea.adapters.GradesStudentAdapter;
import app.tea.com.R;
import app.tea.constants.AppConstants;
import app.tea.db.AppDb;

/**
 * Created by PB5N0145 on 3/4/2016.
 */
public class FragmentGradesStudents extends Fragment {
    public static ArrayList<HashMap<String,String>> mStudentList;
    public static String stud_id;
    public static String stud_name;
    public static int current_index = 0;
    TextView tv_header;
    TextView tv_record;
    View containerView;
    ListView lv_student;
    AppDb appDb;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        containerView = inflater.inflate(R.layout
                .fragments_grades_studlist,container,false);
        tv_header = (TextView) containerView.findViewById(R.id.tv_subjcode);
        tv_record = (TextView) containerView.findViewById(R.id.tv_record);
        getActivity().setTitle(FragmentGradesRecords.record);
        lv_student = (ListView) containerView.findViewById(R.id.lv_students);
        lv_student.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map =
                        mStudentList.get(position);
                current_index = position;
                stud_id = map.get(AppConstants.STUDID);
                stud_name = map.get(AppConstants.STUDNAME);
                Fragment fragment = null;
                fragment = new FragmentGradesMain();
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

        return containerView;
    }



    private void populateList(){
        appDb = new AppDb(getActivity());
        appDb.open();
        mStudentList = appDb.getStudentsUsingSubjects(
                FragmentGradesSubjects.subjCode);
        appDb.close();

    }

    @Override
    public void onResume() {
        tv_header.setText(FragmentGradesSubjects.subjCode);
        tv_record.setText(FragmentGradesSubRecord.subRecord);
        populateList();
        setListAdapter();
        super.onResume();
    }

    private void setListAdapter(){
        GradesStudentAdapter studentListAdapter =
                new GradesStudentAdapter(getActivity(),mStudentList);
        lv_student.setAdapter(studentListAdapter);
    }

}
