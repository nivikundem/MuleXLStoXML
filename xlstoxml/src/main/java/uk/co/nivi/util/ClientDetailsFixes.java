package uk.co.nivi.util;

import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import uk.co.isbn.MLMessageList;
import uk.co.nivi.Bean.PrepareRoutineResult;
import uk.co.nivi.Bean.PrepareRoutineResultMsg;

public class ClientDetailsFixes {

	public static PrepareRoutineResult NameFix(MLMessageList MC) {
		PrepareRoutineResult R1 = new PrepareRoutineResult();
		R1.setMCUpdatedFlag(false);
		ArrayList<PrepareRoutineResultMsg> R1Details = new ArrayList<PrepareRoutineResultMsg>();

		String nameCode = StringUtils.defaultIfEmpty(
				GetMessageCollectionField(MC, "CN-ClientName", 0, 0), "");

		SetMessageCollectionField(MC, "CN-ClientName", 0, 0, nameCode);
		PrepareRoutineResultMsg PRRM = new PrepareRoutineResultMsg();
		PRRM.setMessage("CN-ClientName: " + nameCode
				+ " found - CN-ClientName set to " + nameCode);
		PRRM.setSeverity("I");
		R1Details.add(PRRM);
		R1.setMCUpdatedFlag(true);

		R1.setMC(MC);
		R1.setDetails(R1Details);

		return R1;
	}

	private static String GetMessageCollectionField(MLMessageList MC,
			String Source, Integer idx1, Integer idx2) {
		String Ret = null;
		try {
			switch (Source) {
			case "CN-ClientName":
				Ret = MC.getMMessage().getCNClientName();
				break;
			default:
				Ret = null;
				break;
			}
		} catch (Exception e) {
			Ret = null;
		}

		return (Ret);
	}

	private static void SetMessageCollectionField(MLMessageList MC,
			String Target, Integer idx1, Integer idx2, String value) {
		try {
			if (value == null) {
				switch (Target) {

				case "CN-ClientName":
					MC.getMMessage().setCNClientName(null);
					break;

				default:
					break;
				}
			} else {
				switch (Target) {

				case "CN-ClientName":
					MC.getMMessage().setCNClientName("NK is Nivi Kundem");
					break;

				default:
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Unable to set target field " + Target
					+ " in Fix routines");
		}

		// System.out.println("SetMessageCollectionField  end");
		return;
	}

}
