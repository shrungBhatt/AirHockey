package com.example.android.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.android.airhockey.util.ShaderHelper;
import com.example.android.airhockey.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private final int POSITION_COMPONENT_COUNT = 2;
    private final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;
    private final Context mContext;

    public AirHockeyRenderer(Context context) {
        mContext = context;

        float[] tableVerticesWithTriangles = {
                //traingle 1
                0f, 0f,
                9f, 14f,
                0f, 14f,

                //traingle 2
                0f, 0f,
                9f, 0f,
                9f, 14f,

                //Line 1
                0f, 7f,
                9f, 7f,

                //Mallets
                4.5f, 2f,
                4.5f, 12f
        };

        vertexData = ByteBuffer
                .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(tableVerticesWithTriangles);

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        final String vertexShaderSource = TextResourceReader.readTextFileFromResource(mContext,
                R.raw.simple_vertex_shader);

        final String fragmentShaderSource = TextResourceReader.readTextFileFromResource(mContext,
                R.raw.simple_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileVertexShader(fragmentShaderSource);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT);

    }
}
