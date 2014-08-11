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

public class Logout extends HttpServlet {
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

		out.println("<HTML><HEAD><TITLE>Error 404</TITLE></HEAD>");
		out.println("<a href=\"http://localhost:8080/patflix/index.html\">Go Back</a>");
		out.println("<BODY><H1>Incorrect User or Password</H1>");

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
			
			// Declare our statement
			Statement statement = dbcon.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM cart;");

			request.getParameter("email");
			request.getParameter("password");

			HttpSession session = request.getSession();
			@SuppressWarnings("unchecked")
			ArrayList<ShoppingCart> cart = (ArrayList<ShoppingCart>) session
					.getAttribute("cart");

			Statement stmt2 = dbcon.createStatement();
			stmt2.executeUpdate("DELETE FROM cart WHERE customer_id = '"
					+ session.getAttribute("customerID") + "';");

			for (int i = 0; i < cart.size(); i++) {
				Statement stmt = dbcon.createStatement();
				stmt.executeUpdate("INSERT INTO cart VALUES("
						+ session.getAttribute("customerID") + ", "
						+ cart.get(i).getMovieID() + ", '"
						+ cart.get(i).getTitle() + "', "
						+ cart.get(i).getQuantity() + ");");
			}

			request.getSession(false);
			response.sendRedirect("index.html");

			rs.close();
			statement.close();
			dbcon.close();
		} catch (SQLException ex) {
			response.sendRedirect("index.html");
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