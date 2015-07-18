package main.java.cz2006project.mojojo.Boundary.Activities;

import android.support.v4.app.Fragment;



import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cz2006project.mojojo.R;
import main.java.cz2006project.mojojo.External.ParseTables;

import main.java.cz2006project.mojojo.Entity.Utils.MaterialEditText;
import main.java.cz2006project.mojojo.Entity.Act;


public class AddActivityFragment extends Fragment{


    Button create;
    static View v;
    private Spinner typespinner;
    public static int hourTest, minuteTest, yearTest, monthTest, dayTest;
    public static Calendar calendar;
    private Spinner classspinner;
    private Spinner sectionspinner;
    EditText coordinator;

    private static HashMap<String, Object> activity;
    ImageButton setDate;

    List<String> typelist = new ArrayList<>();
    List<String> classlist = new ArrayList<>();
    List<String> sectionlist = new ArrayList<>();

    /**
     * Default constructor for the AddActivityFragment
     */

    public AddActivityFragment() {
        // Required empty public constructor
    }

    public static AddActivityFragment newInstance() {
        AddActivityFragment fragment = new AddActivityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * This method instantiates a new hash map for Activity with the attributes for the hash map.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activity = new HashMap<>();
        super.onCreate(savedInstanceState);
    }


    /**
     * This method creates a view with all the required UI elements for the AddActivityFragment
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_createactivity, container, false);
        create = (Button) v.findViewById(R.id.submit_button);
        setDate = (ImageButton) v.findViewById(R.id.date_picker);
        classspinner = (Spinner) v.findViewById(R.id.classspinner);
        typespinner = (Spinner) v.findViewById(R.id.typespinner);
        sectionspinner = (Spinner) v.findViewById(R.id.sectionspinner);
        coordinator=(EditText)v.findViewById(R.id.coordinator);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.Activity_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        typespinner.setAdapter(adapter2);


        typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                activity.put(ParseTables.Activity.ACTIVITYTYPE, typespinner.getSelectedItem().toString());
            }

            public void onNothingSelected(AdapterView<?> parent) {


            }

        });

        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(getActivity(),
                R.array.Class_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        classspinner.setAdapter(adapter3);


        classspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                activity.put(ParseTables.Activity.CLASS, classspinner.getSelectedItem().toString());
            }

            public void onNothingSelected(AdapterView<?> parent) {


            }

        });
        ArrayAdapter adapter4 = ArrayAdapter.createFromResource(getActivity(),
                R.array.Section_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sectionspinner.setAdapter(adapter4);


        sectionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                activity.put(ParseTables.Activity.SECTION, sectionspinner.getSelectedItem().toString());
            }

            public void onNothingSelected(AdapterView<?> parent) {


            }

        });

        setDate.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View v) {
                                           DatePickerFragment datePicker = new DatePickerFragment();
                                           datePicker.show(getActivity().getSupportFragmentManager(), "Set Date");
                                       }
                                   }

        );

        create.setOnClickListener(new View.OnClickListener()

                                  {
                                      @Override
                                      public void onClick(View v) {

                                          create.setClickable(false);
                                          addInput();
                                          if (checkIfEmpty()) {
                                              pushDataToParse();
                                          }

                                      }
                                  }

        );
        return v;

    }


    /**
     * This method adds input from the fragment view to the activity hash map.
     */


    public void addInput() {
activity.put(ParseTables.Activity.COORDINATOR,(EditText)v.findViewById(R.id.coordinator));
        activity.put(ParseTables.Activity.COORDINATOR, ParseUser.getCurrentUser().getString("name"));
        activity.put(ParseTables.Activity.ACTIVITYTYPE, typespinner.getSelectedItem().toString());
        activity.put(ParseTables.Activity.CLASS, classspinner.getSelectedItem().toString());
        activity.put(ParseTables.Activity.SECTION, sectionspinner.getSelectedItem().toString());
        activity.put(ParseTables.Activity.ACTIVITYDATE,setDate);

    }

    /**
     * This method checks if any of the mandatory fields to book an activity have been left empty by the user.
     */

    private boolean checkIfEmpty() {


        if (!activity.get(ParseTables.Activity.ACTIVITYTYPE).toString().isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Please select type of activity", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!activity.get(ParseTables.Activity.CLASS).toString().isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Please select class", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!activity.get(ParseTables.Activity.SECTION).toString().isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Please select section", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!activity.get(ParseTables.Activity.COORDINATOR).toString().isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Please select coordinator", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!activity.containsKey(ParseTables.Activity.ACTIVITYDATE)) {
            Toast.makeText(getActivity().getApplicationContext(), "Please enter date", Toast.LENGTH_LONG).show();
            return false;
        }



        return true;
    }



    private void pushDataToParse() {


      Act activity = new Act();


        calendar.set(yearTest, monthTest, dayTest, hourTest, minuteTest);
        activity.put(ParseTables.Activity.ACTIVITYDATE, calendar.getTime());
        activity.put(ParseTables.Activity.ACTIVITYDATE, activity.get(ParseTables.Activity.ACTIVITYDATE));
        activity.put(ParseTables.Activity.SECTION, activity.get(ParseTables.Activity.SECTION));
        activity.put(ParseTables.Activity.ACTIVITYTYPE,activity.get(ParseTables.Activity.ACTIVITYTYPE));
        activity.put(ParseTables.Activity.CLASS, activity.get(ParseTables.Activity.CLASS));
        activity.put(ParseTables.Activity.COORDINATOR, activity.get(ParseTables.Activity.COORDINATOR));

        activity.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                create.setClickable(true);
                Toast.makeText(getActivity().getApplicationContext(),
                        getString(R.string.leave_created), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *This method creates a date picker used to set the date for an activity and push it to Parse
     *
     */


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear++;
            String date = String.valueOf(dayOfMonth) + "/" + monthOfYear + "/" + year;
            ((MaterialEditText) v.findViewById(R.id.activity_date)).setText(date);

            yearTest = year;
            monthTest = monthOfYear - 1;
            dayTest = dayOfMonth;
            calendar = Calendar.getInstance();
            calendar.set(yearTest, monthTest, dayTest, hourTest, minuteTest);
            activity.put(ParseTables.Activity.ACTIVITYDATE, calendar.getTime());


        }

        /**
         *This method opens a date picker dialog for the user to select a date
         *
         */

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog date = new DatePickerDialog(getActivity(), this, year, month, day);


            Calendar cal = Calendar.getInstance();
            cal.set(year, month + 1, day);

            date.getDatePicker().setMinDate(c.getTimeInMillis());
            date.getDatePicker().setMaxDate(cal.getTimeInMillis());


            return date;
        }

    }

}



