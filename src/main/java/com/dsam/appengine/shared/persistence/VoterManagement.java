package com.dsam.appengine.shared.persistence;

import java.util.List;

import com.dsam.appengine.shared.model.Voter;

public interface VoterManagement {
	
	void addVoter(String email);
	List<Voter> getAllVotersEmails();
	void addVote(String key, String token) throws Exception;
}
