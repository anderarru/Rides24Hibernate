package eredua.bean;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import businessLogic.BLFacade;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;


public class CreateRideBean {
	
	
	BLFacade facadeBL=FacadeBean.getBusinessLogic();
	
	private String departure;
	private String arrival;
	private int seats;
	private float price;
	private Date data;


	public BLFacade getFacadeBL() {
		return facadeBL;
	}


	public void setFacadeBL(BLFacade facadeBL) {
		this.facadeBL = facadeBL;
	}


	public CreateRideBean() {}
	
	
	public String getDeparture() {
		return departure;
	}


	public void setDeparture(String departure) {
		this.departure = departure;   
	}


	public String getArrival() {
		return arrival;
	}


	public void setArrival(String arrival) {
		this.arrival = arrival;
	}


	public int getSeats() {
		return seats;
	}


	public void setSeats(int seats) {
		this.seats = seats;
	}


	public float getPrice() {
		return price;
	}


	public void setPrice(float price) {
		this.price = price;
	}


	public Date getData() {
		return data;
	}


	public void setData(Date data) {
		this.data = data;
	}
	
	public Ride egiaztatu() {
		String result = "";
		Ride ret =null;
	    try {
	    	if (this.getSeats()>0) {
	    		ret=facadeBL.createRide(departure, arrival, data, seats, price, "driver3@gmail.com");
	    		result = "Ride created successfully: From "+ ret.getOrigin()+ " To " +ret.getDestination() +" " + ret.getDate() ;
	    	}
	    	else result = "Error! Seat number must be greater than 0.";
	    } catch (RideMustBeLaterThanTodayException e) {
	        result = "Error! Ride must be later than today.";
	    } catch (RideAlreadyExistException e) {
	        result = "Error! Ride already exists.";
	    }
	    finally {
			System.out.println(result);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(result));
		}
	    return ret;
	}

}
