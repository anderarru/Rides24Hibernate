package dataAccess;

import java.io.File;
import java.net.NoRouteToHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.UtilDate;
import domain.Driver;
import domain.Ride;
import domain.User;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.UserAlreadyExist;

import org.hibernate.Query;
import org.hibernate.Session;
import java.util.*;
import eredua.HibernateUtil;

/**
 * It implements the data access to the objectDb database
 */
public class HibernateDataAccess implements DataAccessInterface {

	private Session session;

	public HibernateDataAccess() {
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	public void open() {
		System.out.println("Opening DataAccess instance");
		session = HibernateUtil.getSessionFactory().getCurrentSession();

	}

	@Override
	public void close() {
		System.out.println("DataBase closed");

	}

	@Override
	public void emptyDatabase() {
	}

	@Override
	public void initializeDB() {

		session.beginTransaction();

		try {
			/*
			 * Query del = session.createQuery("delete from Driver");
			 * 
			 * del.executeUpdate();
			 * 
			 * Query del2 = session.createQuery("delete from Ride");
			 * 
			 * del2.executeUpdate();
			 */

			Calendar today = Calendar.getInstance();

			int month = today.get(Calendar.MONTH);
			int year = today.get(Calendar.YEAR);
			if (month == 12) {
				month = 1;
				year += 1;
			}

			// Create drivers

			Driver driver1 = new Driver("driver1@gmail.com", "Aitor Fernandez");
			Driver driver2 = new Driver("driver2@gmail.com", "Ane Gaztañaga");
			Driver driver3 = new Driver("driver3@gmail.com", "Test driver");

			// Create rides

			driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 4, 7);
			driver1.addRide("Donostia", "Gazteiz", UtilDate.newDate(year, month, 6), 4, 8);
			driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 4, 4);

			driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year, month, 7), 4, 8);

			driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 3, 3);
			driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 2, 5);
			driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year, month, 6), 2, 5);

			driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 14), 1, 3);

			session.persist(driver1);
			session.persist(driver2);
			session.persist(driver3);

			session.getTransaction().commit();
			System.out.println("Db initialized");
		} catch (Exception e) {
			e.printStackTrace();
		}
	};

	/**
	 * This method returns all the cities where rides depart
	 * 
	 * @return collection of cities
	 */

	@Override
	public List<String> getDepartCities() {
		session.beginTransaction();
		List<String> cities = session.createQuery("SELECT DISTINCT r.origin FROM Ride r ORDER BY r.origin").list();
		session.getTransaction().commit();

		return cities;

	}

	/**
	 * This method returns all the arrival destinations, from all rides that depart
	 * from a given city
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */

	@Override
	public List<String> getArrivalCities(String from) {
		session.beginTransaction();
		Query q = session.createQuery(
				"SELECT DISTINCT r.destination FROM Ride r WHERE r.origin=:departure ORDER BY r.destination");
		q.setParameter("departure", from);
		List<String> arrivingCities = q.list();
		session.getTransaction().commit();
		return arrivingCities;

	}

	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from        the origin location of a ride
	 * @param to          the destination location of a ride
	 * @param date        the date of the ride
	 * @param nPlaces     available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException         if the same ride already exists for
	 *                                           the driver
	 */

	@Override
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		System.out.println(">> DataAccess: createRide=> from= " + from + " to= " + to + " driver=" + driverEmail
				+ " date " + date);
		try {
			if (new Date().compareTo(date) > 0) {
				throw new RideMustBeLaterThanTodayException(
						ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
			}

			session.beginTransaction();
			Driver driver = (Driver) session.get(Driver.class, driverEmail);
			if (driver.doesRideExists(from, to, date)) {
				session.getTransaction().commit();
				throw new RideAlreadyExistException(
						ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
			}
			Ride ride = driver.addRide(from, to, date, nPlaces, price);
			session.persist(ride);
			session.getTransaction().commit();

			return ride;
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			session.getTransaction().commit();
			return null;
		}

	}

	/**
	 * This method retrieves the rides from two locations on a given date
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */

	@Override
	public List<Ride> getRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getRides=> from= " + from + " to= " + to + " date " + date);

		List<Ride> res = new ArrayList<Ride>();
		session.beginTransaction();
		Query q = session.createQuery(
				"SELECT r FROM Ride r WHERE r.origin=:departure AND r.destination=:arrival AND r.date=:data");
		q.setParameter("departure", from);
		q.setParameter("arrival", to);
		q.setParameter("data", date);
		List<Ride> rides = q.list();
		session.getTransaction().commit();
		for (Ride ride : rides) {
			res.add(ride);
		}

		return res;
	}

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */

	@Override
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getEventsMonth");
		List<Date> res = new ArrayList<Date>();

		Date firstDayMonthDate = UtilDate.firstDayMonth(date);
		Date lastDayMonthDate = UtilDate.lastDayMonth(date);

		session.beginTransaction();
		Query q = session.createQuery(
				"SELECT DISTINCT r.date FROM Ride r WHERE r.origin=:departure AND r.destination=:arrival AND r.date BETWEEN :firstDayMonthDate and :lastDayMonthDate");

		q.setParameter("departure", from);
		q.setParameter("arrival", to);
		q.setParameter("firstDayMonthDate", firstDayMonthDate);
		q.setParameter("lastDayMonthDate", lastDayMonthDate);
		List<Date> dates = q.list();
		session.getTransaction().commit();
		for (Date d : dates) {
			res.add(d);
		}
		return res;
	}

	@Override
	public User register(String username, String password, String mota) throws UserAlreadyExist {

		User u = new User(username, password, mota);
		session.beginTransaction();

		if ((User) session.get(User.class, username) != null) {
			throw new UserAlreadyExist();
		}

		session.persist(u);
		session.getTransaction().commit();

		System.out.println("Gordeta " + u);

		return u;

	}

	@Override
	public boolean login(String username, String password) {
		session.beginTransaction();
		User user = (User) session.get(User.class, username);
		session.getTransaction().commit();
		return (user != null && user.isCorrectPassword(password));
	}

	@Override
	public void cancelRide(String origin, String destination, Integer rideNumber) {
		Ride rideToDelete = null;

		session.beginTransaction();
		
		Query q=session.createQuery("DELETE FROM Ride WHERE origin = :origin AND destination = :destination AND rideNumber = :rideNumber")
				.setParameter("origin", origin).setParameter("destination", destination).setParameter("rideNumber",rideNumber);	
		q.executeUpdate();
		session.getTransaction().commit();

	}

	@Override
	public List<Ride> getAllRides() {
	    List<Ride> res = new ArrayList<Ride>();
	    if (!session.getTransaction().isActive()) {
	        session.beginTransaction(); // Asegurarse de que hay una transacción activa
	    }
	    Query q = session.createQuery("FROM Ride");
	    List<Ride> rides = q.list();
	    session.getTransaction().commit();
	    res.addAll(rides);
	    return res;
	}

	@Override
	public List<Ride> searchByPrice(Float prezioa) {
		List<Ride> res = new ArrayList<Ride>();
		session.beginTransaction();
		Query q = session.createQuery("FROM Ride WHERE price=:prezioa");
		q.setParameter("prezioa", prezioa);
		List<Ride> rides = q.list();
		session.getTransaction().commit();
		for (Ride ride : rides) {
			res.add(ride);
		}
		return res;
	}

}
