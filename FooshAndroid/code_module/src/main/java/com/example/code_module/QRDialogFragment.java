package com.example.code_module;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.UUID;

/**
 * DialogFragment za skeniranje QR koda
 */
public class QRDialogFragment extends DialogFragment implements DialogFragmentItem {
    private String mQRCode;
    private FragmentCommunication mListener;

    public QRDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Metoda koja se poziva prilikom kreiranja dijaloga koja generira novi QR kod
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.QRDialog_white);
        mQRCode = generateQRCode();
    }

    /**
     * Metoda koja kreira pogled sa slikom QR koda
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.dialogfragment_qr, container, false);
        ImageView imageView = view.findViewById(R.id.qr_code);
        imageView.setImageBitmap(createQRBitmap(mQRCode));
        mListener.onCompleted(mQRCode);
        ImageButton exitButton = (ImageButton) view.findViewById(R.id.button_exit);
        exitButton.setOnClickListener(
                new View.OnClickListener() {
                    /**
                     * Metoda za zatvaranje dijaloga
                     * @param v
                     */
                    @Override
                    public void onClick(View v) {
                        QRDialogFragment.this.dismiss();
                    }
                }
        );
        return view;
    }

    /**
     * Generiranje QR koda na temelju UUID-a
     * @return
     */
    private String generateQRCode(){
        return UUID.randomUUID().toString();
    }

    /**
     * Kreiranje Bitmap slike QR koda na temelju teksta
     * @param text
     * @return
     */
    private Bitmap createQRBitmap(String text){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Bitmap bitmap = null;
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,500,500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
        } catch (Exception e) { }

        return bitmap;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCommunication) {
            mListener = (FragmentCommunication) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentCommunication");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Metoda sučelja DialogFragmentItem-a za dohvaćanje dijaloga
     * @return
     */
    @Override
    public DialogFragment getFragment() {
        return this;
    }

    /**
     * Metoda sučelja DialogFragmentItem-a za dohvaćanje imena dijaloga
     * @param context
     * @return
     */
    @Override
    public String getName(Context context) {
        return "QRDialogFragment";
    }

    /**
     * Metoda sučelja DialogFragmentItem-a za zatvaranje dijaloga
     */
    @Override
    public void destroyFragment() {
        dismiss();
    }

    /**
     * Metoda sučelja DialogFragmentItem-a za prikaz dijaloga
     * @param fragmentManager
     * @param tag
     */
    @Override
    public void showFragment(FragmentManager fragmentManager, String tag) {
        show(fragmentManager, tag);
    }
}