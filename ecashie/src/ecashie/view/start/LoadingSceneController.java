package ecashie.view.start;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class LoadingSceneController
{
	@FXML
	private ProgressBar progressBar;
	@FXML
	private Label progressLabel;
	@FXML
	private Label currentTaskLabel;

	private static LoadingSceneController instance = null;

	public static LoadingSceneController getInstance()
	{
		return instance;
	}

	@FXML
	private void initialize()
	{
		instance = this;
	}

	public ProgressBar getProgressBar()
	{
		return progressBar;
	}

	public void setProgress(double progress)
	{
		progressBar.setProgress(progress / 100);
		progressLabel.setText(progress + " %");
	}

	public void setCurrentTask(String task)
	{
		currentTaskLabel.setText(task);
	}
}