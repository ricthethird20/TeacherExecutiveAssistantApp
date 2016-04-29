package app.tea.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import app.tea.adapters.SettingItemsAdapter;
import app.tea.com.R;

/**
 * Created by PB5N0145 on 3/31/2016.
 */
public class FragmentSettingMain extends Fragment {
    ListView lv_setting_items;
    private static ArrayList<HashMap<String,String>> settingItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout
                .fragment_setting_selection,container,false);
        lv_setting_items = (ListView) rootView.findViewById(R.id.lv_setting_items);
        lv_setting_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment;
                if(position == 0)
                    fragment = new FragmentSetting();
                else
                    fragment = new FragmentSettingServer();

                if (fragment != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.rl_contentMain, fragment)
                            .addToBackStack(null)
                            .commit();
                }

            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        populateList();
        setListAdapter();
        super.onResume();
    }

    private void populateList(){
        String records[] = {"Grade Setting","Server Configuration"};
        settingItems = new ArrayList<HashMap<String, String>>();
        for(int i=0;i<records.length;i++){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("SETTING_LABEL",records[i]);
            settingItems.add(hashMap);
        }
    }

    private void setListAdapter(){
        SettingItemsAdapter settingItemsAdapter =
                new SettingItemsAdapter(getActivity(),settingItems);
        lv_setting_items.setAdapter(settingItemsAdapter);
    }
}
