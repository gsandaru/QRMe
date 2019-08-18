package com.gihansandaru.dev.Fragments.UI;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.gihansandaru.dev.Fragments.config.BaseFragment;
import com.gihansandaru.dev.R;
import com.gihansandaru.dev.Utils.PermissionUtil;
import com.gihansandaru.dev.Utils.ProgressDialogUtil;
import com.gihansandaru.dev.databinding.FragmentGenerateBinding;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenerateFragment extends BaseFragment {


    FragmentGenerateBinding fragmentGenerateBinding;
    public GenerateFragment() {
        // Required empty public constructor
    }

    public static GenerateFragment getInstance(){
        return new GenerateFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentGenerateBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_generate, container, false);
        return fragmentGenerateBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentGenerateBinding.generateBottomNavigationBar.setSelectedItemId(R.id.menuItemGenerate);
        fragmentGenerateBinding.generateHomeToolbar.setTitle("Generate QR Code");

        fragmentGenerateBinding.generateBtnFlasher.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(fragmentGenerateBinding.generateEdittext.getText().toString().trim())) {
                String content = fragmentGenerateBinding.generateEdittext.getText().toString().trim();
                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(content,BarcodeFormat.QR_CODE, 1000, 1000);
                    File rootFolder = new File(Environment.getExternalStorageDirectory(), "QRme");
                    if (!rootFolder.exists()) {
                        rootFolder.mkdir();
                    }
                    File generatedFolder = new File(Environment.getExternalStorageDirectory() + "/QRme", "generated");
                    if (!generatedFolder.exists()) {
                        generatedFolder.mkdir();
                    }
                    String fileID = String.valueOf(System.currentTimeMillis());
                    File fileToSave = new File(Environment.getExternalStorageDirectory() + "/QRme/generated/" + fileID + ".png");

                    try (FileOutputStream out = new FileOutputStream(fileToSave)) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("FILE_ID_TO_OPEN",fileID);
                    bundle.putString("DECODED_TEXT",content);
                    bundle.putString("OPEN_MODE","GENERATED");
                    fragmentTransactionListener.replaceFragment(ResultFragment.getInstance(),bundle);

                } catch (Exception e) {
                    if (!TextUtils.isEmpty(e.getMessage())) {
                        Log.e(getClass().getSimpleName(), e.getMessage());
                    }

                }
            }

        });


    }
}
