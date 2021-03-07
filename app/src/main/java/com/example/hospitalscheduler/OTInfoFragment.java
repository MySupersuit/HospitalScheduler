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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OTInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OTInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SURGEON = "param1";
    private static final String CATEGORY = "param2";
    private static final String STAGE = "param3";
    private static final String NUMBER = "param4";
    private static final String PROCEDURE = "param5";

    // TODO: Rename and change types of parameters
    private String surgeon;
    private int category_thumb;
    private int stage;
    private int ot_num;
    private String procedure;

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
     * @param number OT number
     * @param stage Stage of operation
     * @param surgeon Head surgeon of operation
     * @param category_thumb The category thumbnail
     * @return A new instance of fragment OTInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OTInfoFragment newInstance(int number, int stage, String surgeon, int category_thumb, String curr_op) {
        OTInfoFragment fragment = new OTInfoFragment();
        Bundle args = new Bundle();
        args.putInt(NUMBER, number);
        args.putInt(STAGE, stage);
        args.putString(SURGEON, surgeon);
        args.putInt(CATEGORY, category_thumb);
        args.putString(PROCEDURE, curr_op);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        final Bundle args = getArguments();

        if (getArguments() != null) {
            ot_num = getArguments().getInt(NUMBER);
            surgeon = getArguments().getString(SURGEON);
            category_thumb = getArguments().getInt(CATEGORY);
            stage = getArguments().getInt(STAGE);
            procedure = getArguments().getString(PROCEDURE);
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

        surgeon_tv.setText("Surgeon: " + this.surgeon);
        num_tv.setText("OT " + String.valueOf(this.ot_num));
        category_tv.setText("Category: " + getCategoryGivenCategoryThumb(this.category_thumb));
        stage_tv.setText("Stage: "+String.valueOf(this.stage));
        proc_tv.setText("Procedure: " + this.procedure);


        return view;
    }

    private String getCategoryGivenCategoryThumb(int id) {
        switch (id) {
            case R.drawable.humanbrain:
                return "Neuro";
            case R.drawable.ent_icon:
                return "ENT";
            case R.drawable.intestine_icon:
                return "Colorectal";
            case R.drawable.vascularicon:
                return "Vascular";
            case R.drawable.ortho_icon:
                return "Orthopedic";
            default:
                return null;
        }
    }
}