package com.example.hospitalscheduler.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalscheduler.objects.OperationV2;
import com.example.hospitalscheduler.R;

import java.util.ArrayList;

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

        int curr_sched_red = ContextCompat.getColor(mContext, R.color.curr_sched_red);


        if (op.getCurrent_stage() > 0 && op.getCurrent_stage() < 6) {
            marks[op.getCurrent_stage()-1].setBackgroundColor(curr_sched_red);
            stages[op.getCurrent_stage()-1].setBackgroundColor(curr_sched_red);
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

//            testText = itemView.findViewById(R.id.sched_frag_test);
        }
    }
}
