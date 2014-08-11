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

public class manageUsers extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getServletInfo() {
		return "manage users";
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
			
			//ADD NEW USER FORM BEGIN
			String newUser = request.getParameter("newUser");
			if(newUser != null && !newUser.isEmpty())
			{
				String host = request.getParameter("host");
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				Statement statement = dbcon.createStatement();
				ResultSet rs = statement.executeQuery("SELECT User FROM mysql.user WHERE User ='" + username + "';");
				if(rs.first())
				{
					out.println("<div class=\"uk-alert uk-alert-danger\"> Error: Username already exists! </div>");
				}
				else
				{
					Statement select = dbcon.createStatement();
					select.executeUpdate("CREATE USER '" + username + "'@'" + host + "' IDENTIFIED BY '" + password + "';");
					select.close();
					out.println("<div class=\"uk-alert uk-alert-success\"> User successfully added! </div>");
				}
					
			}
			//ADD NEW USER FORM END
			
			//DELETE USER BEGIN
			String delUser = request.getParameter("deleteBtn");
			if (delUser != null)
			{
				String host = request.getParameter("host");
				String username = request.getParameter("username");
				Statement statement = dbcon.createStatement();
				statement.executeUpdate("DROP user '" + username + "'@'" + host + "';");
				out.println("<div class=\"uk-alert uk-alert-success\"> User successfully deleted! </div>");
			}
			//DELETE USER END
			
			Statement statement = dbcon.createStatement();
			ResultSet rs = statement.executeQuery("SELECT Host, User FROM mysql.user;");
			
			if (rs.first() == false)
			{
				out.println("No users found.");
			}
			else
			{
				out.println("<table class=\"uk-table uk-table-hover uk-table-striped\">");
				out.println("<thead>");
				out.println("<tr>");
				out.println("<th>Host</th>");
				out.println("<th>User</th>");
				out.println("<th> </th>");
				out.println("<th> </th>");
				out.println("</tr>");
				out.println("</thead>");
				out.println("<tbody>");
				do 
				{
					out.println("<tr>");
					out.println("<form class=\"uk-form uk-margin\" action=\"manageUsers\" method=\"POST\">");
					out.println("<input type=\"hidden\" name=\"host\" value=\""+rs.getString(1)+"\">");
					out.println("<input type=\"hidden\" name=\"username\" value=\""+rs.getString(2)+"\">");
					out.println("<td>" + rs.getString(1) + "</td>");
					out.println("<td>" + rs.getString(2) + "</td>");
					out.println("<td>");
					out.println("<a class=\"uk-button uk-button-success\" href=\"showPriv?host="+ rs.getString(1) 
							+ "&amp;username=" + rs.getString(2) + "\">Privleges</a></td>");
					out.println("<td><button type=\"submit\" class=\"uk-button uk-button-danger\" name=\"deleteBtn\" value=\"deleteUser\">Delete</a></td>");
					out.println("</form>");
					out.println("</tr>");
				} while(rs.next());
				out.println("</tbody>");
				out.println("</table>");
				//add new user
				out.println("<a class=\"uk-button uk-button-primary\" data-uk-toggle=\"{target:'#addUser'}\">Add User</a>");
				out.println("<div id=\"addUser\" class=\"uk-hidden\">");
				out.println("<form class=\"uk-form uk-margin\" action=\"manageUsers\" method=\"POST\">");
				out.println("");
				out.println("                <fieldset data-uk-margin><BR>");
				out.println("                    <input type=\"hidden\" name=\"newUser\" value=\"1\">");
				out.println("                    <input type=\"text\" name=\"host\"placeholder=\"localhost\" class=\"uk-width-1-6\" required> @");
				out.println("                    <input type=\"text\" name=\"username\" placeholder=\"Username\" class=\"uk-form-width-small\" required>");
				out.println("                    <input type=\"password\" name=\"password\" placeholder=\"Password\" class=\"uk-form-width-small\" required>");
				out.println("                    <button type=\"submit\" class=\"uk-button-small uk-button-success\">Submit</button>");
				out.println("                </fieldset>");
				out.println("");
				out.println("            </form>");
				out.println("</div>");
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