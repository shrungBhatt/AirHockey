package com.example.android.airhockey.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.util.logging.Logger;

import static android.opengl.GLES20.*;

public class TextureHelper {

    private static final String TAG = "TextureHelper";

    public static int loadTexture(Context context,int resourceId){
        final int[] textureObjectIds = new int[1];
        glGenTextures(1,textureObjectIds,0);


        if(textureObjectIds[0] == 0){
            LoggerConfig.log(TAG,"Could not generate a new OpenGL texture object");
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resourceId,
                options);

        if(bitmap == null){
            LoggerConfig.log(TAG,"Could not load the bitmap");

            glDeleteTextures(1,textureObjectIds,0);
            return 0;
        }

        glBindTexture(GL_TEXTURE_2D,textureObjectIds[0]);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        glBindTexture(GL_TEXTURE_2D,0);

        return textureObjectIds[0];
    }
}
