package uk.co.nivi.util;

import java.util.LinkedList;
import java.util.Map;
import uk.co.nivi.Bean.GenericMapOneRoutineResult;
import uk.co.nivi.Bean.GenericMapRoutinesResult;
import uk.co.nivi.component.ISBNConversion;

public class GenericRoutines {
	
	public static String SourceStartChar = "[";
	public static String SourceEndChar = "]";
	public static String MapRoutineStartChar = "{";
	public static String MapRoutineEndChar = "}";
	public static String MapRoutineParamStart = "(";
	public static String MapRoutineParamSeperator = "%";
	public static String MapRoutineParamEnd = ")";
	
	public static String SpecialCharsReplace(String Value)
	{
        String Ret = Value;
    	if (!GenericRoutines.EmptyString(Ret))
    	{
    		Ret = Ret.replace(SourceStartChar,String.valueOf((char) 0x01));
    		Ret = Ret.replace(SourceEndChar,String.valueOf((char) 0x02));
    		Ret = Ret.replace(MapRoutineStartChar,String.valueOf((char) 0x03));
    		Ret = Ret.replace(MapRoutineEndChar,String.valueOf((char) 0x04));
    		Ret = Ret.replace(MapRoutineParamStart,String.valueOf((char) 0x05));
    		Ret = Ret.replace(MapRoutineParamSeperator,String.valueOf((char) 0x06));
    		Ret = Ret.replace(MapRoutineParamEnd,String.valueOf((char) 0x07));
    	}
    	return(Ret);
    }
	
	public static String SpecialCharsReInstate(String Value)
	{
        String Ret = Value;
    	if (!GenericRoutines.EmptyString(Ret))
    	{
    		Ret = Ret.replace(String.valueOf((char) 0x01),SourceStartChar);
    		Ret = Ret.replace(String.valueOf((char) 0x02),SourceEndChar);
    		Ret = Ret.replace(String.valueOf((char) 0x03),MapRoutineStartChar);
    		Ret = Ret.replace(String.valueOf((char) 0x04),MapRoutineEndChar);
    		Ret = Ret.replace(String.valueOf((char) 0x05),MapRoutineParamStart);
    		Ret = Ret.replace(String.valueOf((char) 0x06),MapRoutineParamSeperator);
    		Ret = Ret.replace(String.valueOf((char) 0x07),MapRoutineParamEnd);
    	}

        return(Ret);
    }
	
	public static GenericMapRoutinesResult HandleMapRoutines(String Value)
	{
		GenericMapRoutinesResult Result1 = new GenericMapRoutinesResult();
		Result1.setErrorArray(new LinkedList<String>());
		
		String Ret = Value;
		int varpos = Ret.indexOf(GenericRoutines.MapRoutineStartChar);
				
		while (varpos != -1)
		{
			int spos = Ret.indexOf(GenericRoutines.MapRoutineStartChar, varpos+1);
			int endvarpos = Ret.indexOf(GenericRoutines.MapRoutineEndChar, varpos+1);
			while ((spos != -1) & (spos < endvarpos))
			{
				varpos = spos;
				spos = Ret.indexOf(GenericRoutines.MapRoutineStartChar, varpos+1);
				endvarpos = Ret.indexOf(GenericRoutines.MapRoutineEndChar, varpos+1);
			}
				
			String s1 = "";
			String s2 = "";
			String s3 = "";
			String torepl = Ret.substring(varpos+GenericRoutines.MapRoutineStartChar.length(),endvarpos);
			if (varpos > 0) 
			{
				s1 = Ret.substring(0,varpos);
			}
			if (endvarpos < (Ret.length()-GenericRoutines.MapRoutineEndChar.length())) 
			{
				s3 = Ret.substring(endvarpos+(GenericRoutines.MapRoutineEndChar.length()));
			}
			if (!torepl.equals(""))
			{
				GenericMapOneRoutineResult Res = MapRoutine(torepl);
				s2 = Res.getReturnValue();
				String newError = Res.getErrorString();
				if (!newError.equals(""))
				{
					LinkedList<String> Result2 = Result1.getErrorArray();
					Result2.add(newError);
					Result1.setErrorArray(Result2);
				}
				if (GenericRoutines.EmptyString(s2))
				{ 
					s2 = "";
				}
			}
			Ret = s1 + s2 + s3;
						
			varpos = Ret.indexOf(GenericRoutines.MapRoutineStartChar);
		}
		
		if (GenericRoutines.EmptyString(Ret)) 
		{ 
			Ret = "";
		}
		Result1.setReturnValue(Ret);

        return(Result1);
    }
	
	public static GenericMapOneRoutineResult MapRoutine(String Value)
	{
		GenericMapOneRoutineResult R1 = new GenericMapOneRoutineResult();
		
        String Ret = "";
        String RetError = "";
        
        Integer parmStart = Value.indexOf(MapRoutineParamStart);
        String mapRoutine = Value.substring(0,parmStart);
        String AllParam = Value.substring(parmStart+1,Value.length()-1);
        
		switch (mapRoutine)
		{
		case "SPLIT": 
		{
	        String[] A1 = AllParam.split(MapRoutineParamSeperator);
			Integer occur = Integer.parseInt(A1[2]);
			String[] splitStr = A1[0].split(A1[1]);
			if (splitStr.length > occur)
			{
				Ret = splitStr[occur];
			}
			break;
		}
		
		case "DALOOKUP": 
		{
			
			
			String[] A1 = AllParam.split(MapRoutineParamSeperator);
			if (A1.length > 1)
			{
	    		if ((!GenericRoutines.EmptyString(A1[0])) & (!GenericRoutines.EmptyString(A1[1])))
	    		{
	    			int j = 0;
	    			boolean found = false;   	  	    			
	    			LinkedList<Map> dalookupsMap  = 	   ISBNConversion.dalookups;		    			
	    			while(j < dalookupsMap.size()&& !found)
	    			{
	    				Map M1 = dalookupsMap.get(j);
	    				String fromValue = (String)M1.get("code");
	    				String toValue = (String)M1.get("name");
	    				found = true;
	    				 Ret = toValue;
		    			  
		    			System.out.println("fromValue   "+fromValue+"  Ret "+Ret);	
	    			}
	    			
	    		}
			}
			break;
		}
		}

		if (!GenericRoutines.EmptyString(Ret))
		{ 
			Ret = Ret.trim(); 
		}

		R1.setReturnValue(Ret);
		R1.setErrorString(RetError);
		return(R1);
    }
	public static Boolean EmptyString(String Value)
	{
        if (Value == null)
        {
        	return(true);
        }
        else
        {
        	if (Value.trim().equals(""))
        	{
        		return(true);
        	}
        }
   		
        return(false);
    }
	
	
}
