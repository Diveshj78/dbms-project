

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;
/**
 * Servlet implementation class Main3
 */
@WebServlet("/Main3")
public class Main3 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Main3() {
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
		
		ResultSet rs=null, check=null;
		Statement stmt =null, stmtcheck=null;
		Connection conn=null, con=null;
		int count=0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/njit?user=root&password=root");
	        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	        
	        con= DriverManager.getConnection("jdbc:mysql://localhost/njit?user=root&password=root");
	        stmtcheck= con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	        
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
		String docid=null, title=null, pubname=null;
		int query1=0, query2=0;
		try { 
			radio = request.getParameter("r2"); 
				    	} catch (Exception e) { 
				    		out.println(e);
				    	}
		
		if(radio.equals("search"))
		{
			docid=request.getParameter("docid");
			title=request.getParameter("title");
			pubname=request.getParameter("pubname");
			
			
			try {
				rs=stmt.executeQuery("Select * from DOCUMENT D Natural Join Publisher P WHERE D.DOCID= \'"+docid+"\' or D.title = \'"+title+"\' or P.pubname=\'"+pubname+"\'");
				
				if(rs.first())
				{
					rs.beforeFirst();
				
					out.println("<label for=\"document\">Documents : </label>"
							+"<table>"
							+"<tr>"
							+"<th>Document Id</th>"
							+"<th>Title</th>"
							+"<th>Publisher Name</th>"
							+ "</tr>");
					while(rs.next())
				{
					out.println("<tr>"
							+"<td>"
							+rs.getString("DOCID")
							+"</td>"
							+"<td>"
							+rs.getString("TITLE")
							+"</td>"
							+"<td>"
							+rs.getString("PUBNAME")
							+"</td>"
							+"</tr>"
							);
				}
				}
				else {
					out.println("No data found. Please try different book.");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if (radio.equals("reserve"))
				{
			try {
				rs=stmt.executeQuery("Select * from COPY C NATURAL JOIN DOCUMENT D NATURAL JOIN BRANCH BR NATURAL JOIN PUBLISHER where not exists (select * from BORROWING BG NATURAL JOIN BORROWS B where C.DOCID=B.DOCID and C.COPYNO=B.COPYNO AND C.BID=B.BID AND BG.RDATE IS NULL) and NOT EXISTS (SELECT * FROM RESERVES R WHERE C.DOCID=R.DOCID AND C.COPYNO=R.COPYNO AND C.BID=R.BID )order by C.BID");
				
				if(rs.first())
				{
					rs.beforeFirst();
				
					out.println("<label for=\"document\">Documents Available For Reservation : </label>"
							+"<form method=\"post\" action=\"Main4\">"
							+"<table>"
							+"<tr>"
							+"<th>Select</th>"
							+"<th>Document Id</th>"
							+"<th>Title</th>"
							+"<th>Publisher Name</th>"
							+"<th>Branch Name</th>");
					while(rs.next())
				{
						String tempdocid = rs.getString("DOCID");
						String finalid = tempdocid+" "+rs.getString("COPYNO")+" "+rs.getString("BID");
						
					out.println("<tr>"
							+"<td>"
							+"<input type=\"checkbox\" name=\"selectedForReserve\" id=\"selectedForReserve\" value=\'"+finalid+"\' >"
							+"</td>"
							+"<td>"
							+tempdocid
							+"</td>"
							+"<td>"
							+rs.getString("TITLE")
							+"</td>"
							+"<td>"
							+rs.getString("PUBNAME")
							+"</td>"
							+"<td>"
							+rs.getString("LNAME")
							+"</td>"
							+"</tr>"
							);
				}
					out.println("</table>"
							+"<input type=\"submit\" value=\'Reserve\'>"
							+"</form>");
				}
				else {
					out.println("No books available for reservation");
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				}else if(radio.equals("checkout"))
				{
					try {
						rs=stmt.executeQuery("SELECT * FROM RESERVES WHERE RID =\'"+Main.readerid+"\'");
						
						if(rs.first())
						{
							rs=stmt.executeQuery("SELECT * FROM BORROWING");
							while(rs.next())
							{
							if(rs.isLast())
							{
								String temp=rs.getString("BOR_NO");
								count =Integer.parseInt(temp);
							}
							}
							count+=1;
							
							query1=stmt.executeUpdate("INSERT INTO BORROWING VALUES (\'"+count+"\', CURRENT_TIMESTAMP, NULL)");
							rs=stmt.executeQuery("SELECT * FROM RESERVES WHERE RID =\'"+Main.readerid+"\'");
							
							while(rs.next())
							{
							query2=stmtcheck.executeUpdate("INSERT INTO BORROWS VALUES (\'"+count+"\',\'"+rs.getString("DOCID")+"\', \'"+rs.getString("COPYNO")+"\', \'"+rs.getString("BID")+"\', \'"+Main.readerid+"\') ");	
							}
							if(query2>0)
							{
								out.println("<h2>Books borrowed successfully.</h2>");
								out.println("<h3> Borrow Transaction ID :"+count+"</h3>");
									rs=stmt.executeQuery("SELECT * FROM BORROWS NATURAL JOIN DOCUMENT WHERE BOR_NO=\'"+count+"\'");
									out.println("<h3>Borrowed book list : </h3>");
									
									while(rs.next())
									{
										out.println(rs.getString("TITLE")+"<br>");
									}
									
									int del=stmt.executeUpdate("delete from Reserves where RID ="+Main.readerid);
							}
							else {
								out.println("<h3>Book borrowing failed.</h3>");
							}
						}
						else
						{
							out.println("<h3>No books reserved. Please reserve books before checkout.</h3>");
						}
						
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else if(radio.equals("computefine"))
				{
					try {
						rs=stmt.executeQuery("SELECT (DATEDIFF(CURRENT_TIMESTAMP,BDATE)-20)*0.20*COUNT(COPYNO) FROM BORROWS B NATURAL JOIN BORROWING BRG WHERE BRG.RDATE IS NULL AND B.RID="+Main.readerid);
						
						out.println("The current fine for Reader id :"+Main.readerid+" is $");
						while(rs.next())
						{
							if(Double.parseDouble(rs.getString(1))>0)
							{
								out.print(rs.getString(1));
							}
							else
							{
								out.println("0");
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(radio.equals("return"))
				{
					String branchid=request.getParameter("branchid");
					
					try {
						rs=stmt.executeQuery("SELECT * FROM BORROWING BRG NATURAL JOIN BORROWS WHERE RDATE IS NULL AND BID=\'"+branchid+"\' AND RID ="+Main.readerid);
					
						while(rs.next())
						{
					query1=stmtcheck.executeUpdate("UPDATE BORROWING SET RDATE = CURRENT_TIMESTAMP WHERE BOR_NO =\'"+rs.getString("BOR_NO")+"\'");
						}
						
						if(query1>0)
						{
							out.println("Books returned successfully.");
						}
						else
						{
							out.println("Issue while returning. Please contact admin.");
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(radio.equals("listdoc"))
				{
					String datecount="0";
					try {
						rs=stmt.executeQuery("SELECT DOCID,COPYNO, BID, DATEDIFF(CURRENT_TIMESTAMP,DTIME) AS DATECHECK FROM RESERVATION LEFT JOIN RESERVES ON RES_NO=RESERVATION_NO WHERE RID ="+Main.readerid);
						
						if(rs.first())
						{
							rs.beforeFirst();
							
							out.println("Reserved book details :"
									+"<table>"
									+ "<tr>"
									+ "<th>Document ID</th>"
									+ "<th>Copy No</th>"
									+ "<th>Branch ID</th>"
									+ "<th>Status</th>"
									+ "</tr>"
									);
						while(rs.next())
						{
							out.println("<tr>"
									+ "<td>"
									+rs.getString("DOCID")
									+ "</td>"
									+ "<td>"
									+rs.getString("COPYNO")
									+"</td>"
									+"<td>"
									+rs.getString("BID")
									+"</td>"
									+"<td>"
									);
							
							if(rs.getString("DATECHECK").equals(datecount) )
							{
								out.println("Active");
							}
							else
							{
								out.println("Closed");
							}
							out.println("</td>"
									+"</tr>");
						}
						out.println("</table>");
						}
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(radio.equals("searchdocbypub"))
				{
					pubname=request.getParameter("pname");
					
					try {
						rs=stmt.executeQuery("Select * from DOCUMENT D Natural Join Publisher P WHERE P.pubname=\'"+pubname+"\'");
						
						if(rs.first())
						{
							rs.beforeFirst();
						
							out.println("<label for=\"document\">Documents : </label>"
									+"<table>"
									+"<tr>"
									+"<th>Document Id</th>"
									+"<th>Title</th>"
									+"<th>Publisher Name</th>"
									+ "</tr>");
							while(rs.next())
						{
							out.println("<tr>"
									+"<td>"
									+rs.getString("DOCID")
									+"</td>"
									+"<td>"
									+rs.getString("TITLE")
									+"</td>"
									+"<td>"
									+rs.getString("PUBNAME")
									+"</td>"
									+"</tr>"
									);
						}
						}
						else {
							out.println("No data found. Please try different book.");
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
		
		}
	    
}