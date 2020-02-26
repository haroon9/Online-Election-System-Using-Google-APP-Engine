package com.dsam.appengine.servlets;

import com.dsam.appengine.backend.entities.CandidateEntity;
import com.dsam.appengine.backend.entities.ElectionEntity;
import com.dsam.appengine.backend.entities.VoterEntity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VotingServlet extends HttpServlet {

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
        request.getRequestDispatcher("/voting.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String key = request.getParameter("c_key").trim();
        String token = request.getParameter("token").trim();

        VoterEntity voterEntity = new VoterEntity();
        try {
            voterEntity.addVote(key, token);
            response.sendRedirect("/votesuccess.jsp");
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {

    }
}
