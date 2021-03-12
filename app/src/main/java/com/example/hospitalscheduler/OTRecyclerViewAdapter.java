package com.example.hospitalscheduler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import androidx.recyclerview.widget.RecyclerView;

import static com.example.hospitalscheduler.Utilites.*;

import java.util.ArrayList;
import java.util.List;

public class OTRecyclerViewAdapter extends RecyclerView.Adapter<OTRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<OperatingTheatre> operatingTheatres; // list of objects going into recycler view
    private static final int NUM_STAGES = 5;

    public OTRecyclerViewAdapter(Context mContext, List<OperatingTheatre> operatingTheatres) {
        this.mContext = mContext;
        this.operatingTheatres = operatingTheatres;
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
        OperatingTheatre ot = operatingTheatres.get(position);
        Operation curr_op = ot.getSchedule().get(0);
        Operation next_op = null;
        if (ot.getSchedule().size() > 1) {
            next_op = ot.getSchedule().get(1);
        }

        holder.ot_num.setText("OT " + String.valueOf(ot.getNumber()));

        holder.curr_surgeon.setText(curr_op.getSurgeon());
        String curr_colour = categoryToColour(curr_op.getCategory());
        holder.curr_back_colour.setBackgroundColor(Color.parseColor(curr_colour));
        int curr_back_image = categoryToDrawable(curr_op.getCategory());
        holder.curr_back_image.setImageResource(curr_back_image);
        holder.curr_stage_time.setText(curr_op.getStartTime());     // NOT RIGHT - new field for time in current stage
        holder.curr_stage_num.setText("stage " + curr_op.getStage() + ": ");

        holder.next_surgeon.setText(next_op.getSurgeon());
        String next_colour = categoryToColour(next_op.getCategory());
        holder.next_back_colour.setBackgroundColor(Color.parseColor(next_colour));
        int next_back_image = categoryToDrawable(next_op.getCategory());
        holder.next_back_image.setImageResource(next_back_image);
        holder.next_stage_time.setText(next_op.getStartTime()); // NOT RIGHT - new field for time in current stage

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Operation_Info_Tab_Activity.class);
                ArrayList<Operation> schedule = (ArrayList<Operation>) ot.getSchedule();
                intent.putParcelableArrayListExtra("Schedule", schedule);
                intent.putExtra("Number", ot.getNumber());
                intent.putExtra("Notified", ot.getIsNotified());

                mContext.startActivity(intent);
            }
        });


        holder.notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.notify.isChecked()) {
                    Toast toast = Toast.makeText(mContext, "Getting notifications for " + operatingTheatres.get(position).getNumber(),
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(mContext, "Not getting notifications for " + operatingTheatres.get(position).getNumber(),
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        // Turn completed and current stages blue
        TextView[] stages = {holder.stage1, holder.stage2,
                holder.stage3, holder.stage4, holder.stage4};

        for (int i = 0; i < curr_op.getStage() ; i++) {
            stages[i].setBackgroundColor(Color.parseColor("#40C4FF"));
        }

    }

    @Override
    public int getItemCount() {
        return operatingTheatres.size();
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
