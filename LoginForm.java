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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

public class LoginForm extends HttpServlet {
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

		out.println("<!DOCTYPE html>");
		out.println("<html lang=\"en-gb\" dir=\"ltr\">");
		out.println("<head>");
		out.println("<title></title>");
		out.println("<link rel=\"stylesheet\" href=\"css/uikit.gradient.css\" />");
		out.println("<script");
		out.println("src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"");
			out.println("type=\"text/javascript\"></script>");
		out.println("<script src=\"js/uikit.min.js\" type=\"text/javascript\"></script>");
		out.println("</head>");
		out.println("<style type=\"text/css\">");
		out.println("#container{");
		out.println("position: absolute;");
		out.println("top: 50%;");
		out.println("margin-top: -90px;/* half of #content height*/");
		out.println("left: 0;");
		out.println("width: 100%;");
		out.println("}");
		out.println("#content {");
		out.println("width: 624px;");
		out.println("margin-left: auto;");
		out.println("margin-right: auto;");
		out.println("height: 250px;");
		out.println("}");
		out.println("</style>");

		out.println("<body>");
		out.println("<div id=\"container\">");
		out.println("<div id=\"content\">");
		out.println("<div class=\"uk-panel uk-panel-box uk-width-1-2 uk-container-center uk-text-center\">");
		out.println("<h3 class=\"uk-panel-title\">Patflix</h3>");
		out.println("<div class=\"uk-alert uk-alert-danger\" data-uk-alert>");
		out.println("<a href=\"\" class=\"uk-alert-close uk-close\"></a>");
		out.println("<p>Incorrect username or password.</p>");
		out.println("</div>");

		out.println("<form class=\"uk-form\" data-uk-margin action=\"LoginForm\" method =\"POST\">");
		out.println("<div class=\"uk-form-icon\">");
		out.println("<i class=\"uk-icon-user\"></i> <input type=\"text\" name=\"email\">");
		out.println("</div>");
		out.println("<BR>");
		out.println("<div class=\"uk-form-icon\">");
		out.println("<i class=\"uk-icon-key\"></i> <input type=\"password\" name=\"password\">");
		out.println("</div>");
		out.println("<BR>");
		out.println("<input class=\"uk-button\" type=\"submit\" value=\"Login\">");
		out.println("</form>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");

		try {
			// Class.forName("org.gjt.mm.mysql.Driver");
			//Class.forName("com.mysql.jdbc.Driver").newInstance();

			//Connection dbcon = DriverManager.getConnection(loginUrl, loginUser,
				//	loginPasswd);
			
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
						
			// Declare our statement
			Statement statement = dbcon.createStatement();

			String email = request.getParameter("email");
			String pass = request.getParameter("password");
			String query = "SELECT * FROM customers WHERE email = '" + email
					+ "' AND" + " password = '" + pass + "'";

			// Perform the query
			ResultSet rs = statement.executeQuery(query);

			// Iterate through each row of rs
			if (rs.first()) {
				HttpSession session = request.getSession(true);

				// Search for a shopping cart with user's email
				Statement stmt = dbcon.createStatement();
				ResultSet rs2 = stmt
						.executeQuery("SELECT id FROM customers WHERE email = '"
								+ email + "';");

				Statement stmt2 = dbcon.createStatement();
				ResultSet rs3 = null;

				ArrayList<ShoppingCart> cart = new ArrayList<ShoppingCart>();

				if (rs2.first() == false) {
					out.println("No customer id found.");
				} else {
					rs3 = stmt2
							.executeQuery("SELECT * FROM cart WHERE customer_id = "
									+ rs2.getInt(1) + ";");
					if (rs3.first() == false) {
						session.setAttribute("cart", cart);
						session.setAttribute("customerID", rs2.getInt(1));
					} else {
						do {
							cart.add(new ShoppingCart(rs3.getInt(2), rs3
									.getString(3), rs3.getInt(4)));

						} while (rs3.next());

						session.setAttribute("cart", cart);
						session.setAttribute("customerID", rs2.getInt(1));
					}
				}
				response.sendRedirect("home.html");
			} else {
				//out.println("Incorrect Login");
			}

			rs.close();
			statement.close();
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