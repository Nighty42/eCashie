package ecashie.controller.gui;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Area;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import ecashie.controller.exception.UnexpectedBehaviourException;
import ecashie.controller.i18n.LanguageController;
import ecashie.main.MainApp;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.util.Duration;

public class Notification
{
	public static String text = "No text was specified for notification.";
	public static String type = "error";
	
	private static final URL urlScene = MainApp.class.getResource("/ecashie/view/notification/NotificationScene.fxml");
	
	private static Popup notificationStage;
	private static Timeline timeline;
	private static Timer timer;

	public static void info(String messageKey)
	{
		initNotification(messageKey, "info");
	}

	public static void warning(String messageKey)
	{
		initNotification(messageKey, "warning");
	}

	public static void error(String messageKey)
	{
		initNotification(messageKey, "error");
	}

	private static void initNotification(String messageKey, String notificationType)
	{
		if (notificationStage == null || !notificationStage.isShowing())
		{
			initNotificationStrings(messageKey);

			initNotificationType(notificationType);

			Area area = calculatePositionNearTray();

			initNotificationStage(area);

			initAnimation();

			notificationStage.show(GuiBuilder.PrimaryStage);
		}
	}

	private static void initNotificationStrings(String messageKey)
	{
		messageKey = "notification." + messageKey;
		text = LanguageController.getLocaleString(messageKey, null);
	}
	
	private static void initNotificationType(String notificationType)
	{
		type = notificationType;
	}

	private static Area calculatePositionNearTray()
	{
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Rectangle bounds = gd.getDefaultConfiguration().getBounds();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration());

		Rectangle safeBounds = new Rectangle(bounds);
		safeBounds.x += insets.left;
		safeBounds.y += insets.top;
		safeBounds.width -= (insets.left + insets.right);
		safeBounds.height -= (insets.top + insets.bottom);

		Area area = new Area(bounds);
		area.subtract(new Area(safeBounds));

		return area;
	}

	private static void initNotificationStage(Area area)
	{
		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(urlScene);
			fxmlLoader.setResources(MainApp.ResourceBundle);
			AnchorPane sceneContentPane = (AnchorPane) fxmlLoader.load();

			notificationStage = new Popup();
			notificationStage.getContent().addAll(sceneContentPane);

			notificationStage.setX(area.getBounds().getWidth() - sceneContentPane.getPrefWidth());
			notificationStage.setY(area.getBounds().getY() - sceneContentPane.getPrefHeight());
		}
		catch (Exception e)
		{
			new UnexpectedBehaviourException(e);
		}
	}

	private static void initAnimation()
	{
		KeyFrame fading = new KeyFrame(Duration.millis(2000), new KeyValue(notificationStage.opacityProperty(), 0));

		timeline = new Timeline();
		timeline.getKeyFrames().add(fading);

		timeline.setOnFinished((ae) ->
		{
			closeNotification();
		});

		timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
						timeline.play();
					}
				});

			}
		}, 5000);
	}

	public static void closeNotification()
	{
		timer.cancel();
		timeline.stop();

		notificationStage.hide();
	}

	public static void cancelAnimation()
	{
		timer.cancel();
		timeline.stop();

		notificationStage.opacityProperty().set(1);
	}

	public static void restartAnimation()
	{
		initAnimation();
	}
}
