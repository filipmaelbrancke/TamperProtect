package net.maelbrancke.android.tamperprotect.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import net.maelbrancke.android.tamperprotect.R;
import net.maelbrancke.android.tamperprotect.environment.EnvironmentChecker;
import net.maelbrancke.android.tamperprotect.util.TamperDetectionUtils;

/**
 * Fragment for environment checks.
 */
public class EnvironmentCheckFragment extends Fragment {

    /**
     * SHA1 of the debug signing certificate (obtained through keytool)
     */
    private static final String SIGNING_CERTIFICATE_SHA1 = "80D8648557CF1D3BA3C7C2F21ACD2A423F1223AD";

    private CheckBox mPlaystoreCheckBox;
    private CheckBox mDebuggableCheckBox;
    private CheckBox mSigningKeyCheckBox;
    private PlayStoreChecker mPlayStoreChecker;
    private DebuggingChecker mDebuggingChecker;
    private SigningKeyChecker mSigningKeyChecker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_environment, container, false);
        Button button = (Button) view.findViewById(R.id.environment_check);
        button.setOnClickListener(environmentCheckButtonListener);
        mPlaystoreCheckBox = (CheckBox) view.findViewById(R.id.environment_playstore);
        mDebuggableCheckBox = (CheckBox) view.findViewById(R.id.environment_debuggable);
        mSigningKeyCheckBox = (CheckBox) view.findViewById(R.id.environment_signing_key);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayStoreChecker != null) {
            mPlayStoreChecker.setCallbackFragment(null);
        }
    }

    private void setPlaystoreCheckResult(final Boolean playstoreInstalled) {
        mPlaystoreCheckBox.setEnabled(true);
        if (playstoreInstalled == true) {
            mPlaystoreCheckBox.setChecked(true);
        } else {
            mPlaystoreCheckBox.setChecked(false);
        }
    }

    private void setDebuggableCheckResult(final Boolean debuggable) {
        mDebuggableCheckBox.setEnabled(true);
        if (debuggable == true) {
            mDebuggableCheckBox.setChecked(true);
        } else {
            mDebuggableCheckBox.setChecked(false);
        }
    }

    private void setSigningKeyCheckResult(final Boolean validSigningKey) {
        mSigningKeyCheckBox.setEnabled(true);
        if (validSigningKey == true) {
            mSigningKeyCheckBox.setChecked(true);
        } else {
            mSigningKeyCheckBox.setChecked(false);
        }
    }

    private void doEnvironmentCheck() {
        mPlayStoreChecker = new PlayStoreChecker(getActivity(), EnvironmentCheckFragment.this);
        mPlayStoreChecker.execute();
        mDebuggingChecker = new DebuggingChecker(getActivity(), EnvironmentCheckFragment.this);
        mDebuggingChecker.execute();
        mSigningKeyChecker = new SigningKeyChecker(getActivity(), EnvironmentCheckFragment.this);
        mSigningKeyChecker.execute();

    }

    View.OnClickListener environmentCheckButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            doEnvironmentCheck();
        }
    };

    static class PlayStoreChecker extends EnvironmentChecker {

        public PlayStoreChecker(Context context, EnvironmentCheckFragment fragment) {
            super(context, fragment);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean playstore = TamperDetectionUtils.isInstalledThroughPlayStore(getContext());
            return playstore;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (getCallbackFragment() != null) {
                mCallbackFragment.setPlaystoreCheckResult(result);
            }
        }
    }

    static class DebuggingChecker extends EnvironmentChecker {

        public DebuggingChecker(Context context, EnvironmentCheckFragment fragment) {
            super(context, fragment);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean debuggable = TamperDetectionUtils.isDebuggable(getContext(), true);
            return debuggable;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (getCallbackFragment() != null) {
                mCallbackFragment.setDebuggableCheckResult(result);
            }
        }
    }

    static class SigningKeyChecker extends EnvironmentChecker {

        public SigningKeyChecker(Context context, EnvironmentCheckFragment fragment) {
            super(context, fragment);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean validSigningKey = TamperDetectionUtils.isValidSigningKey(getContext(), SIGNING_CERTIFICATE_SHA1);
            return validSigningKey;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (getCallbackFragment() != null) {
                mCallbackFragment.setSigningKeyCheckResult(result);
            }
        }
    }
}
