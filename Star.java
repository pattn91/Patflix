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
import javax.sql.DataSource;

public class Star extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getServletInfo() {
		return "Stars";
	}

	// Use http GET

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("text/html"); // Response mime type

		// Output stream to STDOUT
		PrintWriter out = response.getWriter();
		navBar.doGet(response);
		
		//containerrrrrrrrrrrr
		out.println("<div class=\"uk-container uk-container-center\">");
		out.println("            <div class=\"uk-grid\" data-uk-grid-margin>");
		out.println("                <div class=\"uk-width-medium-3-4\">");
		out.println("                   <div class=\"uk-panel uk-panel-box\">");
		//need 3 divs to end above
		// continue in try block

		try {
			// Class.forName("org.gjt.mm.mysql.Driver");
			// Class.forName("com.mysql.jdbc.Driver").newInstance();

			// Connection dbcon = DriverManager.getConnection(loginUrl,
			// loginUser,
			// loginPasswd);

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

			String stars = request.getParameter("star");

			Statement select;
			select = dbcon.createStatement();
			Statement stmt = dbcon.createStatement();
			Statement stmt2 = dbcon.createStatement();

			ResultSet starsResult = null;
			ResultSet starsToMovieResult = null;
			ResultSet moviesResult = null;

			System.out.println();

			starsResult = select.executeQuery("SELECT * FROM stars "
					+ "WHERE id = '" + stars + "';");

			if (starsResult.first() == false)
				out.println("No search results found.");
			else {
				do {
					out.println("Star ID: " + starsResult.getInt(1));
					out.println("<BR>");
					out.println("Name: " + starsResult.getString(2) + " "
							+ starsResult.getString(3));
					out.println("<BR>");
					out.println("Birthdate: " + starsResult.getDate(4));
					out.println("<BR>");
					out.println("Photo: <img src=\"" + starsResult.getString(5)
							+ "\">");
					out.println("<BR>");
					out.print("Starred in: ");
					// stars loop
					starsToMovieResult = stmt
							.executeQuery("SELECT movie_id FROM stars_in_movies "
									+ "WHERE star_id = '"
									+ starsResult.getInt(1) + "';");

					if (starsToMovieResult.first()) {
						do {
							moviesResult = stmt2
									.executeQuery("SELECT id, title FROM "
											+ "movies WHERE id = '"
											+ starsToMovieResult.getInt(1)
											+ "';");
							if (moviesResult.first())
								do {
									out.println("<a href=\"Movie?by=id&amp;movie="
											+ moviesResult.getInt(1)
											+ "\">"
											+ moviesResult.getString(2)
											+ "</a><BR>");
								} while (moviesResult.next());
						} while (starsToMovieResult.next());
					}

					out.println("<BR>");
					out.println("<BR>");
				} while (starsResult.next());
			}
		} catch (SQLException | NamingException e) {
			// TODO Auto-generated catch block
			out.println("The movie or movie ID could not be found.");
			out.println();
		}
		//close divs from panel
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