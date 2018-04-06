package ecashie.controller.settings;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ecashie.controller.crypto.CryptoEngine;
import ecashie.controller.utilities.FileOperations;

public class UserSettings
{
	public static File UserSettingsXML = new File(UserData.getDatabaseFolder() + "\\UserSettings.xml");

	public static void read() throws Exception
	{
		if (FileOperations.fileExists(UserSettingsXML))
		{
			Document document = prepareReading();

			readOutCryptKey(document);
		}
	}

	private static Document prepareReading() throws Exception
	{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		Document document = documentBuilder.parse(UserSettingsXML);

		return document;
	}

	private static void readOutCryptKey(Document document)
	{
		NodeList cryptKeyNodeList = document.getElementsByTagName("CryptKey");
		Element cryptKeyElement = (Element) cryptKeyNodeList.item(0);
		CryptoEngine.CryptKey = cryptKeyElement.getAttribute("value");
	}

	public static void write() throws Exception
	{
		if (UserData.getDatabaseFolder().exists())
		{
			FileOperations.createFile(UserSettingsXML);

			Document document = prepareWriting();

			Element rootElement = createRootElement(document);

			createCryptKeyElement(document, rootElement);

			writeXML(document);
		}
	}

	private static Document prepareWriting() throws Exception
	{
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

		return documentBuilder.newDocument();
	}

	private static Element createRootElement(Document document)
	{
		Element rootElement = document.createElement("UserSettings");
		document.appendChild(rootElement);

		return rootElement;
	}

	private static void createCryptKeyElement(Document document, Element rootElement)
	{
		Element languageElement = document.createElement("CryptKey");
		rootElement.appendChild(languageElement);

		languageElement.setAttribute("value", CryptoEngine.CryptKey);
	}

	private static void writeXML(Document document) throws Exception
	{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);

		StreamResult result = new StreamResult(UserSettingsXML);
		transformer.transform(source, result);
	}
}
