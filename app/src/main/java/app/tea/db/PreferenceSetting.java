package app.tea.db;

import android.content.SharedPreferences;

/**
 * Created by PB5N0145 on 3/15/2016.
 */
public class PreferenceSetting {
    private SharedPreferences prefs;
    private final String SKIP_LOGIN = "skip_login";
    private final String USER_NAME = "user_name";
    private final String PWORD = "password";
    private final String IP = "ip";




    public PreferenceSetting(SharedPreferences prefs){
        this.prefs = prefs;
    }
    public String getIP(){
        return prefs.getString(IP,null);
    }

    public boolean isSkipLogin(){
        return prefs.getBoolean(SKIP_LOGIN,false);
    }

    public String getPassword(){
        return prefs.getString(PWORD,null);
    }

    public String getUserName(){
        return prefs.getString(USER_NAME,null);
    }

    public void setServerIp(String ip){
        prefs.edit()
                .putString(IP,ip)
                .commit();
    }

    public void setPassword(String password){
        prefs.edit()
                .putString(PWORD,password)
                .commit();
    }

    public void setUserName(String userName){
        prefs.edit()
                .putString(USER_NAME,userName)
                .commit();
    }
    public void setSkipLogin(boolean skip){
        prefs.edit()
                .putBoolean(SKIP_LOGIN,skip)
                .commit();
    }
}
