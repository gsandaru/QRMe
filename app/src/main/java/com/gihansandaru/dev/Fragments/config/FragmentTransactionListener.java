package com.gihansandaru.dev.Fragments.config;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

public interface FragmentTransactionListener {
    void replaceFragment(Fragment fragment, Bundle bundle);
    void addFragment(Fragment fragment, Bundle bundle);
    void displayMessage(String message, String title, DialogInterface.OnClickListener onClickListener);
}
