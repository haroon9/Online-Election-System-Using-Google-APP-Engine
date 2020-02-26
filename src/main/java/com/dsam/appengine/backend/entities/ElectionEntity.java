package com.dsam.appengine.backend.entities;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.dsam.appengine.shared.model.Candidate;
import com.dsam.appengine.shared.model.VotingPeriod;
import com.dsam.appengine.shared.persistence.ElectionManagement;
import com.google.appengine.api.datastore.*;

public class ElectionEntity implements ElectionManagement {

    public ElectionEntity() {

    }

    // create an entity of kind Electiondate which stores start and end date of election
    @Override
    public void create(Date startDate, Date endDate) throws Exception {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        // Delete old Elections before creating new one
        for (Key key : getKeys()) {
            datastoreService.delete(key);
        }
        Transaction transaction = datastoreService.beginTransaction();
        try {
            Entity electionDate = new Entity("ElectionDate", "Election");
            electionDate.setProperty("startDate", startDate);
            electionDate.setProperty("endDate", endDate);

            datastoreService.put(transaction, electionDate);
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    // get the start and end date of election
    @Override
    public List<VotingPeriod> getDates() {
        List<VotingPeriod> dates = new ArrayList<>();
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("ElectionDate");
        PreparedQuery preparedQuery = datastoreService.prepare(query);

        for (Entity result : preparedQuery.asIterable()) {
            VotingPeriod votingPeriod = new VotingPeriod();
            votingPeriod.setStart(((Date) result.getProperty("startDate")).toInstant().atZone(ZoneId.of("Europe/Berlin")).toLocalDateTime());
            votingPeriod.setEnd(((Date) result.getProperty("endDate")).toInstant().atZone(ZoneId.of("Europe/Berlin")).toLocalDateTime());
            dates.add(votingPeriod);
        }
        if (dates.size() == 0) {
            return new ArrayList<>();
        }
        return dates;
    }

    public List<Key> getKeys() {
        List<Key> keys = new ArrayList<>();
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("ElectionDate");
        PreparedQuery preparedQuery = datastoreService.prepare(query);
        for (Entity result : preparedQuery.asIterable()) {
            keys.add(result.getKey());
        }
        return keys;
    }

    // get the number of total voters
    @Override
    public int totalVoters() {
        return new VoterEntity().getEligibleVoters();
    }

    // get the number of total votes cast
    @Override
    public int totalVotesCast() {
        CandidateEntity candidateEntity = new CandidateEntity();
        List<Candidate> candidates = candidateEntity.getAllCandidates();
        int totalCount = 0;
        if (candidates.isEmpty() || candidates.size() == 0) {
            return 0;
        } else {
            for (Candidate candidate : candidates) {
                totalCount = totalCount + candidate.getVotes();
            }
        }
        return totalCount;
    }

    // returns the percentage of votes cast
    @Override
    public Double percentageOfVotesCast() {
        if (totalVoters() <= 0) {
            return 0.0;
        }
        double p = (double) (totalVotesCast() * 100) / totalVoters();
        return p;
    }

    // returns list of all candidates sort the list by votes he got
    @Override
    public List<Candidate> candidatesOrderByVotes() {
        List<Candidate> candidates = new CandidateEntity().getAllCandidates();
        Collections.sort(candidates, (c1, c2) -> {
            return c2.getVotes() - c1.getVotes();
        });

        return candidates;
    }

}
