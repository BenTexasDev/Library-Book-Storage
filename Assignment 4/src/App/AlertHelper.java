package App;
import javafx.scene.control.Alert;
//import javafx.scene.control.AlertType;
import javafx.scene.control.Alert.AlertType;

public class AlertHelper {
	
	public static void showWarningMessage(String title, String header, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	public static void showSuccessMessgae(String title, String header, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
