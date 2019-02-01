package com.example.base.interfaces;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

/**
 * Sučelje koje implementiraju DialogFragment-i za komunikaciju s aktivnostima/fragmentima koji
 * ih pozivaju
 */
public interface DialogFragmentItem {
    public DialogFragment getFragment();
    public String getName();
    public String getShortName();
    public boolean getIsGenerator();
    public void destroyFragment();
    public void showFragment(FragmentManager fragmentManager, String tag);
}