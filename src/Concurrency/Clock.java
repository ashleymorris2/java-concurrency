package Concurrency;

import java.util.Calendar;


/**
 * Created by Ashley Morris on 21/12/2014.

 */
public class Clock extends Thread {

    private Calendar time;

    private int hours, mins, secs;

    public Clock() {
        //Constructs a clock and sets the time to 12pm.
        time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, 12);
        time.set(Calendar.MINUTE, 0);
        time.set(Calendar.SECOND, 0);
    }

    @Override
    public void run() {
        while (hours <= 13) {
            //Increase the time on the clock object by 5 seconds every 'tick' while the hour is less than or equal to 13
            try {
                time.add(Calendar.SECOND, 5);

                hours = time.get(Calendar.HOUR_OF_DAY);
                mins = time.get(Calendar.MINUTE);
                secs = time.get(Calendar.SECOND);

                System.out.println("time is " + hours+ ":" + mins + ":" + secs);

                sleep(1000);//Pause of one second else the time will update too fast.

            } catch (InterruptedException e) {

            }

        }
    }
}
