package Concurrency;

/**
 * Created by Ashley Morris on 18/12/2014.
 */
public class CarPark extends Thread{

    private int id; //An identifier for this car park.
    private String name;

    private Road inRoad; // The road that leads into this carpark.
    private Road outRoad;// If doing advanced configuration, cars leading away from the carpark.

    private Vehicle [] cars; //An array of vehicles, stores the cars that are sent here. Fixed capacity.
    private int noOfCars; //The current position in the carCapacity array.

    Clock clock; //A common clock object to get timestamp data.


    public CarPark(int id, String name, Road inRoad, int carparkCapacity, Clock clock) {

        this.id = id;
        this.name = name;
        this.clock = clock;
        this.inRoad = inRoad;

        cars = new Vehicle[carparkCapacity];
        noOfCars = 0;
    }

    /**
     *
     * @return True if there is still space in the carpark for new cars. False if the carpark is full.
     */
    private boolean isSpace(){

        Vehicle nextCar;

        if (inRoad.isAvailable()){
            nextCar = inRoad.extract();
            nextCar.setTimeParked(clock.getTimeStamp());
            cars[noOfCars] = nextCar;
            noOfCars++;
        }


        //The number of cars has reached the maximum capacity of the car park.
        // Return false because there is no room left:
        if(cars.length == noOfCars ){
            return false;
        }
        else {
           return true;
        }
    }

    public String getCarparkName(){
        return this.name;
    }

    public synchronized int getRemaining(){
        return cars.length - noOfCars;
    }

    @Override
    public void run() {
        while (isSpace()){
            try{
                sleep(1000);
            }
            catch (InterruptedException iex){
            }
        }
    }


}
