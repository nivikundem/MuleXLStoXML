package uk.co.nivi.component;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transport.PropertyScope;

import uk.co.isbn.MLMessageList;
import uk.co.isbn.MLMessageList.MMessage;
import uk.co.isbn.MLMessageList.MMessage.CDClientDetails;
import uk.co.nivi.Bean.GenericMapRoutinesResult;
import uk.co.nivi.util.GenericRoutines;


//import uk.co.equity.util.GenericRoutines;

public class ISBNConversion implements Callable {

	String retString					= "";

	MLMessageList MC 				= null;
	Integer inum1						= -1;
	Integer inum2						= -1;
	String prefix						= "";
	JAXBContext jc 						= null;

	ArrayList<Map> row1Cols 			= null;
	ArrayList<String> thisRow			= null;
	public static LinkedList<Map> dalookups 	= null;
	//public static  Map dalookups = null;
	LinkedList<String> daerrors 		= new LinkedList<String>();

	ArrayList<String> sessionVarsName = new ArrayList<String>();
	ArrayList<String> sessionVars = new ArrayList<String>();
	
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		
		MuleMessage msg = eventContext.getMessage();
		Map M1 = (Map)msg.getPayload();

		sessionVarsName.add("csvRowNumber");
		sessionVars.add((String) M1.get("RecordNumber"));

		thisRow = (ArrayList<String>)M1.get("csvFields");
		row1Cols = (ArrayList<Map>)eventContext.getMessage().getProperty("row1columnnames", PropertyScope.SESSION);		
		
		 LinkedList<Map> s2t = (LinkedList<Map>)eventContext.getMessage().getProperty("dacolumnmappings", PropertyScope.SESSION);
		
		 dalookups = (LinkedList<Map>)eventContext.getMessage().getProperty("dalookups", PropertyScope.SESSION);
		
		jc = (JAXBContext)eventContext.getMessage().getProperty("jaxbcontextISBN", PropertyScope.SESSION);

		CreateDefaultMessageCollection();
	
		for(int j=0; j < s2t.size(); j++)
		{
			M1 = s2t.get(j);
			String columnName = (String)M1.get("ColumnName");
			String mapToField = (String)M1.get("MapToField");
		    SourceToTarget(columnName,mapToField);
		}
		
	
		s2t=null;	

		Marshaller marshaller = jc.createMarshaller();
		ByteArrayOutputStream Os1 = new ByteArrayOutputStream( );
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(MC,Os1);

		eventContext.getMessage().setSessionProperty("routinereturn", retString);
		if (daerrors != null)
		{
			eventContext.getMessage().setSessionProperty("daerrors", daerrors);
		}
		
		return Os1;
		
	  
		
	}
	
	
	

	private void CreateDefaultMessageCollection()
	{		

		MC = new MLMessageList();		
		MC.setMMessage(new MMessage());		
		MC.getMMessage().setCDClientDetails(new CDClientDetails());
		
    }
	
	
	
	
	private void SourceToTarget(String Source, String Target)
	{
		
		String CatSource = SourceStringDecode(Source);    
		SetTarget(Target,CatSource);		
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
			if (R1.getErrorArray().size()>0)
			{
				daerrors.addAll(R1.getErrorArray());
			}
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
        String Ret = "";
        
        
            Integer iPos = row1Cols.indexOf(Source);
    		if (iPos != -1)
    		{
    			Ret = (String)thisRow.get(iPos);
    		}
    		else
    		{
    			retString = "Failed to find column " +  Source;
    		}
            if (Ret != null) 
            { 
            	Ret = Ret.trim(); 
            }
        
        return(Ret);
   
	}
	
	
		
	
	
	private void SetTarget(String Target, String value)
	{				
		try
		{
			switch (Target)
			{
			
			case "ON-OrderNo": MC.getMMessage().setONOrderNo(value);break;		
			case "CN-ClientName": MC.getMMessage().setCNClientName(value);break;
			case "FN-FirstName": MC.getMMessage().getCDClientDetails().setFNFirstName(value);break;		
			case "LN-LastName":{
				String str = value;;
				String[] splited = str.split("\\s+");
				MC.getMMessage().getCDClientDetails().setLNLastName(splited[1]);
				
				break;
			}
			case "A-Age": MC.getMMessage().getCDClientDetails().setAAge(value);break;
			case "C-County": MC.getMMessage().getCDClientDetails().setCCounty(value);break;
    		default: retString = "Target field not found: " +  Target;break;

        	}
		}
		catch  (Exception e)
		{			
		
			System.out.println("Column to ISBN assignment failed for field: "+Target);
			System.out.println("Cause  "+e.getCause()+" Message: "+e.getLocalizedMessage());
		}
	}
		
	
	
}
