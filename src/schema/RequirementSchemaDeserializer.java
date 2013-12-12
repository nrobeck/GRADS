package schema;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import edu.umn.csci5801.Plan;
import schema.Area;

public class RequirementSchemaDeserializer implements JsonDeserializer<RequirementSchema> {
    @Override
    public RequirementSchema deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
              throws JsonParseException {
        JsonObject jobject = json.getAsJsonObject();
        if(jobject.get("type").getAsString() == "milestone"){
            return (RequirementSchema)new MilestoneSchema(jobject.get("name").getAsString());
        }
        else if(jobject.get("type").getAsString() == "credits"){
            return (RequirementSchema)new CreditsSchema(jobject.get("name").getAsString(),jobject.get("number").getAsInt());
        }
        else if(jobject.get("type").getAsString() == "gpa"){
            return (RequirementSchema)new GpaSchema(jobject.get("name").getAsString(),jobject.get("min").getAsFloat());
        }
        else if(jobject.get("type").getAsString() == "breadth"){
            //Needs areas
            Type areaCollectionType = new TypeToken<ArrayList<Area>>(){}.getType();
            ArrayList<Area> areas = context.deserialize(jobject.get("areas"), areaCollectionType.getClass());
            return (RequirementSchema)new BreadthSchema(jobject.get("name").getAsString(),jobject.get("min").getAsFloat(),areas);
        }
        else{
            return new MilestoneSchema();
        }
    }
}
