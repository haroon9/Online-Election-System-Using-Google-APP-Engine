package com.dsam.appengine.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorHandlerServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        generateErrorPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        generateErrorPage(req, resp);
    }

    private void generateErrorPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Exception exception = (Exception) req.getAttribute("javax.servlet.error.exception");
        Class exceptionClass = (Class) req.getAttribute("javax.servlet.error.exception_type");
        Integer code = (Integer) req.getAttribute("javax.servlet.error.status_code");
        String errorMessage = (String) req.getAttribute("javax.servlet.error.message");
        String requestUri = (String) req.getAttribute("javax.servlet.error.request_uri");
        String servletName = (String) req.getAttribute("javax.servlet.error.servlet_name");
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.print("<div style=\"color: #721c24; background-color: #f8d7da; border-color: #f5c6cb; position: relative; padding: .75rem 1.25rem; margin-bottom: 1rem; border: 1px solid transparent; border-radius: .25rem;\"><div style=\"margin-left: auto; margin-right: auto; font-family: Segoe UI; width: 550px; margin-bottom: 2rem; font-size: 1.5rem; font-weight: 500;\">An error occured, please check the details below</div>");
        writer.print("<table style=\"margin-left: auto; margin-right: auto; font-family: Segoe UI;\">");
        writer.printf("<tr><td style=\"font-weight: 500; padding-right: .5rem;\">exception </td><td>%s%n</td></tr>", exception);
        writer.printf("<tr><td style=\"font-weight: 500; padding-right: .5rem;\">exception_type </td><td> %s%n</td></tr>", exceptionClass);
        writer.printf("<tr><td style=\"font-weight: 500; padding-right: .5rem;\">status_code </td><td> %s%n</td></tr>", code);
        writer.printf("<tr><td style=\"font-weight: 500; padding-right: .5rem;\">message </td><td> %s%n</td></tr>", errorMessage);
        writer.printf("<tr><td style=\"font-weight: 500; padding-right: .5rem;\">request_uri </td><td> %s%n</td></tr>", requestUri);
        writer.printf("<tr><td style=\"font-weight: 500; padding-right: .5rem;\">servlet_name </td><td> %s%n</td></tr>", servletName);
        writer.print("</table></div>");
    }
}
