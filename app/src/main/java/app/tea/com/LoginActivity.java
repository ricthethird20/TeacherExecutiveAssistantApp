package app.tea.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import app.tea.db.PreferenceSetting;

/**
 * Created by Ric on 4/3/2016.
 */
public class LoginActivity extends Activity {
    EditText etPassword;
    TextView tvWelcome;
    CheckBox chkRemember;
    PreferenceSetting prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        prefs = new PreferenceSetting(PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext()));
        if(prefs.isSkipLogin()){

            Intent i = new Intent(getApplicationContext()
                    ,MainActivity.class);
            startActivity(i);
            finish();
        }

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        etPassword = (EditText) findViewById(R.id.et_password);
        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        chkRemember = (CheckBox) findViewById(R.id.chk_remember);
        tvWelcome.setText("Welcome "+prefs.getUserName()+"!");
        btnLogin.setOnClickListener(new LogIn());
        super.onCreate(savedInstanceState);
    }

    private class LogIn implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(!etPassword.getText().toString().isEmpty()){
                if (prefs.getPassword().equals(etPassword.getText().toString())){
                    if(chkRemember.isChecked())
                        prefs.setSkipLogin(true);
                    Intent i = new Intent(getApplicationContext()
                            ,MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext()
                            , "Incorrect password.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
