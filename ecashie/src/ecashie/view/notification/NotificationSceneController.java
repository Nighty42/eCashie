package ecashie.view.notification;

import ecashie.controller.utilities.Notification;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class NotificationSceneController
{
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private Label content;
	@FXML
	private ImageView graphic;

	@FXML
	private void initialize()
	{
		content.setText(Notification.text);
		graphic.setId(Notification.type);
	}

	@FXML
	private void onMouseEnteredAnchorPane()
	{
		Notification.cancelAnimation();
	}

	@FXML
	private void onMouseExitedAnchorPane()
	{
		Notification.restartAnimation();
	}

	@FXML
	private void onActionCloseButton()
	{
		Notification.closeNotification();
	}
}