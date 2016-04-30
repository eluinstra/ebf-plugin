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
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import nl.clockwork.ebms.admin.web.BootstrapDateTimePicker;
import nl.clockwork.ebms.admin.web.BootstrapDateTimePicker.Type;
import nl.clockwork.ebms.admin.web.BootstrapFormComponentFeedbackBorder;
import nl.clockwork.ebms.admin.web.service.message.DataSourcesPanel;
import nl.clockwork.ebms.common.XMLMessageBuilder;
import nl.clockwork.ebms.model.EbMSDataSource;
import nl.logius.digipoort.ebms._2_0.afleverservice._1.AfleverBericht;
import nl.logius.digipoort.ebms._2_0.afleverservice._1.BerichtBijlagenType;
import nl.logius.digipoort.ebms._2_0.afleverservice._1.BerichtInhoudType;
import nl.logius.digipoort.ebms._2_0.afleverservice._1.IdentiteitType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

public class AfleverBerichtEditPanelX extends DataSourcesPanel
{
	private static final long serialVersionUID = 1L;
	protected Log logger = LogFactory.getLog(this.getClass());

	public AfleverBerichtEditPanelX(String id)
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

			add(new BootstrapFormComponentFeedbackBorder("kenmerk.feedback",new TextField<String>("kenmerk").setLabel(new ResourceModel("lbl.kenmerk")).setRequired(true)));
			add(new BootstrapFormComponentFeedbackBorder("berichtsoort.feedback",new TextField<String>("berichtsoort").setLabel(new ResourceModel("lbl.berichtsoort")).setRequired(true)));
			add(new BootstrapFormComponentFeedbackBorder("berichtkenmerk.feedback",new TextField<String>("berichtkenmerk").setLabel(new ResourceModel("lbl.berichtkenmerk")).setRequired(true)));
			add(new BootstrapFormComponentFeedbackBorder("aanleverkenmerk.feedback",new TextField<String>("aanleverkenmerk").setLabel(new ResourceModel("lbl.aanleverkenmerk"))));
			add(new BootstrapFormComponentFeedbackBorder("tijdstempelAangeleverd.feedback",new BootstrapDateTimePicker("tijdstempelAangeleverd","dd-MM-yyyy hh:mm:ss",Type.DATE_TIME).setLabel(new ResourceModel("lbl.tijdstempelAangeleverd")).setRequired(true)));
			add(new IdentiteitFormPanel("identiteitBelanghebbende",new PropertyModel<IdentiteitType>(getModelObject(),"identiteitBelanghebbende")).setRequired(true));
			add(new BootstrapFormComponentFeedbackBorder("rolBelanghebbende.feedback",new TextField<String>("rolBelanghebbende").setLabel(new ResourceModel("lbl.rolBelanghebbende")).setRequired(true)));
			add(new IdentiteitFormPanel("identiteitOntvanger",new PropertyModel<IdentiteitType>(getModelObject(),"identiteitOntvanger")));
			add(new BootstrapFormComponentFeedbackBorder("rolOntvanger.feedback",new TextField<String>("rolOntvanger").setLabel(new ResourceModel("lbl.rolOntvanger"))));
			add(new BerichtInhoudPanel("berichtInhoud",new PropertyModel<BerichtInhoudType>(getModelObject(),"berichtInhoud")));
			add(new BerichtBijlagenPanel("berichtBijlagen",getModelObject().getBerichtBijlagen()));
		}

	}

	public static class AfleverBerichtModel extends AfleverBericht
	{
		private static final long serialVersionUID = 1L;
		private List<FileUpload> file;

		public AfleverBerichtModel()
		{
			tijdstempelAangeleverd = new Date();
			berichtInhoud = new BerichtInhoudType();
			berichtBijlagen = new BerichtBijlagenType();
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