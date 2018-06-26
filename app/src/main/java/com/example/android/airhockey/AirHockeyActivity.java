package com.example.android.airhockey;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AirHockeyActivity extends AppCompatActivity {

    private boolean mIsRendererSet = false;
    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new GLSurfaceView(this);


        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        final ConfigurationInfo configuration = activityManager.getDeviceConfigurationInfo();

        final boolean isES2Supported = configuration.reqGlEsVersion >= 0x20000;

        if(isES2Supported){

            mGLSurfaceView.setEGLContextClientVersion(2);

            mGLSurfaceView.setRenderer(new AirHockeyRenderer(this));
            mIsRendererSet = true;
        }

        setContentView(mGLSurfaceView);
    }


    @Override
    protected void onPause() {
        super.onPause();

        if(mIsRendererSet){
            mGLSurfaceView.onPause();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        if(mIsRendererSet){
            mGLSurfaceView.onResume();
        }
    }
}
