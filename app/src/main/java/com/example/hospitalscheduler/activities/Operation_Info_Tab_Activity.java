package com.example.hospitalscheduler.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hospitalscheduler.fragments.OTInfoFragmentV2;
import com.example.hospitalscheduler.fragments.OTScheduleFragment;
import com.example.hospitalscheduler.objects.OperationV2;
import com.example.hospitalscheduler.R;
import com.example.hospitalscheduler.adapters.OTPagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static com.example.hospitalscheduler.utilities.Utilites.*;

public class Operation_Info_Tab_Activity extends AppCompatActivity {

    private OTPagerAdapter pagerAdapter;

    //Added for Sign Out - For Firebase authentication
    private FirebaseAuth firebaseAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation__info__tab_);

        //Added for Sign Out - Firebase Authentication in app
        firebaseAuthentication = FirebaseAuth.getInstance();

        TabLayout tabLayout = findViewById(R.id.op_info_tabLayout);
        TabItem tabInfo = findViewById(R.id.op_info_tab);
        TabItem tabSchedule = findViewById(R.id.op_schedule_tab);
        ViewPager viewPager = findViewById(R.id.info_view_pager);

        pagerAdapter = new OTPagerAdapter(getSupportFragmentManager());

        ArrayList<OperationV2> schedule = new ArrayList<>();
        int ot_num = -1;
//        int notified = -1;
        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            schedule = intent.getParcelableArrayListExtra("Schedule");
            ot_num = intent.getExtras().getInt("Number");
//            notified = intent.getExtras().getInt("Notified");
        }

        // just pass current operation

        OperationV2 current_op = getCurrentOperation(schedule);
        if (current_op == null) {
            current_op = new OperationV2(ot_num);
        }

        OTInfoFragmentV2 curr_info_frag = OTInfoFragmentV2.newInstance(ot_num, current_op);
        pagerAdapter.add(curr_info_frag, "Current");


        // just pass next operation

        OperationV2 next_op = getNextOperation(schedule, current_op);
        if (next_op == null) {
            next_op = new OperationV2(ot_num);
        }

        OTInfoFragmentV2 next_info_frag = OTInfoFragmentV2.newInstance(ot_num, next_op);
        pagerAdapter.add(next_info_frag, "Next");


        // pass full schedule
        OTScheduleFragment sched_frag = OTScheduleFragment.newInstance(ot_num, schedule);
        pagerAdapter.add(sched_frag, "Schedule");

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /*
    * Function to add a sign out option to tool bar
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Refer to sign out menu file - create menu
        getMenuInflater().inflate(R.menu.sign_out_for_info_screen,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //For clicking on the items
       switch(item.getItemId())
       {
           case R.id.signOutOptionInfo:
               signOutUser(); //function called to sign out the user

       }
       return super.onOptionsItemSelected(item);
    }

    /*
     * Function to Sign Out the user
     */
    private void signOutUser()
    {
        //Signs Out the (current) user
        firebaseAuthentication.signOut();
        finish(); //activity is finished
        // Login Activity relaunched
        startActivity(new Intent(Operation_Info_Tab_Activity.this, loginActivity.class));

    }
}