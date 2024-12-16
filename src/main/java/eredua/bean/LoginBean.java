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

public class LoginBean {

	BLFacade facadeBL=FacadeBean.getBusinessLogic();

	private String username;
	private String password;
	private boolean logged=false;
	private String mota;


	public LoginBean() {}


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

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public String getMota() {
		return mota;
	}


	public void setMota(String mota) {
		this.mota = mota;
	}


	public String login() {
		if (facadeBL.login(username, password)) {
			this.logged=true;
			return "onartu";
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erabiltzailea edo pasahitza gaizki daude"));
			return null;
		}
	}


	public String logout() {
		this.logged=false;
		this.username=null;
		return "menu";
	}

	

}
