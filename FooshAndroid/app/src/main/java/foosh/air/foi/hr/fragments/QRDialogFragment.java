package foosh.air.foi.hr.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

import foosh.air.foi.hr.DialogFragmentItem;
import foosh.air.foi.hr.R;

public class QRDialogFragment extends DialogFragment implements DialogFragmentItem {
    private String mQRCode;
    private OnQRBitmapListener mListener;

    public QRDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.QRDialog_white);
        mQRCode = generateQRCode();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.dialogfragment_qr, container, false);
        ImageView imageView = view.findViewById(R.id.qr_code);
        imageView.setImageBitmap(createQRBitmap(mQRCode));
        mListener.onQRShown(this, mQRCode);
        ImageButton exitButton = (ImageButton) view.findViewById(R.id.button_exit);
        exitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QRDialogFragment.this.dismiss();
                    }
                }
        );

        return view;
    }

    private String generateQRCode(){
        return UUID.randomUUID().toString();
    }

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
        if (context instanceof OnQRBitmapListener) {
            mListener = (OnQRBitmapListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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

    @Override
    public String getName(Context context) {
        return "QRDialogFragment";
    }

    @Override
    public void destroyFragment() {
        dismiss();
    }

    @Override
    public void showFragment(FragmentManager fragmentManager, String tag) {
        show(fragmentManager, tag);
    }

    public interface OnQRBitmapListener{
        void onQRShown(DialogFragmentItem self, String qrCode);
    }
}