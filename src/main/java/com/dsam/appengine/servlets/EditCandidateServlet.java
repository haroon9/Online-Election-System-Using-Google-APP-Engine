package com.dsam.appengine.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dsam.appengine.backend.entities.CandidateEntity;
import com.dsam.appengine.shared.model.Candidate;

public class EditCandidateServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CandidateEntity candidateEntity = new CandidateEntity();
		String candidateKey = request.getParameter("c_key").trim();
		request.setAttribute("candidate", candidateEntity.getCandidateByID(candidateKey));
		request.getRequestDispatcher("/editcandidate.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fName = request.getParameter("fname").trim();
		String surname = request.getParameter("surname").trim();
		String faculty = request.getParameter("faculty").trim();
		String key = request.getParameter("key").trim();
		
		Candidate candidate = new Candidate(fName, surname, faculty);
		
		CandidateEntity candidateEntity = new CandidateEntity();
		candidateEntity.updateCandidate(candidate, key);
		response.sendRedirect("/admin/candidates");
	}
	
}
