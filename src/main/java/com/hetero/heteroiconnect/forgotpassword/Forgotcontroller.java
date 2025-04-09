package com.hetero.heteroiconnect.forgotpassword;
 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
  


@RestController
@RequestMapping("/forgot")
public class Forgotcontroller {
	  
     @Autowired
     ForgotpasswordRepository forgot; 
     
 	@PersistenceContext
 	EntityManager entityManager;
    
	 	@PostMapping("empverify")
		public LinkedHashMap<String, Object> empverify(@RequestBody String login) throws JSONException {
			LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
			org.json.JSONObject object = new org.json.JSONObject(login);
			
			Forgotpassword frgt=null;
			 
			frgt=forgot.empverify(object.getString("empID"));
	         
	        
		     response.put("verify",frgt); 
		         
			return response;
		}
	 	
		@PostMapping("getotp")
		public LinkedHashMap<String, Object> getotp(@RequestBody String login) throws JSONException {
			LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
			org.json.JSONObject object = new org.json.JSONObject(login);
			
			Random random = new Random();
			String generatedPassword = String.format("%04d", random.nextInt(10000));
			 
			response=CallSmsData(object.getString("mobile"),generatedPassword);
			
          String mobile="";
			
			if(object.getString("mobile")==null)
			{
				mobile= "0000000000";
			}
			else
			{
				mobile=object.getString("mobile");
			}
		 
			int otpinsert=forgot.findbyOTPINSERT(object.getString("empID"),generatedPassword,mobile);
			
			if(otpinsert==1)
			{
				
			}
	        
			return response;
		}
		
		@PostMapping("otpverify")
		public LinkedHashMap<String, Object> otpverify(@RequestBody String login) throws JSONException {
			LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
			org.json.JSONObject object = new org.json.JSONObject(login);
			
		  
			  String OTP_QRY = "";
			  OTP_QRY= "select count(*) VALID, IF(TIMEDIFF(TIME(now()),TIME(TIMESTAMP))<=TIME(concat('00',VALID,'00')),1,0) OTPEXPIRE,IFNULL(TIMEDIFF(TIME(now()),TIME(TIMESTAMP) ),0) TIME from test.tbl_employee_forgot_pass where  SNO IN(SELECT MAX(SNO) FROM test.tbl_employee_forgot_pass where employeeid="+object.getString("empID")+") and OTP="+object.getString("OTP")+" and status=1001" ;
			        
		          
				List<Object[]> OTP_obj = entityManager.createNativeQuery(OTP_QRY).getResultList();
		         
		         for (Object temp[] : OTP_obj) {
		        	  
		        	 response.put("VALID",temp[0].toString()); 
		        	 response.put("OTPEXPIRE",temp[1].toString()); 
		        	 response.put("TIME",temp[2].toString()); 
		        	// System.out.println(temp[8].toString());
		         } 
		         
			  
			return response;
		}
		
		
		
		
		@PostMapping("forgotpassword")
		public LinkedHashMap<String, Object> forgotpassword(@RequestBody String login) throws JSONException {
			LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
			org.json.JSONObject object = new org.json.JSONObject(login);
			 
			int i=forgot.findbyforgotstatusclose(object.getString("empID"),object.getString("OTP"),object.getString("password"));
		     
			int j=forgot.findbyforgotpassword(object.getString("empID"),object.getString("password"));
			 
			 if(j<0)
			{
				 response.put("VALID",i); 
			} 
			 else
			 {
				 
			 }
			// response.put("VALID",temp[0].toString()); 
			
			 response.put("VALID",j); 
			 
			return response;
		}
		
		
		
