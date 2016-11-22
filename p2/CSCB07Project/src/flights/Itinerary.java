package flights;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

/**
 * A class to represent the Itinerary class.
 * 
 * @author Lian
 */
public class Itinerary {



  private ArrayList<Flight> flights;
  private double totalCost;
  private Time totalFlightTime;

  /**
   * this function make a new Itinerary
   * 
   * @param flights is the list of flights in the Itinerary.
   */
  public Itinerary(ArrayList<Flight> flights) {
    this.setFlights(flights);
  }

  /**
   * returns the ArrayList of Flight.
   * 
   * @return the ArrayList of Flight.
   */
  public ArrayList<Flight> getFlights() {
    return flights;
  }

  /**
   * This function set the Array List of flights to the param.
   * 
   * @param flights is array list of flights
   */
  public void setFlights(ArrayList<Flight> flights) {
    this.flights = flights;
  }

  /**
   * This function will add the flight into a ArrayList of Flight. after adding the flight totalCost
   * and totalFlightTime will be updated
   * 
   * @param flights is the flight that you add to ArrayList of Flight
   */
  public void addFlight(Flight flights) {
    this.flights.add(flights);
    updateTotalCost();
    updateTotalTime();
  }

  /**
   * returns the totalCost.
   * 
   * @return the totalCost.
   */
  public double getTotalCost() {
    return totalCost;
  }

  /**
   * returns the totalFlightTime.
   * 
   * @return the totalFlightTime.
   */
  public Time getTotalTravelTime() {
    return totalFlightTime;
  }


  /**
   * this function will update the total cost of the Itinerary.
   */
  private void updateTotalCost() {

    double costOfTotal = 0;

    /* for each flight */
    for (int index = 0; index < flights.size(); index = index + 1) {
      /* getting indexed flight */
      Flight indexedFlight = flights.get(index);
      /* adding the cost the flight to sumOfCost of */
      costOfTotal = costOfTotal + indexedFlight.getCost();
    }
    totalCost = costOfTotal;
  }

  /**
   * this function will update the total time of the Itinerary.
   */
  private void updateTotalTime() {

    /* getting first airplane's DateTime for departure */
    Flight firstFlight = flights.get(1);
    Date startingDateTime = firstFlight.getDepartureDateTime();

    /* getting last airplane's DateTime for arrival */
    Flight lastFlight = flights.get(flights.size() - 1);
    Date endingDateTime = lastFlight.getArrivalDateTime();

    /* this means time difference between startingDate and endingDate */
    long dateTimeDiff = startingDateTime.getTime() - endingDateTime.getTime();
    totalFlightTime.setTime(dateTimeDiff);

  }

  /**
   * returns the Origin city of the Itinerary.
   * 
   * @return the Origin city of the Itinerary
   */
  public String getOrigin() {
    return this.flights.get(0).getOrigin();
  }

  /**
   * returns the Destination city of the Itinerary.
   * 
   * @return the Destination city of the Itinerary.
   */
  public String getDestination() {
    return this.flights.get(this.flights.size() - 1).getDestination();
  }

  /* (non-Javadoc)
  * @see java.lang.Object#toString()
  */
  @Override
  public String toString() {
    String retString = null;
    for (Flight currFlight: flights) { 
      retString += (currFlight + "\n");
    }
    retString += (this.getTotalCost()
        + "\n" + this.getTotalTravelTime() + "\n");
    return retString;
  }

}
