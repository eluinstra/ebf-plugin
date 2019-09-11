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

import nl.logius.digipoort.ebms._2_0.afleverservice._1.BerichtInhoudType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

public class BerichtInhoudPanel extends Panel
{
	private static final long serialVersionUID = 1L;
	protected transient Log logger = LogFactory.getLog(this.getClass());

	public BerichtInhoudPanel(String id, IModel<BerichtInhoudType> model)
	{
		super(id);
		add(new BerichtInhoudForm("form",model));
	}
	
	public class BerichtInhoudForm extends Form<BerichtInhoudType>
	{
		private static final long serialVersionUID = 1L;

		public BerichtInhoudForm(String id, IModel<BerichtInhoudType> model)
		{
			super(id,new CompoundPropertyModel<>(model));
			add(new Label("bestandsnaam"));
			add(new Label("mimeType"));
			add(new AjaxButton("remove",new ResourceModel("cmd.remove"),BerichtInhoudForm.this)
			{
				private static final long serialVersionUID = 1L;
				
				@Override
				public boolean isVisible()
				{
					return BerichtInhoudForm.this.getModelObject() != null;
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target)
				{
					BerichtInhoudForm.this.setModelObject(null);
					target.add(BerichtInhoudForm.this);
				}
			});
			final ModalWindow berichtInhoudModalWindow = new BerichtInhoudModalWindow("berichtInhoudModelWindow",BerichtInhoudForm.this)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void addBerichtInhoud(BerichtInhoudType berichtInhoud)
				{
					setModelObject(new BerichtInhoudType());
					getModelObject().setBestandsnaam(berichtInhoud.getBestandsnaam());
					getModelObject().setMimeType(berichtInhoud.getMimeType());
					getModelObject().setInhoud(berichtInhoud.getInhoud());
				}
			};
			add(berichtInhoudModalWindow);
			add(createAddButton("add",berichtInhoudModalWindow));
		}

		private AjaxButton createAddButton(String id, final ModalWindow berichtInhoudModalWindow)
		{
			AjaxButton result = new AjaxButton(id)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return BerichtInhoudForm.this.getModelObject() == null;
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target)
				{
					berichtInhoudModalWindow.show(target);
				}
			};
			return result;
		}
	}
	
}