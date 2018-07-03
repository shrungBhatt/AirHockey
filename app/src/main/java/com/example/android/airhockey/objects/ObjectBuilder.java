package com.example.android.airhockey.objects;

import android.opengl.GLES20;

import com.example.android.airhockey.util.Geometry;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.airhockey.util.Geometry.*;

public class ObjectBuilder {
    private static final int FLOATS_PER_VERTEX = 3;
    private final float[] mVertexData;
    private int mOffset = 0;

    private final List<DrawCommand> mDrawCommandList = new ArrayList<>();

    private ObjectBuilder(int sizeInVertices) {
        mVertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }

    private static int sizeOfCircleInVertices(int numPoints) {
        return 1 + (numPoints + 1);
    }

    private static int sizeOfOpenCylinderInVertices(int numPoints) {
        return (numPoints + 1) * 2;
    }

    static GeneratedData createPuck(Cylinder puck, int numPoints) {
        int size = sizeOfCircleInVertices(numPoints) + sizeOfOpenCylinderInVertices(numPoints);

        ObjectBuilder builder = new ObjectBuilder(size);

        Circle puckTop = new Circle(puck.mCenter.translateY(puck.mHeight / 2f), puck.mRadius);

        builder.appendCircle(puckTop, numPoints);
        builder.appendOpenCylinder(puck, numPoints);

        return builder.build();
    }

    static GeneratedData createMallet(Point center, float radius, float height, int numPoints) {
        int size = sizeOfCircleInVertices(numPoints) * 2 + sizeOfOpenCylinderInVertices(numPoints) * 2;

        ObjectBuilder builder = new ObjectBuilder(size);

        //First generate the mallet base.
        float baseHeight = height * 0.25f;

        Circle baseCircle = new Circle(center.translateY(-baseHeight), radius);

        Cylinder baseCylinder = new Cylinder(baseCircle.mCenter.translateY(-baseHeight / 2f),
                radius, baseHeight);

        builder.appendCircle(baseCircle, numPoints);
        builder.appendOpenCylinder(baseCylinder, numPoints);

        //Generate the handle
        float handleHeight = height * 0.75f;
        float handleRadius = radius / 3f;

        Circle handleCircle = new Circle(center.translateY(height * 0.5f), handleRadius);
        Cylinder handleCylinder = new Cylinder(handleCircle.mCenter.translateY(-handleHeight / 2f),
                handleRadius, handleHeight);

        builder.appendCircle(handleCircle, numPoints);
        builder.appendOpenCylinder(handleCylinder, numPoints);

        return builder.build();

    }

    private void appendCircle(Circle circle, int numPoints) {
        final int startVertex = mOffset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);

        //Center point of fan
        mVertexData[mOffset++] = circle.mCenter.mX;
        mVertexData[mOffset++] = circle.mCenter.mY;
        mVertexData[mOffset++] = circle.mCenter.mZ;

        //Fan around center point. <= id used because we want to generate the point at the starting
        //angle twice to complete the fan
        for (int i = 0; i <= numPoints; i++) {
            float angleInRadians = ((float) i / (float) numPoints) * ((float) Math.PI * 2f);

            mVertexData[mOffset++] = circle.mCenter.mX + circle.mRadius * (float) Math.cos(angleInRadians);
            mVertexData[mOffset++] = circle.mCenter.mY;
            mVertexData[mOffset++] = circle.mCenter.mZ + circle.mRadius * (float) Math.sin(angleInRadians);
        }

        mDrawCommandList.add(new DrawCommand() {
            @Override
            public void draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, startVertex, numVertices);
            }
        });
    }

    private void appendOpenCylinder(Cylinder cylinder, int numPoints) {
        final int startVertex = mOffset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
        final float yStart = cylinder.mCenter.mY - (cylinder.mHeight / 2f);
        final float yEnd = cylinder.mCenter.mY + (cylinder.mHeight / 2f);

        for (int i = 0; i <= numPoints; i++) {
            float angleInRadians = ((float) i / (float) numPoints) * ((float) Math.PI * 2f);

            float xPosition = cylinder.mCenter.mX + cylinder.mRadius * (float) Math.cos(angleInRadians);

            float zPosition = cylinder.mCenter.mZ + cylinder.mRadius * (float) Math.sin(angleInRadians);

            mVertexData[mOffset++] = xPosition;
            mVertexData[mOffset++] = yStart;
            mVertexData[mOffset++] = zPosition;

            mVertexData[mOffset++] = xPosition;
            mVertexData[mOffset++] = yEnd;
            mVertexData[mOffset++] = zPosition;

            mDrawCommandList.add(new DrawCommand() {
                @Override
                public void draw() {
                    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, startVertex, numVertices);
                }
            });
        }

    }

    static interface DrawCommand {
        void draw();
    }

    static class GeneratedData {
        final float[] vertexData;
        final List<DrawCommand> drawList;

        GeneratedData(float[] vertexData, List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }

    }

    private GeneratedData build() {
        return new GeneratedData(mVertexData, mDrawCommandList);
    }
}
