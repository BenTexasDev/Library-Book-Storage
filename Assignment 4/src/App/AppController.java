package App;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Author.Author;
import Author.authAuditViewController;
import Author.authDetailController;
import Author.authListController;
import Book.AuthorBook;
import Book.Book;
import Book.Publisher;
import Book.auditViewController;
import Book.authBookDetailController;
import Book.bookDetailController;
import Book.bookListController;
import Database.AuthorGateway;
import Database.BookGateway;
import Database.PublisherGateway;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

//import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
//import javafx.stage.Stage;




public class AppController implements Initializable{
	
	public static final int AUTHOR_LIST = 1;
	public static final int AUTHOR_DETAIL = 2;
	public static final int BOOK_LIST = 3;
	public static final int BOOK_DETAIL = 4;
	public static final int AUDIT_DETAIL = 5;
	public static final int AUTHOR_AUDIT = 0;
	public static final int AUTHOR_BOOK = 7;
	
	private static Logger logger = LogManager.getLogger();
	private static AppController instance = null;
	
    @FXML private BorderPane rootPane = null;
    @FXML private MenuBar menuBar;
    @FXML private MenuItem menuItemList;
    @FXML private MenuItem menuItemQuit;
    
    
    //private BorderPane rootPane1 = null;
	
	private Connection connection;

    /**
     * Returns the instance of this class.
     * @return
     */
    public static AppController getInstance() {
    	if(instance == null) {
    		instance = new AppController();
		}
		return instance;
    }
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {

	}
 
    public void changeView(int viewType, Object arg) throws AppException {
		try {
			MyController controller = null;
			URL fxmlFile = null;
			switch(viewType) {
				case AUTHOR_LIST:
					fxmlFile = this.getClass().getResource("/Author/ListView.fxml");
					controller = new authListController(new AuthorGateway(connection));
					break;
				case AUTHOR_DETAIL:
					fxmlFile = this.getClass().getResource("/Author/DetailView.fxml");
					//AuthorGateway gateway = new AuthorGateway(connection);
					controller = new authDetailController((Author) arg);
					break;
				case BOOK_DETAIL:
					fxmlFile = this.getClass().getResource("/Book/bookDetailView.fxml");
					PublisherGateway pubGateway = new PublisherGateway(connection);		
					controller = new bookDetailController((Book) arg,pubGateway.getPublishers());
					break;
				case BOOK_LIST:
					controller = new bookListController(new BookGateway(connection));
					fxmlFile = this.getClass().getResource("/Book/bookListView.fxml");
					break;
				case AUDIT_DETAIL:
					controller = new auditViewController((Book) arg); //need to pass param
					fxmlFile =this.getClass().getResource("/Book/AuditView.fxml");
					break;
				case AUTHOR_AUDIT:
					controller = new authAuditViewController((Author)arg);
					fxmlFile =this.getClass().getResource("/Author/authAuditView.fxml");
					break;
				case AUTHOR_BOOK	:
					controller = new authBookDetailController((AuthorBook)arg);
					fxmlFile = this.getClass().getResource("/Book/authBookDetailView.fxml");
			}
		
			FXMLLoader loader = new FXMLLoader(fxmlFile);
			loader.setController(controller);
			
			Parent viewNode = loader.load();
			rootPane.setCenter(viewNode);
			
		} catch (IOException e) {
			throw new AppException(e);
		}
	}


    
    @FXML
    void clickMenuAuthorList(ActionEvent event) {
		logger.info("Author menu item clicked");
		changeView(AUTHOR_LIST, null);
    }
    
    @FXML
    void clickMenuAuthorAdd(ActionEvent event) {
		logger.info("Author menu item clicked");
		Author newAuth = new Author();
		changeView(AUTHOR_DETAIL, newAuth);
    }
	
	@FXML
    void clickMenuQuit(ActionEvent event) {
		logger.info("Quit menu item clicked");
		Platform.exit();
    }
	@FXML
	void clickMenuBookList(ActionEvent event) {
		logger.info("Book menu list clicked");
		changeView(BOOK_LIST,null);
	}
	@FXML
	void clickMenuBookAdd(ActionEvent event) {
		logger.info("Book menu add clicked");
		Book newBook = new Book();
		changeView(BOOK_DETAIL, newBook);
	}
    
    public BorderPane getRootPane() {
		return rootPane;
	}

	public void setRootPane(BorderPane rootPane) {
		this.rootPane = rootPane;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection conn) {
		this.connection = conn;
	}
		
  

}
