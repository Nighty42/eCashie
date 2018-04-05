package ecashie.model.i18n;

public class ResourceBundleString
{
	private String messageKey = "";

	public String getMessageKey()
	{
		return messageKey;
	}

	public void setMessageKey(String messageKey)
	{
		this.messageKey = messageKey;
	}

	private String messageHeader = "";

	public String getMessageHeader()
	{
		return messageHeader;
	}

	public void setMessageHeader(String messageHeader)
	{
		this.messageHeader = messageHeader;
	}

	private String messageContent = "";

	public String getMessageContent()
	{
		return messageContent;
	}

	public void setMessageContent(String messageContent)
	{
		this.messageContent = messageContent;
	}

	public ResourceBundleString(String messageKey, String messageHeader, String messageContent)
	{
		this.messageKey = messageKey;
		this.messageHeader = messageHeader;
		this.messageContent = messageContent;
	}
}
