package com.example.hospitalscheduler;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Comment;

import java.util.List;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.CommentViewHolder> {

    private List<String> comments;
//    private LayoutInflater mInflator;
//    private ItemClickListener mClickListener;
    private Context mContext;

    public CommentRecyclerViewAdapter(Context context, List<String> data) {
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
        String animal = comments.get(position);
        holder.comment_tv.setText(animal);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView comment_tv;

        public CommentViewHolder(View itemView) {
            super(itemView);
            comment_tv = (TextView) itemView.findViewById(R.id.comment_text);

        }
    }


}
