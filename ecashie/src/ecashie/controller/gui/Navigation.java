package ecashie.controller.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Navigation
{
	public static String Current = "";
	public static String Next = "";

	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	private static ObservableList<String> backstack = FXCollections.observableArrayList();

	public static String getBefore()
	{
		return backstack.get(backstack.size() - 2);
	}

	public static void addBefore(String beforeScene)
	{
		backstack.add(beforeScene);
	}

	public void addPropertyChangeListener(PropertyChangeListener l)
	{
		changes.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l)
	{
		changes.removePropertyChangeListener(l);
	}

	public static void goForward()
	{
		if (!Current.equals(Next))
		{
			Current = Next;
		}

		backstack.add(Current);

		GuiBuilder.changeScene();
	}
}
