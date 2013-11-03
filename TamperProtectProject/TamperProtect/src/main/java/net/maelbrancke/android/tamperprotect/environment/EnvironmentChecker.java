package net.maelbrancke.android.tamperprotect.environment;

import android.content.Context;
import android.os.AsyncTask;

import net.maelbrancke.android.tamperprotect.ui.EnvironmentCheckFragment;

/**
 * Environment-checking AsyncTask.
 */
public abstract class EnvironmentChecker extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    protected EnvironmentCheckFragment mCallbackFragment;

    public EnvironmentChecker(final Context context, final EnvironmentCheckFragment fragment) {
        mContext = context;
        mCallbackFragment = fragment;
    }

    public void setCallbackFragment(EnvironmentCheckFragment callbackFragment) {
        mCallbackFragment = callbackFragment;
    }

    public Context getContext() {
        return mContext;
    }

    public EnvironmentCheckFragment getCallbackFragment() {
        return mCallbackFragment;
    }
}
