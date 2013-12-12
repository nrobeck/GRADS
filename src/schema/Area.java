package schema;

import java.util.ArrayList;

import edu.umn.csci5801.model.CourseArea;

public class Area {
    private CourseArea area;
    private ArrayList<String> courses;

    Area(){
        this.area = null;
        this.courses = null;
    }

    Area(CourseArea area){
        this.area = area;
        this.courses = null;
    }
    Area(CourseArea area, ArrayList<String> courses){
        this.area = area;
        this.courses = courses;
    }
}
