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

        Clock myClock = new Clock();

        try {
            CarPark [] carParks = new  CarPark[4];

            //Entry point roads:
            Road entry_a1 = new Road(50);
            Road entry_a2 = new Road(30);
            Road entry_a3 = new Road(60);

            //Junction 1 out Roads:
            Road road1A_North = new Road(7);
            Road road1B_West = new Road(15);

            //Junction 2 out Roads:
            Road road2A_North = new Road(10);
            Road road2B_South = new Road(7);

            //Junction 3 out Roads:
            Road road3A_West = new Road(10);
            Road road3B_East = new Road(10);
            Road road3C_South = new Road(15);

            //Junction 4 out Roads:
            Road road4A_North = new Road(15);
            Road road4B_South = new Road(15);

            EntryPoint northEntry = new EntryPoint(entry_a1, "north", 550, myClock);
            EntryPoint eastEntry = new EntryPoint(entry_a2, "east", 300, myClock);
            EntryPoint southEntry = new EntryPoint(entry_a3, "south", 550, myClock);


            //Junction 1: =================
            //Junction one destination list:
            Map<Integer, String> destinationList1 = new HashMap<Integer, String>();
            destinationList1.put(1, "NORTH");
            destinationList1.put(2,"NORTH");
            destinationList1.put(3, "NORTH");
            destinationList1.put(4, "WEST");

            Junction junction1 = new Junction(myClock, 1, destinationList1);
            junction1.setLightsSequence("NORTH", "SOUTH", null, null );
            junction1.setInRoads(road2B_South, null, entry_a3, null);
            junction1.setOutRoads(road1A_North, null, null, road1B_West);
            junction1.setSouthDuration(340);
            junction1.setNorthDuration(380);


            //Junction 2: =================
            //Junction two destination list:
            Map<Integer, String> destinationList2 = new HashMap<Integer, String>();
            destinationList2.put(1, "NORTH");
            destinationList2.put(2,"NORTH");
            destinationList2.put(3, "NORTH");
            destinationList2.put(4, "SOUTH");

            Junction junction2 = new Junction(myClock, 2, destinationList2);
            junction2.setLightsSequence("NORTH", "SOUTH", "EAST", null);
            junction2.setInRoads(road3B_East, entry_a2, road1A_North, null);
            junction2.setOutRoads(road2A_North, null, road2B_South, null);
            junction2.setNorthDuration(360);
            junction2.setEastDuration(100);
            junction2.setSouthDuration(340);

            //Junction 3: =================
            //Junction three destination list:
            Map<Integer, String> destinationList3 = new HashMap<Integer, String>();
            destinationList3.put(1, "WEST");
            destinationList3.put(2,"WEST");
            destinationList3.put(3, "SOUTH");
            destinationList3.put(4, "EAST");

            Junction junction3 = new Junction(myClock, 3, destinationList3);
            junction3.setLightsSequence("NORTH", "EAST", null, null);
            junction3.setInRoads(entry_a1,road2A_North, null, null);
            junction3.setOutRoads(null, road3B_East, road3C_South, road3A_West);
            junction3.setNorthDuration(360);
            junction3.setEastDuration(360);

            //Junction 4: =================
            //Junction four destination list:
            Map<Integer, String> destinationList4 = new HashMap<Integer, String>();
            destinationList4.put(1, "NORTH");
            destinationList4.put(2,"SOUTH");
            destinationList4.put(3, "EAST");
            destinationList4.put(4, "EAST");

            Junction junction4 = new Junction(myClock, 4 , destinationList4);
            junction4.setLightsSequence("EAST", null, null, null);
            junction4.setInRoads(null,road3A_West, null, null);
            junction4.setOutRoads(road4A_North, null, road4B_South, null);
            junction4.setEastDuration(120);


            //Car Parks: ======
            carParks[0] = new CarPark(1, "University", road4A_North, 100, myClock);
            carParks[1] = new CarPark(2, "Station", road4B_South, 150, myClock);
            carParks[2] = new CarPark(3, "Shopping Centre", road3C_South, 400, myClock);
            carParks[3] = new CarPark(4, "Industrial Park", road1B_West, 1000, myClock);

            //To report how many cars have parked every 5 minutes.
            myClock.setCarParks(carParks);

            northEntry.start();
            eastEntry.start();
            southEntry.start();
            junction1.start();
            junction2.start();
            junction3.start();
            junction4.start();
            for(int i = 0; i < 4; i ++){
                carParks[i].start();
            }
            myClock.start();

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}