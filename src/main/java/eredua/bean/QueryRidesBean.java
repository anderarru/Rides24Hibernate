package eredua.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import businessLogic.BLFacade;
import domain.Ride;

public class QueryRidesBean {

	BLFacade facadeBL = FacadeBean.getBusinessLogic();
	private String selectedDepartureCity;
	private String selectedArrivalCity;
	private List<String> departureCities = new ArrayList<String>();
	private List<String> arrivalCities = new ArrayList<String>();
	private Date data;
	private List<Ride> rides = new ArrayList<Ride>();

	public QueryRidesBean() {

	    departureCities = facadeBL.getDepartCities();

	    if (!departureCities.isEmpty()) {
	        selectedDepartureCity = departureCities.get(0);
	        arrivalCities = facadeBL.getDestinationCities(selectedDepartureCity);

	        if (!arrivalCities.isEmpty()) {
	            selectedArrivalCity = arrivalCities.get(0);
	        }
	    }

	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public List<Ride> getRides() {
		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}

	public BLFacade getFacadeBL() {
		return facadeBL;
	}

	public void setFacadeBL(BLFacade facadeBL) {
		this.facadeBL = facadeBL;
	}

	public String getSelectedDepartureCity() {
		return selectedDepartureCity;
	}

	public void setSelectedDepartureCity(String selectedDepartureCity) {
		this.selectedDepartureCity = selectedDepartureCity;
	}

	public String getSelectedArrivalCity() {
		return selectedArrivalCity;
	}

	public void setSelectedArrivalCity(String selectedArrivalCity) {
		this.selectedArrivalCity = selectedArrivalCity;
	}

	public List<String> getDepartureCities() {
		return departureCities;
	}

	public void setDepartureCities(List<String> departureCities) {
		this.departureCities = departureCities;
	}

	public List<String> getArrivalCities() {
		return arrivalCities;
	}

	public void setArrivalCities(List<String> arrivalCities) {
		this.arrivalCities = arrivalCities;
	}

	public void updateArrivalCities(AjaxBehaviorEvent event) {
		String selectedDeparture = this.selectedDepartureCity;
		if (selectedDeparture != null && !selectedDeparture.isEmpty()) {
			this.arrivalCities = facadeBL.getDestinationCities(selectedDeparture);
			this.selectedArrivalCity= arrivalCities.get(0);
		} else {
			this.arrivalCities = new ArrayList<String>();
		}
	}

	public void updateTable() {
		String result = "";
	    System.out.println(">>> Finding rides:");
	    System.out.println("Departure City: " + this.selectedDepartureCity);
	    System.out.println("Arrival City: " + this.selectedArrivalCity);
	    System.out.println("Date: " + this.data);

	    if (selectedDepartureCity != null && selectedArrivalCity != null && data != null) {
	        this.rides = facadeBL.getRides(this.selectedDepartureCity, this.selectedArrivalCity, this.data);
	        if(this.rides.isEmpty()) {
	        	result="There are no rides available from "+ this.selectedDepartureCity + " to " + this.selectedArrivalCity +" " +this.data;
	        }
	        else {
	        result="These are the available rides from " + this.selectedDepartureCity + " to " + this.selectedArrivalCity +" " +this.data;
	        }
	    } else {
	        this.rides = new ArrayList<Ride>();
	    }
	    FacesContext.getCurrentInstance().addMessage("mezuakQuery", new FacesMessage(result));
	}

	public void handleDepartureChange(AjaxBehaviorEvent event) {
	    System.out.println("Selected Departure City: " + this.selectedDepartureCity);

	    updateArrivalCities(event);
	    updateTable();
	}

	public void handleArrivalChange(AjaxBehaviorEvent event) {
	    System.out.println("Selected Arrival City: " + this.selectedArrivalCity);

	    updateTable();
	}

	public void handleDateChange() {
	    System.out.println("Selected Date: " + this.data);

	    updateTable();
	}


}
