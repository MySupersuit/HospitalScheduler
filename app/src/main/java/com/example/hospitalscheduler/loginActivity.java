package com.example.hospitalscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private final String sampleUsername = "surgeon";
    private final String samplePassword = "password";

    // Boolean to validate the credentials - check if true or false
    boolean isCorrectUsernameAndPassword= false; //set false by default

    //Total number of attempts allowed
    private int countAttempts = 3;

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
                        if(countAttempts == 0)
                        {
                            //Disable the login button
                            loginButton.setEnabled(false);
                        }
                    }
                    //If the boolean - correct is true
                    //Correct credentials
                    else
                    {
                        //Change screen to overview IF the credentials are correct
                        Intent toOverview = new Intent(loginActivity.this,Overview.class);
                        //Switch screen
                        startActivity(toOverview);
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