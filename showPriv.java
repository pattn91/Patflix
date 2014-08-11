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

public class showPriv extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getServletInfo() {
		return "show privleges";
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
		out.println("                <div class=\"uk-width-medium-3-4\">");
		out.println("                   <div class=\"uk-panel uk-panel-box\">");
		//need 3 divs to end above
		// continue in try block

		try {
			// connection pooling
			Context initCtx = new                 InitialContext();
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
			
			//RETRIEVE USERNAME AND HOST
			String host = request.getParameter("host");
			String username = request.getParameter("username");
			
			Statement statement = dbcon.createStatement();
			ResultSet rs = statement.executeQuery("SELECT Host, Db, User, Select_priv, "
					+ "Insert_priv, Update_priv, Delete_priv, Create_priv, Drop_priv "
					+ "FROM mysql.db WHERE User = '" + username + "';");
			
			if (rs.first() == false)
			{
				out.println("User permissions not found.<BR>");
				out.println("<a class=\"uk-button uk-button-danger\" href=\"editPriv?host=localhost&amp;username=" 
						+ username + "\">Edit Privileges</a>");
			}
			else
			{
				out.println("<h2>Global Grants</h2>");
				out.println("<table class=\"uk-table uk-table-hover uk-table-striped\">");
				out.println("<thead>");
				out.println("<tr>");
				out.println("<th>Host</th>");
				out.println("<th>Database</th>");
				out.println("<th>User</th>");
				out.println("<th>Select</th>");
				out.println("<th>Insert</th>");
				out.println("<th>Update</th>");
				out.println("<th>Delete</th>");
				out.println("<th>Create</th>");
				out.println("<th>Drop</th>");
				out.println("</tr>");
				out.println("</thead>");
				out.println("<tbody>");
				do 
				{
					out.println("<form class=\"uk-form uk-margin\" action=\"editPriv\" method=\"GET\">");
					out.println("<input type=\"hidden\" name=\"host\" value=\""+ host +"\">");
					out.println("<input type=\"hidden\" name=\"username\" value=\""+ username +"\">");
					out.println("<input type=\"hidden\" name=\"dbname\" value=\"" + rs.getString(2) + "\">");
					out.println("<tr>");
					out.println("<td>"+ rs.getString(1) +"</td>");
					out.println("<td>"+ rs.getString(2) +"</td>");
					out.println("<td>"+ rs.getString(3) +"</td>");
					out.println("<td>"+ rs.getString(4) +"</td>");
					out.println("<td>"+ rs.getString(5) +"</td>");
					out.println("<td>"+ rs.getString(6) +"</td>");
					out.println("<td>"+ rs.getString(7) +"</td>");
					out.println("<td>"+ rs.getString(8) +"</td>");
					out.println("<td>"+ rs.getString(9) +"</td>");
					out.println("<td><button type=\"submit\" class=\"uk-button-small uk-button-danger\">Edit</button></td>");
					out.println("</tr>");
					out.println("</form>");
				} while(rs.next());
				out.println("</tbody>");
				out.println("</table>");
				
				//SHOW SUPER GRANTS
				out.println("<BR>");
				Statement statement2 = dbcon.createStatement();
				ResultSet rs2 = statement2.executeQuery("SHOW GRANTS FOR "
						+ username + "@" + host +";");
				if (rs2.first())
				{
					out.println("<h3>Explicit Grants</h3>");
					do
					{
						out.println(rs2.getString(1) +"<BR>");
					} while (rs2.next());
				} //else out.println("nothing here");
				
				//SHOW COLUMN GRANTS
				out.println("<BR>");
				Statement statement4 = dbcon.createStatement();
				ResultSet rs4 = statement4.executeQuery("SELECT host, Db, User, Table_name, Column_name, "
						+ "Timestamp FROM mysql.columns_priv WHERE User='" + username +"';");
				if (rs4.first())
				{
					out.println("<h3>Columns Grants</h3>");
					out.println("<table class=\"uk-table uk-table-hover uk-table-striped\">");
					out.println("<thead>");
					out.println("<tr>");
					out.println("<th>Host</th>");
					out.println("<th>Database</th>");
					out.println("<th>User</th>");
					out.println("<th>Table</th>");
					out.println("<th>Column</th>");
					out.println("<th>Timestamp</th>");
					out.println("</tr>");
					out.println("</thead>");
					out.println("<tbody>");
					
					do
					{
						out.println("<tr>");
						out.println("<td>"+ rs4.getString(1) +"</td>");
						out.println("<td>"+ rs4.getString(2) +"</td>");
						out.println("<td>"+ rs4.getString(3) +"</td>");
						out.println("<td>"+ rs4.getString(4) +"</td>");
						out.println("<td>"+ rs4.getString(5) +"</td>");
						out.println("<td>"+ rs4.getString(6) +"</td>");
						out.println("</tr>");
					} while (rs4.next());
					out.println("</tbody>");
					out.println("</table>");
				} //else out.println("nothing here");
				
				//SHOW STORED PROCEDURE GRANTS
				out.println("<BR>");
				Statement statement3 = dbcon.createStatement();
				ResultSet rs3 = statement3.executeQuery("SELECT host, Db, User, Routine_name, Proc_priv, "
						+ "Timestamp FROM mysql.procs_priv WHERE User='" + username +"';");
				if (rs3.first())
				{
					out.println("<h3>Stored Procedure Grants</h3>");
					out.println("<table class=\"uk-table uk-table-hover uk-table-striped\">");
					out.println("<thead>");
					out.println("<tr>");
					out.println("<th>Host</th>");
					out.println("<th>Database</th>");
					out.println("<th>User</th>");
					out.println("<th>Routine</th>");
					out.println("<th>Privleges</th>");
					out.println("<th>Timestamp</th>");
					out.println("</tr>");
					out.println("</thead>");
					out.println("<tbody>");
					
					do
					{
						out.println("<tr>");
						out.println("<td>"+ rs3.getString(1) +"</td>");
						out.println("<td>"+ rs3.getString(2) +"</td>");
						out.println("<td>"+ rs3.getString(3) +"</td>");
						out.println("<td>"+ rs3.getString(4) +"</td>");
						out.println("<td>"+ rs3.getString(5) +"</td>");
						out.println("<td>"+ rs3.getString(6) +"</td>");
						out.println("</tr>");
					} while (rs3.next());
					out.println("</tbody>");
					out.println("</table>");
					out.println("<BR>");
				} //else out.println("nothing here");
			}
			
			//close divs from panel
			out.println("    </div>");
			out.println("  </div>");
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");
			statement.close();
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