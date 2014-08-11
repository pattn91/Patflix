/* A servlet to display the contents of the MySQL movieDB database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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

public class Checkout extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getServletInfo() {
		return "Checkout";
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
		out.println("            <div class=\"uk-grid\" data-uk-grid-margin>");
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

			// Declare our statement

			HttpSession session = request.getSession();
			@SuppressWarnings("unchecked")
			ArrayList<ShoppingCart> cart = (ArrayList<ShoppingCart>) session
					.getAttribute("cart");

			System.out.println(cart.size());
			for (int i = cart.size() - 1; i >= 0; i--) {
				Statement stmt2 = dbcon.createStatement();
				stmt2.executeUpdate("INSERT INTO sales (customer_id, movie_id, sale_date) VALUE ("
						+ session.getAttribute("customerID")
						+ ", "
						+ cart.get(i).getMovieID() + ", CURDATE());");

				out.println("Movie Title: " + cart.get(i).getTitle());
				out.println("<BR>");
				out.println("Quantity: " + cart.get(i).getQuantity());
				out.println("<BR>");
				out.println("<BR>");

				cart.remove(i);
			}

			out.println("<h4 class=\"tm-article-subtitle\">Insert Information</h4>");
			out.println("");
			out.println("                            <form class=\"uk-form uk-form-horizontal\" action=\"CheckoutConfirm\" method=\"POST\">");
			out.println("");
			out.println("                                <div class=\"uk-form-row\">");
			out.println("                                    <label class=\"uk-form-label\" for=\"m_id\">First Name</label>");
			out.println("                                    <div class=\"uk-form-controls\">");
			out.println("                                        <input type=\"text\" name=\"f_name\" placeholder=\"Patrick\" required>");
			out.println("                                    </div>");
			out.println("                                </div>");
			out.println("                                <div class=\"uk-form-row\">");
			out.println("                                    <label class=\"uk-form-label\" for=\"title\">Last Name</label>");
			out.println("                                    <div class=\"uk-form-controls\">");
			out.println("                                        <input type=\"text\"  name=\"l_name\" placeholder=\"Nguyen\" required>");
			out.println("                                    </div>");
			out.println("                                </div>");
			out.println("                                <div class=\"uk-form-row\">");
			out.println("                                    <label class=\"uk-form-label\" for=\"year\">Creditcard No.</label>");
			out.println("                                    <div class=\"uk-form-controls\">");
			out.println("                                        <input type=\"text\"  name=\"creditcard\" placeholder=\"0000000000000000\" required>");
			out.println("                                    </div>");
			out.println("                                </div>");
			out.println("                                <div class=\"uk-form-row\">");
			out.println("                                    <label class=\"uk-form-label\" for=\"director\">Expiration Date:</label>");
			out.println("                                    <div class=\"uk-form-controls\">");
			out.println("                                        <input type=\"text\"  name=\"exp\" placeholder=\"YYYY-MM-DD\" required>");
			out.println("                                    </div>");
			out.println("                                </div>");
			out.println("            <button type=\"submit\" class=\"uk-button uk-button-danger\">Confirm Checkout</button>");
			out.println("                            </form>");

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
		//close divs from panel
		out.println("    </div>");
		out.println("  </div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doGet(request, response);
	}
}