package com.github.eighty88.mcskin.imager.renderer.shapes;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class ObjectGroup implements Groupable {

	private double xOrigin;
	private double yOrigin;
	private double zOrigin;
	private double xRotation;
	private double yRotation;
	private double zRotation;
	public String identifier;
	
	private List<Groupable> children;

	public ObjectGroup(Groupable... group) {
		this(0, 0, 0, group);
	}

	public ObjectGroup(double x, double y, double z, Groupable... group) {
		children = new ArrayList<>();
		add(group);
		xOrigin = x;
		yOrigin = y;
		zOrigin = z;
		xRotation = 0;
		yRotation = 0;
		zRotation = 0;
	}

	public ObjectGroup merge(ObjectGroup otherGroup) {
		add(otherGroup.getChildren());
		return this;
	}
	
	@Override
	public void render(Graphics g) {
		sortObjectGroups();
		recursiveRender(g);
	}

	private void recursiveRender(Graphics g) {		
		
		for(Groupable child : children) {
			if(child instanceof ObjectGroup) {
				((ObjectGroup)child).recursiveRender(g);				
			} else if (child instanceof Tetrahedron) {
				child.render(g);
			}
		}
	}

	public void sort() {
		children.sort(new GroupableSorter());
	}
	
	public void sortObjectGroups() {
		List<ObjectGroup> sortMe = new ArrayList<>();
		for(int i = 0; i < children.size(); i++) {
			if(children.get(i) instanceof ObjectGroup) {
				((ObjectGroup)children.get(i)).sort();
				sortMe.add((ObjectGroup)children.remove(i--));
			}
		}
		
		sortMe.sort(new ObjectGroupSorter());
		children.addAll(sortMe);
	}

	public void add(Groupable... group) {
		for(Groupable g : group) {
			children.add(g);
			g.setParent(this);
		}
	}

	public void add(List<Groupable> group) {
		for(Groupable g : group) {
			children.add(g);
			g.setParent(this);
		}		
	}

	public List<Groupable> getChildren() {
		return children;
	}


	@Override
	public void setParent(ObjectGroup objectGroup) { }

	@Override
	public void move(double deltaX, double deltaY, double deltaZ) {
		xOrigin+= deltaX;
		yOrigin+= deltaY;
		zOrigin+= deltaZ;
		
		for(Groupable g : children) {
			g.move(deltaX, deltaY, deltaZ);
		}
	}
	
	
	@Override
	public void resetLocation() {
		move(-xOrigin, -yOrigin, -zOrigin);
	}

	@Override
	public void setLocation(double newX, double newY, double newZ) {
		resetLocation();
		
		move(newX, newY, newZ);		
	}

	@Override
	public void rotate(boolean CW, double xRotation, double yRotation, double zRotation, Vector3d lightVector) {
		if(!CW) {
			xRotation*=-1;
			yRotation*=-1;
			zRotation*=-1;
		}

		this.xRotation+=xRotation;
		this.yRotation+=yRotation;
		this.zRotation+=zRotation;

		for(Groupable g : children) {
			g.rotate(CW, xRotation, yRotation, zRotation, lightVector);
		}
	}

	@Override
	public void setRotation(double xRotation, double yRotation, double zRotation) {
		resetRotation();
		
		rotate(true, xRotation, yRotation, zRotation, null);		
	}

	@Override
	public void resetRotation() {
		rotate(true, -this.xRotation, -this.yRotation, -this.zRotation, null);		
	}

	public double getLocalXCoordinate() {
		return xOrigin;
	}

	public double getLocalZCoordinate() {
		return zOrigin;
	}

	public double getGlobalYCoordinate() {
		return xOrigin*Math.cos(Math.toRadians(zRotation));
	}

	@Override
	public double getXCoordinate() {
		return getLocalXCoordinate();
	}

	@Override
	public double getYCoordinate() {
		return getGlobalYCoordinate();
	}

	@Override
	public double getZCoordinate() {
		return getLocalZCoordinate();
	}

	public Tetrahedron mergeAll() {
		Tetrahedron output = new Tetrahedron();
		
		for(Groupable child : children) {
			if(child instanceof Tetrahedron) {
				output.merge((Tetrahedron)child);
			} else if (child instanceof ObjectGroup) {
				output.merge(((ObjectGroup)child).mergeAll());
			}
		}
		
		return output;
		
	}

	public String toString() {

		return "Identifier: \"" + identifier + "\"";
	}

	@Override
	public void delete() {
		while(children.size()>0) {
			children.get(0).delete();
			children.remove(0);
		}
		children = null;
	}
	
}
