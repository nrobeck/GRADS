package schema;

import java.util.ArrayList;

public class BreadthSchema extends GpaSchema{
	private ArrayList<String[]> areas;
	BreadthSchema(){
		super(null,-1);
		this.areas = new ArrayList<String[]>();
	}
	BreadthSchema(String name){
		super(name,-1);
		this.areas = new ArrayList<String[]>();
	}
	BreadthSchema(String name, float min){
		super(name,min);
		this.areas = new ArrayList<String[]>();
	}
	BreadthSchema(String name, float min, ArrayList<String[]> areas){
		super(name,min);
		this.areas = areas;
	}
}
