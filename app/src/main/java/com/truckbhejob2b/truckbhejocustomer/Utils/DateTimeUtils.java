package com.truckbhejob2b.truckbhejocustomer.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    public static final DateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public static final DateFormat homeCaseDateFormat = new SimpleDateFormat("dd-MM-yyyy, E", Locale.ENGLISH);
    public static final DateFormat appDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    // To Display Date in App Format
    public static String getDateToDisplay(String serverDate) {

        String outputDateStr = "";
        try {
            Date date = serverDateFormat.parse(serverDate);
            outputDateStr = appDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputDateStr;
    }


    // To Display Date in App Format
    public static String getHomeDate(String serverDate) {

        String outputDateStr = "";
        try {
            Date date = serverDateFormat.parse(serverDate);
            outputDateStr = homeCaseDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputDateStr;
    }

    public static String getDaysToGo(String serverDate) {

        try {
            Date targetDate = serverDateFormat.parse(serverDate);
            Date currentDate = serverDateFormat.parse(serverDateFormat.format(Calendar.getInstance().getTime()));
            int i = currentDate.compareTo(targetDate);
            if (i < 0) {
                if (i == -1)
                    return getDateDifference(currentDate, targetDate, "%1$s to go");
                else
                    return getDateDifference(currentDate, targetDate, "%1$s to go");
            } else if (i > 0) {
                if (i == 1)
                    return getDateDifference(targetDate, currentDate, "%1$s ago");
                else
                    return getDateDifference(targetDate, currentDate, "%1$s ago");
            } else {
                return "Today";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    private static String getDateDifference(Date startDate, Date endDate, String str) {

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

    /*    long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;*/

        if (elapsedDays == 1)
            return String.format(str, elapsedDays + " Day");
        else
            return String.format(str, elapsedDays + " Days");

    }


    public static String getDateToReschedule(String serverDate) {

        String outputDateStr = "";
        try {
            Date date = serverDateFormat.parse(serverDate);
            outputDateStr = serverDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputDateStr;
    }
}
