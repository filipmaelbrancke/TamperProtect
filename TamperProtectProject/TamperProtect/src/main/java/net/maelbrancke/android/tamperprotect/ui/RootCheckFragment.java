package net.maelbrancke.android.tamperprotect.ui;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import net.maelbrancke.android.tamperprotect.R;
import net.maelbrancke.android.tamperprotect.task.RootDetector;
import net.maelbrancke.android.tamperprotect.util.RootDetectionUtils;


/**
 * Fragment for root detection.
 */
public class RootCheckFragment extends Fragment {

    private CheckBox mRootedBaseCheckBox;
    private CheckBox mRootedExtendedCheckBox;
    private RootedBaseDetector mRootedBaseDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooted, container, false);
        Button button = (Button) view.findViewById(R.id.rooted_check);
        button.setOnClickListener(rootDetectionButtonListener);
        mRootedBaseCheckBox = (CheckBox) view.findViewById(R.id.rooted_base_check);
        mRootedExtendedCheckBox = (CheckBox) view.findViewById(R.id.rooted_extended_check);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRootedBaseDetector != null) {
            mRootedBaseDetector.setCallbackFragment(null);
        }
    }

    private void setBaseRootedCheckResult(final Boolean rooted) {
        setCheckResult(rooted, mRootedBaseCheckBox);
    }

    private void setCheckResult(final Boolean enabled, final CheckBox checkBox) {
        checkBox.setEnabled(true);
        if (enabled) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    private void doRootDetection() {
        mRootedBaseDetector = new RootedBaseDetector(RootCheckFragment.this);
        mRootedBaseDetector.execute();
    }

    View.OnClickListener rootDetectionButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            doRootDetection();
        }
    };

    static class RootedBaseDetector extends RootDetector {

        public RootedBaseDetector(RootCheckFragment fragment) {
            super(fragment);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean rooted = RootDetectionUtils.isRooted();
            return rooted;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (getCallbackFragment() != null) {
                mCallbackFragment.setBaseRootedCheckResult(result);
            }
        }
    }
}
