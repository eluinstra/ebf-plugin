/**
 * Copyright 2013 Clockwork
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
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

import nl.clockwork.ebms.admin.web.BootstrapFeedbackPanel;
import nl.clockwork.ebms.admin.web.BootstrapFormComponentFeedbackBorder;
import nl.clockwork.ebms.admin.web.Utils;
import nl.logius.digipoort.ebms._2_0.afleverservice._1.BerichtInhoudType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

public abstract class BerichtInhoudModalWindow extends ModalWindow
{
	private static final long serialVersionUID = 1L;

	public BerichtInhoudModalWindow(String id, Component...components)
	{
		super(id);
		setCssClassName(ModalWindow.CSS_CLASS_GRAY);
		setContent(createBerichtInhoudPanel(components));
		setCookieName("berichtInhoud");
		setCloseButtonCallback(new nl.clockwork.ebms.admin.web.CloseButtonCallback());
	}

	public abstract void addBerichtInhoud(BerichtInhoudType berichtInhoud);

	@Override
	public IModel<String> getTitle()
	{
		return Model.of(getLocalizer().getString("berichtInhoud",this));
	}

	private BerichtInhoudPanel createBerichtInhoudPanel(final Component...components)
	{
		return new BerichtInhoudPanel(getContentId())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void addBerichtInhoud(BerichtInhoudType berichtInhoud)
			{
				BerichtInhoudModalWindow.this.addBerichtInhoud(berichtInhoud);
			}
			
			@Override
			public Component[] getComponents()
			{
				return components;
			}
			
			@Override
			public ModalWindow getWindow()
			{
				return BerichtInhoudModalWindow.this;
			}
		};
	}
	
	public abstract class BerichtInhoudPanel extends Panel
	{
		private static final long serialVersionUID = 1L;
		protected Log logger = LogFactory.getLog(this.getClass());

		public BerichtInhoudPanel(String id)
		{
			super(id);
			add(new BerichtInhoudForm("form"));
		}
		
		public abstract void addBerichtInhoud(BerichtInhoudType berichtInhoud);
		public abstract Component[] getComponents();
		public abstract ModalWindow getWindow();

		public class BerichtInhoudForm extends Form<BerichtInhoudModel>
		{
			private static final long serialVersionUID = 1L;

			public BerichtInhoudForm(String id)
			{
				super(id,new CompoundPropertyModel<BerichtInhoudModel>(new BerichtInhoudModel()));
				add(new BootstrapFeedbackPanel("feedback"));
				add(new BootstrapFormComponentFeedbackBorder("file.feedback",createFileField("file")));
				add(new TextField<String>("bestandsnaam").setLabel(new ResourceModel("lbl.bestandsnaam")));
				add(new TextField<String>("mimeType").setLabel(new ResourceModel("lbl.mimeType")));
				add(createAddButton("add"));
				add(createCancelButton("cancel"));
			}

			private FileUploadField createFileField(String id)
			{
				FileUploadField result = new FileUploadField(id);
				result.setLabel(new ResourceModel("lbl.file"));
				result.setRequired(true);
				return result;
			}

			private AjaxButton createAddButton(String id)
			{
				final AjaxButton result = new AjaxButton(id,new ResourceModel("cmd.add"))
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form)
					{
						BerichtInhoudModel model = BerichtInhoudForm.this.getModelObject();
						for (FileUpload file : model.getFile())
						{
							BerichtInhoudType berichtInhoud = new BerichtInhoudType();
							berichtInhoud.setBestandsnaam(StringUtils.isBlank(model.getBestandsnaam()) ? file.getClientFileName() : model.getBestandsnaam());
							berichtInhoud.setMimeType(StringUtils.isBlank(model.getMimeType()) ? Utils.getContentType(file.getClientFileName()) : model.getMimeType());
							berichtInhoud.setInhoud(file.getBytes());
							addBerichtInhoud(berichtInhoud);
						}
						if (target != null)
						{
							target.add(getComponents());
							getWindow().close(target);
						}
					}

					@Override
					protected void onError(AjaxRequestTarget target, Form<?> form)
					{
						super.onError(target,form);
						if (target != null)
						{
							target.add(form);
						}
					}
				};
				return result;
			}

			private AjaxButton createCancelButton(String id)
			{
				AjaxButton cancel = new AjaxButton(id,new ResourceModel("cmd.cancel"))
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form)
					{
						getWindow().close(target);
					}
				};
				cancel.setDefaultFormProcessing(false);
				return cancel;
			}
		}
	}

	public static class BerichtInhoudModel extends BerichtInhoudType
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
