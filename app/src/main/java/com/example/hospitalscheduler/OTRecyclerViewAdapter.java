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
        view = mInflator.inflate(R.layout.cardview_ot, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OTRecyclerViewAdapter.MyViewHolder holder, int position) {
        OperatingTheatre ot = operatingTheatres.get(position);

        holder.ot_num.setText("OT " + String.valueOf(ot.getNumber()));
        holder.head_surgeon.setText("Head Surgeon:\n" + ot.getSchedule().get(0).getSurgeon());
        holder.start_time.setText( (ot.getSchedule().get(0).getStartTime() == null) ? "Not started" :
                ot.getSchedule().get(0).getStartTime() );

        int categoryImage = categoryToDrawable(ot.getSchedule().get(0).getCategory());
        holder.card_background_image.setImageResource(categoryImage);
        String colour = categoryToColour(ot.getSchedule().get(0).getCategory());
        holder.card_background_colour.setBackgroundColor(Color.parseColor(colour));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Operation_Info_Tab_Activity.class);
                ArrayList<Operation> schedule = (ArrayList<Operation>) ot.getSchedule();
//                bundle.putParcelableArrayList("Schedule", schedule);
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
//        for (int i = 0; i < mData.get(position).getStage() ; i++) {
//            TextView tv = (TextView) holder.row.getChildAt(i);
//            tv.setBackgroundColor(Color.parseColor("#BB40C4FF"));
//        }

    }

    @Override
    public int getItemCount() {
        return operatingTheatres.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ot_num;
        TextView head_surgeon;
        TextView start_time;
        ImageView card_background_image;
        TableLayout tableLayout;
        TableRow row;
        CardView cardView;
        ImageView card_background_colour;

//        ImageButton notify;
        ToggleButton notify;

        public MyViewHolder(View itemView) {
            super(itemView);

            ot_num = (TextView) itemView.findViewById(R.id.ov_ot_num_tv);
            head_surgeon = (TextView) itemView.findViewById(R.id.ov_head_surgeon_tv);
            start_time = (TextView) itemView.findViewById(R.id.ov_start_time_tv);
            card_background_image = (ImageView) itemView.findViewById(R.id.card_background_image_id);
            card_background_colour = (ImageView) itemView.findViewById(R.id.card_background_colour);
            cardView = (CardView) itemView.findViewById(R.id.ot_cardview_id);
            tableLayout = (TableLayout) itemView.findViewById(R.id.ov_card_table);
            row = (TableRow) tableLayout.getChildAt(0);
            notify = (ToggleButton) itemView.findViewById(R.id.ov_notification_button);
        }
    }
}
