package com.github.eighty88.mcskin.imager.renderer.shapes;

import java.awt.Graphics;

public interface Groupable {
	
	void setParent(ObjectGroup objectGroup);

	void move(double deltaX, double deltaY, double deltaZ);
	
	void setLocation(double newX, double newY, double newZ);
	
	void resetLocation();
	
	void rotate(boolean CW, double xRotation, double yRotation, double zRotation, Vector3d lightVector);
	
	void setRotation(double xRotation, double yRotation, double zRotation);
	
	void resetRotation();
	
	double getXCoordinate();
	
	double getYCoordinate();
	
	double getZCoordinate();
	
	void render(Graphics g);
	
	void delete();
	
}
