package com.github.eighty88.mcskin.imager.renderer.point;


public class Point3d {
	
	public double x, y, z;

	public Point3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getDistanceFrom(Point3d other) {
		double deltaX = x-other.x;
		double deltaY = y-other.y;
		double deltaZ = z-other.z;
		
		double deltaXY = Math.sqrt(deltaX*deltaX + deltaY*deltaY);

		return Math.sqrt(deltaXY*deltaXY + deltaZ*deltaZ);
	}
	
	public double getDistanceFrom(double[] other) {
		double deltaX = x-other[0];
		double deltaY = y-other[1];
		double deltaZ = z-other[2];
		
		double deltaXY = Math.sqrt(deltaX*deltaX + deltaY*deltaY);

		return Math.sqrt(deltaXY*deltaXY + deltaZ*deltaZ);
	}
	
	@Override
	public boolean equals(Object o) { 
		if(o instanceof double[] && ((double[])o).length==3) {
			double[] xyz = (double[])o;
			return (xyz[0]==this.x) && (xyz[1]==this.y) && (xyz[2]==this.z); 
		} else if(!(o instanceof Point3d))
			return false;
		return equals((Point3d)o);
	}
	
	public boolean equals(Point3d other) {
		boolean matchX = this.x == other.x;
		boolean matchY = this.y == other.y;
		boolean matchZ = this.z == other.z;
		
		return matchX && matchY && matchZ;
	}
	
	public double[] coords() {
		return new double[] {x, y, z};
	}
	
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
	
	public static Point3d average(Point3d ptA, Point3d ptB) {
		if(ptA.equals(ptB))
			return ptA;
		
		double[] xyz = averageCoords(ptA.coords(), ptB.coords());
		return new Point3d(xyz[0], xyz[1], xyz[2]);
	}
	
	public static double[] averageCoords(double[] ptA, double[] ptB) {
		double xAvg = (ptA[0] + ptB[0])/2.0;
		double yAvg = (ptA[1] + ptB[1])/2.0;
		double zAvg = (ptA[2] + ptB[2])/2.0;
		
		return new double[] {xAvg, yAvg, zAvg};
	}
	
}
