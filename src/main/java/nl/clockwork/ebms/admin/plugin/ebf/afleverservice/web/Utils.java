package nl.clockwork.ebms.admin.plugin.ebf.afleverservice.web;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class Utils
{
	public static XMLGregorianCalendar getXMLGregorianCalendar(GregorianCalendar calendar)
	{
		try
		{
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
		}
		catch (DatatypeConfigurationException e)
		{
			return null;
		}
	}
}
