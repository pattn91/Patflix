/* A servlet to display the contents of the MySQL movieDB database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class addStar extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getServletInfo() {
		return "add star";
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
		out.println("<h4 class=\"tm-article-subtitle\">New Star Form</h4>");
		out.println("");
		out.println("                            <form class=\"uk-form uk-form-horizontal\" action=\"addStarSubmit\" method=\"POST\">");
		out.println("");
		out.println("                                <div class=\"uk-form-row\">");
		out.println("                                    <label class=\"uk-form-label\" for=\"s_id\">Star ID</label>");
		out.println("                                    <div class=\"uk-form-controls\">");
		out.println("                                        <input type=\"text\" NAME=\"s_id\" id=\"s_id\" placeholder=\"000000\" required>");
		out.println("                                    </div>");
		out.println("                                </div>");
		out.println("                                <div class=\"uk-form-row\">");
		out.println("                                    <label class=\"uk-form-label\" for=\"fname\">First Name</label>");
		out.println("                                    <div class=\"uk-form-controls\">");
		out.println("                                        <input type=\"text\" NAME=\"fname\" id=\"fname\" placeholder=\"Jackie\" required>");
		out.println("                                    </div>");
		out.println("                                </div>");
		out.println("                                <div class=\"uk-form-row\">");
		out.println("                                    <label class=\"uk-form-label\" for=\"lname\">Last Name</label>");
		out.println("                                    <div class=\"uk-form-controls\">");
		out.println("                                        <input type=\"text\" NAME=\"lname\" id=\"lname\" placeholder=\"Chan\" required>");
		out.println("                                    </div>");
		out.println("                                </div>");
		out.println("                                <div class=\"uk-form-row\">");
		out.println("                                    <label class=\"uk-form-label\" for=\"dob\">Date of Birth</label>");
		out.println("                                    <div class=\"uk-form-controls\">");
		out.println("                                        <input type=\"date\" NAME=\"dob\" id=\"dob\" placeholder=\"1970-01-01\">");
		out.println("                                    </div>");
		out.println("                                </div>");
		out.println("                                <div class=\"uk-form-row\">");
		out.println("                                    <label class=\"uk-form-label\" for=\"photo_url\">Banner URL</label>");
		out.println("                                    <div class=\"uk-form-controls\">");
		out.println("                                        <input type=\"url\" NAME=\"photo_url\" id=\"photo_url\" placeholder=\"http://www..\">");
		out.println("                                    </div>");
		out.println("                                </div>");
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

			 
			out.println("  <button type=\"submit\" class=\"uk-button\">Add Star</button>");
			out.println("                            </form>");
			out.println("                                </div>");
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
}