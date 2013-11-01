package net.maelbrancke.android.tamperprotect;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class TamperCheckActivity extends Activity {

    private static final String TAG = TamperCheckActivity.class.getSimpleName();

    /**
     * SHA1 of the debug signing certificate (obtained through keytool)
     */
    private static final String SIGNING_CERTIFICATE_SHA1 = "80D8648557CF1D3BA3C7C2F21ACD2A423F1223AD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        boolean playstore = TamperDetectionUtils.isInstalledThroughPlayStore(getApplicationContext());
        boolean debuggable = TamperDetectionUtils.isDebuggable(getApplicationContext(), true);
        boolean validSigningKey = TamperDetectionUtils.isValidSigningKey(getApplicationContext(), SIGNING_CERTIFICATE_SHA1);
        StringBuilder sb = new StringBuilder("Tamper detection results:: ");
        sb.append(" playstore : ").append(playstore);
        sb.append(" / debuggable: ").append(debuggable);
        sb.append(" / valid signing key: ").append(validSigningKey);
        Log.d(TAG, sb.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tamper_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
