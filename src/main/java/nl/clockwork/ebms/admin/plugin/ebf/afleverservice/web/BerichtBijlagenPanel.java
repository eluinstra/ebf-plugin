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

import nl.logius.digipoort.ebms._2_0.afleverservice._1.BerichtBijlagenType;
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

public class BerichtBijlagenPanel extends Panel
{
	private class BerichtBijlageListView extends ListView<BerichtInhoudType>
	{
		private BerichtBijlagenForm berichtBijlagenForm;

		public BerichtBijlageListView(String id, BerichtBijlagenForm berichtBijlagenForm)
		{
			super(id);
			this.berichtBijlagenForm = berichtBijlagenForm;
			setOutputMarkupId(true);
		}

		private static final long serialVersionUID = 1L;

		@Override
		protected void populateItem(final ListItem<BerichtInhoudType> item)
		{
			item.setModel(new CompoundPropertyModel<BerichtInhoudType>(item.getModelObject()));
			item.add(new Label("bestandsnaam"));
			item.add(new Label("mimeType"));
			item.add(new AjaxButton("remove",new ResourceModel("cmd.remove"),berichtBijlagenForm)
			{
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form)
				{
					berichtBijlagenForm.getModelObject().getBerichtBijlagen().getBijlage().remove(item.getModelObject());
					target.add(berichtBijlagenForm);
				}
			});
		}
	}
	private static final long serialVersionUID = 1L;
	protected Log logger = LogFactory.getLog(this.getClass());

	public BerichtBijlagenPanel(String id, BerichtBijlagenType berichtBijlagen)
	{
		super(id);
		add(new BerichtBijlagenForm("form",berichtBijlagen));
	}
	
	public class BerichtBijlagenForm extends Form<BerichtInhoudenModel>
	{
		private static final long serialVersionUID = 1L;

		public BerichtBijlagenForm(String id, BerichtBijlagenType berichtBijlagen)
		{
			super(id,new CompoundPropertyModel<BerichtInhoudenModel>(new BerichtInhoudenModel(berichtBijlagen)));
			add(new BerichtBijlageListView("berichtBijlagen.bijlage",BerichtBijlagenForm.this));
			final ModalWindow berichtInhoudModalWindow = new BerichtInhoudModalWindow("berichtInhoudModelWindow",BerichtBijlagenForm.this)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void addBerichtInhoud(BerichtInhoudType berichtInhoud)
				{
					getModelObject().getBerichtBijlagen().getBijlage().add(berichtInhoud);
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
				protected void onSubmit(AjaxRequestTarget target, Form<?> form)
				{
					berichtInhoudModalWindow.show(target);
				}
			};
			return result;
		}
	}
	
	public BerichtBijlagenType getBerichtInhouden()
	{
		return ((BerichtBijlagenForm)this.get("form")).getModelObject().getBerichtBijlagen();
	}

	public static class BerichtInhoudenModel implements IClusterable
	{
		private static final long serialVersionUID = 1L;
		private BerichtBijlagenType berichtBijlagen;

		public BerichtInhoudenModel(BerichtBijlagenType berichtBijlagen)
		{
			this.berichtBijlagen = berichtBijlagen;
		}

		public BerichtBijlagenType getBerichtBijlagen()
		{
			return berichtBijlagen;
		}
	}

}