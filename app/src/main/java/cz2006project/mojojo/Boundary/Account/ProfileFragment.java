package main.java.cz2006project.mojojo.Boundary.Account;

/**
 * Created by srishti on 30/3/15.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;

import cz2006project.mojojo.R;
import main.java.cz2006project.mojojo.Application.SampleApplication;
import main.java.cz2006project.mojojo.Entity.Utils.MaterialEditText;
import main.java.cz2006project.mojojo.External.ParseTables;



/**
 * <h1>Profile Fragment</h1>
 * The Profile Fragment displays the user's email address and name on the top and also allows the user
 * to change his username and password for logging in to the QwikDoc Application.
 * <p>
 *
 * @author  Srishti Lal
 * @version 1.0
 * @since   2014-03-31
 */


public class ProfileFragment extends Fragment {

    private EditText ePassword, eEmail, eNewPassword, eConfirmPassword;//eInterests
    private TextView tEmail, tFullName;
    private ImageButton editPassword, editEmail, canEditEmail, canEditPassword; //editInterest
    private View.OnClickListener oclEdit, oclSubmit, oclCancelEdit, oclPasswordEdit, oclPasswordSubmit, oclCancelPasswordEdit;
    private View rootView;
    private LinearLayout passwordContainer;
    private View newpassFormContainer;
    private ParseImageView imageProfile;
    private HashMap<String, String> userInfo;


    /**
     * Constructs a new profile fragment
     *
     */

    public ProfileFragment() {
        // Required empty public constructor
    }


    /**
     * Enables animation in the profile fragment page.
     *
     */

