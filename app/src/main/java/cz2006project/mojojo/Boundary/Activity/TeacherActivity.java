package main.java.cz2006project.mojojo.Boundary.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;
import cz2006project.mojojo.R;
import main.java.cz2006project.mojojo.Boundary.Account.ProfileFragment;
import main.java.cz2006project.mojojo.Boundary.Activities.ListActivityFragment;
import main.java.cz2006project.mojojo.Boundary.Leaves.LeaveFragment;
import main.java.cz2006project.mojojo.Application.SampleApplication;
import main.java.cz2006project.mojojo.Entity.Utils.NavigationDrawerFragment;

/**
 * <h1>Patient Activity</h1>
 * The Patient Activity allows the patient to book appointments with doctors, search for doctors
 * and clinics, edit their account etc. It is the starting activity for all patients using QwikDoc.
 *
 *
 * <p>
 *
 * @author  Srishti Lal
 * @version 1.0
 * @since   2014-03-31
 */

public class TeacherActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String TAG = "TeacherActivity";
    public static final boolean DEBUG = SampleApplication.LOG_DEBUG;
    public static final boolean INFO = SampleApplication.LOG_INFO;
    private static final int LOGIN_REQUEST = 0;

    private ParseUser currentUser;


    String[] paths;
    Toolbar toolbar;

    /**
     * Fragment  managing the behaviors, interactions and presentation of the navigation drawer.
     */

    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */

    private CharSequence mTitle;
    private String myTitle;

    /**
     *
     *This method creates a toolbar and a navigation drawer for the activity.
     *
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_exp);

        myTitle = getString(R.string.app_name);
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                toolbar.setTitle(myTitle);
                toolbar.setTitleTextColor(getResources().getColor(R.color.white));

            }
        }


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getResources().getString(R.string.appointments);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


    }

    /**
     *
     *This method determines which fragments are started when the patient
     * clicks on different items in the navigation drawer.
     *
     * The current fragment is replaced by the fragment that performs the action that the patient has
     * requested
     *
     */

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
            default:
                if (DEBUG) Log.d(TAG, "TimeTable Fragment");
                mTitle = "TimeTable";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, LeaveFragment.newInstance())
                        .commit();
                break;
            case 1:
                if (DEBUG) Log.d(TAG, "Leave fragment");
                mTitle = "Leave";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new LeaveFragment())
                        .commit();
                break;
            case 2:
                if (DEBUG) Log.d(TAG, "Activity fragment");
                mTitle = "Activity";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ListActivityFragment())
                        .commit();
                break;
            case 3:
                if (DEBUG) Log.d(TAG, "myprofile fragment");
                mTitle = "Profile";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ProfileFragment())
                        .commit();
                break;


            case 4:
                if (DEBUG) Log.d(TAG, "logout");
                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                TeacherActivity.this.finish();
                                ParseUser.logOut();
                                currentUser = null;
                                ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                                        TeacherActivity.this);
                                startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;



        }

    }
    /**
     *
     *This Method creates the titles to be displayed on the top for each fragment.
     *
     */

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.qwiksearch);
                break;
            case 2:
                mTitle = getString(R.string.profile);
                break;
            case 3:
                mTitle = getString(R.string.logout);
                break;

        }
    }

    public void restoreActionBar() {
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.toolbar.setTitle(mTitle);
        this.getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 131077 && resultCode == Activity.RESULT_OK) {
            paths = data.getStringArrayExtra("all_path");
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}
