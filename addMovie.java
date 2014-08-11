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

public class addMovie extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getServletInfo() {
		return "add movie";
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
		out.println("<h4 class=\"tm-article-subtitle\">New Movie Form</h4>");
		out.println("");
		out.println("                            <form class=\"uk-form uk-form-horizontal\" action=\"addMovieSubmit\" method=\"POST\">");
		out.println("");
		out.println("                                <div class=\"uk-form-row\">");
		out.println("                                    <label class=\"uk-form-label\" for=\"m_id\">Movie ID</label>");
		out.println("                                    <div class=\"uk-form-controls\">");
		out.println("                                        <input type=\"text\" NAME=\"m_id\" id=\"m_id\" placeholder=\"000000\" required>");
		out.println("                                    </div>");
		out.println("                                </div>");
		out.println("                                <div class=\"uk-form-row\">");
		out.println("                                    <label class=\"uk-form-label\" for=\"title\">Title</label>");
		out.println("                                    <div class=\"uk-form-controls\">");
		out.println("                                        <input type=\"text\" NAME=\"title\" id=\"title\" placeholder=\"Rush Hour 3\" required>");
		out.println("                                    </div>");
		out.println("                                </div>");
		out.println("                                <div class=\"uk-form-row\">");
		out.println("                                    <label class=\"uk-form-label\" for=\"year\">Year</label>");
		out.println("                                    <div class=\"uk-form-controls\">");
		out.println("                                        <input type=\"number\" NAME=\"year\" min=\"1800\" max=\"9999\" id=\"year\" placeholder=\"2004\" required>");
		out.println("                                    </div>");
		out.println("                                </div>");
		out.println("                                <div class=\"uk-form-row\">");
		out.println("                                    <label class=\"uk-form-label\" for=\"director\">Director</label>");
		out.println("                                    <div class=\"uk-form-controls\">");
		out.println("                                        <input type=\"text\" NAME=\"director\" id=\"director\" placeholder=\"Tommy Lee\" required>");
		out.println("                                    </div>");
		out.println("                                </div>");
		out.println("                                <div class=\"uk-form-row\">");
		out.println("                                    <label class=\"uk-form-label\" for=\"banner_url\">Banner URL</label>");
		out.println("                                    <div class=\"uk-form-controls\">");
		out.println("                                        <input type=\"url\" NAME=\"banner_url\" id=\"banner_url\" placeholder=\"http://www..\">");
		out.println("                                    </div>");
		out.println("                                </div>");
		out.println("                                <div class=\"uk-form-row\">");
		out.println("                                    <label class=\"uk-form-label\" for=\"trailer_url\">Trailer URL</label>");
		out.println("                                    <div class=\"uk-form-controls\">");
		out.println("                                        <input type=\"url\" NAME=\"trailer_url\" id=\"trailer_url\" placeholder=\"http://www..\">");
		out.println("                                    </div>");
		out.println("                                </div>");
		// continue form star loop and genre loop in try block

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

			 //Declare our statement
			 Statement statement = dbcon.createStatement();
			 Statement statement2 = dbcon.createStatement();
			// String email = request.getParameter("email");
			// String pass = request.getParameter("password");
			 String getStars = "SELECT id, first_name, last_name FROM stars ORDER BY first_name ASC;";
			 String getGenres = "SELECT id, name FROM genres ORDER BY name ASC;";

			// Perform the query
			ResultSet rs = statement.executeQuery(getStars);
			ResultSet rs2 = statement2.executeQuery(getGenres);

			// Iterate through each row of rs
			// if (rs.first()) {
			// @SuppressWarnings("unused")
			// HttpSession session = request.getSession(true);
			// response.sendRedirect("home.html");
			// } else {
			// //out.println("Incorrect Login");
			// }
			out.println("                                <div class=\"uk-form-row\">");
			out.println("                                    <label class=\"uk-form-label\" for=\"star\">Main Star</label>");
			out.println("                                    <div class=\"uk-form-controls\">");
			out.println("                                        <select id=\"star\" name=\"s_id\">");
			if (rs.first()) {
				do {
					out.println("<option value=\"" + rs.getInt(1) + "\">"+ rs.getString(2) + " " + rs.getString(3) +"</option>");
				} while (rs.next());
			}
			out.println("                                        </select>");
			out.println("                                    </div>");
			out.println("                                </div>");
			out.println("                                <div class=\"uk-form-row\">");
			out.println("                                    <label class=\"uk-form-label\" for=\"genre\">Main Genre</label>");
			out.println("                                    <div class=\"uk-form-controls\">");
			out.println("                                        <select id=\"genre\" name=\"g_id\">");
			if (rs2.first()) {
				do {
					out.println("<option value=\"" + rs2.getInt(1) + "\">"+ rs2.getString(2) +"</option>");
				} while (rs2.next());
			}
			out.println("                                        </select>");
			out.println("                                    </div>");
			out.println("                                </div>");
			out.println("  <button type=\"submit\" class=\"uk-button\">Add Movie</button>");
			out.println("                            </form>");
			out.println("                                </div>");
			out.println("    </div>");
			out.println("  </div>");
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");
			rs.close();
			rs2.close();
			statement.close();
			statement2.close();
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
}