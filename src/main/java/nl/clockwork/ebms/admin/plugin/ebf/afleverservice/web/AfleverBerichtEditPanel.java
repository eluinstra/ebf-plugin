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
import java.util.List;

import javax.xml.bind.JAXBException;

import nl.clockwork.ebms.admin.web.BootstrapFormComponentFeedbackBorder;
import nl.clockwork.ebms.admin.web.BootstrapXMLGregorianCalendarDateTimePicker;
import nl.clockwork.ebms.admin.web.service.message.DataSourcesPanel;
import nl.clockwork.ebms.common.XMLMessageBuilder;
import nl.clockwork.ebms.model.EbMSDataSource;
import nl.logius.digipoort.ebms._2_0.afleverservice._1.AfleverBericht;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class AfleverBerichtEditPanel extends DataSourcesPanel
{
	private static final long serialVersionUID = 1L;
	protected Log logger = LogFactory.getLog(this.getClass());
	private enum IdentiteitType
	{
		BSN,KvK,BTW,Fi,OIN;
	}

	public AfleverBerichtEditPanel(String id)
	{
		super(id);
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

			TextField<String> kenmerk = new TextField<String>("kenmerk")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<String> getLabel()
				{
					return Model.of(getLocalizer().getString("lbl.kenmerk",AfleverBerichtForm.this));
				}
			};
			kenmerk.setRequired(true);
			add(new BootstrapFormComponentFeedbackBorder("kenmerkFeedback",kenmerk));

			TextField<String> berichtsoort = new TextField<String>("berichtsoort")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<String> getLabel()
				{
					return Model.of(getLocalizer().getString("lbl.berichtsoort",AfleverBerichtForm.this));
				}
			};
			berichtsoort.setRequired(true);
			add(new BootstrapFormComponentFeedbackBorder("berichtsoortFeedback",berichtsoort));

			TextField<String> berichtkenmerk = new TextField<String>("berichtkenmerk")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<String> getLabel()
				{
					return Model.of(getLocalizer().getString("lbl.berichtkenmerk",AfleverBerichtForm.this));
				}
			};
			berichtkenmerk.setRequired(true);
			add(new BootstrapFormComponentFeedbackBorder("berichtkenmerkFeedback",berichtkenmerk));

			TextField<String> aanleverkenmerk = new TextField<String>("aanleverkenmerk")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<String> getLabel()
				{
					return Model.of(getLocalizer().getString("lbl.aanleverkenmerk",AfleverBerichtForm.this));
				}
			};
			add(aanleverkenmerk);
			add(new BootstrapFormComponentFeedbackBorder("aanleverkenmerkFeedback",aanleverkenmerk));

			BootstrapXMLGregorianCalendarDateTimePicker tijdstempelAangeleverd = new BootstrapXMLGregorianCalendarDateTimePicker("tijdstempelAangeleverd","dd-MM-yyyy hh:mm:ss")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<String> getLabel()
				{
					return Model.of(getLocalizer().getString("lbl.tijdstempelAangeleverd",AfleverBerichtForm.this));
				}
			};
			tijdstempelAangeleverd.setType(BootstrapXMLGregorianCalendarDateTimePicker.Type.DATE_TIME);
			tijdstempelAangeleverd.setRequired(true);
			add(tijdstempelAangeleverd);

			TextField<String> identiteitBelanghebbendeNummer = new TextField<String>("identiteitBelanghebbendeNummer",new PropertyModel<String>(this.getModelObject(),"identiteitBelanghebbende.nummer"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<String> getLabel()
				{
					return Model.of(getLocalizer().getString("lbl.identiteitBelanghebbendeNummer",AfleverBerichtForm.this));
				}
			};
			identiteitBelanghebbendeNummer.setRequired(true);
			add(new BootstrapFormComponentFeedbackBorder("identiteitBelanghebbendeNummerFeedback",identiteitBelanghebbendeNummer));

			DropDownChoice<IdentiteitType> identiteitBelanghebbendeType = new DropDownChoice<IdentiteitType>("identiteitBelanghebbendeType",new PropertyModel<IdentiteitType>(this.getModelObject(),"identiteitBelanghebbende.type"),Arrays.asList(IdentiteitType.values()))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<String> getLabel()
				{
					return Model.of(getLocalizer().getString("lbl.identiteitBelanghebbendeType",AfleverBerichtForm.this));
				}
			};
			identiteitBelanghebbendeType.setRequired(true);
			add(new BootstrapFormComponentFeedbackBorder("identiteitBelanghebbendeTypeFeedback",identiteitBelanghebbendeType));

			TextField<String> rolBelanghebbende = new TextField<String>("rolBelanghebbende")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<String> getLabel()
				{
					return Model.of(getLocalizer().getString("lbl.rolBelanghebbende",AfleverBerichtForm.this));
				}
			};
			rolBelanghebbende.setRequired(true);
			add(new BootstrapFormComponentFeedbackBorder("rolBelanghebbendeFeedback",rolBelanghebbende));

			TextField<String> identiteitOntvangerNummer = new TextField<String>("identiteitOntvangerNummer",new PropertyModel<String>(this.getModelObject(),"identiteitOntvanger.nummer"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<String> getLabel()
				{
					return Model.of(getLocalizer().getString("lbl.identiteitOntvangerNummer",AfleverBerichtForm.this));
				}
				
				@SuppressWarnings("unchecked")
				@Override
				public boolean isRequired()
				{
					return !StringUtils.isEmpty(((DropDownChoice<IdentiteitType>)AfleverBerichtForm.this.get("identiteitOntvangerTypeFeedback:identiteitOntvangerTypeFeedback_body:identiteitOntvangerType")).getInput());
				}
			};
			add(new BootstrapFormComponentFeedbackBorder("identiteitOntvangerNummerFeedback",identiteitOntvangerNummer));

			DropDownChoice<IdentiteitType> identiteitOntvangerType = new DropDownChoice<IdentiteitType>("identiteitOntvangerType",new PropertyModel<IdentiteitType>(this.getModelObject(),"identiteitOntvanger.type"),Arrays.asList(IdentiteitType.values()))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<String> getLabel()
				{
					return Model.of(getLocalizer().getString("lbl.identiteitOntvangerType",AfleverBerichtForm.this));
				}
				
				@SuppressWarnings("unchecked")
				@Override
				public boolean isRequired()
				{
					return !StringUtils.isEmpty(((TextField<String>)AfleverBerichtForm.this.get("identiteitOntvangerNummerFeedback:identiteitOntvangerNummerFeedback_body:identiteitOntvangerNummer")).getInput());
				}
			};
			identiteitOntvangerType.setNullValid(true);
			add(new BootstrapFormComponentFeedbackBorder("identiteitOntvangerTypeFeedback",identiteitOntvangerType));
			
			TextField<String> rolOntvanger = new TextField<String>("rolOntvanger")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<String> getLabel()
				{
					return Model.of(getLocalizer().getString("lbl.rolOntvanger",AfleverBerichtForm.this));
				}
			};
			add(new BootstrapFormComponentFeedbackBorder("rolOntvangerFeedback",rolOntvanger));

			TextField<String> mimeType = new TextField<String>("mimeType",new PropertyModel<String>(this.getModelObject(),"berichtInhoud.mimeType"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<String> getLabel()
				{
					return Model.of(getLocalizer().getString("lbl.mimeType",AfleverBerichtForm.this));
				}
			};
			mimeType.setRequired(true);
			add(new BootstrapFormComponentFeedbackBorder("mimeTypeFeedback",mimeType));

			TextField<String> bestandsnaam = new TextField<String>("bestandsnaam",new PropertyModel<String>(this.getModelObject(),"berichtInhoud.bestandsnaam"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<String> getLabel()
				{
					return Model.of(getLocalizer().getString("lbl.bestandsnaam",AfleverBerichtForm.this));
				}
			};
			bestandsnaam.setRequired(true);
			add(new BootstrapFormComponentFeedbackBorder("bestandsnaamFeedback",bestandsnaam));

			FileUploadField inhoud = new FileUploadField("file")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<String> getLabel()
				{
					return Model.of(getLocalizer().getString("lbl.inhoud",AfleverBerichtForm.this));
				}
			};
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