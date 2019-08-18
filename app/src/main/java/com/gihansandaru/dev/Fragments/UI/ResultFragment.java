package com.gihansandaru.dev.Fragments.UI;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.content.IntentCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.gihansandaru.dev.Fragments.config.BaseFragment;
import com.gihansandaru.dev.R;
import com.gihansandaru.dev.databinding.FragmentResultBinding;

import java.io.File;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends BaseFragment implements View.OnClickListener {

    FragmentResultBinding fragmentResultBinding;
    private String file_id_to_open;
    private String decoded_text;
    private String open_mode;

    public ResultFragment() {
        // Required empty public constructor
    }

    public static ResultFragment getInstance() {
        return new ResultFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentResultBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false);
        return fragmentResultBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentResultBinding.homeToolbar.setTitle("Decoded QR Code");
        fragmentResultBinding.homeToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        fragmentResultBinding.homeToolbar.setNavigationOnClickListener(v ->
                fragmentTransactionListener.replaceFragment(HomeFragment.getInstance(), null)
        );
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            file_id_to_open = bundle.getString("FILE_ID_TO_OPEN", "");
            decoded_text = bundle.getString("DECODED_TEXT", "");
            open_mode = bundle.getString("OPEN_MODE", "");

            File rootFolder = new File(Environment.getExternalStorageDirectory(), "QRme");
            File fileToSave = null;
            if(open_mode.equals("GENERATED")){
                File generatedFolder = new File(rootFolder.getAbsolutePath(), "generated");
                fileToSave =  new File(generatedFolder.getAbsolutePath() +"/" + file_id_to_open + ".png");
            }else{
                fileToSave =  new File(rootFolder.getAbsolutePath() +"/" + file_id_to_open + ".png");
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(fileToSave.getAbsolutePath(), options);
            fragmentResultBinding.imageviewResult.setImageBitmap(bitmap);
            fragmentResultBinding.resultEdittext.setText(decoded_text);
            fragmentResultBinding.btnShare.setOnClickListener(this);
            fragmentResultBinding.btnOpen.setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share:
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("QRMe Data", decoded_text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(),"Content Copied to Clipboard",Toast.LENGTH_LONG).show();


                File rootFolder = new File(Environment.getExternalStorageDirectory(), "QRme");

                File fileToSave = new File(rootFolder.getAbsolutePath() + "/" + file_id_to_open + ".png");

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getContext(),
                            getString(R.string.file_provider_authority), fileToSave));
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileToSave));
                }

                startActivity(Intent.createChooser(shareIntent, "Share QR Image Using.."));
                break;
            case R.id.btn_open:
                if (!decoded_text.equals("")
                        && URLUtil.isValidUrl(decoded_text)) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(decoded_text));
                    startActivity(browserIntent);
                }
                break;
            default:
                break;

        }
    }
}
