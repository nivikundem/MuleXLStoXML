package uk.co.nivi.Bean;

import java.util.ArrayList;



import uk.co.isbn.MLMessageList;

public class PrepareRoutineResult {
	
	private MLMessageList MC;
	private ArrayList<PrepareRoutineResultMsg> Details;
	private Boolean MCUpdatedFlag;
	public MLMessageList getMC() {
		return MC;
	}
	public void setMC(MLMessageList mC) {
		MC = mC;
	}
	public ArrayList<PrepareRoutineResultMsg> getDetails() {
		return Details;
	}
	public void setDetails(ArrayList<PrepareRoutineResultMsg> details) {
		Details = details;
	}
	public Boolean getMCUpdatedFlag() {
		return MCUpdatedFlag;
	}
	public void setMCUpdatedFlag(Boolean mCUpdatedFlag) {
		MCUpdatedFlag = mCUpdatedFlag;
	}


}
