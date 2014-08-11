/* A servlet to display the contents of the MySQL movieDB database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class editPriv extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getServletInfo() {
		return "edit privleges";
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
			
			//RETRIEVE USERNAME AND HOST
			String host = request.getParameter("host");
			String username = request.getParameter("username");
			
			//GRANT PRIVELEGES HERE
			String grant = request.getParameter("grant");
			if(grant != null && !grant.isEmpty())
			{
				String dbname = request.getParameter("dbname");
				String selectCheck = request.getParameter("select");
				String insertCheck = request.getParameter("insert");
				String updateCheck = request.getParameter("update");
				String deleteCheck = request.getParameter("delete");
				String createCheck = request.getParameter("create");
				String dropCheck = request.getParameter("drop");
				String columnCheck = request.getParameter("column");
				
				ArrayList<String> options = new ArrayList<String>();
				if(selectCheck != null) options.add(selectCheck);
				if(insertCheck != null) options.add(insertCheck);
				if(updateCheck != null) options.add(updateCheck);
				if(deleteCheck != null) options.add(deleteCheck);
				if(createCheck != null) options.add(createCheck);
				if(dropCheck != null) options.add(dropCheck);
				String delim = ", ";
				StringBuilder sb = new StringBuilder();
				for (String s: options)
				{
					sb.append(s).append(delim);
				}
				String perm = sb.substring(0, sb.length()-2);
				System.out.println(perm + " " + columnCheck);
				
				Statement statement = dbcon.createStatement();
				Statement flushpriv = dbcon.createStatement();
				
				if (columnCheck != null)
				{
					statement.executeUpdate("GRANT " + perm + " ON " 
						+ dbname + "." + columnCheck + " TO '" + username + "'@'" + host + "';");
				}
				flushpriv.executeUpdate("FLUSH PRIVILEGES;");
				out.println("<div class=\"uk-alert uk-alert-success\">Selected Privliges Granted! </div>");
			}
			//END GRANTING PRIVELEGES
			
			//REVOKE PRIVELEGES HERE
			String revoke = request.getParameter("revoke");
			if(revoke != null && !revoke.isEmpty())
			{
				String dbname = request.getParameter("dbname");
				String selectCheck = request.getParameter("select");
				String insertCheck = request.getParameter("insert");
				String updateCheck = request.getParameter("update");
				String deleteCheck = request.getParameter("delete");
				String createCheck = request.getParameter("create");
				String dropCheck = request.getParameter("drop");
				String columnCheck = request.getParameter("column");
				
				ArrayList<String> options = new ArrayList<String>();
				if(selectCheck != null) options.add(selectCheck);
				if(insertCheck != null) options.add(insertCheck);
				if(updateCheck != null) options.add(updateCheck);
				if(deleteCheck != null) options.add(deleteCheck);
				if(createCheck != null) options.add(createCheck);
				if(dropCheck != null) options.add(dropCheck);
				String delim = ", ";
				StringBuilder sb = new StringBuilder();
				for (String s: options)
				{
					sb.append(s).append(delim);
				}
				String perm = sb.substring(0, sb.length()-2);
				System.out.println(perm + " " + columnCheck);
				
				Statement statement = dbcon.createStatement();
				Statement flushpriv = dbcon.createStatement();
				
				if (columnCheck != null)
				{
					statement.executeUpdate("REVOKE " + perm + " ON " 
						+ dbname + "." + columnCheck + " FROM '" + username + "'@'" + host + "';");
				}
				flushpriv.executeUpdate("FLUSH PRIVILEGES;");
				out.println("<div class=\"uk-alert uk-alert-success\">Selected Privileges Revoked!</div>");
			}
			//END REVOKING PRIVELEGES
			
			//GRANT STORED PROCEDURE PRIVELEGES HERE
			String grant_sp = request.getParameter("grant_sp");
			if(grant_sp != null && !grant_sp.isEmpty())
			{
				String dbname = request.getParameter("dbname");
				String createCheck = request.getParameter("create");
				String alterCheck = request.getParameter("alter");
				String executeCheck = request.getParameter("execute");
				String stored_procedure = request.getParameter("stored_procedure");
				
				ArrayList<String> options = new ArrayList<String>();
				if(alterCheck != null) options.add(alterCheck);
				if(executeCheck != null) options.add(executeCheck);
				String delim = ", ";
				StringBuilder sb = new StringBuilder();
				for (String s: options)
				{
					sb.append(s).append(delim);
				}
				String perm = sb.substring(0, sb.length()-2);
				
				Statement statement = dbcon.createStatement();
				Statement statement2 = dbcon.createStatement();
				Statement flushpriv = dbcon.createStatement();
				
				statement.executeUpdate("GRANT " + perm + " ON PROCEDURE " 
						+ dbname + "." + stored_procedure + " TO '" + username + "'@'" + host + "';");
				
				if(createCheck != null)
				{
					statement2.executeUpdate("GRANT CREATE ROUTINE ON " + dbname + ".* TO "
							+ "'" + username + "'@'" + host + "';");
				}
				
				flushpriv.executeUpdate("FLUSH PRIVILEGES;");
				out.println("<div class=\"uk-alert uk-alert-success\">Selected Privliges Granted! </div>");
			}
			//END STORED PROCEDURE GRANTING PRIV
			
			//REVOKE STORED PROCEDURE PRIVELEGES HERE
			String revoke_sp = request.getParameter("revoke_sp");
			if(revoke_sp != null && !revoke_sp.isEmpty())
			{
				String dbname = request.getParameter("dbname");
				String createCheck = request.getParameter("create");
				String alterCheck = request.getParameter("alter");
				String executeCheck = request.getParameter("execute");
				String stored_procedure = request.getParameter("stored_procedure");
				
				ArrayList<String> options = new ArrayList<String>();
				if(alterCheck != null) options.add(alterCheck);
				if(executeCheck != null) options.add(executeCheck);
				String delim = ", ";
				StringBuilder sb = new StringBuilder();
				for (String s: options)
				{
					sb.append(s).append(delim);
				}
				String perm = sb.substring(0, sb.length()-2);
				
				Statement statement = dbcon.createStatement();
				Statement statement2 = dbcon.createStatement();
				Statement flushpriv = dbcon.createStatement();
				
				statement.executeUpdate("REVOKE " + perm + " ON PROCEDURE " 
						+ dbname + "." + stored_procedure + " FROM '" + username + "'@'" + host + "';");
				
				if(createCheck != null)
				{
					statement2.executeUpdate("REVOKE CREATE ROUTINE ON " + dbname + ".* FROM "
							+ "'" + username + "'@'" + host + "';");
				}
				
				flushpriv.executeUpdate("FLUSH PRIVILEGES;");
				out.println("<div class=\"uk-alert uk-alert-success\">Selected Privileges Revoked!</div>");
			}
			//END REVOKING STORED PROCEDURE PRIV
			
			Statement statement = dbcon.createStatement();
			ResultSet rs = statement.executeQuery("SELECT User, Db "
					+ "FROM mysql.db WHERE User = '"+ username +"';");
			
			if (rs.first() == false)
			{
				out.println("User not found.");
			}
			else
			{
				out.println("<table class=\"uk-table uk-table-hover uk-table-striped\">");
				out.println("<thead>");
				out.println("<tr>");
				out.println("<th>User</th>");
				out.println("<th>Select</th>");
				out.println("<th>Insert</th>");				
				out.println("<th>Update</th>");
				out.println("<th>Delete</th>");
				out.println("<th>Create</th>");
				out.println("<th>Drop</th>");
				out.println("<th>Column</th>");
				out.println("</tr>");
				out.println("</thead>");
				out.println("<tbody>");
				Statement statement2 = dbcon.createStatement();
				ResultSet rs2 = statement2.executeQuery("SHOW TABLES;");
				do 
				{
					out.println("<form class=\"uk-form uk-margin\" action=\"editPriv\" method=\"GET\">");
					out.println("<input type=\"hidden\" name=\"host\" value=\""+ host +"\">");
					out.println("<input type=\"hidden\" name=\"username\" value=\""+ username +"\">");
					out.println("<input type=\"hidden\" name=\"dbname\" value=\""+ rs.getString(2) +"\">");
					out.println("<tr>");
					out.println("<td>"+ rs.getString(1) +"</td>");
					out.println("<td><input type=\"checkbox\" value=\"SELECT\" name=\"select\"></td>");
					out.println("<td><input type=\"checkbox\" value=\"INSERT\" name=\"insert\"></td>");
					out.println("<td><input type=\"checkbox\" value=\"UPDATE\" name=\"update\"></td>");
					out.println("<td><input type=\"checkbox\" value=\"DELETE\" name=\"delete\"></td>");
					out.println("<td><input type=\"checkbox\" value=\"CREATE\" name=\"create\"></td>");
					out.println("<td><input type=\"checkbox\" value=\"DROP\" name=\"drop\"></td>");
					out.println("<td>");
					out.println("<div class=\"uk-form-controls\">");
					out.println("    <select id=\"column\" name=\"column\">");
					if (rs2.first()) {
						out.println("<option value=\"*\">*</option>");
						do {
							out.println("<option value=\"" + rs2.getString(1) + "\">"+ rs2.getString(1) +"</option>");
						} while (rs2.next());
					}
					out.println("   </select>");
					out.println("</div></td>");
					out.println("<td><button type=\"submit\" name=\"grant\" value=\"grant\" class=\"uk-button-small uk-button-success\">Grant</button></td>");
					out.println("<td><button type=\"submit\" name=\"revoke\" value=\"revoke\" class=\"uk-button-small uk-button-danger\">Revoke</button></td>");
					out.println("</tr>");
					out.println("</form>");
				} while(rs.next());
				out.println("</tbody>");
				out.println("</table>");
				//stored procedure privileges
				out.println("<h3>Stored Procedures</h3>");
				out.println("<table class=\"uk-table uk-table-hover uk-table-striped\">");
				out.println("<thead>");
				out.println("<tr>");
				out.println("<th>Procedure</th>");
				out.println("<th>Create</th>");
				out.println("<th>Alter</th>");
				out.println("<th>Execute</th>");
				out.println("</tr>");
				out.println("</thead>");
				out.println("<tbody>");
				Statement statement3 = dbcon.createStatement();
				ResultSet rs3 = statement3.executeQuery("SELECT name FROM mysql.proc;");
				if (rs3.first())
				{
					do 
					{
						out.println("<form class=\"uk-form uk-margin\" action=\"editPriv\" method=\"GET\">");
						out.println("<input type=\"hidden\" name=\"host\" value=\""+ host +"\">");
						out.println("<input type=\"hidden\" name=\"username\" value=\""+ username +"\">");
						out.println("<input type=\"hidden\" name=\"dbname\" value=\"moviedb\">");
						out.println("<input type=\"hidden\" name=\"stored_procedure\" value=\""+ rs3.getString(1) +"\">");
						out.println("<tr>");
						out.println("<td>"+ rs3.getString(1) +"</td>");
						out.println("<td><input type=\"checkbox\" value=\"CREATE\" name=\"create\"></td>");
						out.println("<td><input type=\"checkbox\" value=\"ALTER ROUTINE\" name=\"alter\"></td>");
						out.println("<td><input type=\"checkbox\" value=\"EXECUTE\" name=\"execute\"></td>");
						out.println("<td><button type=\"submit\" name=\"grant_sp\" value=\"grant\" class=\"uk-button-small uk-button-success\">Grant</button></td>");
						out.println("<td><button type=\"submit\" name=\"revoke_sp\" value=\"revoke\" class=\"uk-button-small uk-button-danger\">Revoke</button></td>");
						out.println("</tr>");
						out.println("</form>");
					} while(rs3.next());
					out.println("</tbody>");
					out.println("</table>");
				}
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