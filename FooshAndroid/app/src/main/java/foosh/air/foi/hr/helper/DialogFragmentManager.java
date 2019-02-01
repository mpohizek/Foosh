package foosh.air.foi.hr.helper;

import android.support.v4.app.FragmentManager;

import com.example.code_module.CameraDialogFragment;
import com.example.code_module.DialogFragmentItem;
import com.example.code_module.QRDialogFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility klasa za pohranu svih DialogFragmenat-a, odnosno DialogFragmentItem-a
 */
public class DialogFragmentManager {
    public static final int QRCODE = 0;
    public static final int QRSCANNER = 1;

    private FragmentManager fragmentManager;


    private Map<Integer, DialogFragmentItem> dialogFragmentItems;
    {
        dialogFragmentItems = new HashMap<>();
    }

    public void getResult(int code){
        addDialogFragmentItems(code);
        dialogFragmentItems.get(code).showFragment(fragmentManager, "F");
    }

    public DialogFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    /**
     * Dohvaćanje DialogFragmentItem-a na temelju ključa
     * @param key
     * @return
     */
    public DialogFragmentItem getDialogFragment(int key){
        return dialogFragmentItems.get(key);
    }

    public void addDialogFragmentItems(int code) {
        if (!dialogFragmentItems.containsKey(code)){
            DialogFragmentItem dfi;
            switch (code){
                case QRCODE:
                    dfi = new QRDialogFragment();
                    break;
                case QRSCANNER:
                    dfi = new CameraDialogFragment();
                    break;
                default:
                    dfi = new CameraDialogFragment();
            }
            dialogFragmentItems.put(code, dfi);
        }
    }
}
