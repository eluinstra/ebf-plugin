package nl.clockwork.ebms.admin.plugin.ebf.afleverservice.web;

import java.util.Arrays;

import nl.clockwork.ebms.admin.web.BootstrapFormComponentFeedbackBorder;
import nl.logius.digipoort.ebms._2_0.afleverservice._1.IdentiteitType;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

public class IdentiteitFormPanel extends FormComponentPanel<IdentiteitType>
{
	private enum Type
	{
		BSN,KvK,BTW,Fi,OIN;
	}
	private static final long serialVersionUID = 1L;
	private IdentiteitType identiteitType;
	TextField<String> nummer;
	DropDownChoice<Type> type;

	public IdentiteitFormPanel(String id)
	{
		this(id,null);
	}

	public IdentiteitFormPanel(String id, IModel<IdentiteitType> model)
	{
		super(id,new CompoundPropertyModel<IdentiteitType>(model));

		nummer = new TextField<String>("nummer")
		{
			private static final long serialVersionUID = 1L;
	
			@Override
			public boolean isRequired()
			{
				return IdentiteitFormPanel.this.isRequired();
			}
		};
		nummer.setLabel(new ResourceModel("lbl.nummer"));
		add(new BootstrapFormComponentFeedbackBorder("nummerFeedback",nummer));

		type = new DropDownChoice<Type>("type",Arrays.asList(Type.values()))
		{
			private static final long serialVersionUID = 1L;
	
			@Override
			public boolean isRequired()
			{
				return IdentiteitFormPanel.this.isRequired();
			}
			
			@Override
			public boolean isNullValid()
			{
				return !IdentiteitFormPanel.this.isRequired();
			}
		};
		type.setLabel(new ResourceModel("lbl.type"));
		add(new BootstrapFormComponentFeedbackBorder("typeFeedback",type));
	}

	@Override
	protected void convertInput()
	{
		if (getModelObject() == null) setModelObject(new IdentiteitType());
		identiteitType = getModelObject();
		identiteitType.setNummer(nummer.getConvertedInput());
		identiteitType.setType(type.getConvertedInput() != null ? type.getConvertedInput().toString(): null);
		setConvertedInput(identiteitType);
	}
	
	@Override
	protected void onBeforeRender()
	{
		identiteitType = getModelObject();
		super.onBeforeRender();
	}
	
	public IdentiteitType getIdentiteitType()
	{
		return identiteitType;
	}

	public void setIdentiteitType(IdentiteitType identiteitType)
	{
		this.identiteitType = identiteitType;
	}
}
