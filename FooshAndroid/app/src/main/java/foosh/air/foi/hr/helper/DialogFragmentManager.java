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
    //public static final int QRCODE = 0;
    //public static final int QRSCANNER = 1;

    private FragmentManager fragmentManager;

    private Map<String, DialogFragmentItem> dialogFragmentItems;
    {
        dialogFragmentItems = new HashMap<>();
    }

    public Map<String, DialogFragmentItem> getDialogFragmentItems() {
        return dialogFragmentItems;
    }

    public void getResult(String key){
        dialogFragmentItems.get(key).showFragment(fragmentManager,"F");
    }

    public DialogFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        DialogFragmentItem dialogFragmentItem = new QRDialogFragment();
        addDialogFragment(dialogFragmentItem.getName(), dialogFragmentItem);

        dialogFragmentItem = new CameraDialogFragment();
        addDialogFragment(dialogFragmentItem.getName(), dialogFragmentItem);



        dialogFragmentItem = new QRDialogFragment();
        addDialogFragment(dialogFragmentItem.getName(), dialogFragmentItem);

        dialogFragmentItem = new CameraDialogFragment();
        addDialogFragment(dialogFragmentItem.getName(), dialogFragmentItem);
    }


    public void addDialogFragment(String key, DialogFragmentItem item){
        dialogFragmentItems.put(key, item);
    }

}
