package ecashie.controller.settings;

import java.io.File;
import java.util.Currency;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ecashie.controller.i18n.CurrencyController;
import ecashie.controller.i18n.LanguageController;
import ecashie.controller.utilities.FileOperations;
import ecashie.model.i18n.SupportedCurrency;
import ecashie.model.i18n.SupportedLanguage;

public class AppSettings
{
	public static File AppSettingsXML = new File("src/AppSettings.xml");
	public static String RecentUsedDatabase = "";
	public static SupportedLanguage Language = null;
	public static SupportedCurrency BaseCurrency = null;

	public static void init() throws Exception
	{
		Language = SupportedLanguage.en_GB;
		BaseCurrency = new SupportedCurrency(Currency.getInstance(AppSettings.Language.getLocale()));
		
		LanguageController.changeLanguage(Language);
	}

	public static void read() throws Exception
	{
		if (FileOperations.fileExists(AppSettingsXML))
		{
			Document document = prepareReading();

			readRecentUsedDatabase(document);

			readLanguage(document);

			readCurrency(document);
		}

		validateLanguage();
	}

	private static Document prepareReading() throws Exception
	{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		Document document = documentBuilder.parse(AppSettingsXML);

		return document;
	}

	private static void readRecentUsedDatabase(Document document)
	{
		NodeList recentUsedDatabaseNodeList = document.getElementsByTagName("RecentUsedDatabase");
		Element contributorElement = (Element) recentUsedDatabaseNodeList.item(0);
		RecentUsedDatabase = contributorElement.getAttribute("value");
	}

	private static void readLanguage(Document document)
	{
		NodeList languageNodeList = document.getElementsByTagName("Language");
		Element languageElement = (Element) languageNodeList.item(0);
		Language = new SupportedLanguage(LanguageController.stringToLocale(languageElement.getAttribute("code")));
	}

	private static void readCurrency(Document document)
	{
		NodeList currencyNodeList = document.getElementsByTagName("Currency");
		Element currencyElement = (Element) currencyNodeList.item(0);

		Currency currency = CurrencyController.stringToCurrency(currencyElement.getAttribute("code"));
		String currencySymbol = currencyElement.getAttribute("symbol");
		int currencySymbolPosition = Integer.parseInt(currencyElement.getAttribute("symbolPosition"));
		int thousandsSeparator = Integer.parseInt(currencyElement.getAttribute("thousandsSeparator"));
		int decimalMark = Integer.parseInt(currencyElement.getAttribute("decimalMark"));
		int numberOfDecimalPlaces = Integer.parseInt(currencyElement.getAttribute("numberOfDecimalPlaces"));

		BaseCurrency = new SupportedCurrency(currency, currencySymbol, currencySymbolPosition, thousandsSeparator,
				decimalMark, numberOfDecimalPlaces);
	}

	private static void validateLanguage()
	{
		if (!LanguageController.validateLanguage(Language.getLocale()))
		{
			Language = SupportedLanguage.en_GB;
		}
	}

	public static void write() throws Exception
	{
		FileOperations.createFile(AppSettingsXML);

		Document document = prepareWriting();

		Element rootElement = createRootElement(document);

		createRecentUsedDatabaseElement(document, rootElement);

		createLanguageElement(document, rootElement);

		createCurrencyElement(document, rootElement);

		writeXML(document);
	}

	private static Document prepareWriting() throws Exception
	{
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

		return documentBuilder.newDocument();
	}

	private static Element createRootElement(Document document)
	{
		Element rootElement = document.createElement("AppSettings");
		document.appendChild(rootElement);

		return rootElement;
	}

	private static void createRecentUsedDatabaseElement(Document document, Element rootElement)
	{
		Element recentUsedDatabaseElement = document.createElement("RecentUsedDatabase");
		rootElement.appendChild(recentUsedDatabaseElement);

		recentUsedDatabaseElement.setAttribute("value", RecentUsedDatabase);
	}

	private static void createLanguageElement(Document document, Element rootElement)
	{
		Element languageElement = document.createElement("Language");
		rootElement.appendChild(languageElement);

		languageElement.setAttribute("code", Language.getLocale().toString());
	}

	private static void createCurrencyElement(Document document, Element rootElement)
	{
		Element currencyElement = document.createElement("Currency");
		rootElement.appendChild(currencyElement);

		currencyElement.setAttribute("code", BaseCurrency.getCurrency().getCurrencyCode());
		currencyElement.setAttribute("symbol", BaseCurrency.getCurrencySymbol());
		currencyElement.setAttribute("symbolPosition", BaseCurrency.getCurrencySymbolPositionAsString());
		currencyElement.setAttribute("thousandsSeparator", BaseCurrency.getThousandsSeparatorAsString());
		currencyElement.setAttribute("decimalMark", BaseCurrency.getDecimalMarkAsString());
		currencyElement.setAttribute("numberOfDecimalPlaces", BaseCurrency.getNumberOfDecimalPlacesAsString());
	}

	private static void writeXML(Document document) throws Exception
	{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);

		StreamResult result = new StreamResult(AppSettingsXML);
		transformer.transform(source, result);
	}
}
