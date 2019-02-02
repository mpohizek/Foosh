package com.example.pin_module;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.example.base.interfaces.DialogFragmentItem;

import java.security.SecureRandom;
import java.text.DecimalFormat;

public class GenPINDialogFragment extends DialogFragment implements DialogFragmentItem {
    private DialogFragmentItem.FragmentCommunication mListener;

    private String pin;
    private PinEntryEditText pinEntry;

    public GenPINDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Postavljanje stila PIN dijaloga
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Light_NoTitleBar);
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

        View view = inflater.inflate(R.layout.dialogfragment_genpin, container, false);

        pinEntry = view.findViewById(R.id.txt_pin_entry);;
        pin = generatePIN(6);

        pinEntry.setMaxLength(6);
        pinEntry.setOnClickListener(null);
        mListener.onCompleted(pin);
        ImageButton exitButton = (ImageButton) view.findViewById(R.id.button_exit);
        exitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GenPINDialogFragment.this.dismiss();
                    }
                }
        );

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        pinEntry.setText(pin);
    }

    /**
     * Metoda za generiranje PIN-a (max 8 brojeva)
     * @param length
     * @return
     */
    private String generatePIN(int length){
        if (length > 8)
            length = 8;
        int bound = 1;
        StringBuilder form = new StringBuilder();
        for (int i = 0; i < length; i++){
            bound *= 10;
            form.append('0');
        }
        SecureRandom generator = new SecureRandom();
        int num = generator.nextInt(bound) % bound;

        DecimalFormat format = new DecimalFormat(form.toString());
        return format.format(num);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        return "GenPINDialogFragment";
    }

    @Override
    public String getShortName() {
        return "Generiraj PIN";
    }

    @Override
    public boolean getIsGenerator() {
        return true;
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