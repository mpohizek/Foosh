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
    private DialogFragmentItem.FragmentCommunication context;

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

    public DialogFragmentManager(FragmentManager fragmentManager, DialogFragmentItem.FragmentCommunication context) {
        this.fragmentManager = fragmentManager;
        this.context = context;

        DialogFragmentItem dialogFragmentItem = new QRDialogFragment();
        dialogFragmentItem.setListener(context);
        addDialogFragment(dialogFragmentItem.getName(), dialogFragmentItem);

        dialogFragmentItem = new CameraDialogFragment();
        dialogFragmentItem.setListener(context);
        addDialogFragment(dialogFragmentItem.getName(), dialogFragmentItem);

        dialogFragmentItem = new GenPINDialogFragment();
        dialogFragmentItem.setListener(context);
        addDialogFragment(dialogFragmentItem.getName(), dialogFragmentItem);

        dialogFragmentItem = new ReadPINDialogFragment();
        dialogFragmentItem.setListener(context);
        addDialogFragment(dialogFragmentItem.getName(), dialogFragmentItem);
    }

    private void addDialogFragment(String key, DialogFragmentItem item){
        if (!dialogFragmentItems.containsKey(key)){
            dialogFragmentItems.put(key, item);
        }
    }
}