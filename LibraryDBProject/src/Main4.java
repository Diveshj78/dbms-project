

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Main4
 */
@WebServlet("/Main4")
public class Main4 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Main4() {
        super();
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
		
		ResultSet rs=null;
		Statement stmt =null;
		Connection conn=null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/njit?user=root&password=root");
	        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
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
				+"<input type=\"submit\" value=\"Back\" formaction=\"Page1.html\">"
				+"</form>");
		
		String radio=null;
		String docid=null, title=null, pubname=null, copyno=null, bid=null;
		int count=0,update1=0,update2=0, arraycount=0;
		String[] reservedDocId = request.getParameterValues("selectedForReserve");
		
		for (String temp : reservedDocId) {
			arraycount+=1;
		}
		
		out.print("Count of books selected : "+arraycount+"<br>");
		Pattern p = Pattern.compile("[0-9]+");
		
		try {
        if(!(reservedDocId[0].isBlank()))
        {
        	int tablecount=0;
				rs=stmt.executeQuery("SELECT COUNT(DOCID) FROM RESERVES WHERE RID =\'"+Main.readerid+"\' ");
				if(rs.next())
				tablecount+=Integer.parseInt(rs.getString(1));
				
				rs=stmt.executeQuery("SELECT COUNT(DOCID) FROM BORROWS NATURAL JOIN BORROWING WHERE RID = \'"+Main.readerid+"\' AND RDATE IS NULL");
				if(rs.next())
					tablecount+=Integer.parseInt(rs.getString(1));
				
				out.print("Reserved/borrowed book count:"+tablecount+"<br>");
        	if(tablecount+arraycount <= 10)
			{			

			rs = stmt.executeQuery("Select * from reservation");
			
			while(rs.next())
			{
			if(rs.isLast())
			{
				String temp=rs.getString("RES_NO");
				count =Integer.parseInt(temp);
			}
			}
			count+=1;
			update1=stmt.executeUpdate("INSERT INTO RESERVATION VALUES (\'"+count+"\', CURRENT_TIMESTAMP)");

			
		for (String temp : reservedDocId) {
			Matcher m = p.matcher(temp);
			if(m.find())
			{
				docid=m.group();

			}
			if(m.find())
			{
				copyno=m.group();

			}	
			if(m.find())
			{
				bid=m.group();

			}	
			
					update2=stmt.executeUpdate("INSERT INTO RESERVES values (\'"+count+"\',\'"+docid+"\', \'"+copyno+"\', \'"+bid+"\',\'"+Main.readerid+"\')");
		
		}
		if(update2>0)
		{
			out.println("<h3>Books reserved successfully.</h3>");
				rs=stmt.executeQuery("SELECT * FROM RESERVES NATURAL JOIN DOCUMENT WHERE RESERVATION_NO=\'"+count+"\'");
				out.println("<h3>Books Reserved : </h3>");
				while(rs.next())
				{
					out.println(rs.getString("TITLE")
							+"<br>");
				}
				
			
		}
        }else 
        {
        	out.println("<h3>More than 10 documents are getting reserved. Can not perform reservation.</h3>");
        }
        	} else
        {
        	out.println("<h3>Please select books for reserving</h3>");
        }
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
