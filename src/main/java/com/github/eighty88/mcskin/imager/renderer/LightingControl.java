package com.github.eighty88.mcskin.imager.renderer;

import com.github.eighty88.mcskin.imager.renderer.shapes.Vector3d;

public class LightingControl {

	public static Vector3d lightVector = Vector3d.normalize(new Vector3d(-0.5, -1, -1));
	public static final double AMBIENT_LIGHTING_INDEX = 0.15;
	
}
