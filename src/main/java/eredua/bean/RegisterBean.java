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
import domain.User;
import exceptions.UserAlreadyExist;

public class RegisterBean {

	BLFacade facadeBL = FacadeBean.getBusinessLogic();

	private String username;
	private String password;

	public RegisterBean() {
	}

	public BLFacade getFacadeBL() {
		return facadeBL;
	}

	public void setFacadeBL(BLFacade facadeBL) {
		this.facadeBL = facadeBL;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String register() {
		System.out.println(username);
		System.out.println(password);
		String message;
		if (this.username != null && !this.username.isEmpty() && this.password != null && !this.password.isEmpty()) {
			try {
				User u = facadeBL.register(username, password, "User");
				message = "User registered: " + u;
				this.username = null;
				System.out.println(message);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
				return "onartu"; 
			} catch (UserAlreadyExist e) {
				message = "Error: user already exists";
			}
		} else {
			message = "Error: you must write a username and a password";
		}
		System.out.println(message);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
		return null;
	}

}
