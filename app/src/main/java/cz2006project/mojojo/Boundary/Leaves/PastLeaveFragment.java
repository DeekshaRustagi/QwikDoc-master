package main.java.cz2006project.mojojo.Boundary.Leaves;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
     *This method creates a new instance of the Past Leaves Fragment
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
        v = inflater.inflate(R.layout.fragments_past_leave_list, container, false);

        LeaveList = (RecyclerView) v.findViewById(R.id.listviewpastleave);

        LeaveMainLayout = (LinearLayout) v.findViewById(R.id.leave_main_list);
        emptyLeave = (ScrollView) v.findViewById(R.id.empty_leave);
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
     *This class creates a view showing the list of past leaves to the user
     * which can be clicked to view more details about the leave and follow up the
     * leave
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

        public LeaveAdapter(List<ParseObject> leave) {
            this.leave = leave;
        }

        /**
         *
         * This method inflates the view to see the list of past leaves
         * and attaches a listener so that each leave can be clicked to view
         * further details.
         *
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView cd = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.listviewpastleave, parent, false);
            ViewHolder viewHolder = new ViewHolder(cd);
            viewHolder.itemView.setOnClickListener(LeaveAdapter.this);
            viewHolder.itemView.setTag(viewHolder);
            return viewHolder;
        }


        /**
         *
         * This method binds the data from Parse to the leave view
         * with details about the leave.
         *
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.teacher.setText("Teacher: " + (String)leave.get(position).get(ParseTables.Leave.TEACHER));
            holder.type.setText("Leave Type " + (String) leave.get(position).get(ParseTables.Leave.LEAVETYPE));
            holder.leave_date.setText("Leave Date" + (String) leave.get(position).get(ParseTables.Leave.LEAVEDATE));
            holder.reason.setText("Reason: " + (String)leave.get(position).get(ParseTables.Leave.REASON));



            if (position == expandedPosition) {
                holder.expanded_area.setVisibility(View.VISIBLE);
            } else {
                holder.expanded_area.setVisibility(View.GONE);
            }
        }
        /**
         *Returns the size of the past leave list.
         *
         * @return The number of past leave booked by the user.
         *
         */

        @Override
        public int getItemCount() {
            return leave.size();
        }


        /**
         *Expands the leave view on click to show more details about the all leave that is clicked.
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
         * This method inflates the view to see the list of past leaves
         * and attaches a listener so that each leave can be clicked to view
         * further details.
         *
         */

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView type;
            TextView teacher;
            RelativeLayout expanded_area;
            TextView reason;
            TextView leave_date;






            TextView leave_creator;


            public ViewHolder(View itemView) {
                super(itemView);

                this.teacher = (TextView) itemView.findViewById(R.id.teacher);
                this.type = (TextView) itemView.findViewById(R.id.doctor);
                this.expanded_area = (RelativeLayout) itemView.findViewById(R.id.expanded_area);

                this.reason = (TextView) itemView.findViewById(R.id.reason);
                this.leave_date = (TextView) itemView.findViewById(R.id.leave_date);


            };

               


            }
        }



    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    /**
     *This method creates a date picker used to set the date for an leave and push it to Parse
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
     *This method creates a time picker used to set the date for an leaves and push it to Parse
     *
     */




    /**
     *This method fetches the user's past leaves from Parse by
     * comparing the current date with the date of the leaves
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
     *This populates the past leaves list once the data is fetched from Parse.
     *
     */

    public void doneFetching(List<ParseObject> leave){
        adapter = new LeaveAdapter(leave);
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

