package main.java.cz2006project.mojojo.Boundary.Appointments;

/**
 * Created by srishti on 5/4/15.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import cz2006project.mojojo.R;
import main.java.cz2006project.mojojo.Entity.Appointment;
import main.java.cz2006project.mojojo.External.ParseTables;
import main.java.cz2006project.mojojo.Entity.Utils.CustomTimePicker;



/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */

/**
 * <h1>Past Appointments Fragment</h1>
 * This fragment is used to display the patient's past appointments with doctors. It also allows
 * patients to follow up their past appointments by selecting a date and time for the follow-up appointment.
 *
 * <p>
 *
 * @author  Dhruv Sharma
 * @version 1.0
 * @since   2014-03-31
 */
public class PastAppointmentsFragment extends Fragment {

    RecyclerView appointmentsList;
    RecyclerView.Adapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private boolean refresh = false;
    private boolean check_my_appointments=true;
    View v;
    LinearLayout appointmentsMainLayout;
    ScrollView emptyAppointment;

    public static Date date2= new Date();

    public static Appointment appt= new Appointment();
    public static int yeartest, monthtest, daytest, hourtest, minutetest;


    /**
     *Default construct for the PastAppointmentsFragment
     *
     */
    public PastAppointmentsFragment(){

    }

    /**
     *This method creates a new instance of the Past Appointments Fragment
     * with the required arguments.
     *
     */

