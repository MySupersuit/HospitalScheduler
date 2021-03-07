package com.example.hospitalscheduler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class OTRecyclerViewAdapter extends RecyclerView.Adapter<OTRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<OperatingTheatre> mData; // list of objects going into recycler view

    public OTRecyclerViewAdapter(Context mContext, List<OperatingTheatre> mData) {
        this.mContext = mContext;
        this.mData = mData;
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
        holder.ot_num.setText(String.format("OT %d", mData.get(position).getNumber()));
        holder.op_status.setText(String.format("Stage: %d/5", mData.get(position).getStage()));
        holder.head_surgeon.setText("Surgeon:\n" + mData.get(position).getHeadSurgeon());
        holder.start_time.setText("Time: " + mData.get(position).getCurrStartTime());
        holder.card_background_image.setImageResource(mData.get(position).getCurrOpThumb());
        String colour = getColourGivenCategory(mData.get(position).getCurrOpThumb());
        holder.card_background_colour.setBackgroundColor(Color.parseColor(colour));
        holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Operation_Info_Tab_Activity.class);
                intent.putExtra("Number", mData.get(position).getNumber());
                intent.putExtra("Stage", mData.get(position).getStage());
                intent.putExtra("Surgeon", mData.get(position).getHeadSurgeon());
                intent.putExtra("Time", mData.get(position).getCurrStartTime());
                intent.putExtra("Category", mData.get(position).getCurrOpThumb());
                intent.putExtra("Operation", mData.get(position).getCurrOp());
                mContext.startActivity(intent);
            }
        });


        holder.notify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (holder.notify.isChecked()) {
                    Toast toast = Toast.makeText(mContext, "Getting notifications for " + mData.get(position).getNumber(),
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(mContext, "Not getting notifications for " + mData.get(position).getNumber(),
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

    }

    private String getColourGivenCategory(int id) {
        switch (id) {
            case R.drawable.humanbrain:
                return "#ADD8E6";
            case R.drawable.ent_icon:
                return "#FFC0CB";
            case R.drawable.intestine_icon:
                return "#FFFFAA";
            case R.drawable.vascularicon:
                return "#E6E6FA";
            case R.drawable.ortho_icon:
                return "#FFFFFF";
            default:
                return "FFFFFF";
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ot_num;
        TextView op_status;
        TextView head_surgeon;
        TextView start_time;
        ImageView card_background_image;
        CardView cardView;
        ImageView card_background_colour;
//        ImageButton notify;
        ToggleButton notify;

        public MyViewHolder(View itemView) {
            super(itemView);

            ot_num = (TextView) itemView.findViewById(R.id.ov_ot_num_tv);
            op_status = (TextView) itemView.findViewById(R.id.ov_status_tv);
            head_surgeon = (TextView) itemView.findViewById(R.id.ov_head_surgeon_tv);
            start_time = (TextView) itemView.findViewById(R.id.ov_start_time_tv);
            card_background_image = (ImageView) itemView.findViewById(R.id.card_background_image_id);
            card_background_colour = (ImageView) itemView.findViewById(R.id.card_background_colour);
            cardView = (CardView) itemView.findViewById(R.id.ot_cardview_id);
//            notify = (ImageButton) itemView.findViewById(R.id.ov_notification_button);
            notify = (ToggleButton) itemView.findViewById(R.id.ov_notification_button);
        }
    }
}
