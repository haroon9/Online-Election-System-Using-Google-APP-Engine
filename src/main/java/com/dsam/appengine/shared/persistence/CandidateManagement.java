package com.dsam.appengine.shared.persistence;

import java.util.List;

import com.dsam.appengine.shared.model.Candidate;

public interface CandidateManagement {
	
	void create(Candidate candidate) throws Exception;
	List<Candidate> getAllCandidates();
	Candidate getCandidateByID(String id);
	void updateCandidate(Candidate candidate, String key);
}
