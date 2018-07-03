package com.example.android.airhockey.util;

public class Geometry {

    public static class Point {
        public final float mX, mY, mZ;

        public Point(float x, float y, float z) {
            mX = x;
            mY = y;
            mZ = z;
        }

        public Point translateY(float distance) {
            return new Point(mX, mY + distance, mZ);
        }

    }

    public static class Circle {
        public final Point mCenter;
        public final float mRadius;

        public Circle(Point center, float radius) {
            mCenter = center;
            mRadius = radius;
        }

        public Circle scale(float scale) {
            return new Circle(mCenter, mRadius * scale);

        }
    }

    public static class Cylinder{
        public final Point mCenter;
        public final float mRadius;
        public final float mHeight;


        public Cylinder(Point center, float radius, float height){
            mCenter = center;
            mRadius = radius;
            mHeight = height;

        }
    }

}
