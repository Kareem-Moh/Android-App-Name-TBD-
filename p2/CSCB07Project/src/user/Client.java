/**
 * 
 */

package user;

import database.UserDatabase;
import flights.Flight;
import flights.FullyBookedException;
import flights.Itinerary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * The client class, creates a User object that will represent a client on the application.
 * 
 * @author Harsh
 *
 */
public class Client extends User {

  private ArrayList<Itinerary> bookedTrips = new ArrayList<Itinerary>();
  private String creditCardNumber;
  private Date creditCardExpirary;
  private String billingAddress;

  /**
   * Constructor for the client class.
   * 
   * @param lastName Client's last name
   * @param firstName Client's first name
   * @param email Client's email
   * @param billingAddress Client's billingAddress
   * @param creditCardNumber Client's creditCardNumber
   * @param creditCardExpirary Clients's credit card expirary date
   */
  public Client(String lastName, String firstName, String email, String billingAddress,
      String creditCardNumber, Date creditCardExpirary) {
    super(lastName, firstName, email);
    this.billingAddress = billingAddress;
    this.creditCardNumber = creditCardNumber;
    this.creditCardExpirary = creditCardExpirary;
  }

  /**
   * Select the best fitting Itinerary from a list of itineraries.
   * 
   * @return Selected Itinerary
   */


  /**
   * Add an Itinerary to booked trips.
   * @throws FullyBookedException there are no seats left
   */
  public void bookTrip(Itinerary itinerary) throws FullyBookedException {
    // Decrease the number of seats in each flight of the itinerary
    for (Flight f : itinerary.getFlights()) {
      f.fillSeats(1);
    }
    this.bookedTrips.add(itinerary);
  }


  /**
   * Return credit card Number.
   * @return the creditCardNumber
   */
  public String getCreditCardNumber() {
    return creditCardNumber;
  }

  /**
   * Return credit card expirary date.
   * @return the creditCardExpirary
   */
  public Date getCreditCardExpirary() {
    return creditCardExpirary;
  }

  /**
   * Return the billing address of the client.
   * @return the billingAddress
   */
  public String getBillingAddress() {
    return billingAddress;
  }


  /**
   * Sets the billing address for client.
   * @param billingAddress The new billing address to be set
   */
  public void setBillingAddress(String billingAddress) {
    // TODO Auto-generated method stub
    this.billingAddress = billingAddress;
  }

  @Override
  public ArrayList<Itinerary> viewBookedTrips() {
    return this.bookedTrips;
  }

  /**
   * Updates the Clients personabookedTripsl information with a new given info from the parameters
   * given. Also updates the .csv file in the database.
   * 
   * @param lastName New last name to update
   * @param firstName New FIrst name to update..
   * @param billingAddress New Billing Address to update
   */
  public void updateClientPersonalInformation(String lastName, String firstName,
      String billingAddress) {
    this.setLastName(lastName);
    this.setFirstName(firstName);
    this.billingAddress = billingAddress;
    UserDatabase.getInstance().updateClientsPersonalInformation(this, lastName, firstName,
        billingAddress);
  }

  @Override
  public String toString() {
    DateFormat converter = new SimpleDateFormat("yyyy-MM-dd");
    String creditCardExp = converter.format(creditCardExpirary);
    return String.format("%s;%s;%s;%s;%s;%s", this.getLastName(), this.getFirstName(),
        this.getEmail(), this.getBillingAddress(), this.getCreditCardNumber(), creditCardExp);
  }



}
