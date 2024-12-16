package main;

import java.util.Calendar;
import java.util.Date;

import businessLogic.BLFacade;
import domain.Ride;
import eredua.bean.FacadeBean;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class InitializeDB {

	public static void main(String[] args) throws RideMustBeLaterThanTodayException, RideAlreadyExistException {
		BLFacade facadeBL=FacadeBean.getBusinessLogic();
		facadeBL.initializeBD();
	}

}
