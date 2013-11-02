package net.maelbrancke.android.tamperprotect;

import android.os.Build;

import java.io.File;

/**
 * Some common root detection utilities.
 */
public class RootDetectionUtils {

    /**
     * Try to determine whether running on a rooted device.
     *
     * @return true when the app seems to run on a rooted device, otherwise false
     */
    public static boolean isRooted() {
        if (isRootedSigningKeys()) {
            return true;
        } else if (isRootedSuperuserBinary()) {
            return true;
        } else if (isRootedRunCommand()) {
            return true;
        }
        return false;
    }

    /**
     * Try to determine whether running on a rooted device by looking at the build tags,
     * and checking for 'test-keys', which marks a rom build with the test keys of aosp.
     * Most roms nowadays are signed with other private keys that may or may not still use this
     * label for the keys.
     *
     * @return true when the app seems to run on a rooted device, otherwise false
     */
    public static boolean isRootedSigningKeys() {
        final String buildTags = Build.TAGS;

        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }

        return false;
    }

    /**
     * Try to determine whether running on a rooted device by trying to call su and run
     * a command.
     *
     * @return true when the app seems to run on a rooted device, otherwise false
     */
    public static boolean isRootedRunCommand() {
        if (new ExecShell().executeCommand(ExecShell.SHELL_CMD.check_su_binary) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Try to determine whether running on a rooted device by checking for the existence for
     * binaries.
     *
     * @return true when the app seems to run on a rooted device, otherwise false
     */
    public static boolean isRootedSuperuserBinary() {
        return fileExists("/system/app/Superuser.apk");
    }

    private static boolean fileExists(final String filePath) {
        try {
            final File file = new File(filePath);
            if (file.exists()) {
                return true;
            }
        } catch (NullPointerException npe) {
            // return false
        }
        return false;
    }
}
