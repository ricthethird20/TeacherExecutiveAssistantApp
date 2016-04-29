package app.tea.com;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import app.tea.db.AppDb;

/**
 * Created by PB5N0145 on 3/4/2016.
 */
public class DialogAddStudSubject extends Activity {
    Spinner sp_subjCode;
    Spinner sp_section;
    AppDb appDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_addstudsubject);
        super.onCreate(savedInstanceState);
        Button btn_add = (Button) findViewById(R.id.btnAdd);

        btn_add.setOnClickListener(new AddSubject());
        sp_section = (Spinner) findViewById(R.id.sp_section);
        sp_subjCode = (Spinner) findViewById(R.id.sp_subjcode);
        init();
    }

    private void init(){
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
                        DialogAddStudSubject.this, R.layout.spinner_item,
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
                    sp_section.getSelectedItem() != null){
                String subjCode = sp_subjCode.getSelectedItem().toString();
                String subjSection = sp_section.getSelectedItem().toString();

                appDb.open();
                if (!appDb.checkStudSubject(StudentDetails.studId,subjCode))
                    appDb.insertStudentSubjects(StudentDetails.studId,subjCode,subjSection);
                else
                    Toast.makeText(getApplicationContext(),"Subject already exist."
                            ,Toast.LENGTH_SHORT).show();
                appDb.close();
            }else{
                Toast.makeText(getApplicationContext(),"Please select student's subject"
                        ,Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

}
