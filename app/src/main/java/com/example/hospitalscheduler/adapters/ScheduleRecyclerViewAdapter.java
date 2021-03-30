package com.example.hospitalscheduler.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalscheduler.objects.OperationV2;
import com.example.hospitalscheduler.R;

import java.util.ArrayList;

import static com.example.hospitalscheduler.utilities.Utilites.*;
import static com.example.hospitalscheduler.utilities.Constants.*;

public class ScheduleRecyclerViewAdapter extends RecyclerView.Adapter<ScheduleRecyclerViewAdapter.SchedViewHolder> {

    private ArrayList<OperationV2> schedule;
    private Context mContext;

    public ScheduleRecyclerViewAdapter(Context context, ArrayList<OperationV2> schedule) {
        this.mContext = context;
        this.schedule = schedule;
    }


    @NonNull
    @Override
    public SchedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflator = LayoutInflater.from(mContext);
        View view = mInflator.inflate(R.layout.schedule_card, parent, false);
        return new SchedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleRecyclerViewAdapter.SchedViewHolder holder, int position) {
        Log.d("POS", String.valueOf(position));
        OperationV2 op = schedule.get(position);
        if (op == null) return;

        View[] marks = {holder.mark1, holder.mark2, holder.mark3, holder.mark4, holder.mark5};
        TextView[] stages = {holder.stage1, holder.stage2, holder.stage3, holder.stage4, holder.stage5};
        setStages(marks, stages, op);

        holder.header_category.setText(op.getCategory());
        holder.header_icon.setImageResource(categoryToDrawable(op.getCategory()));
        int cat_colour = Color.parseColor(categoryToColour(op.getCategory()));
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(mContext, R.drawable.top_rounded);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, cat_colour);
        holder.header_cl.setBackground(wrappedDrawable);

        holder.back_colour.setBackgroundColor(Color.parseColor(categoryToColour(op.getCategory())));

        holder.procedure.setText(op.getProcedure());
        holder.surgeon.setText(op.getSurgeon());
        holder.patient.setText(op.getPatient_name());
        holder.nurse.setText(op.getScrubNurse());
        holder.covid.setText((op.getIsCovid() == 1) ? "Yes" : "No");

    }

    private void setStages(View[] marks, TextView[] stages, OperationV2 op) {
        int curr_sched_red = ContextCompat.getColor(mContext, R.color.curr_sched_red);
        int cat_colour = Color.parseColor(categoryToColour(op.getCategory()));
        int text_colour = Color.parseColor(categoryToTextColour(op.getCategory()));
        int white = ContextCompat.getColor(mContext, R.color.white);

        Log.d("CURR", String.valueOf(op.getCurrent_stage()));

        for (int i = 0; i < op.getCurrent_stage() && i < NUM_STAGES; i++) {
            stages[i].setBackgroundColor(cat_colour);
            stages[i].setTextColor(text_colour);
        }

        if (op.getCurrent_stage() > 0 && op.getCurrent_stage() < 6) {
            marks[op.getCurrent_stage() - 1].setBackgroundColor(curr_sched_red);
            stages[op.getCurrent_stage() - 1].setBackgroundColor(curr_sched_red);
            stages[op.getCurrent_stage() - 1].setTextColor(white);
        }

        for (int i = NUM_STAGES - 1; i >= op.getCurrent_stage(); i--) {
            stages[i].setAlpha(0.3f);
        }
    }

    @Override
    public int getItemCount() {
        return schedule.size();
    }

    public static class SchedViewHolder extends RecyclerView.ViewHolder {
        //        TextView testText;
        View mark1, mark2, mark3, mark4, mark5;
        TextView stage1, stage2, stage3, stage4, stage5;

        TextView header_category;
        ImageView header_icon;
        ConstraintLayout header_cl;
        View back_colour;
        TextView procedure, surgeon, patient, nurse, covid;

        public SchedViewHolder(View itemView) {
            super(itemView);
            mark1 = itemView.findViewById(R.id.sched_frag_curr_marker1);
            mark2 = itemView.findViewById(R.id.sched_frag_curr_marker2);
            mark3 = itemView.findViewById(R.id.sched_frag_curr_marker3);
            mark4 = itemView.findViewById(R.id.sched_frag_curr_marker4);
            mark5 = itemView.findViewById(R.id.sched_frag_curr_marker5);

            stage1 = itemView.findViewById(R.id.sched_frag_stage_1);
            stage2 = itemView.findViewById(R.id.sched_frag_stage_2);
            stage3 = itemView.findViewById(R.id.sched_frag_stage_3);
            stage4 = itemView.findViewById(R.id.sched_frag_stage_4);
            stage5 = itemView.findViewById(R.id.sched_frag_stage_5);

            header_category = itemView.findViewById(R.id.sched_frag_header_category);
            header_icon = itemView.findViewById(R.id.sched_frag_header_icon);
            header_cl = itemView.findViewById(R.id.sched_card_header_cl);

            back_colour = itemView.findViewById(R.id.sched_frag_back_colour);

            procedure = itemView.findViewById(R.id.sched_frag_procedure_tv);
            patient = itemView.findViewById(R.id.sched_frag_patient_name_tv);
            surgeon = itemView.findViewById(R.id.sched_frag_surgeon_tv);
            nurse = itemView.findViewById(R.id.sched_frag_nurse_tv);
            covid = itemView.findViewById(R.id.sched_frag_covid_tv);
        }
    }
}
