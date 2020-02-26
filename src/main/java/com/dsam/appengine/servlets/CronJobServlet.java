package com.dsam.appengine.servlets;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dsam.appengine.backend.entities.ElectionEntity;
import com.dsam.appengine.backend.entities.VoterEntity;

public class CronJobServlet extends HttpServlet {

	/**
	 * 
	 */
	private boolean once = false;
	private static final long serialVersionUID = 1L;
	
	// notifies the voter 1 day before the election
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		ElectionEntity electionEntity = new ElectionEntity();
		if(!once && electionEntity.getDates().size() > 0) {
			LocalDateTime electionStartDate = electionEntity.getDates().get(0).getStart();
			LocalDateTime currentDate = LocalDateTime.now(ZoneId.of("Europe/Berlin"));
			Duration duration = Duration.between(electionStartDate, currentDate);
			long diff = Math.abs(duration.toDays());

			if(diff == 1 && electionStartDate.isAfter(currentDate)) {
				once = true;
				VoterEntity voterEntity = new VoterEntity();
				voterEntity.notifyVoters();
			}
			else {
				once = false;
			}
		}
		else {
			once = false;
		}
	}
}
