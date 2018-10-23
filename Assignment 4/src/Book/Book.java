package Book;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import App.AppException;
import Author.Author;
import Database.BookGateway;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Book {
	private int id;
	private SimpleStringProperty title;
	private SimpleStringProperty summary;
	private SimpleIntegerProperty yearPublished;
	private SimpleObjectProperty<Publisher> publisher;
	private SimpleStringProperty isbn;
	private SimpleObjectProperty<LocalDate> dateAdded;
	
	private BookGateway gateway;
	
	public Book() {
		id =0;
		title = new SimpleStringProperty();
		summary = new SimpleStringProperty();
		yearPublished = new SimpleIntegerProperty();
		publisher = new SimpleObjectProperty<Publisher>();
		isbn = new SimpleStringProperty();
		dateAdded = new SimpleObjectProperty<LocalDate>();
		
	}
	
	public Book(String title) {
		this();
		setTitle(title);
	}
	public ObservableList<AuditTrailEntry> fetchBookAuditTrail() throws AppException{
		ObservableList<AuditTrailEntry> listReturn = gateway.getAuditTrails(this);
		return listReturn;
	}
	public void copyValuesFrom(Book source) {
		this.setId(source.getId());
		this.setTitle(source.getTitle());
		this.setSummary(source.getSummary());
		this.setYearPublished(source.getYearPublished());
		this.setPublisher(source.getPublisher());
		this.setISBN(source.getISBN());
		//this.setDateAdded(source.getDateAdded();
		this.setGateway(source.getGateway());
	}
	@Override
	public String toString() {
		return getTitle() + " Published by " + getPublisher() + " summary is " + getSummary() + " ISBN is " + getISBN();
	}
	
	//gateway methods
	//TO DO
	public void delete() throws AppException {
		gateway.deleteBook(this);
	}
	
	public void save() throws AppException {
		if(getId() == 0) {			
			if( gateway !=null) {
			gateway.insertBook(this);
			}else {
				System.out.println("book gateway is null");
			}

		} else {
			gateway.updateBook(this);
		}
	}
	
	
	
	//validators
	public boolean isValidId(int id) {
		if(id < 0)
			return false;
		return true;
	}
	
	public boolean isValidTitle(String title) {
		if(title == null || title.length() < 1 ||  title.length() > 255) {
			return false;
		}
		return true;
	}
	
	public boolean isValidSummary(String summary) {
		if(summary.length() < 0 || summary.length() > 65536 )
			return false;
		return true;
	}
	
	public boolean isValidYearPublished(int year) {
		if( year > Calendar.getInstance().get(Calendar.YEAR))
			return false;
		return true;
	}
	
	public boolean isValidIsbn(String isbn) {
		if(isbn.length() > 13)
			return false;
		return true;
	}
	
	//TO DO: 3a.e vallidation
	
	// accessors
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title.get();
	}

	public void setTitle(String title) {
		this.title.set(title);
	}
	
	public String getSummary() {
		return summary.get();
	}

	public void setSummary(String summary) {
		this.summary.set(summary);
	}
	
	public int getYearPublished() {
		return yearPublished.get();
	}
	
	public void setYearPublished(int yearPublished) {
		this.yearPublished.set(yearPublished);
	}
	
	public Publisher getPublisher() {
		return publisher.get();
	}
	public void setPublisher(Publisher publisher) {
		this.publisher.set(publisher);
	}
	
	public String getISBN() {
		return isbn.get();
	}

	public void setISBN(String isbn) {
		this.isbn.set(isbn);
	}
	
	public LocalDate getDateAdded() {
		return dateAdded.get();
	}

	public void setDateAdded(LocalDate dateAdded) {
		this.dateAdded.set(dateAdded);
	}
	//java fx getters
	public SimpleStringProperty getTitleProperty() {
		return title;
	}
	
	public SimpleStringProperty getSummaryProperty() {
		return summary;
	}
	
	public SimpleIntegerProperty getYearPublishedProperty() {
		return yearPublished;	
	}
	
	public SimpleObjectProperty<Publisher> publisherProperty() {
		return publisher;
	}

	public SimpleStringProperty getIsbnProperty() {
		return isbn;
	}
	
	public SimpleObjectProperty<LocalDate> getDateAddedProperty() {
		if(dateAdded == null){
			dateAdded = new SimpleObjectProperty<>();
		}
		return dateAdded;
	}
	
	
	public BookGateway getGateway() {
		return gateway;
	}

	public void setGateway(BookGateway gateway) {
		this.gateway = gateway;
	}
	
	public List<Book> getBooks() {		
		return this.gateway.getBooks();
	}
	
	public List<AuthorBook> getAuthors(){
		return this.gateway.getAuthorsForBook(this.getId());
	}
	
	
}
