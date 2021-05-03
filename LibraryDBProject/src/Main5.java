

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
/**
 * Servlet implementation class Main3
 */
@WebServlet("/Main5")
public class Main5 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Main5() {
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
				+"<input type=\"submit\" value=\"Back\" formaction=\"Page2.html\">"
				+"</form>");
		
		String radio=null;
		String num=null, branchnum=null;
		String date1=null, date2=null;
		int query1=0, query2=0;
		try { 
			radio = request.getParameter("r2"); 
				    	} catch (Exception e) { 
				    		out.println(e);
				    	}
		
		
		if(radio.equals("adddoc"))
		{
			String docid=request.getParameter("docid");
			String copy=request.getParameter("copy");
			String bid=request.getParameter("branchid");
			String position=request.getParameter("position");
			
			try {
				query1=stmt.executeUpdate("Insert into COPY values(\'"+docid+"\', \'"+copy+"\', \'"+bid+"\', \'"+position+"\')");
				
				if(query1>0)
				{
					out.println("<h3>Record updated successfully.</h3>");
				}
				else
				{
					out.println("<h3>Record not updated. Please check the data.</h3>");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				out.println("<h3>Record not updated. Please check the data.</h3>");
			}
		}
		else if(radio.equals("status"))
		{
			String did=request.getParameter("did");
			
			try {
				rs=stmt.executeQuery("select * from copy LEFT join reserves on copy.DOCID=reserves.DOCID and copy.COPYNO=reserves.COPYNO and copy.BID=reserves.BID where copy.DOCID ="+did);
				
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
					if(rs.getString("Reservation_No") == null)
					{
						out.println("Not reserved");
					}
					else {
						out.println("Reserved");
					}
						out.println("</td>"
								+"</tr>");
				}
				out.println("</table>");
				
rs=stmt.executeQuery("select * from copy LEFT join borrows on copy.DOCID=borrows.DOCID and copy.COPYNO=borrows.COPYNO and copy.BID=borrows.BID where copy.DOCID ="+did);
				
				out.println("Borrowed book details :"
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
					if(rs.getString("BOR_No") == null)
					{
						out.println("Not borrowed");
					}
					else {
						out.println("Borrowed");
					}
						out.println("</td>"
								+"</tr>"
								);
				}
				out.println("</table>");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(radio.equals("addreader"))
		{
			String rname=request.getParameter("rname");
			String rtype=request.getParameter("rtype");
			String radd=request.getParameter("radd");
			String rcon=request.getParameter("rcon");
			
			try {
				rs = stmt.executeQuery("Select * from reader");
				
				while(rs.next())
				{
				if(rs.isLast())
				{
					String temp=rs.getString("RID");
					count =Integer.parseInt(temp);
				}
				}
				count+=1;
				
				query1=stmt.executeUpdate("INSERT INTO READER VALUES (\'"+count+"\', \'"+rtype+"\', \'"+rname+"\', \'"+radd+"\', \'"+rcon+"\')");
				
				if(query1>0)
				{
					out.println("<h3>Reader added successfully.</h3>");
					rs=stmt.executeQuery("SELECT * FROM READER WHERE RID ="+count);
					
					out.println("New Reader details :"
							+"<table>"
							+ "<tr>"
							+ "<th>Reader ID</th>"
							+ "<th>Reader Type</th>"
							+ "<th>Reader Name</th>"
							+ "<th>Reader Address</th>"
							+ "<th>Phone No</th>"
							+ "</tr>"
							);
					while(rs.next())
					{
						out.println(
								"<tr>"
								+"<td>"
								+rs.getString(1)
								+ "</td>"
								+"<td>"
								+rs.getString(2)
								+ "</td>"
								+"<td>"
								+rs.getString(3)
								+"</td>"
								+"<td>"
								+rs.getString(4)
								+"</td>"
								+"<td>"
								+rs.getString(5)
								+"</td>"
								+"</tr>"
								);
					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(radio.equals("printbranch"))
		{
			branchnum=request.getParameter("bid");
			
			try {
				rs=stmt.executeQuery("SELECT * FROM BRANCH WHERE BID="+branchnum);
				
				out.println("<h3>Branch Details :</h3>"
						+ "<table>"
						+ "<tr>"
						+ "<th>Branch Name</th>"
						+"<th>Location</th>"
						+"</tr>"
						);
				
				while(rs.next())
				{
				out.println("<tr>"
						+ "<td>"
						+rs.getString(2)
						+ "</td>"
						+ "<td>"
						+rs.getString(3)
						+ "</td>"
						+ "</tr>");	
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		else if(radio.equals("mfbb"))
		{
			num=request.getParameter("num");
			branchnum=request.getParameter("branchnum");
		
			try {
				rs=stmt.executeQuery("Select r.rid, r.rname, count(CopyNo) as Number_Of_Books from borrows b Natural JOIN reader r where b.bid = \'"+branchnum+"\' Group By R.RID Order by count(CopyNo) desc limit "+num+" ");
			
				out.println("<table>"
						+"<tr>"
						+"<th>Reader ID</th>"
						+"<th>Reader Name</th>"
						+"<th>Number of Books</th>"
						+"</tr>"
						);
				while(rs.next())
				{
					out.println("<tr>"
							+"<td>"
							+rs.getString(1)
							+"</td>"
							+"<td>"
							+rs.getString(2)
							+"</td>"
							+"<td>"
							+rs.getString(3)
							+"</td>"
							+"</tr>");
				}
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (radio.equals("mfbl"))
				{
			num=request.getParameter("num2");
			
				try {
					rs=stmt.executeQuery("Select r.rid, r.rname, count(CopyNo) as Number_Of_Books from borrows b Natural JOIN reader r Group By R.RID Order by count(CopyNo) desc limit "+num+" ");

				out.println("<table>"
						+"<tr>"
						+"<th>Reader ID</th>"
						+"<th>Reader Name</th>"
						+"<th>Number of Books</th>"
						+"</tr>"
						);
				while(rs.next())
				{
					out.println("<tr>"
							+"<td>"
							+rs.getString(1)
							+"</td>"
							+"<td>"
							+rs.getString(2)
							+"</td>"
							+"<td>"
							+rs.getString(3)
							+"</td>"
							+"</tr>");
				}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				} else if(radio.equals("mbb"))
				{
					num=request.getParameter("num");
					branchnum=request.getParameter("branchnum");
				
					try {
						rs=stmt.executeQuery("select b.DocId, title from borrows b Natural JOIN document d where b.bid = \'"+branchnum+"\'  group by docid Order by (select count(CopyNo) from borrows where bid =\'"+branchnum+"\') desc limit "+num);
					
						out.println("<table>"
								+"<tr>"
								+"<th>Document ID</th>"
								+"<th>Title</th>"
								+"</tr>"
								);
						while(rs.next())
						{
							out.println("<tr>"
									+"<td>"
									+rs.getString(1)
									+"</td>"
									+"<td>"
									+rs.getString(2)
									+"</td>"
									+"</tr>");
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(radio.equals("mbl"))
				{
					num=request.getParameter("num2");
					
					try {
						rs=stmt.executeQuery("select b.DocId, title from borrows b Natural JOIN document d group by docid Order by (select count(CopyNo) from borrows) desc limit "+num);
						
						out.println("<table>"
								+"<tr>"
								+"<th>Document ID</th>"
								+"<th>Title</th>"
								+"</tr>"
								);
						while(rs.next())
						{
							out.println("<tr>"
									+"<td>"
									+rs.getString(1)
									+"</td>"
									+"<td>"
									+rs.getString(2)
									+"</td>"
									+"</tr>");
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(radio.equals("top10"))
				{
					num=request.getParameter("year");
					
					try {
						rs=stmt.executeQuery("select b.DocId, title from borrows b NATURAL JOIN document d where b.bor_no in (select bor_no from borrowing where YEAR(BDate) = \'"+num+"\') group by docid Order by (select count(CopyNo) from borrows) desc limit 10");
						
						out.println("<table>"
								+"<tr>"
								+"<th>Document ID</th>"
								+"<th>Title</th>"
								+"</tr>"
								);
						while(rs.next())
						{
							out.println("<tr>"
									+"<td>"
									+rs.getString(1)
									+"</td>"
									+"<td>"
									+rs.getString(2)
									+"</td>"
									+"</tr>");
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(radio.equals("avgfine"))
				{
					date1=request.getParameter("date1");
					date2=request.getParameter("date2");
					
					try {
						rs=stmt.executeQuery("select b.BID, br.Lname, AVG((DATEDIFF(RDate,BDate)-20)*0.20) from borrows b Natural Join Branch br Natural Join borrowing where b.bor_no in (select bor_no from borrowing where BDate >= \'"+date1+"\' and BDate <= \'"+date2+"\' and (DATEDIFF(RDate,BDate)+1)>20) group by BID");
						
						out.println("<table>"
								+"<tr>"
								+"<th>Branch ID</th>"
								+"<th>Branch Name</th>"
								+"<th>Average fine collected in dollars</th>"
								+"</tr>"
								);
						while(rs.next())
						{
							out.println("<tr>"
									+"<td>"
									+rs.getString(1)
									+"</td>"
									+"<td>"
									+rs.getString(2)
									+"</td>"
									+"<td>"
									+rs.getString(3)
									+"</td>"
									+"</tr>");
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	}
}

	    
