package eredua.bean;

import java.util.ArrayList;
import java.util.List;

import businessLogic.BLFacade;
import domain.Ride;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;

public class CancelRideBean {

    BLFacade facadeBL = FacadeBean.getBusinessLogic();

    private Ride selectedRide;
    private ListDataModel<Ride> rides;

    public BLFacade getFacadeBL() {
        return facadeBL;
    }

    public void setFacadeBL(BLFacade facadeBL) {
        this.facadeBL = facadeBL;
    }

    public CancelRideBean() {
 
        updateRidesList();
    }

    public ListDataModel<Ride> getRides() {
        return rides;
    }

    public void setRides(ListDataModel<Ride> rides) {
        this.rides = rides;
    }

    public Ride getSelectedRide() {
        return selectedRide;
    }

    public void setSelectedRide(Ride selectedRide) {
        this.selectedRide = selectedRide;
    }

  
    public void updateRidesList() {
        List<Ride> rideList = facadeBL.getAllRides();
        this.rides = new ListDataModel<Ride>(rideList);
    }


    public void cancel() {
        String msg = "";
        if (selectedRide == null) {
            msg = "You have to select a ride!";
        } else {
            facadeBL.cancelRide(selectedRide.getOrigin(), selectedRide.getDestination(), selectedRide.getRideNumber());
            msg = "Ride from " + selectedRide.getOrigin() + " to " + selectedRide.getDestination() + " " + selectedRide.getDate() + " deleted.";


            updateRidesList();
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
    }
}
