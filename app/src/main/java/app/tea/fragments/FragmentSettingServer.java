package app.tea.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import app.tea.com.R;
import app.tea.db.AppDb;
import app.tea.db.PreferenceSetting;

/**
 * Created by Ric on 3/31/2016.
 */
public class FragmentSettingServer extends Fragment {
    EditText et_ip;
    PreferenceSetting prefs;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_server_setting,container,false);
        et_ip = (EditText) rootView.findViewById(R.id.et_ip);
        prefs = new PreferenceSetting(PreferenceManager
                .getDefaultSharedPreferences(getActivity()));
        return rootView;
    }

    @Override
    public void onResume() {
        et_ip.setText(prefs.getIP());
        super.onResume();
    }

    @Override
    public void onStop() {
        if(!et_ip.getText().toString().isEmpty())
            prefs.setServerIp(et_ip.getText().toString());
        super.onStop();
    }
}
