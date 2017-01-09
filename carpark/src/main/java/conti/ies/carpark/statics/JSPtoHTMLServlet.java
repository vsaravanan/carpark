package conti.ies.carpark.statics;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JSPtoHTMLServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		// get the name of the requested resource
		StringBuffer sb = req.getRequestURL();
		String requestedPage = sb.toString();

		StringTokenizer st = new StringTokenizer(requestedPage, ".");
		// hardcoded because we know the 1st is file name:
		String name = st.nextToken();

		RequestDispatcher disp = req.getRequestDispatcher(name + ".jsp");
		disp.forward(req, res);

	}
}
