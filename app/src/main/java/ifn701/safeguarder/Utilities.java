package ifn701.safeguarder;

import android.content.Context;

import java.util.concurrent.TimeUnit;

/**
 * Created by lua on 30/09/2015.
 */
public class Utilities {
    /**
     * if the day in formatMillisecondToInterval method exceeds MAX_DAY then the returned string
     * will be "many days ago"
     */
    public static final long MAX_DAY = 30;

    Context context;

    public Utilities(Context context) {
        this.context = context;
    }

    /**
     * Format millisecond into string of interval. For example the converted String will be like
     * 3 hours ago, or 20 mins ago, or just now.
     * @param millisecond
     * @return
     */
    public String formatMillisecondToInterval(long millisecond) {
        long day = TimeUnit.MILLISECONDS.toDays(millisecond);
        String str = "";
        if(day > MAX_DAY) {
            return context.getString(R.string.many_days_ago);
        } else {
            if (day > 0) {
                if(day == 1) {
                    str += context.getString(R.string.yesterday);
                } else {
                    str = day + " " + context.getString(R.string.days_ago);
                }
            } else {
                long hour = TimeUnit.MILLISECONDS.toHours(millisecond);
                if(hour > 0) {
                    str = hour + " ";
                    if (hour == 1) {
                        str += context.getString(R.string.hour_ago);
                    } else {
                        str += context.getString(R.string.hours_ago);
                    }
                } else {
                    long min = TimeUnit.MILLISECONDS.toMinutes(millisecond);
                    if(min > 0) {
                        str = min + " ";
                        if(min == 1) {
                            str += context.getString(R.string.minute_ago);
                        } else {
                            str += context.getString(R.string.minutes_ago);
                        }
                    } else {
                        str = context.getString(R.string.just_now);
                    }
                }
            }
        }

        return str;
    }
}
