package com.hetero.heteroiconnect.classes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

public class Attenance_backup {
	 
	@PersistenceContext
	EntityManager entityManager;
	
	 static final Logger logger = Logger.getLogger(Attenance_backup.class);
	
	@SuppressWarnings("unused")
	public synchronized static int  checkingAttendance(String logIn , String logOut, String workedHrs ,String Halfday){


		String []LogINTime=null ; 
		StringBuffer SQL_INSERT = new StringBuffer();
		String []LogOUTTime=null , netHrs=null;
		String NetFlexiHrs ,Leaveon   ;
		int LogInH=0,LogInM=0, LogInSec=0, LogOutH=0,LogOutM=0 , LogOutSec=0, netH=0,netM=0, netSec=0,  totalWorkHrs=9 , diffHrs=0 ,diffMints=0 , deductionHrs=0;


		try{



			LogINTime=logIn.split(":");
			LogOUTTime=logOut.split(":");
			netHrs=workedHrs.split(":");

			LogInH=Integer.parseInt(LogINTime[0]);
			LogInM=Integer.parseInt(LogINTime[1]);
			LogOutH=Integer.parseInt(LogOUTTime[0]);
			LogOutM=Integer.parseInt(LogOUTTime[1]);
			netH=Integer.parseInt(netHrs[0]);
			netM=Integer.parseInt(netHrs[1]);

			//  logger.debug(" login hour.... "+LogInH 	+" \n login minute "+LogInM +" \n logout hr "+LogOutH +" \n login minute "+LogOutM);

			if(LogInH==0 && LogInM==0 && LogOutH==0){
				logger.debug("holiday");
			}
			
			
			

			else if(((LogInH<8 && LogInM>=0)||(LogInH==8 && LogInM<=29)||(LogInH==8 && LogInM>=30))  && Halfday.equalsIgnoreCase("WDAY") && netH>0){
				
				System.out.println("NEW POLICY");
				LogInH =8;
				LogInM=30;
				logger.debug(" CHECKING WORKING HOURS....!!!!!!! ");
				if((LogOutH==17 && LogOutM<30 )|| (LogOutH<17 && LogOutM>=00)){
					//System.out.println("1::::::");

					logger.debug(" early login ....logouted  early");  //totalWorkHrs ,deductionHrs
					if(LogOutH==17 && LogOutM<30  ){ //8:31   ........|| netH==9 && netM==00 
						deductionHrs=1;
						
						//System.out.println("1::::::");

						logger.debug(logIn+ "  LOGIN....!!!!!!!!!!! worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
						
						//System.out.println("17::::::----> 1");
					}
					else if(LogOutH==16 && LogOutM>30 ){  //8:30
						deductionHrs=2;

						logger.debug( logIn+ "IN EARLY LOGIN....!!!!!!!!!!!  worked hours = " + logOut   +"------ deducted hours are..." + deductionHrs);  
						//System.out.println("16::::::----> 2");
					} 
					else if(LogOutH==16 && LogOutM<30 ||LogOutH<16 && LogOutM>=0){// above 7 hrs
						deductionHrs=4;
						//workedHrs  = new StringBuffer().append(netH).append(":").append(netM).toString(); 
						logger.debug( logIn+ " worked hours = " + logOut   +"------ deducted hours are...." + deductionHrs);  
						
						//System.out.println("16::::::----> 4");
					}  
					
				}
				// CORRECTLY LOGOUT
				else{

					if(LogOutH>=17 && LogOutM>=30||LogOutH>18 && LogOutM>=00) {

						logger.debug(" early login ....logouted  CORRECTLY");

						//  logger.debug(" worked hours = " + workedHrs   );
					}

				}

			}
			
			
			///// OLD --Policy----///
			
			
			
			// from below 9:00 
			else if(LogInH<9 && LogInM>=0 && Halfday.equalsIgnoreCase("WDAY")){
				LogInH =9;
				LogInM=0;
				logger.debug(" CHECKING WORKING HOURS....!!!!!!! ");
				if(LogOutH<18 && LogOutM>=00){

					logger.debug(" early login ....logouted  early");  //totalWorkHrs ,deductionHrs
					if(netH==8 && netM>30  ){ //8:31   ........|| netH==9 && netM==00 
						deductionHrs=1;

						logger.debug(logIn+ "  LOGIN....!!!!!!!!!!! worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
					}
					else if(netH==8 && netM<=30 ){  //8:30
						deductionHrs=2;

						logger.debug( logIn+ "IN EARLY LOGIN....!!!!!!!!!!!  worked hours = " + logOut   +"------ deducted hours are..." + deductionHrs);  
					} 
					else if(netH<8 && netM>=00 ){// above 7 hrs
						deductionHrs=4;
						//workedHrs  = new StringBuffer().append(netH).append(":").append(netM).toString(); 
						logger.debug( logIn+ " worked hours = " + logOut   +"------ deducted hours are...." + deductionHrs);  
					}  
				}
				// CORRECTLY LOGOUT
				else{

					if(LogOutH>=18 && LogOutM>=0) {

						logger.debug(" early login ....logouted  CORRECTLY");

						//  logger.debug(" worked hours = " + workedHrs   );
					}

				}

			}
			//FLEXI TIMINGS
			else if(LogInH==9 && LogInM<45 && Halfday.equalsIgnoreCase("WDAY") ){

				// EARLY LOGOUT IN FLEXI TIMINGS

				// logger.debug(" CHECKING WORKING HOURS....!!!!!!! ");
				
				if("1"=="1"){
				//if(LogOutH<=18 && LogOutM>=00){


					//totalWorkHrs ,deductionHrs
					if(netH==8 &&  netM>30   ){
						deductionHrs=1;//8:31 || netH==9 && LogOutM>LogInM  ........|| netH==9 && netM==00 
						logger.debug(" early login ....logouted  early"); 
						logger.debug( logIn+ " ENTERED IN FLEXI TIMINGS \n early login ....logouted  early \n worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
					}
					/* else if(netH==9 && LogOutM>LogInM ){  //8:30
						  deductionHrs=2;
						  workedHrs  = new StringBuffer().append(netH).append(":").append(netM).toString(); 
						    logger.debug(" worked hours = " + workedHrs   +"------ deducted hours are" + deductionHrs);  
					 } */
					else if(netH==8 && netM<=30 ){  //8:30
						deductionHrs=2;

						logger.debug( logIn+ " ENTERED IN FLEXI TIMINGS \n early login ....logouted  early \n worked hours = " + logOut   +"------ deducted hours are...." + deductionHrs);  
					} 
					else  if(netH<8 && netM>=00 ){
						deductionHrs=4;

						logger.debug(logIn+" ENTERED IN FLEXI TIMINGS \n early login ....logouted  early \n worked hours = " + logOut   +"------ deducted hours are...." + deductionHrs);  
					}  

				}

				// CORRECT LOGOUT WITH FLEXI TIMINGS
				else {
					if(LogOutH>=18 && LogOutM>=0) {
						logger.debug(logIn+" ENTERED IN FLEXI TIMINGS \n early login ....CORRECT LOGOUT \n worked hours = " + logOut   );
					} 
				}

			}



			// from 9:45-10:00 ...... ( LogInH==9 &&  LogInM>45 || LogInH<=10 && LogInM<01)

			else if( Halfday.equalsIgnoreCase("WDAY") && LogInH==9 &&  LogInM>=45 || LogInH==10 && LogInM==00 ){ 
				logger.debug(" after 9:45....... 1 hours deducted ");   ///totalWorkHrs ,deductionHrs   netH==9 && netM==00 
				deductionHrs=1;

				if(netH==7 && netM>30  ){ //8:31   ........|| netH==9 && netM==00 
					deductionHrs=deductionHrs+1;

					logger.debug(logIn+"ENTERED IN ONE or 1 deductionHrs \n   worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
				}
				else if(netH==7 && netM<=30 ){  //8:30
					deductionHrs=deductionHrs+2;
					workedHrs  = new StringBuffer().append(netH).append(":").append(netM).toString(); 
					logger.debug(logIn+"  ENTERED IN ONE or 1 deductionHrs \n  worked hours = " + logOut   +"------ deducted hours are...." + deductionHrs);  
				} 
				else if(netH<7 && netM>=00 ){
					deductionHrs=deductionHrs+4;
					workedHrs  = new StringBuffer().append(netH).append(":").append(netM).toString(); 
					logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n  worked hours = " + logOut   +"------ deducted hours are....." + deductionHrs);  
				}  

				else {


					if(LogOutH==18 &&  LogOutM>45 || LogOutH==19 && LogOutM==00){

						deductionHrs=1;
					}

				}
			}
			// FIRST HALF_DAY LEAVE 
			else if (Halfday.equalsIgnoreCase("1st Half") ){
				//LogInH =14;
				Halfday ="In 1st Half LEAVE";
				
				if(netH<4 && netM>=0  ){ 
					deductionHrs=4;
					logger.debug("in ******   SECOND  ******HALF" + deductionHrs);  
					logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n   worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
				}
				
				if(LogOutH<=18 && LogOutM>=00){ 
					/******	if(netH==3 && netM>30  ){ //8:31   ........|| netH==9 && netM==00 
						deductionHrs=1;
						logger.debug(" in ******  FIRST   ******  HALF" + deductionHrs);  
						logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n   worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
					}
					else if(netH==3 && netM<=30 ){  //8:30
						deductionHrs=2;
						workedHrs  = new StringBuffer().append(netH).append(":").append(netM).toString(); 

						logger.debug(" in  ******  FIRST ******   HALF" + deductionHrs);  
						logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n  worked hours = " + logOut   +"------ deducted hours are...." + deductionHrs);  
					} 
					else if(netH<3 && netM>=00 ){
						deductionHrs=4;
						workedHrs  = new StringBuffer().append(netH).append(":").append(netM).toString(); 
						logger.debug(" in ******  FIRST   ******  HALF" + deductionHrs);  
					}   ******/
					
					/*if(netH<4 && netM>=0  ){ 
						deductionHrs=4;
						logger.debug("in ******   SECOND  ******HALF" + deductionHrs);  
						logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n   worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
					}*/
				}
				else{
					if(LogOutH>=18 &&  LogOutM>=00){

						logger.debug("your leaves will be counted ......for ur 1st half leave");  

					}
				}

			}

			// SECOND HALF_DAY LEAVE 
			else if (Halfday.equalsIgnoreCase("2nd Half")){ 

				//LogInH =9;
				Halfday ="In 2nd Half LEAVE";
				
				if((netH>=4 && netM>=0) && (LogInH==9 && LogOutM<=45) || LogInH<=9 ){ //8:31   ........|| netH==9 && netM==00 
					deductionHrs=0;
					logger.debug("in ******   SECOND  ******HALF" + deductionHrs);  
					logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n   worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
				}else if((netH>=4 && netM>=0) && (LogInH==9 && LogOutM>45) || (LogInH==10 &&  LogOutM==0) ){
				   
					deductionHrs=1;
					logger.debug("in ******   SECOND  ******HALF" + deductionHrs);  
					
				}else{
					deductionHrs=4;
				}
				
				
				if(LogOutH<13 && LogOutM>=00){ 
					/*****
					if(netH==3 && netM>30  ){ //8:31   ........|| netH==9 && netM==00 
						deductionHrs=1;
						logger.debug("in ******   SECOND  ******HALF" + deductionHrs);  
						logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n   worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
					}
					else if(netH==3 && netM<=30 ){  //8:30
						deductionHrs=2;
						workedHrs  = new StringBuffer().append(netH).append(":").append(netM).toString(); 

						logger.debug(" in ******   SECOND  ******HALF" + deductionHrs);  
						logger.debug(logIn+ " ENTERED IN ONE or 1 deductionHrs \n  worked hours = " + logOut   +"------ deducted hours are...." + deductionHrs);  
					} 
					else if(netH<3 && netM>=00 ){
						deductionHrs=4;
						workedHrs  = new StringBuffer().append(netH).append(":").append(netM).toString(); 

						logger.debug(" in ******   SECOND  ******HALF" + deductionHrs);  
						logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n  worked hours = " + logOut   +"------ deducted hours are....." + deductionHrs);  
					}  ******/
					
				/*	if(netH<4 && netM>=0  ){ //8:31   ........|| netH==9 && netM==00 
						deductionHrs=4;
						logger.debug("in ******   SECOND  ******HALF" + deductionHrs);  
						logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n   worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
					}*/
					
				}else{
					if(LogOutH>=13 &&  LogOutM>=00){

						logger.debug("your leaves will be counted ......for ur 2nd half leave");  

					}
				}



			}else if (logIn.equalsIgnoreCase(logOut)&& logIn!="00:00:00" && logOut!="00:00:00" && workedHrs.equalsIgnoreCase("00:00:00") )
			{  //logIn , logOut, workedHrs
				deductionHrs=4;
				logger.debug("in ******   IN=OUT and net 00 " + deductionHrs);
				//logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n   worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);
		   }

			// after 10:00
			else  {
				 logger.debug(" after 10 .....4 hours deducted  "); // comment by venu 26-12-2020 
				deductionHrs=4;
			
				if(LogInH>=10 && LogInM>0 || LogInH>10 && LogInM>=0){
					LogInH =14;
					if(netH>=4 && netM>=0){

						logger.debug(logIn+"  worked hours = " + logOut   +"--deducted hours are ..... " + deductionHrs);  
					} 

					if(netH==3 && netM>30  ){ //8:31   ........|| netH==9 && netM==00 
						deductionHrs=deductionHrs+1;

						logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n   worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
					}
					else if(netH==3 && netM<=30 ){  //8:30
						deductionHrs=deductionHrs+2;
						workedHrs  = new StringBuffer().append(netH).append(":").append(netM).toString(); 
						logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n  worked hours = " + logOut   +"------ deducted hours are...." + deductionHrs);  
					} 
					else if(netH<3 && netM>=00 ){
						deductionHrs=deductionHrs+4;
						workedHrs  = new StringBuffer().append(netH).append(":").append(netM).toString(); 
						logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n  worked hours = " + logOut   +"------ deducted hours are....." + deductionHrs);  
					}  
				}
				
				
			} // else statement 



			//  logger.debug("INSERTING DATA IN TABLE ");




		}catch(Exception ex){
			ex.printStackTrace();
		}
		return deductionHrs;
	}
	
	
	


	public synchronized static String AhmecheckingAttendance(String logIn , String logOut, String workedHrs ,String Halfday, int login_9_10_counter){

		String total_ded_hrs="";
		String []LogINTime=null ; 
		StringBuffer SQL_INSERT = new StringBuffer();
		String []LogOUTTime=null , netHrs=null;
		String NetFlexiHrs ,Leaveon   ;
		int policyLogOutH=17, policyLogOutM=30 ,LogInH=0,LogInM=0, LogInSec=0, LogOutH=0,LogOutM=0 , LogOutSec=0, netH=0,netM=0, netSec=0,  totalWorkHrs=9 , 
				diffHrs=0 ,diffMints=0 , deductionHrs=0 , deductionMts=0 ;


		try{



			LogINTime=logIn.split(":");
			LogOUTTime=logOut.split(":");
			netHrs=workedHrs.split(":");

			LogInH=Integer.parseInt(LogINTime[0]);
			LogInM=Integer.parseInt(LogINTime[1]);
			LogOutH=Integer.parseInt(LogOUTTime[0]);
			LogOutM=Integer.parseInt(LogOUTTime[1]);
			netH=Integer.parseInt(netHrs[0]);
			netM=Integer.parseInt(netHrs[1]);

			//  logger.debug(" login hour.... "+LogInH 	+" \n login minute "+LogInM +" \n logout hr "+LogOutH +" \n login minute "+LogOutM);

			if(LogInH==0 && LogInM==0 && LogOutH==0){
				logger.debug("holiday");
			}
			// from below 9:00 
			else if(LogInH<9 && LogInM>=0 && Halfday.equalsIgnoreCase("WDAY")){
				LogInH =9;
				if(LogOutH<policyLogOutH && LogOutM>= 00 ){ 

					if(LogOutM>00 && LogOutM>=31 ){
						deductionHrs=policyLogOutH-LogOutH;
						deductionMts=59-LogOutM;

					}
					if(LogOutM>=00 && LogOutM<30 ){
						deductionHrs=policyLogOutH-LogOutH;
						deductionMts=29-LogOutM;

					}
				}
				else if(LogOutH==policyLogOutH && LogOutM<=30) {
					deductionHrs=policyLogOutH-LogOutH;
					deductionMts=29-LogOutM;


				}
				else if(LogOutH==policyLogOutH && LogOutM>=30) {
					logger.debug("  login ....logouted  CORRECTLY");


				}
				else{
					if(LogOutH>policyLogOutH && LogOutM>=00) {
						logger.debug("  login ....logouted  CORRECTLY .........But spent more time in office");
					}
				}

			}
			//FLEXI TIMINGS
			else if(LogInH==9 && LogInM<=45 && Halfday.equalsIgnoreCase("WDAY") &&  LogInM>0 )
			{
				int  deductionHrs_new=0;

				if(login_9_10_counter<=3 && LogInM<=10 && login_9_10_counter>0 &&  LogInM>0 && ((LogOutH==17 && LogOutM>= 30)||(LogOutH>17 && LogOutM>= 00))) {
					deductionHrs=0;
					deductionMts=0;
				}else {
					deductionHrs_new=1;
					deductionMts=0;
				}
				// EARLY LOGOUT IN FLEXI TIMINGS
				//if("1"=="1"){

				if(LogOutH<policyLogOutH && LogOutM>= 00 ){ 

					if(LogOutM>00 && LogOutM>=31 ){
						deductionHrs=policyLogOutH-LogOutH;
						deductionMts=59-LogOutM;
						deductionHrs=deductionHrs+deductionHrs_new;
					}
					if(LogOutM>=00 && LogOutM<30 ){
						deductionHrs=policyLogOutH-LogOutH;
						deductionMts=29-LogOutM;
						deductionHrs=deductionHrs+deductionHrs_new;
					}
				}
				else if(LogOutH==policyLogOutH && LogOutM<=30) {
					deductionHrs=policyLogOutH-LogOutH;
					deductionMts=29-LogOutM;
					deductionHrs=deductionHrs+deductionHrs_new;
					logger.debug("  login ....logouted  CORRECTLY");

				}
				else if(LogOutH==policyLogOutH && LogOutM>=30) {
					deductionHrs=	deductionHrs_new;


				}
				else{
					if(LogOutH>policyLogOutH && LogOutM>=00) {
						deductionHrs=	deductionHrs_new;

						logger.debug("  login ....logouted  CORRECTLY .........But spent more time in office");
					}
				}



			}





			// from 9:45-10:00 ...... ( LogInH==9 &&  LogInM>45 || LogInH<=10 && LogInM<01)

			else if( Halfday.equalsIgnoreCase("WDAY") && ((LogInH==9 &&  LogInM>=46)||(LogInH>=10 &&  LogInM>=00) ) ){ 
				logger.debug(" after 9:45....... 1 hours deducted ");   ///totalWorkHrs ,deductionHrs   netH==9 && netM==00 
				int  deductionHrs_new=4;

				//if("1"=="1"){

				if(LogOutH< policyLogOutH && LogOutM>= 00 ){ 


					if(LogOutM>00 && LogOutM>31 ){
						deductionHrs=policyLogOutH-LogOutH;
						deductionMts=59-LogOutM;
						deductionHrs=deductionHrs+deductionHrs_new;
					}
					if(LogOutM>=00 && LogOutM<=30 ){
						deductionHrs=policyLogOutH-LogOutH;
						deductionMts=29-LogOutM;
						deductionHrs=deductionHrs+deductionHrs_new;
					}
					
					logger.debug(logIn+ "  LOGIN....!!!!!!!!!!! worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
				}


				else if(LogOutH==policyLogOutH && LogOutM>=30) {
					deductionHrs=deductionHrs_new;

					logger.debug("  login ....logouted  CORRECTLY");
				}
				else{
					if(LogOutH>policyLogOutH && LogOutM>=00) {

						deductionHrs=deductionHrs_new;
						logger.debug("  login ....logouted  CORRECTLY .........But spent more time in office");

					}
				}
			}
			
			// FIRST HALF_DAY LEAVE 
			else if (Halfday.equalsIgnoreCase("1st Half") ){
				//LogInH =14;
				Halfday ="In 1st Half LEAVE";

				if(netH<4 && netM>=0  ){ 
					deductionHrs=4;
					logger.debug("in ******   1st Half  ******HALF" + deductionHrs);  
					logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n   worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
				}


				else if(LogOutH==policyLogOutH && LogOutM>=30) {

					logger.debug("  login ....logouted  CORRECTLY");

					//  logger.debug(" worked hours = " + workedHrs   );
				}
				else{
					if(LogOutH>policyLogOutH && LogOutM>=00) {

						logger.debug("  login ....logouted  CORRECTLY .........But spent more time in office");

						//  logger.debug(" worked hours = " + workedHrs   );
					}
				}


			}

			// SECOND HALF_DAY LEAVE 
			else if (Halfday.equalsIgnoreCase("2nd Half")){ 

				//LogInH =9;
				Halfday ="In 2nd Half LEAVE";


				if((netH>=4 && netM>=0) && (LogInH==9 && login_9_10_counter<=3 && LogInM<=10 && login_9_10_counter>0) || LogInH<=9 ){ //8:31   ........|| netH==9 && netM==00 
					deductionHrs=0;
					logger.debug("in ******   SECOND  ******HALF" + deductionHrs);  
					logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n   worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
				}
				if((netH>=4 && netM>=0) && (LogInH==9 && login_9_10_counter>3 && LogInM<45 && login_9_10_counter>0) ){ //8:31   ........|| netH==9 && netM==00 
					deductionHrs=1;
					logger.debug("in ******   SECOND  ******HALF" + deductionHrs);  
					logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n   worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
				}
				else{
					deductionHrs=4;
				}


			}




			else if (logIn.equalsIgnoreCase(logOut)&& logIn!="00:00:00" && logOut!="00:00:00" && workedHrs.equalsIgnoreCase("00:00:00") ){  //logIn , logOut, workedHrs
				deductionHrs=4;
				logger.debug("in ******   IN=OUT and net 00 " + deductionHrs);  
			}	

			total_ded_hrs= String.valueOf(deductionHrs)+":"+ String.valueOf(deductionMts);


		}catch(Exception ex){
			ex.printStackTrace();
		}
		return total_ded_hrs;
	}
	
	
}
