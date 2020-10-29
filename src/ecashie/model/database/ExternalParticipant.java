package ecashie.model.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ExternalParticipant
{
	private static ObservableList<ExternalParticipant> externalParticipantList = FXCollections.observableArrayList();

	public static ObservableList<ExternalParticipant> getList()
	{
		return externalParticipantList;
	}

	private int identifier;

	public int getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(int identifier)
	{
		this.identifier = identifier;
	}

	private String name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	private String description;

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public ExternalParticipant(int identifier, String name, String description)
	{
		this.identifier = identifier;
		this.name = name;
		this.description = description;
	}
}
