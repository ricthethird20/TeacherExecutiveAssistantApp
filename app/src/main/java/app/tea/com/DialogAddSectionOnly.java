package app.tea.com;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import app.tea.constants.AppConstants;
import app.tea.db.AppDb;

/**
 * Created by PB5N0145 on 3/5/2016.
 */
public class DialogAddSectionOnly extends Activity{
    Spinner sp_section;
    Button btn_add;
    AppDb appDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_addsection);
        sp_section = (Spinner) findViewById(R.id.sp_section);
        btn_add = (Button) findViewById(R.id.btnAdd);
        btn_add.setOnClickListener(new AddSection());
        super.onCreate(savedInstanceState);
        appDb = new AppDb(this);
        appDb.open();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, appDb.spinnerSectionList());
        sp_section.setAdapter(adapter);
    }
    private class AddSection implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(sp_section.getSelectedItem() != null){
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put(AppConstants.SECTNAME, sp_section.getSelectedItem().toString());
                if(!SubjectDetails.arr_sect.contains(hashMap))
                    SubjectDetails.arr_sect.add(hashMap);
                Intent i = new Intent(SubjectDetails.intentAddSection);
                sendBroadcast(i);
            }else{
                Toast.makeText(getApplicationContext(),"No section selected."
                        ,Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
    @Override
    protected void onDestroy() {
        appDb.close();
        super.onDestroy();
    }
}
