package com.cjburkey.randomgl.util;

public final class Format {
    
    public static String format1(double number) {
        return format(number, 1);
    }
    
    public static String format2(double number) {
        return format(number, 2);
    }
    
    public static String format3(double number) {
        return format(number, 3);
    }
    
    public static String format4(double number) {
        return format(number, 4);
    }
    
    public static String format(double number, int places) {
        return String.format("%." + places + "f", number);
    }
    
}