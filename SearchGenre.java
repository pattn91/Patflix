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

public class SearchGenre extends HttpServlet {
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
		navBar.doGet(response);

		// containerrrrrrrrrrrr
		out.println("<div class=\"uk-container uk-container-center\">");
		out.println("            <div class=\"uk-grid\" data-uk-grid-margin>");
		out.println("                <div class=\"uk-width-medium-3-4\">");
		out.println("                   <div class=\"uk-panel uk-panel-box\">");
		// need 3 divs to end above
		// continue in try block

		// button flags for nextpage and previouspage
		boolean prevFlag = true;
		boolean nextFlag = true;

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

			String genre = request.getParameter("genre");
			String order = request.getParameter("order");
			String type = request.getParameter("type");

			HttpSession session = request.getSession();
			session.getAttribute("cart");

			// pagination vars
			int noOfRecords = 0;
			int counter = 0;
			int page = 1;
			int offset; // changes every page
			if (request.getParameter("page") != null)
				page = Integer.parseInt(request.getParameter("page"));
			prevFlag = (page == 0);
			offset = page * Integer.parseInt(request.getParameter("rpp"));
			int recordsPerPage = Integer.parseInt(request.getParameter("rpp")) + 1;
			// end pagination vars

			out.println("Sort by: ");
			out.print(" <a class=\"uk-button\" href=\"SearchGenre?by=title&amp;genre="
					+ genre
					+ "&amp;order=title&amp;type=asc&amp;page=0&amp;rpp="
					+ request.getParameter("rpp")
					+ "\"><i class=\"uk-icon-sort-alpha-asc\"></i> Title</a> ");
			out.print("<a class=\"uk-button\" href=\"SearchGenre?by=title&amp;genre="
					+ genre
					+ "&amp;order=title&amp;type=desc&amp;page=0&amp;rpp="
					+ request.getParameter("rpp")
					+ "\"><i class=\"uk-icon-sort-alpha-desc\"></i> Title</a> ");
			out.print("<a class=\"uk-button\" href=\"SearchGenre?by=title&amp;genre="
					+ genre
					+ "&amp;order=year&amp;type=asc&amp;page=0&amp;rpp="
					+ request.getParameter("rpp")
					+ "\"><i class=\"uk-icon-sort-numeric-asc\"></i> Year</a> ");
			out.print("<a class=\"uk-button\" href=\"SearchGenre?by=title&amp;genre="
					+ genre
					+ "&amp;order=year&amp;type=desc&amp;page=0&amp;rpp="
					+ request.getParameter("rpp")
					+ "\"><i class=\"uk-icon-sort-numeric-desc\"></i> Year</a> ");
			out.println("<BR><BR>");

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

			movieResult = select
					.executeQuery("SELECT "
							+ "SQL_CALC_FOUND_ROWS * FROM genres_in_movies join movies on genres_in_movies.movie_id"
							+ " = movies.id WHERE genre_id = (SELECT id FROM genres WHERE name = '"
							+ genre + "')" + " ORDER BY " + order + " " + type
							+ " LIMIT " + offset + ", " + recordsPerPage + ";");

			// begin pagination
			if (movieResult.last()) {
				noOfRecords = movieResult.getRow();
				if (noOfRecords < recordsPerPage)
					nextFlag = false;
				movieResult.beforeFirst();
			}
			// end pagination

