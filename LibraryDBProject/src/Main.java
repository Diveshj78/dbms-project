

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Main
 */
@WebServlet("/Main")
public class Main extends HttpServlet {
	
	public static String readerid;
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Main() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>"
				+"<head>"
				+"<meta charset=\"ISO-8859-1\">\r\n"
				+ "<title>City Library</title>\r\n"
				+ "<meta http-equiv=\"Cache-Control\" content=\"no-cache, no-store, must-revalidate\" />\r\n"
				+ "<meta http-equiv=\"Pragma\" content=\"no-cache\" />\r\n"
				+ "<meta http-equiv=\"Expires\" content=\"0\" />"
				+"<link rel=\"stylesheet\" href=\"NewFile.css\">"
				+"</head>"
				+"<body>"
				+"<header>"
				+"<h1>Welcome to City Library</h1>"
				+"</header>"
				+"<div>"
				+"<form method=\"post\">"
				+"<input type=\"submit\" value=\"Home\" formaction=\"Main.html\">"
				+"</form>");
		
		int radio=0;
		try { 
			radio = Integer.parseInt(request.getParameter("r1")); 
				    	} catch (Exception e) { 
				    		out.println(e);
				    	}
		
		if(radio== 1)
			{
			readerid = request.getParameter("readerid");
		ResultSet rs=null;
		
	    try {
	    	 Class.forName("com.mysql.cj.jdbc.Driver");
	         Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/njit?user=root&password=root");
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		 try{ 
			rs=stmt.executeQuery("Select * from READER where RID =\'"+readerid+"\'"); //Check reader id
		}catch(Exception e){
			out.println(e);
		}}catch(Exception ex) {
			out.println(ex);
		}
	    try {
			if(rs.next())
				response.sendRedirect(request.getContextPath() + "/Page1.html");
			else
				out.println("<h3>Invalid Reader ID. Please enter valid data.</h3>");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			}
		else if(radio== 2)
		{
			String adminId = request.getParameter("adminId");
			String password = request.getParameter("password");
			
			ResultSet rs=null;
			
		    try {
		    	 Class.forName("com.mysql.cj.jdbc.Driver");
		         Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/njit?user=root&password=root");
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			 try{ 
				rs=stmt.executeQuery("Select * from ADMIN where ADMINID =\'"+adminId+"\' and PASSWORD =\'"+password+"\'"); //Check admin id & password
			}catch(Exception e){
				out.println(e);
			}
			 try {
					if(rs.next())
						response.sendRedirect(request.getContextPath() + "/Page2.html");
					else
						out.println("Invalid Admin ID &/or Password. Please enter valid data.");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}catch(Exception ex) {
				out.println(ex);
			}
	}

		out.println(
				"</div>"
				+"</body>"
				+"</html>");
}}