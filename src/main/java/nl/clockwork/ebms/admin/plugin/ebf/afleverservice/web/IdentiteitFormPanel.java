package nl.clockwork.ebms.admin.plugin.ebf.afleverservice.web;

import java.util.Arrays;

import nl.clockwork.ebms.admin.web.BootstrapFormComponentFeedbackBorder;
import nl.logius.digipoort.ebms._2_0.afleverservice._1.IdentiteitType;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

public class IdentiteitFormPanel extends FormComponentPanel<IdentiteitType>
{
	private enum Type
	{
		BSN,KvK,BTW,Fi,OIN;
	}
	private static final long serialVersionUID = 1L;
	private TextField<String> nummer;
	private DropDownChoice<Type> type;

	public IdentiteitFormPanel(String id)
	{
		this(id,null);
	}

	public IdentiteitFormPanel(String id, IModel<IdentiteitType> model)
	{
		super(id,model);

		nummer = new TextField<String>("nummer",new Model<String>())
		{
			private static final long serialVersionUID = 1L;
	
			@SuppressWarnings("unchecked")
			@Override
			public boolean isRequired()
			{
				return IdentiteitFormPanel.this.isRequired() || !StringUtils.isEmpty(((DropDownChoice<IdentiteitType>)IdentiteitFormPanel.this.get("type.feedback:type.feedback_body:type")).getInput());
			}
		};
		nummer.setLabel(new ResourceModel("lbl.nummer"));
		add(new BootstrapFormComponentFeedbackBorder("nummer.feedback",nummer));

		type = new DropDownChoice<Type>("type",new Model<Type>(),Arrays.asList(Type.values()))
		{
			private static final long serialVersionUID = 1L;
	
			@SuppressWarnings("unchecked")
			@Override
			public boolean isRequired()
			{
				return IdentiteitFormPanel.this.isRequired() || !StringUtils.isEmpty(((TextField<String>)IdentiteitFormPanel.this.get("nummer.feedback:nummer.feedback_body:nummer")).getInput());
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public boolean isNullValid()
			{
				return !IdentiteitFormPanel.this.isRequired() || StringUtils.isEmpty(((TextField<String>)IdentiteitFormPanel.this.get("nummer.feedback:nummer.feedback_body:nummer")).getInput());
			}
		};
		type.setLabel(new ResourceModel("lbl.type"));
		add(new BootstrapFormComponentFeedbackBorder("type.feedback",type));
	}

	@Override
	protected void convertInput()
	{
		if (nummer.getConvertedInput() != null || type.getConvertedInput() != null)
		{
			IdentiteitType identiteitType = new IdentiteitType();
			identiteitType.setNummer(nummer.getConvertedInput());
			identiteitType.setType(type.getConvertedInput() != null ? type.getConvertedInput().toString() : null);
			setConvertedInput(identiteitType);
		}
		else
			setConvertedInput(null);
	}

	@Override
	protected void onBeforeRender()
	{
		if (getModelObject() != null)
		{
			nummer.setModelObject(getModelObject().getNummer());
			type.setModelObject(getModelObject().getType() != null ? Type.valueOf(getModelObject().getType()) : null);
		}
		super.onBeforeRender();
	}
}
