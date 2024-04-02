package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import model.User;
import viewModel.UserDB;

@ManagedBean(name="signUpBean")
@SessionScoped
public class SignUpBean {
	private User user;
	private String password;
	private boolean error;
	
	public SignUpBean() {
		error = false;
		user = new User();
	}
	
	public boolean getError() {
		return error;
	}
	public User getUser() {
		return user;
	}
	public String getPassword() {
		return password;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String logup() {
		UserDB userDB = new UserDB();
		if(!userDB.exists(user)) {
			userDB.INSERT(user, password);
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			session.setAttribute("user", user);
			error = false;
			return "ongoingAuctions.xhtml";
		}
		else {
			error = true;
			return "signUp.xhtml";
		}
	}

}
