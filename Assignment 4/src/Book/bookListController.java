package Book;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import App.AppController;
import App.MyController;
import Database.BookGateway;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class bookListController implements Initializable, MyController {
	private static Logger logger = LogManager.getLogger();
	
    @FXML private Button searchButton;
    @FXML private TextField searchTxt;
    @FXML private ListView<Book> listViewBooks;
    @FXML private Button deleteBook;
    private ObservableList<Book> books;
    
    private BookGateway gateway;
    
    
    public bookListController(BookGateway gateway) {
	this.gateway = gateway;
	books = this.gateway.getBooks();
    }
    
    public bookListController(Book book,ObservableList<Book>books) {
    	this.books = books;
    	
    }
    
	@FXML void booklistViewClicked(MouseEvent event) {
    	if(event.getClickCount() > 1) {
    		Book book = listViewBooks.getSelectionModel().getSelectedItem();
    		System.out.println(book);
   			if(book != null) {
    			AppController.getInstance().changeView(AppController.BOOK_DETAIL, book);
        		logger.info(book.getTitle() + " clicked");
    		}
    	}
    }
	
	@FXML void onSearchClicked(ActionEvent event){
		books = this.gateway.searchBooks(searchTxt.getText());
		this.listViewBooks.setItems(books);
		
	}
	
	@FXML
    void onDeleteBookClicked(ActionEvent event) {
        gateway.deleteBook(listViewBooks.getSelectionModel().getSelectedItem());
    }
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.listViewBooks.setItems(books);
		//searchTxt.textProperty().bindBidirectional();
		
	}

}