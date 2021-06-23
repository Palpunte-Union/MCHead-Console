package com.github.eighty88.mcskin.imager.renderer.shapes;

import java.util.Comparator;

public class PolygonSorter implements Comparator<Polygon3d> {

	@Override
	public int compare(Polygon3d o1, Polygon3d o2) {
		if(o1.getYAverage()>o2.getYAverage())
			return 1;
		else if (o2.getYAverage()>o1.getYAverage())
			return -1;
		return 0;
	}

}
