/*package main.java.cz2006project.mojojo.Boundary.Timetable;

import android.support.v4.app.Fragment;



import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.text.format.DateFormat;
import android.text.format.Time;
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
import main.java.cz2006project.mojojo.Entity.Timetable;
import main.java.cz2006project.mojojo.External.ParseTables;

import main.java.cz2006project.mojojo.Entity.Utils.MaterialEditText;
import main.java.cz2006project.mojojo.Entity.Act;


public class AddTimeTableFragment extends Fragment{


    Button create;
    static View v;
    Time startTime;
    Time endTime;
    public static int hourTest, minuteTest, yearTest, monthTest, dayTest;
    public static Calendar calendar;
    private Spinner classspinner;
    private Spinner sectionspinner;
    EditText teacher;
    EditText subject;
    private static HashMap<String, Object> timetable;
    ImageButton setDate;


    List<String> classlist = new ArrayList<>();
    List<String> sectionlist = new ArrayList<>();

    /**
     * Default constructor for the AddActivityFragment

    public AddTimeTableFragment() {
        // Required empty public constructor
    }

    public static AddTimeTableFragment newInstance() {
        AddTimeTableFragment fragment = new AddTimeTableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * This method instantiates a new hash map for Activity with the attributes for the hash map.


    @Override
    public void onCreate(Bundle savedInstanceState) {
        timetable = new HashMap<>();
        super.onCreate(savedInstanceState);
    }


    /**
     * This method creates a view with all the required UI elements for the AddActivityFragment


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_createactivity, container, false);
        create = (Button) v.findViewById(R.id.submit_button);
        setDate = (ImageButton) v.findViewById(R.id.date_picker);
        classspinner = (Spinner) v.findViewById(R.id.classspinner);

        sectionspinner = (Spinner) v.findViewById(R.id.sectionspinner);
        teacher=(EditText)v.findViewById(R.id.teacher);
        teacher=(EditText)v.findViewById(R.id.subject);

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
                timetable.put(ParseTables.TeacherSchedule.CLASS, classspinner.getSelectedItem().toString());
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
                timetable.put(ParseTables.TeacherSchedule.SECTION, sectionspinner.getSelectedItem().toString());
            }

            public void onNothingSelected(AdapterView<?> parent) {


            }

        });



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



    public void addInput() {
        timetable.put(ParseTables.TeacherSchedule.TEACHER,(EditText)v.findViewById(R.id.coordinator));
        timetable.put(ParseTables.TeacherSchedule.SUBJECT, (EditText) v.findViewById(R.id.coordinator));
        timetable.put(ParseTables.TeacherSchedule.STRARTTIME,(Time)v.findViewById(R.id.startTime) );
        timetable.put(ParseTables.TeacherSchedule.CLASS, classspinner.getSelectedItem().toString());
        timetable.put(ParseTables.TeacherSchedule.SECTION, sectionspinner.getSelectedItem().toString());
        timetable.put(ParseTables.TeacherSchedule.ENDTIME,(Time)v.findViewById(R.id.endTime));

    }

    /**
     * This method checks if any of the mandatory fields to book an activity have been left empty by the user.


    private boolean checkIfEmpty() {


        if (!timetable.get(ParseTables.TeacherSchedule.TEACHER).toString().isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Please enter teacher name", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!timetable.get(ParseTables.TeacherSchedule.CLASS).toString().isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Please select class", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!timetable.get(ParseTables.TeacherSchedule.SECTION).toString().isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Please select section", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!timetable.get(ParseTables.TeacherSchedule.SUBJECT).toString().isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Please enter subject", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!timetable.containsKey(ParseTables.TeacherSchedule.STRARTTIME)) {
            Toast.makeText(getActivity().getApplicationContext(), "Please enter start time", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!timetable.containsKey(ParseTables.TeacherSchedule.ENDTIME)) {
            Toast.makeText(getActivity().getApplicationContext(), "Please enterend time", Toast.LENGTH_LONG).show();
            return false;
        }



        return true;
    }



    private void pushDataToParse() {


        Timetable timetable = new Timetable();


        calendar.set(yearTest, monthTest, dayTest, hourTest, minuteTest);

        timetable.put(ParseTables.TeacherSchedule.TEACHER  timetable.get(ParseTables.TeacherSchedule.TEACHER));
        timetable.put(ParseTables.TeacherSchedule.SECTION,  timetable.get(ParseTables.TeacherSchedule.SECTION));
        timetable.put(ParseTables.TeacherSchedule.SUBJECT, timetable.get(ParseTables.TeacherSchedule.SUBJECT));
        timetable.put(ParseTables.TeacherSchedule.CLASS,  timetable.get(ParseTables.TeacherSchedule.CLASS));
        timetable.put(ParseTables.TeacherSchedule.STRARTTIME,  timetable.get(ParseTables.TeacherSchedule.STRARTTIME));
        timetable.put(ParseTables.TeacherSchedule.ENDTIME,  timetable.get(ParseTables.TeacherSchedule.ENDTIME));
        timetable.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                create.setClickable(true);
                Toast.makeText(getActivity().getApplicationContext(),
                        getString(R.string.leave_created), Toast.LENGTH_SHORT).show();
            }
        });
    }



    /**
     *This method creates a time picker used to set the time for an appointment and push it to Parse
     *

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            String time;
            String min = Integer.toString(minute);
            if (minute == 0) {
                min = "00";
            }
            else {
                min = "30";
            }

            if (hourOfDay > 12) {
                hourOfDay = hourOfDay - 12;
                time = String.valueOf(hourOfDay) + ":" + min + " pm";
                hourTest = hourOfDay;

            } else {
                time = String.valueOf(hourOfDay) + ":" + min + " am";
                hourTest = hourOfDay;

            }


            appointments.put(ParseTables.Appointment.TIME, time);
            ((MaterialEditText) v.findViewById(R.id.appointment_time)).setText(time);
        }

        /**
         *This method opens a dialog for the user to select a time.




        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = 9;
            int minute = 0;
            CustomTimePicker cusTimePicker = new CustomTimePicker(getActivity(),TimePickerDialog.THEME_HOLO_LIGHT ,this, hour, minute, DateFormat.is24HourFormat(getActivity()));


            return cusTimePicker;
        }
    }
}



}
*/