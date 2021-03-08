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

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

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

        Intent intent = getIntent();
        int ot_num = intent.getExtras().getInt("Number");
        int ot_stage = intent.getExtras().getInt("Stage");
        String surgeon_name = intent.getExtras().getString("Surgeon");
        String start_time = intent.getExtras().getString("Time");
        int category_thumb = intent.getExtras().getInt("Category");
        String curr_op = intent.getExtras().getString("Operation");

        OTInfoFragment in_info_frag = OTInfoFragment.newInstance(ot_num, ot_stage, surgeon_name,
                category_thumb, curr_op);

//        OTInfoFragment info_frag = OTInfoFragment.newInstance(String.valueOf(ot_num), surgeon_name);

        OTScheduleFragment sched_frag = OTScheduleFragment.newInstance(ot_num, start_time);

        pagerAdapter.add(in_info_frag, "Info");
        pagerAdapter.add(sched_frag, "Schedule");

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }
}