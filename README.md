#Mule Demo for using - EXCEL_To_XML Project
#Converting XLS to XML using smooks and MULE ESB

Mule Demo for using - XLS-to-XML Project
--------------
Demo for converting XLS message to XML using Smooks and MULE ESB


This project 
---------
1.	Reads excel sheet from the input folder and converts into the CSV[EXCEL - TO -CSV]
2.	Reads the CSV file converts to java object using smooks [CSV - TO- - Java]
3.	Reads the java objects convert into the xml using jaxb-xml-to-object-transformer
4.	Emails the XML


Mule components
---------
1.	File connectors
2.	Context property place folders
3. 	FileInbound
4.	Message Properties transformer
5.	Java Component - to convert xls to CSV
6.	FileOutbound
7.	Enricher
8.	Java Component - to convert CSV to Xml
9.	Flow Ref
10.	Sub flow
11.	For-each
12.	Choice
13.	VM Inbound/Outbound
14.	Object-to-string-transformer
15.	jaxb-xml-to-object-transformer
16.	Database component
17.	SMTP connector



To Run
-------
Run as mule server or deploy into the mule sever as Mule Deployable Archive war and copy into mule-standalone the directory of the mule-standalone/apps


Technologies
---------
- J2E
- MySQL
- Mule Anypointstudio
- Smooks
