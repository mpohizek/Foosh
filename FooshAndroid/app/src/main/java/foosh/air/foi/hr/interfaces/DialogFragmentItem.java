package foosh.air.foi.hr.interfaces;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

/**
 * Sučelje koje implementiraju DialogFragment-i za komunikaciju s aktivnostima/fragmentima koji
 * ih pozivaju
 */
public interface DialogFragmentItem {
    public DialogFragment getFragment();
    public String getName(Context context);
    public void destroyFragment();
    public void showFragment(FragmentManager fragmentManager, String tag);

    /**
     * Sučelje za komunikaciju QRDialogFragmenta s aktivnošću pozivatelja
     */
    public interface FragmentCommunicationQRDialog {
        public void onQRShownfromActivity (DialogFragmentItem self, String qrCode);
    }

    /**
     * Sučelje za komunikaciju CameraDialogFragmenta s aktivnošću pozivatelja
     */
    public interface FragmentCommunicationCameraDialog {
        public void onQRScannedfromActivity (DialogFragmentItem self, String qrCode);
    }
}