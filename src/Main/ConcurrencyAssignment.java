package Main;

import Concurrency.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashley Morris on 22/12/2014.
 *
 * Vehicle destinations are:
 *
 *  1: University = 10%
 *  2: Station = 20%
 *  3: Shopping Centre = 30%
 *  4: Industrial Park = 40%
 *
 **/
public class ConcurrencyAssignment {

    public static void main(String[] args) {

        try {
            Map<Integer, String> destinationList1 = new HashMap<Integer, String>();
            destinationList1.put(1, "South");
            destinationList1.put(2, "South");
            destinationList1.put(3, "South");
            destinationList1.put(4, "south");

            Clock myClock = new Clock();
            CarPark [] carParks = new  CarPark[3];

            Road road1 = new Road(50);
            Road outSouth = new Road(50);
            Road outEast = new Road(50);
            Road outWest = new Road(50);
            Road outNorth = new Road(50);

            EntryPoint northEntry = new EntryPoint(road1, "north", 50, myClock);

            Junction junction1 = new Junction(myClock, 1, destinationList1);
            junction1.setInRoads(road1, null, null, null);
            junction1.setOutRoads(outWest, outNorth, outSouth, outEast);
            junction1.setLightsSequence("north", null, null, null);
            junction1.setNorthDuration(60);

            carParks[0] = new CarPark(1, "Hospital", outSouth, 60, myClock);
            carParks[1] = new CarPark(2, "University", outNorth, 600, myClock);
            carParks[2] = new CarPark(1, "Home", outEast, 45, myClock);

            myClock.setCarParks(carParks);

            northEntry.start();
            junction1.start();
            carParks[0].start();
            carParks[1].start();
            carParks[2].start();
            myClock.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}