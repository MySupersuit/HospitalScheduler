package com.example.hospitalscheduler.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.hospitalscheduler.fragments.OTInfoFragmentV2;
import com.example.hospitalscheduler.fragments.OTScheduleFragment;
import com.example.hospitalscheduler.objects.OperationV2;
import com.example.hospitalscheduler.R;
import com.example.hospitalscheduler.adapters.OTPagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import static com.example.hospitalscheduler.utilities.Utilites.*;

public class Operation_Info_Tab_Activity extends AppCompatActivity {

    private OTPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation__info__tab_);

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

}