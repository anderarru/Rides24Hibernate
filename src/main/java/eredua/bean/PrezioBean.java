package eredua.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import domain.Ride;

public class PrezioBean {

	BLFacade facadeBL=FacadeBean.getBusinessLogic();

	private Float prezioa;
	private List<Ride> rides = new ArrayList<Ride>();


	public PrezioBean() {}


	public BLFacade getFacadeBL() {
		return facadeBL;
	}

	public void setFacadeBL(BLFacade facadeBL) {
		this.facadeBL = facadeBL;
	}


	public Float getPrezioa() {
		return prezioa;
	}


	public void setPrezioa(Float prezioa) {
		this.prezioa = prezioa;
	}


	public List<Ride> getRides() {
		return rides;
	}


	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}

	public String searchByPrice() {
		this.rides=facadeBL.searchByPrice(prezioa);
		return "erakutsi";
	}
	

	

}
