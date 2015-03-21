package uk.co.nivi.component;

import java.util.ArrayList;
import java.util.Map;

import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.milyn.Smooks;
import org.milyn.container.ExecutionContext;
import org.milyn.payload.JavaResult;
import org.milyn.payload.StringSource;

public class SmooksCSVToPOJO implements Callable {

	
	uk.co.csvtopojo.MessageList MC = null;
	
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		
		MuleMessage msg = eventContext.getMessage();
		
		ArrayList<Map> csvLines = null;
		
		Smooks smooks = new Smooks("/smooks-config-csv2obj.xml");
		try {
			ExecutionContext executionContext = smooks.createExecutionContext();
			JavaResult result = new JavaResult();
			smooks.filterSource(executionContext, new StringSource(msg.getPayloadAsString()), result);
			csvLines = (ArrayList<Map>)result.getBean("csvFile");
		}
		catch (Exception e)
		{
			System.out.println("smooks CSV TO POJO failed");
			System.out.println(e.toString());
		}
		finally {
			smooks.close();
		}
		for (int i=0;i<csvLines.size();i++)
		{
			Map M1 = (Map)csvLines.get(i);
			ArrayList<String> thisRow = (ArrayList<String>)M1.get("csvFields");
			for (int j = 0;j<thisRow.size();j++)
			{
				String old1 = (String)thisRow.get(j);
				String new1 = old1.trim();
				if (!old1.equals(new1)) {thisRow.set(j, new1);}
			}
		}
		return csvLines;
		
	}
}
