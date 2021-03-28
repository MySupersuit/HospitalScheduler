package com.example.hospitalscheduler.activities;

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
import com.google.android.material.snackbar.Snackbar;

public class loginActivity extends AppCompatActivity {

    //Variables needed for the login screen

    //Login Button on screen
    private Button loginButton;

    //Number of attempts displayed on the login screen
    private TextView numberOfAttempts;

    // Username and password entered
    private EditText username;
    private EditText userPassword;

    //Actual credentials(username, password) used for comparison
    //final = cannot change the value
    private final String sampleUsername = "12345";
    private final String samplePassword = "password";

    // Boolean to validate the credentials - check if true or false
    boolean isCorrectUsernameAndPassword= false; //set false by default

    //Total number of attempts allowed
    private int countAttempts = 3;

    //Layout
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //XML variable linked to Java variable for each element

        //Login button
        loginButton = findViewById(R.id.confirmLoginbutton);
        //Number of attempts text
        numberOfAttempts = findViewById(R.id.textViewNumberOfAttempts);

        //Retrieve the username and password entered
        username = findViewById(R.id.editTextHospitalUsername);
        userPassword = findViewById(R.id.editTextAppPassword);

        //Layout from XML
        layout = findViewById(R.id.loginLayout);

        // For now - the login button moves to the Overview screen
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Transform the username into String format, ent = entered
                String entUsername = username.getText().toString();
                // Repeat the String transformation to the password
                String entPassword = userPassword.getText().toString();

                //Check if the user input is empty - username or password
                if( entUsername.isEmpty() || entPassword.isEmpty())
                {
                    //Show an error message if either the username or password is empty
                    Toast.makeText(loginActivity.this,"Empty username/password/both. Please try again.",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    //Now check the entered credentials
                    isCorrectUsernameAndPassword = correctCredentials(entUsername,entPassword);

                    //If the boolean - correct is false
                    if(isCorrectUsernameAndPassword == false)
                    {
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
                            Snackbar msgZeroAttempts = Snackbar.make(v,"Contact it@thehospital.ie",Snackbar.LENGTH_INDEFINITE);
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
                    //If the boolean - correct is true
                    //Correct credentials
                    else
                    {
                        //Change screen to overview IF the credentials are correct
                        Intent toOverview = new Intent(loginActivity.this, OverviewActivity.class);
                        //Switch screen
                        startActivity(toOverview);
                        //Prevent user from going back to the login screen
                        finish(); //Removes the login activity from the back stack

                    }


                }


            }
        });
    }

    /* Function to check if the username and password are correct
     * Returns true or false
     */

    private boolean correctCredentials(String u, String p)
    {
        //If both the username and(&&) the password match, return true
        if(u.equals(sampleUsername) && p.equals(samplePassword))
        {
            //Both are correct
            return true;
        }
        return false; //are incorrect
    }
}