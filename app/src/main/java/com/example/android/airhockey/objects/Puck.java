package com.example.android.airhockey.objects;

import com.example.android.airhockey.programs.ColorShaderProgram;
import com.example.android.airhockey.util.VertexArray;

import java.util.List;

import static com.example.android.airhockey.objects.ObjectBuilder.*;
import static com.example.android.airhockey.util.Geometry.*;

public class Puck {
    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float mRadius, mHeight;

    private final VertexArray mVertexArray;
    private final List<DrawCommand> mDrawCommandList;

    public Puck(float radius, float height, int numPointsAroundPuck) {
        GeneratedData generatedData = createPuck(new Cylinder(new Point(0f, 0f, 0f),
                radius, height), numPointsAroundPuck);
        mRadius = radius;
        mHeight = height;

        mVertexArray = new VertexArray(generatedData.vertexData);
        mDrawCommandList = generatedData.drawList;
    }

    public void bindData(ColorShaderProgram colorShaderProgram) {
        mVertexArray.setVertexAttribPointer(0, colorShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }

    public void draw(){
        for(DrawCommand drawCommand : mDrawCommandList){
            drawCommand.draw();
        }
    }

}
