package app.tea.com;

import app.tea.db.PreferenceSetting;
import eneter.messaging.diagnostic.EneterTrace;
import eneter.messaging.endpoints.typedmessages.*;
import eneter.messaging.messagingsystems.messagingsystembase.*;
import eneter.messaging.messagingsystems.tcpmessagingsystem.TcpMessagingSystemFactory;
import eneter.net.system.EventHandler;
import android.os.Handler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import app.tea.constants.AppConstants;
import app.tea.db.AppDb;
import app.tea.functions.CSVReadWrite;

/**
 * Created by PB5N0145 on 3/4/2016.
 */
public class DialogExportGrade extends Activity {
    Spinner sp_subjCode;
    Spinner sp_section;
    Spinner sp_grade;
    AppDb appDb;
    ArrayList<String> gradeList;
    CSVReadWrite csvWrite;
    PreferenceSetting prefs;
    private Handler myRefresh = new Handler();
    private IDuplexTypedMessageSender<String, String> mySender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_export_grade);
        super.onCreate(savedInstanceState);
        Button btn_add = (Button) findViewById(R.id.btnAdd);

        btn_add.setOnClickListener(new AddSubject());
        sp_section = (Spinner) findViewById(R.id.sp_section);
        sp_subjCode = (Spinner) findViewById(R.id.sp_subjcode);
        sp_grade = (Spinner) findViewById(R.id.sp_grade);
        init();
        initializeTCPConnection();
    }

    private void initializeTCPConnection(){
        Thread anOpenConnectionThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    openConnection();
                }
                catch (Exception err)
                {
                    if(mySender != null)
                        Log.d("TEA","OKay");
                    else
                        Log.d("TEA","Error connecting...");
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext()
//                                    ,"Connection to server failed.",Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    finish();
                }
            }
        });
        anOpenConnectionThread.start();
    }

    private void openConnection() throws Exception
    {
        // Create sender sending MyRequest and as a response receiving MyResponse
        IDuplexTypedMessagesFactory aSenderFactory = new DuplexTypedMessagesFactory();
        mySender = aSenderFactory.createDuplexTypedMessageSender(java.lang.String.class,java.lang.String.class);

        // Subscribe to receive response messages.
        mySender.responseReceived().subscribe(myOnResponseHandler);
        IMessagingSystemFactory aMessaging = new TcpMessagingSystemFactory();
        IDuplexOutputChannel anOutputChannel
                = aMessaging.createDuplexOutputChannel("tcp://"+prefs.getIP()+":8060/");

        // Attach the output channel to the sender and be able to send
        // messages and receive responses.
        mySender.attachDuplexOutputChannel(anOutputChannel);
        
    }

    private EventHandler<TypedResponseReceivedEventArgs<String>> myOnResponseHandler
            = new EventHandler<TypedResponseReceivedEventArgs<String>>()
    {
        @Override
        public void onEvent(Object sender,
                            TypedResponseReceivedEventArgs<String> e)
        {
            onResponseReceived(sender, e);
        }
    };

    private void onResponseReceived(Object sender,
                                    final TypedResponseReceivedEventArgs<String> e)
    {
        // Display the result - returned number of characters.
        // Note: Marshal displaying to the correct UI thread.
        myRefresh.post(new Runnable() {
            @Override
            public void run() {
                Log.d("TEA", e.getResponseMessage().toString());
                //myResponseEditText.setText(Integer.toString(e.getResponseMessage().Length));
            }
        });
    }

    @Override
    protected void onDestroy() {
       mySender.detachDuplexOutputChannel();
       Log.d("TEA", "TCP Connection detached...");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mySender.detachDuplexOutputChannel();
        super.onBackPressed();
    }

    private void init(){
        csvWrite = new CSVReadWrite();
        prefs = new PreferenceSetting(PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext()));
        appDb = new AppDb(this);
        appDb.open();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, appDb.getSubjectsOnly());
        sp_subjCode.setAdapter(adapter);
        appDb.close();
        sp_subjCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String subjCode = sp_subjCode.getSelectedItem().toString();
                appDb.open();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        DialogExportGrade.this, R.layout.spinner_item,
                        appDb.getSectionsUsingSubj(subjCode));
                sp_section.setAdapter(adapter);
                appDb.close();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    private class AddSubject implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(sp_subjCode.getSelectedItem() != null &&
                    sp_section.getSelectedItem() != null && sp_grade.getSelectedItem() != null){
                String subjCode = sp_subjCode.getSelectedItem().toString();
                String subjSection = sp_section.getSelectedItem().toString();
                String gradeFor = sp_grade.getSelectedItem().toString();
                csvWrite.setSaveFileName(gradeFor+"_"+subjCode+"-"+subjSection+".csv");
                try {
                    getGrades(subjSection, subjCode, gradeFor);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(getApplicationContext(),"Please select student's subject"
                        ,Toast.LENGTH_SHORT).show();
            }
            //finish();
        }
    }

    private void getGrades(String section,String subjCode,String part) throws IOException {

        ArrayList<String> header = new ArrayList<String>();
        header.add("Name");
        header.add("Student ID");
        header.add("Quiz");
        header.add("Attendance");
        header.add("Behavior");
        header.add("Seatwork");
        header.add("Homework");
        header.add("Major Exam");
        header.add(part + " Grade");

        try{
            mySender.sendRequestMessage(subjCode+";"+part);
        } catch (Exception err) {
            Log.d("TEA.","Error! -> "+err.toString());
        }
        //csvWrite.setCSVHeader(header);

        appDb.open();
        for(String studId : appDb.getStudIdBySection(section)){
            gradeList = new ArrayList<String>();
            gradeList.add(appDb.getStudInfo(studId, AppConstants.COL_STUD_LNAME)+" "+
                    appDb.getStudInfo(studId,AppConstants.COL_STUD_FNAME)+" "+
                    appDb.getStudInfo(studId,AppConstants.COL_STUD_MNAME));
            gradeList.add(studId);


            gradeList.add(String.valueOf(appDb
                    .getQuizGradeUsingStudId(studId, part, subjCode)));

            gradeList.add(String.valueOf(appDb
                    .getClassStandingUsingStudId(studId,part,subjCode).get(0)));

            gradeList.add(String.valueOf(appDb
                    .getClassStandingUsingStudId(studId,part,subjCode).get(1)));

            gradeList.add(String.valueOf(appDb
                    .getClassStandingUsingStudId(studId, part, subjCode).get(2)));

            gradeList.add(String.valueOf(appDb
                    .getClassStandingUsingStudId(studId,part,subjCode).get(3)));

            gradeList.add(String.valueOf(appDb.getExam(studId, part, subjCode)));

            gradeList.add(String.valueOf(
                    getFinalGrade(appDb.getQuizGradeUsingStudId(studId, part, subjCode),
                            appDb.getClassStandingUsingStudId(studId, part, subjCode).get(0),
                            appDb.getClassStandingUsingStudId(studId, part, subjCode).get(1),
                            appDb.getClassStandingUsingStudId(studId, part, subjCode).get(2),
                            appDb.getClassStandingUsingStudId(studId, part, subjCode).get(3),
                            appDb.getExam(studId, part, subjCode))));
            Log.d("TEA", "---------------------------------------------");
            Log.d("TEA","Student id :"+studId+" ; Part : "+part);
            Log.d("TEA","QUIZ - > "+appDb.getQuizGradeUsingStudId(studId, part, subjCode));
            Log.d("TEA","ATTENDANCE -> "+appDb.getClassStandingUsingStudId(studId,part,subjCode).get(0));
            Log.d("TEA","BEHAVIOR -> "+appDb.getClassStandingUsingStudId(studId,part,subjCode).get(1));
            Log.d("TEA","SEATWORK -> "+appDb.getClassStandingUsingStudId(studId,part,subjCode).get(2));
            Log.d("TEA","HOMEWORK -> "+appDb.getClassStandingUsingStudId(studId,part,subjCode).get(3));
            Log.d("TEA", "EXAM -> " + appDb.getExam(studId, part, subjCode));
            Log.d("TEA", "---------------------------------------------");


                    try{
                        String row = "";
                        for (String str : gradeList) {
                            row += str+";";
                        }
                        mySender.sendRequestMessage(row);
                    } catch (Exception err) {
                        Log.d("TEA.","Error! -> "+err.toString());
                    }
                    gradeList.clear();

            //csvWrite.saveToCSVwithMultiple(gradeList);
            //gradeList.clear();
        }
        try{

            mySender.sendRequestMessage("end");
        } catch (Exception err) {
            Log.d("TEA.","Error! -> "+err.toString());
        }
        //csvWrite.closeSaveFile();
        //Log.d("TEA","Directory : "+csvWrite.getSDcardDir().getPath()+csvWrite.getSaveFileDir()+" ; Filename : "+csvWrite.getSaveFileName());

        appDb.close();
        //finish();
        //sendToBluetooth();
    }

    private double getFinalGrade(double quiz,double attendance,
                                 double behavior,double seatwork, double homework, double exam){
            double quizRate = 0;
            double classStandingRate = 0;
            double attendanceRate = 0;
            double behaviorRate = 0;
            double seatWorkRate = 0;
            double homeWorkRate = 0;
            double examRate = 0;
            double finalGrade = 0;

            quizRate = ((double)appDb
                    .getPercentage(AppConstants.COL_QUIZ) / 100) * quiz;
            examRate = ((double)appDb
                    .getPercentage(AppConstants.COL_EXAM) / 100) * exam;

            attendanceRate = ((double) appDb
                    .getPercentage(AppConstants.COL_ATTENDANCE) / 100) * attendance;


            behaviorRate = ( (double) appDb
                    .getPercentage(AppConstants.COL_BEHAVIOR) / 100) * behavior;
            seatWorkRate = ( (double) appDb
                    .getPercentage(AppConstants.COL_SEATWORK) / 100) * seatwork;
            homeWorkRate = ( (double) appDb
                    .getPercentage(AppConstants.COL_HOMEWORK) / 100) * homework;

            classStandingRate = (attendanceRate + behaviorRate + seatWorkRate + homeWorkRate)
                    * ( (double) appDb.getPercentage(AppConstants.COL_CLASS_STANDING) / 100);

            finalGrade = quizRate + examRate + classStandingRate;
        Log.d("TEA", "Quiz rate -> " + quizRate);
        Log.d("TEA", "Exam rate -> " + examRate);
        Log.d("TEA", "Attendance rate -> " + attendanceRate);
        Log.d("TEA", "behavior rate -> " + behaviorRate);
        Log.d("TEA", "seatwork rate -> " + seatWorkRate);
        Log.d("TEA","homework rate -> "+homeWorkRate);
        Log.d("TEA", "class standing rate -> " + classStandingRate);

        return finalGrade;
    }

    private void sendToBluetooth(){
        String path = csvWrite.getSDcardDir().getPath()+csvWrite
                .getSaveFileDir()+"/"+csvWrite.getSaveFileName();
        File file = new File(path);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(intent);
    }

}
