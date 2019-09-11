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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import nl.clockwork.ebms.admin.web.BootstrapDateTimePicker;
import nl.clockwork.ebms.admin.web.BootstrapDateTimePicker.Type;
import nl.clockwork.ebms.admin.web.BootstrapFormComponentFeedbackBorder;
import nl.clockwork.ebms.admin.web.service.message.DataSourcesPanel;
import nl.clockwork.ebms.common.JAXBParser;
import nl.clockwork.ebms.model.EbMSDataSource;
import nl.logius.digipoort.ebms._2_0.afleverservice._1.AfleverBericht;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;

public class AfleverBerichtEditPanel extends DataSourcesPanel
{
	private enum IdentiteitType
	{
		BSN,KvK,BTW,Fi,OIN;
	}
	private static final long serialVersionUID = 1L;
	protected transient Log logger = LogFactory.getLog(this.getClass());

	public AfleverBerichtEditPanel(String id)
	{
		super(id);
		add(new Label("title",new ResourceModel("afleverBericht")));
		add(new AfleverBerichtForm("form"));
	}

	@Override
	public List<EbMSDataSource> getDataSources()
	{
		ArrayList<EbMSDataSource> result = new ArrayList<>();
		try
		{
			AfleverBerichtModel afleverBericht = ((AfleverBerichtForm)get("form")).getModelObject();
			afleverBericht.getFile().forEach(f -> afleverBericht.getBerichtInhoud().setInhoud(f.getBytes()));
			String xml = JAXBParser.getInstance(AfleverBericht.class).handle(afleverBericht);
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
			super(id,new CompoundPropertyModel<>(new AfleverBerichtModel()));
			setMultiPart(true);

			add(new BootstrapFormComponentFeedbackBorder("kenmerk.feedback",new TextField<String>("kenmerk").setLabel(new ResourceModel("lbl.kenmerk")).setRequired(true)));
			add(new BootstrapFormComponentFeedbackBorder("berichtsoort.feedback",new TextField<String>("berichtsoort").setLabel(new ResourceModel("lbl.berichtsoort")).setRequired(true)));
			add(new BootstrapFormComponentFeedbackBorder("berichtkenmerk.feedback",new TextField<String>("berichtkenmerk").setLabel(new ResourceModel("lbl.berichtkenmerk")).setRequired(true)));
			add(new BootstrapFormComponentFeedbackBorder("aanleverkenmerk.feedback",new TextField<String>("aanleverkenmerk").setLabel(new ResourceModel("lbl.aanleverkenmerk"))));
			add(new BootstrapFormComponentFeedbackBorder("tijdstempelAangeleverd.feedback",new BootstrapDateTimePicker("tijdstempelAangeleverd","dd-MM-yyyy hh:mm:ss",Type.DATE_TIME).setLabel(new ResourceModel("lbl.tijdstempelAangeleverd")).setRequired(true)));

			add(new BootstrapFormComponentFeedbackBorder("identiteitBelanghebbende.nummer.feedback",new TextField<String>("identiteitBelanghebbende.nummer").setLabel(new ResourceModel("lbl.identiteitBelanghebbende.nummer")).setRequired(true)));

			DropDownChoice<IdentiteitType> identiteitBelanghebbendeType = new DropDownChoice<>("identiteitBelanghebbende.type",Arrays.asList(IdentiteitType.values()));
			identiteitBelanghebbendeType.setLabel(new ResourceModel("lbl.identiteitBelanghebbende.type"));
			identiteitBelanghebbendeType.setRequired(true);
			identiteitBelanghebbendeType.setNullValid(false);
			add(new BootstrapFormComponentFeedbackBorder("identiteitBelanghebbende.type.feedback",identiteitBelanghebbendeType));

			add(new BootstrapFormComponentFeedbackBorder("rolBelanghebbende.feedback",new TextField<String>("rolBelanghebbende").setLabel(new ResourceModel("lbl.rolBelanghebbende")).setRequired(true)));

			TextField<String> identiteitOntvangerNummer = new TextField<String>("identiteitOntvanger.nummer")
			{
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public boolean isRequired()
				{
					return !StringUtils.isEmpty(((DropDownChoice<IdentiteitType>)AfleverBerichtForm.this.get("identiteitOntvanger.type.feedback:identiteitOntvanger.type.feedback_body:identiteitOntvanger.type")).getInput());
				}
			};
			identiteitOntvangerNummer.setLabel(new ResourceModel("lbl.identiteitOntvanger.nummer"));
			add(new BootstrapFormComponentFeedbackBorder("identiteitOntvanger.nummer.feedback",identiteitOntvangerNummer));

			DropDownChoice<IdentiteitType> identiteitOntvangerType = new DropDownChoice<IdentiteitType>("identiteitOntvanger.type",Arrays.asList(IdentiteitType.values()))
			{
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public boolean isRequired()
				{
					return !StringUtils.isEmpty(((TextField<String>)AfleverBerichtForm.this.get("identiteitOntvanger.nummer.feedback:identiteitOntvanger.nummer.feedback_body:identiteitOntvanger.nummer")).getInput());
				}
				
				@SuppressWarnings("unchecked")
				@Override
				public boolean isNullValid()
				{
					return StringUtils.isEmpty(((TextField<String>)AfleverBerichtForm.this.get("identiteitOntvanger.nummer.feedback:identiteitOntvanger.nummer.feedback_body:identiteitOntvanger.nummer")).getInput());
				}
			};
			identiteitOntvangerType.setLabel(new ResourceModel("lbl.identiteitOntvanger.type"));
			add(new BootstrapFormComponentFeedbackBorder("identiteitOntvanger.type.feedback",identiteitOntvangerType));

			add(new BootstrapFormComponentFeedbackBorder("rolOntvanger.feedback",new TextField<String>("rolOntvanger").setLabel(new ResourceModel("lbl.rolOntvanger"))));

			TextField<String> mimeType = new TextField<>("berichtInhoud.mimeType");
			mimeType.setLabel(new ResourceModel("lbl.berichtInhoud.mimeType"));
			mimeType.setRequired(true);
			add(new BootstrapFormComponentFeedbackBorder("mimeType.feedback",mimeType));

			TextField<String> bestandsnaam = new TextField<>("berichtInhoud.bestandsnaam");
			bestandsnaam.setLabel(new ResourceModel("lbl.berichtInhoud.bestandsnaam"));
			bestandsnaam.setRequired(true);
			add(new BootstrapFormComponentFeedbackBorder("bestandsnaam.feedback",bestandsnaam));

			FileUploadField inhoud = new FileUploadField("file");
			inhoud.setLabel(new ResourceModel("lbl.berichtInhoud.inhoud"));
			inhoud.setRequired(true);
			add(new BootstrapFormComponentFeedbackBorder("inhoud.feedback",inhoud));

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
			tijdstempelAangeleverd = new Date();
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