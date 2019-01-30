package foosh.air.foi.hr.helper;

import java.util.HashMap;
import java.util.Map;

import foosh.air.foi.hr.interfaces.DialogFragmentItem;

public class DialogFragmentManager {
    private static DialogFragmentManager instance;
    public static DialogFragmentManager getInstance(){
        if (instance == null){
            instance = new DialogFragmentManager();
        }
        return instance;
    }

    private Map<String, DialogFragmentItem> dialogFragmentItems;
    {
        dialogFragmentItems = new HashMap<>();
    }

    public void addDialogFragment(String key, DialogFragmentItem dialogFragmentItem){
        if (!dialogFragmentItems.keySet().contains(key)){
            dialogFragmentItems.put(key, dialogFragmentItem);
        }
    }

    public DialogFragmentItem getDialogFragment(String key){
        return dialogFragmentItems.get(key);
    }
}
