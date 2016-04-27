package nl.clockwork.ebms.admin.plugin.ebf.afleverservice.web;

import java.util.Arrays;

import nl.clockwork.ebms.admin.web.BootstrapFormComponentFeedbackBorder;
import nl.logius.digipoort.ebms._2_0.afleverservice._1.IdentiteitType;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

public class IdentiteitPanel extends Panel
{
	private enum Type
	{
		BSN,KvK,BTW,Fi,OIN;
	}
	private static final long serialVersionUID = 1L;
	private boolean required;

	public IdentiteitPanel(String id, IModel<IdentiteitType> model)
	{
		super(id,model);
		add(new IdentiteitTypeForm("form",model));
	}

	public class IdentiteitTypeForm extends Form<IdentiteitType>
	{
		private static final long serialVersionUID = 1L;
		
		public IdentiteitTypeForm(String id, IModel<IdentiteitType> model)
		{
			super(id,new CompoundPropertyModel<IdentiteitType>(model));

			add(new BootstrapFormComponentFeedbackBorder("nummerFeedback",createNummerTextField()));
			add(new BootstrapFormComponentFeedbackBorder("typeFeedback",createTypeChoice()));
		}

		private FormComponent<String> createNummerTextField()
		{
			TextField<String> result = new TextField<String>("nummer")
			{
				private static final long serialVersionUID = 1L;
				
				@Override
				public boolean isRequired()
				{
					return required;
				}
			};
			result.setLabel(new ResourceModel("lbl.nummer"));
			return result;
		}

		private DropDownChoice<Type> createTypeChoice()
		{
			DropDownChoice<Type> result = new DropDownChoice<Type>("type",Arrays.asList(Type.values()))
			{
				private static final long serialVersionUID = 1L;
				
				@Override
				public boolean isRequired()
				{
					return required;
				}
			};
			result.setLabel(new ResourceModel("lbl.type"));
			return result;
		}
	}

	public IdentiteitPanel setRequired(boolean required)
	{
		this.required = required;
		return this;
	}
}
