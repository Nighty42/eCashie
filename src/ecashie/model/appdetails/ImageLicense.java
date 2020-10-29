package ecashie.model.appdetails;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ImageLicense
{
	public static ObservableList<ImageLicense> ImageLicenseList = FXCollections.observableArrayList();

	private String identifier = "";

	public String getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	private String text = "";

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	private String source = "";

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public ImageLicense(String identifier, String text, String source)
	{
		this.identifier = identifier;
		this.text = text;
		this.source = source;
		
		ImageLicenseList.add(this);
	}

	public static ImageLicense getLicenseByID(String licenseID)
	{
		if (licenseID.isEmpty())
		{
			return null;
		}
		else
		{
			return ImageLicenseList.filtered(x -> x.getIdentifier().equals(licenseID)).get(0);
		}
	}
}
