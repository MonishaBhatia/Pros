package pros.app.com.pros.base;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Weeks;
import org.joda.time.Years;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static String getDateDifference(String createdAt){

        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date createdAtDate = new Date();
        Date currentDate =  new Date();

        try {
            createdAtDate = format.parse(createdAt);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        DateTime dt1 = new DateTime(createdAtDate);
        DateTime dt2 = new DateTime(currentDate);

        int yearsAgo = Years.yearsBetween(dt1, dt2).getYears();
        int monthsAgo = Months.monthsBetween(dt1, dt2).getMonths();
        int weeksAgo = Weeks.weeksBetween(dt1,dt2).getWeeks();
        int daysAgo = Days.daysBetween(dt1, dt2).getDays();
        int hoursAgo = Hours.hoursBetween(dt1, dt2).getHours() % 24;
        int minutesAgo = Minutes.minutesBetween(dt1, dt2).getMinutes() % 60;
        int secondsAgo = Seconds.secondsBetween(dt1, dt2).getSeconds() % 60;

        if(yearsAgo > 0){
            return yearsAgo+"Y";
        } else if(weeksAgo > 0){
            return weeksAgo+"W";
        } else if(daysAgo > 0){
            return daysAgo+"D";
        } else if (hoursAgo > 0){
            return hoursAgo+"H";
        } else if(minutesAgo > 0){
            return minutesAgo+"M";
        } else if(secondsAgo > 0){
            return secondsAgo+"S";
        }


       /* Log.e("Joda", Days.daysBetween(dt1, dt2).getDays() + " days, ");
        Log.e("Joda", Hours.hoursBetween(dt1, dt2).getHours() % 24 + " hours, ");
        Log.e("Joda", Minutes.minutesBetween(dt1, dt2).getMinutes() % 60 + " minutes, ");
        Log.e("Joda", Seconds.secondsBetween(dt1, dt2).getSeconds() % 60 + " seconds.");

        Log.e("Joda", Weeks.weeksBetween(dt1,dt2).getWeeks() + " weeks");
        Log.e("Joda", Months.monthsBetween(dt1, dt2).getMonths() + " months");
        Log.e("Joda", Years.yearsBetween(dt1, dt2).getYears() + " Years");*/

        return null;
    }
}