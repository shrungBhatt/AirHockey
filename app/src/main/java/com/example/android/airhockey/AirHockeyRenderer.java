package com.example.android.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.android.airhockey.objects.Mallet;
import com.example.android.airhockey.objects.Table;
import com.example.android.airhockey.programs.ColorShaderProgram;
import com.example.android.airhockey.programs.TextureShaderProgram;
import com.example.android.airhockey.util.LoggerConfig;
import com.example.android.airhockey.util.MatrixHelper;
import com.example.android.airhockey.util.ShaderHelper;
import com.example.android.airhockey.util.TextResourceReader;
import com.example.android.airhockey.util.TextureHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "AirHockeyRenderer";

    private final Context mContext;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private Table mTable;
    private Mallet mMallet;

    private TextureShaderProgram mTextureShaderProgram;
    private ColorShaderProgram mColorShaderProgram;

    private int texture;

    public AirHockeyRenderer(Context context){
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0.0f,0.0f,0.0f,0.0f);

        mTable = new Table();
        mMallet = new Mallet();

        mTextureShaderProgram = new TextureShaderProgram(mContext);
        mColorShaderProgram = new ColorShaderProgram(mContext);

        texture = TextureHelper.loadTexture(mContext,R.drawable.air_hockey_surface);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(projectionMatrix, 45,
                (float) width / (float) height, 1f, 10f);

        setIdentityM(modelMatrix, 0);

        translateM(modelMatrix, 0, 0f, 0f, -2.8f);
        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

        final float[] temp = new float[16];
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);


    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //Clear the rendering surface
        glClear(GL_COLOR_BUFFER_BIT);

        //Draw the table
        mTextureShaderProgram.useProgram();
        mTextureShaderProgram.setUniforms(projectionMatrix, texture);
        mTable.bindData(mTextureShaderProgram);
        mTable.draw();

        //Draw the mallets
        mColorShaderProgram.useProgram();
        mColorShaderProgram.setUniforms(projectionMatrix);
        mMallet.bindData(mColorShaderProgram);
        mMallet.draw();

    }

}
