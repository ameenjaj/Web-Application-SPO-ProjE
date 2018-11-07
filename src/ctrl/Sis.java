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
 * Servlet implementation class Sis
 */
@WebServlet("/Sis.do")
public class Sis extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	    String calc = request.getParameter("calc");
		
		if (calc != null) {
			String prefix = request.getParameter("prefix");
			String minGpa = request.getParameter("minGpa");
			String sortBy = request.getParameter("sortBy");
			
			Writer out = response.getWriter();
			response.setContentType("text/json");
			

			try
			{
				Engine engine = Engine.getInstance();
				out.write("{\"status\": 1, \"result\": " +  engine.getSIS(prefix, sortBy, minGpa) + "}");
			} 
			catch (Exception e)
			{
				out.write("{\"status\": 0, \"error\": \"" + e.getMessage() + "\"}");
			}
			
			
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
