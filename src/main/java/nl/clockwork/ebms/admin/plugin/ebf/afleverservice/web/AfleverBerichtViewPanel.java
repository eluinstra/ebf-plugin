/**
 * Copyright 2013 Clockwork
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this bestand except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.clockwork.ebms.admin.plugin.ebf.afleverservice.web;

import java.util.List;

import javax.xml.bind.JAXBException;

import nl.clockwork.ebms.admin.dao.EbMSDAO;
import nl.clockwork.ebms.admin.model.EbMSAttachment;
import nl.clockwork.ebms.common.XMLMessageBuilder;
import nl.logius.digipoort.ebms._2_0.afleverservice._1.AfleverBericht;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.IGenericComponent;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AfleverBerichtViewPanel extends Panel implements IGenericComponent<AfleverBericht>
{
	private static final long serialVersionUID = 1L;
	protected Log logger = LogFactory.getLog(this.getClass());
	@SpringBean(name="ebMSAdminDAO")
	private EbMSDAO ebMSDAO;

	public AfleverBerichtViewPanel(String id, List<EbMSAttachment> attachments) throws JAXBException
	{
		super(id);
		EbMSAttachment attachment = attachments.get(0);
		attachment = ebMSDAO.findAttachment(attachment.getMessage().getMessageId(),attachment.getMessage().getMessageNr(),attachment.getContentId());
		setModel(new CompoundPropertyModel<AfleverBericht>(XMLMessageBuilder.getInstance(AfleverBericht.class).handle(new String(attachment.getContent()))));
		add(new Label("kenmerk"));
		add(new Label("berichtsoort"));
		add(new Label("berichtkenmerk"));
		add(new Label("aanleverkenmerk"));
		add(new DateLabel("tijdstempelAangeleverd",new StyleDateConverter(true)));
		add(new Label("identiteitBelanghebbende.nummer"));
		add(new Label("identiteitBelanghebbende.type"));
		add(new Label("rolBelanghebbende"));
		add(new Label("identiteitOntvanger.nummer"));
		add(new Label("identiteitOntvanger.type"));
		add(new Label("rolOntvanger"));
		add(new Label("berichtInhoud.mimeType"));
		add(new Label("berichtInhoud.bestandsnaam"));
	}

	@Override
	public AfleverBericht getModelObject()
	{
		return (AfleverBericht)getDefaultModelObject();
	}

	@Override
	public void setModelObject(AfleverBericht object)
	{
		setDefaultModelObject(object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IModel<AfleverBericht> getModel()
	{
		return (IModel<AfleverBericht>)getDefaultModel();
	}

	@Override
	public void setModel(IModel<AfleverBericht> model)
	{
		setDefaultModel(model);
	}

}