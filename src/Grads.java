
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
//TODO need to make own exceptions for this class (and all other classes)
//TODO create the NOTICE file listed above in the copyright info
//TODO comment code in line (assignment appendix part 7)
//TODO javadocs comments

/**
 * 
 * @author Group22
 * about GRADS
 */
import java.util.List;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.StudentRecord;
 
public class GRADS implements GRADSIntf{

    /**
     * The constructor for the GRADS class.
     * @param studentsFileName filename of the students database
     * @param coursesFileName filename of the courses database
     * @param usersFileName filename of the users database
     */
    public GRADS(String studentsFileName, String coursesFileName, String 
usersFileName) {
        this.coursesFile = coursesFileName;
        this.studentsFile = studentsFileName;
        this.usersFile = usersFileName;
    }
    
    //private variables for the GRADS class
    private User currentUser;
    private String studentsFile;
    private String coursesFile;
    private String usersFile;
    private SummaryBuilder summaryBuilder = new SummaryBuilder();
    private DataManager dbManager = new DataManager();
    private TranscriptHandler transcriptHandler = new TranscriptHandler(dbManager);
    private boolean GPC;
    
    /**
     * Returns a list of the student IDs for the GPCs department.
     * @see edu.umn.csci5801.GRADSIntf#getStudentIDs()
     */
    @Override
    public List<String> getStudentIDs(){
        //check if the user is a GPC
        if(GPC) {
            //get the student id list for the GPCs department
            return dbManager.getStudentIDList(currentUser.getDepartment());
        }
        else {
            //TODO this needs to be changed to throw an error/exception
            return null;
        }
    }

    /**
     * Defines the current user of the GRADS system.
     * @see edu.umn.csci5801.GRADSIntf#setUser(java.lang.String)
     */
    @Override
    public void setUser(String userId) throws Exception {
        //check that the given userId is in the system
        if(dbManager.getUserByID(userId) != null) {
            //set the current user
            currentUser = dbManager.getUserByID(userId);
            //set the boolean result of whether or not the currentUser is a GPC 
            GPC = (currentUser.getRole() == "GPC");
        }
        //if the userId is not in the system, go here
        else {
            //throw exception saying userId is not in system
            throw new Exception("error: invalid user ID");
        }
    }

    /**
     * Returns who is defined as the current user of the GRADS system 
     * @see edu.umn.csci5801.GRADSIntf#getUser()
     */
    @Override
    public String getUser(){
        //check if currentUser is set
        if(currentUser != null) {
            //return the X500 of the current user
            return currentUser.getID();
        }
        //if currentUser is not set, go here
        else {
            //TODO throw an error
            return null;
        }
    }

    /**
     * Returns the transcript of the student whose X500 is passed to it. 
     * @see edu.umn.csci5801.GRADSIntf#getTranscript(java.lang.String)
     */
    @Override
    public StudentRecord getTranscript(String userId) throws Exception {
        //check to see if the current user is a GPC
        if(GPC) {
            //check that the X500 is valid
            if(dbManager.getUserByID(userId) != null) {
                //return student transcript
                return transcriptHandler.getTranscript(userId);
            }
            else {
                //throw invalid X500 exception
                throw new Exception("error: userId is invalid");
            }
        }
        else {
            //throw non GPC user exception
            throw new Exception("error: user is not a GPC");
        }
    }

    /*
     * (non-Javadoc)
     * @see edu.umn.csci5801.GRADSIntf#updateTranscript(java.lang.String, edu.umn.csci5801.model.StudentRecord)
     */
    @Override
    public void updateTranscript(String userId, StudentRecord transcript)
            throws Exception {
        if(GPC) {
            if(dbManager.getStudentRecords(userId) != null) {
                transcriptHandler.updateTranscript(userId, transcript);
            }
            else {
                throw new Exception("error: user is not valid");
            }
        }
        else {
            throw new Exception("error: only GPCs may modify transcripts");
        }
        
    }

    /*
     * (non-Javadoc)
     * @see edu.umn.csci5801.GRADSIntf#addNote(java.lang.String, java.lang.String)
     */
    @Override
    public void addNote(String userId, String note) throws Exception {
        if(GPC) {
            if(dbManager.getUserByID(userId) != null) {
                transcriptHandler.addNote(userId, note);
            }
            else {
                throw new Exception("error: invalid userId");
            }
        }
        else {
            throw new Exception("error: must be a GPC to add a note");
        }
        
    }

    /*
     * (non-Javadoc)
     * @see edu.umn.csci5801.GRADSIntf#generateProgressSummary(java.lang.String)
     */
    @Override
    public ProgressSummary generateProgressSummary(String userId)
            throws Exception {
        if(GPC) {
            if(dbManager.getUserByID(userId) != null) {
                return summaryBuilder.getStudentSummary(userId);
            }
            else {
                throw new Exception("error: invalid userID");
            }
        }
        else {
            if(currentUser.getID() == userId) {
                return summaryBuilder.getStudentSummary(userId);
            }
            else {
                throw new Exception("error: only GPCs or the student to whom the record belongs can view the student summary");
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see edu.umn.csci5801.GRADSIntf#simulateCourses(java.lang.String, java.util.List)
     */
    @Override
    public ProgressSummary simulateCourses(String userId,
            List<CourseTaken> courses) throws Exception {
        if(GPC) {
            if(dbManager.getUserByID(userId) != null) {
                return summaryBuilder.createStudentSummary(userId, courses);
            }
            else {
                throw new Exception("error: invalid userId");
            }
        }
        else {
            if(currentUser.getID() == userId) {
                if(dbManager.getUserByID(userId) != null) {
                    return summaryBuilder.createStudentSummary(userId, courses);
                }
                else {
                    throw new Exception("error: invalid userId");
                }
            }
            else {
                throw new Exception("error: only a GPC or the student to whom this ID belongs may view the progress summary");
            }
        }
    }
}
    