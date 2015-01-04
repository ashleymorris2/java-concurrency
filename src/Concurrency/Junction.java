package Concurrency;

import java.text.SimpleDateFormat;
import java.util.Map;
import Enum.Compass;

/**
 * Created by Ashley Morris on 18/12/2014.
 */
public class Junction extends Thread {

    SimpleDateFormat minutesSeconds = new SimpleDateFormat("m'm's's'");
    private Road inNorth, inEast, inSouth, inWest; //The roads representing the way into this junction
    private Road outNorth, outEast, outSouth, outWest;//The roads representing the way out of this

    private Compass first, second, third, fourth; //The sequence that the lights operate in represented by direction.

    private int id;
    private int carsThrough = 0;/*A simple counter that counts how many cars
                             are passed through a junction while it is on green, reset to 0 when red.*/

    private Clock clock;

    private int northDuration, eastDuration, southDuration, westDuration;

    //Stores a list of destinations and their associated exit routes from this junction.
    // Integer represents the destination, String represents the direction the vehicle has to exit the
    // junction for that destination.
    private Map<Integer, String> destinationList;


    /**constructor
     *
     * @param clock
     * @param id
     * @param destinationList
     */
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


    /**
     * Sets the sequence that the lights operate in, refered to by their direction. Some directions can operate twice,
     * or not at all depending on the configuration.
     *
     * @param first A string:  The direction of the first light in the sequence.
     * @param second
     * @param third
     * @param fourth
     */
    public void setLightsSequence(String first, String second, String third, String fourth){

        String upperCase;

        //First converts the passed string to an upper case value. Then passes this value to the enum class.
        //Allows for null
        //This will store the direction representing it's position in the sequence.
        try {
            if(first != null) {
                upperCase = first.toUpperCase();
                this.first = Compass.valueOf(upperCase);
            }
            if(second!=null) {
                upperCase = second.toUpperCase();
                this.second = Compass.valueOf(upperCase);
            }
            if(third != null) {
                upperCase = third.toUpperCase();
                this.third = Compass.valueOf(upperCase);
            }
            if(fourth != null) {
                upperCase = fourth.toUpperCase();
                this.third = Compass.valueOf(upperCase);
            }
        }
        catch (IllegalArgumentException e) {
            //Catch this exception in main.
            System.out.println("Invalid compass direction!");
            throw new IllegalArgumentException();
        }
    }


    /**
     * Sets the duration that the lights will remain green for, for the different directions.
     *
     */
    public void setNorthDuration(int northDuration) {
        this.northDuration = northDuration;
    }
    public void setEastDuration(int eastDuration) {
        this.eastDuration = eastDuration;
    }
    public void setSouthDuration(int southDuration) {
        this.southDuration = southDuration;
    }
    public void setWestDuration(int westDuration) {
        this.westDuration = westDuration;
    }


    //==================================================================================================================


