package com.parse.ui;



import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Fragment for the user signup screen.
 */
public class ParseSignupFragment extends ParseLoginFragmentBase implements OnClickListener, AdapterView.OnItemLongClickListener {

    public static final String USERNAME = "com.parse.ui.ParseSignupFragment.USERNAME";
    public static final String PASSWORD = "com.parse.ui.ParseSignupFragment.PASSWORD";
    public static final String NAME = "com.parse.ui.ParseSignupFragment.NAME";
    static View v;
    private EditText usernameField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private EditText emailField;
    private EditText nameField;
    private EditText contactnumberField;

    private Button createAccountButton;
    private CheckBox reminderBySMS;
    private CheckBox reminderByEmail;
    private TextView reminderText;

    private static HashMap<String, String> users;
    private ParseOnLoginSuccessListener onLoginSuccessListener;

    ImageButton setDate;
    public EditText signup_DOB;
    public ArrayAdapter<String> adapter_specialty, adapter_branch;
    private DatePicker dp;

    ParseUser user = new ParseUser();




    public void onCreate(Bundle savedInstanceState) {
        users = new HashMap<>();
        super.onCreate(savedInstanceState);
    }

    private ParseLoginConfig config;
    private int minPasswordLength;

    private static final String LOG_TAG = "ParseSignupFragment";
    private static final int DEFAULT_MIN_PASSWORD_LENGTH = 6;
    private static final String USER_OBJECT_NAME_FIELD = "name";
    private static final String USER_CN_TYPE_FIELD = "contactnumber";



    public static ParseSignupFragment newInstance(Bundle configOptions, String username, String password) {
        ParseSignupFragment signupFragment = new ParseSignupFragment();
        Bundle args = new Bundle(configOptions);
        args.putString(ParseSignupFragment.USERNAME, username);
        args.putString(ParseSignupFragment.PASSWORD, password);

        signupFragment.setArguments(args);
        return signupFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.com_parse_ui_parse_signup_fragment,
                parent, false);




        setDate=  (ImageButton) v.findViewById(R.id.date_picker);
        signup_DOB= (EditText) v.findViewById(R.id.signup_DOB);

        setDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.show(getActivity().getSupportFragmentManager(), "Set Date");
            }
        });

        Bundle args = getArguments();
        config = ParseLoginConfig.fromBundle(args, getActivity());

        minPasswordLength = DEFAULT_MIN_PASSWORD_LENGTH;
        if (config.getParseSignupMinPasswordLength() != null) {
            minPasswordLength = config.getParseSignupMinPasswordLength();
        }

        String username =  args.getString(USERNAME);
        String password =  args.getString(PASSWORD);


        ImageView appLogo = (ImageView) v.findViewById(R.id.helpguru);
        usernameField = (EditText) v.findViewById(R.id.signup_username_input);
        passwordField = (EditText) v.findViewById(R.id.signup_password_input);
        confirmPasswordField = (EditText) v.findViewById(R.id.signup_confirm_password_input);
        emailField = (EditText) v.findViewById(R.id.signup_email_input);
        contactnumberField = (EditText) v.findViewById(R.id.signup_contactnumber_input);

        nameField = (EditText) v.findViewById(R.id.signup_name_input);

        reminderByEmail = (CheckBox) v.findViewById(R.id.Email);
        reminderBySMS = (CheckBox) v.findViewById(R.id.SMS);
        reminderText = (TextView) v.findViewById(R.id.remindertext);


        createAccountButton = (Button) v.findViewById(R.id.create_account);

        usernameField.setText(username);
        passwordField.setText(password);

        if (appLogo != null && config.getAppLogo() != null) {
            appLogo.setImageResource(config.getAppLogo());
        }

        if (config.isParseLoginEmailAsUsername()) {
            usernameField.setHint(R.string.com_parse_ui_email_input_hint);
            usernameField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            if (emailField != null) {
                emailField.setVisibility(View.GONE);
            }
        }

        if (config.getParseSignupSubmitButtonText() != null) {
            createAccountButton.setText(config.getParseSignupSubmitButtonText());
        }

        createAccountButton.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                String passwordAgain = confirmPasswordField.getText().toString();

                String email = null;
                if (config.isParseLoginEmailAsUsername()) {
                    email = usernameField.getText().toString();
                } else if (emailField != null) {
                    email = emailField.getText().toString();
                }

                String name = null;
                if (nameField != null) {
                    name = nameField.getText().toString();
                }

                String contactnumber = null;
                if (contactnumberField != null) {
                    contactnumber = contactnumberField.getText().toString();
                }

                if (username.length() == 0) {
                    if (config.isParseLoginEmailAsUsername()) {
                        showToast(R.string.com_parse_ui_no_email_toast);
                    } else {
                        showToast(R.string.com_parse_ui_no_username_toast);
                    }
                } else if (password.length() == 0) {
                    showToast(R.string.com_parse_ui_no_password_toast);
                } else if (contactnumber.length() == 0) {
                    showToast(R.string.com_parse_ui_no_contactnumber_toast);
                }else if (password.length() < minPasswordLength) {
                    showToast(getResources().getQuantityString(
                            R.plurals.com_parse_ui_password_too_short_toast,
                            minPasswordLength, minPasswordLength));
                } else if (passwordAgain.length() == 0) {
                    showToast(R.string.com_parse_ui_reenter_password_toast);
                } else if (!password.equals(passwordAgain)) {
                    showToast(R.string.com_parse_ui_mismatch_confirm_password_toast);
                    confirmPasswordField.selectAll();
                    confirmPasswordField.requestFocus();
                } else if (email != null && email.length() == 0) {
                    showToast(R.string.com_parse_ui_no_email_toast);
                } else if (name != null && name.length() == 0) {
                    showToast(R.string.com_parse_ui_no_name_toast);

                } else {


                    // Set standard fields
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setEmail(email);

                    if(reminderByEmail.isChecked())
                        user.put("EmailReminder","Yes");
                    else
                        user.put("EmailReminder", "No");

                    if(reminderBySMS.isChecked())
                        user.put("SMSReminder", "Yes");
                    else
                        user.put("SMSReminder", "No");
                    // Set additional custom fields only if the user filled it out
                    if (name.length() != 0) {
                        user.put(USER_OBJECT_NAME_FIELD, name);
                    }

                    if (contactnumber.length() != 0) {
                        user.put("contactnumber", contactnumber);
                    }


                    loadingStart();
                    user.signUpInBackground(new SignUpCallback() {

                        @Override
                        public void done(ParseException e) {
                            if (isActivityDestroyed()) {
                                return;
                            }

                            if (e == null) {
                                loadingFinish();
                                signupSuccess();
                            } else {
                                loadingFinish();
                                if (e != null) {
                                    debugLog(getString(R.string.com_parse_ui_login_warning_parse_signup_failed) +
                                            e.toString());
                                    switch (e.getCode()) {
                                        case ParseException.INVALID_EMAIL_ADDRESS:
                                            showToast(R.string.com_parse_ui_invalid_email_toast);
                                            break;
                                        case ParseException.USERNAME_TAKEN:
                                            showToast(R.string.com_parse_ui_username_taken_toast);
                                            break;
                                        case ParseException.EMAIL_TAKEN:
                                            showToast(R.string.com_parse_ui_email_taken_toast);
                                            break;
                                        default:
                                            showToast(R.string.com_parse_ui_signup_failed_unknown_toast);
                                    }
                                }
                            }
                        }
                    });
                }

            }
        });


        return v;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear++;
            String date = String.valueOf(dayOfMonth) + "/" + monthOfYear + "/" + year;

            ((EditText) v.findViewById(R.id.signup_DOB)).setText(date);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ParseOnLoginSuccessListener) {
            onLoginSuccessListener = (ParseOnLoginSuccessListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseOnLoginSuccessListener");
        }

        if (activity instanceof ParseOnLoadingListener) {
            onLoadingListener = (ParseOnLoadingListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseOnLoadingListener");
        }
    }

    @Override
    public void onClick(View v) {

        }




    @Override
    protected String getLogTag() {
        return LOG_TAG;
    }

    private void signupSuccess() {
        onLoginSuccessListener.onLoginSuccess();
    }


    public void addListenerOnChkSMS() {


        reminderBySMS.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    if (reminderBySMS.isChecked()) {

                        user.put("SMSReminder", 1);

                    } else
                        user.put("SMSReminder", 0);


                }

            }
        });

    }

    public void addListenerOnChkEmail() {


        reminderByEmail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {


                    if (reminderByEmail.isChecked()) {
                        user.put("emailReminder", 1);
                    }
                    else
                        user.put("emailReminder", 0);

                }

            }
        });

    }
}



