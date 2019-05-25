package com.SegalTech.MeTalk;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigureNameFragment extends Fragment {

    public ConfigureNameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configure_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
        Button skipButton = view.findViewById(R.id.skip_button);
        final EditText usernameBox = view.findViewById(R.id.username_box);
        Button proceedButton = view.findViewById(R.id.proceed_button);
        final MeTalk application = (MeTalk) getActivity().getApplication();
        skipButton.setOnClickListener(Navigation.createNavigateOnClickListener(
                R.id.action_configureNameFragment_to_messagingFragment));
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                application.messageViewModel.insertUsername(usernameBox.getText().toString());
                Navigation.findNavController(v).navigate(
                        R.id.action_configureNameFragment_to_messagingFragment);
            }
        });
    }
}
