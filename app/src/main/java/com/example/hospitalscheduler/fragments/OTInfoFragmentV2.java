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

import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.hospitalscheduler.R;
import com.example.hospitalscheduler.adapters.CommentRecyclerViewAdapter;
import com.example.hospitalscheduler.objects.Comment;
import com.example.hospitalscheduler.objects.OperationV2;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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

    List<Comment> comments;

    TextView first_comment;
    TextView first_comment_time;
    TextView num_of_comments, num_of_comments_expand;
    ImageView icon;
    TextView category;
    TextView ot_num_tv, procedure, patient_name;
    ConstraintLayout covid_click, header_layout;
    TextView covid_info_text;
    ImageView covid_icon;
    ScrollView scrollView;
    TextView surgeon_tv, nurse_tv, anesth_tv, registrar_tv;
    ConstraintLayout surgeon, nurse, anesth, registrar;
    ImageView surgeon_icon;
    TextView staff_title;
    ConstraintLayout stage1, stage2, stage3, stage4, stage5;
    TextView tv1, tv2, tv3, tv4, tv5;


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

        OperationV2 curr_op = this.operation;

        comments = new ArrayList<>(curr_op.comments);
        Collections.sort(comments);

        mAdapter = new CommentRecyclerViewAdapter(mContext, comments);
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
        num_of_comments_expand = view.findViewById(R.id.num_of_comments_expand);
        num_of_comments_expand.setText(String.valueOf(comments.size()));
        first_comment_time = view.findViewById(R.id.first_comment_time);
        if (comments.size() > 0) {
            first_comment.setText(comments.get(0).getContent());
            first_comment_time.setText(epochTimeToHourMin(comments.get(0).getTime()));
        } else {
            first_comment.setText(getString(R.string.no_comments));
        }

        close_comment_btn = view.findViewById(R.id.close_comment_btn);
        comment_input = view.findViewById(R.id.comment_input);
        comment_input.setRawInputType(InputType.TYPE_CLASS_TEXT);
        category = view.findViewById(R.id.info_frag_category_tv);
        category.setText(op_cat);
        ot_num_tv = view.findViewById(R.id.info_frag_ot_num);
        ot_num_tv.setText("OT " + ot_num);
        patient_name = view.findViewById(R.id.info_frag_patient_name);
        patient_name.setText(curr_op.getPatient_name());
        surgeon_icon = view.findViewById(R.id.info_frag_surgeon_icon);

        staff_title = view.findViewById(R.id.info_frag_staff_title);

        surgeon_tv = view.findViewById(R.id.info_frag_surgeon_tv);
        surgeon_tv.setText(curr_op.getSurgeon());
        surgeon = view.findViewById(R.id.info_frag_surgeon);
        nurse_tv = view.findViewById(R.id.info_frag_nurse_tv);
        nurse_tv.setText(curr_op.getScrubNurse());
        nurse = view.findViewById(R.id.info_frag_nurse);
        anesth_tv = view.findViewById(R.id.info_frag_anesth_tv);
        anesth_tv.setText(curr_op.getAnaesthetist());
        anesth = view.findViewById(R.id.info_frag_anesth);
        registrar_tv = view.findViewById(R.id.info_frag_registrar_tv);
        registrar_tv.setText(curr_op.getRegistrar());
        registrar = view.findViewById(R.id.info_frag_registrar);

        header_layout = view.findViewById(R.id.ot_frag_header_info);

        // if no operation
        if (operation.getCategory().equals(NO_OP)) {
            surgeon.setVisibility(View.GONE);
            nurse.setVisibility(View.GONE);
            anesth.setVisibility(View.GONE);
            registrar.setVisibility(View.GONE);
            comment_overview.setVisibility(View.GONE);
            staff_title.setText(NO_OP);
        }

        covid_click = view.findViewById(R.id.info_frag_covid_layout);
        covid_info_text = view.findViewById(R.id.info_frag_covid_info_text);
        covid_icon = view.findViewById(R.id.info_frag_covid_icon);
        int cov_colour = ContextCompat.getColor(mContext, R.color.notif_red);

        if (curr_op.getIsCovid() == 1) {
            covid_icon.setColorFilter(cov_colour);
            covid_info_text.setText(R.string.COVID_message);
        } else {
            covid_info_text.setText(R.string.no_COVID_message);
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
        for (int i = 0; i < curr_op.getCurrent_stage()-1; i++) {
            switch (i) {
                case 0:
//                    stages[i].setBackgroundColor(cat_colour);
                    Drawable unwrappedDrawable1 = AppCompatResources.getDrawable(getContext(), R.drawable.left_rounded);
                    Drawable wrappedDrawable1 = DrawableCompat.wrap(unwrappedDrawable1);
                    DrawableCompat.setTint(wrappedDrawable1, cat_colour);
                    stages[i].setBackground(wrappedDrawable1);
                    break;
                case (NUM_STAGES-1):
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
                case NUM_STAGES:
                    Drawable unwrappedDrawable2 = AppCompatResources.getDrawable(getContext(), R.drawable.right_rounded);
                    Drawable wrappedDrawable2 = DrawableCompat.wrap(unwrappedDrawable2);
                    DrawableCompat.setTint(wrappedDrawable2, off_white);
                    stages[i].setBackground(wrappedDrawable2);
                    break;
                default:
                    stages[i].setBackgroundColor(off_white);
            }
        }

        tv1 = view.findViewById(R.id.info_frag_1_tv);
        tv2 = view.findViewById(R.id.info_frag_2_tv);
        tv3 = view.findViewById(R.id.info_frag_3_tv);
        tv4 = view.findViewById(R.id.info_frag_4_tv);
        tv5 = view.findViewById(R.id.info_frag_5_tv);
        TextView[] stageTvs = {tv1, tv2, tv3, tv4, tv5};

        int sec_colour = Color.parseColor(categoryToSecColour(operation.getCategory()));
        int white = ContextCompat.getColor(mContext, R.color.white);
        switch (operation.getCurrent_stage()) {
            case 1:
                Drawable unwrappedDrawable1 = AppCompatResources.getDrawable(getContext(), R.drawable.left_rounded);
                Drawable wrappedDrawable1 = DrawableCompat.wrap(unwrappedDrawable1);
                DrawableCompat.setTint(wrappedDrawable1, sec_colour);
                stages[0].setBackground(wrappedDrawable1);
                stageTvs[0].setTextColor(white);
                break;
            case NUM_STAGES:
                Drawable unwrappedDrawable2 = AppCompatResources.getDrawable(getContext(), R.drawable.right_rounded);
                Drawable wrappedDrawable2 = DrawableCompat.wrap(unwrappedDrawable2);
                DrawableCompat.setTint(wrappedDrawable2, sec_colour);
                stages[NUM_STAGES-1].setBackground(wrappedDrawable2);
                stageTvs[NUM_STAGES-1].setTextColor(white);
                break;
            default:
                stages[operation.getCurrent_stage()-1].setBackgroundColor(sec_colour);
                stageTvs[operation.getCurrent_stage()-1].setTextColor(white);
        }

        header_layout.setBackgroundColor(cat_colour);

        // Keyboard comment enter handler
        comment_input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {

                String text = comment_input.getText().toString();
                long curr_sec = System.currentTimeMillis() / 1000L;
                comment_input.setText("");
                closeKeyboard();
                addCommentToDatabase(text, curr_sec);
                mAdapter.notifyDataSetChanged();

                first_comment.setText(text);
                first_comment_time.setText(epochTimeToHourMin(curr_sec));
                num_of_comments.setText(String.valueOf(comments.size()));
                num_of_comments_expand.setText(String.valueOf(comments.size()));

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


    private void addCommentToDatabase(String text, long time) {
        String ot_num = String.valueOf(operation.getTheatre_number());
        String op_id = operation.getId();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://hospitalscheduler-41566-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference ref = database.getReference("operations")
                .child(ot_num)
                .child(op_id);

        comments.add(new Comment(text, time));
        ref.child("comments").setValue(comments);
        Collections.sort(comments);
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
        closeKeyboard();
    }

    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getView().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
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