package com.example.adminstrator.salesdiary3;

/**
 * Created by Richard on 6/16/2016.
 */

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;


public class Login extends AppCompatActivity {
    private final String USERNAME = "USERNAME";
    private final String SALES_RECORD_TABLE = "SALES RECORD TABLE";
    private AlertDialog.Builder infoDialog;
    private AlertDialog.Builder errorDialog;
    private AlertDialog.Builder confirmDialog;
    private String salesRecordName;
    @BindView(R.id.username2)
    EditText username;
    @BindView(R.id.password2)
    EditText password;
    @BindView(R.id.Login2)
    Button login;
    @BindView(R.id.signup2)
    Button signup;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        final databaseManager db = new databaseManager(this);

        login.setOnClickListener((view) -> {
            final String uname = username.getText().toString().trim();
            final String PASSWORD = password.getText().toString().trim();
            salesRecordName = username.getText().toString().trim() + getString(R.string.sales_record_suffix);

            if (!((uname.equals("")) || uname.length() <= 1) || (PASSWORD.equals("") || PASSWORD.length() <= 1)) {
                if (db.getPassword(uname).equals(getString(R.string.invalid_user_message))) {
                    errorDialog = new AlertDialog.Builder(Login.this);
                    errorDialog.setTitle(getString(R.string.error_message));
                    errorDialog.setIcon(R.mipmap.errorwhite);
                    errorDialog.setMessage(R.string.invalid_user_message);
                    errorDialog.setPositiveButton(getString(R.string.closebtn), null);
                    errorDialog.show();
                }
                if (PASSWORD.equals(db.getPassword(uname))) {
                    infoDialog = new AlertDialog.Builder(Login.this);
                    infoDialog.setTitle(getString(R.string.info_dialog_title));
                    infoDialog.setIcon(R.mipmap.infowhite);
                    infoDialog.setMessage(getString(R.string.info_dialog_message));
                    infoDialog.setPositiveButton(getString(R.string.Okbtn), (dialog, id) -> {
                        if (db.getCountOfProductsInCatalog(uname) > 0) {
                            Intent gotoMainActivity = new Intent(Login.this, MainActivity.class);
                            gotoMainActivity.putExtra(USERNAME, uname);
                            gotoMainActivity.putExtra(SALES_RECORD_TABLE, salesRecordName);
                            startActivity(gotoMainActivity);

                        } else {
                            AlertDialog.Builder exceptionBox = new AlertDialog.Builder(Login.this);
                            exceptionBox.setTitle(getString(R.string.error_dialog_title));
                            exceptionBox.setIcon(R.mipmap.errorwhite);
                            exceptionBox.setMessage(getString(R.string.error_dialog_message));
                            exceptionBox.setPositiveButton(getString(R.string.Okbtn), (dialog1, id1) ->
                            {
                                AlertDialog.Builder confirmBox = new AlertDialog.Builder(Login.this);
                                confirmBox.setTitle(getString(R.string.confirm_dialog_title));
                                confirmBox.setIcon(R.mipmap.confirm2);
                                confirmBox.setMessage(getString(R.string.confirm_dialog_message));
                                confirmBox.setPositiveButton(getString(R.string.yesbtn), (dialog2, id2) ->
                                {

                                    Intent mainActivity = new Intent(Login.this, MainActivity.class);
                                    mainActivity.putExtra(USERNAME, uname);
                                    mainActivity.putExtra(SALES_RECORD_TABLE, salesRecordName);
                                    startActivity(mainActivity);
                                });
                                confirmBox.setNegativeButton(getString(R.string.nobtn), null);
                                confirmBox.show();
                            });
                            exceptionBox.show();
                        }
                        //clear the username and password values
                        clear(username, password);
                    });
                    infoDialog.show();
                } else {
                    errorDialog = new AlertDialog.Builder(Login.this);
                    errorDialog.setTitle(getString(R.string.error_dialog_title));
                    errorDialog.setIcon(R.mipmap.errorwhite);
                    errorDialog.setMessage(getString(R.string.error_password_message));
                    errorDialog.setPositiveButton(getString(R.string.closebtn), null);
                    errorDialog.show();
                }
            } else {
                errorDialog = new AlertDialog.Builder(Login.this);
                errorDialog.setTitle(getString(R.string.error_dialog_title));
                errorDialog.setIcon(R.mipmap.errorwhite);
                errorDialog.setMessage(getString(R.string.error_login_message));
                errorDialog.setPositiveButton(getString(R.string.closebtn), (dialog, id) -> dialog.dismiss());
                errorDialog.show();

            }
        });

        signup.setOnClickListener((view) -> {
            confirmDialog = new AlertDialog.Builder(Login.this);
            confirmDialog.setTitle(getString(R.string.confirm_dialog_title));
            confirmDialog.setIcon(R.mipmap.confirm2);
            confirmDialog.setMessage(getString(R.string.confirm_question_message));
            confirmDialog.setPositiveButton(getString(R.string.yesbtn), (dialog, id) ->
            {
                Intent gotoSignup = new Intent(Login.this, signUp.class);
                clear(username, password);
                startActivity(gotoSignup);
            });
            confirmDialog.setNegativeButton(getString(R.string.nobtn), null);
            confirmDialog.show();
        });

    }

    @Override
    public void onConfigurationChanged(final Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
    }

    public void clear(EditText... editTexts) {
        //clear all editTet arguments
        for (EditText editText : editTexts) {
            editText.setText("");
        }
    }
}
