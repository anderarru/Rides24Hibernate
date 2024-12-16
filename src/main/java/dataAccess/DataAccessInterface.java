package dataAccess;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import domain.*;
import exceptions.*;

public interface DataAccessInterface {

		
	/**
	 * This method opens the database
	 */
	void open();
	
	/**
	 * This method closes the database
	 */
	void close();

	
	/**
	 * This method removes all the elements of the database
	 */
	void emptyDatabase();
	
	
	/**
	 * This is the data access method that initializes the database with some events and questions.
	 * This method is invoked by the business logic (constructor of BLFacadeImplementation) when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */
	void initializeDB();

	
	List<String> getDepartCities();
	
	
	List<String> getArrivalCities(String from);

	
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail) throws  RideAlreadyExistException, RideMustBeLaterThanTodayException;
	
	
	public List<Ride> getRides(String from, String to, Date date);
	
	
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date);

	public User register(String username, String password, String mota) throws UserAlreadyExist;

	boolean login(String username, String password);

	public void cancelRide(String origin, String destination, Integer rideNumber);

	public List<Ride> getAllRides();

	List<Ride> searchByPrice(Float prezioa);


}