package Author;

import java.net.URL;
import java.util.ResourceBundle;

import App.AppController;
import App.MyController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class authAuditViewController implements Initializable, MyController{
	 @FXML
	    private Label labelAuthtitle;

	    @FXML
	    private ListView<authAuditTrail> authListView;
	    
	    private ObservableList<authAuditTrail> authEntries;
	    
	    private Author author;
	    
	    public authAuditViewController(Author author) {
            this.author = author;
            this.authEntries = author.fetchAuthorAuditTrail();
    
	    }
	    
	    @FXML
        void backButtonClicked(ActionEvent event) {
	    	AppController.getInstance().changeView(AppController.AUTHOR_DETAIL, author);

    }



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		 labelAuthtitle.setText("Audit for " + author.getFirstName());
         authEntries.sort((o1, o2) -> o2.getAuthDateAdded().compareTo(o1.getAuthDateAdded()));
         this.authListView.setItems(authEntries);
		
	}

}
