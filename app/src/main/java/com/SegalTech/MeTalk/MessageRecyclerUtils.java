package com.SegalTech.MeTalk;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MessageRecyclerUtils {

    static class MessageHolder extends RecyclerView.ViewHolder {
        public final TextView text;
        public final TextView time;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.message_text);
            time = itemView.findViewById(R.id.message_time);
        }
    }

    interface MessageLongClickCallback {
        void onLongClickMessage(Message message);
    }

    static class MessageCallBack extends DiffUtil.ItemCallback<Message> {

        @Override
        public boolean areItemsTheSame(@NonNull Message m1, @NonNull Message m2) {
            return m1 == m2;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message m1, @NonNull Message m2) {
            return ((m1.messageText.equals(m2.messageText))
                    && (m1.messageTime.equals(m2.messageTime)));
        }
    }

    static class MessageAdapter extends ListAdapter<Message, MessageHolder> {

        public MessageAdapter() {
            super (new MessageCallBack());
        }

        public MessageLongClickCallback callback;

        @NonNull
        @Override
        public MessageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            final Context context = viewGroup.getContext();
            View itemView = LayoutInflater.from(context).inflate(R.layout.single_message,
                    viewGroup, false);
            final MessageHolder holder = new MessageHolder(itemView);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Message message = getItem(holder.getAdapterPosition());
                    if (callback != null)
                    {
                        callback.onLongClickMessage(message);
                    }
                    return true;
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MessageHolder messageHolder, int i) {
            Message message = getItem(i);
            messageHolder.text.setText(message.messageText);
            String pattern;
            Calendar today = Calendar.getInstance();
            today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                      today.get(Calendar.DATE), 0, 0 ,0);
            if (message.messageTime.after(today.getTime()))
            {
                pattern = "HH:mm";
            }
            else
            {
                pattern = "dd/MM/yyyy HH:mm";
            }
            SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern,
                    Locale.getDefault());
            messageHolder.time.setText(dateFormatter.format(message.messageTime));
        }
    }
}
