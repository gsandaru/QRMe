package com.gihansandaru.dev.Fragments.UI;


import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.gihansandaru.dev.Fragments.config.BaseFragment;
import com.gihansandaru.dev.R;
import com.gihansandaru.dev.Utils.PermissionUtil;
import com.gihansandaru.dev.databinding.FragmentScanBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class HomeFragment extends BaseFragment implements  View.OnClickListener , BottomNavigationView.OnNavigationItemSelectedListener {

    FragmentScanBinding fragmentScanBinding;
    private boolean mIsFlashOn;
    private DecoratedBarcodeView decoratedBarcodeView;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment getInstance(){
        return new HomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       fragmentScanBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_scan, container, false);
        return fragmentScanBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fragmentScanBinding.scanBottomNavigationBar.setSelectedItemId(R.id.menuItemScan);
        fragmentScanBinding.scanBottomNavigationBar.setOnNavigationItemSelectedListener(this);
        fragmentScanBinding.scanHomeToolbar.setTitle("SCAN");
        decoratedBarcodeView = fragmentScanBinding.scanDecoratedBarcodeView;
        fragmentScanBinding.scanDecoratedBarcodeView.resume();
        fragmentScanBinding.scanDecoratedBarcodeView.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                decoratedBarcodeView.pause();
                if(result.getBitmap() != null) {
                    File rootFolder = new File(Environment.getExternalStorageDirectory(), "QRme");
                    if (!rootFolder.exists()) {
                        rootFolder.mkdir();
                    }
                    String fileID = String.valueOf(System.currentTimeMillis());
                    File fileToSave = new File(rootFolder.getAbsolutePath() +"/" + fileID + ".png");

                    try (FileOutputStream out = new FileOutputStream(fileToSave)) {
                        result.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("FILE_ID_TO_OPEN",fileID);
                    bundle.putString("DECODED_TEXT",result.getText());
                    bundle.putString("OPEN_MODE","SCAN");
                    fragmentTransactionListener.replaceFragment(ResultFragment.getInstance(),bundle);

                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });

        fragmentScanBinding.scanBtnFlasher.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan_btn_flasher:

                if (mIsFlashOn) {
                    decoratedBarcodeView.setTorchOff();
                } else {
                    decoratedBarcodeView.setTorchOn();
                }

                mIsFlashOn = !mIsFlashOn;
                break;


            default:
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        decoratedBarcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        decoratedBarcodeView.pause();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.menuItemGenerate:
                fragmentTransactionListener.replaceFragment(GenerateFragment.getInstance(),null);
                break;
            case R.id.menuItemSaved:
                break;
            case R.id.menuItemScan:
                fragmentTransactionListener.replaceFragment(HomeFragment.getInstance(),null);

                break;
        }
        return false;
    }
}

