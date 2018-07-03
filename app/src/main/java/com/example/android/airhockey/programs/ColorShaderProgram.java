package com.example.android.airhockey.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.example.android.airhockey.R;

import static android.opengl.GLES20.*;

public class ColorShaderProgram extends ShaderProgram {
    //Uniform location
    private final int uMatrixLocation;

    //Attribute location
    private final int aPositionLocation;
    private final int aColorLocation;

    public ColorShaderProgram(Context context){
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);

        //Retrieve uniform location for the shader program
        uMatrixLocation = glGetUniformLocation(program,U_MATRIX);

        //Retrieve attribute location for the shader program.
        aPositionLocation = glGetAttribLocation(program,A_POSITION);
        aColorLocation = glGetAttribLocation(program,A_COLOR);
    }

    public void setUniforms(float[] matrix){
        //Pass the matrix into the shader program

        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

    public int getColorAttributeLocation(){
        return aColorLocation;
    }
}
