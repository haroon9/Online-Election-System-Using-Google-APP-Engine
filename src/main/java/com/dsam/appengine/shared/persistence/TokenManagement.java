package com.dsam.appengine.shared.persistence;

import java.util.List;

import com.google.appengine.api.datastore.Entity;

public interface TokenManagement {
	String generateToken();
	boolean validateToken(String token) throws Exception;
	void inValidateToken(String token);
	List<Entity> getAllTokens();
}
