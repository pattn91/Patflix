/* A servlet to display the contents of the MySQL movieDB database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class dbReports extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getServletInfo() {
		return "manage usrs";
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
		out.println("<h1>Admin Dashboard</h1>");
		out.println("");
		out.println("            <div class=\"uk-grid\" data-uk-grid-margin>");
		out.println("");
		out.println("                <div class=\"uk-width-medium-1-1\">");
		out.println("                    <button class=\"uk-button\" data-uk-offcanvas=\"{target:'#admin-panel'}\">Open Admin Panel</button>");
		out.println("                </div>");
		out.println("");
		out.println("                <div class=\"uk-width-medium-1-2\">");
		out.println("                   <div class=\"uk-panel uk-panel-box\">");
		//need 3 divs to end above
		// continue in try block

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
			
			String choice = request.getParameter("report");
			switch(choice)
			{
			case "moviesWithoutStar": moviesWithoutStar(out, dbcon); break;
			case "starsWithoutMovie": starsWithoutMovie(out, dbcon); break;
			case "moviesWithoutGenre": moviesWithoutGenre(out, dbcon); break;
			case "genresWithoutMovie": genresWithoutMovie(out, dbcon); break;
			case "starsWithoutName": starsWithoutName(out, dbcon); break;
			case "expiredCreditCards": expiredCreditCards(out, dbcon); break;
			case "dupeMovies": dupeMovies(out, dbcon); break;
			case "dupeStars": dupeStars(out, dbcon); break;
			case "dupeGenres": dupeGenres(out, dbcon); break;
			case "dateChecker": dateChecker(out, dbcon); break;
			case "emailChecker": emailChecker(out, dbcon); break;
			}
			//close divs from panel
			out.println("    </div>");
			out.println("  </div>");
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");
			dbcon.close();
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL Exception:  " + ex.getMessage());
				ex = ex.getNextException();
			} // end while
		} // end catch SQLException

		catch (java.lang.Exception ex) {
			out.println("<HTML>" + "<HEAD><TITLE>" + "MovieDB: Error"
					+ "</TITLE></HEAD>\n<BODY>" + "<P>SQL error in doGet: "
					+ ex.getMessage() + "</P></BODY></HTML>");
			return;
		}
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doGet(request, response);
	}
	//INSERT ALL THE REPORT STUFF BELOW
	public static void moviesWithoutStar(PrintWriter out, Connection connection)
	{
		out.println("The following are movies without stars:");
		out.println("<BR>");
		
		ResultSet result;
		Statement select;
		
		try {
			select = connection.createStatement();
			
			result = select.executeQuery("SELECT * FROM movies WHERE id NOT IN (SELECT movie_id "
					+ "FROM stars_in_movies);");
			
			if (result.first())
			{
				do
				{
					out.println("Movie ID: " + result.getInt(1)); out.println("<BR>");
					out.println("Title: " + result.getString(2)); out.println("<BR>");
					out.println("Year: " + result.getInt(3)); out.println("<BR>");
					out.println("Director: " + result.getString(4)); out.println("<BR>");
					out.println("Banner URL: " + result.getString(5)); out.println("<BR>");
					out.println("Trailer URL: " + result.getString(6)); out.println("<BR>");
					out.println(); out.println("<BR>");
				} while (result.next());
				out.println(); out.println("<BR>");
			}
			else
			{
				out.println("No movies without stars found.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void starsWithoutMovie(PrintWriter out, Connection connection)
	{
		out.println("The following are stars without movies:");
		out.println("<BR>");
		
		ResultSet result;
		Statement select;
		
		try {
			select = connection.createStatement();
			
			result = select.executeQuery("SELECT * FROM stars WHERE id NOT IN (SELECT star_id "
					+ "FROM stars_in_movies);");
			
			if (result.first())
			{
				do
				{
					
					out.println("Star ID: " + result.getInt(1)); out.println("<BR>");
					out.println("Name: " + result.getString(2) + " " 
							+ result.getString(3)); out.println("<BR>");
					out.println("Date of birth: " + result.getString(4)); out.println("<BR>");
					out.println("URL: " + result.getString(5)); out.println("<BR>");
					out.println(); out.println("<BR>");
				} while (result.next());
				out.println(); out.println("<BR>");
			}
			else
			{
				out.println("No stars without movies found.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void moviesWithoutGenre(PrintWriter out, Connection connection)
	{
		out.println("The following are movies without genres:");
		out.println("<BR>");
		
		ResultSet result;
		Statement select;
		
		try {
			select = connection.createStatement();
			
			result = select.executeQuery("SELECT * FROM movies WHERE id NOT IN (SELECT movie_id "
					+ "FROM genres_in_movies);");
			
			if (result.first())
			{
				do
				{
					out.println("Movie ID: " + result.getInt(1)); out.println("<BR>");
					out.println("Title: " + result.getString(2)); out.println("<BR>");
					out.println("Year: " + result.getInt(3)); out.println("<BR>");
					out.println("Director: " + result.getString(4)); out.println("<BR>");
					out.println("Banner URL: " + result.getString(5)); out.println("<BR>");
					out.println("Trailer URL: " + result.getString(6)); out.println("<BR>");
					out.println(); out.println("<BR>");
				} while (result.next());
				out.println(); out.println("<BR>");
			}
			else
			{
				out.println("No movies without genres found.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void genresWithoutMovie(PrintWriter out, Connection connection)
	{
		out.println("The following are genres without movies:");
		out.println("<BR>");
		
		ResultSet result;
		Statement select;
		
		try {
			select = connection.createStatement();
			
			result = select.executeQuery("SELECT * FROM genres WHERE id NOT IN (SELECT genre_id "
					+ "FROM genres_in_movies);");
			
			if (result.first())
			{
				do
				{
					
					out.println("Genre ID: " + result.getInt(1)); out.println("<BR>");
					out.println("Genre: " + result.getString(2)); out.println("<BR>");
					out.println(); out.println("<BR>");
				} while (result.next());
				out.println(); out.println("<BR>");
			}
			else
			{
				out.println("No genres without movies found.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void starsWithoutName(PrintWriter out, Connection connection)
	{
		out.println("The following are stars without a first name or last name:");
		out.println("<BR>");
		
		ResultSet result;
		Statement select;
		
		try {
			select = connection.createStatement();
			
			result = select.executeQuery("SELECT * FROM stars WHERE first_name IS NULL OR last_name IS NULL;");
			
			if (result.first())
			{
				do
				{
					
					out.println("Star ID: " + result.getInt(1)); out.println("<BR>");
					out.println("Date of birth: " + result.getString(4)); out.println("<BR>");
					out.println("URL: " + result.getString(5)); out.println("<BR>");
					out.println(); out.println("<BR>");
				} while (result.next());
				out.println(); out.println("<BR>");
			}
			else
			{
				out.println("No stars without a first name or last name found.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void expiredCreditCards(PrintWriter out, Connection connection)
	{	
		out.println("The following are expired credit cards:");
		out.println("<BR>");
		
		ResultSet result;
		Statement select;
		
		try {
			select = connection.createStatement();
			
			result = select.executeQuery("SELECT * FROM creditcards WHERE id IN (SELECT cc_id "
					+ "FROM customers) AND expiration < CURDATE();");
			
			if (result.first())
			{
				do
				{
					out.println("Credit Card Number: " + result.getString(1)); out.println("<BR>");
					out.println("Name: " + result.getString(2) + " " + result.getString(3)); out.println("<BR>");
					out.println("Expiration Date: " + result.getString(4)); out.println("<BR>");
					out.println(); out.println("<BR>");
				} while (result.next());
				out.println(); out.println("<BR>");
			}
			else
			{
				out.println("No expired credit cards found.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void dupeMovies(PrintWriter out, Connection connection)
	{	
		out.println("The following are duplicate movies:");
		out.println("<BR>");
		
		ResultSet result;
		Statement select;
		
		try {
			select = connection.createStatement();
			
			result = select.executeQuery("SELECT id, title FROM movies WHERE title IN "
				+ "(SELECT title FROM movies GROUP BY title, year HAVING count(*) > 1) "
				+ "ORDER BY title;");
			
			if (result.first())
			{
				do
				{
					out.println("Movie ID: " + result.getInt(1)); out.println("<BR>");
					out.println("Movie Name: " + result.getString(2)); out.println("<BR>");
					out.println(); out.println("<BR>");
				} while (result.next());
				out.println(); out.println("<BR>");
			}
			else
			{
				out.println("No duplicate movies found.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void dupeStars(PrintWriter out, Connection connection)
	{	
		out.println("The following are duplicate stars:");
		out.println("<BR>");
		
		ResultSet result;
		Statement select;
		
		try {
			select = connection.createStatement();
			
			result = select.executeQuery("SELECT id, first_name, last_name, dob FROM stars WHERE first_name IN "
				+ "(SELECT first_name FROM stars GROUP BY first_name, last_name, dob HAVING count(*) > 1) "
				+ "ORDER BY first_name;");
			
			if (result.first())
			{
				do
				{
					out.println("Star ID: " + result.getInt(1)); out.println("<BR>");
					out.println("Name: " + result.getString(2) + " " + result.getString(3)); out.println("<BR>");
					out.println("Date of Birth: " + result.getString(4)); out.println("<BR>");
					out.println(); out.println("<BR>");
				} while (result.next());
				out.println(); out.println("<BR>");
			}
			else
			{
				out.println("No duplicate stars found.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void dupeGenres(PrintWriter out, Connection connection)
	{	
		out.println("The following are duplicate genres:");
		out.println("<BR>");
		
		ResultSet result;
		Statement select;
		
		try {
			select = connection.createStatement();
			
			result = select.executeQuery("SELECT id, name FROM genres WHERE name IN "
				+ "(SELECT name FROM genres GROUP BY name HAVING count(*) > 1) "
				+ "ORDER BY name;");
			
			if (result.first())
			{
				do
				{
					out.println("Genre ID: " + result.getInt(1)); out.println("<BR>");
					out.println("Genre Name: " + result.getString(2)); out.println("<BR>");
					out.println(); out.println("<BR>");
				} while (result.next());
				out.println(); out.println("<BR>");
			}
			else
			{
				out.println("No duplicate stars found.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void dateChecker(PrintWriter out, Connection connection)
	{	
		out.println("The following are stars that have birth dates that are after today's date or "
				+ "before the date 1900-01-01: ");
		out.println("<BR>");
		
		ResultSet result;
		Statement select;
		
		try {
			select = connection.createStatement();
			
			result = select.executeQuery("SELECT id, first_name, last_name, dob from "
					+ "stars WHERE dob > CURDATE() OR dob < '1900-01-01' ORDER BY first_name;");
			
			if (result.first())
			{
				do
				{
					out.println("Star ID: " + result.getInt(1)); out.println("<BR>");
					out.println("Name: " + result.getString(2) + " " + result.getString(3)); out.println("<BR>");
					out.println("Date of Birth: " + result.getString(4)); out.println("<BR>");
					out.println(); out.println("<BR>");
				} while (result.next());
				out.println(); out.println("<BR>");
			}
			else
			{
				out.println("No stars found.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void emailChecker(PrintWriter out, Connection connection)
	{	
		out.println("The following are customers with emails missing @: ");
		out.println("<BR>");
		
		ResultSet result;
		Statement select;
		
		try {
			select = connection.createStatement();
			
			result = select.executeQuery("SELECT * FROM customers WHERE email NOT IN "
					+ "(SELECT email FROM customers WHERE email LIKE '%@%');");
			
			if (result.first())
			{
				do
				{
					out.println("Customer ID: " + result.getInt(1)); out.println("<BR>");
					out.println("Name: " + result.getString(2) + " " + result.getString(3)); out.println("<BR>");
					out.println("Email: " + result.getString(6)); out.println("<BR>");
					out.println(); out.println("<BR>");
				} while (result.next());
				out.println(); out.println("<BR>");
			}
			else
			{
				out.println("No customers found.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}