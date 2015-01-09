
/**
 * Created by Ashley Morris on 17/12/2014.
 *
 * 1. Concurrency.EntryPoint objects should run as threads.
 *
 * 2. An Concurrency.EntryPoint should generate Concurrency.Vehicle objects with destinations at a pre-determined rate, and feed
 * them into the road network as long as space is available on the road.
 *
 * 3. The Concurrency.EntryPoint should give the car a timestamp of when it entered the town.
 *
 * 4. The destinations of cars should be weighted randomly:
 *
 */

package Concurrency;

//Producer.

public class EntryPoint extends Thread {

    private Vehicle car; //The car that is generated to be passed into the buffer (road)
    private Road road; //The buffer that represents the section of road that this entry point is connected to.

    private String name; // The name of this entry point. (North, East or South)
    private int maxNumOfCars;//The maximum number of cars that this entry point is to generate per hour.

    private Clock clock;

    public EntryPoint(Road road, String name, int maxNumOfCars, Clock clock) {
        this.road = road;
        this.name = name;
        this.maxNumOfCars = maxNumOfCars;
        this.clock = clock;
    }


    /**Generates a car with a random destination and feeds it into the road network that this entry
     * point is connected to.
     */
    public void generate() {

        //System.out.println("Item producing");
        int destination = assignDestination();
        long timeStamp = clock.getTimeStamp();

        car = new Vehicle(destination, timeStamp);
        road.insert(car);
    }


    @Override
    public void run() {
        if (road.isSpace()) {
            for (int i = 0; i < maxNumOfCars; i++) {
                try {
                    sleep((int) (Math.random()) * 50);
                } catch (InterruptedException iex) {

                }
                //Check to see if there is any space on the road before attempting to place a car on it.
                generate();

                if (!clock.simulationRunning()) {
                    //timer is up, stop thread?
                    break;
                }
            }

        }
    }


    /**Generates a random number between 1-100 inclusive and returns an integer between 1-4
     * representing a vehicles destination takes no parameters.
     1: University = 10%
     2: Station = 20%
     3: Shopping Centre = 30%
     4: Industrial Park = 40%
     **/
    int assignDestination(){

        int randNumber = (int) (Math.random() * 100) + 1;
        int destination = 0;

        if (randNumber <= 10){
            destination = 1;
        }
        else if(randNumber > 10 && randNumber <=30){
            destination = 2;
        }
        else if (randNumber > 30 && randNumber <= 60){
            destination = 3;
        }
        else if (randNumber > 60){
            destination = 4;
        }

        return destination;
    }
}
