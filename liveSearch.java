/* A servlet to display the contents of the MySQL movieDB database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

public class liveSearch extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getServletInfo() {
		return "MovieDB Search";
	}

	// Use http GET

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("text/html"); // Response mime type

		// Output stream to STDOUT
		PrintWriter out = response.getWriter();

		try {
			
			// connection pooling
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			if (envCtx == null)
				out.println("envCtx is NULL");

			// Look up our data source
			DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");

			if (ds == null)
				out.println("ds is null.");

			Connection dbcon = ds.getConnection();
			if (dbcon == null)
				out.println("dbcon is null.");

			String movie = request.getParameter("movie");
			String order = request.getParameter("order");
			String type = request.getParameter("type");
			String rpp = request.getParameter("rpp");
			if(order == null) order = "title";
			if(type == null) type = "asc";
			if(rpp == null) rpp = "5";
			// cart stuff
			HttpSession session = request.getSession();
			session.getAttribute("cart");

			// pagination vars
			int noOfRecords = 0;
			int page = 1;
			int offset; // changes every page
			if (request.getParameter("page") != null)
				page = Integer.parseInt(request.getParameter("page"));
			else page = 0;
			offset = page * Integer.parseInt(rpp);
			int recordsPerPage = Integer.parseInt(rpp) + 1;
			// end pagination vars

			Statement select;
			select = dbcon.createStatement();
			ResultSet movieResult = null;

			movieResult = select.executeQuery("SELECT "
					+ "SQL_CALC_FOUND_ROWS * FROM movies "
					+ "WHERE title LIKE '%" + movie + "%' ORDER BY " + order
					+ " " + type + " LIMIT " + offset + ", " + recordsPerPage
					+ ";");

			// begin pagination
			if (movieResult.last()) {
				noOfRecords = movieResult.getRow();
				if (noOfRecords < recordsPerPage) {
				}
				movieResult.beforeFirst();
			}
			// end pagination

			if (movieResult.first() == false)
				out.println("No search results found.");
			else {
				do {
					out.println("<a href=\"Movie?by=id&amp;movie="
							+ movieResult.getString(1) + "\">"
							+ movieResult.getString(2) + "</a> ("
							+ movieResult.getString(3)+")");
					out.println("<BR>");
				} while (movieResult.next());
			}
		} catch (SQLException | NamingException e) {
			// TODO Auto-generated catch block
			out.println("No Results.");
			out.println();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doGet(request, response);
	}
}