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

import java.util.ArrayList;
import java.util.List;

import nl.logius.digipoort.ebms._2_0.afleverservice._1.BerichtInhoudType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.io.IClusterable;

public class BerichtInhoudPanel extends Panel
{
	private static final long serialVersionUID = 1L;
	protected Log logger = LogFactory.getLog(this.getClass());

	public BerichtInhoudPanel(String id)
	{
		super(id);
		add(new BerichtInhoudForm("form"));
	}
	
	public class BerichtInhoudForm extends Form<BerichtInhoudType>
	{
		private static final long serialVersionUID = 1L;

		public BerichtInhoudForm(String id)
		{
			super(id,new CompoundPropertyModel<BerichtInhoudType>(new BerichtInhoudType()));
			add(new Label("bestandsnaam"));
			add(new Label("mimeType"));
			add(new AjaxButton("remove",new ResourceModel("cmd.remove"),berichtInhoudForm)
			{
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form)
				{
					berichtInhoudForm.getModelObject().getBerichtInhouden().remove(item.getModelObject());
					target.add(berichtInhoudForm);
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
					return BerichtInhoudForm.this.getModelObject() != null;
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form)
				{
					berichtInhoudModalWindow.show(target);
				}
			};
			return result;
		}
	}
	
	public BerichtInhoudType getBerichtInhoud()
	{
		return ((BerichtInhoudenForm)this.get("form")).getModelObject().getBerichtInhoud();
	}

	public static class BerichtInhoudModel implements IClusterable
	{
		private static final long serialVersionUID = 1L;
		private BerichtInhoudType berichtInhoud = new BerichtInhoudType();

		public BerichtInhoudType getBerichtInhoud()
		{
			return berichtInhoud;
		}
	}

}