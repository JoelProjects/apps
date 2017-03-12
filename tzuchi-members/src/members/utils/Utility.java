package members.utils;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class contains common methods will be used.
 * 
 * @author Cheng-Hung Chou
 * @since 3/1/2005
 */
public class Utility
{
   public static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
   public static String US_DATE_PATTERN = "MM/dd/yyyy";
   public static String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
   public static String DEFAULT_DOUBLE_PATTERN = "###,##0.00";
   public static String DEFAULT_INTEGER_PATTERN = "###,###";
   
   /**
    * Checks if a string is empty (null or of size 0 after removing white space
    * from both ends).
    *
    * @param str the string to be checked
    * @return true if it's empty
    */
   public static boolean isEmpty(String str)
   {
      if(str == null || str.trim().length() == 0)
         return true;
      else
         return false;
   }

   /**
    * Trims a string. If it's empty, empty string will be returned.
    *
    * @param str the string to be trimmed
    * @return trimmed string
    */
   public static String trim(String str)
   {
      if(isEmpty(str))
         return "";
      else
         return str.trim();
   }

   static public String getDateTimeString(Date d)
   {
      SimpleDateFormat formatter = new SimpleDateFormat();
      formatter.applyPattern(DEFAULT_DATE_TIME_PATTERN);
      return formatter.format(d);
   }

   static public String getDateString(Date d)
   {
      SimpleDateFormat formatter = new SimpleDateFormat();
      formatter.applyPattern(DEFAULT_DATE_PATTERN);
      return formatter.format(d);
   }

   /**
    * Returns a string with current date and time information with format
    * like yyyyMMddHHmmssSSS.
    *
    * @return a date time string
    */
   public static String getLongDateTimeStr()
   {
      return dateFormatter("yyyyMMddHHmmssSSS");
   }

   /**
    * Returns a string with current date information with a specified format.
    *
    * @param pattern a pattern to the date format
    * @return a date string
    */
   public static String dateFormatter(String pattern)
   {
      SimpleDateFormat formatter = new SimpleDateFormat(pattern);
      Date date = new Date();
      return formatter.format(date);
   }
   
   public static Date dateParser(String dateStr, String pattern) throws ParseException
   {
      SimpleDateFormat formatter = new SimpleDateFormat(pattern);
      return formatter.parse(dateStr);
   }   

   public static Date dateParser(String dateStr) throws ParseException
   {
      Date date = null;
      try
      {
         SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
         formatter.setLenient(true);
         date = formatter.parse(dateStr);
      }
      catch(ParseException ex1)
      {
         try
         {
            SimpleDateFormat formatter = new SimpleDateFormat(US_DATE_PATTERN);
            formatter.setLenient(true);
            date = formatter.parse(dateStr);
         } 
         catch(ParseException ex2)
         {
            throw ex2;
         }
      }
      
      if(date != null)
      {
         Calendar cal = Calendar.getInstance();
         cal.setTime(date);
         if(cal.get(Calendar.YEAR) < 1000)
            throw new ParseException("Year should be 4 digits.", 1);
      }
      
      return date;
   }
   
   public static String numberFormatter(double num)
   {
      DecimalFormat formatter = new DecimalFormat(DEFAULT_DOUBLE_PATTERN);
      return formatter.format(num);
   }  
   
   public static String numberFormatter(int num)
   {
      DecimalFormat formatter = new DecimalFormat(DEFAULT_INTEGER_PATTERN);
      return formatter.format(num);
   }    
   
   public static String big5ToUnicode(String big5)
   {
      String unicode = "";

      if(!isEmpty(big5))
      {
         try
         {
            unicode = new String(big5.getBytes("ISO-8859-1"), "BIG5");
         }
         catch(java.io.UnsupportedEncodingException e)
         {
            System.out.println(e);
         }
      }

      return unicode;
   }

   public static String digestPassword(String password)
   {
      if(isEmpty(password))
         return password;
      
      String digest = null;
      try
      {
         // use message digest to encrypt password and the algorithm is MD5
         MessageDigest md = (MessageDigest)MessageDigest.getInstance("MD5")
               .clone();
         md.update(password.getBytes());
         byte[] byteDigest = md.digest();
         StringBuffer strBuffer = new StringBuffer(byteDigest.length * 2);
         // convert to hex
         for(int i = 0; i < byteDigest.length; i++)
         {
            strBuffer.append(convertDigit(byteDigest[i] >> 4));
            strBuffer.append(convertDigit(byteDigest[i] & 0xf));
         }

         digest = strBuffer.toString();
      }
      catch(Exception e)
      {
         System.out.println(e);
      }

      return digest;
   }

   private static char convertDigit(int i)
   {
      i &= 0xf;
      if(i >= 10)
         return (char)((i - 10) + 97);
      else
         return (char)(i + 48);
   }
}
