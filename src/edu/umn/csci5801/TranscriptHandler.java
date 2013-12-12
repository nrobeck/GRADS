package edu.umn.csci5801;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.MilestoneSet;
import edu.umn.csci5801.model.Professor;
import edu.umn.csci5801.model.StudentRecord;

/**
 * The module of GRADS used to get and store transcripts.
 *
 * @author Nathan
 *
 */
public class TranscriptHandler {
    // private data manager and validator
    DataManager dbManager = new DataManager();
    DataValidator dataValidator = new DataValidator();

    /**
     * Constructor for the Transcript handler
     *
     * @param dbMan
     */
    public TranscriptHandler(DataManager dbMan) {
        dbManager = dbMan;
    }

    /**
     * Update the transcript of the student whose id is entered with the record
     * that is entered.
     *
     * @param studentID
     *            the id of the student whose record needs updating.
     * @param record
     *            the new record to update the database with
     */
    public void updateTranscript(String studentID, StudentRecord record) {
        // check the input record for validity
        if (validateTranscript(record)) {
            // store the new record if transcript is valid
            dbManager.storeTranscript(studentID, record);
        }
    }

    /**
     * Adds the input record note to the student record of the input student.
     *
     * @param studentID
     *            the id of the student whose record will have the note
     * @param note
     *            the note to add to the student record
     */
    public void addNote(String studentID, String note) {
        // retrieve the student transcript
        StudentRecord transcript = dbManager.getStudentData(studentID);

        if (note != null) {
            // add the note
            List<String> notes = transcript.getNotes();

            notes.add(note);

            transcript.setNotes(notes);

        }

        // store the modified transcript
        dbManager.storeTranscript(studentID, transcript);
    }


    /**
     * Checks the validity of all the values of the input record.
     *
     * @param record
     *            student record to validate
     * @return true if valid, false if invalid
     */
    private boolean validateTranscript(StudentRecord transcript) {
        // send the transcript to the validator
        dataValidator.setTranscript(transcript);
        // create boolean to return
        boolean valid = false;

        // check each field result from data validator
        // check if student is valid
        if (dataValidator.studentIsValid()) {
            // check if department is valid
            if (dataValidator.departmentIsValid()) {
                // check if degree is valid
                if (dataValidator.degreeIsValid()) {
                    // check if term is valid
                    if (dataValidator.termIsValid()) {
                        // check if professor is valid
                        if (dataValidator.professorsAreValid()) {
                            // check if course is valid
                            if (dataValidator.coursesAreValid()) {
                                // if all nested ifs are true, record is
                                // valid->set valid to true
                                valid = true;
                            }
                        }
                    }
                }
            }
        }

        return valid;
    }

    /**
     * Retrieve the student record for the student whose id is given.
     *
     * @param userId
     *            id of the student whose record needs retrieval
     * @return the record of the student
     */
    public StudentRecord getTranscript(String userId) {
        // create the student record
        StudentRecord record = dbManager.getStudentData(userId);
        if (record == null) {
            return null;
        }
        // make sure that all values are not null
        if (record.getAdvisors() == null) {
            record.setAdvisors(new ArrayList<Professor>());
        }

        // TODO: make sure all other things are not null

        // make sure the courses Taken list is not null
        if (record.getCoursesTaken() == null) {
            record.setCoursesTaken(new ArrayList<CourseTaken>());
        }

        // make sure the milestone sets list is not null
        if (record.getMilestonesSet() == null) {
            record.setMilestonesSet(new ArrayList<MilestoneSet>());
        }

        // return the record
        return record;
    }

}
