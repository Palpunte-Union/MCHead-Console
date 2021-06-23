package com.github.eighty88.mcskin.imager.renderer.shapes;

import java.util.Comparator;

public class ObjectGroupSorter implements Comparator<ObjectGroup> {

	@Override
	public int compare(ObjectGroup o1, ObjectGroup o2) {
		int output = 0;
		
		if(o1.getGlobalYCoordinate() > o2.getGlobalYCoordinate()) 
			output = 1;
		else if (o2.getGlobalYCoordinate() > o1.getGlobalYCoordinate())
			output = -1;
		
		return output;
	}

}
