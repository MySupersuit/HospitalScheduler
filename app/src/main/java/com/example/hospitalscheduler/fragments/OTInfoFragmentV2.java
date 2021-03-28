package com.example.hospitalscheduler.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.hospitalscheduler.R;
import com.example.hospitalscheduler.adapters.CommentRecyclerViewAdapter;
import com.example.hospitalscheduler.objects.Comment;
import com.example.hospitalscheduler.objects.OperationV2;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.hospitalscheduler.utilities.Utilites.*;
import static com.example.hospitalscheduler.utilities.Constants.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OTInfoFragmentV2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OTInfoFragmentV2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NUMBER = "param1";
    private static final String SCHEDULE = "param2";
    private static final String NOTIFIED = "param3";
    private static final String OPERATION = "param4";

    private int ot_num;
    private ArrayList<OperationV2> schedule;
    private OperationV2 operation;
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
    ConstraintLayout top_section;
    EditText comment_input;

    ArrayList<Comment> comments;

    TextView first_comment;
    TextView num_of_comments;
    ImageView icon;
    TextView category;
    TextView ot_num_tv, procedure, patient_name;
    ConstraintLayout covid_click, header_layout;
    TextView covid_info_text;
    ImageView covid_icon;
    ScrollView scrollView;
    TextView surgeon_tv;
    ImageView surgeon_icon;

    ConstraintLayout stage1, stage2, stage3, stage4, stage5;


    public OTInfoFragmentV2() {
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
    public static OTInfoFragmentV2 newInstance(int number, OperationV2 operation) {
        OTInfoFragmentV2 fragment = new OTInfoFragmentV2();
        Bundle args = new Bundle();
        args.putInt(NUMBER, number);

        args.putParcelable(OPERATION, operation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ot_num = getArguments().getInt(NUMBER);
            operation = getArguments().getParcelable(OPERATION);
            Log.d("TET", this.operation.toString());
            Log.d("TET", this.operation.getCategory());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mContext = getActivity();

        View view = inflater.inflate(R.layout.fragment_ot_info_v3, container, false);

        mRecyclerView = view.findViewById(R.id.comment_rv);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        comments = new ArrayList<>();
        comments.add(new Comment("Slight delay in prepping but finished now", 10));
        comments.add(new Comment("first", 1));
        comments.add(new Comment("Oh boy this one's dying ngl", 2));
        comments.add(new Comment("lmao", 3));
        comments.add(new Comment("no I'm serious", 4));
        Collections.sort(comments);
        mAdapter = new CommentRecyclerViewAdapter(mContext, comments);

        OperationV2 curr_op = this.operation;

        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        String op_cat = curr_op.getCategory();

        scrollView = view.findViewById(R.id.info_frag_scroll_view);

        icon = view.findViewById(R.id.info_frag_icon);
        icon.setImageResource(categoryToDrawable(op_cat));
        comment_overview = view.findViewById(R.id.info_frag_comment_overview_layout);
        comment_section = view.findViewById(R.id.info_frag_comment_section);
        bottom_info_section = view.findViewById(R.id.info_frag_bottom_section);
//        top_section = view.findViewById(R.id.info_frag_top_info);
        commentBar = view.findViewById(R.id.comment_topbar);
        first_comment = view.findViewById(R.id.first_comment);
        num_of_comments = view.findViewById(R.id.num_of_comments);
        num_of_comments.setText(String.valueOf(comments.size()));
        first_comment.setText(comments.get(0).getContent());
        close_comment_btn = view.findViewById(R.id.close_comment_btn);
        comment_input = view.findViewById(R.id.comment_input);
        category = view.findViewById(R.id.info_frag_category_tv);
        category.setText(op_cat);
        ot_num_tv = view.findViewById(R.id.info_frag_ot_num);
        ot_num_tv.setText("OT " + ot_num);
        patient_name = view.findViewById(R.id.info_frag_patient_name);
        patient_name.setText(curr_op.getPatient_name());
        surgeon_icon = view.findViewById(R.id.info_frag_surgeon_icon);
        surgeon_tv = view.findViewById(R.id.info_frag_surgeon_tv);
        header_layout = view.findViewById(R.id.ot_frag_header_info);

        // This works - would list still be better? depends on amount of staff
//        surgeon_icon.setVisibility(View.GONE);
//        surgeon_tv.setVisibility(View.GONE);

        covid_click = view.findViewById(R.id.info_frag_covid_layout);
        covid_info_text = view.findViewById(R.id.info_frag_covid_info_text);
        covid_icon = view.findViewById(R.id.info_frag_covid_icon);
        int cov_colour = ContextCompat.getColor(mContext, R.color.notif_red);

        if (curr_op.getIsCovid() == 1) {
            covid_icon.setColorFilter(cov_colour);
            covid_info_text.setText("Patient has COVID or is close-contact");
        } else {
            covid_info_text.setText("No COVID information");
            covid_icon.setColorFilter(ContextCompat.getColor(mContext, R.color.grey));
        }

        procedure = view.findViewById(R.id.info_frag_procedure);
        procedure.setText(curr_op.getProcedure());

        stage1 = view.findViewById(R.id.info_frag_1);
        stage2 = view.findViewById(R.id.info_frag_2);
        stage3 = view.findViewById(R.id.info_frag_3);
        stage4 = view.findViewById(R.id.info_frag_4);
        stage5 = view.findViewById(R.id.info_frag_5);

        ConstraintLayout[] stages = {stage1, stage2, stage3, stage4, stage5};
        int cat_colour = Color.parseColor(categoryToColour(curr_op.getCategory()));
        Log.d("CAT", curr_op.getCategory());
        Log.d("COL", categoryToColour(curr_op.getCategory()));
        for (int i = 0; i < curr_op.getCurrent_stage(); i++) {
            switch (i) {
                case 0:
//                    stages[i].setBackgroundColor(cat_colour);
                    Drawable unwrappedDrawable1 = AppCompatResources.getDrawable(getContext(), R.drawable.left_rounded);
                    Drawable wrappedDrawable1 = DrawableCompat.wrap(unwrappedDrawable1);
                    DrawableCompat.setTint(wrappedDrawable1, cat_colour);
                    stages[i].setBackground(wrappedDrawable1);
                    break;
                case 4:
//                    stages[i].setBackgroundColor(cat_colour);
                    Drawable unwrappedDrawable2 = AppCompatResources.getDrawable(getContext(), R.drawable.right_rounded);
                    Drawable wrappedDrawable2 = DrawableCompat.wrap(unwrappedDrawable2);
                    DrawableCompat.setTint(wrappedDrawable2, cat_colour);
                    stages[i].setBackground(wrappedDrawable2);
                    break;
                default:
                    stages[i].setBackgroundColor(cat_colour);
                    break;
            }
        }
        int off_white = ContextCompat.getColor(mContext, R.color.off_white);
        for (int i = NUM_STAGES - 1; i >= curr_op.getCurrent_stage(); i--) {
            switch (i) {
                case 0:
                    Drawable unwrappedDrawable1 = AppCompatResources.getDrawable(getContext(), R.drawable.left_rounded);
                    Drawable wrappedDrawable1 = DrawableCompat.wrap(unwrappedDrawable1);
                    DrawableCompat.setTint(wrappedDrawable1, off_white);
                    stages[i].setBackground(wrappedDrawable1);
                    break;
                case 4:
                    Drawable unwrappedDrawable2 = AppCompatResources.getDrawable(getContext(), R.drawable.right_rounded);
                    Drawable wrappedDrawable2 = DrawableCompat.wrap(unwrappedDrawable2);
                    DrawableCompat.setTint(wrappedDrawable2, off_white);
                    stages[i].setBackground(wrappedDrawable2);
                    break;
                default:
                    stages[i].setBackgroundColor(off_white);
            }
        }

        header_layout.setBackgroundColor(cat_colour);

        // Keyboard comment enter handler
        comment_input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String text = comment_input.getText().toString();
                comment_input.setText("");
                comments.add(new Comment(text, 50));
                Collections.sort(comments);
                mAdapter.notifyDataSetChanged();
                return true;
            }
            return false;
        });

        // onClick handlers
        comment_overview.setOnClickListener(v -> handleCommentOverviewClick(container));
        close_comment_btn.setOnClickListener(v -> handleCommentsClose(container));
        covid_click.setOnClickListener(v -> handleCovidClick(container));

        return view;
    }

    private void handleCommentOverviewClick(ViewGroup v) {
        scrollView.fullScroll(ScrollView.FOCUS_UP);

        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(300);
        transition.addTarget(comment_section);

        TransitionManager.beginDelayedTransition(v, transition);
        comment_section.setVisibility(View.VISIBLE);

    }

    private void handleCommentsClose(ViewGroup v) {
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(200);
        transition.addTarget(comment_section);

        TransitionManager.beginDelayedTransition(v, transition);
        comment_section.setVisibility(View.GONE);
    }


    private void handleCovidClick(ViewGroup v) {
        if (covid_info_text.getVisibility() == View.GONE) {
            Transition transition = new Slide(Gravity.END);
            transition.setDuration(200);
            transition.addTarget(covid_info_text);

            TransitionManager.beginDelayedTransition(v, transition);
            covid_info_text.setVisibility(View.VISIBLE);

        } else if (covid_info_text.getVisibility() == View.VISIBLE) {
            Transition transition = new Slide(Gravity.START);
            transition.setDuration(200);
            transition.addTarget(covid_info_text);

            TransitionManager.beginDelayedTransition(v, transition);
            covid_info_text.setVisibility(View.GONE);
        }
    }
}