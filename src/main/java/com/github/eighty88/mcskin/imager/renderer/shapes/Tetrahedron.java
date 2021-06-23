package com.github.eighty88.mcskin.imager.renderer.shapes;

import com.github.eighty88.mcskin.imager.renderer.PointLight;

import java.awt.Color;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tetrahedron implements Groupable {
	
	public double xRotation;
	public double yRotation;
	public double zRotation;
	
	private List<Polygon3d> polygons;
	
	public Tetrahedron(Polygon3d... polygons) {
		this();
		Collections.addAll(this.polygons, polygons);
	}
	
	public Tetrahedron() {
		this.polygons = new ArrayList<>();
		
		xRotation = 0;
		yRotation = 0;
		zRotation = 0;
	}
	
	public Tetrahedron(List<Polygon3d> polygons) {
		this();
		for(Polygon3d poly : polygons) {
			this.polygons.add(poly.copy());
		}
	}

	@Override
	public void render(Graphics g) {
		sortPolygons();
		for(Polygon3d poly : polygons) {
			poly.render(g);
		}
	}
	
	public void render(Graphics g, PointLight light) {
		sortPolygons();
		for(Polygon3d poly : polygons) {
			poly.render(g, light);
		}
	}
	
	public void renderLighting(Graphics g, int numSubdivisions, ArrayList<PointLight> lightingSources) {
		renderLighting(g, numSubdivisions, lightingSources, false);
	}
	
	public void renderLighting(Graphics g, int numSubdivisions, ArrayList<PointLight> lightingSources, boolean gradientShading) {
		
		Tetrahedron lightingTetra = this.subdivide(numSubdivisions);
		Color oldPolyColor;
		for(Polygon3d poly : lightingTetra.polygons) {
			double closeIntensity = lightingSources.get(0).getIntensity(poly.getClosestPoint(lightingSources.get(0).origin()).coords());
			double farIntensity = lightingSources.get(0).getIntensity(poly.getFurthestPoint(lightingSources.get(0).origin()).coords());
			
			for(PointLight light : lightingSources) {
				if(closeIntensity > light.getIntensity(poly.getAverage())) {
					closeIntensity = light.getIntensity(poly.getAverage());
				}
			}
			
			oldPolyColor = poly.getColor();
			int closeRed  = Math.max(0, Math.min(255, (int)(oldPolyColor.getRed() - oldPolyColor.getRed()*closeIntensity)));
			int closeGreen= Math.max(0, Math.min(255, (int)(oldPolyColor.getGreen() - oldPolyColor.getGreen()*closeIntensity)));
			int closeBlue = Math.max(0, Math.min(255, (int)(oldPolyColor.getBlue() - oldPolyColor.getBlue()*closeIntensity)));
			poly.setColor(new Color(closeRed, closeGreen, closeBlue, oldPolyColor.getAlpha()));
			
			if(gradientShading) {
				int farRed  = Math.max(0, Math.min(255, (int)(oldPolyColor.getRed() - oldPolyColor.getRed()*farIntensity)));
				int farGreen= Math.max(0, Math.min(255, (int)(oldPolyColor.getGreen() - oldPolyColor.getGreen()*farIntensity)));
				int farBlue = Math.max(0, Math.min(255, (int)(oldPolyColor.getBlue() - oldPolyColor.getBlue()*farIntensity)));
				poly.setShadedColor(new Color(farRed, farGreen, farBlue, oldPolyColor.getAlpha()));
			}
		}
		
		lightingTetra.render(g, lightingSources.get(0));
	}
	
	public void rotate(boolean CW, double xRotation, double yRotation, double zRotation, Vector3d lightVector) {
		this.xRotation+=xRotation;
		this.yRotation+=yRotation;
		this.zRotation+=zRotation;
		
		for(Polygon3d poly : polygons) {
			poly.rotate(CW, xRotation, yRotation, zRotation);
		}
		sortPolygons();
	}

	@Override
	public void setRotation(double xRotation, double yRotation, double zRotation) {
		double deltaX = xRotation-this.xRotation;
		double deltaY = yRotation-this.yRotation;
		double deltaZ = zRotation-this.zRotation;
		
		rotate(true, deltaX, deltaY, deltaZ, null);
	}

	@Override
	public void resetRotation() {
		rotate(false, xRotation, yRotation, zRotation, null);
		xRotation = 0;
		yRotation = 0;
		zRotation = 0;
	}
	
	@Override
	public void move(double deltaX, double deltaY, double deltaZ) {
		for(Polygon3d poly : polygons) {
			poly.move(deltaX, deltaY, deltaZ);
		}
	}

	@Override
	public void setLocation(double newX, double newY, double newZ) {
		double deltaX = newX-getXCoordinate();
		double deltaY = newY-getYCoordinate();
		double deltaZ = newZ-getZCoordinate();
		
		move(deltaX, deltaY, deltaZ);
	}
	
	@Override
	public void resetLocation() {
		move(-getXCoordinate(), -getYCoordinate(), -getZCoordinate());
	}

	public double getXCoordinate() {
		if(polygons.isEmpty())
			return 0;
		
		int xSum = 0;
		for(Polygon3d poly : polygons) {
			xSum+=poly.getXAverage();
		}

		return ((double)xSum) / polygons.size();
	}

	public double getYCoordinate() {
		if(polygons.isEmpty())
			return 0;
		
		int ySum = 0;
		for(Polygon3d poly : polygons) {
			ySum+=poly.getYAverage();
		}

		return ((double)ySum) / polygons.size();
	}

	public double getZCoordinate() {
		if(polygons.isEmpty())
			return 0;
		
		int zSum = 0;
		for(Polygon3d poly : polygons) {
			zSum+=poly.getZAverage();
		}

		return ((double)zSum) / polygons.size();
	}
	
	private void sortPolygons() {
		polygons.sort(new PolygonSorter());
	}
	
	@Override
	public void setParent(ObjectGroup objectGroup) {}
	
	public List<Polygon3d> getPolygons() {
		return polygons;
	}
	
	public Tetrahedron subdivide(int numSubdivisions) {
		Tetrahedron output = new Tetrahedron();
		
		for(Polygon3d poly : polygons) {
			output.merge(poly.subdivide(numSubdivisions));
		}
		
		return output;
	}
	
	public void merge(Tetrahedron other) {
		for(Polygon3d poly : other.getPolygons()) {
			polygons.add(poly.copy());
		}

	}
	
	@Override
	public void delete() {
		while(polygons.size()>0) {
			polygons.get(0).delete();
			polygons.remove(0);
		}
		polygons = null;
	}
	
}
