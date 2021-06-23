package com.github.eighty88.mcskin.imager.renderer;

import com.github.eighty88.mcskin.imager.renderer.point.Point3d;

public class PointLight {

	private final Point3d source;
	private final double radius;
	private final double power;
	
	public PointLight(Point3d src, double rad, double pow) {
		source = src;
		radius = rad;
		power = pow;
	}
	
	public double getIntensity(double[] pointCoords) {
		double distance = source.getDistanceFrom(pointCoords);
		
		double intensity = radius*Math.pow(distance/radius, power);
		
		return Math.min(1, Math.max(LightingControl.AMBIENT_LIGHTING_INDEX, intensity));
	}
	
	public Point3d origin() {
		return source;
	}
}
