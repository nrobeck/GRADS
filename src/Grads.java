
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

	public GRADS() {
		// TODO Auto-generated constructor stub
	}
	
	//private variables
	private User currentUser;
	private SummaryBuilder summaryBuilder;
	private DataManager dbManager;
	private TranscriptHandler transcriptHandler;
	
	public List<String> getStudentIDs(){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUser(String userId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StudentRecord getTranscript(String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
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
	