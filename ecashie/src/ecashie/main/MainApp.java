package ecashie.main;

import java.net.BindException;
import java.util.ResourceBundle;

import com.sun.javafx.application.LauncherImpl;

import ecashie.controller.exception.UnexpectedBehaviourException;
import ecashie.controller.gui.GuiBuilder;
import ecashie.controller.gui.Navigation;
import ecashie.controller.utilities.SocketListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class MainApp extends Application
{
	public static MainApp application;
	public static ResourceBundle ResourceBundle;

	public static void main(String[] args)
	{
		if (appIsAlreadyRunning())
		{
			Platform.exit();
			System.exit(0);
		}
		else
		{
			LauncherImpl.launchApplication(MainApp.class, AppLoader.class, args);
		}
	}

	@Override
	public void init()
	{
		try
		{
			StartApp.start();
		}
		catch (Exception e)
		{
			new UnexpectedBehaviourException(e);
		}
	}

	@Override
	public void start(Stage primaryStage)
	{
		if (!AppLoader.IsFailed)
		{
			GuiBuilder.initPrimaryStage(primaryStage);

			// DEBUG: Show MainScene
			// Navigation.addBefore("StartScene");
			//
			// Navigation.Next = "MainScene";
			// Navigation.goForward(false);

			Navigation.Next = "StartScene";
			Navigation.goForward(false);
		}
	}

	private static boolean appIsAlreadyRunning()
	{
		try
		{
			new SocketListener();

			Thread threadConnectionListener = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					SocketListener.serverSocketListener();
				}
			});

			threadConnectionListener.start();
		}
		catch (BindException e)
		{
			SocketListener.clientSocketListener();

			return true;
		}
		catch (Exception e)
		{
			return true;
		}

		return false;
	}
}
