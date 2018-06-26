package com.example.android.airhockey.util;


import static android.opengl.GLES20.*;

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";


    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = glCreateShader(type);

        if (shaderObjectId == 0) {
            LoggerConfig.log(TAG, "Could not create new shader.");
            return 0;
        }

        glShaderSource(shaderObjectId, shaderCode);
        glCompileShader(shaderObjectId);

        final int compileStatus[] = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

        LoggerConfig.log(TAG, "Results of compiling source: " + "\n" + shaderCode + "\n" +
                glGetShaderInfoLog(shaderObjectId));

        if(compileStatus[0] == 0){
            glDeleteShader(shaderObjectId);

            LoggerConfig.log(TAG,"Compilation of shader failed");
            return 0;
        }

        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId,int fragmentShaderId){
        final int programObjectId = glCreateProgram();

        if(programObjectId == 0){
            LoggerConfig.log(TAG,"Could not create new program");
            return 0;
        }

        glAttachShader(programObjectId,vertexShaderId);
        glAttachShader(programObjectId,fragmentShaderId);

        glLinkProgram(programObjectId);

        final int linkStatus[] = new int[1];
        glGetProgramiv(programObjectId,GL_LINK_STATUS,linkStatus,0);

        if(linkStatus[0] == 0){

            LoggerConfig.log(TAG,"Linking of program failed" + "\n" +
                    glGetProgramInfoLog(programObjectId));

            return 0;
        }

        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId){
        glValidateProgram(programObjectId);

        final int validateResult[] = new int[1];
        glGetShaderiv(programObjectId,GL_VALIDATE_STATUS,validateResult,0);

       return validateResult[0] != 0;
    }

}
