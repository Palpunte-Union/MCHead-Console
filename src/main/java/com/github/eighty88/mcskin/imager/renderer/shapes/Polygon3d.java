package com.github.eighty88.mcskin.imager.renderer.shapes;

import com.github.eighty88.mcskin.imager.renderer.PointLight;
import com.github.eighty88.mcskin.imager.renderer.point.Point3d;
import com.github.eighty88.mcskin.imager.renderer.point.PointConverter;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Polygon3d {

	private Color baseColor;
	private Color shadedColor;
	private List<Point3d> points;
	
	public Polygon3d(Color color, Point3d... pts) {
		this.baseColor = color;
		points = new ArrayList<>();

		Collections.addAll(points, pts);
	}
	
	public Polygon3d(Color color, List<Point3d> pts) {
		this.baseColor = color;
		points = pts;
	}
	
	public void render(Graphics g) {
		render(g, null);
	}

	public void render(Graphics g, PointLight light) {
		if(baseColor.getAlpha() == 0)
			return;
		
		Polygon poly = new Polygon();
		
		Point p;
		for (Point3d point : points) {
			p = PointConverter.convertPoint(point);
			poly.addPoint(p.x, p.y);
		}
		
		if(shadedColor == null || light == null) {
			g.setColor(baseColor);
			g.fillPolygon(poly);
		} else {
			Graphics2D g2d = (Graphics2D)g;
			Point close = PointConverter.convertPoint(getClosestPoint(light.origin()));
			Point far = PointConverter.convertPoint(getFurthestPoint(light.origin()));
			g2d.setPaint(new GradientPaint((int)far.getX(), (int)far.getY(), shadedColor, (int)close.getX(), (int)close.getY(), baseColor));
			g2d.fillPolygon(poly);
		}
	}
		
	public double getYAverage() {
		double sum = 0;
		
		for(Point3d p : points) {
			sum+= p.y;
		}
		return sum/points.size();
	}
	
	public double getXAverage() {
		double sum = 0;
		
		for(Point3d p : points) {
			sum+= p.x;
		}
		return sum/points.size();
	}
	
	public double getZAverage() {
		double sum = 0;
		
		for(Point3d p : points) {
			sum+= p.z;
		}
		return sum/points.size();
	}
	
	public double[] getAverage() {
		return new double[] {getXAverage(), getYAverage(), getZAverage()};
	}
	
	public void rotate(boolean CW, double xRotation, double yRotation, double zRotation) {
		for (Point3d p : points) {
			PointConverter.rotateXAxis(p, CW, xRotation);
			PointConverter.rotateYAxis(p, CW, yRotation);
			PointConverter.rotateZAxis(p, CW, zRotation);
		}
	}

	public Point3d getClosestPoint(Point3d other) {
		Point3d closest = points.get(0);
		double distance = closest.getDistanceFrom(other);
		for(Point3d pt : points) {
			if(pt.getDistanceFrom(other) < distance) {
				closest = pt;
				distance = pt.getDistanceFrom(other);
			}
		}
		return closest;
	}
	
	public Point3d getFurthestPoint(Point3d other) {
		Point3d furthest = points.get(0);
		double distance = furthest.getDistanceFrom(other);
		for(Point3d pt : points) {
			if(pt.getDistanceFrom(other) > distance) {
				furthest = pt;
				distance = pt.getDistanceFrom(other);
			}
		}
		return furthest;
	}

	public Color getColor() {
		return baseColor;
	}

	public void setColor(Color c) {
		baseColor = c;		
	}
	
	public void setShadedColor(Color c) {
		shadedColor = c;
	}

	public void move(double deltaX, double deltaY, double deltaZ) {
		
		for(Point3d pt : points) {
			pt.x+=deltaX;
			pt.y+=deltaY;
			pt.z+=deltaZ;
		}
		
	}
	
	public Tetrahedron subdivide(int numSubdivisions) {
		if(numSubdivisions<=0 || points.size() < 3)
			return new Tetrahedron(this);
		
		Tetrahedron output = new Tetrahedron();

		ArrayList<Polygon3d> newPolygons = new ArrayList<>();
		ArrayList<Point3d> newPolyPoints;
		for(Point3d ptA : points) {
			newPolyPoints = new ArrayList<>();
			for(Point3d ptB : points) {
				newPolyPoints.add(Point3d.average(ptA, ptB));
			}
			newPolygons.add(new Polygon3d(baseColor, newPolyPoints));
		}
		
		numSubdivisions--;
		
		for(Polygon3d poly : newPolygons) {
			output.merge(poly.subdivide(numSubdivisions));
		}
		
		return output;
			
	}
	
	public Polygon3d copy() {
		return new Polygon3d(baseColor, points);
	}
	
	public void delete() {
		while(points.size() > 0)
			points.remove(0);
		
		baseColor = null;
		points = null;
	}
}
