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

public class ReadPINDialogFragment extends DialogFragment implements DialogFragmentItem {
    private DialogFragmentItem.FragmentCommunication mListener;

    private PinEntryEditText pinEntry;

    public ReadPINDialogFragment() {
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

        View view = inflater.inflate(R.layout.dialogfragment_readpin, container, false);

        pinEntry = view.findViewById(R.id.txt_pin_entry);;
        pinEntry.setMaxLength(6);
        pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                StringBuilder stringBuilder = new StringBuilder(str.length());
                stringBuilder.append(str);
                mListener.onCompleted(stringBuilder.toString());
            }
        });
        ImageButton exitButton = (ImageButton) view.findViewById(R.id.button_exit);
        exitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ReadPINDialogFragment.this.dismiss();
                    }
                }
        );

        return view;
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
        return "ReadPINDialogFragment";
    }

    @Override
    public String getShortName() {
        return "Učitaj PIN";
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