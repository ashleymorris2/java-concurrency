package Concurrency;

import java.util.Calendar;

/**
 * Created by Ashley Morris on 18/12/2014.
 */
public class Junction extends Thread {

    private Road inNorth, inEast, inSouth, inWest; //The roads representing the way into this junction
    private Road outNorth, outEast, outSouth, outWest;//The roads representing the way out of this junction

    private Clock clock;
    private int id;

    private int sequence[];

    public Junction(Clock clock, int id) {
        this.clock = clock;
        this.id = id;
    }


    /***
     * Sets the Roads that lead into this junction all parameters are Roads from different directions.
     */
    public void setInRoads(Road inNorth, Road inEast, Road inSouth, Road inWest) {
        this.inNorth = inNorth;
        this.inEast = inEast;
        this.inSouth = inSouth;
        this.inWest = inWest;
    }


    /***
     * Sets the Roads that lead out of this junction all parameters are Roads to different directions.
     */
    public void setOutRoads(Road outNorth, Road outEast, Road outSouth, Road outWest){
        this.outNorth = outNorth;
        this.outEast = outEast;
        this.outSouth = outSouth;
        this.outWest = outWest;
    }





    //==================================================================================================================


    /**
     *
     * @param inRoad The Road leading into this junction where the lights are currently green.
     * @param timeStamp The time in milliseconds representing when the lights on this junction turned green for this
     *                  road in.
     *
     * @return
     */
    public boolean consume(Road inRoad, Long timeStamp){
        Calendar timestamp;
        timestamp = Calendar.getInstance();
        timestamp.setTimeInMillis(timeStamp);

        //Keep checking to see if a car is available, if not then sleep.
        // Upon waking check if this light should still be green. Before testing the loop condition again.
        while (!inRoad.isAvailable()){
            try {
                sleep(500);
            } catch (InterruptedException e) {}

            //check lights (return false if lights have changed)
        }

        Vehicle nextCar = inRoad.extract();

        nextCar.getDestination();//route the detination here.

        return false;//light is now red
        }




    public void run() {

        //while "green" consume 1{
        //while
    }


}
