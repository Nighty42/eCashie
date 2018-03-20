package org.ecashie.controller.utilities;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Area;
import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.ecashie.MainApp;
import org.ecashie.MainAppController;
import org.ecashie.controller.errorhandling.UnexpectedBehaviourException;
import org.ecashie.controller.gui.GuiBuilder;
import org.ecashie.controller.internationalization.ResourceBundleString;

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
	
	private static final URL urlScene = MainApp.class.getResource("view/notification/NotificationScene.fxml");
	
	private static Popup notificationStage;
	private static Timeline timeline;
	private static Timer timer;

	public static void info(String messageKey)
	{
		initializeNotification(messageKey, "info");
	}

	public static void warning(String messageKey)
	{
		initializeNotification(messageKey, "warning");
	}

	public static void error(String messageKey)
	{
		initializeNotification(messageKey, "error");
	}

	private static void initializeNotification(String messageKey, String notificationType)
	{
		if (notificationStage == null || !notificationStage.isShowing())
		{
			initializeNotificationStrings(messageKey);

			initializeNotificationType(notificationType);

			Area area = calculatePositionNearTray();

			initializeNotificationStage(area);

			initializeAnimation();

			notificationStage.show(GuiBuilder.primaryStage);
		}
	}

	private static void initializeNotificationStrings(String messageKey)
	{
		messageKey = "notification." + messageKey;
		text = ResourceBundleString.getLocaleString(messageKey, null);
	}
	
	private static void initializeNotificationType(String notificationType)
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

	private static void initializeNotificationStage(Area area)
	{
		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(urlScene);
			fxmlLoader.setResources(MainAppController.ResourceBundle);
			AnchorPane sceneContentPane = (AnchorPane) fxmlLoader.load();

			notificationStage = new Popup();
			notificationStage.getContent().addAll(sceneContentPane);

			notificationStage.setX(area.getBounds().getWidth() - sceneContentPane.getPrefWidth());
			notificationStage.setY(area.getBounds().getY() - sceneContentPane.getPrefHeight());
		}
		catch (IOException e)
		{
			new UnexpectedBehaviourException();
		}
	}

	private static void initializeAnimation()
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
		initializeAnimation();
	}
}
