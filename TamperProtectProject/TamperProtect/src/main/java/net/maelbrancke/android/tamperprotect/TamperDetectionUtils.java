package net.maelbrancke.android.tamperprotect;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;


/**
 * Tamper detection utilities.
 */
public class TamperDetectionUtils {



    /**
     * Check whether the app is currently running in the emulator.
     * There is no supported mechanism in the Android SDK to determine this.
     * We try to determine this by looking through the system properties,
     * via the {@link android.os.Build} class.
     *
     * @return true when the app seems to run on an emulator, otherwise false
     */
    public static boolean isRunningInEmulator() {
        boolean runningInEmulator = false;
        if (Build.BRAND.equalsIgnoreCase("generic")) {
            runningInEmulator = true;
        } else if (Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK")) {
            runningInEmulator = true;
        } else if (Build.PRODUCT.contains("sdk") || Build.PRODUCT.equalsIgnoreCase("full_x86")) {
            runningInEmulator = true;
        }
        return runningInEmulator;
    }

    /**
     * Check whether the application is installed through the Play Store.
     *
     * @param context Context
     * @return true when the app is installed through the Play store, otherwise false
     */
    public static boolean isInstalledThroughPlayStore(final Context context) {
        boolean playStoreInstalled = false;
        PackageManager packageManager = context.getPackageManager();
        final String packageInstallerName = packageManager.getInstallerPackageName(getPackageName(context));
        if ("com.android.vending".equals(packageInstallerName)) {
            // App is installed through the Play Store
            playStoreInstalled = true;
        }
        return playStoreInstalled;
    }

    /**
     * Check whether the application is debuggable.
     * The first check is to see what the PackageManager reports.
     * If wanted, an additional check can be performed by looking at the certificate.
     * The default auto-generated certificate has the DN 'CN=Android Debug,O=Android,C=US',
     * as described at http://developer.android.com/tools/publishing/app-signing.html#debugmode
     * If the app's DN matches this default, it is probably using the debug certificate.
     *
     * @param context Context
     * @return true when the app is debuggable, otherwise false
     */
    public static boolean isDebuggable(final Context context, final boolean includeDefaultDebugCertificateCheck) {
        boolean debuggable = ( 0 != ( context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) );
        if (!debuggable && includeDefaultDebugCertificateCheck) {
            debuggable = isDebugCertificateCheck(context);
        }
        return debuggable;
    }

    private static boolean isDebugCertificateCheck(final Context context) {
        final X500Principal DEBUG_CERTIFICATE_DN = new X500Principal("CN=Android Debug,O=Android,C=US");
        boolean debuggable = false;

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo( getPackageName(context), PackageManager.GET_SIGNATURES );
            Signature[] signatures = packageInfo.signatures;

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            for (int i = 0; i < signatures.length; i++) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
                debuggable = certificate.getSubjectX500Principal().equals(DEBUG_CERTIFICATE_DN);
                if (debuggable) {
                    break;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            // package not found - debuggable = false
        } catch (CertificateException e) {
            // certificate factory non-instantiable - debuggable = false
        }
        return debuggable;
    }

    /**
     * Get the application package name.
     *
     * @param context Context
     * @return package name
     */
    public static String getPackageName(final Context context) {
        return context.getPackageName();
    }

}
