package com.dsam.appengine.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dsam.appengine.backend.entities.ElectionEntity;

public class ElectionResultServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ElectionEntity electionEntity = new ElectionEntity();
            request.setAttribute("voters", electionEntity.totalVoters());
            request.setAttribute("votescast", electionEntity.totalVotesCast());
            request.setAttribute("percentage", electionEntity.percentageOfVotesCast());
            request.setAttribute("candidates", electionEntity.candidatesOrderByVotes());
            request.setAttribute("VotingPeriod", electionEntity.getDates());
            request.getRequestDispatcher("/result.jsp").forward(request, response);
    }
}
