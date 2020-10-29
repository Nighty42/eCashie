package ecashie.model.appdetails;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ImageDesigner
{
	public static ObservableList<ImageDesigner> ImageDesignerList = FXCollections.observableArrayList();

	private String identifier = "";

	public String getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
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

	public ImageDesigner(String identifier, String designer, String homepage)
	{
		this.identifier = identifier;
		this.designer = designer;
		this.homepage = homepage;

		ImageDesignerList.add(this);
	}

	public static ImageDesigner getDesignerByID(String designerID)
	{
		if (designerID.isEmpty())
		{
			return null;
		}
		else
		{
			return ImageDesignerList.filtered(x -> x.getIdentifier().equals(designerID)).get(0);
		}
	}
}
