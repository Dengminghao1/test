/*
 * Decompiled with CFR 0.151.
 */
package com.multi.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConfigClass {
    public static long RECORD_NUM = 0L;
    public static int SocketTimeout = 10000;
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static Date date = new Date();
    public static Map<String, Thread> DATA_THREAD = new HashMap<String, Thread>();
    public static Set<String> wif_sets = new HashSet<String>();
    public static String STORE_PATH = "D:\\eeg_Server\\eegDataSave\\" + dateFormat.format(date) + "\\";
    public static String SERVER_PORT = "8234";

    public static Set<String> getWif_sets() {
        return wif_sets;
    }

    public static void setWif_sets(Set<String> wif_sets) {
        ConfigClass.wif_sets = wif_sets;
    }
}

