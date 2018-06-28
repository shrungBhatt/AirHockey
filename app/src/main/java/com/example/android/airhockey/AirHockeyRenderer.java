package com.example.android.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.android.airhockey.util.LoggerConfig;
import com.example.android.airhockey.util.MatrixHelper;
import com.example.android.airhockey.util.ShaderHelper;
import com.example.android.airhockey.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "AirHockeyRenderer";

    private final int POSITION_COMPONENT_COUNT = 2;
    private final int COLOR_COMPONENT_COUNT = 3;
    private final int BYTES_PER_FLOAT = 4;
    private final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private final FloatBuffer vertexData;
    private final Context mContext;
    private int mProgram;

    private int mAttributePositionLocation;
    private int mAttributeColorLocation;
    private int mUniformMatrixLocation;

    private static final String ATTRIBUTE_COLOR = "a_Color";
    private static final String ATTRIBUTE_POSITION = "a_Position";
    private static final String UNIFORM_MATRIX = "u_Matrix";

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];


    public AirHockeyRenderer(Context context) {
        mContext = context;

        float[] tableVerticesWithTriangles = {
                // Order of coordinates: X, Y, R, G, B
                // Triangle Fan
                0f, 0f, 1f, 1f, 1f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                // Line 1
                -0.5f, 0f, 1f, 0f, 0f,
                0.5f, 0f, 1f, 0f, 0f,
                // Mallets
                0f, -0.4f, 0f, 0f, 1f,
                0f, 0.4f, 1f, 0f, 0f
        };

        vertexData = ByteBuffer
                .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(tableVerticesWithTriangles);

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        final String vertexShaderSource = TextResourceReader.readTextFileFromResource(mContext,
                R.raw.simple_vertex_shader);

        final String fragmentShaderSource = TextResourceReader.readTextFileFromResource(mContext,
                R.raw.simple_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        mProgram = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(mProgram);
        }

        glUseProgram(mProgram);


        mAttributePositionLocation = glGetAttribLocation(mProgram, ATTRIBUTE_POSITION);
        mAttributeColorLocation = glGetAttribLocation(mProgram, ATTRIBUTE_COLOR);
        mUniformMatrixLocation = glGetUniformLocation(mProgram, UNIFORM_MATRIX);


        vertexData.position(0);
        glVertexAttribPointer(mAttributePositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);

        glEnableVertexAttribArray(mAttributePositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(mAttributeColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);

        glEnableVertexAttribArray(mAttributeColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);

       /* final float aspectRatio = width > height ? (float) width / (float) height :
                (float) height / (float) width;

        if(width > height){
            orthoM(projectionMatrix,0,-aspectRatio,aspectRatio,-1f,1f,
                    -1f,1f);

        }else{
            orthoM(projectionMatrix,0,-1f,1f,-aspectRatio,aspectRatio,
                    -1f,1f);
        }*/

        MatrixHelper.perspectiveM(projectionMatrix, 45,
                (float) width / (float) height, 1f, 10f);

        setIdentityM(modelMatrix,0);

        translateM(modelMatrix, 0, 0f, 0f, -2.8f);
        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

        final float[] temp = new float[16];
        multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
        System.arraycopy(temp,0,projectionMatrix,0,temp.length);


    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT);

        glUniformMatrix4fv(mUniformMatrixLocation, 1, false, projectionMatrix, 0);
        //draw the rectangle
//        glUniform4f(mUniformColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        //draw the line
//        glUniform4f(mUniformColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);

        //draw the bottom mallet
//        glUniform4f(mUniformColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);

        //draw the upper mallet
//        glUniform4f(mUniformColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);
    }
}