    public static PastAppointmentsFragment newInstance(Boolean check){
        PastAppointmentsFragment pastAppointmentsFragment = new PastAppointmentsFragment();
        Bundle b = new Bundle();
        b.putBoolean("check", check);
        pastAppointmentsFragment.setArguments(b);
        return pastAppointmentsFragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(this.getArguments() != null){
            check_my_appointments = getArguments().getBoolean("check");
        }
    }
    /**
     *This method creates a view with all the required UI elements for the PastAppointmentsFragment
     *
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragments_past_appointments_list, container, false);

        appointmentsList = (RecyclerView) v.findViewById(R.id.listviewpastappointments);
        appointmentsMainLayout = (LinearLayout) v.findViewById(R.id.appointments_main_list);
        emptyAppointment = (ScrollView) v.findViewById(R.id.empty_appointments);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        appointmentsList.setLayoutManager(layoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = true;
                fetchData();
            }
        });
        fetchData();

        return v;
    }


    /**
     *This class creates a view showing the list of past appointments to the user
     * which can be clicked to view more details about the appointment and follow up the
     * appointment.
     *
     */
    public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> implements View.OnClickListener {

        private int expandedPosition = -1;
        private List<ParseObject> appointments;

        /**
         *
         * This method instantiates a list of Parse Appointment Objects to populate the view.
         *
         */

        public AppointmentsAdapter(List<ParseObject> appointments) {
            this.appointments = appointments;
        }

        /**
         *
         * This method inflates the view to see the list of past appointments
         * and attaches a listener so that each appointment can be clicked to view
         * further details.
         *
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView cd = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.listviewpastappointments, parent, false);
            ViewHolder viewHolder = new ViewHolder(cd);
            viewHolder.itemView.setOnClickListener(AppointmentsAdapter.this);
            viewHolder.itemView.setTag(viewHolder);
            return viewHolder;
        }


        /**
         *
         * This method binds the data from Parse to the appointment view
         * with details about the appointment.
         *
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.appointment_type.setText("Appointment Type: " + (String)appointments.get(position).get(ParseTables.Appointment.TYPE));
            holder.doctor.setText("Doctor: " + (String)appointments.get(position).get(ParseTables.Appointment.DOCTOR));
            holder.clinic.setText("Clinic: " + (String)appointments.get(position).get(ParseTables.Appointment.CLINIC));
            holder.appointment_date.setText(appointments.get(position).get(ParseTables.Appointment.DATE)+" "+appointments.get(position).get(ParseTables.Appointment.TIME));
            holder.appointment_time.setText((String)appointments.get(position).get(ParseTables.Appointment.TIME));



            if(check_my_appointments){
                holder.appointment_followup.setVisibility(View.VISIBLE);
            }

            if (position == expandedPosition) {
                holder.expanded_area.setVisibility(View.VISIBLE);
            } else {
                holder.expanded_area.setVisibility(View.GONE);
            }
        }
        /**
         *Returns the size of the past appointments list.
         *
         * @return The number of past appointments booked by the user.
         *
         */

        @Override
        public int getItemCount() {
            return appointments.size();
        }


        /**
         *Expands the appointment view on click to show more details about the appointment that is clicked.
         *
         *
         */

        @Override
        public void onClick(View v) {
            final ViewHolder holder = (ViewHolder) v.getTag();
            if (holder.getPosition() == expandedPosition) {
                holder.expanded_area.setVisibility(View.GONE);
                expandedPosition = -1;
            } else {
                if (expandedPosition >= 0) {
                    int prev = expandedPosition;
                    notifyItemChanged(prev);
                }
                expandedPosition = holder.getPosition();
                notifyItemChanged(expandedPosition);
            }

        }

        /**
         *
         * This method inflates the view to see the list of past appointments
         * and attaches a listener so that each appointment can be clicked to view
         * further details.
         *
         */

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView appointment_type;
            TextView clinic;
            TextView doctor;
            RelativeLayout expanded_area;
            TextView notes;
            TextView appointment_date;
            TextView appointment_time;
            Button appointment_followup;
            Boolean Pre_Assessment;



            TextView appointment_creator;


            public ViewHolder(View itemView) {
                super(itemView);
                this.appointment_type = (TextView) itemView.findViewById(R.id.appointment_type);
                this.clinic = (TextView) itemView.findViewById(R.id.clinic);
                this.doctor = (TextView) itemView.findViewById(R.id.doctor);
                this.expanded_area = (RelativeLayout) itemView.findViewById(R.id.expanded_area);

                this.notes = (TextView) itemView.findViewById(R.id.notes);
                this.appointment_date = (TextView) itemView.findViewById(R.id.appointment_date);
                this.appointment_time = (TextView) itemView.findViewById(R.id.appointment_time);
                this.appointment_followup = (Button) itemView.findViewById(R.id.appointment_followup);
                //this.appointment_change = (Button) itemView.findViewById(R.id.appointment_change);
                appointment_followup.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {

                        appt.put(ParseTables.Appointment.PATIENT, appointments.get(getPosition()).get(ParseTables.Appointment.PATIENT));
                        appt.put(ParseTables.Appointment.DOCTOR, appointments.get(getPosition()).get(ParseTables.Appointment.DOCTOR));
                        appt.put(ParseTables.Appointment.CLINIC, appointments.get(getPosition()).get(ParseTables.Appointment.CLINIC));
                        appt.put(ParseTables.Appointment.TYPE, appointments.get(getPosition()).get(ParseTables.Appointment.TYPE));


                        if(appointments.get(getPosition()).get("Pre_Assessment")==Boolean.TRUE) {

                            Toast.makeText(getActivity().getApplicationContext(),
                                    getString(R.string.pre_assessment_needed), Toast.LENGTH_SHORT).show();


                        }
                        else{
                            DatePickerFragment datePicker = new DatePickerFragment();
                            datePicker.show(getActivity().getSupportFragmentManager(), "Set Date");
                            TimePickerFragment timePickerFragment = new TimePickerFragment();
                            timePickerFragment.show(getActivity().getSupportFragmentManager(), "Set Time");
                            appointment_followup.setClickable(true);
                        }
                    }
                });

               


            }
        }
    }


    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    /**
     *This method creates a date picker used to set the date for an appointment and push it to Parse
     *
     */

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {





            Date date2 = appt.getDate("Date");


            Calendar calendar = GregorianCalendar.getInstance();

            calendar.setTime(date2);
            hourtest = calendar.get(Calendar.HOUR_OF_DAY);
            minutetest = calendar.get(Calendar.MINUTE);

            calendar.set(year, monthOfYear, dayOfMonth, hourtest, minutetest);

            appt.put("Date", calendar.getTime());

            appt.saveInBackground(/*new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    Toast.makeText(getActivity().getApplicationContext(),
                            getString(R.string.appointment_created), Toast.LENGTH_SHORT).show();
                }
            }*/);

        }
        /**
         *@return dialog to set date of follow-up appointment
         *
         */

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog date =   new DatePickerDialog(getActivity(), this, year, month, day);



            Calendar cal = Calendar.getInstance();
            cal.set(year, month + 1, day);

            date.getDatePicker().setMinDate(c.getTimeInMillis());
            date.getDatePicker().setMaxDate(cal.getTimeInMillis());



            //If you need you can set min date too


            return date;
        }
    }

    /**
     *This method creates a time picker used to set the date for an appointment and push it to Parse
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
            } else {
                time = String.valueOf(hourOfDay) + ":" + min + " am";
            }



            Calendar calendar = GregorianCalendar.getInstance();

            calendar.setTime(date2);
            yeartest = calendar.get(Calendar.YEAR);
            monthtest = calendar.get(Calendar.MONTH);
            daytest = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(yeartest, monthtest, daytest, hourOfDay, minute);

            appt.put("Date", calendar.getTime());
            appt.put("time", time);


        }

        /**
         *@return dialog to set time of follow-up appointment
         *
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = 9;
            int minute = 0;
            CustomTimePicker cusTimePicker = new CustomTimePicker(getActivity(), TimePickerDialog.THEME_HOLO_LIGHT, this, hour, minute, DateFormat.is24HourFormat(getActivity()));


            return cusTimePicker;
        }

    }


    /**
     *This method fetches the user's past appointments from Parse by
     * comparing the current date with the date of the appointment.
     *
     */
    public void fetchData(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Appointment");

        if(check_my_appointments){
            query.whereEqualTo("patient", ParseUser.getCurrentUser().getString("name"));
            Calendar currentDate = Calendar.getInstance();
            Date current = currentDate.getTime();
            query.whereLessThan("Date", current);
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                doneFetching(parseObjects);
            }
        });


    }


    /**
     *This populates the past appointments list once the data is fetched from Parse.
     *
     */

    public void doneFetching(List<ParseObject> appointments){
        adapter = new AppointmentsAdapter(appointments);
        appointmentsList.setAdapter(adapter);
        if (refresh == true) {
            swipeRefreshLayout.setRefreshing(false);
            refresh = false;
        }
        if(check_my_appointments && adapter.getItemCount() == 0){
            appointmentsMainLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }





}

