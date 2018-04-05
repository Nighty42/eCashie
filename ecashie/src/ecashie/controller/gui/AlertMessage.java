package ecashie.controller.gui;

import java.util.Optional;

import ecashie.controller.i18n.LanguageController;
import ecashie.main.MainApp;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AlertMessage
{	
	public static String title = "Missing parameters";
	public static String header = "Missing header";
	public static String content = "No text was specified for notification.";

	public static Optional<ButtonType> yesNo(String messageKey, String[] contentArgs)
	{
		initializeMessageStrings(messageKey, contentArgs);

		Alert alert = initializeAlert();

		initializeAlertIcon(alert);

		initializeAlertStyle(alert);

		return alert.showAndWait();
	}
	
	private static void initializeMessageStrings(String messageKey, String[] contentArgs)
	{
		messageKey = "message." + messageKey;
		title = LanguageController.getLocaleString(messageKey + ".title", null);
		header = LanguageController.getLocaleString(messageKey + ".header", null);
		content = LanguageController.getLocaleString(messageKey + ".content", contentArgs);
	}

	private static Alert initializeAlert()
	{
		Alert alert = new Alert(AlertType.CONFIRMATION, content, ButtonType.NO, ButtonType.YES);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setResizable(false);
		
		return alert;
	}
	
	private static void initializeAlertIcon(Alert alert)
	{
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/ecashie/resources/img/logo_32x32.png")));
	}
	
	private static void initializeAlertStyle(Alert alert)
	{
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(MainApp.class.getResource("/ecashie/resources/css/general.css").toExternalForm());
	}
}
