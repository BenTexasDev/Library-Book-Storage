package App;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Database.DatabaseConnectFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/*CS 4743 Assignment 4 by Aaron Franco and Ben Barela
 * 
 */
public class Launcher extends Application {
	private static Logger logger = LogManager.getLogger();

	private Connection conn;
	
	@Override
	public void start(Stage stage) throws Exception {
		
		URL fxmlFile = this.getClass().getResource("appView.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);
			
		try {
			AppController controller = AppController.getInstance();
			controller.setConnection(conn);
			
			loader.setController(controller);
			
			Parent root = loader.load();
			controller.setRootPane((BorderPane) root);
			
			Scene scene = new Scene(root, 600, 400);
		    
			stage.setTitle("CS 4743 Assignment 4");
			stage.setScene(scene);
			stage.show();
			
		

		} catch (IOException exception) {
			logger.error("failed to load ");
			exception.printStackTrace();
		}

	}

	@Override
	public void init() throws Exception {
		super.init();
		
		logger.info("Creating connection...");
		
		try {
			conn = DatabaseConnectFactory.createConnection();
		} catch(AppException e) {
			logger.fatal("Cannot connect to db");
			Platform.exit();
		}
	}

	@Override
	public void stop() throws Exception {
		logger.info("Entered stop()...");
		super.stop();
		logger.info("Closing connection...");
		
		conn.close();
		logger.info("Exiting stop()...");
	}

	public static void main(String[] args) {
		logger.info("Entered main()...");

		launch(args);

		logger.info("Exiting main()...");
	}

}
