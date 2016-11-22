package flights;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class encapsulates all the information required for a flight.
 * This class will be populated by information provided in the respective flights.csv file which
 * will be in the following format:
 * Number;DepartureDateTime;ArrivalDateTime;Airline;Origin;Destination;Price
 * An example is as follows: AC489;2016-09-09 09:09;2016-09-09 13:27;FlightsRUs;Chicago;Los
 * Angelos;300.99
 * @author William Granados, Kareem Mohamed, Lian Pakingan 
 */
public class Flight {

  private String flightNumber;
  private Date departureDateTime;
  private Date arrivalDateTime;
  private String airline;
  private String origin;
  private String destination;
  private double cost;

  /**
   * Generic constructor which encapsulates all the necessities for a flight.
   * 
   * @param flightNumber is the flight number.
   * @param departureDateTime is the date and time of departure.
   * @param arrivalDateTime is the date and time of arrival.
   * @param airline is the flight.
   * @param origin is the city of origin.
   * @param destination is the city of destination.
   * @param cost is the cost of the flight.
   */
  public Flight(String flightNumber, Date departureDateTime, Date arrivalDateTime, String airline,
      String origin, String destination, double cost) {
    this.flightNumber = flightNumber;
    this.departureDateTime = departureDateTime;
    this.arrivalDateTime = arrivalDateTime;
    this.airline = airline;
    this.origin = origin;
    this.destination = destination;
    this.cost = cost;
  }

  /**
   * returns the flightNumber.
   * 
   * @return the flightNumber.
   */
  public String getFlightNumber() {
    return flightNumber;
  }

  /**
   * @param flightNumber the flightNumber to set.
   */
  public void setFlightNumber(String flightNumber) {
    this.flightNumber = flightNumber;
  }

  /**
   * returns the departureDateTime.
   * 
   * @return the departureDateTime.
   */
  public Date getDepartureDateTime() {
    return departureDateTime;
  }

  /**
   * @param departureDateTime the departureDateTime to set.
   */
  public void setDepartureDateTime(Date departureDateTime) {
    this.departureDateTime = departureDateTime;
  }

  /**
   * returns the arrivalDateTime.
   * 
   * @return the arrivalDateTime.
   */
  public Date getArrivalDateTime() {
    return arrivalDateTime;
  }

  /**
   * @param arrivalDateTime the arrivalDateTime to set.
   */
  public void setArrivalDateTime(Date arrivalDateTime) {
    this.arrivalDateTime = arrivalDateTime;
  }

  /**
   * returns the airline.
   * 
   * @return the airline.
   */
  public String getAirline() {
    return airline;
  }

  /**
   * @param airline the airline to set.
   */
  public void setAirline(String airline) {
    this.airline = airline;
  }

  /**
   * returns the origin.
   * 
   * @return the origin.
   */
  public String getOrigin() {
    return origin;
  }

  /**
   * @param origin the origin to set.
   */
  public void setOrigin(String origin) {
    this.origin = origin;
  }

  /**
   * returns the destination.
   * 
   * @return the destination.
   */
  public String getDestination() {
    return destination;
  }

  /**
   * @param destination the destination to set.
   */
  public void setDestination(String destination) {
    this.destination = destination;
  }

  /**
   * returns the cost.
   * 
   * @return the cost.
   */
  public double getCost() {
    return cost;
  }

  /**
   * @param cost the cost to set.
   */
  public void setCost(double cost) {
    this.cost = cost;
  }

  @Override
  public String toString() {
    DateFormat converter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String departureDateTime = converter.format(this.getDepartureDateTime());
    String arrivalDateTime = converter.format(this.getArrivalDateTime());
    return String.format("%s;%s;%s;%s;%s;%s;%s", this.getFlightNumber(), departureDateTime,
        arrivalDateTime, this.getAirline(), this.getOrigin(),
        this.getDestination(), String.format("%.2f", this.getCost()));

  }

}
