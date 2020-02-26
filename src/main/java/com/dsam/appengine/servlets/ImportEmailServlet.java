package com.dsam.appengine.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dsam.appengine.backend.entities.ElectionEntity;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import com.dsam.appengine.backend.entities.VoterEntity;

public class ImportEmailServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		VoterEntity voterEntity = new VoterEntity();
		req.setAttribute("voters", voterEntity.getAllVotersEmails());
		ElectionEntity electionEntity = new ElectionEntity();
		req.setAttribute("VotingPeriod", electionEntity.getDates());
		req.getRequestDispatcher("/voter.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		if(ServletFileUpload.isMultipartContent(req)) {
			FileItemFactory itemFactory = new DiskFileItemFactory();
			FileUpload fileUpload = new FileUpload(itemFactory);
			VoterEntity voterEntity = new VoterEntity();
			try {
				List<FileItem> items = fileUpload.parseRequest(new ServletRequestContext(req));

				for(FileItem item : items) {
					if(!item.isFormField()) {
						BufferedInputStream buff=new BufferedInputStream(item.getInputStream());
						BufferedReader reader = new BufferedReader(new InputStreamReader(buff));
						String email;

						//Read File Line By Line
						while ((email = reader.readLine()) != null)   {
							if (voterEntity.isValidEmail(email)) {
								voterEntity.addVoter(email);
							} else {
								throw new Exception("One or many emails are invalid");
							}
						}
						buff.close();
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			} catch (Exception e) {
				//e.printStackTrace();
				throw new ServletException(e.getMessage());
			}

		}


		resp.sendRedirect("/admin/voters");
	}
}
