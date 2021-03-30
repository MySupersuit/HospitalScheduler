package com.example.hospitalscheduler.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalscheduler.objects.OperatingTheatreV2;
import com.example.hospitalscheduler.objects.OperationV2;
import com.example.hospitalscheduler.activities.Operation_Info_Tab_Activity;
import com.example.hospitalscheduler.interfaces.OverviewOnClickListener;
import com.example.hospitalscheduler.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import static com.example.hospitalscheduler.utilities.Utilites.*;
import static com.example.hospitalscheduler.utilities.Constants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OTRecyclerViewAdapter extends RecyclerView.Adapter<OTRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    //    private ArrayList<OperatingTheatre> operatingTheatres; // list of objects going into recycler view
    private ArrayList<OperatingTheatreV2> operatingTheatresV2;
    private OverviewOnClickListener ovcl;

    public OTRecyclerViewAdapter(Context mContext, ArrayList<OperatingTheatreV2> operatingTheatresV2, OverviewOnClickListener c) {
        this.mContext = mContext;
        this.operatingTheatresV2 = operatingTheatresV2;
        this.ovcl = c;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflator = LayoutInflater.from(mContext);

//        view = mInflator.inflate(R.layout.cardview_ot_v4, parent, false);
        view = mInflator.inflate(R.layout.cardview_ot_v5, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OTRecyclerViewAdapter.MyViewHolder holder, int position) {

        Log.d("NUM", String.valueOf(position));
        OperatingTheatreV2 ot = operatingTheatresV2.get(position);

        OperationV2 curr_op = getCurrentOperation(ot.getSchedule());
        OperationV2 next_op = getNextOperation(ot.getSchedule(), curr_op);

        if (curr_op == null) {
            curr_op = new OperationV2(ot.getNumber());
        }
        if (next_op == null) {
            next_op = new OperationV2(ot.getNumber());
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

        holder.cardview.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, Operation_Info_Tab_Activity.class);
            ArrayList<OperationV2> schedule = ot.getSchedule();
            intent.putParcelableArrayListExtra("Schedule", schedule);
            intent.putExtra("Number", ot.getNumber());
            intent.putExtra("Notified", ot.getIsNotified());

            ovcl.stopAnimCallback(ot.getNumber());

            mContext.startActivity(intent);
        });

        holder.notify.setOnClickListener(v -> {
            if (holder.notify.isChecked()) {
                makeSnackbar("Getting notifications for " + operatingTheatresV2.get(position).getNumber(),
                        v, Snackbar.LENGTH_SHORT);

                HashMap<String, Integer> not = (HashMap<String, Integer>) loadMap();
                not.put(String.valueOf(ot.getNumber()), 1);
                Log.d("MAP", not.toString());
                saveMap(not);
                Log.d("SAVE", "saved apparently");

                ot.setIsNotified(1);
            } else {
                makeSnackbar("Not getting notifications for " + operatingTheatresV2.get(position).getNumber(),
                        v, Snackbar.LENGTH_SHORT);

                HashMap<String, Integer> not = (HashMap<String, Integer>) loadMap();
                not.put(String.valueOf(ot.getNumber()), 0);
                Log.d("MAP", not.toString());
                saveMap(not);
                Log.d("SAVE", "saved apparently");

                ot.setIsNotified(0);
            }
        });

        // Turn completed and current stages blue
        TextView[] stages = {holder.stage1, holder.stage2,
                holder.stage3, holder.stage4, holder.stage5};
        ImageView[] curr_marks = {holder.curr_mark1, holder.curr_mark2,
                holder.curr_mark3, holder.curr_mark4, holder.curr_mark5};
        ImageView[] next_marks = {holder.next_mark1, holder.next_mark2,
                holder.next_mark3, holder.next_mark4, holder.next_mark5};

        setStagesColour(stages, curr_op, next_op,
                curr_op.getCategory(), next_op.getCategory(),
                holder.curr_op_header, holder.next_op_header,
                curr_marks, next_marks);
