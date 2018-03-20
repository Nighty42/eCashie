package ecashie.view.root;

import javafx.scene.layout.BorderPane;

public class RootLayout
{
	private BorderPane borderPane;
	private String name;
	private String path;
	private boolean maximized;
	private boolean resizable;

	public static RootLayout fullScreen = new RootLayout("FullScreen",
			"/ecashie/view/root/RootLayoutFullScreen.fxml", true, true);
	public static RootLayout nonFullScreen = new RootLayout("NonFullScreen",
			"/ecashie/view/root/RootLayoutNonFullScreen.fxml", false, false);

	public RootLayout(String _name, String _path, boolean _maximized, boolean _resizable)
	{
		borderPane = new BorderPane();
		name = _name;
		path = _path;
		maximized = _maximized;
		resizable = _resizable;
	}

	/*
	 * GETTER
	 */

	public BorderPane getBorderPane()
	{
		return borderPane;
	}

	public String getName()
	{
		return name;
	}

	public String getPath()
	{
		return path;
	}

	public boolean getMaximized()
	{
		return maximized;
	}

	public boolean getResizable()
	{
		return resizable;
	}

	/*
	 * SETTER
	 */

	public void setBorderPane(BorderPane _borderPane)
	{
		borderPane = _borderPane;
	}
}
