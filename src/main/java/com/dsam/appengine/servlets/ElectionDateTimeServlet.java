package com.dsam.appengine.servlets;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dsam.appengine.backend.entities.ElectionEntity;
import com.dsam.appengine.shared.model.VotingPeriod;

public class ElectionDateTimeServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ElectionEntity electionEntity = new ElectionEntity();
        request.setAttribute("VotingPeriod", electionEntity.getDates());
        request.getRequestDispatcher("/electiontime.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String startDate = request.getParameter("start").trim();
        String endDate = request.getParameter("end").trim();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        if (start.isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new DateTimeException("Starttime cannot be in the past!");
        } else if (end.isBefore(start)) {
            throw new DateTimeException("End of election can not be before it even started, duh!");

        } else {
            ElectionEntity electionEntity = new ElectionEntity();
            VotingPeriod period = new VotingPeriod();
            period.setStart(start);
            period.setEnd(end);

            try {
                electionEntity.create(Date.from(period.getStart().atZone(ZoneId.of("Europe/Berlin")).toInstant()), Date.from(period.getEnd().atZone(ZoneId.of("Europe/Berlin")).toInstant()));
            } catch (Exception e) {
                throw new ServletException(e.getMessage());
            }
        }

        response.sendRedirect("/");
    }
}
