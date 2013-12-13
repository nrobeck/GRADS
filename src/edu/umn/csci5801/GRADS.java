package edu.umn.csci5801;

// GRADS.java
/**
 *Licensed to the Apache Software Foundation (ASF) under one
 *or more contributor license agreements.  See the NOTICE file
 *distributed with this work for additional information
 *regarding copyright ownership.  The ASF licenses this file
 *to you under the Apache License, Version 2.0 (the
 *"License"); you may not use this file except in compliance
 *with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing,
 *software distributed under the License is distributed on an
 *"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *KIND, either express or implied.  See the License for the
 *specific language governing permissions and limitations
 *under the License.
 */
//TODO create the NOTICE file listed above in the copyright info

/**
 * The GRADS class is the top level of the GRADS system. It interacts
 * with the user interface, checks user permissions, and accesses
 * appropriate submodules for tasks.
 * @author Group22
 *
 */

import java.util.List;

import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.StudentRecord;
import exceptions.InvalidX500Exception;
import exceptions.UserNotAllowedException;
import exceptions.UserNotGPCException;

public class GRADS implements GRADSIntf{

    /**
     * The constructor for the GRADS class.
     * @param studentsFileName filename of the students database
     * @param coursesFileName filename of the courses database
     * @param usersFileName filename of the users database
     */
    public GRADS(String studentsFileName, String coursesFileName, String usersFileName) {
        this.coursesFile = coursesFileName;
        this.studentsFile = studentsFileName;
        this.usersFile = usersFileName;

        // re-initialize the the sub-modules
        dbManager = new DataManager(coursesFile, studentsFile, null, usersFile);
        transcriptHandler = new TranscriptHandler(dbManager);
        summaryBuilder = new SummaryBuilder(dbManager, transcriptHandler);
    }

    //private variables for the GRADS class
    private User currentUser;
    private String studentsFile;
    private String coursesFile;
    private String usersFile;
    private DataManager dbManager = new DataManager(coursesFile, studentsFile, null, usersFile);
    private TranscriptHandler transcriptHandler = new TranscriptHandler(dbManager);
    private SummaryBuilder summaryBuilder = new SummaryBuilder(dbManager, transcriptHandler);
    private boolean GPC;

    /**
     * Returns a list of the student IDs for the GPCs department.
     * @throws UserNotGPCException thrown if the current user is not a GPC
     * @see edu.umn.csci5801.GRADSIntf#getStudentIDs()
     * @return IDList of the users in the current GPC users department
     */
    @Override
    public List<String> getStudentIDs() throws Exception{
        //check if the user is a GPC
        if(GPC) {
            //get the student id list for the GPCs department
            return dbManager.getStudentIDList(currentUser.getDepartment());
        }
        //go here if user is not a GPC
        else {
            //throw user not GPC exception
            throw new UserNotGPCException(currentUser.getID());
        }
    }

    /**
     * Defines the current user of the GRADS system.
     * @param userId the X500 of the user of the system
     * @throws InvalidX500Exception thrown if userId is not valid in system
     * @see edu.umn.csci5801.GRADSIntf#setUser(java.lang.String)
     */
    @Override
    public void setUser(String userId) throws Exception {
        //check that the given userId is in the system
        if(dbManager.getUserByID(userId) != null) {
            //set the current user
            currentUser = dbManager.getUserByID(userId);
            //set the boolean result of whether or not the currentUser is a GPC
            GPC = (currentUser.getRole().equals("GRADUATE_PROGRAM_COORDINATOR"));
        }
        //if the userId is not in the system, go here
        else {
            //throw exception saying userId is not in system
            throw new InvalidX500Exception(userId);
        }
    }

    /**
     * Returns who is defined as the current user of the GRADS system
     * @see edu.umn.csci5801.GRADSIntf#getUser()
     * @return X500 of the current user
     */
    @Override
    public String getUser(){
        //return the X500 of the current user
        return currentUser.getID();
    }

    /**
     * Returns the transcript of the student whose X500 is passed to it.
     * @param userId the X500 of the student whose transcript is to be retrieved
     * @throws InvalidX500Exception thrown if userId is invalid
     * @throws UserNotGPCException thrown if current user is not a GPC
     * @see edu.umn.csci5801.GRADSIntf#getTranscript(java.lang.String)
     * @return the transcript of the user with the input X500
     */
    @Override
    public StudentRecord getTranscript(String userId) throws Exception {
        //check to see if the current user is a GPC
        if(GPC) {
            //check that the X500 is valid
            if(dbManager.getUserByID(userId) != null) {
                //return student transcript
            	if(dbManager.getUserByID(userId).getDepartment().equals(currentUser.getDepartment())){
            		return transcriptHandler.getTranscript(userId);
            	}
            	else {
            		throw new UserNotAllowedException(currentUser.getID());
            	}
            }
            else {
                //throw invalid X500 exception
                throw new InvalidX500Exception(userId);
            }
        }
        else {
            //throw non GPC user exception
            throw new UserNotGPCException(currentUser.getID());
        }
    }

