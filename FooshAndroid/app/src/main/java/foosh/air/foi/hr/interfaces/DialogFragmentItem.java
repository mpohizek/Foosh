package foosh.air.foi.hr.interfaces;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

public interface DialogFragmentItem {
    public DialogFragment getFragment();
    public String getName(Context context);
    public void destroyFragment();
    public void showFragment(FragmentManager fragmentManager, String tag);

    public interface FragmentCommunicationQRDialog {
        public void onQRShownfromActivity (DialogFragmentItem self, String qrCode);
    }

    public interface FragmentCommunicationCameraDialog {
        public void onQRScannedfromActivity (DialogFragmentItem self, String qrCode);
    }
}