    private static void slide_down(Context context, View v) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        if (a != null)
            a.reset();
        if (v != null) {
            v.clearAnimation();
            v.setVisibility(View.VISIBLE);
            v.startAnimation(a);
        }
    }


    /**
     * Enables animation in the profile fragment page.
     *
     */

    private static void slide_up(Context context, View v) {

        Animation a = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        if (a != null)
            a.reset();
        if (v != null) {
            v.clearAnimation();
            if (a != null) {
                v.startAnimation(a);
            }
            v.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        int p = getActivity().getResources().getColor(R.color.accountColorPrimary);
        int s = getActivity().getResources().getColor(R.color.accountColorPrimaryDark);
        SampleApplication.setCustomTheme((ActionBarActivity) getActivity(), p, s);
        userInfo = new HashMap<>();
        init();
        try {
            fetchInfoFromParse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }

    /**
     * This method fetches the current users username, name and image (if available) from the Parse database
     * to display in the header of the page
     *
     */

    private void fetchInfoFromParse() throws Exception {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.get(ParseTables.Users.USERNAME) != null)
                tEmail.setText(currentUser.getString(ParseTables.Users.USERNAME));


            if (currentUser.get(ParseTables.Users.NAME) != null)
                tFullName.setText(currentUser.getString(ParseTables.Users.NAME));

        } else {
            //TODO: handle errors if any generated
        }

        ParseFile profileFile = null;
        if (currentUser != null) {
            profileFile = currentUser.getParseFile(ParseTables.Users.NAME);
        }
        imageProfile.setParseFile(profileFile);
        imageProfile.loadInBackground();
    }


    /**
     * This method instantiates the view of the profile fragment by creating a view for each element of the fragment.
     * It also determines which view is visible when the fragment is first created.
     * It also defines listeners for button clicks in the current view to perform some action.
     *
     */

    private void init() {

        tFullName = (TextView) rootView.findViewById(R.id.account_info_fullname);
        tEmail = (TextView) rootView.findViewById(R.id.account_info_email);

        ePassword = (MaterialEditText) rootView.findViewById(R.id.account_info_password);
        editPassword = (ImageButton) rootView.findViewById(R.id.edit_password_button);

        eEmail = (MaterialEditText) rootView.findViewById(R.id.account_info_emailid);
        editEmail = (ImageButton) rootView.findViewById(R.id.edit_email_button);


        newpassFormContainer = rootView.findViewById(R.id.new_password_form_container);

        eNewPassword = (MaterialEditText) rootView.findViewById(R.id.account_info_new_password);
        eConfirmPassword = (MaterialEditText) rootView.findViewById(R.id.account_info_confirm_password);


        passwordContainer = (LinearLayout) rootView.findViewById(R.id.account_info_container_password);

        canEditEmail = (ImageButton) rootView.findViewById(R.id.cancel_edit_email_button);
        canEditPassword = (ImageButton) rootView.findViewById(R.id.cancel_edit_password_button);

        ePassword.setEnabled(false);
        eEmail.setEnabled(false);
        eNewPassword.setEnabled(false);
        eConfirmPassword.setEnabled(false);

        imageProfile = (ParseImageView) rootView.findViewById(R.id.account_info_picture);

        oclEdit = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.edit_email_button:
                        eEmail.setSelected(true);
                        eEmail.setSelection(0, eEmail.getText().length());
                        eEmail.setFocusable(true);
                        eEmail.setEnabled(true);
                        eEmail.setFocusableInTouchMode(true);
                        canEditEmail.setVisibility(View.VISIBLE);
                        break;

                }


                //v.setBackground();
                ((ImageButton) v).setImageResource(R.drawable.tick);

                v.setOnClickListener(oclSubmit);
            }
        };

        oclSubmit = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.edit_email_button:
                        changeAttribute(eEmail, ParseTables.Users.USERNAME);
                        canEditEmail.setVisibility(View.INVISIBLE);
                        break;

                }

                //v.setBackground
                ((ImageButton) v).setImageResource(R.drawable.pencil);

                v.setOnClickListener(oclEdit);
            }
        };

        oclCancelEdit = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                switch (v.getId()) {
                    case R.id.cancel_edit_email_button:
                        editEmail.setImageResource(R.drawable.pencil);
                        editEmail.setOnClickListener(oclEdit);
                        editEmail.setEnabled(false);
                        editEmail.setFocusable(false);
                        break;

                }
            }
        };


        editEmail.setOnClickListener(oclEdit);
        canEditEmail.setOnClickListener(oclCancelEdit);

        oclPasswordEdit = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ePassword.setEnabled(true);
                eNewPassword.setEnabled(true);
                eConfirmPassword.setEnabled(true);
                ePassword.setFocusable(true);
                ePassword.setFocusableInTouchMode(true);
                ePassword.setHint(getActivity().getString(R.string.old_password));
                canEditPassword.setVisibility(View.VISIBLE);
                slide_down(getActivity(), newpassFormContainer);
                ImageButton clicked = (ImageButton) rootView.findViewById(v.getId());
                clicked.setImageResource(R.drawable.tick);
                editPassword.setOnClickListener(oclPasswordSubmit);
            }
        };

        oclPasswordSubmit = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canEditPassword.setVisibility(View.INVISIBLE);
                ePassword.setFocusable(false);
                ePassword.setEnabled(false);
                ePassword.setFocusableInTouchMode(false);
                eNewPassword.setEnabled(false);
                eConfirmPassword.setEnabled(false);
                ePassword.setHint(getActivity().getString(R.string.password));
                changePassword();
                slide_up(getActivity(), newpassFormContainer);
                ((ImageButton) v).setImageResource(R.drawable.pencil);
                editPassword.setOnClickListener(oclPasswordEdit);

            }
        };

        oclCancelPasswordEdit = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                editPassword.setImageResource(R.drawable.pencil);
                ePassword.setFocusable(false);
                ePassword.setEnabled(false);
                ePassword.setFocusableInTouchMode(false);
                eNewPassword.setEnabled(false);
                eConfirmPassword.setEnabled(false);
                ePassword.setHint(getActivity().getString(R.string.password));
                slide_up(getActivity(), newpassFormContainer);
                editPassword.setImageResource(R.drawable.pencil);
                editPassword.setOnClickListener(oclPasswordEdit);

            }
        };

        editPassword.setOnClickListener(oclPasswordEdit);
        canEditPassword.setOnClickListener(oclCancelPasswordEdit);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .penaltyLog()
                .build();
        StrictMode.setThreadPolicy(policy);

    }


    /**
     *
     * This method is used to change a particular field that can be changed by the user
     * in the profile fragment. It notifies the user when the change is successfully completed.
     *
     * @param et The EditText field that is filled in by the user with new text.
     * @param attr The attribute that is being changed by the user. For eg. Name or Username
     *
     */


    private void changeAttribute(EditText et, final String attr) {
        if (attr.equals(ParseTables.Users.USERNAME) || attr.equals(ParseTables.Users.NAME)) {
            if (et.getText().toString().isEmpty())
                Toast.makeText(getActivity(), attr + "cannot be empty", Toast.LENGTH_LONG).show();
        }

        ParseUser cu = ParseUser.getCurrentUser();
        cu.put(attr, et.getText().toString());
        cu.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    Toast.makeText(getActivity(), "Updated " + attr.toLowerCase() + " successfully.", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(getActivity(), "Email already taken. Please pick another one", Toast.LENGTH_LONG).show();
                }

            }
        });
        et.setEnabled(false);
        et.setFocusable(false);
    }


    /**
     *
     * This method is used to change the password of the current user in the database.
     *
     * @return true if user's password is successfully changed.
     */
    private boolean changePassword() {
        String oldPassword, newPassword, confirmPassword;


        oldPassword = ePassword.getText().toString();
        newPassword = eNewPassword.getText().toString();
        confirmPassword = eConfirmPassword.getText().toString();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {

            Toast.makeText(getActivity(), "Please enter the Old Password and the new password twice.", Toast.LENGTH_LONG).show();
            ePassword.setText("");
            eNewPassword.setText("");
            eConfirmPassword.setText("");
            return false;

        }

        if (!(newPassword.equals(confirmPassword))) {

            Toast.makeText(getActivity(), "New passwords don't match", Toast.LENGTH_LONG).show();

            eNewPassword.setText("");
            eConfirmPassword.setText("");
            return false;
        }

        ParseUser cu = ParseUser.getCurrentUser();
        authenticate(cu.getUsername(), oldPassword, newPassword);
        return true;
    }


    /**
     *
     * This method is used to authenticate the user using his new password after updating his password in the database.
     *
     *
     * @param oldPassword The password entered in the old password field.
     * @param newPassword The password entered in the new password field.
     *
     */

    public void authenticate(String username, String oldPassword, final String newPassword) {
        ParseUser.logInInBackground(
                username, oldPassword,
                new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            parseUser.setPassword(newPassword);
                            parseUser.saveEventually(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getActivity(), "Updated Password Successfully", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Unable to update password : " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Authentication Failed")
                                    .setCancelable(true)
                                    .setMessage("Old password is Incorrect!! Password not changed.")
                                    .show();
                        }
                    }
                }
        );
    }


}





