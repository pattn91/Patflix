/* A servlet to display the contents of the MySQL movieDB database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;


public class AddToCart extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getServletInfo() {
		return "Add to Cart";
	}

	// Use http GET

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("text/html"); // Response mime type

		// Output stream to STDOUT
		PrintWriter out = response.getWriter();

		out.println("<HTML><HEAD><TITLE>Cart</TITLE></HEAD>");
		out.println("<script>" + "function goBack()" + "{"
				+ "window.history.back()" + "}" + "</script>"
				+ "<button onclick=\"goBack()\">Go Back</button>");
		out.println("<BODY><H1>Your Cart</H1>");

		try {
			// Class.forName("org.gjt.mm.mysql.Driver");
			// Class.forName("com.mysql.jdbc.Driver").newInstance();

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

			HttpSession session = request.getSession();
			@SuppressWarnings("unchecked")
			ArrayList<ShoppingCart> cart = (ArrayList<ShoppingCart>) session
					.getAttribute("cart");

			String movie = request.getParameter("movie");
			int movieID = Integer.parseInt(movie);
			boolean cartFlag = true;
			// Declare our statement
			Statement statement = dbcon.createStatement();
			ResultSet movieResult = statement
					.executeQuery("SELECT * FROM movies WHERE id = " + movieID
							+ ";");
			movieResult.first();
			for (int i = 0; i < cart.size(); i++) {
				if (cart.get(i).getMovieID() == movieID) {
					cart.get(i).incrementQuantity();
					cartFlag = false;
				}
			}
			if (cartFlag) {
				cart.add(new ShoppingCart(movieResult.getInt(1), movieResult
						.getString(2), 1));
			}
			session.setAttribute("cart", cart);
			out.println("<script>" + "function goBack()" + "{"
					+ "window.history.back()" + "}" + "</script>"
					+ "<body onload=\"goBack()\">");
			movieResult.close();
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