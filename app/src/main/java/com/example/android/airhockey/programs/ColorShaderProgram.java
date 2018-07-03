package com.example.android.airhockey.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.example.android.airhockey.R;

import static android.opengl.GLES20.*;

public class ColorShaderProgram extends ShaderProgram {
    //Uniform location
    private final int uMatrixLocation;
    private final int uColorLocation;

    //Attribute location
    private final int aPositionLocation;

    public ColorShaderProgram(Context context){
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);

        //Retrieve uniform location for the shader program
        uMatrixLocation = glGetUniformLocation(program,U_MATRIX);
        uColorLocation = glGetUniformLocation(program,U_COLOR);

        //Retrieve attribute location for the shader program.
        aPositionLocation = glGetAttribLocation(program,A_POSITION);
    }

    public void setUniforms(float[] matrix,float r, float g, float b){
        //Pass the matrix into the shader program

        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
        glUniform4f(uColorLocation,r,g,b,1f);
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

}
