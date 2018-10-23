package Author;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import App.AppException;
import Database.AuthorGateway;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class Author {
	private int id;
	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleObjectProperty<LocalDate> dateOfBirth;
	private SimpleStringProperty gender;
	private SimpleStringProperty webSite;
	private LocalDateTime lastModified;
	

	private AuthorGateway gateway;
	
	public Author() {
		
		id = 0;
		firstName = new SimpleStringProperty();
		lastName = new SimpleStringProperty();
		dateOfBirth = new SimpleObjectProperty<LocalDate>();
		gender = new SimpleStringProperty("Unkown");
		webSite = new SimpleStringProperty();
		lastModified = null;
		//gateway = new AuthorGateway(null);
	}

	public Author(String firstName, String lastName) {
		this();
		
		setFirstName(firstName);
		setLastName(lastName);
	}
	
	 public ObservableList<authAuditTrail> fetchAuthorAuditTrail() throws AppException{
	        ObservableList<authAuditTrail> authlistReturn = gateway.getAuthAuditTrails(this);
	        return authlistReturn;
	 }
	
	//gateway methods
	public void delete() throws AppException {
		gateway.deleteAuthor(this);
	}
	
	public void save() throws AppException {
		if(getId() == 0) {
			//System.out.println("inserting author called inside Author model");
			if( gateway !=null) {
			gateway.insertAuthor(this);
			}else {
				//System.out.println("gateway is null");
			}
		} else {
			gateway.updateAuthor(this);
		}
	}

	@Override
	public String toString() {
		return getFirstName() + " " + getLastName();
	}

	public void copyValuesFrom(Author source) {
		this.setId(source.getId());
		this.setFirstName(source.getFirstName());
		this.setLastName(source.getLastName());
		this.setGender(source.getGender());
		this.setWebSite(source.getWebSite());
		this.setDateOfBirth(source.getDateOfBirth());
		this.setGateway(source.getGateway());
		this.setLastModified(source.getLastModified());
	}

	//validators
	
	public boolean isValidId(int id) {
		if(id < 0)
			return false;
		return true;
	}

	public boolean isValidFirstName(String firstName) {
		if(firstName == null || firstName.length() < 1 || firstName.length() > 100)
			return false;
		return true;
	}

	public boolean isValidLastName(String lastName) {
		if(lastName == null || lastName.length() < 1 || lastName.length() > 100)
			return false;
		return true;
	}
	
	public boolean isValidGender(String gender) {
		if(gender == null || (!gender.equals("Male") && !gender.equals("Female") && !gender.equals("Unknown")))
			return false;
		return true;
	}

	public boolean isValidWebSite(String webSite) {
		if(webSite == null)
			return true;
		if(webSite.length() > 100)
			return false;
		return true;
	}

	public boolean isValidDateOfBirth(LocalDate dob) {
		if(dob == null || !dob.isBefore(LocalDate.now()))
			return false;
		return true;
	}
	
	//accessors
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}

	public String getLastName() {
		return lastName.get();
	}

	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth.get();
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth.set(dateOfBirth);
	}

	public String getGender() {
		return gender.get();
	}

	public void setGender(String gender) {
		this.gender.set(gender);
	}

	public String getWebSite() {
		return webSite.get();
	}

	public void setWebSite(String webSite) {
		this.webSite.set(webSite);
	}
	
	public SimpleStringProperty getFirstNameProperty() {
		return firstName;
	}

	public SimpleStringProperty getLastNameProperty() {
		return lastName;
	}

	public SimpleObjectProperty<LocalDate> getDateOfBirthProperty() {
		if(dateOfBirth == null){
			dateOfBirth = new SimpleObjectProperty<>();
		}
		return dateOfBirth;
	}
	
	public SimpleStringProperty getGenderProperty() {
		return gender;
	}

	public SimpleStringProperty getWebSiteProperty() {
		return webSite;
	}

	public AuthorGateway getGateway() {
		return gateway;
	}

	public void setGateway(AuthorGateway gateway) {
		this.gateway = gateway;
	}

	public List<Author> getAuthors() {		
		return this.gateway.getAuthors();
	}
	public LocalDateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(LocalDateTime lastModified) {
		this.lastModified = lastModified;
	}
}

	
	
	

