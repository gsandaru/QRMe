package com.gihansandaru.dev.Fragments.config;

import android.content.Context;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {


    protected FragmentTransactionListener fragmentTransactionListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentTransactionListener) {
            fragmentTransactionListener = (FragmentTransactionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentTransactionListener");
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
        fragmentTransactionListener = null;
    }
}