			public static synchronized  LinkedHashMap<String, Object> CallSmsData(String Mobile,String Otp){
				 
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				
				String message="Request Not Processed Please contact admin..!";
				String ErrorCode="2002";
				try{
					Date mydate = new Date(System.currentTimeMillis());
					String data = "";
					data += "APIKey="+URLEncoder.encode("zgC7j9EY4k2vALqNECIWHg" ,"UTF-8");
					data += "&senderid="+URLEncoder.encode("HETERO" ,"UTF-8");
					data +="&channel="+URLEncoder.encode("2","UTF-8");
					data += "&DCS="+URLEncoder.encode( "0" ,"UTF-8");
					data += "&flashsms="+URLEncoder.encode("0" ,"UTF-8");
					data += "&number="+URLEncoder.encode( ""+Mobile+"" ,"UTF-8") ;
					///data += "&text="+URLEncoder.encode(""+Otp+" is OTP for password change in your iConnect, OTP is valid for 15 mins Please do not share with anyone." ,"UTF-8");
					//data += "&text="+URLEncoder.encode("Hi, We have Successfully Generated OTP "+Otp+" on Forgot Password request. Kindly use This OTP To Update Your password with us. Hetero" ,"UTF-8");
					
					data += "&text="+URLEncoder.encode(""+Otp+" is OTP for password change in your iConnect. OTP is valid for 15 mins. Please do not share with anyone. Hetero" ,"UTF-8");
					
					data += "&route="+URLEncoder.encode( "47" ,"UTF-8");
					data += "&DLTTemplateId="+URLEncoder.encode( "1307161656863660290" ,"UTF-8");
					data += "&PEID="+URLEncoder.encode( "1301159171444767061" ,"UTF-8");
				
					URL url = new URL("https://push.smsc.co.in/api/mt/SendSMS?" + data);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");

					conn.setRequestMethod("GET"); 
					conn.setDoOutput(true);
					conn.setDoInput(true);
					conn.setRequestProperty("Accept", "application/json");
					conn.setUseCaches(false);
					conn.connect();
					 
					System.out.println(conn);
					
					
					BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String line;
					StringBuffer buffer = new StringBuffer();
					while((line = rd.readLine()) != null){
						buffer.append(line).append("\n");
					}
				//System.out.println(buffer.toString());
					rd.close();
					conn.disconnect();
					// Start Jason object Retriving */
					JSONParser parser = new JSONParser();
					JSONObject jsonObject = (JSONObject) parser.parse(buffer.toString());
					//System.out.println(jsonObject);
					ErrorCode = (String) jsonObject.get("ErrorCode");
					//System.out.println(ErrorCode);
					message = (String) jsonObject.get("ErrorMessage");
					//System.out.println(message);
					if(ErrorCode.equalsIgnoreCase("000")){
						message="Success";
					}else if(ErrorCode.equalsIgnoreCase("001"))
					{
						message="001 login details cannot be blank";
					}
					else if(ErrorCode.equalsIgnoreCase("003"))
					{
						message="003 sender cannot be blank";
					}
					else if(ErrorCode.equalsIgnoreCase("004"))
					{
						message="004 message text cannot be blank";
					}
					else if(ErrorCode.equalsIgnoreCase("005"))
					{
						message="005 message data cannot be blank";
					}
					else if(ErrorCode.equalsIgnoreCase("006"))
					{
						message="006 error: generic error description";
					}
					else if(ErrorCode.equalsIgnoreCase("007"))
					{
						message="007 username or password is invalid";
					}
					else if(ErrorCode.equalsIgnoreCase("008"))
					{
						message="008 account not active";
					}
					else if(ErrorCode.equalsIgnoreCase("009"))
					{
						message="009 account locked, contact your account manager";
					}
					else if(ErrorCode.equalsIgnoreCase("010"))
					{
						message="010 api restriction";
					}
					else if(ErrorCode.equalsIgnoreCase("011"))
					{
						message="011 ip address restriction";
					}
					else if(ErrorCode.equalsIgnoreCase("012"))
					{
						message="012 invalid length of message text";
					}
					else if(ErrorCode.equalsIgnoreCase("013"))
					{
						message="013 mobile numbers not valid";
					}
					else if(ErrorCode.equalsIgnoreCase("014"))
					{
						message="014 account locked due to spam message contact support";
					}
					else if(ErrorCode.equalsIgnoreCase("015"))
					{
						message="015 senderid not valid";
					}
					else if(ErrorCode.equalsIgnoreCase("017"))
					{
						message="017 groupid not valid";
					}
					else if(ErrorCode.equalsIgnoreCase("018"))
					{
						message="018 multi message to group is not supported";
					}
					else if(ErrorCode.equalsIgnoreCase("019"))
					{
						message="019 schedule date is not valid";
					}
					else if(ErrorCode.equalsIgnoreCase("020"))
					{
						message="020 message or mobile number cannot be blank";
					}
					else if(ErrorCode.equalsIgnoreCase("021"))
					{
						message="021 insufficient credits";
					}
					else if(ErrorCode.equalsIgnoreCase("022"))
					{
						message="022 invalid jobid";
					}
					else if(ErrorCode.equalsIgnoreCase("023"))
					{
						message="023 parameter missing";
					}
					else if(ErrorCode.equalsIgnoreCase("024"))
					{
						message="024 invalid template or template mismatch";
					}

					else if(ErrorCode.equalsIgnoreCase("025"))
					{
						message="025 {Field} can not be blank or empty";
					}
					else if(ErrorCode.equalsIgnoreCase("026"))
					{
						message="026 invalid date range";
					}
					else if(ErrorCode.equalsIgnoreCase("027"))
					{
						message="027 invalid optin user";
					}
					// End Jason object Retriving */


				}
				catch (Exception e){
					e.printStackTrace();
				}

				map.put("message", message);
				map.put("ErrorCode", ErrorCode);
				map.put("Mobile", Mobile);
				
				return map;
			}
	    
}
