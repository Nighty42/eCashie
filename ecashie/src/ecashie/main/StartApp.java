package ecashie.main;

import java.io.IOException;
import java.security.Security;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.xml.sax.SAXException;

import ecashie.controller.appdetails.AppDetails;
import ecashie.controller.exception.LoggingNotAvailableException;
import ecashie.controller.i18n.LanguageController;
import ecashie.controller.logging.ApplicationLogger;
import ecashie.controller.settings.AppSettings;

public class StartApp {
	public static void start() throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException,
			XMLStreamException, InterruptedException {
		AppPreloader.notifyPreloader(25, "Initialize Logging and Settings");

		initAppSettings();

		initLogging();

		AppPreloader.notifyPreloader(65, "Read Application Settings");

		readAppDetails();

		readAppSettings();

		AppPreloader.notifyPreloader(90, "Initialize Security Provider and Language");

		initSecurityProvider();

		initLanguage();
	}

	private static void initAppSettings() {
		try {
			AppSettings.init();
		} catch (IOException e) {
			new LoggingNotAvailableException();
		}
	}

	private static void initLogging() {
		try {
			ApplicationLogger.setup();
		} catch (IOException e) {
			new LoggingNotAvailableException();
		}
	}

	private static void readAppDetails() throws IllegalArgumentException, ParserConfigurationException, SAXException,
			IOException, XMLStreamException {
		AppDetails.read();
	}

	private static void readAppSettings()
			throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
		AppSettings.read();
	}

	private static void initSecurityProvider() throws NullPointerException, SecurityException {
		Security.addProvider(new BouncyCastleProvider());
	}

	private static void initLanguage() throws IOException {
		LanguageController.changeLanguage(AppSettings.Language);
	}
}
