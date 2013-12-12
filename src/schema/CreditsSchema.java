package schema;

public class CreditsSchema extends RequirementSchema{
    private int number;
    CreditsSchema(){
        super(null);
        this.number = -1;
    }
    CreditsSchema(String name){
        super(name);
        this.number = -1;
    }
    CreditsSchema(String name, int number){
        super(name);
        this.number = number;
    }
}
