package com.github.eighty88.mcskin.imager.renderer.shapes;

import java.util.Comparator;

public class GroupableSorter implements Comparator<Groupable> {

	@Override
	public int compare(Groupable o1, Groupable o2) {
		return Double.compare(o1.getYCoordinate(), o2.getYCoordinate());
	}

}
