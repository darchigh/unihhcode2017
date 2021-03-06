package com.liisa.chatbotapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chatbotapp.mambaObj.ChatMessage;

import java.util.ArrayList;

/**
 * Created by liisa_000 on 08/04/2017.
 */


public class Recycler extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChatMessage> messageArrayList;
    private int SELF = 1;
    private int OTHER = 0;

    public Recycler(ArrayList<ChatMessage> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == SELF) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chatitem_user, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chatitem_watson, parent, false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messageArrayList.get(position);
        if (message.isIncoming()) {
            return OTHER;
        } else {
            return SELF;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messageArrayList.get(position);
        message.setMessage(message.getMessage());
        ((ViewHolder) holder).message.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message;

        public ViewHolder(View view) {
            super(view);
            message = (TextView) itemView.findViewById(R.id.message);
        }

    }
}