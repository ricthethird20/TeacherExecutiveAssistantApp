package app.tea.com;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import app.tea.db.AppDb;

/**
 * Created by PB5N0145 on 3/5/2016.
 */
public class DialogAddSection extends Activity {
    EditText et_section;
    EditText et_room;
    AppDb appDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_addsection);
        super.onCreate(savedInstanceState);
        Button btn_addSection = (Button) findViewById(R.id.btnAdd);
        et_section = (EditText) findViewById(R.id.et_section);
        et_room = (EditText) findViewById(R.id.et_room);
        btn_addSection.setOnClickListener(new AddSection());

    }
    private class AddSection implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String section = et_section.getText().toString();
            String room = et_room.getText().toString();
            if(!section.isEmpty()  && !room.isEmpty() ){
                appDB = new AppDb(DialogAddSection.this);
                appDB.open();
                appDB.insertSection(section,room);
                finish();
            }else{
                Snackbar.make(v, "Invalid section details", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }
}
