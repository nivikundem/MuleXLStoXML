package uk.co.nivi.Bean;

import java.util.LinkedList;

public class GenericMapRoutinesResult {
	
	private String returnValue;
	private LinkedList<String> errorArray;

	public String getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}
	public LinkedList<String> getErrorArray() {
		return errorArray;
	}
	public void setErrorArray(LinkedList<String> errorArray) {
		this.errorArray = errorArray;
	}


}
