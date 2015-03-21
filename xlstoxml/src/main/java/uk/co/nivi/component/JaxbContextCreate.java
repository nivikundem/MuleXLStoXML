package uk.co.nivi.component;

import javax.xml.bind.JAXBContext;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class JaxbContextCreate implements Callable {
	
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		
		JAXBContext jc = JAXBContext.newInstance("uk.co.csvtopojo");

		return jc;
		
	}
}
