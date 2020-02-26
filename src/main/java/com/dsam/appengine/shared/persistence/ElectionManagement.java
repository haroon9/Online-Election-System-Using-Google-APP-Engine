package com.dsam.appengine.shared.persistence;

import java.util.Date;
import java.util.List;

import com.dsam.appengine.shared.model.Candidate;
import com.dsam.appengine.shared.model.VotingPeriod;
import com.google.appengine.api.datastore.EntityNotFoundException;

public interface ElectionManagement {
	
	void create (Date startDate, Date endDate) throws EntityNotFoundException, Exception;

	List<VotingPeriod> getDates();
	
	int totalVoters();
	
	int totalVotesCast();
	
	Double percentageOfVotesCast();
	
	List<Candidate> candidatesOrderByVotes();
}
