package com.SegalTech.MeTalk;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageDetailsFragment extends Fragment {

    static final String MESSAGE_TIME_PREFIX = "Message time: ";
    static final String MESSAGE_ORIGIN_PREFIX = "Message origin: ";

    public MessageDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        final Message message = (Message)getArguments().getSerializable(
                MainActivity.MESSAGE_NAV_ARG_KEY);

        TextView messageTimeTextView = view.findViewById(R.id.message_details_time);
        TextView messageOriginTextView = view.findViewById(R.id.message_details_origin);

        String pattern;
        Calendar today = Calendar.getInstance();
        today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DATE), 0, 0 ,0);

        String messageTimeText = MESSAGE_TIME_PREFIX;
        if (message.messageTime.after(today.getTime()))
        {
            messageTimeText = messageTimeText + "Today, ";
            pattern = MainActivity.MESSAGE_FROM_TODAY_FORMAT;
        }
        else
        {
            pattern = MainActivity.MESSAGE_FROM_BEFORE_FORMAT;
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern, Locale.getDefault());

        messageTimeText = messageTimeText + dateFormatter.format(
                message.messageTime);
        messageTimeTextView.setText(messageTimeText);

        String messageOriginText = MESSAGE_ORIGIN_PREFIX + message.messageOrigin;
        messageOriginTextView.setText(messageOriginText);

        ImageButton deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)getActivity();
                if (mainActivity == null)
                {
                    return;
                }
                MeTalk application = (MeTalk) getActivity().getApplication();
                if (application.messageViewModel.getAllMessages().getValue() == null)
                {
                    return;
                }
                int messageIndex = application.messageViewModel.getAllMessages().getValue().
                        indexOf(message);
                application.messageViewModel.deleteMessage(message);

                MessageRecyclerUtils.MessageAdapter adapter = mainActivity.getAdapter();

                adapter.notifyItemRemoved(messageIndex);
                Navigation.findNavController(v).navigate(
                        R.id.action_messageDetailsFragment_to_messagingFragment);
            }
        });
    }
}
