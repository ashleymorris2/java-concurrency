/**
 * Created by Ashley Morris on 17/12/2014.
 *
 * 1. Concurrency.Road objects should NOT run as threads.
 *
 * 2. In the simulation, the road sections are acting as passive buffers (shared memory space) between
 * the various entry points, junctions and car parks. In other words, you have something like several
 * Producer/Consumer scenarios chained together.
 *
 * 3. A road will act like a queue data structure. The road class should be implemented as a fixed size
 * array to store the car objects as they pass onto and off it (capacity according to the configuration of
 * the simulation). DO NOT use a Java collection instead of an array
 *
 * 4. One simple way to implement the road is as a circular buffer, enforcing a strict ordering to the
 * addition and removal of cars.
 *
 * This array stores two pointers, a read pointer and a write pointer,
 * indicating where the next insert should take place and from where the next
 * element should be removed.  For example:
 *
 *                       [2] [3] [ ] [ ] [ ] [ ] [0] [1]
 *                                ^               ^
 *                                |               |
 *                              write            read
 *
 * Need to be careful not to let the two pointers overlap.
 *
 *
 * 5. You will need to provide a method for an entry point or junction to check if there is space on the
 * road before attempting to add a car, and another for a junction or car parks to see whether a car is
 * available for them to remove or not.
 *
 */

package Concurrency;

//Buffer

public class Road {

    private Vehicle [] cars; //The array that represents the cars currently on the road.
    private int available; // The number of cars that are available for removal in the buffer.

    private int head = 0;//The current beginning (first element) of the buffer. Read from.
    private int tail = 0;//The current end (last element) of the buffer. Write to.

    private String fromDirection;


    /**
     * Constructor for the Road class.
     *
     * @param size The capacity that this length of road can hold in cars.
     */
    public Road(int size) {

        //Buffer can't be less than 1.
        if(size <= 0){
            throw new IllegalArgumentException("Size parameter needs to be greater than 0");
        }
        cars = new Vehicle[size];
        available = 0;
    }


    /**
     * Will add a car object to this road (if there is space else it will wait until there is some)
     * and notify any waiting threads that there is a car available.
     *
     * @param car The car that is to be inserted into the Road buffer.
     */
    public synchronized void insert(Vehicle car){

        //Check to see if the buffer is full and tell the producers to wait if so.
        while (available == cars.length){
            //Remove:
            //System.out.println("waiting on insert");
            try{
                wait();
            } catch (InterruptedException iex) {
                iex.printStackTrace();
            }

        }
        cars[tail] = car; //The new car is added to the last element in the array.
        available = available +1;
        tail++;

        //If we have reached the end of the array we need to loop back to the beginning
        if(tail == cars.length){
           tail = 0;
        }

        //Notify any waiting threads that there is now data available for consumption
        notifyAll();
    }


    /**
     * Will remove a car object from this road buffer (if there is one to remove) if not, it will
     * signal for threads to wait until something is available and notify them when something is.
     *
     * @return The car object that the head of the buffer is currently pointing at.
     */
    public synchronized Vehicle extract(){

        Vehicle car;

        //Wait while the buffer is empty
        while (available == 0){
            //System.out.println("waiting on extract");
            try {
                wait();
            }
            catch (InterruptedException iex){
            }
        }

        //Retrieve the first car in the array:
        car = cars[head];
        cars[head] = null;

        available --;
        head++;

        if(head == cars.length){
            head =0;
        }

        notifyAll();

        return car;
    }


    /**
     * @return The maximum capacity for the Road buffer.
     */
    public int capacity(){
        return cars.length;
    }

    /**
     * Gets the destination for the next car that is in the buffer without removing it. (In case of a gridlock situation)

     * @return An integer representing the next cars destination.
     */

    public synchronized int getNextDestination(){

        return cars[head].getDestination();
    }


    /**
     * Checks whether a car is available for removal.
     *
     * @return The number of elements (cars) that are currently waiting (available) for removal.
     */
    public synchronized int getWaiting(){
        return available;
    }


    /**
     * Checks whether a car is available for removal.
     *
     * @return True if there is 1 or more. False if not.
     */
    public synchronized boolean isAvailable(){
        if(available > 0){
            return true;
        }
        else{
            return false;
        }
    }


    public void setFromDirection(String direction){
        this.fromDirection = direction;
    }


    public String getFromDirection(){
        return this.fromDirection;
    }


    /**
     * Checks whether there is space on the Road for a car. (in the buffer)
     *
     * @return True if there is space to write to. False if the buffer is full.
     */
    public synchronized boolean isSpace(){
        if(available == cars.length) {
            return false;
        }
        else {
            return true;
        }
    }

}
