package Book;

import javafx.scene.control.TextArea;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import App.AppController;
import App.MyController;
import Author.Author;
import Database.BookGateway;
import App.AlertHelper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.NumberStringConverter;

public class bookDetailController  implements Initializable, MyController {
	private static Logger logger = LogManager.getLogger();
	
	
		@FXML
	    private ComboBox<Publisher> publisherCombo;

	    @FXML
	    private Button saveBook;

	    @FXML
	    private TextField bookName;

	    @FXML
	    private TextField isbnText;

	    @FXML
	    private TextField yearPublished;

	    @FXML
	    private TextArea summaryTextArea;
	    
	    @FXML
	    private Button auditButton;
	    
	    @FXML
	    private ListView<AuthorBook> listViewAuthors;
	    
	    @FXML
	    private Button addAuthor;
	    
	    @FXML
	    private Button deleteAuthor;

	    
	  
	  private Book book;
	  private Book oldBook = new Book();
	  private ObservableList<Publisher> publishers;
	  private ObservableList<AuthorBook> authors;
	  
	  private BookGateway gateway;
	  
	  public bookDetailController(){
		  
	  }
	  
	  
	  public bookDetailController(Book book, ObservableList<Publisher> publishers) {
	    	this();
	    	
	    	this.book = book;
	    this.oldBook.copyValuesFrom(book);
	    	this.publishers = publishers;
	    	this.gateway = new BookGateway(AppController.getInstance().getConnection());
	    }
	  
	  //
	  @FXML void onSaveBookClicked(ActionEvent event) {
	    	Book testBook = new Book();
	    	testBook.copyValuesFrom(book);
	   
	    	if(!testBook.isValidTitle(testBook.getTitle())) {
	    		logger.error("Invalid book name " + testBook.getTitle());
	    	}else if(!testBook.isValidIsbn(testBook.getISBN())) {
	    		logger.error("Invalid Isbn " + testBook.getISBN());
	    	}else if(!testBook.isValidSummary(testBook.getSummary())) {
	    		logger.error("Invalid summary " + testBook.getSummary());
	    	}else if(!testBook.isValidYearPublished(testBook.getYearPublished())) {
	    		logger.error("Invalid year published " + testBook.getYearPublished());
	    	}else {
	    		book.copyValuesFrom(testBook);
	    		book.setGateway(gateway);
	    		book.save();
	    		if(oldBook.getTitle() != null) {
		    		System.out.println(book.toString() + " " + oldBook.toString());
		    		String message = book.getGateway().createAuditMessage(book, oldBook);
		    		book.getGateway().insertIntoAuditTrail(book.getId(), message);
	    		}
	    		AlertHelper.showSuccessMessgae("Book Saved", "Book Saved", "You saved " + book.getTitle());
	    	}
	    
	    }
	  
	  @FXML void onAuditButtonClick(ActionEvent event) {
		  logger.info("Audit button clicked for " + book.getTitle());
		  if(book.getId() == 0) {
	    		AlertHelper.showWarningMessage("STOP!", "Save the new Book record first!","Before clicking on the Audit Trail Button!");
	    		return;
	    	}
		  AppController.getInstance().changeView(AppController.AUDIT_DETAIL, book);

	    }
	 @FXML void onAddButtonClick(ActionEvent event) {
		 AuthorBook author = new AuthorBook();
		 author.setBook(book);
		 AppController.getInstance().changeView(AppController.AUTHOR_BOOK, author);
		 
	 }
	 @FXML void onDeletButtonClick(ActionEvent event) {
		 //delete AuthorBook reference
		 //add audit trail to book audit trail describing the delete
		 if (listViewAuthors.getSelectionModel().getSelectedItem() == null){
			 return;
		 }else{
			 AuthorBook authBook = (listViewAuthors.getSelectionModel().getSelectedItem());
			 gateway.deleteAuthorBook(authBook.getAuthor(), authBook.getBook());
			 String msg = "Deleted author " + authBook.getAuthor().getFirstName() + " " + authBook.getAuthor().getLastName() + " as an author of this book";
			 gateway.insertIntoAuditTrail(authBook.getBook().getId(), msg);
		 }
	 }
	 @FXML void listViewClicked(MouseEvent event) {
	    	if(event.getClickCount() > 1) {
	    		AuthorBook author = listViewAuthors.getSelectionModel().getSelectedItem();
	    		System.out.println(author);
	   			if(author != null) {
	   				author.setBook(book);
	   				AppController.getInstance().changeView(AppController.AUTHOR_BOOK, author);
	   				logger.info(author.toString() + " clicked");
	    		}
	    	}
	 }
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {		// TODO Auto-generated method stub
		bookName.textProperty().bindBidirectional(book.getTitleProperty());
		summaryTextArea.textProperty().bindBidirectional(book.getSummaryProperty());
		isbnText.textProperty().bindBidirectional(book.getIsbnProperty());
		//yearPublished.textProperty().bind(book.getYearPublishedProperty().toString());
		yearPublished.textProperty().bindBidirectional(book.getYearPublishedProperty(),new NumberStringConverter("####"));
		
		publisherCombo.getSelectionModel().selectFirst(); //trying to set Unkown to default
		publisherCombo.setItems(publishers);
        publisherCombo.valueProperty().bindBidirectional(book.publisherProperty());
        //System.out.println("book id is " + book.getId());
        this.authors = this.gateway.getAuthorsForBook(this.book.getId());
        this.listViewAuthors.setItems(authors);
	}



}


