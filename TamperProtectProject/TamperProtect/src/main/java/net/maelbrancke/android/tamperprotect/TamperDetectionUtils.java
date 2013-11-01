package net.maelbrancke.android.tamperprotect;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

/**
 * Tamper detection utilities.
 */
public class TamperDetectionUtils {


    public static boolean isInstalledThroughPlayStore(final Context context) {
        boolean playStoreInstalled = false;
        PackageManager packageManager = context.getPackageManager();
        final ApplicationInfo applicationInfo = context.getApplicationInfo();
        final String packageInstallerName = packageManager.getInstallerPackageName(applicationInfo.packageName);
        if ("com.android.vending".equals(packageInstallerName)) {
            // App is installed through the Play Store
            playStoreInstalled = true;
        }
        return playStoreInstalled;
    }

    /**
     * Check whether the app is currently running in the emulator.
     * There is no supported mechanism in the Android SDK to determine this.
     * We try to determine this by looking through the system properties,
     * via the {@link android.os.Build} class.
     *
     * @return whether the app seems to run on an emulator
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
}
