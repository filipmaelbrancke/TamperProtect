package net.maelbrancke.android.tamperdetection.task;

import android.os.AsyncTask;

import net.maelbrancke.android.tamperdetection.ui.RootCheckFragment;

/**
 * Root detector AsyncTask.
 */
public abstract class RootDetector extends AsyncTask<Void, Void, Boolean> {
    protected RootCheckFragment mCallbackFragment;

    public RootDetector(final RootCheckFragment fragment) {
        mCallbackFragment = fragment;
    }

    public void setCallbackFragment(RootCheckFragment callbackFragment) {
        mCallbackFragment = callbackFragment;
    }

    public RootCheckFragment getCallbackFragment() {
        return mCallbackFragment;
    }
}