//        holder.curr_mark1.setColorFilter(Color.parseColor("#FF0000"));

        // Set notification bell
        if (ot.getIsNotified() == 1) {
            holder.notify.setChecked(true);
        } else {
            holder.notify.setChecked(false);
        }

    }

    private void setStagesColour(TextView[] stages, OperationV2 curr_op, OperationV2 next_op,
                                 String curr_cat, String next_cat,
                                 TextView curr_op_header, TextView next_op_header,
                                 ImageView[] curr_marks, ImageView[] next_marks) {
        int blue_curr = ContextCompat.getColor(mContext, R.color.stage_blue_curr);
        int blue_prev = ContextCompat.getColor(mContext, R.color.stage_blue_prev);
        int light_blue = ContextCompat.getColor(mContext, R.color.light_blue);
        int orange_curr = ContextCompat.getColor(mContext, R.color.stage_orange_curr);
        int orange_prev = ContextCompat.getColor(mContext, R.color.stage_orange_prev);
        int white = ContextCompat.getColor(mContext, R.color.white);
        int black = ContextCompat.getColor(mContext, R.color.black);
        int transparant = ContextCompat.getColor(mContext, R.color.transparent);

        int curr_op_sec_col = Color.parseColor(categoryToSecColour(curr_cat));
        int curr_op_col = Color.parseColor(categoryToColour(curr_cat));
        int next_op_sec_col = Color.parseColor(categoryToSecColour(next_cat));
        int next_op_col = Color.parseColor(categoryToColour(next_cat));
        int curr_op_text_col = Color.parseColor(categoryToTextColour(curr_cat));
        int next_op_text_col = Color.parseColor(categoryToTextColour(next_cat));

        curr_op_header.setBackgroundColor(curr_op_col);
        next_op_header.setBackgroundColor(next_op_col);

        for (int i = 0; i <curr_marks.length; i++) {
            curr_marks[i].setImageAlpha(0);
            next_marks[i].setImageAlpha(0);
        }

        // Current Operation first
        if (curr_op.getCurrent_stage() > 0 && curr_op.getCurrent_stage() < NUM_STAGES+1) {
            stages[curr_op.getCurrent_stage() - 1].setBackgroundColor(curr_op_sec_col);
            stages[curr_op.getCurrent_stage() - 1].setTextColor(white);
            stages[curr_op.getCurrent_stage() - 1].setAlpha(1f);
            curr_marks[curr_op.getCurrent_stage() - 1].setImageAlpha(255);
            curr_marks[curr_op.getCurrent_stage() - 1].setColorFilter(curr_op_col);

            for (int i = curr_op.getCurrent_stage() - 2; i >= 0; i--) {
                stages[i].setBackgroundColor(curr_op_col);
                stages[i].setTextColor(curr_op_text_col);
                stages[i].setAlpha(1f);
                curr_marks[i].setImageAlpha(0);
            }
        }

        // Next Operation second as will nearly always be behind the current operation
        if (next_op.getCurrent_stage() > 0) {
            stages[next_op.getCurrent_stage() - 1].setBackgroundColor(next_op_sec_col);
            stages[next_op.getCurrent_stage() - 1].setTextColor(white);
            stages[next_op.getCurrent_stage() - 1].setAlpha(1f);
            next_marks[next_op.getCurrent_stage() - 1].setImageAlpha(255);
            next_marks[next_op.getCurrent_stage() - 1].setColorFilter(next_op_col);

            for (int i = next_op.getCurrent_stage() - 2; i >= 0; i--) {
                stages[i].setBackgroundColor(next_op_col);
                stages[i].setTextColor(next_op_text_col);
                stages[i].setAlpha(1f);
                next_marks[i].setImageAlpha(0);
            }
        }

        // Make others white
        // others are from last stage to current highest stage
        int highest_stage = Math.max(curr_op.getCurrent_stage(),
                next_op.getCurrent_stage());
        for (int i = NUM_STAGES - 1; i >= highest_stage; i--) {
            stages[i].setBackgroundColor(white);
            stages[i].setTextColor(black);
            stages[i].setAlpha(0.2f);
        }
    }

    private void saveMap(Map<String, Integer> inputMap) {
        SharedPreferences pSharedPref = mContext.getSharedPreferences("is_notified_pref", Context.MODE_PRIVATE);
        if (pSharedPref != null) {
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("My_map").apply();
            editor.putString("My_map", jsonString);
            editor.apply();
        }
    }

    private Map<String, Integer> loadMap() {
        Map<String, Integer> outputMap = new HashMap<>();
        SharedPreferences pSharedPref = mContext.getSharedPreferences(
                "is_notified_pref", Context.MODE_PRIVATE);
        try {
            if (pSharedPref != null) {
                String jsonString = pSharedPref.getString("My_map", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    int value = (Integer) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
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
        View curr_back_colour;
        ImageView curr_back_image;
        TextView curr_stage_time;
        TextView curr_stage_num;
        TextView curr_op_header;

        TextView next_surgeon;
        View next_back_colour;
        ImageView next_back_image;
        TextView next_stage_time;
        TextView next_stage_num;
        TextView next_op_header;

        TextView stage1, stage2, stage3, stage4, stage5;
        ImageView curr_mark1, curr_mark2, curr_mark3, curr_mark4, curr_mark5;
        ImageView next_mark1, next_mark2, next_mark3, next_mark4, next_mark5;

        public MyViewHolder(View itemView) {
            super(itemView);

            // cardview v2
            ot_num = (TextView) itemView.findViewById(R.id.ov_ot_num_tv);
            notify = (ToggleButton) itemView.findViewById(R.id.ov_notification_button);
            cardview = (CardView) itemView.findViewById(R.id.ot_cardview_id);

            curr_stage_time = (TextView) itemView.findViewById(R.id.curr_stage_time);
            curr_stage_num = (TextView) itemView.findViewById(R.id.curr_in_stage);
            curr_surgeon = (TextView) itemView.findViewById(R.id.ov_curr_surgeon);
            curr_back_colour = (View) itemView.findViewById(R.id.ov_curr_back_colour);
            curr_back_image = (ImageView) itemView.findViewById(R.id.ov_curr_back_image);
            curr_op_header = (TextView) itemView.findViewById(R.id.curr_op_header);

            next_back_colour = (View) itemView.findViewById(R.id.ov_next_back_colour);
            next_back_image = (ImageView) itemView.findViewById(R.id.ov_next_back_image);
            next_surgeon = (TextView) itemView.findViewById(R.id.ov_next_surgeon);
            next_stage_time = (TextView) itemView.findViewById(R.id.next_stage_time);
            next_stage_num = (TextView) itemView.findViewById(R.id.next_in_stage);
            next_op_header = (TextView) itemView.findViewById(R.id.next_op_header);

            stage1 = (TextView) itemView.findViewById(R.id.ov_status_1);
            stage2 = (TextView) itemView.findViewById(R.id.ov_status_2);
            stage3 = (TextView) itemView.findViewById(R.id.ov_status_3);
            stage4 = (TextView) itemView.findViewById(R.id.ov_status_4);
            stage5 = (TextView) itemView.findViewById(R.id.ov_status_5);

            curr_mark1 = (ImageView) itemView.findViewById(R.id.ov_current_marker_1);
            curr_mark2 = (ImageView) itemView.findViewById(R.id.ov_current_marker_2);
            curr_mark3 = (ImageView) itemView.findViewById(R.id.ov_current_marker_3);
            curr_mark4 = (ImageView) itemView.findViewById(R.id.ov_current_marker_4);
            curr_mark5 = (ImageView) itemView.findViewById(R.id.ov_current_marker_5);

            next_mark1 = (ImageView) itemView.findViewById(R.id.ov_next_marker_1);
            next_mark2 = (ImageView) itemView.findViewById(R.id.ov_next_marker_2);
            next_mark3 = (ImageView) itemView.findViewById(R.id.ov_next_marker_3);
            next_mark4 = (ImageView) itemView.findViewById(R.id.ov_next_marker_4);
            next_mark5 = (ImageView) itemView.findViewById(R.id.ov_next_marker_5);

        }
    }
}
