package app.tea.com;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.tea.db.AppDb;
import app.tea.db.PreferenceSetting;
import app.tea.fragments.FragmentGradesSubjects;
import app.tea.fragments.FragmentSectionMain;
import app.tea.fragments.FragmentSetting;
import app.tea.fragments.FragmentSettingMain;
import app.tea.fragments.FragmentStudentMain;
import app.tea.fragments.FragmentSubjectMain;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    AppDb appDB;
    TextView tvHeaderName;
    TextView tvHeaderPosition;
    PreferenceSetting prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        init();
        setDefaultFragment();
        prefs = new PreferenceSetting(PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext()));
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        tvHeaderName = (TextView)headerView.findViewById(R.id.tv_username);
        tvHeaderPosition = (TextView)headerView.findViewById(R.id.tv_position);
        tvHeaderPosition.setText("Instructor");
        tvHeaderName.setText("Hi "+prefs.getUserName()+"!");

    }
    private void setDefaultFragment(){
        Fragment fragment = new FragmentStudentMain();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.rl_contentMain, fragment)
                .addToBackStack(null)
                .commit();
    }
    private void init(){
        appDB = new AppDb(this);
        appDB.open();
        appDB.createTable();
        appDB.close();
    }

    @Override
    public void onBackPressed() {
            int countHistory = getFragmentManager().getBackStackEntryCount();
            if(countHistory == 1)
                super.onBackPressed();
            else
                getFragmentManager().popBackStack();
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Fragment fragment = new FragmentSetting();
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.rl_contentMain, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_student) {
            fragment = new FragmentStudentMain();
            setTitle("Students");
        } else if (id == R.id.nav_subjects) {
            fragment = new FragmentSubjectMain();
            setTitle("Subjects");
        } else if (id == R.id.nav_sections) {
            fragment = new FragmentSectionMain();
            setTitle("Sections");
        } else if (id == R.id.nav_grades) {
            fragment = new FragmentGradesSubjects();
            setTitle("Grades");
        } else if (id == R.id.nav_export) {

            Intent i = new Intent(getApplicationContext(),DialogExportGrade.class);
            startActivity(i);

        } else if (id == R.id.nav_settings) {
            fragment = new FragmentSettingMain();
            setTitle("Settings");
        } else if (id == R.id.nav_logout) {
            prefs.setSkipLogin(false);


            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            finish();

        }
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.rl_contentMain, fragment)
                    .addToBackStack(null)
                    .commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);




        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
