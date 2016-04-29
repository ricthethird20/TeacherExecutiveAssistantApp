package app.tea.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;

import app.tea.adapters.SectionListAdapter;
import app.tea.com.DialogAddSection;
import app.tea.com.DialogMenu;
import app.tea.com.R;
import app.tea.constants.AppConstants;
import app.tea.db.AppDb;

/**
 * Created by PB5N0145 on 3/4/2016.
 */
public class FragmentSectionMain extends Fragment {
    private static ArrayList<HashMap<String,String>> mSectionList;
    AppDb appDb;
    View containerView;
    ListView lv_section;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        containerView = inflater.inflate(R.layout
                .fragment_sectionmain,container,false);
        lv_section = (ListView) containerView.findViewById(R.id.lv_section);
        lv_section.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map =
                        mSectionList.get(position);
                String sectName = map.get(AppConstants.SECTNAME);
                String sectRoom = map.get(AppConstants.SECTROOM);
                Intent i = new Intent(getActivity().getApplicationContext(), DialogMenu.class);
                i.putExtra(AppConstants.EXTRA_CLASS, "Section");
                i.putExtra(AppConstants.EXTRA_ID, sectName);
                getActivity().startActivity(i);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) containerView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity().getApplicationContext(), DialogAddSection.class);
                getActivity().startActivity(i);
            }
        });
        return containerView;
    }
    private void populateList(){
        appDb = new AppDb(getActivity().getApplicationContext());
        appDb.open();
        mSectionList = appDb.getSections();
        appDb.close();
    }

    private void setListAdapter(){
        lv_section.invalidateViews();
        if(mSectionList.size() > 0) {
            SectionListAdapter sectionListAdapter =
                    new SectionListAdapter(getActivity(), mSectionList);
            sectionListAdapter.notifyDataSetChanged();
            lv_section.setAdapter(sectionListAdapter);
        }
    }

    @Override
    public void onResume() {
        if(mSectionList != null)
            mSectionList.clear();
        populateList();
        setListAdapter();
        super.onResume();
    }
}
