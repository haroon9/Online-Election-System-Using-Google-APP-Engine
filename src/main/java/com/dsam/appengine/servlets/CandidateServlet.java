package com.dsam.appengine.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dsam.appengine.backend.entities.CandidateEntity;
import com.dsam.appengine.backend.entities.ElectionEntity;
import com.dsam.appengine.shared.model.Candidate;

public class CandidateServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CandidateEntity candidateEntity = new CandidateEntity();
		request.setAttribute("candidatesList", candidateEntity.getAllCandidates());
		ElectionEntity electionEntity = new ElectionEntity();
		request.setAttribute("VotingPeriod", electionEntity.getDates());
		request.getRequestDispatcher("/candidates.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String firstName = request.getParameter("fname").trim();
		String surname = request.getParameter("sname").trim();
		String faculty = request.getParameter("faculty").trim();
		
		Candidate candidate =  new Candidate(firstName, surname, faculty);
		
		CandidateEntity candidateEntity = new CandidateEntity();
		try {
			candidateEntity.create(candidate);
		} catch (Exception e) {
			throw new ServletException(e.getMessage());
		}
		
		response.sendRedirect("/admin/candidates");
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) {
		
	}
}
