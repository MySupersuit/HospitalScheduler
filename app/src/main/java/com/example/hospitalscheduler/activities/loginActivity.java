package com.example.hospitalscheduler.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalscheduler.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {

    //Variables needed for the login screen

    //Login Button on screen
    private Button loginButton;

    //Number of attempts displayed on the login screen
    private TextView numberOfAttempts;

    // Email and password entered
    //private EditText username;
    private EditText userEmailAddress;
    private EditText userPassword;

    //Actual credentials(username, password) used for comparison
    //final = cannot change the value
    //private final String sampleUsername = "12345";
    //private final String samplePassword = "password";

    // Boolean to validate the credentials - check if true or false
    boolean isCorrectUsernameAndPassword= false; //set false by default

    //Total number of attempts allowed
    private int countAttempts = 3;

    //Layout
    ConstraintLayout layout;

    //For Firebase authentication
    private FirebaseAuth firebaseAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Firebase Authentication checks if user is logged in or not
        //If user is signed in = display Overview Screen
        //If user is signed out show Login screen

        //Firebase Authentication in app
        firebaseAuthentication = FirebaseAuth.getInstance();

        //User currently SIGNED-IN user using the app
        FirebaseUser currentUser = firebaseAuthentication.getCurrentUser();

        //If the user is signed in = show the Overview screen
        if(currentUser!=null)
        {
            finish(); //get rid of the Login Activity
            //Used to change to Overview screen directly
            Intent skip = new Intent(this, OverviewActivity.class);
            //Launch Overview screen
            this.startActivity(skip);


        }
        // User is signed out = show login screen
        else
        {
            setContentView(R.layout.activity_login);

            //Link the Java Variables to XML - call function
            xmlToJava();




            // For now - the login button moves to the Overview screen
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Transform the username into String format, ent = entered
                    String entUserEmailAdd = userEmailAddress.getText().toString();
                    // Repeat the String transformation to the password
                    String entPassword = userPassword.getText().toString();

                    //Check if the user input is empty - user email or password
                    if( entUserEmailAdd.isEmpty() || entPassword.isEmpty())
                    {
                        //Show an error message if either the username or password is empty
                        Toast.makeText(loginActivity.this,"Empty user email/password/both. Please try again.",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        //Check if the credentials match the database credentials
                        // Also pass the view for the snackbar message
                        correctCredentials(entUserEmailAdd,entPassword,v);


                    }


                }
            });

        }


    }

    /* Function to check if the user email and password are correct
     *
     */

    private void correctCredentials(String ue, String p, View view)
    {
       // Sign-in a user with the email and password
        // Listener checks that task has completed successfully
        firebaseAuthentication.signInWithEmailAndPassword(ue,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    //Correct credentials
                    //Notify the user with a message
                    Toast.makeText(loginActivity.this,"Logging in...",Toast.LENGTH_SHORT).show();


                    //User can move to the Overview screen
                    //Change screen to overview IF the credentials are correct
                    Intent toOverview = new Intent(loginActivity.this, OverviewActivity.class);
                    //Switch screen
                    startActivity(toOverview);
                    //Prevent user from going back to the login screen
                    finish(); //Removes the login activity from the back stack
                }
                else
                {
                    //If authentication did not work - database
                    // Incorrect credentials
                    //Reduce the number of remaining attempts by 1 on the login screen
                    //Incorrect credentials
                    countAttempts--;

                    //Update the screen count of attempts with current (reduced) count
                    numberOfAttempts.setText("Number of attempts left: "+countAttempts);


                    //Once no attempts are left
                    if(countAttempts == 0) {
                        //Disable the login button
                        loginButton.setEnabled(false);
                        //Change the login button to grey to indicate that the button is disabled
                        loginButton.setBackgroundColor(Color.GRAY);

                        //Security Message 2
                        //Snackbar message to user
                        //Indefinite length
                        //View parameter
                        Snackbar msgZeroAttempts = Snackbar.make(view,"Contact it@thehospital.ie",Snackbar.LENGTH_INDEFINITE);
                        msgZeroAttempts.setAction("CLOSE", new View.OnClickListener() { //CLOSE text
                            @Override
                            public void onClick(View v) {
                                //If user presses close - message is dismissed
                                msgZeroAttempts.dismiss();
                            }
                        }); // end of action
                        //Display the button
                        msgZeroAttempts.show();

                    }else{ //Counts are not equal to 0
                        //Security Message 1 - invalid username and password
                        //Length = short
                        Toast toast_attemptsNotZero = Toast.makeText(loginActivity.this,"Incorrect username/password/both. Please try again",Toast.LENGTH_SHORT);
                        //Display the message
                        toast_attemptsNotZero.show();
                    }

                }

            }
        }); // end of createUserWithEmailAndPassword - listener
    }

    // Method to assign XML layout variables to Java variables
    private void xmlToJava()
    {
        //User email address
        userEmailAddress = (EditText)findViewById(R.id.editTextHospitalEmail);
        //User password
        userPassword = (EditText)findViewById(R.id.editTextAppPassword);
        //Login button
        loginButton = findViewById(R.id.confirmLoginbutton);

        //Number of attempts text
        numberOfAttempts = findViewById(R.id.textViewNumberOfAttempts);
        //Layout from XML
        layout = findViewById(R.id.loginLayout);


    }
}