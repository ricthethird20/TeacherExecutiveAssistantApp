package app.tea.com;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import app.tea.adapters.StudentListAdapter;
import app.tea.adapters.SubSubjectListAdapter;
import app.tea.constants.AppConstants;
import app.tea.db.AppDb;

/**
 * Created by PB5N0145 on 3/4/2016.
 */
public class StudentDetails extends Fragment {
    AppDb appDb;
    View containerView;
    EditText et_studId;
    EditText et_fname;
    EditText et_mname;
    EditText et_lname;
    NumberPicker np_yr;
    ListView lv_subjects;
    Spinner sp_section;
    public static String studId;
    public static ArrayList<HashMap<String,String>> subjList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            ArrayList<String> studInfo = new ArrayList<String>();
            studId = bundle.getString(
                    AppConstants.STUDENT_KEY, "");
            appDb.open();
            studInfo = appDb.getStudInfo(studId);
            appDb.close();
            if(studInfo != null){
                et_studId.setText(studId);
                et_lname.setText(studInfo.get(0));
                et_fname.setText(studInfo.get(1));
                et_mname.setText(studInfo.get(2));
                np_yr.setValue(Integer.parseInt(studInfo.get(3)));
                sp_section.setPrompt(studInfo.get(4));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        containerView = inflater.inflate(R.layout
                .activity_addstudent,container,false);
        et_studId = (EditText) containerView.findViewById(R.id.et_studNo);
        et_fname = (EditText) containerView.findViewById(R.id.et_fname);
        et_mname = (EditText) containerView.findViewById(R.id.et_mname);
        et_lname = (EditText) containerView.findViewById(R.id.et_lname);
        np_yr = (NumberPicker) containerView.findViewById(R.id.np_year);
        sp_section = (Spinner) containerView.findViewById(R.id.sp_section);
        lv_subjects = (ListView) containerView.findViewById(R.id.lv_subjects);
        subjList = new ArrayList<HashMap<String, String>>();
        init();
        FloatingActionButton fab = (FloatingActionButton) containerView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studId = et_studId.getText().toString();
                Intent i = new Intent(getActivity(),DialogAddStudSubject.class);
                getActivity().startActivity(i);
            }
        });
        return containerView;
    }

    private void init(){
        appDb  = new AppDb(getActivity().getApplicationContext());
        appDb.open();
        np_yr.setMaxValue(5);
        np_yr.setMinValue(1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity().getApplicationContext(), R.layout.spinner_item, appDb.spinnerSectionList());
        sp_section.setAdapter(adapter);
        appDb.close();
    }
    @Override
    public void onStop() {
        saveData();
        super.onStop();
    }

    private void saveData(){

        if(!et_studId.getText().toString().isEmpty() &&
                sp_section.getSelectedItem() != null){

            String lname = et_lname.getText().toString();
            String fname = et_fname.getText().toString();
            String mname = et_mname.getText().toString();
            studId = et_studId.getText().toString();
            String section = sp_section.getSelectedItem().toString();
            int yr = np_yr.getValue();
            appDb.open();
            appDb.insertStudent(studId, lname, fname, mname, yr, section);
            appDb.close();
        }
    }

    @Override
    public void onResume() {
        if(!et_studId.getText()
                .toString().isEmpty()){
            populateSubjList();
            setListAdapter();
        }
        super.onResume();
    }
    private void populateSubjList(){
        appDb = new AppDb(getActivity().getApplicationContext());
        appDb.open();
        Log.d("TEA","Student ID "+studId);
        subjList = appDb.getStudSubjects(studId);
        appDb.close();
    }
    private void setListAdapter(){
        if (subjList.size()>0){
            SubSubjectListAdapter subSubjectListAdapter =
                    new SubSubjectListAdapter(getActivity(),subjList);
            lv_subjects.setAdapter(subSubjectListAdapter);
        }
    }

}
