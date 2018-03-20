package ecashie.model.settings;

import java.io.File;
import java.io.IOException;
import java.util.Currency;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ecashie.controller.i18n.CurrencyUtils;
import ecashie.controller.i18n.LanguageUtils;
import ecashie.controller.i18n.SupportedCurrency;
import ecashie.controller.i18n.SupportedLanguage;
import ecashie.controller.utilities.FileOperations;

public class AppSettings
{
	public static File AppSettingsXML = new File(System.getProperty("user.dir") + "\\AppSettings.xml");

	public static String recentUsedDatabase = "";
	public static SupportedLanguage language = SupportedLanguage.en_GB;
	public static SupportedCurrency baseCurrency = new SupportedCurrency(Currency.getInstance(AppSettings.language.getLocale()));

	public static void read() throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException
	{
		if (FileOperations.fileExists(AppSettingsXML))
		{
			Document document = prepareReading();

			readOutRecentUsedDatabase(document);

			readOutLanguage(document);

			readOutCurrency(document);
		}

		validateLanguage();
	}

	private static Document prepareReading() throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		Document document = documentBuilder.parse(AppSettingsXML);

		return document;
	}

	private static void readOutRecentUsedDatabase(Document document)
	{
		NodeList recentUsedDatabaseNodeList = document.getElementsByTagName("RecentUsedDatabase");
		Element contributorElement = (Element) recentUsedDatabaseNodeList.item(0);
		recentUsedDatabase = contributorElement.getAttribute("value");
	}

	private static void readOutLanguage(Document document)
	{
		NodeList languageNodeList = document.getElementsByTagName("Language");
		Element languageElement = (Element) languageNodeList.item(0);
		language = new SupportedLanguage(LanguageUtils.stringToLocale(languageElement.getAttribute("code")));
	}

	private static void readOutCurrency(Document document)
	{
		NodeList currencyNodeList = document.getElementsByTagName("Currency");
		Element currencyElement = (Element) currencyNodeList.item(0);

		Currency currency = CurrencyUtils.stringToCurrency(currencyElement.getAttribute("code"));
		String currencySymbol = currencyElement.getAttribute("symbol");
		int currencySymbolPosition = Integer.parseInt(currencyElement.getAttribute("symbolPosition"));
		int thousandsSeparator = Integer.parseInt(currencyElement.getAttribute("thousandsSeparator"));
		int decimalMark = Integer.parseInt(currencyElement.getAttribute("decimalMark"));
		int numberOfDecimalPlaces = Integer.parseInt(currencyElement.getAttribute("numberOfDecimalPlaces"));

		baseCurrency = new SupportedCurrency(currency, currencySymbol, currencySymbolPosition, thousandsSeparator,
				decimalMark, numberOfDecimalPlaces);
	}

	private static void validateLanguage()
	{
		if (!LanguageUtils.validateLanguage(language.getLocale()))
		{
			language = SupportedLanguage.en_GB;
		}
	}

	public static void write() throws ParserConfigurationException, TransformerException, IOException
	{
		FileOperations.createFile(AppSettingsXML);

		Document document = prepareWriting();

		Element rootElement = createRootElement(document);

		createRecentUsedDatabaseElement(document, rootElement);

		createLanguageElement(document, rootElement);

		createCurrencyElement(document, rootElement);

		writeXML(document);
	}

	private static Document prepareWriting() throws ParserConfigurationException
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

		recentUsedDatabaseElement.setAttribute("value", recentUsedDatabase);
	}

	private static void createLanguageElement(Document document, Element rootElement)
	{
		Element languageElement = document.createElement("Language");
		rootElement.appendChild(languageElement);

		languageElement.setAttribute("code", language.getLocale().toString());
	}

	private static void createCurrencyElement(Document document, Element rootElement)
	{
		Element currencyElement = document.createElement("Currency");
		rootElement.appendChild(currencyElement);

		currencyElement.setAttribute("code", baseCurrency.getCurrency().getCurrencyCode());
		currencyElement.setAttribute("symbol", baseCurrency.getCurrencySymbol());
		currencyElement.setAttribute("symbolPosition", baseCurrency.getCurrencySymbolPositionAsString());
		currencyElement.setAttribute("thousandsSeparator", baseCurrency.getThousandsSeparatorAsString());
		currencyElement.setAttribute("decimalMark", baseCurrency.getDecimalMarkAsString());
		currencyElement.setAttribute("numberOfDecimalPlaces", baseCurrency.getNumberOfDecimalPlacesAsString());
	}

	private static void writeXML(Document document) throws TransformerException
	{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);

		StreamResult result = new StreamResult(AppSettingsXML);
		transformer.transform(source, result);
	}
}
