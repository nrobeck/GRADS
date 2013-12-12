package schema;

import edu.umn.csci5801.model.Department;

public class UserSchema{
    IdNameSchema user;
    String role;
    Department department;
    public UserSchema(){}
    public UserSchema(IdNameSchema idNameSchema, String role, Department department){
        this.user = idNameSchema;
        this.role = role;
        this.department = department;
    }
    public IdNameSchema getID(){
        return this.user;
    }

    public String getRole(){
        return this.role;
    }

    public Department getDepartment(){
        return this.department;
    }
}
