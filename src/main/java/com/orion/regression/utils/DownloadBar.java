package com.orion.regression.utils;

public class DownloadBar {
    public static final String format = "Downloading JSON.. %d bytes downloaded\r";    

    public static void update(int done) {
        System.out.printf(format, done);
    }    
}
