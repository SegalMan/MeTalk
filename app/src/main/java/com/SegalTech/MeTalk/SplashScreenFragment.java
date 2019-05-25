package com.SegalTech.MeTalk;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.SegalTech.MeTalk.R;
import com.race604.drawable.wave.WaveDrawable;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashScreenFragment extends Fragment {

    public SplashScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        final MainActivity mainActivity = (MainActivity)getActivity();
        MeTalk application = (MeTalk) mainActivity.getApplication();
        application.messageViewModel.getUsername().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                int destinationId;
                if ((s != null) && (!s.equals("")))
                {
                    destinationId = R.id.messagingFragment;
                }
                else
                {
                    destinationId = R.id.configureNameFragment;
                }
                Navigation.findNavController(view).navigate(destinationId);
            }
        });

        ImageView mImageView = view.findViewById(R.id.splashImage);
        WaveDrawable mWaveDrawable = new WaveDrawable(getContext(), R.mipmap.splash);
        mWaveDrawable.setIndeterminate(true);
        mImageView.setImageDrawable(mWaveDrawable);
    }
}
