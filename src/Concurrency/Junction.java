package Concurrency;

import java.text.SimpleDateFormat;
import java.util.Map;
import Enum.Compass;

/**
 * Created by Ashley Morris on 18/12/2014.
 */
public class Junction extends Thread {

    private Road inNorth, inEast, inSouth, inWest; //The roads representing the way into this junction
    private Road outNorth, outEast, outSouth, outWest;//The roads representing the way out of this junction

    private int id;
    private int carsThrough = 0;/*A simple counter that counts how many cars
                             are passed through a junction while it is on green, reset to 0 when red.*/

    private Clock clock;
    SimpleDateFormat minutesSeconds = new SimpleDateFormat("mmmsss");

    //Stores a list of destinations and their associated exit routes from this junction.
    private Map<Integer, String> destinationList;


    public Junction(Clock clock, int id, Map destinationList) {
        this.clock = clock;
        this.id = id;
        this.destinationList = destinationList;
    }


    /**
     * Sets the Roads that lead into this junction, all parameters are Roads from different directions.
     */
    public void setInRoads(Road inNorth, Road inEast, Road inSouth, Road inWest) {
        this.inNorth = inNorth;
        if (this.inNorth != null) {
            this.inNorth.setFromDirection("North");
        }

        this.inEast = inEast;
        if (this.inEast != null) {
            this.inEast.setFromDirection("East");
        }

        this.inSouth = inSouth;
        if (this.inSouth != null) {
            this.inSouth.setFromDirection("South");
        }

        this.inWest = inWest;
        if (this.inWest != null) {
            this.inWest.setFromDirection("West");
        }
    }


    /**
     * Sets the Roads that lead out of this junction all parameters are Roads to different directions.
     */
    public void setOutRoads(Road outNorth, Road outEast, Road outSouth, Road outWest) {
        this.outNorth = outNorth;
        this.outEast = outEast;
        this.outSouth = outSouth;
        this.outWest = outWest;
    }


    //==================================================================================================================

    /**
     * @param inRoad    The Road leading into this junction where the lights are currently green.
     * @param timeStamp The time in milliseconds representing when the time the lights on this junction turned green for
     *                  this section of road in.
     * @return True if the light is still green (run again) or Red if false (move on to next route)
     */
    public boolean isGreen(Road inRoad, Long timeStamp, int duration) {

        Road exitRoad;
        Vehicle car;

        //Polling to check if a car is available:
        //Keep checking to see if a car is available, if not then sleep.
        //Upon waking check if this light should still be green. Before testing the loop condition again.
        while (!inRoad.isAvailable()) {
            try {
                sleep(500);
            } catch (InterruptedException e) {
            }
            if (clock.timerCompleted(timeStamp, duration)) {
                //Lights have changed!
                System.out.println("Time:" + minutesSeconds.format(clock.getTimeStamp()) + " - Junction " + id + ": "
                        + carsThrough + " cars through from " + inRoad.getFromDirection() + "," + inRoad.getWaiting() +
                        " cars waiting.");
                return false;
            }
        }

        //Ask the next car its destination, provides the cars destination integer and returns a road.
        exitRoad = getExitRoute(inRoad.getNextDestination());

        //If there is no space on the exit road then there will be gridlock! Display message:
        if (!exitRoad.isSpace()) {
            System.out.println("Time:" + minutesSeconds.format(clock.getTimeStamp()) + " - Junction " + id + ": "
                    + carsThrough + " cars through from " + inRoad.getFromDirection() + "," + inRoad.getWaiting() +
                    " cars waiting. GRIDLOCK");
        }

        //Keep checking to see if the road ahead clears.
        while (!exitRoad.isSpace()) {
            try {
                sleep(500);
            } catch (InterruptedException e) {
            }
            if (clock.timerCompleted(timeStamp, duration)) {
                //Lights have changed!
                System.out.println("Time:" + minutesSeconds.format(clock.getTimeStamp()) + " - Junction " + id + ": "
                        + carsThrough + " cars through from " + inRoad.getFromDirection() + "," + inRoad.getWaiting() +
                        " cars waiting.");

                return false;
            }
        }

        //If it has made it this far then it is ok to proceed.
        car = inRoad.extract();
        exitRoad.insert(car);
        carsThrough++;

        //Sleep to represent cars moving over the junction.
        try {
            sleep(5000);//5 second sleep. Maximum rate of 12 per minute (60/12 = 5)
        } catch (InterruptedException iex) {

        }


        //Final check to see if lights are still on green or not:
        if (!clock.timerCompleted(timeStamp, duration)) {
            //Timer hasn't completed the lights are still green:
            return true;
        } else {
            System.out.println("Time:" + minutesSeconds.format(clock.getTimeStamp()) + " - Junction " + id + ": "
                    + carsThrough + " cars through from " + inRoad.getFromDirection() + "," + inRoad.getWaiting() +
                    " cars waiting.");
            return false;
        }
    }

    public Road getExitRoute(int destination){

        String exitDirection = destinationList.get(destination);
        exitDirection = exitDirection.toUpperCase(); 

        try {
            Compass direction = Compass.valueOf(exitDirection);
            switch (direction) {
                case NORTH:
                    return outNorth;
                case EAST:
                    return outEast;
                case SOUTH:
                    return outSouth;
                case WEST:
                    return outWest;
            }
        }
        //ValueOf throws IllegalArgumentException if passed a string that isn't a valid member.
        catch (IllegalArgumentException ie){
            System.out.println("Invalid compass direction!");
        }

        //default return null
        return null;
    }


    public void run() {

        //while simulation is running:

        //while "green" isGreen 1{

    }


}
