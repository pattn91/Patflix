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

public class addStarSubmit extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getServletInfo() {
		return "MovieDB Login";
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
			 
			 String s_id = request.getParameter("s_id");
			 String fname = request.getParameter("fname");
			 String lname = request.getParameter("lname");
			 String dob = request.getParameter("dob");
			 String photo_url = request.getParameter("photo_url");
			 
			 
			 String query = "CALL add_star("+ s_id + ", '" + fname + "', '" + lname +
					 "', '" + dob + "', '" + photo_url + "');";
			// Perform the query
			 ResultSet rs = statement2.executeQuery("SELECT id, first_name,"
			 		 + " last_name, dob from stars where id =" 
					 + s_id + " OR first_name = '" + fname + "' AND " 
					 + "last_name = '" + lname + "' AND " 
					 + "dob = '" + dob + "';");
			 if(rs.first())
			 {
				 out.println("<h1>Error: Star already exists.</h1>");
			 }
			 else
			 {
				 out.println("<h1>Success! Star added.</h1>");
				 statement.executeQuery(query);
			 }
			 
			 out.println("Star ID: " + s_id);
				out.println("<BR>");
				out.println("Name: " + fname + " "
						+ lname);
				out.println("<BR>");
				out.println("Birthdate: " + dob);
				out.println("<BR>");
				out.println("Photo: <img src=\"" + photo_url
						+ "\">");
			out.println("<BR>");
			
			out.println("    </div>");
			out.println("  </div>");
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");
			statement.close();
			statement2.close();
			rs.close();
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