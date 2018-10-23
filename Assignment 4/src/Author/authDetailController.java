package Author;
import java.net.URL;
import java.text.Format;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import App.AlertHelper;
import App.AppController;
import App.MyController;
import Database.AuthorGateway;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.converter.LongStringConverter;

public class authDetailController implements Initializable, MyController{
	
	private static Logger logger = LogManager.getLogger();
	
	@FXML private Button saveButton;

    @FXML private DatePicker authDOB;

    @FXML private TextField authFirst;

    @FXML private TextField authLast;

    @FXML private TextField authSite;

    @FXML private Button auditButton;

    @FXML private ComboBox<String> authGender;
    
    private Author author;
    private Author oldAuthor = new Author();
    private AuthorGateway gateway;
    
    private ObservableList<String> genderTypes = 
    	    FXCollections.observableArrayList(
    	        "Male",
    	        "Female",
    	        "Unkown"
    	    );
  
    
    
    public authDetailController() {
    	
    }
    
    public authDetailController(Author author) {
    	this();
    	
    	this.author = author;
    	this.oldAuthor.copyValuesFrom(author);
    	this.gateway = new AuthorGateway(AppController.getInstance().getConnection());
 
   
    }
    
    
    @FXML void onSaveClicked(ActionEvent event) {
    		logger.info("Save clicked");  
    		Author testAuthor = new Author();
    		testAuthor.copyValuesFrom(author);
    		//testAuthor.setDateOfBirth();
    		//testAuthor.setFirstName(authFirst.toString());
    		//testAuthor.setLastName(authLast.toString());
    		//testAuthor.setGender(authGender.getValue());
    		//testAuthor.setWebSite(authSite.toString());
    		
    		if(!testAuthor.isValidFirstName(testAuthor.getFirstName())) {
    			logger.info("Not valid first name");    			
    		}else if(!testAuthor.isValidLastName(testAuthor.getLastName())) {
    			logger.info("Not valid last name"); 
    		}else if (!testAuthor.isValidGender(testAuthor.getGender())) {
    			logger.info("not valid gender " + testAuthor.getGender()); 
    		}else if (!testAuthor.isValidWebSite(testAuthor.getWebSite())){
    			logger.info("Not valid web site"); 
    		}else if (!testAuthor.isValidId(testAuthor.getId())) {
    			logger.info("Not valid id"); 
    		}else {
    			//System.out.println(author.getFirstName() + " " + author.getLastName() + " " + author.getId() + " " + author.getGender() + " " + author.getWebSite());
    			author.copyValuesFrom(testAuthor);
    			//System.out.println(AppController.getInstance().);
    			//author.setGateway(AppController.getInstance().g);
    			author.setGateway(this.gateway);
    			author.save();
    			String message = author.getGateway().createAuditMessage(oldAuthor,author);
	    		author.getGateway().insertIntoAuditTrail(author.getId(), message);
	    		AppController.getInstance().changeView(AppController.AUTHOR_DETAIL,author);
    		}
    }
    
    @FXML void onAuthorAuditClick(ActionEvent event) {
        logger.info("Audit button clicked for " + author.getFirstName());
        if(author.getId() == 0) {
              AlertHelper.showWarningMessage("STOP!", "Save the new Author first!","Before clicking on the Audit Trail Button!");
              return;
          }
        AppController.getInstance().changeView(AppController.AUTHOR_AUDIT, author);

      }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	authGender.setItems(genderTypes);
	    //	if(author != null) {
	    		//authGender.setItems(genderTypes);
	    		authFirst.textProperty().bindBidirectional(author.getFirstNameProperty());
	    		authLast.textProperty().bindBidirectional(author.getLastNameProperty());
	    		authDOB.valueProperty().bindBidirectional(author.getDateOfBirthProperty());
	    		authSite.textProperty().bindBidirectional(author.getWebSiteProperty());
	    		authGender.valueProperty().bindBidirectional(author.getGenderProperty());
	    	//	}
			
		}

}
