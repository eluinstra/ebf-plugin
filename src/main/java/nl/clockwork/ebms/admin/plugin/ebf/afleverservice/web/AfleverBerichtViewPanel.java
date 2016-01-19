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
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AfleverBerichtViewPanel extends Panel
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
		AfleverBericht afleverBericht = XMLMessageBuilder.getInstance(AfleverBericht.class).handle(new String(attachment.getContent()));
		add(new Label("kenmerk",afleverBericht.getKenmerk()));
		add(new Label("berichtsoort",afleverBericht.getBerichtsoort()));
		add(new Label("berichtkenmerk",afleverBericht.getBerichtkenmerk()));
		add(new Label("aanleverkenmerk",afleverBericht.getAanleverkenmerk()));
		add(new Label("tijdstempelAangeleverd",afleverBericht.getTijdstempelAangeleverd().toXMLFormat()));
		add(new Label("identiteitBelanghebbendeNummer",afleverBericht.getIdentiteitBelanghebbende().getNummer()));
		add(new Label("identiteitBelanghebbendeType",afleverBericht.getIdentiteitBelanghebbende().getType()));
		add(new Label("rolBelanghebbende",afleverBericht.getRolBelanghebbende()));
		add(new Label("identiteitOntvangerNummer",afleverBericht.getIdentiteitOntvanger() == null ? "" : afleverBericht.getIdentiteitOntvanger().getNummer()));
		add(new Label("identiteitOntvangerType",afleverBericht.getIdentiteitOntvanger() == null ? "" : afleverBericht.getIdentiteitOntvanger().getType()));
		add(new Label("rolOntvanger",afleverBericht.getKenmerk()));
		add(new Label("mimeType",afleverBericht.getBerichtInhoud().getMimeType()));
		add(new Label("bestandsnaam",afleverBericht.getBerichtInhoud().getBestandsnaam()));
	}

}