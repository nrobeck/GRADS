package schema;

import schema.Area;
import java.util.ArrayList;

public class BreadthSchema extends GpaSchema{
	private ArrayList<Area> areas;
	BreadthSchema(){
		super(null,-1);
		this.areas = new ArrayList<Area>();
	}
	BreadthSchema(String name){
		super(name,-1);
		this.areas = new ArrayList<Area>();
	}
	BreadthSchema(String name, float min){
		super(name,min);
		this.areas = new ArrayList<Area>();
	}
	BreadthSchema(String name, float min, ArrayList<Area> areas){
		super(name,min);
		this.areas = areas;
	}
}
