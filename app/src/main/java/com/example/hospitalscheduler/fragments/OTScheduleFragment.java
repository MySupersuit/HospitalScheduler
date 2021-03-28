package com.example.hospitalscheduler.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hospitalscheduler.R;
import com.example.hospitalscheduler.adapters.ScheduleRecyclerViewAdapter;
import com.example.hospitalscheduler.objects.OperationV2;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OTScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OTScheduleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SCHEDULE = "param3";
    private static final String OT_NUM = "param1";

    // TODO: Rename and change types of parameters
    private ArrayList<OperationV2> schedule;
    private int ot_num;

    private RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private ScheduleRecyclerViewAdapter mAdapter;
    private Context mContext;

    private TextView sched_header_tv;

    public OTScheduleFragment() {
        // Required empty public constructor
    }

    public static OTScheduleFragment newInstance(int ot_num, ArrayList<OperationV2> schedule) {
        OTScheduleFragment fragment = new OTScheduleFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(SCHEDULE, schedule);
        args.putInt(OT_NUM, ot_num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            schedule = getArguments().getParcelableArrayList(SCHEDULE);
            this.ot_num = getArguments().getInt(OT_NUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ot_schedule_v2, container, false);
        mContext = getContext();

        this.mRecyclerView = view.findViewById(R.id.sched_frag_recycler_view);
        this.sched_header_tv = view.findViewById(R.id.sched_frag_header_tv);
        this.sched_header_tv.setText("OT " + this.ot_num);

        this.mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        schedule.add(0, new OperationV2());
        schedule.add(0,new OperationV2());
        schedule.add(0,new OperationV2());
        this.mAdapter = new ScheduleRecyclerViewAdapter(mContext, schedule);

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}