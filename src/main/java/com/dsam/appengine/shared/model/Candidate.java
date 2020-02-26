package com.dsam.appengine.shared.model;

import com.google.appengine.api.datastore.Key;

public class Candidate {

	private String firstName;
	private String surName;
	private String faculty;
	Key key;
	private int votes;
	
	public Candidate() {
		// TODO Auto-generated constructor stub
	}
	
	public Candidate(String firstName, String surName, String faculty) {
		this.firstName = firstName;
		this.surName = surName;
		this.faculty = faculty;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSurName() {
		return surName;
	}
	public void setSurName(String surName) {
		this.surName = surName;
	}
	public String getFaculty() {
		return faculty;
	}
	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}
	
	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}
}
