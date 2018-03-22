package ecashie.view.inputfields;

import java.io.File;

import ecashie.controller.validation.Validation;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

public class FileFolderPathFieldController
{
	private InputField inputField;

	public InputField getInputField()
	{
		return inputField;
	}

	public void setInputField(InputField inputField)
	{
		this.inputField = inputField;
	}
	
	private GridPane gridPane;

	public GridPane getGridPane()
	{
		return gridPane;
	}

	public void setGridPane(GridPane gridPane)
	{
		this.gridPane = gridPane;
	}
	
	private TextField textField;

	public TextField getTextField()
	{
		return textField;
	}

	public void setTextField(TextField textField)
	{
		this.textField = textField;
	}

	private Label statusLabel;

	public Label getStatusLabel()
	{
		return statusLabel;
	}

	public void setStatusLabel(Label statusLabel)
	{
		this.statusLabel = statusLabel;
	}

	private String tempPath = "";

	public String getTempPath()
	{
		return tempPath;
	}

	public void setTempPath(String tempPath)
	{
		this.tempPath = tempPath;
	}

	private boolean enableLiveValidation = false;

	public boolean isEnableLiveValidation()
	{
		return enableLiveValidation;
	}

	public void setEnableLiveValidation(boolean enableLiveValidation)
	{
		this.enableLiveValidation = enableLiveValidation;
	}

	public FileFolderPathFieldController(InputField inputField, GridPane gridPane, TextField textField,
			Label statusLabel)
	{
		this.textField = textField;
		this.gridPane = gridPane;
		this.statusLabel = statusLabel;
		this.inputField = inputField;

		initializeControlEvents();
	}

	private void initializeControlEvents()
	{
		textField.textProperty().addListener((observable, oldValue, newValue) ->
		{
			if (Validation.fireOnTextChangeInputField)
			{
				onTextChangeTextField(observable, oldValue, newValue);
			}
		});
	}

	private void onTextChangeTextField(ObservableValue<? extends String> observable, String oldValue, String newValue)
	{
		validate();
	}

	// ================================================================================
	// Drag and Drop-Events
	// ================================================================================

	public void onDragExitedTextField(DragEvent dragEvent)
	{
		textField.setText(tempPath);
	}

	public void onDragEnteredTextField(DragEvent dragEvent)
	{
		textField.setText("");
	}

	public void onDragOverTextField(DragEvent dragEvent)
	{
		Dragboard dragBoard = dragEvent.getDragboard();

		if (dragBoard.hasFiles())
		{
			dragEvent.acceptTransferModes(TransferMode.COPY);
		}
		else
		{
			dragEvent.consume();
		}
	}

	public void onDragDroppedTextField(DragEvent dragEvent)
	{
		Dragboard dragBoard = dragEvent.getDragboard();
		boolean success = false;

		if (dragBoard.hasFiles())
		{
			File folder = dragBoard.getFiles().get(0);
			String folderPath = folder.getAbsolutePath();

			success = true;
			tempPath = folderPath;
			textField.setText(folderPath);
		}

		dragEvent.setDropCompleted(success);
		dragEvent.consume();
	}

	// ================================================================================
	// Validation
	// ================================================================================

	public void validate()
	{
		inputField.validateWithVisualization(enableLiveValidation, gridPane, statusLabel);
	}
}
