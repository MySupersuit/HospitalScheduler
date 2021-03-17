package com.example.hospitalscheduler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OTInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OTInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NUMBER = "param1";
    private static final String SCHEDULE = "param2";
    private static final String NOTIFIED = "param3";

    private int ot_num;
    private ArrayList<OperationV2> schedule;
    private int isNotified;

    private TextView num_tv;
    private TextView category_tv;
    private TextView stage_tv;
    private TextView surgeon_tv;
    private TextView proc_tv;

    public OTInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param number OT number
//     * @param stage Stage of operation
//     * @param surgeon Head surgeon of operation
//     * @param category_thumb The category thumbnail
//     * @return A new instance of fragment OTInfoFragment.
     */
    public static OTInfoFragment newInstance(int number, ArrayList<OperationV2> schedule, int isNotified) {
        OTInfoFragment fragment = new OTInfoFragment();
        Bundle args = new Bundle();
        args.putInt(NUMBER, number);
        args.putParcelableArrayList(SCHEDULE, schedule);
        args.putInt(NOTIFIED, isNotified);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        final Bundle args = getArguments();

        if (getArguments() != null) {
            ot_num = getArguments().getInt(NUMBER);
            this.schedule = getArguments().getParcelableArrayList(SCHEDULE);
            this.isNotified = getArguments().getInt(NOTIFIED);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ot_info_, container, false);
        this.num_tv = (TextView) view.findViewById(R.id.info_frag_num);
        this.surgeon_tv = (TextView) view.findViewById(R.id.info_frag_surgeon);
        this.category_tv = (TextView) view.findViewById(R.id.info_frag_category);
        this.stage_tv = (TextView) view.findViewById(R.id.info_frag_stage);
        this.proc_tv = (TextView) view.findViewById(R.id.info_frag_procedure);

        surgeon_tv.setText("Surgeon: " + this.schedule.get(0).getSurgeon());
        num_tv.setText("OT " + String.valueOf(this.ot_num));
        category_tv.setText("Category: " + this.schedule.get(0).getCategory());
        stage_tv.setText("Stage: "+ String.valueOf(this.schedule.get(0).getCurrent_stage()));
        proc_tv.setText("Procedure: " + this.schedule.get(0).getProcedure());

        return view;
    }

}