package com.example.android.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.android.airhockey.util.LoggerConfig;
import com.example.android.airhockey.util.ShaderHelper;
import com.example.android.airhockey.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "AirHockeyRenderer";

    private final int POSITION_COMPONENT_COUNT = 2;
    private final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;
    private final Context mContext;
    private int mProgram;

    private static final String UNIFORM_COLOR = "u_Color";
    private int mUniformColorLocation;

    private static final String ATTRIBUTE_POSITION = "a_Position";
    private int mAttributePositionLocation;

    public AirHockeyRenderer(Context context) {
        mContext = context;

        float[] tableVerticesWithTriangles = {
                //traingle 1
                -0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,
                // Triangle 2
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,
                // Line 1
                -0.5f, 0f,
                0.5f, 0f,
                // Mallets
                0f, -0.25f,
                0f, 0.25f
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

        mUniformColorLocation = glGetUniformLocation(mProgram, UNIFORM_COLOR);

        mAttributePositionLocation = glGetAttribLocation(mProgram, ATTRIBUTE_POSITION);

        vertexData.position(0);
        glVertexAttribPointer(mAttributePositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, 0, vertexData);

        glEnableVertexAttribArray(mAttributePositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT);

        //draw the rectangle
        glUniform4f(mUniformColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 0, 6);

        //draw the line
        glUniform4f(mUniformColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);

        //draw the bottom mallet
        glUniform4f(mUniformColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);

        //draw the upper mallet
        glUniform4f(mUniformColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 2);
    }
}
