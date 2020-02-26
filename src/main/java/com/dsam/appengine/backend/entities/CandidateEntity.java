package com.dsam.appengine.backend.entities;

import java.util.ArrayList;
import java.util.List;

import com.dsam.appengine.shared.model.Candidate;
import com.dsam.appengine.shared.persistence.CandidateManagement;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;

public class CandidateEntity implements CandidateManagement {

    // creates a new candidate with properties firstname, lastname and faculty
    @Override
    public void create(Candidate candidate) throws Exception {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction transaction = datastoreService.beginTransaction();
        Filter firstNameFilter = new FilterPredicate("firstName", FilterOperator.EQUAL, candidate.getFirstName());
        Filter lastNameFilter = new FilterPredicate("surname", FilterOperator.EQUAL, candidate.getSurName());
        Filter facultyFilter = new FilterPredicate("faculty", FilterOperator.EQUAL, candidate.getFaculty());

        Filter validFilter = CompositeFilterOperator.and(firstNameFilter, lastNameFilter, facultyFilter);
        Query query = new Query("Candidate").setFilter(validFilter);
        Entity entity = datastoreService.prepare(query).asSingleEntity();

        try {
            if (entity != null) {
                throw new Exception("Candidate already exists. Please check your data.");
            } else if (candidate.getSurName() == null || candidate.getSurName().isEmpty() || candidate.getFirstName() == null || candidate.getFirstName().isEmpty() || candidate.getFaculty() == null || candidate.getFaculty().isEmpty()) {
                throw new Exception("Attributes of Candidate can not be empty/null.");
            } else {
                Key key = KeyFactory.createKey("Election", "Candidate");
                Entity candidateEntity = new Entity("Candidate", key);
                candidateEntity.setProperty("firstName", candidate.getFirstName());
                candidateEntity.setProperty("surname", candidate.getSurName());
                candidateEntity.setProperty("faculty", candidate.getFaculty());
                candidateEntity.setProperty("Votes", 0L);

                datastoreService.put(transaction, candidateEntity);
            }
            transaction.commit();

        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }

    }

    // returns all candidates for jsp presentation
    @Override
    public List<Candidate> getAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Candidate");
        PreparedQuery preparedQuery = datastoreService.prepare(query);

        for (Entity result : preparedQuery.asIterable()) {
            Candidate candidate = new Candidate();
            candidate.setFirstName((String) result.getProperty("firstName"));
            candidate.setSurName((String) result.getProperty("surname"));
            candidate.setFaculty((String) result.getProperty("faculty"));
            Long l = (Long) (result.getProperty("Votes"));
            int votes = l.intValue();
            candidate.setVotes(votes);
            candidate.setKey(result.getKey());
            candidates.add(candidate);
        }
        if (candidates.size() == 0) {
            return new ArrayList<>();
        }
        return candidates;
    }

    @Override
    public void updateCandidate(Candidate candidate, String key) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction transaction = datastoreService.beginTransaction();

//		Key k = KeyFactory.createKey("Candidate", Long.valueOf(key));
        Key k = new KeyFactory.Builder("Election", "Candidate").addChild("Candidate", Long.parseLong(key)).getKey();
        try {
            Entity entity = datastoreService.get(k);
            entity.setProperty("firstName", candidate.getFirstName());
            entity.setProperty("surname", candidate.getSurName());
            entity.setProperty("faculty", candidate.getFaculty());
            datastoreService.put(transaction, entity);
            transaction.commit();
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    // get candidate by key
    @Override
    public Candidate getCandidateByID(String id) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key key = new KeyFactory.Builder("Election", "Candidate").addChild("Candidate", Long.parseLong(id)).getKey();
        try {
            Entity candidate = datastoreService.get(key);
            Candidate can = new Candidate();
            can.setFirstName((String) candidate.getProperty("firstName"));
            can.setSurName((String) candidate.getProperty("surname"));
            can.setFaculty((String) candidate.getProperty("faculty"));
            can.setKey(candidate.getKey());
            return can;
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}