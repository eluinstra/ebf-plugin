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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBException;

import nl.clockwork.ebms.admin.web.BootstrapFormComponentFeedbackBorder;
import nl.clockwork.ebms.admin.web.BootstrapXMLGregorianCalendarDateTimePicker;
import nl.clockwork.ebms.admin.web.service.message.DataSourcesPanel;
import nl.clockwork.ebms.common.XMLMessageBuilder;
import nl.clockwork.ebms.model.EbMSDataSource;
import nl.logius.digipoort.ebms._2_0.afleverservice._1.AfleverBericht;
import nl.logius.digipoort.ebms._2_0.afleverservice._1.IdentiteitType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

public class AfleverBerichtEditPanel extends DataSourcesPanel
{
	private static final long serialVersionUID = 1L;
	protected Log logger = LogFactory.getLog(this.getClass());

	public AfleverBerichtEditPanel(String id)
	{
		super(id);
		add(new Label("title",new ResourceModel("afleverBericht")));
		add(new AfleverBerichtForm("form"));
	}

	@Override
	public List<EbMSDataSource> getDataSources()
	{
		ArrayList<EbMSDataSource> result = new ArrayList<EbMSDataSource>();
		try
		{
			AfleverBerichtModel afleverBericht = ((AfleverBerichtForm)get("form")).getModelObject();
			for (FileUpload file : afleverBericht.getFile())
				afleverBericht.getBerichtInhoud().setInhoud(file.getBytes());
			String xml = XMLMessageBuilder.getInstance(AfleverBericht.class).handle(afleverBericht);
			result.add(new EbMSDataSource("afleverbericht.xml","application/xml",xml.getBytes()));
		}
		catch (JAXBException e)
		{
			error(e.getMessage());
			logger.error("",e);
		}
		return result;
	}

	private static class AfleverBerichtForm extends Form<AfleverBerichtModel>
	{
		private static final long serialVersionUID = 1L;
		
		public AfleverBerichtForm(String id)
		{
			super(id,new CompoundPropertyModel<AfleverBerichtModel>(new AfleverBerichtModel()));
			setMultiPart(true);

			add(new BootstrapFormComponentFeedbackBorder("kenmerkFeedback",new TextField<String>("kenmerk").setLabel(new ResourceModel("lbl.kenmerk")).setRequired(true)));
			add(new BootstrapFormComponentFeedbackBorder("berichtsoortFeedback",new TextField<String>("berichtsoort").setLabel(new ResourceModel("lbl.berichtsoort")).setRequired(true)));
			add(new BootstrapFormComponentFeedbackBorder("berichtkenmerkFeedback",new TextField<String>("berichtkenmerk").setLabel(new ResourceModel("lbl.berichtkenmerk")).setRequired(true)));
			add(new BootstrapFormComponentFeedbackBorder("aanleverkenmerkFeedback",new TextField<String>("aanleverkenmerk").setLabel(new ResourceModel("lbl.aanleverkenmerk"))));
			add(new BootstrapFormComponentFeedbackBorder("tijdstempelAangeleverdFeedback",new BootstrapXMLGregorianCalendarDateTimePicker("tijdstempelAangeleverd","dd-MM-yyyy hh:mm:ss").setType(BootstrapXMLGregorianCalendarDateTimePicker.Type.DATE_TIME).setLabel(new ResourceModel("lbl.tijdstempelAangeleverd")).setRequired(true)));
			//add(new IdentiteitPanel("identiteitBelanghebbende",new PropertyModel<IdentiteitType>(getModelObject(),"identiteitBelanghebbende")).setRequired(true));
			add(new IdentiteitFormPanel("identiteitBelanghebbende",new PropertyModel<IdentiteitType>(getModelObject(),"identiteitBelanghebbende")).setRequired(true));
			add(new BootstrapFormComponentFeedbackBorder("rolBelanghebbendeFeedback",new TextField<String>("rolBelanghebbende").setLabel(new ResourceModel("lbl.rolBelanghebbende")).setRequired(true)));
			//add(new IdentiteitPanel("identiteitOntvanger",new PropertyModel<IdentiteitType>(getModelObject(),"identiteitOntvanger")));
			add(new IdentiteitFormPanel("identiteitOntvanger",new PropertyModel<IdentiteitType>(getModelObject(),"identiteitOntvanger")));
			add(new BootstrapFormComponentFeedbackBorder("rolOntvangerFeedback",new TextField<String>("rolOntvanger").setLabel(new ResourceModel("lbl.rolOntvanger"))));

			TextField<String> mimeType = new TextField<String>("berichtInhoud.mimeType");
			mimeType.setLabel(new ResourceModel("lbl.mimeType"));
			mimeType.setRequired(true);
			add(new BootstrapFormComponentFeedbackBorder("mimeTypeFeedback",mimeType));

			TextField<String> bestandsnaam = new TextField<String>("berichtInhoud.bestandsnaam");
			bestandsnaam.setLabel(new ResourceModel("lbl.bestandsnaam"));
			bestandsnaam.setRequired(true);
			add(new BootstrapFormComponentFeedbackBorder("bestandsnaamFeedback",bestandsnaam));

			FileUploadField inhoud = new FileUploadField("file");
			inhoud.setLabel(new ResourceModel("lbl.inhoud"));
			inhoud.setRequired(true);
			add(new BootstrapFormComponentFeedbackBorder("inhoudFeedback",inhoud));

			//BerichtBijlagenType berichtBijlagen;
			//FoutLijstType foutLijst;
		}

	}

	public static class AfleverBerichtModel extends AfleverBericht
	{
		private static final long serialVersionUID = 1L;
		private List<FileUpload> file;

		public AfleverBerichtModel()
		{
			tijdstempelAangeleverd = Utils.getXMLGregorianCalendar(new GregorianCalendar());
		}

		public List<FileUpload> getFile()
		{
			return file;
		}
		public void setFile(List<FileUpload> file)
		{
			this.file = file;
		}
	}
}