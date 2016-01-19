package nl.clockwork.ebms.admin.plugin.ebf.afleverservice.web;

import java.util.ArrayList;
import java.util.List;

import nl.clockwork.ebms.admin.model.EbMSAttachment;
import nl.clockwork.ebms.admin.web.MessageProvider;
import nl.clockwork.ebms.admin.web.service.message.DataSourcesPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.panel.Panel;

public class MessageProviderImpl extends MessageProvider
{
	protected transient Log logger = LogFactory.getLog(this.getClass());

	@Override
	public List<MessageViewPanel> getMessageViewPanels()
	{
		List<MessageViewPanel> result = new ArrayList<MessageViewPanel>();
		result.add(new MessageViewPanel(Constants.AfleverService,Constants.AfleverAction)
		{
			@Override
			public Panel getPanel(String id, List<EbMSAttachment> attachments) throws Exception
			{
				return new AfleverBerichtViewPanel(id,attachments);
			}
		});
		return result;
	}

	@Override
	public List<MessageEditPanel> getMessageEditPanels()
	{
		List<MessageEditPanel> result = new ArrayList<MessageEditPanel>();
		result.add(new MessageEditPanel(Constants.AfleverService,Constants.AfleverAction)
		{
			@Override
			public DataSourcesPanel getPanel(String id)
			{
				return new AfleverBerichtEditPanel(id);
			}
		});
		return result;
	}

}
