package com.dsam.appengine.backend.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.dsam.appengine.shared.persistence.TokenManagement;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;

public class TokenEntity implements TokenManagement{

	// generates a unique token
	@Override
	public String generateToken() {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Transaction transaction = datastoreService.beginTransaction();
		String uniqueToken = UUID.randomUUID().toString();
		try {
			Entity token = new Entity("Token");
			token.setProperty("TokenString", uniqueToken);
			token.setProperty("Invalidate", "false");
			datastoreService.put(transaction, token);
			transaction.commit();
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
		return uniqueToken;
	}
	
	// check if a user given token exists in the db
	@Override
	public boolean validateToken(String token) throws Exception {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new FilterPredicate("TokenString", FilterOperator.EQUAL, token);
		Query query = new Query("Token").setFilter(filter);
		Entity entity = datastoreService.prepare(query).asSingleEntity();
		if(entity == null) {
			throw new Exception("Token is invalid. please try with a correct token");
		} 
		else if(entity.getProperty("Invalidate").toString().equals("true")) {
			throw new Exception("The token is expired. Sorry You can not vote.");
		}
		return true; 
	}
	
	// remove the token upon using from the db
	@Override
	public void inValidateToken(String token) {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Transaction transaction = datastoreService.beginTransaction();
		Filter filter = new FilterPredicate("TokenString", FilterOperator.EQUAL, token);
		Query query = new Query("Token").setFilter(filter);
		try {
			Entity entity = datastoreService.prepare(query).asSingleEntity();
			entity.setProperty("Invalidate", "true");
			datastoreService.put(transaction, entity);
			transaction.commit();
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
	}

	// returns all the token enties in a list
	@Override
	public List<Entity> getAllTokens() {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Token");
		List<Entity> entities = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
		return (entities.isEmpty()) ? new ArrayList<Entity>() : entities;
	}
	
}
