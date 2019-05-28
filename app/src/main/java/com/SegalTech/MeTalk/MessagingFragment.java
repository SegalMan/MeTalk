package com.SegalTech.MeTalk;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

import java.util.List;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messaging, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        final MainActivity mainActivity = (MainActivity)getActivity();
        if (mainActivity == null)
        {
            return;
        }
        final MeTalk application = (MeTalk) mainActivity.getApplication();

        final MessageRecyclerUtils.MessageAdapter messageRecyclerAdapter = mainActivity.getAdapter();

        application.messageViewModel.getAllMessages().observe(this,
                new Observer<List<Message>>() {
                    @Override
                    public void onChanged(List<Message> messages) {
                        messageRecyclerAdapter.submitList(messages);
                    }
                });

        mainActivity.setUsername(application.messageViewModel.getUsername().getValue());

        String username = mainActivity.getUsername();

        if (!username.equals(""))
        {
            TextView usernameTextView = view.findViewById(R.id.username);
            String usernameText = username + "'s messages";
            usernameTextView.setText(usernameText);
        }


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                MainActivity mainActivity = (MainActivity)getActivity();

                if (mainActivity == null)
                {
                    return;
                }
                int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();

                if (count == 0)
                {
                    if (getContext() == null)
                    {
                        return;
                    }
                    new AlertDialog.Builder(getContext()).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                            .setMessage("Are you sure you want to exit?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().finish();
                                }
                            }).setNegativeButton("No", null).show();
                }
                else
                {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // Configure RecyclerView
        final RecyclerView messageRecycler = view.findViewById(R.id.message_recycler);
        messageRecycler.setLayoutManager(new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false));
        messageRecycler.setAdapter(messageRecyclerAdapter);
        messageRecyclerAdapter.callback = this;
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
                    Toast t = Toast.makeText(mainActivity.getApplicationContext(),
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
                    messageRecyclerAdapter.notifyItemInserted(messageRecycler.getChildCount());
                    messageBox.setText("");
                    messageBox.clearFocus();
                }
            }
        });
    }

    @Override
    public void onLongClickMessage(final Message message) {

        Bundle args = new Bundle();
        args.putSerializable(MainActivity.MESSAGE_NAV_ARG_KEY, message);

        View view = this.getView();

        if (view != null)
        {
            Navigation.findNavController(this.getView()).navigate(
                    R.id.action_messagingFragment_to_messageDetailsFragment, args);
        }

    }
}
