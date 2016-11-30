/**
 * 
 */

package user;



import database.FlightDatabase;
import database.UserDatabase;
import flights.Flight;
import flights.FullyBookedException;
import flights.Itinerary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;



/**
 * The Admin class, this class will create an Admin object responsible for the administrative 
 * duties of this application.
 * @author Harsh Author of class.
 *
 */
public class Admin extends User {
  Client selectedClient = null;

  /**
   * Constructor for Admin class.
   * @param lastName The last name of the Admin
   * @param firstName The first name of the Admin
   * @param email The email of the Admin
   */
  public Admin(String lastName, String firstName, String email) {
    super(lastName, firstName, email);
    // TODO Auto-generated constructor stub
  }

  /**
   * Updates the Clients personabookedTripsl information with a new given info from the parameters
   * given. Also updates the .csv file in the database. Since this is an administrative method
   * a Client must be selected before hand and then this method should be called.
   * 
   * @param lastName New last name to update
   * @param firstName New FIrst name to update..
   * @param billingAddress New Billing Address to update
   */
  public void updateClientPersonalInformation(String lastName, String firstName,
      String billingAddress) {
    Client client = this.selectedClient;
    // Update the same user in the database
    for (Client clients : UserDatabase.getInstance().getUsers().values()) {
      if (clients.getEmail().equals(client.email)) {
        // clients.updateClientPersonalInformation(client, lastName, firstName, billingAddress);
        clients.setLastName(lastName);
        clients.setFirstName(firstName);
        clients.setBillingAddress(billingAddress);
        UserDatabase.getInstance().updateClientsPersonalInformation(client, lastName, firstName,
            billingAddress);
        break;
      }
    }
  }

  /**
   * Select a User who will be manipulated.
   * 
   */
  public void selectUser(Client client) {
    // Add code below
    this.selectedClient = client;
  }

  /**
   * Views the users that are currently registered on the system. Returns a list of viewers.
   * 
   * @return ArrayList of users
   */
  public Map<String, Client> viewUsers() {
    return UserDatabase.getInstance().getUsers();
  }

  @Override
  public ArrayList<Itinerary> viewBookedTrips() {
    return this.selectedClient.viewBookedTrips();
  }

  @Override
  public void bookTrip(Itinerary itinerary) throws FullyBookedException {
    this.selectedClient.bookTrip(itinerary);
  }

  /**
   * Updates the Clients email, with a new given email from Parameters given. Also updates the .csv
   * file in the database.
   * 
   * @param client A user on the system who is a client
   * @param email A new email for the client to be updated
   */
  public void updateClientEmail(Client client, String email) {
    client.email = email;
    UserDatabase.getInstance().updateClientEmail(client, email);
  }

  /**
   * Adds a new flight to the current list of flights.
   * 
   * @param flight A flight object to be added to the list of flights
   */
  public void addFlight(Flight flight) {
    FlightDatabase.getInstance().getFlights().add(flight);
  }

  /**
   * Adds new flights to the current list of flights, from a file, where the file contains flights
   * in csv format.
   * 
   * @param flightPath The file path to the flights to be added
   * @throws IOException  likely files were not found
   */
  public void uploadFLights(String flightPath) throws IOException {
    FlightDatabase.getInstance().readFlightsFromFile(flightPath);
  }



}
