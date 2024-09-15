package com.example.connectfourgame;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;

public class FragmentUtils {

    private static final String TAG = "FragmentUtils";

    public static void replaceFragment(FragmentManager fragmentManager, Fragment fragment, int containerId) {
        Log.d(TAG, "Replacing fragment: " + fragment.getClass().getSimpleName());
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static void addFragment(FragmentManager fragmentManager, Fragment fragment, int containerId) {
        Log.d(TAG, "Adding fragment: " + fragment.getClass().getSimpleName());
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerId, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
