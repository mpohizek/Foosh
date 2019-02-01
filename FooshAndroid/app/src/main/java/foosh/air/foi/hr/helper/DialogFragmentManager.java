package foosh.air.foi.hr.helper;

import android.support.v4.app.FragmentManager;

import com.example.base.interfaces.DialogFragmentItem;
import com.example.code_module.CameraDialogFragment;
import com.example.code_module.QRDialogFragment;
import com.example.pin_module.GenPINDialogFragment;
import com.example.pin_module.ReadPINDialogFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility klasa za pohranu svih DialogFragmenat-a, odnosno DialogFragmentItem-a
 */
public class DialogFragmentManager {
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

        dialogFragmentItem = new GenPINDialogFragment();
        addDialogFragment(dialogFragmentItem.getName(), dialogFragmentItem);

        dialogFragmentItem = new ReadPINDialogFragment();
        addDialogFragment(dialogFragmentItem.getName(), dialogFragmentItem);

    }


    private void addDialogFragment(String key, DialogFragmentItem item){
        if (!dialogFragmentItems.containsKey(key)){
            dialogFragmentItems.put(key, item);
        }
    }

}
