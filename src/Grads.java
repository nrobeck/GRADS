
// GRADS.java
/*
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

    public GRADS(String studentsFileName, String coursesFileName, String 
usersFileName) {
        // TODO Auto-generated constructor stub
        this.coursesFile = coursesFileName;
        this.studentsFile = studentsFileName;
        this.usersFile = usersFileName;
    }
    
    //private variables
    private User currentUser;
    private SummaryBuilder summaryBuilder;
    private DataManager dbManager = new DataManager();
    private TranscriptHandler transcriptHandler;
    private String studentsFile;
    private String coursesFile;
    private String usersFile;
    
    public List<String> getStudentIDs(){
        if(currentUser.getRole() == "GPC") {
            return dbManager.getStudentIDList(currentUser.getDepartment());
        }
        else {
            //this needs to be changed to throw an error/exception
            return null;
        }
    }

    @Override
    public void setUser(String userId) throws Exception {
        // TODO Auto-generated method stub
        if(dbManager.getUserByID(userId) != null) {
            this.currentUser = dbManager.getUserByID(userId);
        }
        else {
            throw new Exception("error: invalid user ID");
        }
    }

    @Override
    public String getUser(){
        //ISSUE: can't throw exception per GRADSIntf, how do we send error?
        if(currentUser != null) {
            return currentUser.getID();
        }
        else {
            return null;//TODO needs to throw error here
        }
    }

    @Override
    public StudentRecord getTranscript(String userId) throws Exception {
        if(currentUser.getRole() == "GPC") {
            if(dbManager.getUserByID(userId) != null) {
                return transcriptHandler.getTranscript(userId);
            }
            else {
                throw new Exception("error: userId is invalid");
            }
        }
        else {
            throw new Exception("error: user is not a GPC");
        }
    }

    @Override
    public void updateTranscript(String userId, StudentRecord transcript)
            throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addNote(String userId, String note) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ProgressSummary generateProgressSummary(String userId)
            throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProgressSummary simulateCourses(String userId,
            List<CourseTaken> courses) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
    