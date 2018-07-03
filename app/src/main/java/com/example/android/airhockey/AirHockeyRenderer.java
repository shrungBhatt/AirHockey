package com.example.android.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.android.airhockey.objects.Mallet;
import com.example.android.airhockey.objects.Puck;
import com.example.android.airhockey.objects.Table;
import com.example.android.airhockey.programs.ColorShaderProgram;
import com.example.android.airhockey.programs.TextureShaderProgram;
import com.example.android.airhockey.util.MatrixHelper;
import com.example.android.airhockey.util.TextureHelper;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "AirHockeyRenderer";

    private final Context mContext;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];


    private Table mTable;
    private Mallet mMallet;
    private Puck mPuck;


    private TextureShaderProgram mTextureShaderProgram;
    private ColorShaderProgram mColorShaderProgram;

    private int texture;

    public AirHockeyRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        mTable = new Table();
        mMallet = new Mallet(0.08f, 0.15f, 32);
        mPuck = new Puck(0.06f, 0.02f, 32);

        mTextureShaderProgram = new TextureShaderProgram(mContext);
        mColorShaderProgram = new ColorShaderProgram(mContext);

        texture = TextureHelper.loadTexture(mContext, R.drawable.air_hockey_surface);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(projectionMatrix, 45,
                (float) width / (float) height, 1f, 10f);

        setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f,
                0f, 1f, 0f);



    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //Clear the rendering surface
        glClear(GL_COLOR_BUFFER_BIT);

        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        positionTableInScene();
        mTextureShaderProgram.useProgram();
        mTextureShaderProgram.setUniforms(modelViewProjectionMatrix, texture);
        mTable.bindData(mTextureShaderProgram);
        mTable.draw();

        //Draw the mallets
        positionObjectInScene(0f, mMallet.mHeight / 2f, -0.6f);
        mColorShaderProgram.useProgram();
        mColorShaderProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f);
        mMallet.bindData(mColorShaderProgram);
        mMallet.draw();

        positionObjectInScene(0f, mMallet.mHeight / 2f, 0.2f);
        mColorShaderProgram.setUniforms(modelViewProjectionMatrix, 0.0f, 0.0f, 1f);
        mMallet.draw();

        //Draw the puck
        positionObjectInScene(0f, mPuck.mHeight / 2f, -0.2f);
        mColorShaderProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
        mPuck.bindData(mColorShaderProgram);
        mPuck.draw();


    }

    private void positionObjectInScene(float x, float y, float z) {
        setIdentityM(modelMatrix,0);
        translateM(modelMatrix,0,x,y,z);
        multiplyMM(modelViewProjectionMatrix,0,viewProjectionMatrix,0,modelMatrix,0);
    }

    private void positionTableInScene() {
        //The table is defined in terms of X & Y coordinates, so we rotate it
        //90 degrees to lie flat on XZ plane.
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, 0f, 0f, -0.2f);
        rotateM(modelMatrix, 0,-90f,1f,0f,0f);
        multiplyMM(modelViewProjectionMatrix,0,viewProjectionMatrix,0,modelMatrix,0);
    }

}
