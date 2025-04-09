package com.hetero.heteroiconnect.otherservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository
public class OtherDao {

	
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	private DataSource dataSource;

	   
       public void setDataSource(DataSource dataSource) {
           this.jdbcTemplate = new JdbcTemplate(dataSource);
       }
       
       
       @SuppressWarnings("rawtypes")
   	public JSONArray getlist(String BUD) throws SQLException
   	{
   		JSONArray empid= new JSONArray();
   		net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
   		 
   			String EMPINFO="  select p.employeesequenceno 'EMPID', p.callname 'NAME',s.name'STATUS',   ifnull(em.email,'NA') 'PRO_MAIL',ifnull(em.mobile,'NA') AS MOBILE, ifnull(f.salesofficename,'NA') as SALEOFFICE ,   IFNULL(HQLOC.NAME,'NA') HQ, IFNULL(RLOC.NAME,'') REGION ,   ifnull(DEPT.NAME,'NA') AS DEPT,ifnull(DES.NAME,'NA') AS DESIGNATION, ifnull(DEPT.CODE,'NA') AS DEPT_CODE,ifnull(DES.CODE,'NA') AS DESIGNATION_CODE    ,ifnull(DES.DESIGNATIONID,'0') DESIGNATIONID,ifnull(DEPT.DEPARTMENTID,'0') DEPARTMENTID from hclhrm_prod.tbl_employee_primary p    left join hclhrm_prod.tbl_status_codes s on s.status=p.status    left join hcladm_prod.tbl_costcenter co on p.costcenterid=co.costcenterid    left join hclhrm_prod.tbl_employee_professional_contact em on p.employeeid=em.employeeid    left join hclhrm_prod_others.tbl_employee_salesoffice S ON S.employeeid=p.employeeid    left join hclhrm_prod_others.tbl_salesoffice f ON s.salesofffice=f.salesofficeCODE   LEFT JOIN hclhrm_prod.tbl_employee_professional_details DD ON P.EMPLOYEEID=DD.EMPLOYEEID    LEFT JOIN HCLLCM_PROD.TBL_LOCATION RLOC ON DD.SUBLOCATIONID=RLOC.LOCATIONID   LEFT JOIN HCLLCM_PROD.TBL_LOCATION HQLOC ON DD.WORKLOCATIONID=HQLOC.LOCATIONID   LEFT JOIN hcladm_prod.tbl_department DEPT ON DEPT.DEPARTMENTID=DD.DEPARTMENTID    LEFT JOIN hcladm_prod.tbl_designation DES ON DES.DESIGNATIONID=DD.DESIGNATIONID    where  p.companyid in ("+BUD+") and p.status in (1001)  and p.employeeid not in (1,0) and p.employeesequenceno not in (1,0)   ";
   			
   		 PreparedStatement preparedStatement = null;
	     Connection connection = dataSource.getConnection();
	     preparedStatement =connection.prepareStatement(EMPINFO);
	     
	     ResultSet rs = null;
			rs=null;
			
			JSONObject jason = new JSONObject();
	 		
	 		JSONObject Maste_jason = new JSONObject();
	 		
	 		JSONArray jasonarray=new JSONArray();
			
				  rs = preparedStatement.executeQuery();
				ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
				// String name = rsmd.getColumnName(1);
				int columnCount = rsmd.getColumnCount();
				while(rs.next()){
					
					for (int i = 1; i <= columnCount; i++ ) {
						
						 
					//	flag=true;
					   // jason.put(rsmd.getColumnName(i),rs.getString(i));
					    
						  jason.put(rsmd.getColumnName(i),rs.getString(i));
					//System.out.println(rsmd.getColumnName(i)+"~~~"+rs.getString(i));
					    
					    
				     }
			 
					jasonarray.add(jason);
					jason=new JSONObject();
				
				}
   			
   			
   			/*List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
   			  for (@SuppressWarnings("rawtypes") Map row : rows) {
   				  ADD=new net.sf.json.JSONObject();
   				  //TYPE, DATE, Available, ENABLE
   				  ADD.put("REQUESTTYPE",row.get("REQUESTTYPE"));
   				  ADD.put("FLAG",row.get("FLAG"));
   				  empid.add(ADD);
   	        }
   			}catch(Exception err){
   				System.out.println("Exception at reverse" +err);
   			}*/
   		return jasonarray;
		 
     }
   	}
