package Main;

import Concurrency.Clock;
import Concurrency.EntryPoint;
import Concurrency.Junction;
import Concurrency.Road;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashley Morris on 22/12/2014.
 */
public class ConcurrencyAssignment {

    public static void main(String[] args) {

        try {
            Map<Integer, String> destinationList1 = new HashMap<Integer, String>();
            destinationList1.put(1, "NORTH");
            destinationList1.put(2, "South");
            destinationList1.put(3, "EAST");
            destinationList1.put(4, "WEST");

            Clock myClock = new Clock();

            Road road1 = new Road(50);
            Road road2 = new Road(90);

            Road road3 = new Road(55);

            EntryPoint north = new EntryPoint(road1, "north", 30, myClock);
            EntryPoint east = new EntryPoint(road3, "east", 55, myClock);

            Junction junction1 = new Junction(myClock, 1, destinationList1);
            junction1.setInRoads(road1, road3, null, null);
            junction1.setOutRoads(road2,road2,road2,road2);

            junction1.setLightsSequence("NORTH", "east", null, null);
            junction1.setNorthDuration(120);
            junction1.setEastDuration(60);



            junction1.start();
            north.start();
            east.start();
            myClock.start();


        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}