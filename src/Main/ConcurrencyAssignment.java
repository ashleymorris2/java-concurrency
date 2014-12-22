package Main;

import Concurrency.Clock;
import Concurrency.EntryPoint;
import Concurrency.Road;

/**
 * Created by Ashley Morris on 22/12/2014.
 */
public class ConcurrencyAssignment {

    public static void main(String[] args) {

        Clock myClock = new Clock();
        Road road1 = new Road(50);

        EntryPoint north = new EntryPoint(road1, "north", 550, myClock);

        myClock.start();
        north.start();

    }
}
