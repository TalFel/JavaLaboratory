package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import model.User;
import viewModel.UserDB;

@ManagedBean(name="signInBean")
@SessionScoped
public class SignInBean {
	private String cellphone;
	private String password;
	private boolean error;
	
	public SignInBean() {
		error = false;
	}
	
	public String getCellphone() {
		return cellphone;
	}
	public String getPassword() {
		return password;
	}
	public boolean getError() {
		return error;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	
	public String login() {
		UserDB userDB = new UserDB();
		User user = userDB.login(cellphone, password);
		if(user != null) {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			session.setAttribute("user", user);
			error = false;
			return "ongoingAuctions.xhtml";
		}
		else {
			error = true;
			return "signIn.xhtml";
		}
	}

}
