package edu.umn.csci5801;
// User.java
//
/** TODO: copyright */

import java.lang.String;

import edu.umn.csci5801.model.Department;

/**
 *
 * @author Kyle
 *
 * Storage class for user information.
 * This is used for user validation and specifying departments
 * Information is only allowed to be set on creation
 * This is to reduce mistakes in overriding data;
 *
 */
public class User {
    /** the x500 of the user */
    private String id;
    /** GPC or student only*/
    private String role;
    /** the department of the user */
    private Department department;

    /**
     * The constructor for the User class
     * @param id the X500 of the user
     * @param role whether the user is a gpc or a student
     * @param department what department the user is a member of
     */
    public User(String id, String role, Department department){
        //set the id to be the input id
        this.id = id;
        //set the role to be the input role
        this.role = role;
        //set the department to be the input department
        this.department = department;
    }

    /**
     * A standard getter to return the X500 of the user
     * @return a string representation of the X500
     */
    public String getID(){
        //return the X500
        return id;
    }

    /**
     * A standard getter to return the role of the user
     * @return a string representation of whether the user is a student or a gpc
     */
    public String getRole(){
        //return the user role as a string
        return role;
    }

    /**
     * A standard getter to return the role of the user
     * @return a string representation of the department the user belongs to
     */
    public Department getDepartment(){
        //return a string representation of the users department
        return department;
    }

    /**
     * Compares the input object to the current user for equality.
     * If the IDs are the same the object and user are considered equal.
     * ASSUME NO ID DUPLICATES
     * @param o the object that the user is to be compared to
     */
    @Override
    public boolean equals(Object o){
        //check if the object is actually the user
        if (this == o)
            return true;
        //check if the object is null...cannot be equal
        if (o == null)
            return false;
        //declare user object
        User user;
        //check if object is in fact a user object
        if(o.getClass() != this.getClass()){
            return false;
        }
        else{
            //cast the object as a user
            user = (User) o;
        }
        //check if the user id is the same as the object id
        if(this.getID().equals(user.getID())){
            return true;
        }
        else{
            return false;
        }
    }
}
