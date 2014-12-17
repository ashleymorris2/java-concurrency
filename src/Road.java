/**
 * Created by Ashley Morris on 17/12/2014.
 *
 * 1. Road objects should NOT run as threads.
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
 * 5. You will need to provide a method for an entry point or junction to check if there is space on the
 * road before attempting to add a car, and another for a junction or car parks to see whether a car is
 * available for them to remove or not.
 *
 */
public class Road {




}
