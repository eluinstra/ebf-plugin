package nl.clockwork.ebms.admin.plugin.ebf.afleverservice.web;

import java.util.Arrays;

import nl.clockwork.ebms.admin.web.BootstrapFormComponentFeedbackBorder;
import nl.logius.digipoort.ebms._2_0.afleverservice._1.IdentiteitType;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
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

	public IdentiteitPanel(String id, IModel<String> title, IModel<IdentiteitType> model)
	{
		super(id,model);
		add(new IdentiteitTypeForm("form",title,model));
	}

	public class IdentiteitTypeForm extends Form<IdentiteitType>
	{
		private static final long serialVersionUID = 1L;
		
		public IdentiteitTypeForm(String id, IModel<String> title, IModel<IdentiteitType> model)
		{
			super(id,new CompoundPropertyModel<IdentiteitType>(model));

			add(new Label("title",title));

			TextField<String> nummer = new TextField<String>("nummer");
			nummer.setLabel(new ResourceModel("lbl.nummer"));
			nummer.setRequired(true);
			add(new BootstrapFormComponentFeedbackBorder("nummerFeedback",nummer));

			DropDownChoice<Type> type = new DropDownChoice<Type>("type",Arrays.asList(Type.values()));
			type.setLabel(new ResourceModel("lbl.type"));
			type.setRequired(true);
			add(new BootstrapFormComponentFeedbackBorder("typeFeedback",type));
		}

	}
}
