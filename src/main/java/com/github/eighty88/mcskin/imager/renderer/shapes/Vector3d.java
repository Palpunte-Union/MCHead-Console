package com.github.eighty88.mcskin.imager.renderer.shapes;

import com.github.eighty88.mcskin.imager.renderer.point.Point3d;

public class Vector3d {
	
	private final Point3d start;
	private final Point3d end;
	
	public Vector3d(double x, double y, double z) {
		start = new Point3d(0, 0, 0);
		end = new Point3d(x, y, z);
	}
	
	public double getX() {
		return start.x-end.x;
	}
	
	public double getY() {
		return start.y-end.y;
	}
	
	public double getZ() {
		return start.y-end.y;
	}
	
	public String toString() {
		return "<" + getX() + "i, " + getY() + "j, " + getZ() + "k>";
	}

	public static Vector3d normalize(Vector3d vector) {
		double magnitude = Math.sqrt(vector.getX()*vector.getX() + vector.getY()*vector.getY() + vector.getZ()*vector.getZ());
		return new Vector3d(vector.getX()/magnitude, vector.getY()/magnitude, vector.getZ()/magnitude);
	}
	
}
