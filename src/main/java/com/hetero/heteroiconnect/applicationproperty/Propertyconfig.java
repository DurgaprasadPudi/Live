package com.hetero.heteroiconnect.applicationproperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app") // prefix app, find app.* values
public class Propertyconfig {
    private static   String profileimage;
    private static   String BankDetails;
    private static   String PANDetails;
    private static   String AadharDetails;
    private static   String HR_DOCUMENTS_PDF;
    
    
   


//    private static String databaseUrl;
//    private static String dbUserName;
//    private static String dbPassword;
//    private static String databaseName;

    @Autowired
    public Propertyconfig(@Value("${profile_image_path}") String profileimage,@Value("${BankDetails}") String BankDetails,@Value("${PANDetails}") String PANDetails,@Value("${AadharDetails}") String AadharDetails,@Value("${HR_DOCUMENTS_PDF}") String HR_DOCUMENTS_PDF) {
       
    	  this.profileimage = profileimage;
          this.BankDetails = BankDetails;
          this.PANDetails = PANDetails;
          this.AadharDetails = AadharDetails;
          this.HR_DOCUMENTS_PDF = HR_DOCUMENTS_PDF;
         
        System.out.println(profileimage);
    }
    
   
    public static String getprofileimagepath() {
        return profileimage;
    }
    
    
    public static String getBankDetailspath() {
        return BankDetails;
    }
    
    public static String getPANDetailspath() {
        return PANDetails;
    }
    
    public static String getAadharDetailspath() {
        return AadharDetails;
    }
    
    public static String getHR_DOCUMENTS_PDF() {
        return HR_DOCUMENTS_PDF;
    }
    
}
