package app.tea.com;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import app.tea.constants.AppConstants;
import app.tea.db.AppDb;

/**
 * Created by PB5N0145 on 3/4/2016.
 */
public class DialogMenu extends Activity {
    String module;
    String key;
    AppDb appDb;

    private static OnSelectActionListener onSelectActionListener;
    public interface OnSelectActionListener {
        void setFragment(Fragment fragment);
    }
    public static void setOnSelectActionListener(OnSelectActionListener listener){
        onSelectActionListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_selectstudent);
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            module = extras.getString(AppConstants.EXTRA_CLASS);
            key = extras.getString(AppConstants.EXTRA_ID);
        }
        appDb = new AppDb(this);
        Button btn_view = (Button) findViewById(R.id.btn_view);
        Button btn_delete = (Button) findViewById(R.id.btn_delete);
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_view.setOnClickListener(new ViewDetails());
        btn_delete.setOnClickListener(new DeleteRecord());
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(module.equals("Section") || module.equals("Quiz"))
            btn_view.setVisibility(View.GONE);
        else
            btn_view.setVisibility(View.VISIBLE);
    }

    private class ViewDetails implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            gotoDetails();
        }
    }

    private void gotoDetails(){
        if(key != null){
            Fragment fragment = null;
            switch (module){
                case "Student":
                    fragment = new StudentDetails();
                    break;
                case "Subject":
                    fragment = new SubjectDetails();
                    break;
                case "Section":
                    break;
            }
            onSelectActionListener.setFragment(fragment);
        }
        finish();
    }

    private class DeleteRecord implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            deleteRecord();
        }
    }

    private void deleteRecord(){
        if(key != null){
            switch (module){
                case "Student":
                    appDb.open();
                    appDb.deleteRecord(AppConstants
                            .STUD_TABLE, AppConstants.COL_STUD_ID, key);
                    appDb.deleteRecord(AppConstants
                            .STUDSUBJ_TABLE,AppConstants.COL_STUD_ID,key);
                    appDb.close();
                    break;
                case "Subject":
                    appDb.open();
                    appDb.deleteRecord(AppConstants
                        .SUBJ_TABLE,AppConstants.COL_SUBJ_CODE,key);
                    appDb.close();
                    break;
                case "Section":
                    appDb.open();
                    appDb.deleteRecord(AppConstants
                            .SECT_TABLE,AppConstants.COL_SECT_NAME,key);
                    appDb.close();
                    break;
                case "Quiz":
                    appDb.open();
                    appDb.deleteRecord(AppConstants
                            .QUIZ_TABLE,"rowid",key);
                    appDb.close();
                    break;
            }
        }
        finish();
    }
}
