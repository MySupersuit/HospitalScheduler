package com.example.hospitalscheduler.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalscheduler.R;
import com.example.hospitalscheduler.objects.Comment;

import java.util.List;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.CommentViewHolder> {

//    private List<String> comments;
    private List<Comment> comments;
//    private LayoutInflater mInflator;
//    private ItemClickListener mClickListener;
    private Context mContext;

    public CommentRecyclerViewAdapter(Context context, List<Comment> data) {
//        this.mInflator = LayoutInflater.from(context);
        this.mContext = context;
        this.comments = data;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflator = LayoutInflater.from(mContext);
        View view = mInflator.inflate(R.layout.rv_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerViewAdapter.CommentViewHolder holder, int position) {
        String comment = comments.get(position).getContent();
        long time = comments.get(position).getTime();
        holder.comment_tv.setText(comment);
        holder.time_tv.setText(String.valueOf(time));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView comment_tv;
        TextView time_tv;

        public CommentViewHolder(View itemView) {
            super(itemView);
            comment_tv = (TextView) itemView.findViewById(R.id.comment_text);
            time_tv = (TextView) itemView.findViewById(R.id.comment_time);
        }
    }


}
