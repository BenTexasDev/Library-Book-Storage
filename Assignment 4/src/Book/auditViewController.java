package Book;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import App.AppController;
import App.MyController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


public class auditViewController implements Initializable, MyController{
	@FXML
    private Label labelBooktitle;

    @FXML
    private ListView<AuditTrailEntry> listView;
    
    private ObservableList<AuditTrailEntry> entries;
    
    private Book book;
    
    public auditViewController(Book book) {
    		this.book = book;
    		this.entries = book.fetchBookAuditTrail();
    	
    }
    @FXML
    void backClicked(ActionEvent event) {
    	AppController.getInstance().changeView(AppController.BOOK_DETAIL, book);

    }
    
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {	
    		labelBooktitle.setText("Audit for " + book.getTitle());
    		entries.sort((o1, o2) -> o2.getDateAdded().compareTo(o1.getDateAdded()));
    		this.listView.setItems(entries);
    	
    }
    
}
