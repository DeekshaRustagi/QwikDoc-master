package main.java.cz2006project.mojojo.Boundary.Leaves;
import android.widget.AdapterView.OnItemSelectedListener;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cz2006project.mojojo.R;
import main.java.cz2006project.mojojo.External.ParseTables;
import main.java.cz2006project.mojojo.Entity.Leave;
import main.java.cz2006project.mojojo.Entity.Utils.MaterialEditText;
import android.widget.EditText;
import android.widget.AdapterView.OnItemSelectedListener;


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
     * This method instantiates a new hash map for leaves with the attributes for the hash map.
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
      /*  typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                   Log.d("type","flag4");

                final ParseQuery<ParseObject> query = ParseQuery.getQuery("Leave");
                query.whereExists("Teacher");
                query.whereEqualTo("LeaveType", typespinner.getSelectedItem().toString());


                typelist.clear();

                query.findInBackground(new FindCallback<ParseObject>() {

                    @Override
                    public void done(List<ParseObject> leave, ParseException e) {
                        // The query returns a list of objects from the "questions" class
                        if (e == null) {
                            for (ParseObject type : leave) {
                                // Get the questionTopic value from the question object
                                String initial = typespinner.getSelectedItem().toString();

                                String Teachername = type.getString("name");
                                typelist.add(Teachername);

                                Log.d("Teacher", "name: " + type.getString("name"));
                                ArrayAdapter adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, typelist);
                                typespinner.setAdapter(adapter1);
                                Log.d("doctor", "name: " + type.getString("name"));


                            }

                        } else {
                            Log.d("notretreive", "Error: " + e.getMessage());
                        }
                    }
                });
            }


            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.show(getActivity().getSupportFragmentManager(), "Set Date");
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                create.setClickable(false);
                addInput();
                if (checkIfEmpty()) {
                    pushDataToParse();
                }

            }
        });
return v;
    }
*/

        typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

          @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // On selecting a spinner item
                String item = arg0.getItemAtPosition(arg2).toString();
                              // Showing selected spinner item
                Toast.makeText(arg0.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.show(getActivity().getSupportFragmentManager(), "Set Date");
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                create.setClickable(false);
                addInput();
                if (checkIfEmpty()) {
                    pushDataToParse();
                }

            }

        });

        return v;
    }


     /**
      * This method adds input from the fragment view to the leaves hash map.
      */


     public void addInput() {
         Log.d("deek1","flag3");
       /*  leaves.put(ParseTables.Leave.TEACHER,((EditText)v.findViewById(R.id.teacher)).getText()+"");
         //leaves.put(ParseTables.Leave.TEACHER, ParseUser.getCurrentUser().getString("name"));
         leaves.put(ParseTables.Leave.LEAVETYPE, typespinner.getSelectedItem().toString());
         Log.d("put type", "flag2");
         leaves.put(ParseTables.Leave.REASON, ((MaterialEditText) v.findViewById(R.id.reason)).getText()+"");
         leaves.put(ParseTables.Leave.LEAVEDATE, calendar.getTime());*/
         leaves.put(ParseTables.Leave.TEACHER, ParseUser.getCurrentUser().getString("name"));
         leaves.put("Teacher", ((EditText)v.findViewById(R.id.teacher)).getText().toString());
         leaves.put("Reason", ((MaterialEditText) v.findViewById(R.id.reason)).getText().toString());
         //leaves.put("LeaveDate", calendar.getTime());
         leaves.put("LeaveType", typespinner.getSelectedItem().toString());

     }

     /**
      * This method checks if any of the mandatory fields to book an leaves have been left empty by the user.
      */

     private boolean checkIfEmpty() {


         if (!leaves.containsKey(ParseTables.Leave.LEAVETYPE)) {
             Toast.makeText(getActivity().getApplicationContext(), "Please select type of leave", Toast.LENGTH_LONG).show();
             return false;
         }


         if (!leaves.containsKey(ParseTables.Leave.LEAVEDATE)) {
             Toast.makeText(getActivity().getApplicationContext(), "Please enter date", Toast.LENGTH_LONG).show();
             return false;
         }
         if (!leaves.containsKey(ParseTables.Leave.TEACHER)) {
             Toast.makeText(getActivity().getApplicationContext(), "Please enter teacher name", Toast.LENGTH_LONG).show();
             return false;
         }

         return true;
     }


     /**
      * This method pushes the data from the leaves hash map created earlier, to the Parse database
      * by creating a new Leave object with the details entered by the user in the Parse Leave Class.
      */


     private void pushDataToParse() {


         Leave leave = new Leave();

         calendar.set(yearTest, monthTest, dayTest, hourTest, minuteTest);
         leave.put(ParseTables.Leave.LEAVEDATE, calendar.getTime());
         leave.put(ParseTables.Leave.LEAVEDATE, leaves.get(ParseTables.Leave.LEAVEDATE));
         leave.put(ParseTables.Leave.LEAVETYPE, leaves.get(ParseTables.Leave.LEAVETYPE));
         Log.d("deek","flag1");
         leave.put(ParseTables.Leave.REASON, leaves.get(ParseTables.Leave.REASON));
         leave.put(ParseTables.Leave.TEACHER, leaves.get(ParseTables.Leave.TEACHER));


         leave.saveInBackground(new SaveCallback() {
             @Override
             public void done(ParseException e) {
                 create.setClickable(true);
                 Toast.makeText(getActivity().getApplicationContext(),
                         getString(R.string.leave_created), Toast.LENGTH_SHORT).show();
             }
         });
     }



    /**
      *This method creates a date picker used to set the date for an leaves and push it to Parse
      *
      */


     public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

         @Override
         public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
             monthOfYear++;
             String date = String.valueOf(dayOfMonth) + "/" + monthOfYear + "/" + year;
             ((MaterialEditText) v.findViewById(R.id.leave_date)).setText(date);

             yearTest = year;
             monthTest = monthOfYear - 1;
             dayTest = dayOfMonth;
             calendar = Calendar.getInstance();
             calendar.set(yearTest, monthTest, dayTest, hourTest, minuteTest);
             leaves.put(ParseTables.Leave.LEAVEDATE, calendar.getTime());


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



