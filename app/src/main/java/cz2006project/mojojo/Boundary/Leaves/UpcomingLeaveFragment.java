package main.java.cz2006project.mojojo.Boundary.Leaves;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.app.Dialog;
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
import android.app.DatePickerDialog;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import cz2006project.mojojo.R;
import main.java.cz2006project.mojojo.External.ParseTables;
import main.java.cz2006project.mojojo.Entity.Utils.CustomTimePicker;


/**
 * A simple {@link Fragment} subclass.
 */

/**
 * <h1>Upcoming Appointments Fragment</h1>
 * This fragment is used to display the patient's upcoming appointments with doctors. It also allows
 * patients to change date and time, or cancel their upcoming appointments.
 *
 * <p>
 *
 * @author  Dhruv Sharma
 * @version 1.0
 * @since   2014-03-31
 */
public class UpcomingLeaveFragment extends Fragment {

    RecyclerView LeaveList;
    RecyclerView.Adapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private boolean refresh = false;
    private boolean check_my_leaves = true;
    View v;
    LinearLayout LeaveMainLayout;
    ScrollView emptyLeaves;


    public static ParseObject appt;
    public static int yeartest, monthtest, daytest, hourtest, minutetest;


    /**
     *Default construct for the UpcomingLeaveFragment
     *
     */

    public UpcomingLeaveFragment() {

    }


    /**
     *This method creates a new instance of the Past Appointments Fragment
     * with the required arguments.
     *
     */


    public static UpcomingLeaveFragment newInstance(Boolean check) {
        UpcomingLeaveFragment upcomingAppointmentsFragment = new UpcomingLeaveFragment();
        Bundle b = new Bundle();
        b.putBoolean("check", check);
        upcomingAppointmentsFragment.setArguments(b);
        return upcomingAppointmentsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (this.getArguments() != null) {
            check_my_leaves = getArguments().getBoolean("check");
        }
    }


    /**
     *This method creates a view with all the required UI elements for the UpcomingLeaveFragment.
     *
     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragments_upcoming_appointments_list, container, false);
        LeaveList = (RecyclerView) v.findViewById(R.id.listviewappointments);
      LeaveMainLayout = (LinearLayout) v.findViewById(R.id.appointments_main_list);
        emptyLeaves = (ScrollView) v.findViewById(R.id.empty_appointments);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        LeaveList.setLayoutManager(layoutManager);
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
            CardView cd = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_appointments, parent, false);
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

            holder.teacher.setText("Teacher: " + (String) appointments.get(position).get(ParseTables.Appointment.DOCTOR));
            holder.type.setText("Type of Leave: " + (String) appointments.get(position).get(ParseTables.Appointment.CLINIC));
            holder.leave_date.setText(appointments.get(position).get(ParseTables.Appointment.DATE) + " " + appointments.get(position).get(ParseTables.Appointment.TIME));
            holder.leave_time.setText((String)appointments.get(position).get(ParseTables.Appointment.TIME));



            if (check_my_leaves) {
                holder.leave_delete.setVisibility(View.VISIBLE);
                holder.leave_changedate.setVisibility(View.VISIBLE);
                holder.leave_changetime.setVisibility(View.VISIBLE);

            }

            if (position == expandedPosition) {
                holder.expanded_area.setVisibility(View.VISIBLE);
            } else {
                holder.expanded_area.setVisibility(View.GONE);
            }
        }

        /**
         *Returns the size of the upcoming appointments list.
         *
         * @return The number of upcoming appointments booked by the user.
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

            TextView type;
            TextView teacher;
            RelativeLayout expanded_area;
            TextView reason;
            TextView leave_date;
            TextView leave_time;
            Button leave_delete;
            Button leave_changedate;
            Button leave_changetime;




            public ViewHolder(View itemView) {
                super(itemView);

                this.type = (TextView) itemView.findViewById(R.id.clinic);
                this.teacher = (TextView) itemView.findViewById(R.id.doctor);
                this.expanded_area = (RelativeLayout) itemView.findViewById(R.id.expanded_area);

                this.reason = (TextView) itemView.findViewById(R.id.notes);
                this.leave_date = (TextView) itemView.findViewById(R.id.appointment_date);
                this.leave_time = (TextView) itemView.findViewById(R.id.appointment_time);
                this.leave_delete = (Button) itemView.findViewById(R.id.appointment_delete);
                this.leave_changedate = (Button) itemView.findViewById(R.id.appointment_changedate);
                this.leave_changetime = (Button) itemView.findViewById(R.id.appointment_changetime);
                //this.appointment_change = (Button) itemView.findViewById(R.id.appointment_change);


                leave_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        builder.setTitle("Confirm");
                        builder.setMessage("Are you sure you want to cancel this leave?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                appt = appointments.get(getPosition());
                                Calendar calendar = Calendar.getInstance();
                                int year = calendar.get(Calendar.YEAR);
                                int month = calendar.get(Calendar.MONTH);
                                int day = calendar.get(Calendar.DATE);
                                calendar.set(year, month, day, 0, 0, 0);
                                Date BegginingOfToday = calendar.getTime();
                                if (appt.getDate("Date").after(BegginingOfToday)) {
                                    appt.deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                fetchData();
                                            } else {
                                                Toast.makeText(getActivity().getApplicationContext(), "Internet Connection Problem", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    });
                                }

                                dialog.dismiss();
                            }

                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();


                    }
                });

                leave_changedate.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        appt = appointments.get(getPosition());
                        DatePickerFragment datePicker = new DatePickerFragment();
                        datePicker.show(getActivity().getSupportFragmentManager(), "Set Date");

                    }
                });


                leave_changetime.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        appt = appointments.get(getPosition());
                        TimePickerFragment timePickerFragment = new TimePickerFragment();
                        timePickerFragment.show(getActivity().getSupportFragmentManager(), "Set Time");

                    }
                });
            }
        }
    }
    /**
     *This method fetches the user's upcoming appointments from Parse by
     * comparing the current date with the date of the appointment.
     *
     */
    public void fetchData() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Leave");

        if (check_my_leaves) {
            query.whereEqualTo("teacher", ParseUser.getCurrentUser().getString("name"));
            Calendar currentDate = Calendar.getInstance();
            Date current = currentDate.getTime();
            query.whereGreaterThanOrEqualTo("Date", current);
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                doneFetching(parseObjects);
            }
        });


    }

    /**
     *This populates the upcoming appointments list once the data is fetched from Parse.
     *
     */
    public void doneFetching(List<ParseObject> appointments) {
        adapter = new AppointmentsAdapter(appointments);
        LeaveList.setAdapter(adapter);
        if (refresh == true) {
            swipeRefreshLayout.setRefreshing(false);
            refresh = false;
        }
        if (check_my_leaves && adapter.getItemCount() == 0) {
            LeaveMainLayout.setVisibility(View.GONE);
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


    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    /**
     *This method creates a date picker used to change the date for an appointment and push it to Parse
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
            appt.saveInBackground();
        }


        /**
         *@return dialog to change date of upcoming appointment
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
     *This method creates a time picker used to change the date for an appointment and push it to Parse
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


            Date date2 = appt.getDate("Date");
            Calendar calendar = GregorianCalendar.getInstance();

            calendar.setTime(date2);
            yeartest = calendar.get(Calendar.YEAR);
            monthtest = calendar.get(Calendar.MONTH);
            daytest = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(yeartest, monthtest, daytest, hourOfDay, minute);

            appt.put("Date", calendar.getTime());
            appt.put("time", time);
            appt.saveInBackground();
        }


        /**
         *@return dialog to change time of upcoming appointment
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
}