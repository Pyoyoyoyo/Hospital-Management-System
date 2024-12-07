package com.main.hospitalmanagementsys.safemode;

public class SafeModeHandler {

    private static boolean isInSafeMode = false;

    public static void activateSafeMode() {
        isInSafeMode = true;
        System.out.println("Switching to Safe Mode: Limited functionality enabled.");

    }

    public static void deactivateSafeMode() {
        isInSafeMode = false;
        System.out.println("Exiting Safe Mode: Full functionality restored.");
    }

    public static boolean isInSafeMode() {
        return isInSafeMode;
    }
}
