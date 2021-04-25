package com.example.challenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FeedRecViewAdapter extends RecyclerView.Adapter<FeedRecViewAdapter.ViewHolder>{

    List<Message> messages = new ArrayList<>();
    private Context mContext;
    public FeedRecViewAdapter(Context context){
        this.mContext = context;
    }

    public void setMessages(List<Message> list){
        this.messages = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_feed, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTxt.setText(messages.get(position).getName());
        holder.messageTxt.setText(messages.get(position).getContent());
        holder.dataTxt.setText(messages.get(position).getData());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nameTxt, messageTxt, dataTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            messageTxt = itemView.findViewById(R.id.messageTxt);
            dataTxt = itemView.findViewById(R.id.dataTxt);
        }
    }

}
