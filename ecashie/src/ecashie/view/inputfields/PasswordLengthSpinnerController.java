package ecashie.view.inputfields;

import java.io.IOException;
import java.security.SecureRandom;

import ecashie.controller.utilities.GeneralOperations;
import ecashie.controller.utilities.Notification;
import ecashie.controller.validation.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class PasswordLengthSpinnerController
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

	private Spinner<Integer> passwordLengthSpinner;

	public Spinner<Integer> getPasswordLengthSpinner()
	{
		return passwordLengthSpinner;
	}

	public void setPasswordLengthSpinner(Spinner<Integer> passwordLengthSpinner)
	{
		this.passwordLengthSpinner = passwordLengthSpinner;
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

	public PasswordLengthSpinnerController(InputField inputField, Spinner<Integer> lengthSpinner)
	{
		this.inputField = inputField;
		this.passwordLengthSpinner = lengthSpinner;

		setValueFactoryOfPasswordLengthSpinner();

		addTextChangedEventToPasswordLengthSpinner();

		setIncrementHandlerOfPasswordLengthSpinner();
	}

	public void setValueFactoryOfPasswordLengthSpinner()
	{
		SpinnerValueFactory<Integer> valueFactory = //
				new SpinnerValueFactory.IntegerSpinnerValueFactory(inputField.getMinValue().get(),
						inputField.getMaxValue().get(), inputField.getMinValue().get());

		passwordLengthSpinner.setValueFactory(valueFactory);
	}

	public void addTextChangedEventToPasswordLengthSpinner()
	{
		passwordLengthSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) ->
		{
			if (Validation.fireOnTextChangeInputField)
			{
				validate();
			}
		});
	}

	public void setIncrementHandlerOfPasswordLengthSpinner()
	{
		SpinnerIncrementHandler handler = new SpinnerIncrementHandler();

		passwordLengthSpinner.addEventFilter(MouseEvent.MOUSE_PRESSED, handler);
		passwordLengthSpinner.addEventFilter(MouseEvent.MOUSE_RELEASED, evt ->
		{
			if (evt.getButton() == MouseButton.PRIMARY)
			{
				handler.stop();
			}
		});
	}

	public void onActionMinimumPasswordLength()
	{
		passwordLengthSpinner.getValueFactory().setValue(inputField.getMinValue().get());

		inputField.selectAll();
	}

	public void onActionMaximumPasswordLength()
	{
		passwordLengthSpinner.getValueFactory().setValue(inputField.getMaxValue().get());

		inputField.selectAll();
	}

	public String onActionGeneratePassword()
	{
		// NOTE: This implementation of the generation of passwords just supports the latin
		// alphabet which is commonly used in Britain and Germany. Others are currently not
		// supported. Could be interesting to implement the generation of chinese characters
		// or the support of other charactersets of countries all over the world.

		ObservableList<Integer> initialPoolOrder = buildInitialPoolOrder();

		ObservableList<Integer> randomPoolOrder = randomizePoolOrder(initialPoolOrder);

		return generateRandomPassword(randomPoolOrder);
	}

	public ObservableList<Integer> buildInitialPoolOrder()
	{
		// 0 - Großbuchstaben (2) | 1 - Kleinbuchstaben (2) | 2 - Ziffern (2) | 3 - Sonderzeichen (2)
		ObservableList<Integer> initialPoolOrder = FXCollections.observableArrayList(0, 0, 1, 1, 2, 2, 3, 3);
		SecureRandom secureRandomCaseBlocks = new SecureRandom();

		int passwordLength = Integer.parseInt(passwordLengthSpinner.getEditor().getText());
		int poolOrderLength = initialPoolOrder.size();

		while (poolOrderLength < passwordLength)
		{
			int caseBlock = secureRandomCaseBlocks.nextInt(4);
			initialPoolOrder.add(caseBlock);
			++poolOrderLength;
		}

		return initialPoolOrder;
	}

	public ObservableList<Integer> randomizePoolOrder(ObservableList<Integer> initialPoolOrder)
	{
		ObservableList<Integer> randomPoolOrder = FXCollections.observableArrayList(initialPoolOrder);
		SecureRandom secureRandomPoolOrder = new SecureRandom();

		int index = -1;
		int temp = -1;

		for (int i = randomPoolOrder.size() - 1; i > 0; --i)
		{
			index = secureRandomPoolOrder.nextInt(i + 1);
			temp = randomPoolOrder.get(index);
			randomPoolOrder.set(index, randomPoolOrder.get(i));
			randomPoolOrder.set(i, temp);
		}

		return randomPoolOrder;
	}

	public String generateRandomPassword(ObservableList<Integer> randomPoolOrder)
	{
		char[] randomPassword = new char[randomPoolOrder.size()];
		SecureRandom secureRandomCases = new SecureRandom();

		char[] upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		char[] lower = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		char[] numeric = "0123456789".toCharArray();
		char[] special = "^°!\"§$%&/{([)]=}?\\´`+*~#'<>|,;.:-_".toCharArray();

		int index = -1;

		for (int i = 0; i < randomPoolOrder.size(); ++i)
		{
			int poolBlock = randomPoolOrder.get(i);

			switch (poolBlock)
			{
				case 0:
					index = secureRandomCases.nextInt(upper.length);
					randomPassword[i] = upper[index];
					break;
				case 1:
					index = secureRandomCases.nextInt(lower.length);
					randomPassword[i] = lower[index];
					break;
				case 2:
					index = secureRandomCases.nextInt(numeric.length);
					randomPassword[i] = numeric[index];
					break;
				case 3:
					index = secureRandomCases.nextInt(special.length);
					randomPassword[i] = special[index];
					break;
			}
		}

		return new String(randomPassword);
	}

	public void onActionCopyPassword(String password) throws IOException
	{
		GeneralOperations.copyToClipboard(password);

		Notification.info("copyToClipboard.password");
	}

	public void validate()
	{
		inputField.validateWithoutVisualization();
	}
}
