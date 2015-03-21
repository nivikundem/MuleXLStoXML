package uk.co.nivi.component;

import javax.xml.bind.JAXBContext;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class JaxbContextCreateISBN implements Callable {
	
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		
		JAXBContext jc = JAXBContext.newInstance("uk.co.isbn");

		return jc;
		
	}
}
