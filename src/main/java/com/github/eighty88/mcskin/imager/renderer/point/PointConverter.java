package com.github.eighty88.mcskin.imager.renderer.point;


import java.awt.Point;

public class PointConverter {

	public static double SCALE = 1;
	public static int FOV_SCALE = 250;
	public static double CAM_DISTANCE = 15;
	public static int WIDTH;
	public static int HEIGHT;
	
	public static Point convertPoint(Point3d point3d) {
		double x3d = point3d.x;
		double y3d = point3d.z;
		double depth = point3d.y * SCALE;
		double[] newVal = scale(x3d, y3d, depth);
		int x2d = (int)(WIDTH / 2 + newVal[1]);
		int y2d = (int)(HEIGHT / 2 - newVal[0]);

		return new Point(x2d, y2d);
	}
	
	private static double[] scale(double x3d, double y3d, double depth) {
		double dist = Math.sqrt(x3d*x3d + y3d*y3d);
		double theta = Math.atan2(x3d, y3d);
		double camDepth = CAM_DISTANCE-depth;
		double localScale = Math.abs(FOV_SCALE/(camDepth+FOV_SCALE));
		dist *= localScale;

		return new double[]{dist*Math.cos(theta), dist*Math.sin(theta)};
	}

	public static void rotateXAxis(Point3d p, boolean CW, double degrees) {
		double radius = Math.sqrt(p.y*p.y + p.z*p.z);
		double theta = Math.atan2(p.y, p.z);
		theta += 2*Math.PI/360*degrees*(CW?-1:1);
		
		p.y = radius*Math.sin(theta);
		p.z = radius*Math.cos(theta);
	}
	
	public static void rotateYAxis(Point3d p, boolean CW, double degrees) {
		double radius = Math.sqrt(p.x*p.x + p.z*p.z);
		double theta = Math.atan2(p.x, p.z);
		theta += 2*Math.PI/360*degrees*(CW?1:-1);
		
		p.z = radius*Math.cos(theta);
		p.x = radius*Math.sin(theta);
	}

	public static void rotateZAxis(Point3d p, boolean CW, double degrees) {
		double radius = Math.sqrt(p.x*p.x + p.y*p.y);
		double theta = Math.atan2(p.x, p.y);
		theta += 2*Math.PI/360*degrees*(CW?-1:1);
		
		p.x = radius*Math.sin(theta);
		p.y = radius*Math.cos(theta);
	}
	
}
