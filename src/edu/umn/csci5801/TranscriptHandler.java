package edu.umn.csci5801;

import com.google.gson.JsonObject;

import edu.umn.csci5801.model.StudentRecord;


public class TranscriptHandler {
    DataManager dbManager;

    public TranscriptHandler(DataManager dbMan){
        dbManager = dbMan;
    }

    public StudentRecord getStudentRecord(String studentID){
        StudentRecord record = new StudentRecord();

        // TODO: stuff

        return null;
    }

    public void updateTranscript(String studentID, StudentRecord record){
        // TODO: stuff
    }

    public void addNote(String studentID, String note){
        // TODO: stuff
    }

    private boolean validateTranscript(StudentRecord record){
        // TODO: stuff

        return true;
    }

    private JsonObject recordToJson(StudentRecord record){
        JsonObject retVal = new JsonObject();

        // TODO: stuff

        return null;
    }

    public StudentRecord getTranscript(String userId) {
        // TODO Auto-generated method stub
        return null;
    }
}
