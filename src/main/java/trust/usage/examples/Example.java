package trust.usage.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import trust.infoManager.DataProvisioner;
import trust.infoManager.populator.PopulationInfo;
import trust.infoManager.populator.Populator;
import trust.infoManager.utils.RValue;
import trust.infoManager.utils.TDValue;

public class Example {
	
	private static void printTDValues(Map<String, Map<String, TDValue>> tdValues) {
		int count = 0;
		for (Entry<String, Map<String, TDValue>> externalEntry : tdValues.entrySet()) {
			String recommender = externalEntry.getKey();
			for (Entry<String, TDValue> internalEntry : externalEntry.getValue().entrySet()) {
				String recommendee = internalEntry.getKey();
				TDValue tdValue = internalEntry.getValue();
				
				System.out.println(recommender + "=>" + recommendee + ":"
						+ tdValue.getTrust() + "," + tdValue.getDistrust());
				
				count++;
			}
		}
		System.out.println("TD values found: " + count);
	}
	
	private static void printRValues(Map<String, Map<String, RValue>> rValues) {
		int count = 0;
		for (Entry<String, Map<String, RValue>> externalEntry : rValues.entrySet()) {
			String recommender = externalEntry.getKey();
			for (Entry<String, RValue> internalEntry : externalEntry.getValue().entrySet()) {
				String recommendee = internalEntry.getKey();
				RValue rValue = internalEntry.getValue();
				
				System.out.println(recommender + "=>" + recommendee + ":"
						+ rValue.getRecommendation());
				
				count++;
			}
		}
		System.out.println("R values found: " + count);
	}

	public static void main(String[] args) {
		Populator populator = new Populator();
		
		// Set the variables needed for the random population.
		double maxTrust = 1.0;
		double minTrust = 0.7;
		double maxDistrust = 0.3;
		double minDistrust = 0.0;
		double maxRecommendation = 1.0;
		double minRecommendation = 0.6;
		double trustOffset = 0.025;
		int numOfRecs = 1000;
		int numOfSPs = 100;
		double percentageOfConn2Recs = 0.05;
		double percentageOfConn2SPs = 0.05;
		PopulationInfo info = new PopulationInfo(maxTrust, 
												 minTrust, 
												 maxDistrust, 
												 minDistrust, 
												 maxRecommendation, 
												 minRecommendation, 
												 trustOffset, 
												 numOfRecs, 
												 numOfSPs, 
												 percentageOfConn2Recs, 
												 percentageOfConn2SPs);
		// Call the populator to perform it.
		populator.populate(info);
		
		// Get info from DB.
		DataProvisioner dataProvisioner = new DataProvisioner();
		Map<String, List<String>> combinations = new HashMap<>();
		
		// Set list of recommenders (empty means everyone)
		List<String> from = new ArrayList<>();
		combinations.put("from", from);
		
		// Set list of recommendees (empty means everyone)
		List<String> to = new ArrayList<>();
		combinations.put("to", to);
		
		// Method gets all pairs where recommender is part of "from" list
		// and recommendee is part of "to" list.
		Map<String, Map<String, RValue>> rValues = dataProvisioner.getR(combinations);
		// Print results
		printRValues(rValues);
		
		// Narrow down recommenders to get.
		from.add("R12");
		from.add("R35");
		from.add("R92");
		from.add("R128");
		Map<String, Map<String, TDValue>> tdValues = dataProvisioner.getTD(combinations);
		// Print results
		printTDValues(tdValues);

	}

}
