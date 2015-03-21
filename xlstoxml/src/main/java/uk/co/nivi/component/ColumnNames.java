package uk.co.nivi.component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transport.PropertyScope;

public class ColumnNames implements Callable {

	@Override
	public ArrayList onCall(MuleEventContext eventContext) throws Exception {
		
		MuleMessage msg = eventContext.getMessage();
		Map M1 = (Map)msg.getPayload();

		ArrayList<String> A1 = (ArrayList<String>)M1.get("csvFields");
		
		return A1;
		
	}
}
