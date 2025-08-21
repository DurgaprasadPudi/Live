package com.hetero.heteroiconnect.privileges;

import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;

@RestController
@RequestMapping("/employeerights")
public class EmployeeprivilegesConroller {
	 
    
	@PersistenceContext    
	EntityManager entityManager;
	 
	 
	@PostMapping("privileges")
	public LinkedHashMap<String, Object> attendance(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
		 
			JSONArray main = new JSONArray();
			 	
		    String Privilege_QRY = "";
		    
		    
		    if(object.getString("empID").toString().equals("30546"))
		    {
		    	 Privilege_QRY= " SELECT p.PRIVILEGEID,NAME ACCESSNAME,PARENT FROM hclhrm_prod_others.tbl_employee_iconnect_privileges p left join hclhrm_prod_others.iconnect_rights r on r.PRIVILEGEID=p.PRIVILEGEID and p.PRIVILEGEID!=12 where p.employeeid in("+object.getString("empID")+") ";
				    
		    }
		    else
		    {
		    	 Privilege_QRY= " SELECT p.PRIVILEGEID,NAME ACCESSNAME,PARENT FROM hclhrm_prod_others.tbl_employee_iconnect_privileges p left join hclhrm_prod_others.iconnect_rights r on r.PRIVILEGEID=p.PRIVILEGEID where p.employeeid in("+object.getString("empID")+") ";
				    	
		    }
		    	
		    
		   
		   
		         List<Object[]> Privilege_Obj = entityManager.createNativeQuery(Privilege_QRY).getResultList();
		         for (Object temp[] : Privilege_Obj) {
		        	 net.sf.json.JSONObject addobj=new net.sf.json.JSONObject();
		        	 addobj.put("privilegeid", temp[0].toString());
		        	 addobj.put(temp[1].toString(),"true");
		        	 addobj.put("parent",temp[2].toString());
		        	 main.add(addobj);
		         } 
		        
					
		    response.put("Rights",main);
			 

			return response;
	}
	
	
}
