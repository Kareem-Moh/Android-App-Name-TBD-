package flights;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

/**
 * This is class keeps track of all the flights required to get from one destination to another,
 * while also keeping track of the incurred cost and travel time.
 * 
 * @author William Granados, Kareem Mohamed, Liam Pakingan
 * @see Flight
 */
public class Itinerary {

  /** Container where we will keep track of all our flights. */
  private ArrayList<Flight> flights;
  /** Accumulated cost for each flight in our @link{flights}; rounded to two decimal places. */
  private double totalCost;
  /** Accumulated time to get from our starting departure location to our arrival destination. */
  private Duration totalFlightTime;


  /**
   * Generic constructor called when we don't have any flights to add.
   */
  public Itinerary() {
    this.flights = new ArrayList<Flight>();
    this.totalCost = 0;
  }

  /**
   * Generic constructor called when we do have flights to be added to our itinerary.
   * 
   * @param flights is the list of flights in the Itinerary.
   */
  public Itinerary(ArrayList<Flight> flights) {
    this.setFlights(flights);
  }

  /**
   * Returns the ArrayList of Flight in this Itinerary.
   * 
   * @return the ArrayList of Flight.
   */
  public ArrayList<Flight> getFlights() {
    return flights;
  }

  /**
   * Sets the ArrayList of of flights in our class and also updates totalCost and totalTime.
   * 
   * @param flights is array list of flights
   */
  public void setFlights(ArrayList<Flight> flights) {
    this.flights = flights;
    this.updateTotalCost();
    this.updateTotalTime();
  }

  /**
   * Appends this flight to the the end of our current flight list.
   * 
   * @param flight is the flight that you add to ArrayList of Flight
   */
  public void addFlight(Flight flight) {
    this.flights.add(flight);
    this.updateTotalCost();
    this.updateTotalTime();
  }

  /**
   * Returns the accumulated cost for each flight in our {@link flights}.
   * 
   * @return the totalCost.
   */
  public double getTotalCost() {
    return totalCost;
  }

  /**
   * Returns the accumulated travel time for each flight in our {@link flights}.
   * 
   * @return the totalFlightTime.
   */
  public Duration getTotalTravelTime() {
    return totalFlightTime;
  }

  /**
   * Updates the total cost based on the flights in our {@link flights}.
   */
  private void updateTotalCost() {
    double totalCost = 0.0;
    for (int index = 0; index < this.flights.size(); index++) {
      totalCost += flights.get(index).getCost();
    }
    this.totalCost = totalCost;
  }

  /**
   * Updates the total travel time based on the flights in our {@link flights}.
   */
  private void updateTotalTime() {
    Instant startingDateTime = this.flights.get(0).getDepartureDateTime().toInstant();
    Instant endingDateTime =
        this.flights.get(this.flights.size() - 1).getArrivalDateTime().toInstant();
    this.totalFlightTime = Duration.between(startingDateTime, endingDateTime);
  }

  /**
   * Returns the first flight in our {@link flights}.
   * 
   * @return last flight
   */
  public Flight getFirstFlight() {
    return this.flights.get(0);
  }

  /**
   * Returns the last flight in our {@link flights}.
   * 
   * @return last flight
   */
  public Flight getLastFlight() {
    return this.flights.get(this.flights.size() - 1);
  }

  /**
   * Returns the first city in our itinerary; coined the "origin".
   * 
   * @return the origin city for our Itinerary
   */
  public String getOrigin() {
    return this.flights.get(0).getOrigin();
  }

  /**
   * Returns the last city in our itinerary; coined the "destination".
   * 
   * @return the destination city for our Itinerary.
   */
  public String getDestination() {
    return this.flights.get(this.flights.size() - 1).getDestination();
  }

  @Override
  public String toString() {
    String ret = "";
    for (Flight flight : this.flights) {
      ret += String.format("%s\n", flight.toStringWithoutCost());
    }
    ret += String.format("%.2f\n", this.getTotalCost());
    ret += String.format("%.2f", (double) this.getTotalTravelTime().getSeconds() / 60.0 / 60.0);
    return ret;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((flights == null) ? 0 : flights.hashCode());
    long temp;
    temp = Double.doubleToLongBits(totalCost);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((totalFlightTime == null) ? 0 : totalFlightTime.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Itinerary other = (Itinerary) obj;
    if (flights == null) {
      if (other.flights != null) {
        return false;
      }
    } else if (!flights.equals(other.flights)) {
      return false;
    }
    if (Double.doubleToLongBits(totalCost) != Double.doubleToLongBits(other.totalCost)) {
      return false;
    }
    if (totalFlightTime == null) {
      if (other.totalFlightTime != null) {
        return false;
      }
    } else if (!totalFlightTime.equals(other.totalFlightTime)) {
      return false;
    }
    return true;
  }
}
