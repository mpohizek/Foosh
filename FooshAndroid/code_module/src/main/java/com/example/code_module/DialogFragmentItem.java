package com.example.code_module;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

/**
 * Sučelje koje implementiraju DialogFragment-i za komunikaciju s aktivnostima/fragmentima koji
 * ih pozivaju
 */
public interface DialogFragmentItem {
    public DialogFragment getFragment();
    public String getName();
    public boolean getIsGenerator();
    public void destroyFragment();
    public void showFragment(FragmentManager fragmentManager, String tag);
}