			if (movieResult.first() == false)
				out.println("No search results found.");
			else {
				do {
					counter++; // increases count of records, quit if > 5
					out.println("Movie ID: " + movieResult.getInt(3));
					out.println("<BR>");
					out.println("Title: " + "<a href=\"Movie?by=id&amp;movie="
							+ movieResult.getString(3) + "\">"
							+ movieResult.getString(4) + "</a>");
					out.println("<BR>");
					out.println("Year: " + movieResult.getInt(5));
					out.println("<BR>");
					out.println("Director: " + movieResult.getString(6));
					out.println("<BR>");
					out.print("Stars: ");
					// stars loop
					movieToStarsResult = stmt3
							.executeQuery("SELECT star_id FROM stars_in_movies "
									+ "WHERE movie_id = '"
									+ movieResult.getInt(3) + "';");

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
									+ movieResult.getInt(3) + "';");

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
											+ "&amp;order=title&amp;type=asc&amp;page=0&amp;rpp=5\">"
											+ genreResult.getString(1)
											+ " </a>");
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
					out.println("<BR>");
					// add to cart button
					out.print("<script type=\"text/javascript\">"
							+ "function show_alert()"
							+ "{"
							+ "alert(\"Movie was added to cart!\");"
							+ "}"
							+ "</script>"
							+ "<FORM ACTION=\"AddToCart\" METHOD=\"GET\">"
							+ "<INPUT TYPE=\"HIDDEN\" NAME=\"movie\" VALUE=\""
							+ movieResult.getInt(3)
							+ "\">"
							+ "<INPUT TYPE=\"SUBMIT\" VALUE=\"Add to Cart\" onclick=\"show_alert()\" STYLE=\"float: left;\"> </FORM>");
					out.println("<BR><BR>");
					// end add to cart button
					if (counter == (recordsPerPage - 1)) {
						movieResult.next();
					} // counter is =5 then move cursor to end
				} while (movieResult.next());
				out.println("<BR>");
				// rpp modifier
				out.println("| <a href=\"SearchGenre?by=genre&amp;genre="
						+ genre
						+ "&amp;order=title&amp;type=asc&amp;page=0&amp;rpp=5\">5</a> |");
				out.print("| <a href=\"SearchGenre?by=title&amp;genre="
						+ genre
						+ "&amp;order=title&amp;type=asc&amp;page=0&amp;rpp=10\">10</a> |");
				out.print("| <a href=\"SearchGenre?by=title&amp;genre="
						+ genre
						+ "&amp;order=title&amp;type=asc&amp;page=0&amp;rpp=15\">15</a> |");
				out.print("| <a href=\"SearchGenre?by=title&amp;genre="
						+ genre
						+ "&amp;order=title&amp;type=asc&amp;page=0&amp;rpp=20\">20</a> |");
				out.print("| <a href=\"SearchGenre?by=title&amp;genre="
						+ genre
						+ "&amp;order=title&amp;type=asc&amp;page=0&amp;rpp=25\">25</a> |");
			}
		} catch (SQLException | NamingException e) {
			// TODO Auto-generated catch block
			out.println("No movies found under that genre.");
			out.println();
		}
		if (prevFlag) {
			out.print("<BUTTON STYLE=\"float: left;\">Previous Page </BUTTON>");
		} else {
			out.print("<FORM ACTION=\"SearchGenre\" METHOD=\"GET\">"
					+ "<INPUT TYPE=\"HIDDEN\" NAME=\"genre\" VALUE=\""
					+ request.getParameter("genre")
					+ "\">"
					+ "<INPUT TYPE=\"HIDDEN\" NAME=\"order\" VALUE=\""
					+ request.getParameter("order")
					+ "\">"
					+ "<INPUT TYPE=\"HIDDEN\" NAME=\"type\" VALUE=\""
					+ request.getParameter("type")
					+ "\">"
					+ "<INPUT TYPE=\"HIDDEN\" NAME=\"page\" VALUE=\""
					+ (Integer.parseInt((request.getParameter("page"))) - 1)
					+ "\">"
					+ "<INPUT TYPE=\"HIDDEN\" NAME=\"rpp\" VALUE=\""
					+ request.getParameter("rpp")
					+ "\">"
					+ "<INPUT TYPE=\"SUBMIT\" VALUE=\"Previous Page\" STYLE=\"float: left;\"> </FORM>");
		}
		out.print("   ");
		if (!nextFlag) {
			out.print("<BUTTON STYLE=\"float: right;\">Next Page </BUTTON>");
		} else {
			out.print("<FORM ACTION=\"SearchGenre\" METHOD=\"GET\">"
					+ "<INPUT TYPE=\"HIDDEN\" NAME=\"genre\" VALUE=\""
					+ request.getParameter("genre")
					+ "\">"
					+ "<INPUT TYPE=\"HIDDEN\" NAME=\"order\" VALUE=\""
					+ request.getParameter("order")
					+ "\">"
					+ "<INPUT TYPE=\"HIDDEN\" NAME=\"type\" VALUE=\""
					+ request.getParameter("type")
					+ "\">"
					+ "<INPUT TYPE=\"HIDDEN\" NAME=\"page\" VALUE=\""
					+ (Integer.parseInt((request.getParameter("page"))) + 1)
					+ "\">"
					+ "<INPUT TYPE=\"HIDDEN\" NAME=\"rpp\" VALUE=\""
					+ request.getParameter("rpp")
					+ "\">"
					+ "<INPUT TYPE=\"SUBMIT\" VALUE=\"Next Page\" STYLE=\"float: right;\"> </FORM>");
		}
		// close divs from panel
		out.println("    </div>");
		out.println("  </div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doGet(request, response);
	}
}