package ecashie.model.appdetails;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ImageSource
{
	public static ObservableList<ImageSource> ImageSourceList = FXCollections.observableArrayList();

	private String identifier = "";

	public String getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	private String designerID = "";

	public String getDesignerID()
	{
		return designerID;
	}

	public void setDesignerID(String designerID)
	{
		this.designerID = designerID;
		
		this.designer = ImageDesigner.getDesignerByID(designerID).getDesigner();
		this.homepage = ImageDesigner.getDesignerByID(designerID).getHomepage();
	}
	
	private String designer = "";
	
	public String getDesigner()
	{
		return designer;
	}

	public void setDesigner(String designer)
	{
		this.designer = designer;
	}
	
	private String homepage = "";
	
	public String getHomepage()
	{
		return homepage;
	}

	public void setHomepage(String homepage)
	{
		this.homepage = homepage;
	}
	
	private String licenseID = "";

	public String getLicenseID()
	{
		return licenseID;
	}

	public void setLicenseID(String licenseID)
	{
		this.licenseID = licenseID;
	}
	
	private String license = "";
	
	public String getLicense()
	{
		return license;
	}

	public void setLicense(String license)
	{
		this.license = license;
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

	private String comment = "";

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}
	
	public ImageSource(String identifier, String designerID, String licenseID, String source, String comment)
	{
		this.identifier = identifier;
		this.designerID = designerID;
		this.licenseID = licenseID;
		this.source = source;
		this.comment = comment;
		
		this.designer = ImageDesigner.getDesignerByID(designerID).getDesigner();
		this.homepage = ImageDesigner.getDesignerByID(designerID).getHomepage();
		this.license = ImageLicense.getLicenseByID(licenseID).getText();
		
		ImageSourceList.add(this);
	}
}
