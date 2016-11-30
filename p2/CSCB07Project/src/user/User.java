package user;


import database.FlightDatabase;

import flights.FullyBookedException;
import flights.Itinerary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A class to represent the User class.
 * 
 * @author Harsh
 * @author Kareem
 */
public abstract class User extends Account {

  /**
   * Constructor for the User class.
   * 
   * @param email String representation of users email
   * @param firstName First name of the user
   * @param lastName Last name of the user
   */
  public User(String lastName, String firstName, String email) {
    super(lastName, firstName, email);
  }

  public abstract void updateClientPersonalInformation(String lastName, String firstName,
      String billingAddress);



  /**
   * A user enters origin, destination, and required dates. Returns a list of itineraries of which
   * the user can select most relevant itinerary, sorted by total cost or total flight time.
   * 
   * @param origin String representation of the Origin of trip
   * @param destination String representation of the destination of trip
   * @param departureDate String representation of the departure date of trip
   * @return A list of appropriate list of itineraries sorted by total cost or total time
   */
  public ArrayList<Itinerary> getFlightTrips(String departureDate, String origin,
      String destination) {
    // Add code below
    return null;
  }

  /**
   * A user enters origin, destination, and required dates. Returns a list of itineraries of which
   * is sorted sorted by total flight time.
   * 
   * @param origin String representation of the Origin of trip.
   * @param destination String representation of the destination of trip.
   * @param departureDate String representation of the departure date of trip.
   * @return A list of appropriate list of itineraries total time.
   */
  public ArrayList<Itinerary> getFlightTripsSortedByTime(String departureDate, String origin,
      String destination) {
    return FlightDatabase.getInstance().queryIteneraryByTime(departureDate, origin, destination);
  }

  /** If a User wants to check if a direct flight from an origin to a destination on a given
   * Date exists, they will use this method.
   * 
   * @param date The date of the flight
   * @param origin The origin of the flight
   * @param destination The destination of the flight
   * @return A list of flights in a string format
   * @throws IOException Checks if file exists
   */
  public List<String> getDirectFLights(String date, String origin, String destination)
      throws IOException {
    return FlightDatabase.getInstance().getDirectFlights(date, origin, destination);
  }

  /**A user can select an Itinerary, if a client selects an Itinerary they can book it for 
   * themselves, where an administrator can book a flight for any client.
   * 
   * @return A selected Itinerary
   */
  public Itinerary selectItinerary() {
    // Add code below
    return null;
  }


  /**A user can view booked trips, if a client calls this method, then a client can view
   * their booked trips, where an administrator can select any client and view their booked trips.
   * 
   * @return A list of booked flights
   */
  public abstract ArrayList<Itinerary> viewBookedTrips();

  /**A user can book a trip,
   * If a Client calls this method they will book the trip for themselves,
   * If an administrator calls this method they can book a trip for any selected Client.
   * 
   * @param itinerary The trip to book
   */
  public abstract void bookTrip(Itinerary itinerary) throws FullyBookedException;



}
