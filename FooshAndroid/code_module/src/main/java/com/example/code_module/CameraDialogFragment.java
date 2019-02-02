package com.example.code_module;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.base.interfaces.DialogFragmentItem;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Collections;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Fragment za skeniranje QR koda kamerom uređaja
 */
public class CameraDialogFragment extends DialogFragment implements ZXingScannerView.ResultHandler, DialogFragmentItem {
    private static final int MY_CAMERA_REQUEST_CODE = 101;

    private DialogFragmentItem.FragmentCommunication mListener;

    private ZXingScannerView zXingScannerView;

    public CameraDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Postavljanje stila QR dijaloga i zahtjevanja prava pristupa kameri uređaja
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar);
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            if (getActivity().checkSelfPermission(android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                getActivity().requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }
        }
    }

    /**
     * Inflate dijaloga i postavljane button eventa
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.dialogfragment_camera, container, false);

        ViewGroup viewGroup = view.findViewById(R.id.camera_preview);
        zXingScannerView = new ZXingScannerView(getActivity());
        zXingScannerView.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));
        zXingScannerView.setAutoFocus(true);
        viewGroup.addView(zXingScannerView);

        ImageButton exitButton = (ImageButton) view.findViewById(R.id.button_exit);
        exitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zXingScannerView.stopCamera();
                        CameraDialogFragment.this.dismiss();
                    }
                }
        );

        return view;
    }

    /**
     * Na pauzu dijalog za skeniranje se zatvara
     */
    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    /**
     * dohvaćanje rezultata sekiranja i slanje rezultata u aktivnost koja implemenitra OnQRCameraListener sučelje
     * @param result
     */
    @Override
    public void handleResult(Result result) {
        try {
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(200);
        } catch(Exception ok){
            Log.d("Vibration", "Cannot vibrate");
        }

        mListener.onCompleted(result.getText());
    }

    /**
     * Ispisavanje poruke u slučaju prihvaćanja/odbijanja pristupa kameri uređaja
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Camera permission granted!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Camera permission denied!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public DialogFragment getFragment() {
        return this;
    }

    /**
     * Vraća ime fragmenta
     * @return
     */
    @Override
    public String getName() {
        return "CameraDialogFragment";
    }

    @Override
    public String getShortName() {
        return "Skeniraj QR kod";
    }

    @Override
    public boolean getIsGenerator() {
        return false;
    }

    @Override
    public void destroyFragment() {
        dismiss();
    }

    @Override
    public void showFragment(FragmentManager fragmentManager, String tag) {
        show(fragmentManager, tag);
    }

    @Override
    public DialogFragment setListener(FragmentCommunication context) {
        mListener = context;
        return this;
    }
}