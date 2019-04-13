package com.example.metalk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MessageRecyclerUtils {

    private static final String HOUR_ZERO_PREFIX = "0";
    private static final String MINUTE_ZERO_PREFIX = ":0";
    private static final String COMMA = ":";

    static class MessageHolder extends RecyclerView.ViewHolder {
        public final TextView text;
        public final TextView time;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.message_text);
            time = itemView.findViewById(R.id.message_time);
        }
    }

    static class MessageCallBack extends DiffUtil.ItemCallback<Message> {

        @Override
        public boolean areItemsTheSame(@NonNull Message m1, @NonNull Message m2) {
            return m1 == m2;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message m1, @NonNull Message m2) {
            return m1.equals(m2);
        }
    }

    static class MessageAdapter extends ListAdapter<Message, MessageHolder> {

        public MessageAdapter() {
            super (new MessageCallBack());
        }

        @NonNull
        @Override
        public MessageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            View itemView = LayoutInflater.from(context).inflate(R.layout.single_message,
                    viewGroup, false);
            return new MessageHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MessageHolder messageHolder, int i) {
            Message message = getItem(i);
            messageHolder.text.setText(message.messageText);
            messageHolder.time.setText(message.messageTime);
        }
    }
}
