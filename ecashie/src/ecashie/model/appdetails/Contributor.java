package ecashie.model.appdetails;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Contributor
{
	public static ObservableList<Contributor> ContributorList = FXCollections.observableArrayList();

	private String name = "";

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	private String roles = "";

	public String getRoles()
	{
		return roles;
	}

	public void setRoles(String roles)
	{
		this.roles = roles;
	}

	public Contributor(String name, String roles)
	{
		this.name = name;
		this.roles = roles;
		
		ContributorList.add(this);
	}
}
