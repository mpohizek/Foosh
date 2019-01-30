package foosh.air.foi.hr.helper;

import java.util.HashMap;
import java.util.Map;

import foosh.air.foi.hr.interfaces.DialogFragmentItem;

/**
 * Utility klasa za pohranu svih DialogFragmenat-a, odnosno DialogFragmentItem-a
 */
public class DialogFragmentManager {
    /**
     * Singleton klase DialogFragmentManager-a
     * @return
     */
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

    /**
     * Dodavanja novog DialogFragmentaItem-a u HashMap-u elemenata na temelju ključa
     * @param key
     * @param dialogFragmentItem
     */
    public void addDialogFragment(String key, DialogFragmentItem dialogFragmentItem){
        if (!dialogFragmentItems.keySet().contains(key)){
            dialogFragmentItems.put(key, dialogFragmentItem);
        }
    }

    /**
     * Dohvaćanje DialogFragmentItem-a na temelju ključa
     * @param key
     * @return
     */
    public DialogFragmentItem getDialogFragment(String key){
        return dialogFragmentItems.get(key);
    }
}
