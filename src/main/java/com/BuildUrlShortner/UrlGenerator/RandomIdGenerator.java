package com.BuildUrlShortner.UrlGenerator;

import java.text.ParseException;
import java.util.Date;   
import java.text.SimpleDateFormat;  

public class RandomIdGenerator {
    public String getRandomId(int n)
    {
         String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
         StringBuilder sb = new StringBuilder(n);
    
     for (int i = 0; i < n; i++) {
      int index
       = (int)(AlphaNumericString.length()
         * Math.random());
          sb.append(AlphaNumericString
         .charAt(index));
     }
     return sb.toString();
    }
    
    public Boolean isExpiredCheck(int time,String createdDate){
        Date date = new Date();
       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {   
            // Use parse method to get date object of both dates  
            Date date1 = formatter.parse(createdDate);   
            Date date2 = date;   
               
            long time_difference = date2.getTime() - date1.getTime();  
            
            // Calculate time difference in minutes  
            
            long minutes_difference = (time_difference / (1000*60)) % 60;   
            System.out.print(   
                "Difference "  
                + "between two dates is: ");   
            System.out.println(   
                + minutes_difference   
                + " minutes, "  
                );  
             if(minutes_difference>time){
                return true;
             }
             else{
                return false;
             }
            
        }   
        // Catch parse exception   
        catch (ParseException except) {   
            except.printStackTrace();   
        }
        return null;      
    }
}
