package com.hetero.heteroiconnect.classes;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
public class Attcompre {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public synchronized static Map getattencedata(ResultSet rs) {
	 Map Data=new HashMap();
	 String CommanField$key=null;
	 ResultSetMetaData resultSetMetaData=null;
	try {
		resultSetMetaData = rs.getMetaData();
	} catch (SQLException e) {
		e.printStackTrace();
	}
      int Obj_one_count=0;
	try {
		Obj_one_count = resultSetMetaData.getColumnCount();
	} catch (SQLException e) {
		e.printStackTrace();
	}
      try {
		while(rs.next()) {
			   CommanField$key=rs.getString(1);
			  for(int i=1;i<=Obj_one_count;i++){
			   String ColumName=resultSetMetaData.getColumnName(i);
			   Data.put(CommanField$key+"_"+ColumName, rs.getString(ColumName));
			  }
		  }
	} catch (SQLException e) {
		e.printStackTrace();
	}
		// System.out.println("MAP DATA =========="+Data);
		return Data;
	}
	
	
	
	
	
	
	
	
	//Date Format long date_difference 13:30:00
	public static String dateFormatLong() {
		Date date = new Date();
		String format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(date);
	}


//Date format for mysql
public static String dateFormatMySql(String date) throws ParseException {
String stringDateFormat = "14/09/2011";
DateFormat formatter =new SimpleDateFormat("dd/MM/yyyy");
Date convertedDate =(Date) formatter.parse(stringDateFormat);
System.out.println("Date from dd/MM/yyyy String in Java : " + convertedDate);
return ""+convertedDate;
}
//date difference
public static long date_difference(String dateStart)
{
 String dateStop = Attcompre.dateFormatLong();
 	Date date = new Date();
	String format = "yyyy-MM-dd HH:mm:ss";
	SimpleDateFormat form = new SimpleDateFormat(format);
	
	//sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	//return sdf.format(date);
	Date d1 = null;
	Date d2 = null;
	try {
	
		d1 = form.parse(dateStart);
		d2 = form.parse(dateStop);
		//in milliseconds
		long diff = d2.getTime() - d1.getTime();
		//long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000);
		return diffHours;
	}
	catch(Exception e)
	{
		 e.printStackTrace();
		 return 0;
	}

}
public static long date_difference1(String dateStart2)
{
 String dateStop = Attcompre.dateFormatLong();
 	Date date = new Date();
	String format = "yyyy-MM-dd HH:mm:ss";
	SimpleDateFormat form = new SimpleDateFormat(format);
	
	//sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	//return sdf.format(date);
	Date d1 = null;
	Date d2 = null;
	try {
	
		d1 = form.parse(dateStart2);
		d2 = form.parse(dateStop);
		//in milliseconds
		long diff = d2.getTime() - d1.getTime();
		long diffMinutes = diff / (60 * 1000) % 60;
		return diffMinutes;
	}
	catch(Exception e)
	{
		 e.printStackTrace();
		 return 0;
	}

}














	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
