package Author;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import App.AppController;
import App.MyController;
import Database.AuthorGateway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class authListController implements Initializable, MyController {
	private static Logger logger = LogManager.getLogger();

	@FXML
	private ListView<Author> listViewAuthors;
	private ObservableList<Author> authors;
	@FXML
    private Button DeleteButton;
	
	
	private AuthorGateway gateway;

	public authListController(AuthorGateway gateway) {
		this.gateway = gateway;
		authors = this.gateway.getAuthors();
	}
	
	public authListController(ObservableList<Author> authors) {
    	this.authors = authors;
    	
    }

	
	@FXML void listViewClicked(MouseEvent event) {
    	if(event.getClickCount() > 1) {
    		Author author = listViewAuthors.getSelectionModel().getSelectedItem();
    		System.out.println(author);
   			if(author != null) {
    			AppController.getInstance().changeView(AppController.AUTHOR_DETAIL, author);
        		logger.info(author.getFirstName() + author.getLastName() + " clicked");
    		}
    	}
    }
	
	 @FXML
	    void onDeleteClicked(ActionEvent event) {
		 	gateway.deleteAuthor(listViewAuthors.getSelectionModel().getSelectedItem());
	    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.listViewAuthors.setItems(authors);

	}
}