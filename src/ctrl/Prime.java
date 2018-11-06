package ctrl;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;

/**
 * Servlet implementation class Prime
 */
@WebServlet("/prime.do")
public class Prime extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		Engine engine = Engine.getInstance();
		
		String calc = request.getParameter("calc");
		
		if (calc != null) {
			String previous = request.getParameter("previous");
			String min = (!previous.equals(""))? previous : request.getParameter("min");
			String max = request.getParameter("max");
			
			Writer out = response.getWriter();
			response.setContentType("text/json");

			try
			{
				out.write("{\"status\": 1, \"result\": " +  engine.nextPrime(min, max) + "}");
			} 
			catch (Exception e)
			{
				out.write("{\"status\": 0, \"error\": \"" + e.getMessage() + "\"}");
			}
			
			
		} 
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
