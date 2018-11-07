package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO
{
	private static String dbURL = "jdbc:derby://localhost:64413/EECS;user=student;password=secret";;
	private List<StudentBean> students;
	public StudentDAO(String name, String category, String gpa) throws Exception {
		students = new ArrayList<>();
		initializeStudents(name, category, gpa);
	}
	 
	public void initializeStudents(String name, String category, String gpa) throws Exception {
		  Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
		  Connection con = DriverManager.getConnection(StudentDAO.dbURL);
		  Statement s = con.createStatement();
		  s.executeUpdate("set schema roumani");
		//SQL query to obtain the NAME and PRICE of an item whose number is itemNo in a table ITEM
		  String query = "SELECT  SURNAME, GIVENNAME, MAJOR, COURSES, GPA FROM SIS WHERE GIVENNAME LIKE '%" 
				  				+ name.trim() +"%' and GPA > " + gpa +" order by " + category;
		  
		  ResultSet r = s.executeQuery(query);
		  
		  while (r.next())
		  {
		  	String full_name =  r.getString("SURNAME") + ", " + r.getString("GIVENNAME");
		  	this.students.add(new StudentBean(full_name, r.getString("MAJOR"),
		  										r.getInt("COURSES"), r.getDouble("GPA")));
		  }
		  //else
		  {
		  	//throw new Exception(itemNo + " not found! ");
		  }
		  r.close(); 
		  s.close(); 
		  con.close();

	}
	 
	public List<StudentBean> retreive() {
		 
		 return students;
	 }
	 
	public String retreiveInJson() {
		 String response = "[";
		 int counter = 0;
		 for (StudentBean student: students) {
			 counter++;
			 response += String.format("{\"name\": \"%s\", \"major\": \"%s\", \"gpa\": %f, \"courses\": %d},", 
					  student.getName(), student.getMajor(),  student.getGpa(), student.getCourses());
		 }
		 response += "{\"name\": \"%s\", \"major\": \"Hello\", \"gpa\": 2.2, \"courses\": 1}]";
			 			 
		 return response;
	 }
	
}
