package main.java.cz2006project.mojojo.Boundary.Leaves;

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
import main.java.cz2006project.mojojo.Entity.Leave;
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
public class PastLeaveFragment extends Fragment {

    RecyclerView LeaveList;
    RecyclerView.Adapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private boolean refresh = false;
    private boolean check_my_leaves=true;
    View v;
    LinearLayout LeaveMainLayout;
    ScrollView emptyLeave;

    public static Date date2= new Date();

    public static Leave lev= new Leave();
    public static int yeartest, monthtest, daytest, hourtest, minutetest;


    /**
     *Default construct for the PastLeaveFragment
     *
     */
    public PastLeaveFragment(){

    }

    /**
     *This method creates a new instance of the Past Appointments Fragment
     * with the required arguments.
     *
     */

    public static PastLeaveFragment newInstance(Boolean check){
        PastLeaveFragment pastLeaveFragment = new PastLeaveFragment();
        Bundle b = new Bundle();
        b.putBoolean("check", check);
        pastLeaveFragment.setArguments(b);
        return pastLeaveFragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(this.getArguments() != null){
            check_my_leaves = getArguments().getBoolean("check");
        }
    }
    /**
     *This method creates a view with all the required UI elements for the PastLeaveFragment
     *
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragments_past_appointments_list, container, false);

        LeaveList = (RecyclerView) v.findViewById(R.id.listviewpastappointments);

        LeaveMainLayout = (LinearLayout) v.findViewById(R.id.appointments_main_list);
        emptyLeave = (ScrollView) v.findViewById(R.id.empty_appointments);
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
    public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.ViewHolder> implements View.OnClickListener {

        private int expandedPosition = -1;
        private List<ParseObject> leave;

        /**
         *
         * This method instantiates a list of Parse Leave Objects to populate the view.
         *
         */

        public LeaveAdapter(List<ParseObject> appointments) {
            this.leave = leave;
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
            viewHolder.itemView.setOnClickListener(LeaveAdapter.this);
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

            holder.teacher.setText("Teacher: " + (String)leave.get(position).get(ParseTables.Leave.TEACHER));
            holder.type.setText("Leave Type " + (String) leave.get(position).get(ParseTables.Leave.LEAVETYPE));
            holder.leave_date.setText("Leave Date" + (String) leave.get(position).get(ParseTables.Leave.LEAVEDATE));





            if(check_my_leaves){
                holder.leave_followup.setVisibility(View.VISIBLE);
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
            return leave.size();
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

            Button leave_followup;




            TextView leave_creator;


            public ViewHolder(View itemView) {
                super(itemView);

                this.teacher = (TextView) itemView.findViewById(R.id.clinic);
                this.type = (TextView) itemView.findViewById(R.id.doctor);
                this.expanded_area = (RelativeLayout) itemView.findViewById(R.id.expanded_area);

                this.reason = (TextView) itemView.findViewById(R.id.notes);
                this.leave_date = (TextView) itemView.findViewById(R.id.appointment_date);


            };

               


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





            Date date2 = lev.getDate("Date");


            Calendar calendar = GregorianCalendar.getInstance();

            calendar.setTime(date2);
            hourtest = calendar.get(Calendar.HOUR_OF_DAY);
            minutetest = calendar.get(Calendar.MINUTE);

            calendar.set(year, monthOfYear, dayOfMonth, hourtest, minutetest);

            lev.put("Date", calendar.getTime());

            lev.saveInBackground(/*new SaveCallback() {
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




    /**
     *This method fetches the user's past appointments from Parse by
     * comparing the current date with the date of the appointment.
     *
     */
    public void fetchData(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Leaves");

        if(check_my_leaves){
            query.whereEqualTo("Teacher", ParseUser.getCurrentUser().getString("name"));
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
        adapter = new LeaveAdapter(appointments);
        LeaveList.setAdapter(adapter);
        if (refresh == true) {
            swipeRefreshLayout.setRefreshing(false);
            refresh = false;
        }
        if(check_my_leaves && adapter.getItemCount() == 0){
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





}