    /**
     * Runs while this section of in road is green.
     *
     * @param inRoad    The Road leading into this junction where the lights are currently green.
     * @param timeStamp The time in milliseconds representing when the time the lights on this junction turned green for
     *                  this section of road in.
     * @return True if the light is still green (run again) or Red if false (move on to next route)
     */
    private boolean isGreen(Road inRoad, Long timeStamp, int duration) {

        Road exitRoad;
        Vehicle car;

        //Polling to check if a car is available:
        //Keep checking to see if a car is available, if not then sleep.
        //Upon waking check if this light should still be green. Before testing the loop condition again.
        while (!inRoad.isAvailable()) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
            }
            if (clock.timerCompleted(timeStamp, duration)) {
                //Lights have changed!
                System.out.println("Time:" + minutesSeconds.format(clock.getTimeStamp()) + " - Junction " + id + ": "
                        + carsThrough + " cars through from " + inRoad.getFromDirection() + "," + inRoad.getWaiting() +
                        " cars waiting.");
                carsThrough = 0;
                return false;
            }
        }

        //Ask the next car its destination, provides the cars destination integer and returns a road.
        exitRoad = getExitRoute(inRoad.getNextDestination());


        //Check for gridlock and then keep checking to see if the road ahead clears.
        while (!exitRoad.isSpace()) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
            }
            if (clock.timerCompleted(timeStamp, duration)) {

                //Lights have changed! Final report:
                System.out.println("Time: " + minutesSeconds.format(clock.getTimeStamp()) + " - Junction " + id + ": "
                        + carsThrough + " cars through from " + inRoad.getFromDirection() + ", " + inRoad.getWaiting() +
                        " cars waiting. GRIDLOCK");
                carsThrough = 0;
                return false;
            }
        }

        //If it has made it this far then it is ok to proceed.
        car = inRoad.extract();
        exitRoad.insert(car);
        carsThrough++;

        //Sleep to represent cars moving over the junction.
        try {
            sleep(1000);//1 second sleep. Maximum rate of 12 per minute.  (60/12 = 5)
                        // The clock is running in 5 second intervals. 1 second sleep allows the maximum rate of 12 a minute.

        } catch (InterruptedException iex) {

        }


        //Final check to see if lights are still on green or not:
        if (!clock.timerCompleted(timeStamp, duration)) {
            //Timer hasn't completed the lights are still green:
            return true;
        } else {
            //Final report.
            System.out.println("Time:" + minutesSeconds.format(clock.getTimeStamp()) + " - Junction " + id + ": "
                    + carsThrough + " cars through from " + inRoad.getFromDirection() + ", " + inRoad.getWaiting() +
                    " cars waiting.");
            carsThrough = 0;
            return false;
        }
    }

    public void run() {

        //Loop while the simulation is running.
        while (clock.simulationRunning()){

            Road currentRoad; //The current road that we are working with.
            int currentDuration; //Duration that the light will remain green for.
            Long timeStamp; //Represents the time that the loop is first entered.

            //First
            if(first != null) {
            currentRoad = getInRoad(first.toString());
            currentDuration = getDuration(first.toString());
            timeStamp = clock.getTimeStamp();
                System.out.println("Lights 1 entered: " +minutesSeconds.format(timeStamp));
                while (isGreen(currentRoad, timeStamp, currentDuration)) {

                }
            }

            //Second
            if(second != null) {
                currentRoad = getInRoad(second.toString());
                currentDuration = getDuration(second.toString());
                timeStamp = clock.getTimeStamp();
                while (isGreen(currentRoad, timeStamp, currentDuration)) {

                }
            }

            //Third
            if(third != null) {
                currentRoad = getInRoad(third.toString());
                currentDuration = getDuration(third.toString());
                timeStamp = clock.getTimeStamp();
                while (isGreen(currentRoad, timeStamp, currentDuration)) {

                }
            }

            //Fourth
            if(fourth != null) {
                currentRoad = getInRoad(fourth.toString());
                currentDuration = getDuration(fourth.toString());
                timeStamp = clock.getTimeStamp();
                while (isGreen(currentRoad, timeStamp, currentDuration)) {

                }
            }
        }

    }


    /**
     * Takes a destination integer and searches the map for the corresponding exit route. Then returns the Road that
     * represents that exit.
     *
     * @param destination The vehicles final destination. Maps to one of 4 directions out of the junction.
     * @return The exit road that maps to that destination.
     */
    private Road getExitRoute(int destination){

        //Searches the map by the destination.
        String exitDirection = destinationList.get(destination);

        //Convert to uppercase just in case it isn't already.
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
            //Catch in main.
            System.out.println("Invalid compass direction!");
            throw new IllegalArgumentException();
        }

        //default return null
        return null;
    }


    /**
     * Gets the input road for the given direction.
     *
     * @param inputDirection The direction of this junction that the road comes into.
     * @return A Road that comes in from that direction.
     */
    private Road getInRoad(String inputDirection)
    {
        try{
            Compass direction = Compass.valueOf(inputDirection);
            switch (direction) {
                case NORTH:
                    return inNorth;
                case EAST:
                    return inEast;
                case SOUTH:
                    return inSouth;
                case WEST:
                    return inWest;
            }
        }//ValueOf throws IllegalArgumentException if passed a string that isn't a valid member.
        catch (IllegalArgumentException iex){
            //Catch in main.
            System.out.println("Invalid compass direction!");
            throw new IllegalArgumentException();

        }

        //default return null
        return null;
    }

    private int getDuration(String inputDirection)
    {
        try{
            Compass direction = Compass.valueOf(inputDirection);
            switch (direction) {
                case NORTH:
                    return northDuration;
                case EAST:
                    return eastDuration;
                case SOUTH:
                    return southDuration;
                case WEST:
                    return westDuration;
            }
        }//ValueOf throws IllegalArgumentException if passed a string that isn't a valid member.
        catch (IllegalArgumentException iex){
            //Catch in main.
            System.out.println("Invalid compass direction!");
            throw new IllegalArgumentException();

        }

       return 0;
    }

}
