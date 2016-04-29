package app.tea.com;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import app.tea.constants.AppConstants;
import app.tea.db.AppDb;
import app.tea.fragments.FragmentSetting;

/**
 * Created by PB5N0145 on 3/18/2016.
 */
public class Dialog_Setting extends Activity {
    AppDb appDb;
    NumberPicker numPercent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_settingpercent);
        appDb = new AppDb(this);
        numPercent = (NumberPicker) findViewById(R.id.numValue);
        TextView tv_record = (TextView) findViewById(R.id.tv_recordName);
        Button btnUpdate = (Button) findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new UpdatePercentage());
        tv_record.setText(FragmentSetting.record);
        numPercent.setMinValue(0);
        numPercent.setMaxValue(100);
        if(FragmentSetting.percentage.equals("---"))
            numPercent.setValue(0);
        else
            numPercent.setValue(Integer.parseInt(FragmentSetting.percentage));
        super.onCreate(savedInstanceState);
    }

    private class UpdatePercentage implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Log.d("TEA","PART -> "+getSubRecord());
            Log.d("TEA","Percent -> "+numPercent.getValue());
            appDb.open();
            appDb.insertGradePercent(numPercent.getValue(), getSubRecord());
            appDb.close();
            finish();
        }
    }

    private String getSubRecord(){
        String subrecord = null;
        switch (FragmentSetting.record){
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
            case "Quiz":
                subrecord = AppConstants.COL_QUIZ;
                break;
            case "Major Exam":
                subrecord = AppConstants.COL_EXAM;
                break;
            case "Prelim":
                subrecord = AppConstants.PRELIM;
                break;
            case "Midterm":
                subrecord = AppConstants.MIDTERM;
                break;
            case "Semi-Finals":
                subrecord = AppConstants.COL_SEMIFINAL;
                break;
            case "Class Standing":
                subrecord = AppConstants.COL_CLASS_STANDING;
        }
        return subrecord;
    }

}
