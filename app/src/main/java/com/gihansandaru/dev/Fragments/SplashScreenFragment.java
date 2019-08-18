package com.gihansandaru.dev.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gihansandaru.dev.Fragments.UI.HomeFragment;
import com.gihansandaru.dev.Fragments.config.BaseFragment;
import com.gihansandaru.dev.R;

import java.util.Timer;
import java.util.TimerTask;


public class SplashScreenFragment extends BaseFragment {


    public SplashScreenFragment() {
        // Required empty public constructor
    }

    public static SplashScreenFragment newInstance(){
        return new SplashScreenFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                fragmentTransactionListener .replaceFragment(HomeFragment.getInstance(), null);
            }
        }, 2000);
    }


}
