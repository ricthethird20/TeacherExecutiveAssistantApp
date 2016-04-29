package app.tea.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import app.tea.adapters.GradesQuizAdapter;
import app.tea.adapters.GradesRecordAdapter;
import app.tea.com.DialogMenu;
import app.tea.com.R;
import app.tea.constants.AppConstants;
import app.tea.db.AppDb;

/**
 * Created by Ricardo on 3/10/2016.
 */
public class FragmentGradesMain extends Fragment {
    ListView lv_quiz;
    TextView tv_name;
    LinearLayout ll_quizseat;
    public static String record;
    AppDb appDb;
    private static ArrayList<HashMap<String,String>> quizList;
    private static String stud_id;
    private static String stud_name;
    NumberPicker np_grade;
    NumberPicker np_dec;
    NumberPicker np_quizseat_no;
    Button btn_next;
    private static int mIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView  = inflater.inflate(
        R.layout.fragment_gradesclassorquiz,container,false);
        appDb = new AppDb(getActivity());
        np_grade = (NumberPicker) rootView.findViewById(R.id.numberPicker2);
        np_dec = (NumberPicker) rootView.findViewById(R.id.numberPicker3);
        np_quizseat_no = (NumberPicker) rootView.findViewById(R.id.numQuizSeatNo);
        btn_next = (Button) rootView.findViewById(R.id.button2);
        btn_next.setOnClickListener(new NextStudent());
        mIndex = FragmentGradesStudents.current_index;
        stud_id = FragmentGradesStudents.stud_id;
        stud_name =  FragmentGradesStudents.stud_name;
        TextView tv_subjCode = (TextView) rootView.findViewById(R.id.tv_subjcode);
        TextView tv_record = (TextView) rootView.findViewById(R.id.tv_record);
        ll_quizseat = (LinearLayout) rootView.findViewById(R.id.ll_quiz);
        lv_quiz = (ListView) rootView.findViewById(R.id.lv_quiz);
        tv_name = (TextView) rootView.findViewById(R.id.tv_name);
        tv_subjCode.setText(FragmentGradesSubjects.subjCode);
        tv_record.setText(FragmentGradesSubRecord.subRecord);
        tv_name.setText(stud_name);
        np_quizseat_no.setMaxValue(10);
        np_quizseat_no.setMinValue(1);
        np_dec.setMaxValue(99);
        np_dec.setMinValue(0);
        np_grade.setMaxValue(100);
        np_grade.setMinValue(0);
        getStudentGrade();
        lv_quiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map =
                        quizList.get(position);
                String quiz = map.get(AppConstants.HASHQUIZ);
                String rowid = map.get(AppConstants.HASHQUIZID);
                Intent i = new Intent(getActivity().getApplicationContext(), DialogMenu.class);
                i.putExtra(AppConstants.EXTRA_CLASS, "Quiz");
                i.putExtra(AppConstants.EXTRA_ID, rowid);
                startActivity(i);
            }
        });
        Button btn_save = (Button) rootView.findViewById(R.id.btnSave);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((getSubRecord().equals(AppConstants.COL_QUIZ))){
                    if(np_grade.getValue() > 0){
                        String grade = np_grade.getValue()+"."+ np_dec.getValue();
                        String quizSeatNo = np_quizseat_no.getValue()+"";

                        if(getRecord() != ""){
                            appDb.open();
                            appDb.insertStudentGrades(stud_id, FragmentGradesSubjects.subjCode,
                                    getSubRecord(), getRecord(), getTable(), grade, quizSeatNo);
                            Log.d("TEA", "ID : " + stud_id);
                            Log.d("TEA","SUBJ : "+FragmentGradesSubjects.subjCode);
                            Log.d("TEA","TABLE : "+getTable());
                            Log.d("TEA","PART : "+ getRecord());
                            Log.d("TEA","SUBRECORD : "+getSubRecord());
                            Log.d("TEA","Quiz/Seat No : "+quizSeatNo);
                            Log.d("TEA","GRADE : "+grade);
                            appDb.close();

                            populateList();
                        }
                    }
                }
            }
        });
        return rootView;
    }

    private void getStudentGrade(){
        appDb.open();
        String g = appDb.getStudentGrades(stud_id,FragmentGradesSubjects.subjCode
                , getRecord(),getSubRecord(),getTable());
        Log.d("TEA", "ID : " + stud_id);
        Log.d("TEA", "SUBJ : " + FragmentGradesSubjects.subjCode);
        Log.d("TEA", "COL : " + getRecord());

        if(g != null){
            if(g != ""){
                Log.d("TEA","this -> "+g);
                String s[] = g.split("\\.");
                int w = Integer.parseInt(s[0]);
                int d = Integer.parseInt(s[1]);
                np_grade.setValue(w);
                np_dec.setValue(d);
            }
            else{
                np_grade.setValue(0);
                np_dec.setValue(0);
            }
        }else{
            np_grade.setValue(0);
            np_dec.setValue(0);
        }

        appDb.close();
    }

    private class NextStudent implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            mIndex++;

            if(mIndex < FragmentGradesStudents.mStudentList.size()){
                HashMap<String, String> map =
                        FragmentGradesStudents.mStudentList.get(mIndex);
                String grade = np_grade.getValue()+"."+ np_dec.getValue();
                if(getRecord() != ""){
                    if(!(getSubRecord().equals(AppConstants.COL_QUIZ))){
                        appDb.open();
                        appDb.insertStudentGrades(stud_id, FragmentGradesSubjects.subjCode,
                                getSubRecord(), getRecord(), getTable(), grade,null);
                        appDb.close();
                    }
                }
                stud_id = map.get(AppConstants.STUDID);
                stud_name = map.get(AppConstants.STUDNAME);
                getStudentGrade();
                getQuizList();
                tv_name.setText(stud_name);

            }
        }
    }

    private String getTable(){
        String table = null;
        switch (FragmentGradesSubRecord.subRecord){
            case "Attendance":
            case "Behavior":
            case "Seatwork":
            case "Homework":
                table = AppConstants.CLASS_STANDING_TABLE;
                break;
            case "Quizzes":
                table = AppConstants.QUIZ_TABLE;
                break;
            case "Major Exam":
                table = AppConstants.EXAM_TABLE;
                break;
        }
        return table;
    }

    private String getSubRecord(){
        String subrecord = null;
        switch (FragmentGradesSubRecord.subRecord){
            case "Attendance":
                subrecord = AppConstants.COL_ATTENDANCE;
                break;
            case "Behavior":
                subrecord = AppConstants.COL_BEHAVIOR;
                break;
            case "Seatwork":
                subrecord = AppConstants.COL_SEATWORK;
                break;
            case "Homework":
                subrecord = AppConstants.COL_HOMEWORK;
                break;
            case "Quizzes":
                subrecord = AppConstants.COL_QUIZ;
                break;
            case "Major Exam":
                subrecord = AppConstants.COL_EXAM;
                break;
        }
        return subrecord;
    }

    private String getRecord(){
        String part = "";
        switch (FragmentGradesRecords.record){
            case "Prelim":
                part = AppConstants.PRELIM;
                break;
            case "Midterm":
                part = AppConstants.MIDTERM;
                break;
            case "Semi-Finals":
                part = AppConstants.SEMIFINAL;
                break;
        }
        return part;
    }

    @Override
    public void onStop() {
        if(!(getSubRecord().equals(AppConstants.COL_QUIZ))){
            if(np_grade.getValue() > 0){
                String grade = np_grade.getValue()+"."+ np_dec.getValue();
                if(getRecord() != ""){
                    appDb.open();
                    appDb.insertStudentGrades(stud_id, FragmentGradesSubjects.subjCode,
                            getSubRecord(), getRecord(), getTable(), grade,null);
                    Log.d("TEA", "ID : " + stud_id);
                    Log.d("TEA","SUBJ : "+FragmentGradesSubjects.subjCode);
                    Log.d("TEA","TABLE : "+getTable());
                    Log.d("TEA","PART : "+ getRecord());
                    Log.d("TEA","SUBRECORD : "+getSubRecord());
                    Log.d("TEA","GRADE : "+grade);
                    appDb.close();
                }
            }
        }
        super.onStop();
    }

    private void getQuizList(){
        if(getSubRecord().equals(AppConstants.COL_QUIZ)){
            ll_quizseat.setVisibility(View.VISIBLE);
            populateList();
        }else
            ll_quizseat.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {

        getQuizList();
        super.onResume();
    }

    private void populateList(){
        if(quizList != null)
            quizList.clear();
        appDb.open();
        quizList = appDb.getQuiz(stud_id, FragmentGradesSubjects.subjCode,getRecord());
        appDb.close();
        if(quizList != null && quizList.size() > 0)
            setListAdapter();
        else
            lv_quiz.invalidateViews();

    }
    private void setListAdapter(){
        lv_quiz.invalidateViews();
        GradesQuizAdapter studentListAdapter =
                new GradesQuizAdapter(getActivity(),quizList);
        lv_quiz.setAdapter(studentListAdapter);
    }
}
