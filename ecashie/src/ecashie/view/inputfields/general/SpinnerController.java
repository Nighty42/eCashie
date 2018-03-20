package ecashie.view.inputfields.general;

import ecashie.controller.validation.Validation;
import ecashie.view.inputfields.InputField;
import javafx.event.EventHandler;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class SpinnerController
{
	private Spinner<Integer> spinner;

	public Spinner<Integer> getSpinner()
	{
		return spinner;
	}

	public void setSpinner(Spinner<Integer> spinner)
	{
		this.spinner = spinner;
	}
	
	private InputField inputField;

	public InputField getInputField()
	{
		return inputField;
	}

	public void setInputField(InputField inputField)
	{
		this.inputField = inputField;
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

	public SpinnerController(InputField inputField, Spinner<Integer> spinner)
	{
		this.inputField = inputField;
		this.spinner = spinner;

		setValueFactoryOfSpinner();

		addTextChangedEventToSpinner();

		setIncrementHandlerOfSpinner();
	}

	public void setValueFactoryOfSpinner()
	{
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
				inputField.getMinValue().get(), inputField.getMaxValue().get(), inputField.getMinValue().get());

		spinner.setValueFactory(valueFactory);
	}

	public void addTextChangedEventToSpinner()
	{
		spinner.getEditor().addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent event)
			{
				if (Validation.fireOnTextChangeInputField)
				{
					validate();
				}
			}
		});
	}

	public void setIncrementHandlerOfSpinner()
	{
		SpinnerIncrementHandler handler = new SpinnerIncrementHandler();

		spinner.addEventFilter(MouseEvent.MOUSE_PRESSED, handler);
		spinner.addEventFilter(MouseEvent.MOUSE_RELEASED, evt ->
		{
			if (evt.getButton() == MouseButton.PRIMARY)
			{
				handler.stop();
			}
		});
	}

	public void onActionMinimum()
	{
		spinner.getValueFactory().setValue(inputField.getMinValue().get());

		inputField.selectAll();
	}

	public void onActionMaximum()
	{
		spinner.getValueFactory().setValue(inputField.getMaxValue().get());

		inputField.selectAll();
	}

	public void validate()
	{
		inputField.validateWithoutVisualization();
	}
}
