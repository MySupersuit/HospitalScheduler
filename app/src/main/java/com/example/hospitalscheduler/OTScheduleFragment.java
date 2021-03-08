package com.example.hospitalscheduler;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OTScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OTScheduleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TIME = "param1";
    private static final String NUMBER = "param2";

    // TODO: Rename and change types of parameters
    private String start_time;
    private int ot_num;

    private TextView num_tv;
    private TextView start_time_tv;

    public OTScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OTScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OTScheduleFragment newInstance(int ot_num, String start_time) {
        OTScheduleFragment fragment = new OTScheduleFragment();
        Bundle args = new Bundle();
        args.putString(TIME, start_time);
        args.putInt(NUMBER, ot_num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            start_time = getArguments().getString(TIME);
            ot_num = getArguments().getInt(NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_o_t_schedule, container, false);
        this.num_tv = view.findViewById(R.id.sched_frag_num);
        this.start_time_tv = view.findViewById(R.id.sched_frag_start_time);

        num_tv.setText("OT " + String.valueOf(this.ot_num));
        start_time_tv.setText("Start time: " + start_time);

        return view;
    }
}