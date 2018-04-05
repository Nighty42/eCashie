package ecashie.main;

import java.io.IOException;

import com.sun.javafx.application.LauncherImpl;

import ecashie.controller.exception.LoggingNotAvailableException;
import ecashie.view.start.LoadingSceneController;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppPreloader extends Preloader
{
	private static String Task = "Initialize Preloader";

	public static Stage PreloaderStage;
	private Scene preloaderScene;

	@Override
	public void init() throws Exception
	{
		Platform.runLater(() -> {
			try
			{
				Parent root = FXMLLoader.load(MainApp.class.getResource("/ecashie/view/start/LoadingScene.fxml"));

				preloaderScene = new Scene(root);
			}
			catch (IOException e)
			{
				new LoggingNotAvailableException();
			}
		});
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		PreloaderStage = stage;

		PreloaderStage.getIcons()
				.add(new Image(MainApp.class.getResourceAsStream("/ecashie/resources/img/logo_32x32.png")));
		PreloaderStage.getIcons()
				.add(new Image(MainApp.class.getResourceAsStream("/ecashie/resources/img/logo_48x48.png")));

		PreloaderStage.setTitle("Loading, please wait...");
		PreloaderStage.setScene(preloaderScene);
		PreloaderStage.resizableProperty().setValue(Boolean.FALSE);
		PreloaderStage.show();
	}

	@Override
	public void handleStateChangeNotification(StateChangeNotification info)
	{
		switch (info.getType())
		{
		case BEFORE_START:
			PreloaderStage.hide();
			break;
		default:
			break;
		}
	}

	@Override
	public void handleApplicationNotification(PreloaderNotification info)
	{
		if (info instanceof ProgressNotification)
		{
			LoadingSceneController.getInstance().setProgress(((ProgressNotification) info).getProgress());
			LoadingSceneController.getInstance().setCurrentTask(Task);
		}
	}

	public static void notifyPreloader(double progress, String task)
	{
		Task = task;
		LauncherImpl.notifyPreloader(MainApp.application, new Preloader.ProgressNotification(progress));
	}
}