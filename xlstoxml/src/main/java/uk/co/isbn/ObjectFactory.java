//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.25 at 07:22:55 AM GMT 
//


package uk.co.isbn;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.co.isbn package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.co.isbn
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MLMessageList }
     * 
     */
    public MLMessageList createMLMessageList() {
        return new MLMessageList();
    }

    /**
     * Create an instance of {@link MLMessageList.MMessage }
     * 
     */
    public MLMessageList.MMessage createMLMessageListMMessage() {
        return new MLMessageList.MMessage();
    }

    /**
     * Create an instance of {@link MLMessageList.MMessage.CDClientDetails }
     * 
     */
    public MLMessageList.MMessage.CDClientDetails createMLMessageListMMessageCDClientDetails() {
        return new MLMessageList.MMessage.CDClientDetails();
    }

}
