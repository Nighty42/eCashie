package ecashie.main;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXException;

import ecashie.controller.exception.UnexpectedBehaviourException;
import ecashie.controller.gui.Navigation;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application
{
	public static ResourceBundle ResourceBundle;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			StartApp.start(primaryStage);

			// DEBUG: MainScene
			// Navigation.addBefore("StartScene");
			//
			// Navigation.Next = "MainScene";
			// Navigation.goForward();

			// DEBUG: StartScene
			Navigation.Next = "StartScene";
			Navigation.goForward();
		}
		catch (NullPointerException | SecurityException | IllegalArgumentException | ParserConfigurationException
				| SAXException | XMLStreamException | IOException e)
		{
			new UnexpectedBehaviourException();
		}
	}
}
