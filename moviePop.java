/* A servlet to display the contents of the MySQL movieDB database */

import java.io.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class moviePop extends HttpServlet {
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

			Statement select;
			select = dbcon.createStatement();

			// For getting genres for the movie
			ResultSet genreResult = null;
			ResultSet movieToGenreResult = null;
			Statement stmt = dbcon.createStatement();
			Statement stmt2 = dbcon.createStatement();
			// End genre section

			// For getting stars for the movie
			ResultSet starsResult = null;
			ResultSet movieToStarsResult = null;
			Statement stmt3 = dbcon.createStatement();
			Statement stmt4 = dbcon.createStatement();
			// End stars section

			ResultSet movieResult = null;


			movieResult = select.executeQuery("SELECT * FROM movies "
					+ "WHERE id = '" + movie + "';");

			if (movieResult.first() == false)
				out.println("No search results found.");
			else {
				do {
					out.println("Movie ID: " + movieResult.getInt(1));
					out.println("<BR>");
					out.println("Title: " + movieResult.getString(2));
					out.println("<BR>");
					out.println("Year: " + movieResult.getInt(3));
					out.println("<BR>");
					out.println("Director: " + movieResult.getString(4));
					out.println("<BR>");
					out.print("Stars: ");
					// stars loop
					movieToStarsResult = stmt3
							.executeQuery("SELECT star_id FROM stars_in_movies "
									+ "WHERE movie_id = '"
									+ movieResult.getInt(1) + "';");

					if (movieToStarsResult.first()) {
						do {
							starsResult = stmt4
									.executeQuery("SELECT id, first_name, last_name FROM "
											+ "stars WHERE id = '"
											+ movieToStarsResult.getInt(1)
											+ "';");
							if (starsResult.first())
								do {
									out.println("<a href=\"Star?by=id&amp;star="
											+ starsResult.getInt(1)
											+ "\">"
											+ starsResult.getString(2)
											+ " "
											+ starsResult.getString(3) + "</a>");
								} while (starsResult.next());
						} while (movieToStarsResult.next());
					}

					out.println("<BR>");
					out.print("Genres: ");
					// genre loop

					movieToGenreResult = stmt
							.executeQuery("SELECT genre_id FROM genres_in_movies "
									+ "WHERE movie_id = '"
									+ movieResult.getInt(1) + "';");

					if (movieToGenreResult.first()) {
						do {
							genreResult = stmt2
									.executeQuery("SELECT name FROM genres WHERE id = '"
											+ movieToGenreResult.getInt(1)
											+ "';");
							if (genreResult.first())
								do {
									out.println("<a href=\"SearchGenre?by=id&amp;genre="
											+ genreResult.getString(1)
											+ "&amp;order=title&amp;type=asc&amp;page=0&amp;rpp=5"
											+ "\">"
											+ genreResult.getString(1)
											+ "</a>");
								} while (genreResult.next());
						} while (movieToGenreResult.next());
					}
					out.println("<BR>");
					out.println("Banner URL: " + "<img src=\""
							+ movieResult.getString(5) + "\" alt=\""
							+ movieResult.getString(2) + "\">");
					out.println("<BR>");
					out.println("Trailer URL: " + "<a href=\""
							+ movieResult.getString(6) + "\">"
							+ movieResult.getString(2) + "</a>");
					out.println("<BR>");
					out.println();
				} while (movieResult.next());
				out.println("<BR>");
			}
		} catch (SQLException | NamingException e) {
			// TODO Auto-generated catch block
			out.println("The movie or movie ID could not be found.");
			out.println();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doGet(request, response);
	}
}