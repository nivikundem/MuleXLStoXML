package uk.co.nivi.component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transport.PropertyScope;
import uk.co.csvtopojo.MessageList;
import uk.co.csvtopojo.MessageList.Message;
import uk.co.csvtopojo.MessageList.Message.ClientDetails;
import uk.co.nivi.Bean.GenericMapRoutinesResult;
import uk.co.nivi.util.GenericRoutines;



public class BuildOutputMessage implements Callable {

	String returnStatus = "ok";
	String returnStatusText = "";
	ByteArrayOutputStream Os1 = null;
	
	JAXBContext jaxbcontext = null;
	JAXBContext jaxbcontextisbn = null;
	
	
	uk.co.isbn.MLMessageList mlMessageList = null;
	MessageList ml = null;	
	
	
	Message mh = null;
	
	ClientDetails clientDetails = null;

	
	ArrayList<String> sessionVarsName = new ArrayList<String>();
	ArrayList<String> sessionVars = new ArrayList<String>();
	

	
	Integer idx1 = null;
	Integer idx2 = null;
	
	String dbTarget 			= null;
	String dbSource 			= null;
	String dbFormatting 		= null;
	String dbArraySet	 		= null;
	String dbParentArraySets	= null;
	String dbCreateArraySet		= null;

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		
		try
		{
			jaxbcontext = (JAXBContext)eventContext.getMessage().getProperty("jaxbcontextmessage", PropertyScope.SESSION);
			jaxbcontextisbn = (JAXBContext)eventContext.getMessage().getProperty("jaxbcontextISBN", PropertyScope.SESSION);

			MuleMessage msg = eventContext.getMessage();
	 	   	byte[] Bain = msg.getPayloadAsBytes();
	 	   	InputStream is = null;
	 	   	is = new ByteArrayInputStream(Bain);

			Unmarshaller unmarshaller = jaxbcontextisbn.createUnmarshaller();
			mlMessageList = (uk.co.isbn.MLMessageList)unmarshaller.unmarshal(is);
			
				
			LinkedList<Map> s2t = (LinkedList<Map>)eventContext.getMessage().getProperty("dbsourcetotarget", PropertyScope.SESSION);
		//	System.out.println("s2t ::: "+s2t.size());
			Map M1 = s2t.get(0);
			Os1 = new ByteArrayOutputStream( );
			
			ml = new MessageList();				
			mh = new Message();
			
			clientDetails = new ClientDetails();				
			mh.setClientDetails(clientDetails);
			
			ml.setMessage(mh);
			
			for(int j=0; j < s2t.size(); j++)
			{
				M1 = s2t.get(j);			
				String target = (String)M1.get("target");
				String source = (String)M1.get("source");
				SourceToTarget(target,source,null);
			}					
			
			try
			{						
				   Marshaller jaxbMarshaller = jaxbcontext.createMarshaller();
				   jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				   jaxbMarshaller.marshal(ml,Os1);

			}
			catch (JAXBException e) 
			{
				e.printStackTrace();
				returnStatus = "Failed";
				returnStatusText = "Build Final xml - Failed JAXB formatting";
			}
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			returnStatus = "Failed";
			returnStatusText = "Build Final xml - General failure";
		}
		
		eventContext.getMessage().setSessionProperty("returnstatus", returnStatus);
		eventContext.getMessage().setSessionProperty("returnstatustext", returnStatusText);
		return Os1;
		
	
	}
	
	
	private void SourceToTarget(String Target, String Source, String Format)
	{		
	
		String CatSource = SourceStringDecode(Source);     
        if (!GenericRoutines.EmptyString(CatSource))
        {
        	SetTarget(Target,CatSource);
        }
		
    }
	
	private String SourceStringDecode(String Source)
	{
		
	
		String s1;
		String s2;
		String s3;
		String CatSource = Source;
		
		int varpos = CatSource.indexOf(GenericRoutines.SourceStartChar);
		
		
		while (varpos != -1)
		{
			
		
			s1 = "";
			s2 = "";
			s3 = "";
			int endvarpos = CatSource.indexOf(GenericRoutines.SourceEndChar, varpos);
			String torepl = CatSource.substring(varpos+GenericRoutines.SourceStartChar.length(),endvarpos);
			
			
			if (varpos > 0) 
			{
				
				s1 = CatSource.substring(0,varpos);
			}
			if (endvarpos < (CatSource.length()-GenericRoutines.SourceEndChar.length())) 
			{
				
				s3 = CatSource.substring(endvarpos+(GenericRoutines.SourceEndChar.length()));
			}
			if (!GenericRoutines.EmptyString(torepl))
			{
				
				s2 = GetSource(torepl);
				if (GenericRoutines.EmptyString(s2)) 
				{ 					
					s2 = "";
				}
				else
				{					
					s2 = GenericRoutines.SpecialCharsReplace(s2);
				}
			}
			
		   CatSource = s1 + s2 + s3;			
			varpos = CatSource.indexOf(GenericRoutines.SourceStartChar);
			
		}
		if (!GenericRoutines.EmptyString(CatSource))
		{
			
			CatSource = CatSource.trim();
			GenericMapRoutinesResult R1 = GenericRoutines.HandleMapRoutines(CatSource);
			CatSource = R1.getReturnValue();

		}

		if (!GenericRoutines.EmptyString(CatSource))
		{
			
			CatSource = GenericRoutines.SpecialCharsReInstate(CatSource);
		}
		
		if (GenericRoutines.EmptyString(CatSource))
		{ 
			
			CatSource = "";
		}
		
		
	
		return CatSource;
    }
	
	private String GetSource(String Source)
	{
		
        String Ret = null;
        try
        {
      
        	switch (Source)
            {               
         	case "ON-OrderNo": Ret = mlMessageList.getMMessage().getONOrderNo();break;		
			case "CN-ClientName": Ret = mlMessageList.getMMessage().getCNClientName();break;
			case "FN-FirstName": Ret = mlMessageList.getMMessage().getCDClientDetails().getFNFirstName();break;		
			case "LN-LastName": Ret = mlMessageList.getMMessage().getCDClientDetails().getLNLastName();break;
			case "A-Age": Ret = mlMessageList.getMMessage().getCDClientDetails().getAAge();break;
			case "C-County": Ret = mlMessageList.getMMessage().getCDClientDetails().getCCounty();break;        	
        	
          	default: Ret = null; break;
            }
        }
     
        catch (Exception e)
        {
        	Ret = null;
        }
        if (Ret != null) 
        { 
        	Ret = Ret.trim();
        }
        
         return(Ret);
    }
	

	private void SetTarget(String Target, String value)
	{
		switch (Target)
        {     
		case "OrderNo": mh.setOrderNo(value); break;
    	case "ClientName": mh.setClientName(value); break;
        case "FirstName": mh.getClientDetails().setFirstName(value); break;
    	case "LastName": mh.getClientDetails().setLastName(value); break;
    	case "Age": mh.getClientDetails().setAge(value); break;
    	case "County": mh.getClientDetails().setCounty(value); break;  

        }        
	}
	
}
