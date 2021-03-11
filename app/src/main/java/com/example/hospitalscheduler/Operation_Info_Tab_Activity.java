package com.example.hospitalscheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

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

        ArrayList<Operation> schedule = new ArrayList<>();
        int ot_num = -1;
        int notified = -1;
        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            schedule = intent.getParcelableArrayListExtra("Schedule");
            ot_num = intent.getExtras().getInt("Number");
            notified = intent.getExtras().getInt("Notified");
        }

        OTInfoFragment info_frag = OTInfoFragment.newInstance(ot_num, schedule, notified);
        pagerAdapter.add(info_frag, "Info");

        OTScheduleFragment sched_frag = OTScheduleFragment.newInstance(ot_num, schedule.get(0).getStartTime());
        pagerAdapter.add(sched_frag, "Schedule");

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }
}