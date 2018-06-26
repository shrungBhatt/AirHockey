package com.example.android.airhockey.util;

import android.util.Log;

public class LoggerConfig {

    public static final boolean ON = true;

    public static void log(String tag,String message){

        if(ON){
            Log.e(tag,message);
        }


    }
}
