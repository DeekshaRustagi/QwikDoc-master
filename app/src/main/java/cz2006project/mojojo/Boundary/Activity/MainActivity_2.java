package main.java.cz2006project.mojojo.Boundary.Activity;
/*
 *  Copyright (c) 2014, Parse, LLC. All rights reserved.
 *
 *  You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 *  copy, modify, and distribute this software in source code or binary form for use
 *  in connection with the web services and APIs provided by Parse.
 *
 *  As with any software that integrates with the Parse platform, your use of
 *  this software is subject to the Parse Terms of Service
 *  [https://www.parse.com/about/terms]. This copyright notice shall be
 *  included in all copies or substantial portions of the software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import com.facebook.AppEventsLogger;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * <h1>Main Activity</h1>
 * The Main Activity is the starting point of the application. It starts the Parse Login Activity
 * if the user is not already logged into QwikDoc or starts the PatientActivity if the user is
 * already logged into QwikDoc.
 *
 *
 * <p>
 *
 * @author  Srishti Lal
 * @version 1.0
 * @since   2014-03-31
 */

public class MainActivity extends Activity {
    private static final int LOGIN_REQUEST = 0;

    private String pat;
    private ParseUser currentUser;
    String newline = System.getProperty("line.separator");


    /**Starts the patient activity for the user if the user type is Patient, otherwise redirects user back to
     * the login page.
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (currentUser != null && currentUser.getString("type").equals("Patient") ) {

            Intent patientIntent = new Intent(this, PatientActivity.class);
            startActivity(patientIntent);

            sendMail();



        } else {


            ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                    MainActivity.this);
            startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);
        }

    }

    /**
     * Starts the patient activity for the user if the user type is Patient, otherwise redirects user back to
     * the login page.
     *
     */

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = ParseUser.getCurrentUser();



        if (currentUser != null ) {

            Intent patientIntent = new Intent(this, PatientActivity.class);
            startActivity(patientIntent);

            sendMail();
        }
        else {
            ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                    MainActivity.this);
            startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);
        }


    }




    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }


    /**
     * If user type logged in is Admin, then a mail is sent to all those patients
     * who have an upcoming appointment within 24 hours. This email is only
     * sent to each patient once regardless of how many times this activity is started.
     *
     */

    public void sendMail() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Appointment");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> appointmentList, ParseException e) {
                if (e == null) {
                    //Log.d("score", "Retrieved " + appointmentList.size() + " appointments");
                    for (final ParseObject appointment : appointmentList) {
                        Date appointmentDate = appointment.getDate("Date");
                        Boolean ReminderSent = appointment.getBoolean("ReminderSent");
                        if (ReminderSent == false) {

                            Calendar cal = Calendar.getInstance();
                            Calendar cal1 = Calendar.getInstance();
                            //Date dt2 = cal1.getTime();

                            cal1.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE+ 1, Calendar.HOUR_OF_DAY, Calendar.MINUTE);
                            Date date = cal.getTime();
                            long duration  = appointmentDate.getTime() - date.getTime();
                            long diffinHours = TimeUnit.MILLISECONDS.toHours(duration);


                            if (diffinHours <= 24) {
                                pat = appointment.getString("patient");


                                ParseQuery<ParseUser> pquery = ParseUser.getQuery();
                                pquery.whereEqualTo("name", pat);
                                pquery.getFirstInBackground(new GetCallback<ParseUser>() {
                                    public void done(final ParseUser patient, ParseException e) {
                                        if (e == null) {


                                                try {
                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage(patient.getString("contactnumber"), null, "APPOINTMENT REMINDER! Please do be punctual for your Appointment", null, null);

                                                    appointment.put("ReminderSent", true);
                                                    appointment.saveInBackground();


                                                } catch (Exception ex) {

                                                    ex.printStackTrace();

                                                }



                                                Map<String, String> params = new HashMap<>();
                                                params.put("text", "Dear " + patient.getString("name") + newline + newline + "Reminder for your upcoming appointment:" + newline + "Doctor: " + "Dr " + appointment.getString("doctor") + newline + "Clinic: " + appointment.getString("clinic") + newline + "Date: " + appointment.getDate("Date") + newline + newline + "Please do be punctual for your appointment!" + newline + "Regards," + newline + "The QwikDoc Team");
                                                params.put("subject", "Appointment Reminder for " + patient.getString("name"));
                                                params.put("fromEmail", "qwikdoc@hotmail.com");
                                                params.put("fromName", "QwikDoc");
                                                params.put("toEmail", patient.getEmail());

                                                params.put("toName", patient.getString("name"));


                                                ParseCloud.callFunctionInBackground("sendMail", params, new FunctionCallback<Object>() {
                                                    @Override
                                                    public void done(Object response, ParseException exc) {
                                                        appointment.put("ReminderSent", true);
                                                        appointment.saveInBackground();


                                                    }


                                                });

                                        } else {

                                        }

                                    }

                                });
                            }

                        }


                    }


                }else {
                }
            }
        });
    }




}
