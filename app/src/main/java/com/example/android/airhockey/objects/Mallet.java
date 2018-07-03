package com.example.android.airhockey.objects;

import android.opengl.GLES20;

import com.example.android.airhockey.Constants;
import com.example.android.airhockey.programs.ColorShaderProgram;
import com.example.android.airhockey.util.VertexArray;

public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) *
            Constants.BYTES_PER_FLOAT;

    private static final float[] mVertexData = {
            //Order of coordinates: X, Y, R, G, B.
            0f, -0.4f, 0f, 0f, 1f,
            0f, 0.4f, 1f, 0f, 0f};

    private final VertexArray mVertexArray;

    public Mallet() {
        mVertexArray = new VertexArray(mVertexData);

    }

    public void bindData(ColorShaderProgram colorProgram) {
        mVertexArray.setVertexAttribPointer(0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
        mVertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                colorProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);
    }


    public void draw(){
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,2);
    }
}
