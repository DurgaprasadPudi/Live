package com.hetero.heteroiconnect.classes;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

public class Attenance {
	 
	@PersistenceContext
	EntityManager entityManager;
	
	 static final Logger logger = Logger.getLogger(Attenance.class);
	
	@SuppressWarnings("unused")
	public static double  checkingAttendance(String logIn , String logOut, String workedHrs ,String Halfday,String DATEON,String DAYSTATUS){
		
		
		//System.out.println(logOut+"---->"+workedHrs);


		String []LogINTime=null ; 
		StringBuffer SQL_INSERT = new StringBuffer();
		String []LogOUTTime=null , netHrs=null;
		String NetFlexiHrs ,Leaveon   ;
		int LogInH=0,LogInM=0, LogInSec=0, LogOutH=0,LogOutM=0 , LogOutSec=0, netH=0,netM=0, netSec=0,  totalWorkHrs=9 , diffHrs=0 ,diffMints=0 ;
		double deductionHrs=0.0;


		try{



			LogINTime=logIn.split(":");
			LogOUTTime=logOut.split(":");
			netHrs=workedHrs.split(":");

			LogInH=Integer.parseInt(LogINTime[0]);
			LogInM=Integer.parseInt(LogINTime[1]);
			LogInSec=Integer.parseInt(LogINTime[2]);
			LogOutH=Integer.parseInt(LogOUTTime[0]);
			LogOutM=Integer.parseInt(LogOUTTime[1]);
			netH=Integer.parseInt(netHrs[0]);
			netM=Integer.parseInt(netHrs[1]);
			
			
			
			
			
			//// 8 to 5 effective from 
			
			  LocalDate eight_to_five_effective_from = LocalDate.parse("2024-08-05");
			  LocalDate ParseDate_eight_to_five = LocalDate.parse(DATEON);
			  
			  int compareValue_eight_to_five= eight_to_five_effective_from.compareTo(ParseDate_eight_to_five);
			  
			
			/// 8:30 to 5:30 effective  from 
			
			  LocalDate today = LocalDate.parse("2022-12-05");
			  LocalDate pastDate = LocalDate.parse(DATEON);
			  
			  int compareValue = today.compareTo(pastDate);
			  
			  
			  
			  
			  
			  // System.out.println("compareValue"+compareValue);


//			  if (compareValue > 0) {   
//			    //System.out.println("below Dates");
//			  } else if (compareValue < 0||compareValue==0) {
//			    //System.out.println("above Dates || Equals");
//			  }     
 
			//  logger.debug(" login hour.... "+LogInH 	+" \n login minute "+LogInM +" \n logout hr "+LogOutH +" \n login minute "+LogOutM);

//			if(LogInH==0 && LogInM==0 && LogOutH==0){
//				logger.debug("holiday");  
			  
//			} 
			  
			  //// 8 to 5  Policy
			  
			  if(compareValue_eight_to_five <= 0)
			  {

				        
				  System.err.println(DATEON+"DATE"+LogInH+"<---LogInH-***-LogInM--->"+LogInM+"LogOutH-->"+LogOutH+"LogOutM---->"+LogOutM+"----"+DAYSTATUS);
				  
				  		 
				     
				  		//// NEW CODE 
				         
				         LocalTime intime = LocalTime.of(LogInH, LogInM);
					  		LocalTime outtime = LocalTime.of(LogOutH, LogOutM);
					  		//Duration totalDuration = Duration.between(intime, outtime);
					  		
					  		double daystatus =0;
					  		if("1st Half".equals(DAYSTATUS)||"2nd Half".equals(DAYSTATUS))
					  		{
					  			 daystatus = 0.5;
					  		}
					  		else  
					  		{
					  			daystatus = 1;
					  		}
				         
				        // LocalTime intime = LocalTime.of(LogInH, LogInM);
				        // LocalTime outtime = LocalTime.of(LogOutH, LogOutM);
				        // double daystatus = 1.0; // full day 1.0 and halfday 0.5
				         String totalHoursandmins = "0:0";
				         double after = 0.0;
				         
				         if (!(intime.equals(LocalTime.MIDNIGHT) || outtime.equals(LocalTime.MIDNIGHT))) {
				             Duration totalDuration = Duration.between(intime, outtime);
				             long totalhours = Math.abs(totalDuration.toHours());
				             long totalminutes = totalDuration.toMinutes() % 60; // Calculate minutes part
				             totalHoursandmins = String.format("%d:%02d", totalhours, totalminutes);

				             double startTime = intime.getHour();
				             double endTime = intime.getMinute();
				             after = startTime + (endTime / 100.0);
				         }
				         System.err.println("Intime:" + intime + ",Outtime:" + outtime + ",Totalduration:" + totalHoursandmins+"after"+after);
				         
				         
				         double totalNetHours=0.0;
				         /////  Effective 27th August 2024 ---> 9 Hours Condition added
				          
				         
				         LocalDate eight_to_five_effective = LocalDate.parse("2024-08-27");
						  LocalDate ParseDate_eight = LocalDate.parse(DATEON);
						  
						  int compareValueEffectiveFromNineHours= eight_to_five_effective.compareTo(ParseDate_eight);
						  
						  
						  if (compareValueEffectiveFromNineHours <= 0) {
							    // If the date is on or after 27th August 2024, call the 9 Hours Method
							    totalNetHours = calculateTotalNetHoursSecound(intime, outtime, daystatus, after);
							  
							    System.out.println(totalNetHours+"call the 9 Hours Method");
						  
						  } else {
							    // If the date is before 27th August 2024, call the 8:51 Hours Method
							    totalNetHours = calculateTotalNetHours(intime, outtime, daystatus, after);
							
							    System.out.println(totalNetHours+"call the 8:51 Hours Method");
						  }
				         
				        
				         
				         System.out.println(totalNetHours+"totalNetHours PRASAD");

				        // double deductions = 0.0;

				         // If intime or outtime is 00:00, give 8 hours deductions
				         if (daystatus >= 1.0 && (intime.equals(LocalTime.MIDNIGHT) || outtime.equals(LocalTime.MIDNIGHT))) {
				        	 deductionHrs = 8.0;
				         } else {
				             // Half day condition
				             if (daystatus < 1.0 && totalNetHours >= 4.3) {
				            	 deductionHrs = 0;
				             } else if (daystatus < 1.0 && totalNetHours < 4.3) {
				            	 deductionHrs = 4;
				             }

				             // Full day Condition
				             if (daystatus >= 1.0 && totalNetHours >= 9 && after <= 10.0) {
				            	 deductionHrs = 0;
				             } else if (daystatus >= 1.0 && totalNetHours < 4.3 && after <= 10.0) {
				            	 deductionHrs = 8;
				             } else if (daystatus >= 1.0 && totalNetHours >= 4.3 && after <= 10.0) {
				                 if (totalNetHours >= 5.0 && totalNetHours < 7.0) {
				                	 deductionHrs = 4;
				                 } else {
				                	 deductionHrs = 9 - totalNetHours;
				                 }
				             } else if (daystatus >= 1.0 && totalNetHours >= 4.3 && after > 10.0) {
				            	 deductionHrs = 4;
				             } else if (daystatus >= 1.0 && totalNetHours < 4.3 && after > 10.0) {
				            	 deductionHrs = 8;
				             }
				         }

				  		
				  		System.err.println("Total Net Hours: " + totalNetHours + ", Deductions: " + deductionHrs+"daystatus:"+daystatus);

			  }
//			 
			  
			  /// 8:30 to 5:30 :
			
			  else if( (compareValue < 0||compareValue==0)&&((LogInH<8 && LogInM>=0)||(LogInH==8 && LogInM<=29))  && Halfday.equalsIgnoreCase("WDAY") && netH>0){
				
				System.out.println("NEW POLICY");
				LogInH =8;
				LogInM=30;
				logger.debug(" CHECKING WORKING HOURS....!!!!!!! ");
				System.out.println(LogOutH+"LogOutH<--%%%%%--->LogOutM"+LogOutM);
				//if((LogOutH==17 && LogOutM<30 )|| (LogOutH<17 && LogOutM>=00)){
					
				 
				if(LogOutH>=17 && LogOutM>=30||LogOutH>=18 && LogOutM>=00) {

					logger.debug(" early login ....logouted  CORRECTLY");
					
					System.out.println("early login ....logouted  CORRECTLY");

					//  logger.debug(" worked hours = " + workedHrs   );
				}
				
				else{

					//System.out.println("1::::::");

//					logger.debug(" early login ....logouted  early");  //totalWorkHrs ,deductionHrs
//					if(netM==17 && netM<30  ){ //8:31   ........|| netH==9 && netM==00 
//						deductionHrs=1;
//					 
//						logger.debug(logIn+ "  LOGIN....!!!!!!!!!!! worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
//						  
//					}
//					else if(netM==16 && netM>30 ){  //8:30
//						deductionHrs=2;
//						logger.debug( logIn+ "IN EARLY LOGIN....!!!!!!!!!!!  worked hours = " + logOut   +"------ deducted hours are..." + deductionHrs);  
//					} 
//					else if(LogOutH==16 && LogOutM<30 ||LogOutH<16 && LogOutM>=0){// above 7 hrs
//						deductionHrs=4;
//						logger.debug( logIn+ " worked hours = " + logOut   +"------ deducted hours are...." + deductionHrs);  
//					}  
					
					logger.debug(" early login ....logouted  early");  //totalWorkHrs ,deductionHrs
				//	System.out.println(netH+"netH<--%%%%%--->netM"+netM);
					
					if(netH>=8 && netM>30||netH>=9 ){ //8:31   ........|| netH==9 && netM==00 
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
					
					//System.out.println(deductionHrs+"deductionHrs");
				 

				}
			 
				
				/////Backup
				
		/*		
				
				
if(LogOutH<=17 && LogOutM<30){
					
					//System.out.println("1::::::");

//					logger.debug(" early login ....logouted  early");  //totalWorkHrs ,deductionHrs
//					if(netM==17 && netM<30  ){ //8:31   ........|| netH==9 && netM==00 
//						deductionHrs=1;
//					 
//						logger.debug(logIn+ "  LOGIN....!!!!!!!!!!! worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
//						  
//					}
//					else if(netM==16 && netM>30 ){  //8:30
//						deductionHrs=2;
//						logger.debug( logIn+ "IN EARLY LOGIN....!!!!!!!!!!!  worked hours = " + logOut   +"------ deducted hours are..." + deductionHrs);  
//					} 
//					else if(LogOutH==16 && LogOutM<30 ||LogOutH<16 && LogOutM>=0){// above 7 hrs
//						deductionHrs=4;
//						logger.debug( logIn+ " worked hours = " + logOut   +"------ deducted hours are...." + deductionHrs);  
//					}  
					
					logger.debug(" early login ....logouted  early");  //totalWorkHrs ,deductionHrs
				//	System.out.println(netH+"netH<--%%%%%--->netM"+netM);
					
					if(netH>=8 && netM>30||netH>=9 ){ //8:31   ........|| netH==9 && netM==00 
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
					
					//System.out.println(deductionHrs+"deductionHrs");
					  
				 
					
				}
				// CORRECTLY LOGOUT
				else{

					if(LogOutH>=17 && LogOutM>=30||LogOutH>18 && LogOutM>=00) {

						logger.debug(" early login ....logouted  CORRECTLY");

						//  logger.debug(" worked hours = " + workedHrs   );
					}

				}   */
				

			}
			
			
		///// OLD --Policy----///
			// from below 9:00 
			else if(((LogInH<9 && LogInM>=0)||(LogInH==9 && LogInM==0))  && Halfday.equalsIgnoreCase("WDAY") && netH>0){
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
			else if(((LogInH==9 && LogInM<45) || (LogInH==9 && LogInM==45 && LogInSec==0))&& Halfday.equalsIgnoreCase("WDAY") && netH>0 ){

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

			//else if( Halfday.equalsIgnoreCase("WDAY") && ((LogInH==9 &&  LogInM==46 && LogInSec>=0 )|| (LogInH==10 && LogInM==00) || (LogInH==9 &&  LogInM>45)) && netH>0 ){ 
			
			//   ******************* changed on 07-08-2021 ************
			else if( Halfday.equalsIgnoreCase("WDAY") && ((LogInH==9 &&  LogInM==45 && LogInSec>0 )|| (LogInH==10 && LogInM==00) || (LogInH==9 &&  LogInM>45)) && netH>0 ){ 
							
			
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

				if(LogInH<=9) {
				LogInH =9;
				LogInM=0;
				}
				
				
				Halfday ="In 2nd Half LEAVE";
				
			//	if(netH>=4 && netM>=0 && LogInH==9 && LogInM<45 && (LogOutM<=45 || LogOutM>=45)) { //8:31   ........|| netH==9 && netM==00 
					if(netH>=4 && netM>=0 && ((LogInH==9 && LogInM<45) || (LogInH==9 && LogInM==45 && LogInSec==0))) { 
					//
					deductionHrs=0;
					logger.debug("in ******   SECOND  ******HALF" + deductionHrs);  
					logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n   worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
				}else if((netH>=4 && netM>=0) && ((LogInH==9 &&  LogInM==45 && LogInSec>0 )|| (LogInH==10 && LogInM==00) || (LogInH==9 &&  LogInM>45 ))){
				
				//else if((netH>=4 && netM>=0) && (LogInH==9 && LogOutM>45) || (LogInH==10 &&  LogOutM==0) ){
				
				   
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
			
				if( LogInH>=10 && LogInM>0 || LogInH>10 && LogInM>=0){
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
	
	
	
	
	
	@SuppressWarnings("unused")
	public static int  CompositesAttendance(String logIn , String logOut, String workedHrs ,String Halfday,String DATEON){
		
		
		//System.out.println(logOut+"---->"+workedHrs);
 
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
			LogInSec=Integer.parseInt(LogINTime[2]);
			LogOutH=Integer.parseInt(LogOUTTime[0]);
			LogOutM=Integer.parseInt(LogOUTTime[1]);
			netH=Integer.parseInt(netHrs[0]);
			netM=Integer.parseInt(netHrs[1]);
			
			
			
			  LocalDate today = LocalDate.parse("2023-04-24");
			  LocalDate pastDate = LocalDate.parse(DATEON);

			  int compareValue = today.compareTo(pastDate);
			  // System.out.println("compareValue"+compareValue);


			  if (compareValue > 0) {
			    //System.out.println("below Dates");
			  } else if (compareValue < 0||compareValue==0) {
			    //System.out.println("above Dates || Equals");
			  }  
 
			//  logger.debug(" login hour.... "+LogInH 	+" \n login minute "+LogInM +" \n logout hr "+LogOutH +" \n login minute "+LogOutM);

			if(LogInH==0 && LogInM==0 && LogOutH==0){
				logger.debug("holiday");
			}
			 
			
	
			
			
		///// OLD --Policy----///
			// from below 9:00 
			else if(((LogInH<8 && LogInM>=0)||(LogInH==8 && LogInM==0))  && Halfday.equalsIgnoreCase("WDAY") && netH>0){
				LogInH =8;
				LogInM=0;
				logger.debug(" CHECKING WORKING HOURS....!!!!!!! ");
				

					logger.debug(" early login ....logouted  early");  //totalWorkHrs ,deductionHrs
					if(netH<9) 
					{
					if(netH==8 && netM>30 ||netH>=9 ){ //8:31   ........|| netH==9 && netM==00 
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

					if(LogOutH>=17 && LogOutM>=0) {

						logger.debug(" early login ....logouted  CORRECTLY");

						//  logger.debug(" worked hours = " + workedHrs   );
					}

				}

			}
			
			
		// 8:30 Policy 
else if(((LogInH<8 && LogInM>=0)||(LogInH==8 && LogInM<=29))  && Halfday.equalsIgnoreCase("WDAY") && netH>0){
				
				System.out.println("NEW POLICY");
				LogInH =8;
				LogInM=30;
				logger.debug(" CHECKING WORKING HOURS....!!!!!!! ");
				System.out.println(LogOutH+"LogOutH<--%%%%%--->LogOutM"+LogOutM);
				//if((LogOutH==17 && LogOutM<30 )|| (LogOutH<17 && LogOutM>=00)){
					
				 
//				if(LogOutH>=17 && LogOutM>=30||LogOutH>=18 && LogOutM>=00) {
//
//					logger.debug(" early login ....logouted  CORRECTLY");
//					
//					System.out.println("early login ....logouted  CORRECTLY");
//
//
//				}
//				
//				else{

	
					logger.debug(" early login ....logouted  early");  //totalWorkHrs ,deductionHrs
				//	System.out.println(netH+"netH<--%%%%%--->netM"+netM);
					if(netH<9) 
					{
						
					
					if(netH>=8 && netM>30){ //8:31   ........|| netH==9 && netM==00 
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
			//	}
			

			}
			
			
			//FLEXI TIMINGS 8:45
			else if(((LogInH==8 && LogInM<45) || (LogInH==8 && LogInM==45 && LogInSec==0))&& Halfday.equalsIgnoreCase("WDAY") && netH>0 ){

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
					if(LogOutH>=17 && LogOutM>=0) {
						logger.debug(logIn+" ENTERED IN FLEXI TIMINGS \n early login ....CORRECT LOGOUT \n worked hours = " + logOut   );
					} 
				}

			}



			// from 9:45-10:00 ...... ( LogInH==9 &&  LogInM>45 || LogInH<=10 && LogInM<01)

			//else if( Halfday.equalsIgnoreCase("WDAY") && ((LogInH==9 &&  LogInM==46 && LogInSec>=0 )|| (LogInH==10 && LogInM==00) || (LogInH==9 &&  LogInM>45)) && netH>0 ){ 
			
			//   ******************* changed on 07-08-2021 ************
			else if( Halfday.equalsIgnoreCase("WDAY") && ((LogInH==8 &&  LogInM==45 && LogInSec>0 )|| (LogInH==9 && LogInM==00) || (LogInH==8 &&  LogInM>45)) && netH>0 ){ 
							
			
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


					if(LogOutH==17 &&  LogOutM>45 || LogOutH==18 && LogOutM==00){

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
				
				if(LogOutH<=17 && LogOutM>=00){ 
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
					if(LogOutH>=17 &&  LogOutM>=00){

						logger.debug("your leaves will be counted ......for ur 1st half leave");  

					}
				}

			}

			// SECOND HALF_DAY LEAVE 
			else if (Halfday.equalsIgnoreCase("2nd Half")){ 

				if(LogInH<=8) {
				LogInH =8;
				LogInM=0;
				}
				
				
				Halfday ="In 2nd Half LEAVE";
				
			//	if(netH>=4 && netM>=0 && LogInH==9 && LogInM<45 && (LogOutM<=45 || LogOutM>=45)) { //8:31   ........|| netH==9 && netM==00 
					if(netH>=4 && netM>=0 && ((LogInH==8 && LogInM<45) || (LogInH==8 && LogInM==45 && LogInSec==0))) { 
					//
					deductionHrs=0;
					logger.debug("in ******   SECOND  ******HALF" + deductionHrs);  
					logger.debug(logIn+" ENTERED IN ONE or 1 deductionHrs \n   worked hours = " + logOut  +"------ deducted hours are....." + deductionHrs);  
				}else if((netH>=4 && netM>=0) && ((LogInH==8 &&  LogInM==45 && LogInSec>0 )|| (LogInH==9 && LogInM==00) || (LogInH==8 &&  LogInM>45 ))){
				
				//else if((netH>=4 && netM>=0) && (LogInH==9 && LogOutM>45) || (LogInH==10 &&  LogOutM==0) ){
				
				   
					deductionHrs=1;
					logger.debug("in ******   SECOND  ******HALF" + deductionHrs);  
					
				}else{
					deductionHrs=4;
				}
				
				
				if(LogOutH<12 && LogOutM>=00){ 
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
					if(LogOutH>=12 &&  LogOutM>=00){

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
			
				if( LogInH>=9 && LogInM>0 || LogInH>9 && LogInM>=0){
					LogInH =13;
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
	


	public static String AhmecheckingAttendance(String logIn , String logOut, String workedHrs ,String Halfday, int login_9_10_counter){

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
	
	 
	     public static double calculateTotalNetHours(LocalTime intime, LocalTime outtime, double daystatus, double after) {
	         // If intime or outtime is 00:00, return 0 net hours
	         if (intime.equals(LocalTime.MIDNIGHT) || outtime.equals(LocalTime.MIDNIGHT)) {
	             return 0.0;
	         }

	         if (after <= 10.0) {
	        	 
	        	 
	        	 System.out.println("CALLING 1000");
	             LocalTime minStartTime = LocalTime.of(8, 0);
	             if (intime.isBefore(minStartTime)) {
	                 intime = minStartTime;
	             }
	             LocalTime minOutTime = LocalTime.of(19, 0);
	             if (outtime.isAfter(minOutTime)) {
	                 outtime = minOutTime;
	             }

	             Duration duration = Duration.between(intime, outtime);
	             long hours = duration.toHours();
	             long minutes = duration.toMinutes() % 60;
	             double netHours = 0.0;

	             
	             if (daystatus >= 1.0) {
	 				if (minutes <= 50) {
	 					netHours = hours;
	 				} else if (minutes > 50) {
	 					netHours = hours + 1.0;
	 				}
	 			} else if (daystatus < 1.0) {
	 				if (minutes <= 30) {
	 					netHours = hours;
	 				} else if (minutes > 30) {
	 					netHours = hours + 1.0;
	 				}
	 			}
	             return netHours;

	         } 
	         
	         else {
	        	 Duration duration = Duration.between(intime, outtime);
                 long hours = duration.toHours();
                 long minutes = duration.toMinutes() % 60;
	 			double netHours = 0.0;
	 			if (hours >= 4.3) {
	 				if (minutes <= 30) {
	 					netHours = hours;
	  
	 				} else {
	 					netHours = hours + 1.0;
	 				}
	  
	 			} else if (hours < 4.3) {
	 				if (minutes <= 30) {
	 					netHours = hours;
	  
	 				} else {
	 					netHours = hours + 1.0;
	 				}
	 			}
	 			return netHours;
	 		}
	 	
	     }
	     
	     
	     public static double calculateTotalNetHoursSecound(LocalTime intime, LocalTime outtime, double daystatus, double after) {
	         // If intime or outtime is 00:00, return 0 net hours
	         if (intime.equals(LocalTime.MIDNIGHT) || outtime.equals(LocalTime.MIDNIGHT)) {
	             return 0.0;
	         }

	         if (after <= 10.0) {
	        	 
	        	 
	        	 System.out.println("CALLING 1000");
	             LocalTime minStartTime = LocalTime.of(8, 0);
	             if (intime.isBefore(minStartTime)) {
	                 intime = minStartTime;
	             }
	             LocalTime minOutTime = LocalTime.of(19, 0);
	             if (outtime.isAfter(minOutTime)) {
	                 outtime = minOutTime;
	             }

	             Duration duration = Duration.between(intime, outtime);
	             long hours = duration.toHours();
	             long minutes = duration.toMinutes() % 60;
	             double netHours = 0.0;

	             
	             if (daystatus >= 1.0) {
	 				if (minutes <= 59) {
	 					netHours = hours;
	 				} else if (minutes > 59) {
	 					netHours = hours + 1.0;
	 				}
	 			} else if (daystatus < 1.0) {
	 				if (minutes <= 30) {
	 					netHours = hours;
	 				} else if (minutes > 30) {
	 					netHours = hours + 1.0;
	 				}
	 			}
	             return netHours;

	         } 
	         
	         else {
	        	 Duration duration = Duration.between(intime, outtime);
                 long hours = duration.toHours();
                 long minutes = duration.toMinutes() % 60;
	 			double netHours = 0.0;
	 			if (hours >= 4.3) {
	 				if (minutes <= 30) {
	 					netHours = hours;
	  
	 				} else {
	 					netHours = hours + 1.0;
	 				}
	  
	 			} else if (hours < 4.3) {
	 				if (minutes <= 30) {
	 					netHours = hours;
	  
	 				} else {
	 					netHours = hours + 1.0;
	 				}
	 			}
	 			return netHours;
	 		}
	 	
	     }
	     
	     
	     
	 }

	
	

