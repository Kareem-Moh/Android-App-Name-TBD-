package user;


import database.FlightDatabase;
import database.UserDatabase;
import flights.Flight;
import flights.Itinerary;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * A class to represent the User class.
 * 
 * @author Harsh
 * @author Kareem
 */
public class User extends Account {

  private ArrayList<Itinerary> bookedTrips = new ArrayList<Itinerary>();
  private String creditCardNumber;
  private Date creditCardExpirary;

  /**
   * Constructor for the User class.
   * 
   * @param email String representation of users email
   * @param billingAddress String representation of Billing address
   * @param firstName First name of the user
   * @param lastName Last name of the user
   */
  public User(String lastName, String firstName, String email, String billingAddress,
      String creditCardNumber, Date creditCardExpirary) {
    super(lastName, firstName, email, billingAddress);
    this.creditCardNumber = creditCardNumber;
    this.creditCardExpirary = creditCardExpirary;
  }

  /**
   * Updates the Clients email, with a new given email from Parameters given. Also updates the .csv
   * file in the database.
   * 
   * @param client A user on the system who is a client
   * @param email A new email for the client to be updated
   */
  public void updateClientEmail(User client, String email) {
    client.email = email;
    // Insert code below
    try {
      UserDatabase.updateClientsEmail(client, email);
    } catch (IOException exception) {
      // TODO Auto-generated catch block
      exception.printStackTrace();
    }
  }

  /**
   * get the CC number.
   * 
   * @author Kareem
   * @return the creditCardNumber
   */
  public String getCreditCardNumber() {
    return creditCardNumber;
  }

  /**
   * get the CC expiry.
   * 
   * @author Kareem
   * @return the creditCardExpirary
   */
  public Date getCreditCardExpirary() {
    return creditCardExpirary;
  }

  /**
   * Updates the Clients personabookedTripsl information with a new given info from the parameters
   * given. Also updates the .csv file in the database.
   * 
   * @param client The user who's information is being updated
   * @param lastName New last name to update
   * @param firstName New FIrst name to update..
   * @param billingAddress New Billing Address to update
   */
  public void updateClientPersonalInformation(User client, String lastName, String firstName,
      String billingAddress) {
    // Update the same user in the database
    for (User clients : UserDatabase.getUsers()) {
      if (clients.getEmail().equals(client.email)) {
        // clients.updateClientPersonalInformation(client, lastName, firstName, billingAddress);
        clients.setLastName(lastName);
        clients.setFirstName(firstName);
        clients.setBillingAddress(billingAddress);

        try {
          UserDatabase.updateClientsPersonalInformation(client, lastName, firstName,
              billingAddress);
        } catch (IOException exception) {
          // TODO Auto-generated catch block
          exception.printStackTrace();
        }
        break;
      }
    }
  }

  /**
   * Views the users that are currently registered on the system. Returns a list of viewers.
   * 
   * @return ArrayList of users
   */
  public ArrayList<User> viewUsers() {
    return UserDatabase.getUsers();
  }

  /**
   * Select a User who will be manipulated.
   * 
   * @return Returns a User that has been selected
   */
  public User selectUser() {
    // Add code below
    return null;
  }

  /**
   * Adds a new flight to the current list of flights.
   * 
   * @param flight A flight object to be added to the list of flights
   */
  public void addFlight(Flight flight) {
    FlightDatabase.getFlights().add(flight);
  }

  /**
   * Adds new flights to the current list of flights, from a file, where the file contains flights
   * in csv format.
   * 
   * @param flightPath The file path to the flights to be added
   */
  public void uploadFLights(String flightPath) {
    FlightDatabase.readFlightsFromFile(flightPath);
  }

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

    /* this is the sorted ArrayList of Itinerary */
    ArrayList<Itinerary> result = new ArrayList<Itinerary>();

    /* calling getFlightTrips */
    ArrayList<Itinerary> allItinerary = getFlightTrips(departureDate, origin, destination);

    /* this list means list of times for each Itinerary */
    ArrayList<Time> allTime = new ArrayList<Time>();

    /* getting list of all times */
    for (int index = 0; index < allItinerary.size(); index = index + 1) {
      /* getting indexed Itinerary */
      Itinerary indexedItinerary = allItinerary.get(index);
      /* adding the time to allTime */
      allTime.add(indexedItinerary.getTotalTravelTime());
    }

    /* this will sort the Arraylist of Time */
    Collections.sort(allTime);

    /* this means if the indexed flight is in result */
    ArrayList<Boolean> isItineraryInResult = new ArrayList<Boolean>();

    /* filling the array list(isItineraryInResult) with false */
    for (int index = 0; index < allItinerary.size(); index = index + 1) {
      isItineraryInResult.add(false);
    }

    /* putting the Itinerary in the result in increasing order */
    for (int index = 0; index < allItinerary.size(); index = index + 1) {
      /* getting the indexed element in allTime */
      Time indexedTime = allTime.get(index);

      /*
       * this boolean means if we have found the itinerary that has Total Travel Time of indexedTime
       */
      boolean itineraryNotFound = true;

      /* this means the index of the Itinerary with total TotalTravelTime is equal to indexedTime */
      int findIndexTotalTravelTime = 0;

      while (itineraryNotFound) {

        /* getting indexed Itinerary */
        Itinerary indexedItinerary = allItinerary.get(findIndexTotalTravelTime);

        /* check that total travel time of indexedItinerary is the same as indexedTime */
        if (indexedTime == indexedItinerary.getTotalTravelTime()) {
          /* and the indexedItinerary is not in result */

          if (false == isItineraryInResult.get(findIndexTotalTravelTime)) {
            /* add the itinerary into the result */
            result.add(indexedItinerary);
            /* replace the indexed element in isItineraryInResult with True */
            isItineraryInResult.set(findIndexTotalTravelTime, true);
            /* change itineraryNotFound to false */
            itineraryNotFound = false;
          }
        }
        /* increasing the index by 1 */
        findIndexTotalTravelTime = findIndexTotalTravelTime + 1;
      }
    }
    return result;
  }

  /**
   * Select the best fitting Itinerary from a list of itineraries.
   * 
   * @return Selected Itinerary
   */
  public Itinerary selectItinerary() {
    // Add code below
    return null;
  }

  /**
   * Add an Itinerary to booked trips.
   */
  public void bookTrip(Itinerary itinerary) {
    this.bookedTrips.add(itinerary);
  }

  /**
   * Returns the list of booked trips, list of itineraries where the itinerary could be 1 or more
   * flights.
   * 
   * @return The list of booked trips
   */
  public ArrayList<Itinerary> viewBookedTrips() {
    return bookedTrips;
  }

  @Override
  public String toString() {
    DateFormat converter = new SimpleDateFormat("yyyy-MM-dd");
    String creditCardExp = converter.format(creditCardExpirary);
    return String.format("%s;%s;%s;%s;%s;%s", this.getLastName(), this.getFirstName(),
        this.getEmail(), this.getBillingAddress(), this.getCreditCardNumber(),
        creditCardExp);
  }


}
