package ecashie.model.appdetails;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AppVersion
{
	public static ObservableList<AppVersion> AppVersionList = FXCollections.observableArrayList();

	private String versionNumber = "";

	public String getVersionNumber()
	{
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber)
	{
		this.versionNumber = versionNumber;
	}
	
	private String versionReleaseDate = "";

	public String getVersionReleaseDate()
	{
		return versionReleaseDate;
	}

	public void setVersionReleaseDate(String versionReleaseDate)
	{
		this.versionReleaseDate = versionReleaseDate;
	}

	private ObservableList<String> newFunctions = FXCollections.observableArrayList();

	public ObservableList<String> getNewFunctions()
	{
		return newFunctions;
	}

	public void setNewFunctions(ObservableList<String> newFunctions)
	{
		this.newFunctions = newFunctions;
	}

	private ObservableList<String> importantChanges = FXCollections.observableArrayList();

	public ObservableList<String> getImportantChanges()
	{
		return importantChanges;
	}

	public void setImportantChanges(ObservableList<String> importantChanges)
	{
		this.importantChanges = importantChanges;
	}

	private ObservableList<String> bugFixes = FXCollections.observableArrayList();

	public ObservableList<String> getBugFixes()
	{
		return bugFixes;
	}

	public void setBugFixes(ObservableList<String> bugFixes)
	{
		this.bugFixes = bugFixes;
	}

	public AppVersion(String versionNumber, String versionReleaseDate, ObservableList<String> newFunctions, ObservableList<String> importantChanges,
			ObservableList<String> bugFixes)
	{
		this.versionNumber = versionNumber;
		this.versionReleaseDate = versionReleaseDate;
		this.newFunctions = newFunctions;
		this.importantChanges = importantChanges;
		this.bugFixes = bugFixes;

		AppVersionList.add(this);
	}
}
