package ecashie.controller.gui;

import java.net.URL;

import ecashie.controller.exception.UnexpectedBehaviourException;
import ecashie.controller.settings.UserData;
import ecashie.main.ExitApp;
import ecashie.main.MainApp;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiBuilder
{
	public static Stage PrimaryStage;

	public static void initPrimaryStage(Stage primaryStage)
	{
		PrimaryStage = primaryStage;

		PrimaryStage.setTitle("eCashie");

		PrimaryStage.getIcons()
				.add(new Image(MainApp.class.getResourceAsStream("/ecashie/resources/img/logo_32x32.png")));
		PrimaryStage.getIcons()
				.add(new Image(MainApp.class.getResourceAsStream("/ecashie/resources/img/logo_48x48.png")));

		PrimaryStage.setOnCloseRequest((WindowEvent we) -> {
			ExitApp.exit();
		});
	}

	public static void changeScene(boolean wait)
	{
		try
		{
			PrimaryStage.setScene(createScene(initFxmlLoader(Navigation.Next)));

			if (Navigation.Next.equals("MainScene"))
			{
				PrimaryStage.setMaximized(true);
			}
		}
		catch (Exception e)
		{
			new UnexpectedBehaviourException(e);
		}

		if (!wait)
		{
			PrimaryStage.show();
		}
	}

	private static String generateScenePath(String sceneName)
	{
		String scenePath = "";

		switch (Navigation.Current)
		{
			case "MainScene":
				if (sceneName.contains("Settings"))
				{
					scenePath = "/ecashie/view/settings/" + sceneName + ".fxml";
				}
				else
				{
					scenePath = "/ecashie/view/main/" + sceneName + ".fxml";
				}
				break;
			case "StartScene":
				scenePath = "/ecashie/view/start/" + sceneName + ".fxml";
				break;
		}

		return scenePath;
	}

	private static FXMLLoader initFxmlLoader(String sceneName)
	{
		String scenePath = generateScenePath(sceneName);

		URL urlScene = MainApp.class.getResource(scenePath);

		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(urlScene);
		fxmlLoader.setResources(MainApp.ResourceBundle);

		return fxmlLoader;
	}

	private static Scene createScene(FXMLLoader fxmlLoader) throws Exception
	{
		return new Scene(fxmlLoader.load());
	}

	private static AnchorPane createSceneContentPane(FXMLLoader fxmlLoader) throws Exception
	{
		return (AnchorPane) fxmlLoader.load();
	}

	public static void embedPaneIntoScene(AnchorPane anchorPane, String sceneName)
	{
		try
		{
			AnchorPane scenePane = createSceneContentPane(initFxmlLoader(sceneName));

			anchorPane.getChildren().clear();
			anchorPane.getChildren().add(scenePane);
		}
		catch (Exception e)
		{
			new UnexpectedBehaviourException(e);
		}
	}

	public static void updateWindowTitle()
	{
		if (PrimaryStage != null)
		{
			Platform.runLater(new Runnable()
			{
				@Override
				public void run()
				{
					PrimaryStage.setTitle("eCashie - " + UserData.getCashJournalName());
				}
			});
		}
	}
}
