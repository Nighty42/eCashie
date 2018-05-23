package ecashie.controller.exception;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import ecashie.controller.gui.HyperlinkUtils;
import ecashie.controller.logging.ApplicationLogger;
import ecashie.main.MainApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GeneralExceptionHandler
{
	public static void generateAlert(String messageHeader, String messageContent, String hyperlink)
	{
		Alert alert = createExceptionAlert(messageHeader, messageContent, hyperlink);

		Optional<ButtonType> result = alert.showAndWait();

		evaluateButtonResultOpenHyperlink(result, hyperlink);
	}

	private static Alert createExceptionAlert(String messageHeader, String messageContent, String hyperlink)
	{
		String logMessage = ApplicationLogger.getMessages().get(0);
		int rows = StringUtils.countMatches(logMessage, "\tat ") + 2;

		Alert alert = new Alert(AlertType.ERROR, messageContent, ButtonType.NO, ButtonType.YES);
		alert.setTitle("eCashie - Error");
		alert.setHeaderText(messageHeader);
		alert.setResizable(false);

		Label label = new Label("Exception Stacktrace:");
		TextArea textArea = new TextArea(logMessage);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setPrefRowCount(rows);
		textArea.setMaxHeight(250);

		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expandableContentGrid = new GridPane();
		expandableContentGrid.setMinWidth(650);
		expandableContentGrid.setMaxHeight(250);
		expandableContentGrid.add(label, 0, 0);
		expandableContentGrid.add(textArea, 0, 1);

		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		
		alert.getDialogPane().setExpandableContent(expandableContentGrid);
		alert.getDialogPane().expandedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldBoolean, Boolean newBoolean)
			{
		        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		        alertStage.setX((screenBounds.getWidth() - alertStage.getWidth()) / 2);
		        alertStage.setY((screenBounds.getHeight() - alertStage.getHeight()) / 2);
			}
		});
		
		alertStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/ecashie/resources/img/logo_32x32.png")));

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(MainApp.class.getResource("/ecashie/resources/css/General.css").toExternalForm());

		return alert;
	}

	public static void evaluateButtonResultOpenHyperlink(Optional<ButtonType> result, String hyperlink)
	{
		if (result.get() == ButtonType.YES)
		{
			HyperlinkUtils.openHyperlink(hyperlink);
		}
	}

	public static void logException(Exception e)
	{
		ApplicationLogger.logException(e);
	}
}
