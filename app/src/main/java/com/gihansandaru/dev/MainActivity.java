package com.gihansandaru.dev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.gihansandaru.dev.Fragments.SplashScreenFragment;
import com.gihansandaru.dev.Fragments.UI.GenerateFragment;
import com.gihansandaru.dev.Fragments.UI.HomeFragment;
import com.gihansandaru.dev.Fragments.UI.ResultFragment;
import com.gihansandaru.dev.Fragments.config.FragmentTransactionListener;
import com.gihansandaru.dev.Utils.PermissionUtil;
import com.gihansandaru.dev.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements FragmentTransactionListener  {

    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        addFragment(SplashScreenFragment.newInstance(),null);
        PermissionUtil.on().requestPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);

     }



    @Override
    public void replaceFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.coordinator_layout_fragment_container, fragment).commit();
    }

    @Override
    public void addFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.coordinator_layout_fragment_container, fragment).commit();
    }

    @Override
    public void displayMessage(String message,String title,DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Message");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Ok",  onClickListener);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.coordinator_layout_fragment_container);
        if (currentFragment instanceof SplashScreenFragment
                ||currentFragment instanceof HomeFragment
                ||currentFragment instanceof GenerateFragment) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Are you sure to Exit ?");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    finish();
                    System.exit(0);

                }
            });
            alertDialog.show();
            return;

        }

        if (currentFragment  instanceof ResultFragment) {
            replaceFragment(HomeFragment.getInstance(),null);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }
}
