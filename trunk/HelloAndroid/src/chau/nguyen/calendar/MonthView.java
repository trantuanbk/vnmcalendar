package chau.nguyen.calendar;


// -----------------------------------------------------------------------------
//MonthView.java
//-----------------------------------------------------------------------------

/*
* =============================================================================
* Copyright (c) 1998-2005 Jeffrey M. Hunter. All rights reserved.
*
* All source code and material located at the Internet address of
* http://www.idevelopment.info is the copyright of Jeffrey M. Hunter, 2005 and
* is protected under copyright laws of the United States. This source code may
* not be hosted on any other site without my express, prior, written
* permission. Application to host any of the material elsewhere can be made by
* contacting me at jhunter@idevelopment.info.
*
* I have made every effort and taken great care in making sure that the source
* code and other content included on my web site is technically accurate, but I
* disclaim any and all responsibility for any loss, damage or destruction of
* data or any other property which may arise from relying on it. I will in no
* case be liable for any monetary damages arising from such loss, damage or
* destruction.
*
* As with any code, ensure to test this code in a development environment
* before attempting to run it in production.
* =============================================================================
*/

import java.util.*;
import java.text.*;

/**
* -----------------------------------------------------------------------------
* Used to provide an example of several Date features used to produce a
* convenient view of a month in compact form.
*
* @version 1.0
* @author  Jeffrey M. Hunter  (jhunter@idevelopment.info)
* @author  http://www.idevelopment.info
* -----------------------------------------------------------------------------
*/

public class MonthView {

   /** List names of the month */
   public final static String[] months = {
       "January" , "February" , "March",
       "April"   , "May"      , "June",
       "July"    , "August"   , "September",
       "October" , "November" , "December"
   };


   /** List the days in each month */
   public final static int dom[] = {
       31, 28, 31,  /* jan, feb, mar */
       30, 31, 30,  /* apr, may, jun */
       31, 31, 30,  /* jul, aug, sep */
       31, 30, 31   /* oct, nov, dec */
   };


   /**
    * Helper utility used to print
    * a String to STDOUT.
    * @param s String that will be printed to STDOUT.
    */
   private void printMonth(int mm, int yy) {

       // The number of days to leave blank at
       // the start of this month.
       int leadSpaces = 0;

       System.out.println();
       System.out.println("  " + months[mm] + " " + yy);

       if (mm < 0 || mm > 11) {
           throw new IllegalArgumentException(
               "Month " + mm + " bad, must be 0-11");
       }

       GregorianCalendar cal = new GregorianCalendar(yy, mm, 1);

       System.out.println("Su Mo Tu We Th Fr Sa");

       // Compute how much to leave before before the first day of the month.
       // getDay() returns 0 for Sunday.
       leadSpaces = cal.get(Calendar.DAY_OF_WEEK)-1;

       int daysInMonth = dom[mm];

       if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
           ++daysInMonth;
       }

       // Blank out the labels before 1st day of the month
       for (int i = 0; i < leadSpaces; i++) {
           System.out.print("   ");
       }

       for (int i = 1; i <= daysInMonth; i++) {

           // This "if" statement is simpler than messing with NumberFormat
           if (i<=9) {
               System.out.print(" ");
           }
           System.out.print(i);

           if ((leadSpaces + i) % 7 == 0) { // Wrap if EOL
               System.out.println();
           } else {
               System.out.print(" ");
           }

       }
       System.out.println();
   }



   /**
    * Sole entry point to the class and application.
    * @param args Array of String arguments.
    */
   public static void main(String[] args) {

       int month, year;

       MonthView mv = new MonthView();

       if (args.length == 2) {
           mv.printMonth(Integer.parseInt(args[0])-1, Integer.parseInt(args[1]));
       } else {
           Calendar today = Calendar.getInstance();
           mv.printMonth(today.get(Calendar.MONTH), today.get(Calendar.YEAR));
       }

   }

}