package app.tea.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.tea.db.PreferenceSetting;

/**
 * Created by Ric on 4/3/2016.
 */
public class ActivityRegister extends Activity {
    EditText etPassword;
    EditText etUser;
    EditText etConfirm;
    PreferenceSetting prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        prefs = new PreferenceSetting(PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext()));
        if(prefs.getUserName() != null){
            Intent i = new Intent(getApplicationContext()
                    ,LoginActivity.class);
            startActivity(i);
            finish();
        }
        Button btnLogin = (Button) findViewById(R.id.btn_reg);
        etPassword = (EditText) findViewById(R.id.et_password);
        etConfirm = (EditText) findViewById(R.id.et_confirm);
        etUser = (EditText) findViewById(R.id.et_user);

        btnLogin.setOnClickListener(new Register());
        super.onCreate(savedInstanceState);
    }

    private class Register implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(!etPassword.getText().toString().isEmpty() &&
                    !etUser.getText().toString().isEmpty() &&
                        !etConfirm.getText().toString().isEmpty()){
                    if(validateRegister()){
                       Intent i = new Intent(getApplicationContext()
                               ,LoginActivity.class);
                       startActivity(i);
                        finish();
                    }
            }else{
                Toast.makeText(getApplicationContext()
                        ,"Incomplete details.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateRegister(){
        String pword = etPassword.getText().toString();
        String conf = etConfirm.getText().toString();
        String userName = etUser.getText().toString();
        if(pword.equals(conf)){
            prefs.setPassword(pword);
            prefs.setUserName(userName);
            return true;
        }else{
            Toast.makeText(getApplicationContext()
                    ,"Password does not match.",Toast.LENGTH_SHORT).show();
        }
        return false;
    }


}
