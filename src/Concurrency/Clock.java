package Concurrency;

import java.util.Calendar;


/**
 * Created by Ashley Morris on 21/12/2014.
 */
public class Clock extends Thread {

    private static Calendar time;
    private static int hours, mins, secs;

    private CarPark [] carParks;

    private boolean simulation;


    public Clock() {
        //Constructs a clock and sets the time to 12pm.
        time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, 12);
        time.set(Calendar.MINUTE, 0);
        time.set(Calendar.SECOND, 0);
        time.set(Calendar.MILLISECOND, 0);
        simulation = true;
    }


    @Override
    public void run() {
        while (simulation) {

            //Increase the time on the clock object by 5 seconds every 'tick'.
            try {
                time.add(Calendar.SECOND, 1);

                hours = time.get(Calendar.HOUR_OF_DAY);
                mins = time.get(Calendar.MINUTE);
                secs = time.get(Calendar.SECOND);

                if(hours == 13 && mins == 0 && secs == 0){
                    System.out.println("OVER");
                    simulation = false;
                }

                //If the number is divisible by 5 with no remainder must be a multiple of 5 minutes.
                if (mins % 5 ==0 && secs ==0){
                    System.out.println("");
                    System.out.println("Time: " + mins + "m   ");

                    //Check for null or else don't attempt to print any results
                    if(carParks != null) {
                        for (int i = 0; i < carParks.length; i++) {
                            System.out.println(carParks[i].getCarparkName() + ": " + carParks[i].getRemaining() + " Spaces");
                        }
                        System.out.println("");
                    }
                }

                //System.out.println("time is " + hours+ ":" + mins + ":" + secs);

                sleep(200);//Pause of 1/5 a second or else the time will update too fast.

            }
            catch (InterruptedException iex) {

            }
        }
    }


    /**
     * Checks to see if the duration has elapsed between the time stamp and now.
     *
     * @param timeStamp The time in milliseconds to measure from.
     * @param duration The difference in seconds between
     * @return True if the difference is 0. False if not.
     */
    public synchronized boolean timerCompleted(Long timeStamp, int duration){

        Long difference = time.getTimeInMillis() - timeStamp; //Calculate the difference between the two time periods.
        int differenceSecs = (int) (difference / 1000); //Difference in seconds. 1 second = 1000 milliseconds.

        //If seconds difference is equal to the duration then the timer is completed.
        if(differenceSecs < duration){
            return false;
        }
        else{
            return true;
        }
    }


    /**
     *
     * @param carParks
     */
    public void setCarParks(CarPark [] carParks){
        this.carParks = carParks;
    }

    /**
     * Returns the status of the simulation.
     * @return A boolean. True if the simulation is still running. False if not.
     */
    public synchronized boolean simulationRunning(){
        return simulation;
    }


    /**
     * Gets a timestamp of the current clock time in milliseconds.
     *
     * @return A long that represents the current time on the clock in milliseconds.
     */
    public synchronized long getTimeStamp(){

        long timeStamp = time.getTimeInMillis();

        return timeStamp;
    }


    public synchronized int getHours(){
        return hours;
    }
    public synchronized int getMins(){
        return mins;
    }
    public synchronized int getSecs(){
        return secs;
    }



}
