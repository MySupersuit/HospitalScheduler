    package com.example.hospitalscheduler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import static com.example.hospitalscheduler.Utilites.*;

import java.util.ArrayList;
import java.util.List;

public class OTRecyclerViewAdapter extends RecyclerView.Adapter<OTRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    //    private ArrayList<OperatingTheatre> operatingTheatres; // list of objects going into recycler view
    private ArrayList<OperatingTheatreV2> operatingTheatresV2;
    private static final int NUM_STAGES = 5;

    public OTRecyclerViewAdapter(Context mContext, ArrayList<OperatingTheatreV2> operatingTheatresV2) {
        this.mContext = mContext;
        this.operatingTheatresV2 = operatingTheatresV2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflator = LayoutInflater.from(mContext);
//        view = mInflator.inflate(R.layout.cardview_ot, parent, false);
//        view = mInflator.inflate(R.layout.cardview_ot_v2, parent, false);
//        view = mInflator.inflate(R.layout.cardview_ot_v3, parent, false);
        view = mInflator.inflate(R.layout.cardview_ot_v4, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OTRecyclerViewAdapter.MyViewHolder holder, int position) {

        OperatingTheatreV2 ot = operatingTheatresV2.get(position);
        OperationV2 curr_op = ot.getSchedule().get(0);
        OperationV2 next_op = null;
        if (ot.getSchedule().size() > 1) {
            next_op = ot.getSchedule().get(1);
        }

        holder.ot_num.setText("OT " + ot.getNumber());

        holder.curr_surgeon.setText(curr_op.getSurgeon());
        String curr_colour = categoryToColour(curr_op.getCategory());
        holder.curr_back_colour.setBackgroundColor(Color.parseColor(curr_colour));
        int curr_back_image = categoryToDrawable(curr_op.getCategory());
        holder.curr_back_image.setImageResource(curr_back_image);
        holder.curr_stage_num.setText("stage " + curr_op.getCurrent_stage());
        String mins_in_curr_stage = getMinutesSince(curr_op.getCurr_stage_start_time());
        holder.curr_stage_time.setText(mins_in_curr_stage);

        holder.next_surgeon.setText(next_op.getSurgeon());
        String next_colour = categoryToColour(next_op.getCategory());
        holder.next_back_colour.setBackgroundColor(Color.parseColor(next_colour));
        int next_back_image = categoryToDrawable(next_op.getCategory());
        holder.next_back_image.setImageResource(next_back_image);
        holder.next_stage_num.setText("stage " + next_op.getCurrent_stage());
        String mins_in_next_stage = getMinutesSince(next_op.getCurr_stage_start_time());
        holder.next_stage_time.setText(mins_in_next_stage);

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Operation_Info_Tab_Activity.class);
                ArrayList<OperationV2> schedule = (ArrayList<OperationV2>) ot.getSchedule();
                intent.putParcelableArrayListExtra("Schedule", schedule);
                intent.putExtra("Number", ot.getNumber());
                intent.putExtra("Notified", ot.getIsNotified());

                Log.d("STOP", v.toString());
                v.clearAnimation();

                mContext.startActivity(intent);
            }
        });

        holder.notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.notify.isChecked()) {
                    makeSnackbar("Getting notifications for " + operatingTheatresV2.get(position).getNumber(), v, Snackbar.LENGTH_SHORT);
                    ot.setIsNotified(1);
                } else {
                    makeSnackbar("Not getting notifications for " + operatingTheatresV2.get(position).getNumber(), v, Snackbar.LENGTH_SHORT);
                    ot.setIsNotified(0);
                }
            }
        });

        // Turn completed and current stages blue
        TextView[] stages = {holder.stage1, holder.stage2,
                holder.stage3, holder.stage4, holder.stage5};
        Log.d("SetStage", String.valueOf(curr_op.getTheatre_number()));
        setStagesColour(stages, curr_op, next_op);

        // Set notification bell
        if (ot.getIsNotified() == 1) {
            holder.notify.setChecked(true);
        }

    }

    // Assume more than 999 minutes is error
    private String getMinutesSince(long timestamp) {
        long curr_time = System.currentTimeMillis() / 1000L;
        long difference = curr_time - timestamp;
        int mins = (int) Math.ceil((double)difference/60);
        return (mins > 999) ? "0" : String.valueOf(mins);
    }

    private void setStagesColour(TextView[] stages, OperationV2 curr_op, OperationV2 next_op) {
        int blue_curr = ContextCompat.getColor(mContext, R.color.stage_blue_curr);
        int blue_prev = ContextCompat.getColor(mContext, R.color.stage_blue_prev);
        int light_blue = ContextCompat.getColor(mContext, R.color.light_blue);
        int orange_curr = ContextCompat.getColor(mContext, R.color.stage_orange_curr);
        int orange_prev = ContextCompat.getColor(mContext, R.color.stage_orange_prev);
        int white = ContextCompat.getColor(mContext, R.color.white);

        // Current Operation first
        if (curr_op.getCurrent_stage() > 0) {
            stages[curr_op.getCurrent_stage() - 1].setBackgroundColor(blue_curr);

            for (int i = curr_op.getCurrent_stage() - 2; i >= 0; i--) {
                stages[i].setBackgroundColor(light_blue);
            }
        }

        // Next Operation second
        if (next_op.getCurrent_stage() > 0) {
            stages[next_op.getCurrent_stage() - 1].setBackgroundColor(orange_curr);

            for (int i = next_op.getCurrent_stage() - 2; i >= 0; i--) {
                stages[i].setBackgroundColor(orange_prev);
            }
        }

        // Make others white
        // others are from last stage to current highest stage
        int highest_stage = Math.max(curr_op.getCurrent_stage(),
                next_op.getCurrent_stage());
        Log.d("IMP", String.valueOf(highest_stage));
        for (int i = NUM_STAGES-1; i >= highest_stage; i--) {
            stages[i].setBackgroundColor(white);
        }
    }

    @Override
    public int getItemCount() {
        return operatingTheatresV2.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // v2
        TextView ot_num;
        ToggleButton notify;
        CardView cardview;

        TextView curr_surgeon;
        ImageView curr_back_colour;
        ImageView curr_back_image;
        TextView curr_stage_time;
        TextView curr_stage_num;

        TextView next_surgeon;
        ImageView next_back_colour;
        ImageView next_back_image;
        TextView next_stage_time;
        TextView next_stage_num;

        TextView stage1, stage2, stage3, stage4, stage5;

        public MyViewHolder(View itemView) {
            super(itemView);

            // cardview v2
            ot_num = (TextView) itemView.findViewById(R.id.ov_ot_num_tv);
            notify = (ToggleButton) itemView.findViewById(R.id.ov_notification_button);
            cardview = (CardView) itemView.findViewById(R.id.ot_cardview_id);

            curr_stage_time = (TextView) itemView.findViewById(R.id.curr_stage_time);
            curr_stage_num = (TextView) itemView.findViewById(R.id.curr_in_stage);
            curr_surgeon = (TextView) itemView.findViewById(R.id.ov_curr_surgeon);
            curr_back_colour = (ImageView) itemView.findViewById(R.id.ov_curr_back_colour);
            curr_back_image = (ImageView) itemView.findViewById(R.id.ov_curr_back_image);

            next_back_colour = (ImageView) itemView.findViewById(R.id.ov_next_back_colour);
            next_back_image = (ImageView) itemView.findViewById(R.id.ov_next_back_image);
            next_surgeon = (TextView) itemView.findViewById(R.id.ov_next_surgeon);
            next_stage_time = (TextView) itemView.findViewById(R.id.next_stage_time);
            next_stage_num = (TextView) itemView.findViewById(R.id.next_in_stage);

            stage1 = (TextView) itemView.findViewById(R.id.ov_status_1);
            stage2 = (TextView) itemView.findViewById(R.id.ov_status_2);
            stage3 = (TextView) itemView.findViewById(R.id.ov_status_3);
            stage4 = (TextView) itemView.findViewById(R.id.ov_status_4);
            stage5 = (TextView) itemView.findViewById(R.id.ov_status_5);

        }
    }
}
