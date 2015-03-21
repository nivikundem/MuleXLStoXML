package uk.co.nivi.component;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;

import org.apache.poi.ss.usermodel.*;

public class XlsToCsv implements Callable {

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
 	   	MuleMessage msg = eventContext.getMessage();
		FileInputStream Fis = (FileInputStream)msg.getPayload();
		InputStream inStreamObject = ((InputStream) Fis);
		
		Workbook localWorkbook = null;
		DataFormatter formatter = null;
		Sheet localSheet = null;
		Row localRow = null;
		Cell localCell = null;
		StringBuffer buffer = null;

		byte[] Bout = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream( );
		String encoding = "UTF8";
	    OutputStreamWriter osw = new OutputStreamWriter(os, encoding);
	    BufferedWriter bw = new BufferedWriter(osw);
		
		try
	     {
		   localWorkbook = WorkbookFactory.create(inStreamObject);
	
		   formatter = new DataFormatter(true);		   

		   int sheet = 0;
    	   localSheet = localWorkbook.getSheetAt(sheet);
    	   int rowcount = localSheet.getPhysicalNumberOfRows();
    	   for(int j = 0; j < rowcount; j++) {
    		   localRow = localSheet.getRow(j);
    		   buffer = new StringBuffer();
    		   int lastCellNum = localRow.getLastCellNum();
    		   for(int i = 0; i < lastCellNum; i++)
    		   {
    			   localCell = localRow.getCell(i);
                   if(localCell != null) 
                   {
                       buffer.append(escapeEmbeddedCharacters(formatter.formatCellValue(localCell).toString()));
                   }
                   if(i != (lastCellNum-1)) 
                   {
                	   buffer.append(",");
                   }
               }
    		   bw.write(buffer.toString().trim());
    		   if (j != (rowcount-1))
    		   {
    			   bw.newLine();
    		   }
    	   }
		   
	     }
	     catch (UnsupportedEncodingException e)
	     {
	       System.err.println(e.toString());
	     }
	     catch (IOException e)
	     {
	       System.err.println(e.toString());
	     }
	     catch (Exception e)
	     {
	       System.err.println(e.toString());
	     }
		finally 
		{
		   if (bw != null)
		   {
		 	   bw.flush();
		       bw.close();
		   }
		}
		
		Bout = os.toByteArray();
		inStreamObject.close();
		Fis.close();

		return Bout;
	}

	private String escapeEmbeddedCharacters(String field)
	{
        StringBuffer buffer = null;

// If there are any speech marks (") in the field each occurrence
// must be escaped with another set of speech marks and then the
// entire field should be enclosed within another set of speech marks.
// Thus, "Yes" he said would become """Yes"" he said"
        if(field.contains("\"")) 
        {
            buffer = new StringBuffer(field.replaceAll("\"", "\\\"\\\""));
            buffer.insert(0, "\"");
            buffer.append("\"");
        }
        else 
        {
// If the field contains either embedded separator or EOL characters, then 
// escape the whole field by surrounding it with speech marks.
            buffer = new StringBuffer(field);
            if((buffer.indexOf(",")) > -1 || (buffer.indexOf("\n")) > -1) {
                buffer.insert(0, "\"");
                buffer.append("\"");
            }
        }
        return(buffer.toString().trim());
    }
}