    /**
     * Sends modifications to the students transcript for update in the database.
     * @param userId the X500 of the student the transcript belongs to
     * @param transcript the (modified) student record that will be written to the database
     * @throws InvalidX500Exception thrown if userId is invalid
     * @throws UserNotGPCException thrown if current user is not a GPC
     * @see edu.umn.csci5801.GRADSIntf#updateTranscript(java.lang.String, edu.umn.csci5801.model.StudentRecord)
     */
    @Override
    public void updateTranscript(String userId, StudentRecord transcript) throws Exception {
        //check if the current user is a GPC
        if(GPC) {
            //check if the X500 is valid
            if(dbManager.getStudentData(userId) != null) {
                //push the transcript changes
                transcriptHandler.updateTranscript(userId, transcript);
            }
            //run if invalid X500
            else {
                //throw invalid X500 exception
                throw new InvalidX500Exception(userId);
            }
        }
        //run if current user is not GPC
        else {
            //throw user not a GPC exception
            throw new UserNotGPCException(currentUser.getID());
        }

    }

    /**
     * Adds a note to the transcript of the user with the input X500.
     * @param userId the X500 of the student whose transcript is to have the note appended
     * @param note the note to be appended to the student transcript
     * @throws InvalidX500Exception thrown if userId is invalid
     * @throws UserNotGPCException thrown if current user is not a GPC
     * @see edu.umn.csci5801.GRADSIntf#addNote(java.lang.String, java.lang.String)
     */
    @Override
    public void addNote(String userId, String note) throws Exception {
        //check if the current user is a GPC
        if(GPC) {
            //check to make sure X500 is valid
            if(dbManager.getUserByID(userId) != null) {
                //add the note to the transcript
                transcriptHandler.addNote(userId, note);
            }
            //go here if X500 invalid
            else {
                //throw exception that X500 is invalid
                throw new InvalidX500Exception(userId);
            }
        }
        //go here if user is not GPC
        else {
            //throw user not a GPC exception
            throw new UserNotGPCException(currentUser.getID());
        }

    }

    /**
     * Returns a progress summary for the student with the input X500
     * @param userId the identifier of the student
     * @throws InvalidX500Exception thrown if userId is invalid
     * @throws UserNotAllowedException thrown if current user is not a GPC
     * @return progress summary of the user with the input X500
     * @see edu.umn.csci5801.GRADSIntf#generateProgressSummary(java.lang.String)
     */
    @Override
    public ProgressSummary generateProgressSummary(String userId) throws Exception {
        //check if user is GPC
        if(GPC) {
            //test to ensure valid X500
            if(dbManager.getUserByID(userId) != null) {
                //return student progress summary
                return summaryBuilder.getStudentSummary(userId);
            }
            //go here if X500 invalid
            else {
                //throw invalid X500 exception
                throw new InvalidX500Exception(userId);
            }
        }
        //go here if user is not GPC
        else {
            //check if user is attempting to generate their own summary
            if(currentUser.getID().equals(userId)) {
                //return progress summary if user is calling own summary
                return summaryBuilder.getStudentSummary(userId);
            }
            //go here if user is not calling own summary
            else {
                //throw permission not allowed exception
                throw new UserNotAllowedException(userId);
            }
        }
    }

    /**
     * Return a progress summary assuming the input student has taken the input classes as well as classes they have actually taken.
     * @param userId the identifier of the student
     * @param courses the hypothetical courses to be added to the summary
     * @throws InvalidX500Exception thrown if userId is invalid
     * @throws UserNotAllowedException thrown if current user is not a GPC
     * @return progress summary of the user with the input X500 assuming they have taken the courses listed in the courses parameter
     * @see edu.umn.csci5801.GRADSIntf#simulateCourses(java.lang.String, java.util.List)
     */
    @Override
    public ProgressSummary simulateCourses(String userId, List<CourseTaken> courses) throws Exception {
        //check if user is a GPC
        if(GPC) {
            //check if X500 is valid
            if(dbManager.getUserByID(userId) != null) {
                //return a student summary with additional input courses
                return summaryBuilder.createStudentSummary(userId, courses);
            }
            //go here if X500 is invalid
            else {
                //throw invalid X500 exception
                throw new InvalidX500Exception(userId);
            }
        }
        //go here if user is not a GPC
        else {
            //check if user is generating summary of themselves
            if(currentUser.getID().equals(userId)) {
                //check if x500 is valid
                if(dbManager.getUserByID(userId) != null) {
                    //return student summary with additional input courses
                    return summaryBuilder.createStudentSummary(userId, courses);
                }
                //go here if x500 invalid
                else {
                    //throw invalid X500 exception
                    throw new InvalidX500Exception(userId);
                }
            }
            //go here if user is not GPC or testing for self
            else {
                //throw operation not allowed exception
                throw new UserNotAllowedException(userId);
            }
        }
    }
}
//end GRADS.java
