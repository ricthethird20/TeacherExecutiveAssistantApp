package app.tea.com;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import app.tea.adapters.SubSectionListAdapter;
import app.tea.adapters.SubjectListAdapter;
import app.tea.constants.AppConstants;
import app.tea.db.AppDb;

/**
 * Created by PB5N0145 on 3/4/2016.
 */
public class SubjectDetails extends Fragment {
    AppDb appDb;
    View containerView;
    Spinner sp_section;
    MessageReceiver msg;
    ListView lv_section;
    EditText et_subjCode;
    EditText et_subjDesc;
    String subjCode;
    public static ArrayList<HashMap<String,String>> arr_sect;
    public static final String intentAddSection = "intent_add_section";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            subjCode = bundle.getString(AppConstants.SUBJECT_KEY,"");
            Log.d("TEA","Subjcode -> "+subjCode);
            appDb.open();
            et_subjCode.setText(subjCode);
            et_subjDesc.setText(appDb.getSubjectDesc(subjCode));
            appDb.close();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        containerView = inflater.inflate(R.layout
                .activity_addsubject,container,false);
        lv_section = (ListView) containerView.findViewById(R.id.listSections);
        et_subjCode = (EditText) containerView.findViewById(R.id.et_subjCode);
        et_subjDesc = (EditText) containerView.findViewById(R.id.et_subjDesc);
        init();
        FloatingActionButton fab = (FloatingActionButton) containerView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DialogAddSectionOnly.class);
                getActivity().startActivity(i);
            }
        });
        return containerView;
    }

    private void init(){
        arr_sect = new ArrayList<HashMap<String, String>>();
        appDb  = new AppDb(getActivity().getApplicationContext());
        appDb.open();
        msg = new MessageReceiver();
        getActivity().registerReceiver(msg, new IntentFilter(intentAddSection));

    }
    @Override
    public void onStop() {
        getActivity().unregisterReceiver(msg);
        if ((!et_subjCode.getText().toString().isEmpty() &&
                !et_subjDesc.getText().toString().isEmpty()) || arr_sect.size() > 0){
            saveData();
        }else{
            Toast.makeText(getActivity().getApplicationContext(),"Subject not save, Incomplete details"
                    ,Toast.LENGTH_SHORT).show();
        }
        arr_sect.clear();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(subjCode != null){
            for(int i = 0;i<getSections().size(); i++) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put(AppConstants.SECTNAME,getSections().get(i));
                Log.d("TEA","Onresume -> Sections : "+getSections().get(i));
                if(!arr_sect.contains(hashMap))
                    arr_sect.add(hashMap);
            }
        }
        setListAdapter2();
    }

    private void setListAdapter2(){
        if (arr_sect.size() > 0){
            Log.d("TEA","arr_sect size "+arr_sect.size());
            SubSectionListAdapter sectionListAdapter =
                    new SubSectionListAdapter(getActivity(),arr_sect);
            lv_section.setAdapter(sectionListAdapter);
        }

    }

    private ArrayList<String> getSections(){
        ArrayList<String> sectionList = new ArrayList<String>();
        appDb.open();
        sectionList = appDb.getSectionsUsingSubj2(subjCode);
        Log.d("TEA","List count - > "+sectionList.size());
        appDb.close();
        return sectionList;
    }

    private void saveData(){

        appDb.deleteRecord(AppConstants.SUBJ_TABLE,
                AppConstants.COL_SUBJ_CODE,subjCode);

        for(HashMap<String,String> x: arr_sect){

            String c = x.get(AppConstants.SECTNAME);
            String subjCode = et_subjCode.getText().toString();
            String subjDesc = et_subjDesc.getText().toString();
            Log.d("TEA","SECTIONS -> "+x.get(AppConstants.SECTNAME));
            appDb.insertSubjects(subjCode,subjDesc,c);
        }
    }

    private void setListAdapter(){
        if (arr_sect.size() > 0){
            SubSectionListAdapter sectionListAdapter =
                    new SubSectionListAdapter(getActivity(),arr_sect);
            lv_section.setAdapter(sectionListAdapter);
        }
    }
    private class MessageReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            setListAdapter();
        }
    }


}
