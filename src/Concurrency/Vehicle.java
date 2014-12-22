/**
 * Created by Ashley Morris on 17/12/2014.
 *
 *
 * 1. Concurrency.Vehicle objects are created by Concurrency. EntryPoint objects to simulate the vehicles arriving on the local road
 * network from the motorway junctions etc.
 *
 * 2. The class needs to have attributes which store its destination (one of several, determined when it is
 * created according to configurable ratios), the time it entered Gridlock, and the time it finally
 * parked.
 *
 * 3. As well as constructor and (destructor??) methods, Vehicle must have a method to allow a Junction
 * to read the vehicle’s destination.
 *
 */

package Concurrency;

public class Vehicle {

    private int destination; // An integer representing the vehicles final destination (1-4 for the moment)
    private long timeEntered; //The time in milliseconds representing when the vehicle arrives in the simulation
    private long timeParked; //The time in milliseconds representing when the vehicle parks.

    public Vehicle(int destination, long timeEntered) {

        //The entry point class will set each vehicles destination and the timestamp for when it enters.
        this.destination = destination;
        this.timeEntered = timeEntered;
    }

    public void setTimeParked(long timeParked) {
        this.timeParked = timeParked;
    }

    public int getDestination() {
        return destination;
    }

    public long getTimeEntered() {
        return timeEntered;
    }

    public long getTimeParked() {
        return timeParked;
    }



}
