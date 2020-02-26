package com.dsam.appengine.backend.entities;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.validator.routines.EmailValidator;

import com.dsam.appengine.shared.model.Voter;
import com.dsam.appengine.shared.persistence.VoterManagement;
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

public class VoterEntity implements VoterManagement {

	// add voter email to database
	@Override
	public void addVoter(String email) {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Transaction transaction = datastoreService.beginTransaction();
		TokenEntity tokenEntity = new TokenEntity();
		try {
			Key key = KeyFactory.createKey("Election", "Voter");
			Entity voterEntity = new Entity("Voter", key);
			if(!checkIfEmailExists(email)) {
				voterEntity.setProperty("VoterEmail", email);
				tokenEntity.generateToken();
				datastoreService.put(transaction, voterEntity);
			}
			transaction.commit();
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
	}

	// returns list of voters emails
	@Override
	public List<Voter> getAllVotersEmails() {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Voter");
		PreparedQuery preparedQuery = datastoreService.prepare(query);
		List<Voter> voters = new ArrayList<>();
		
		for(Entity v : preparedQuery.asIterable()) {
			Voter voter = new Voter();
			voter.setEmail((String) v.getProperty("VoterEmail"));
			voters.add(voter);
		}
		
		if(voters.size() == 0) {
			return new ArrayList<>();
		}
		return voters;
	}
	
	
	
	// notify the voters about the election
	public void notifyVoters() {
		Properties properties = new Properties();
		Session session = Session.getDefaultInstance(properties, null);
		
		try {
			TokenEntity tokenEntity = new TokenEntity();
			List<Entity> entities = tokenEntity.getAllTokens();
			int size = entities.size();
			for(Voter address : getAllVotersEmails()) {
				if(entities.get(size - 1).getProperty("Invalidate").toString().equals("false")) {
					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress("haroon.gul9@gmail.com", "Admin"));
					message.setReplyTo(null);
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(address.getEmail()));
					message.setSubject("University Election");
					message.setText("University election is starting tomorrow. Visit this link and use the"
							+ " below token to Vote. After using the token it will expire. When the election "
							+ "finishes you can visit the result link to see the immediate results.\n\n"
							+ "Voting interface: localhost:8080/voting \n"
							+ "Token: " + entities.get(size - 1).getProperty("TokenString") + "\n"
									+ "Election results: localhost:8080/result");
					Transport.send(message);
				}
				size--;
			}
		} catch (AddressException e) {
			e.getMessage();
		} catch (MessagingException e) {
			e.getMessage();
		} catch (UnsupportedEncodingException e) {
			e.getMessage();
		}
	}
	
	// checks if a given email is a valid email or not
	public boolean isValidEmail(String email) {
		EmailValidator validator = EmailValidator.getInstance();
		return validator.isValid(email);
	}
	
	public boolean checkIfEmailExists(String email) {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new FilterPredicate("VoterEmail", FilterOperator.EQUAL, email);
		Query query = new Query("Voter").setFilter(filter);
		Entity entity = datastoreService.prepare(query).asSingleEntity();
		return entity != null;
	}

	// add vote to candidate
	@Override
	public void addVote(String key, String token) throws Exception {
		TokenEntity tokenEntity = new TokenEntity();
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Transaction transaction = datastoreService.beginTransaction();
		Key k = new KeyFactory.Builder("Election", "Candidate").addChild("Candidate", Long.parseLong(key)).getKey();
		
		try {
			Entity candidate = datastoreService.get(k);
			if(tokenEntity.validateToken(token)) {
				candidate.setProperty("Votes", (Long) (candidate.getProperty("Votes")) + 1);
				tokenEntity.inValidateToken(token);
				datastoreService.put(transaction, candidate);
			}
			transaction.commit();
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// returns the size of all eligible voters
	public int getEligibleVoters() {
		return getAllVotersEmails().size();
	}
}
