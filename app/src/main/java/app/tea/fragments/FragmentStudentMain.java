package app.tea.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import app.tea.adapters.StudentListAdapter;
import app.tea.com.DialogMenu;
import app.tea.com.R;
import app.tea.com.StudentDetails;
import app.tea.constants.AppConstants;
import app.tea.db.AppDb;

/**
 * Created by PB5N0145 on 3/4/2016.
 */
public class FragmentStudentMain extends Fragment implements DialogMenu.OnSelectActionListener {
    private static ArrayList<HashMap<String,String>> mStudentList;
    View containerView;
    ListView lv_student;
    AppDb appDb;
    String studId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        containerView = inflater.inflate(R.layout
                .fragment_studentmain,container,false);
        lv_student = (ListView) containerView.findViewById(R.id.lv_students);
        lv_student.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map =
                        mStudentList.get(position);

                studId = map.get(AppConstants.STUDID);
                Intent i = new Intent(getActivity().getApplicationContext(), DialogMenu.class);
                i.putExtra(AppConstants.EXTRA_CLASS, "Student");
                i.putExtra(AppConstants.EXTRA_ID, studId);
                getActivity().startActivity(i);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) containerView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                fragment = new StudentDetails();
                getActivity().setTitle("Add student");
                if (fragment != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.rl_contentMain, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        DialogMenu.setOnSelectActionListener(this);
        return containerView;
    }

    @Override
    public void onResume() {
        if(mStudentList != null)
            mStudentList.clear();
        populateList();
        setListAdapter();
        super.onResume();
    }

    private void populateList(){
        appDb = new AppDb(getActivity().getApplicationContext());
        appDb.open();
        mStudentList = appDb.getStudents();
        appDb.close();
    }

    private void setListAdapter(){
        lv_student.invalidateViews();
        if (mStudentList.size()>0){

            StudentListAdapter studentListAdapter =
                    new StudentListAdapter(getActivity(),mStudentList);
            lv_student.setAdapter(studentListAdapter);
        }
    }

    @Override
    public void setFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.STUDENT_KEY,studId);
        fragment.setArguments(bundle);
        getActivity().setTitle("Add student");
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.rl_contentMain, fragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
    }
}
