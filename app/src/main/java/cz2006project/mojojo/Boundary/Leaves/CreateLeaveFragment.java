package main.java.cz2006project.mojojo.Boundary.Leaves;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
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
import main.java.cz2006project.mojojo.Entity.Utils.CustomTimePicker;
import main.java.cz2006project.mojojo.Entity.Appointment;
import main.java.cz2006project.mojojo.Entity.Utils.MaterialEditText;

/**
 * <h1>Create Appointment Fragment</h1>
 * This fragment is used by patients to book appointments with doctors by inputting all necessary details
 * required to book the appointment.
 * <p/>
 * <p/>
 *
 * @author Srishti Lal
 * @version 1.0
 * @since 2014-03-31
 */


public class CreateLeaveFragment extends Fragment {


    Button create;
    static View v;
    private Spinner typespinner;
    public static int hourTest, minuteTest, yearTest, monthTest, dayTest;
    public static Calendar calendar;


    private static HashMap<String, Object> leaves;
    ImageButton setDate;

    List<String> typelist = new ArrayList<>();


    /**
     * Default constructor for the CreateLeaveFragment
     */

    public CreateLeaveFragment() {
        // Required empty public constructor
    }


    /**
     * This method instantiates a new hash map for Appointments with the attributes for the hash map.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        leaves = new HashMap<>();
        super.onCreate(savedInstanceState);
    }


    /**
     * This method creates a view with all the required UI elements for the CreateLeaveFragment
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_createleave, container, false);
        create = (Button) v.findViewById(R.id.submit_button);
        setDate = (ImageButton) v.findViewById(R.id.date_picker);

        typespinner = (Spinner) v.findViewById(R.id.typespinner);


        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.leave_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        typespinner.setAdapter(adapter2);


        typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                leaves.put(ParseTables.Leave.LEAVETYPE, typespinner.getSelectedItem().toString());


            }
        });





    public void onNothingSelected(AdapterView<?> parent) {
    }


    setDate.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.show(getActivity().getSupportFragmentManager(), "Set Date");
    }
    }

    );

    create.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){

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
     * This method adds input from the fragment view to the appointments hash map.
     */


    public void addInput() {

        leaves.put(ParseTables.Leave.TEACHER, ParseUser.getCurrentUser().getString("name"));
        leaves.put(ParseTables.Leave.LEAVETYPE, typespinner.getSelectedItem().toString());

        leaves.put(ParseTables.Leave.REASON, ((MaterialEditText) v.findViewById(R.id.notes)).getText() + "");


    }

    /**
     * This method checks if any of the mandatory fields to book an appointment have been left empty by the user.
     */

    private boolean checkIfEmpty() {


        if (leaves.get(ParseTables.Leave.CLINIC).toString().isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Please select clinic", Toast.LENGTH_LONG).show();
            return false;
        }


        if (!leaves.containsKey(ParseTables.Appointment.DATE)) {
            Toast.makeText(getActivity().getApplicationContext(), "Please enter date", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!appointments.containsKey(ParseTables.Appointment.TIME)) {
            Toast.makeText(getActivity().getApplicationContext(), "Please enter time", Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }


    /**
     * This method pushes the data from the 'appointments' hash map created earlier, to the Parse database
     * by creating a new Appointment object with the details entered by the user in the Parse Appointment Class.
     */


    private void pushDataToParse() {


        Appointment appointment = new Appointment();

        calendar.set(yearTest, monthTest, dayTest, hourTest, minuteTest);
        appointments.put(ParseTables.Appointment.DATE, calendar.getTime());
        appointment.put(ParseTables.Appointment.DATE, appointments.get(ParseTables.Appointment.DATE));
        appointment.put(ParseTables.Appointment.TIME, appointments.get(ParseTables.Appointment.TIME));


        appointment.put(ParseTables.Appointment.TYPE, appointments.get(ParseTables.Appointment.TYPE));
        appointment.put(ParseTables.Appointment.NOTES, appointments.get(ParseTables.Appointment.Reason));
        appointment.put(ParseTables.Appointment.PATIENT, appointments.get(ParseTables.Appointment.Teacher));
        appointment.put("ReminderSent", false);

        appointment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                create.setClickable(true);
                Toast.makeText(getActivity().getApplicationContext(),
                        getString(R.string.appointment_created), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *This method creates a date picker used to set the date for an appointment and push it to Parse
     *
     */
}

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear++;
            String date = String.valueOf(dayOfMonth) + "/" + monthOfYear + "/" + year;
            ((MaterialEditText) v.findViewById(R.id.appointment_date)).setText(date);

            yearTest = year;
            monthTest = monthOfYear - 1;
            dayTest = dayOfMonth;
            calendar = Calendar.getInstance();
            calendar.set(yearTest, monthTest, dayTest, hourTest, minuteTest);
            appointments.put(ParseTables.Appointment.DATE, calendar.getTime());


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

    /**
     *This method creates a time picker used to set the time for an appointment and push it to Parse
     *
     */
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
         *
         */

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = 9;
            int minute = 0;
            CustomTimePicker cusTimePicker = new CustomTimePicker(getActivity(),TimePickerDialog.THEME_HOLO_LIGHT ,this, hour, minute, DateFormat.is24HourFormat(getActivity()));


            return cusTimePicker;
        }
    }
}