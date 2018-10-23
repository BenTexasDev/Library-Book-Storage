package Book;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import App.AlertHelper;
import App.AppController;
import App.MyController;
import Author.Author;
import Database.AuthorGateway;
import Database.BookGateway;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class authBookDetailController implements Initializable,MyController {
	private static Logger logger = LogManager.getLogger();

    @FXML
    private TextField book_id;

    @FXML
    private TextField royalty;

    @FXML
    private Button buttonAuthBook;
    
    @FXML
    private Button buttonBack;

    @FXML
    private ComboBox<Author> author_id;
    
    private BookGateway gateway;
    private AuthorGateway authGateway;
    private AuthorBook authorBook;
    private ObservableList<Author> authors;
  
    
    public authBookDetailController(){
    	
    }
    


    public authBookDetailController(AuthorBook authorBook) {
    	//this.gateway = gateway;
    	this.authorBook = authorBook;
    	this.gateway = new BookGateway(AppController.getInstance().getConnection());
    	this.authGateway = new AuthorGateway(AppController.getInstance().getConnection());
    	this.authors = authGateway.getAuthors();
    }
    

    

    @FXML public void onSaveClicked() {
    		if (authorBook == null) {
    			authorBook = new AuthorBook();
    		}
    		authorBook.setAuthor(author_id.getValue());
    		String rs = royalty.getText();
    		int royalty = Integer.parseInt(rs);
    		if (royalty > 100) {
    			AlertHelper.showWarningMessage("STOP!", "You cannot enter more than 100%", "");
    			return;
    		}
    		authorBook.setRoyalty(royalty);
    		authorBook.save();
    		String msg = "Updated author info " + authorBook.getAuthor() + "connected to book";
    		gateway.insertIntoAuditTrail(authorBook.getBook().getId(), msg);
    		AlertHelper.showSuccessMessgae("Royalty", "Royalty Saved", "You have saved a new royalty");
    }
    
    @FXML public void onBackClicked(ActionEvent event) {
    	AppController.getInstance().changeView(AppController.BOOK_DETAIL,authorBook.getBook());
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		author_id.getSelectionModel().select(authorBook.getAuthor());;; //trying to set Unkown to default
		author_id.setItems(authors);
		String rs = authorBook.getRoyalty()+ "";
		royalty.setText(rs);
		//royalty.textProperty().bindBidirectional(authorBook.getRoyalty());
		//quthor_id.valueProperty().bindBidirectional(authorBook.)
		//publisherCombo.valueProperty().bindBidirectional(book.publisherProperty());
		
	}

}