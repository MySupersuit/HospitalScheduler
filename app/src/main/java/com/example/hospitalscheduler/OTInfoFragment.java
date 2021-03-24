package com.example.hospitalscheduler;

import android.animation.LayoutTransition;
import android.content.Context;
import android.drm.DrmStore;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ActionMenuView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static com.example.hospitalscheduler.Utilites.*;

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

    private ConstraintLayout comment_overview;
    private RecyclerView mRecyclerView;
    CommentRecyclerViewAdapter mAdapter;
    Context mContext;
    RecyclerView.LayoutManager mLayoutManager;
    ConstraintLayout commentBar;
    ImageButton close_comment_btn;
    ConstraintLayout comment_section;
    ConstraintLayout bottom_info_section;
    EditText comment_input;
    ArrayList<String> comments;
    TextView first_comment;
    TextView num_of_comments;
    ImageView icon;
    TextView category;
    TextView ot_num_tv, procedure, patient_name;


    ConstraintLayout stage1, stage2, stage3, stage4, stage5;

    public OTInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //     * @param number OT number
     * //     * @param stage Stage of operation
     * //     * @param surgeon Head surgeon of operation
     * //     * @param category_thumb The category thumbnail
     * //     * @return A new instance of fragment OTInfoFragment.
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

        mContext = getActivity();

        View view = inflater.inflate(R.layout.fragment_ot_info_v2, container, false);

        mRecyclerView = view.findViewById(R.id.comment_rv);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        comments = new ArrayList<>();
        comments.add("Slight delay in prepping but finished now");
        comments.add("first");
        comments.add("Oh boy this one's dying ngl");
        comments.add("lmao");
        comments.add("no I'm serious");
        mAdapter = new CommentRecyclerViewAdapter(mContext, comments);

        OperationV2 curr = this.schedule.get(0);

        mRecyclerView.setAdapter(mAdapter);

        String op_cat = curr.getCategory();

        icon = view.findViewById(R.id.info_frag_icon);
        icon.setImageResource(categoryToDrawable(op_cat));
        comment_overview = view.findViewById(R.id.info_frag_comment_overview_layout);
        comment_section = view.findViewById(R.id.info_frag_comment_section);
        bottom_info_section = view.findViewById(R.id.info_frag_bottom_section);
        commentBar = view.findViewById(R.id.comment_topbar);
        first_comment = view.findViewById(R.id.first_comment);
        num_of_comments = view.findViewById(R.id.num_of_comments);
        num_of_comments.setText(String.valueOf(comments.size()));
        first_comment.setText(comments.get(0));
        close_comment_btn = view.findViewById(R.id.close_comment_btn);
        comment_input = view.findViewById(R.id.comment_input);
        category = (TextView) view.findViewById(R.id.info_frag_category_tv);
        category.setText(op_cat);
        ot_num_tv = view.findViewById(R.id.info_frag_ot_num);
        ot_num_tv.setText("OT " + ot_num);
        patient_name = view.findViewById(R.id.info_frag_patient_name);
        patient_name.setText(curr.getPatient_name());

        procedure = view.findViewById(R.id.info_frag_procedure);
        procedure.setText(curr.getProcedure());

        stage1 = view.findViewById(R.id.info_frag_1);
        stage2 = view.findViewById(R.id.info_frag_2);
        stage3 = view.findViewById(R.id.info_frag_3);
        stage4 = view.findViewById(R.id.info_frag_4);
        stage5 = view.findViewById(R.id.info_frag_5);

        ConstraintLayout[] stages = {stage1, stage2, stage3, stage4, stage5};
        String colour = categoryToColour(curr.getCategory());
        for (int i = 0; i < curr.getCurrent_stage(); i++) {
            stages[i].setBackgroundColor(Color.parseColor(colour));
        }



        comment_input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String text = comment_input.getText().toString();
                comment_input.setText("");
                ArrayList<String> new_comments = new ArrayList<>(comments);
                new_comments.add(text);
                comments.clear();
                comments.addAll(new_comments);
                mAdapter.notifyDataSetChanged();

                return true;
            }
            return false;
        });

        comment_overview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("1", "click");
                Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                        R.anim.slide_in_bottom);
                bottomUp.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        bottom_info_section.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                comment_section.startAnimation(bottomUp);
                comment_section.setVisibility(View.VISIBLE);
//                bottom_info_section.setVisibility(View.GONE);

            }
        });

        close_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("2", "click");
                Animation topDown = AnimationUtils.loadAnimation(getContext(),
                        R.anim.slide_out_bottom);

                comment_section.startAnimation(topDown);

                comment_section.setVisibility(View.GONE);
                bottom_info_section.setVisibility(View.VISIBLE);

            }
        });

        return view;

    }

}