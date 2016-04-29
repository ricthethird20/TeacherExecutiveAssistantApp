package app.tea.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import app.tea.adapters.SubjectListAdapter;
import app.tea.com.DialogMenu;
import app.tea.com.R;
import app.tea.com.SubjectDetails;
import app.tea.constants.AppConstants;
import app.tea.db.AppDb;

/**
 * Created by PB5N0145 on 3/4/2016.
 */
public class FragmentSubjectMain extends Fragment implements DialogMenu.OnSelectActionListener  {
    private static ArrayList<HashMap<String,String>> mSubjectList;
    View containerView;
    ListView lv_subject;
    AppDb appDb;
    String subjCode;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        containerView = inflater.inflate(R.layout
                .fragment_subjectmain,container,false);
        lv_subject = (ListView) containerView.findViewById(R.id.lv_subject);

        lv_subject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map =
                        mSubjectList.get(position);
                String studName = map.get(AppConstants.SUBJDESC);
                subjCode = map.get(AppConstants.SUBJCODE);
                Intent i = new Intent(getActivity().getApplicationContext(), DialogMenu.class);
                i.putExtra(AppConstants.EXTRA_CLASS, "Subject");
                i.putExtra(AppConstants.EXTRA_ID, subjCode);
                getActivity().startActivity(i);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) containerView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                fragment = new SubjectDetails();
                getActivity().setTitle("Add Subject");
                if (fragment != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.rl_contentMain, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        DialogMenu.setOnSelectActionListener(this);
        return containerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mSubjectList != null)
            mSubjectList.clear();
        populateList();
        setListAdapter();
    }

    private void populateList(){
        appDb = new AppDb(getActivity().getApplicationContext());
        appDb.open();
        mSubjectList = appDb.getSubjects();
        appDb.close();
    }

    private void setListAdapter(){
        lv_subject.invalidateViews();
        if(mSubjectList.size() > 0){
            SubjectListAdapter subjectListAdapter =
                    new SubjectListAdapter(getActivity(),mSubjectList);
            subjectListAdapter.notifyDataSetChanged();
            lv_subject.setAdapter(subjectListAdapter);
        }
    }

    @Override
    public void setFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        Log.d("TEA", "setFragment - > " + subjCode);
        bundle.putString(AppConstants.SUBJECT_KEY,subjCode);
        fragment.setArguments(bundle);
        getActivity().setTitle("Add subject");
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.rl_contentMain, fragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
    }
}
