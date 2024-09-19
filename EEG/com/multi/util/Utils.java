/*
 * Decompiled with CFR 0.151.
 */
package com.multi.util;

import java.io.File;
import java.net.Socket;

public class Utils {
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String parseLine(String str) {
        try {
            String finalData = "";
            String data_upper = str.toUpperCase();
            String[] after_split = Utils.stringToStringArray(data_upper, 2);
            if (after_split.length != 36) {
                return "";
            }
            for (int i = 0; i < after_split.length; ++i) {
                String tempData = after_split[i];
                if ("AA".equals(tempData)) {
                    if ("AA".equals(tempData = after_split[++i])) {
                        tempData = after_split[++i];
                        while (170 == Integer.parseInt(tempData, 16)) {
                            tempData = after_split[++i];
                        }
                        while (Integer.parseInt(tempData, 16) > 170) {
                        }
                        int length = Integer.parseInt(tempData, 16);
                        ++i;
                        String[] value = new String[32];
                        int checkSum = 0;
                        for (int j = 0; j < length; ++j) {
                            value[j] = after_split[i];
                            checkSum += Integer.parseInt(after_split[i], 16);
                            ++i;
                        }
                        int check = Integer.parseInt(after_split[i], 16);
                        checkSum &= 0xFF;
                        if (check != (checkSum = ~checkSum & 0xFF)) continue;
                        for (int valueIndex = 0; valueIndex < value.length; valueIndex += length) {
                            int extendedCodeLevel = 0;
                            while (value[valueIndex] == "55") {
                                ++extendedCodeLevel;
                                ++valueIndex;
                            }
                            String see_code = value[valueIndex];
                            int code = Integer.parseInt(value[valueIndex], 16);
                            ++valueIndex;
                            if (code >= 128) {
                                length = Integer.parseInt(value[valueIndex], 16);
                                ++valueIndex;
                            } else {
                                length = 1;
                            }
                            int high = 0;
                            int middle = 0;
                            int low = 0;
                            int bow = 0;
                            for (int m = 0; m < length; ++m) {
                                int temp;
                                if ("83".equals(see_code)) {
                                    if (m % 3 == 0) {
                                        high = (Integer.parseInt(value[valueIndex + m], 16) & 0xFF) << 16;
                                    } else if (m % 3 == 1) {
                                        middle = (Integer.parseInt(value[valueIndex + m], 16) & 0xFF) << 8;
                                    } else {
                                        low = Integer.parseInt(value[valueIndex + m], 16) & 0xFF;
                                        bow = high | middle | low;
                                        finalData = finalData + Integer.toString(bow) + ",";
                                    }
                                }
                                if ("04".equals(see_code)) {
                                    temp = Integer.parseInt(value[valueIndex + m], 16) & 0xFF;
                                    finalData = finalData + Integer.toString(temp) + ",";
                                }
                                if (!"05".equals(see_code)) continue;
                                temp = Integer.parseInt(value[valueIndex + m], 16) & 0xFF;
                                finalData = finalData + Integer.toString(temp) + ",";
                            }
                        }
                        continue;
                    }
                    return "";
                }
                return "";
            }
            return finalData;
        }
        catch (Exception exception) {
            return "";
        }
    }

    public static String[] stringToStringArray(String src, int length) {
        if (null == src || src.equals("")) {
            return null;
        }
        if (length <= 0) {
            return null;
        }
        int n = (src.length() + length - 1) / length;
        String[] split = new String[n];
        for (int i = 0; i < n; ++i) {
            split[i] = i < n - 1 ? src.substring(i * length, (i + 1) * length) : src.substring(i * length);
        }
        return split;
    }

    public static String getMACAddress(Socket socket) throws Exception {
        String addressString = "";
        try {
            String addressToString = socket.getInetAddress().toString();
            String finalData = addressToString.split("\\.")[3];
            if (finalData.length() == 1) {
                finalData = "00" + finalData;
            }
            if (finalData.length() == 2) {
                finalData = "0" + finalData;
            }
            addressString = "E-" + finalData;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return addressString;
    }

    public static void checkFile(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
    }
}

