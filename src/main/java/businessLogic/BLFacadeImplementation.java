package businessLogic;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;



import dataAccess.DataAccessInterface;
import domain.Ride;
import domain.User;
import domain.Driver;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.UserAlreadyExist;
import exceptions.RideAlreadyExistException;

/**
 * It implements the business logic as a web service.
 */

public class BLFacadeImplementation  implements BLFacade {
	DataAccessInterface dbManager;

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		    
		//dbManager.close();

		
	}
	
    public BLFacadeImplementation(DataAccessInterface da)  {
		
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		
		dbManager=da;		
	}
    
    
    /**
     * {@inheritDoc}
     */
 public List<String> getDepartCities(){
    	dbManager.open();	
		
		 List<String> departLocations=dbManager.getDepartCities();		

		dbManager.close();
		
		return departLocations;
    	
    }
    /**
     * {@inheritDoc}
     */
	 public List<String> getDestinationCities(String from){
		dbManager.open();	
		
		 List<String> targetCities=dbManager.getArrivalCities(from);		

		dbManager.close();
		
		return targetCities;
	}

	/**
	 * {@inheritDoc}
	 */
 
   public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverEmail ) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{
	   
		dbManager.open();
		Ride ride=dbManager.createRide(from, to, date, nPlaces, price, driverEmail);		
		dbManager.close();
		return ride;
   };
	
   /**
    * {@inheritDoc}
    */
	 
	public List<Ride> getRides(String from, String to, Date date){
		dbManager.open();
		List<Ride>  rides=dbManager.getRides(from, to, date);
		dbManager.close();
		return rides;
	}

    
	/**
	 * {@inheritDoc}
	 */
	
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		dbManager.open();
		List<Date>  dates=dbManager.getThisMonthDatesWithRides(from, to, date);
		dbManager.close();
		return dates;
	}
	
	@Override
	public User register(String username, String password, String mota) throws UserAlreadyExist {
		dbManager.open();
		User u = dbManager.register(username, password, mota);
		dbManager.close();
		return u;
	}
	
	@Override
	public boolean login(String username, String password) {
		dbManager.open();
		boolean ret = dbManager.login(username, password);
		dbManager.close();
		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
    
	 public void initializeBD(){
    	dbManager.open();
		dbManager.initializeDB();
		dbManager.close();
	}

	@Override
	public void emptyDatabase() {
	   	dbManager.open();
		dbManager.emptyDatabase();
		dbManager.close();
		
	}

	@Override
	public void cancelRide(String origin, String destination, Integer rideNumber) {
		dbManager.open();
		dbManager.cancelRide(origin,destination,rideNumber);
		dbManager.close();
	}

	@Override
	public List<Ride> getAllRides() {
		dbManager.open();
		List<Ride> rides=dbManager.getAllRides();
		dbManager.close();
		return rides;
	}

	@Override
	public List<Ride> searchByPrice(Float prezioa) {
		dbManager.open();
		List<Ride> rides=dbManager.searchByPrice(prezioa);
		dbManager.close();
		return rides;
	}


}

