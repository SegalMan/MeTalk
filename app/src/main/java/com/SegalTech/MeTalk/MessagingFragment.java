package com.SegalTech.MeTalk;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagingFragment extends Fragment
        implements MessageRecyclerUtils.MessageLongClickCallback {

    static final String EMPTY_MSG_TOAST_TEXT = "But you didn't type a message :(";

    public MessagingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messaging, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final MainActivity mainActivity = (MainActivity)getActivity();
        final MeTalk application = (MeTalk) mainActivity.getApplication();

        mainActivity.username = application.messageViewModel.getUsername().getValue();

        if (mainActivity.username != null)
        {
            TextView usernameTextView = view.findViewById(R.id.username);
            String usernameText = mainActivity.username + "'s messages";
            usernameTextView.setText(usernameText);
        }

        // Configure RecyclerView
        final RecyclerView messageRecycler = view.findViewById(R.id.message_recycler);
        messageRecycler.setLayoutManager(new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false));
        messageRecycler.setAdapter(mainActivity.adapter);
        mainActivity.adapter.callback = this;
        messageRecycler.setItemAnimator(new SlideInRightAnimator());

        final ImageButton sendButton = view.findViewById(R.id.send_button);
        final EditText messageBox = view.findViewById(R.id.message_box);

        // Sets button onClickListener
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Shows empty message error via Toast
                if (messageBox.getText().toString().isEmpty())
                {
                    Toast t = Toast.makeText(getActivity().getApplicationContext(),
                            EMPTY_MSG_TOAST_TEXT,
                            Toast.LENGTH_SHORT);

                    t.setGravity(Gravity.BOTTOM, 0, 250);
                    t.show();
                }

                // Re-submits message list with new message
                else
                {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                    String origin = sp.getString("Origin", null);
                    application.messageViewModel.insertMessage(new Message(messageBox.getText().
                            toString(), origin));
                    mainActivity.adapter.notifyItemInserted(messageRecycler.getChildCount());
                    messageBox.setText("");
                    messageBox.clearFocus();
                }
            }
        });

        // Retains RecyclerView and EditText contents on changed configuration
        if (savedInstanceState != null)
        {
            messageBox.setText(savedInstanceState.getCharSequence(MainActivity.MSG_BOX_KEY));
        }
    }

    @Override
    public void onLongClickMessage(final Message message) {

        Bundle args = new Bundle();
        args.putSerializable(MainActivity.MESSAGE_NAV_ARG_KEY, message);

        Navigation.findNavController(this.getView()).navigate(
                R.id.action_messagingFragment_to_messageDetailsFragment, args);
    }
}
