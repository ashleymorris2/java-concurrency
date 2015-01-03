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

            EntryPoint north = new EntryPoint(road1, "north", 550, myClock);
            Junction junction1 = new Junction(myClock, 1, destinationList1);
            junction1.setInRoads(road1, null, null, null);
            junction1.setLightsSequence("NORTH", "south", "west", null);


            myClock.start();
            north.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}