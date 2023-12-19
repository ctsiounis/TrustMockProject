package trust.infoManager.populator;

import java.util.HashMap;
import java.util.Map;

import trust.databaseConnector.concreteImpl.MySQLDatabaseConnectorAPI;
import trust.databaseConnector.databaseAbstraction.DatabaseConnectorAPI;
import trust.infoManager.utils.ARValue;
import trust.infoManager.utils.RValue;
import trust.infoManager.utils.TDValue;

public class Populator {
	private DatabaseConnectorAPI dbConnAPI = new MySQLDatabaseConnectorAPI();

	private int getRandomNumber(int max, int exclude) {
		int n;
		do {
			double r = Math.random();
			n = (int) Math.round(r * max);
		} while (n == exclude);

		return n;
	}

	public void populate(PopulationInfo info) {
		double maxTrust = info.getMaxTrust();
		double minTrust = info.getMinTrust();
		double maxDistrust = info.getMaxDistrust();
		double minDistrust = info.getMinDistrust();
		
		double maxRecommendation = info.getMaxRecommendation();
		double minRecommendation = info.getMinRecommendation();

		double trustOffset = info.getTrustOffset();
		
		int numOfRecs = info.getNumOfRecs();
		int numOfSPs = info.getNumOfSPs();
		double percentageOfConn2Recs = info.getPercentageOfConn2Recs();
		double percentageOfConn2SPs = info.getPercentageOfConn2SPs();

		Map<String, TDValue> spValues = new HashMap<String, TDValue>();
		Map<String, ARValue> arValues = new HashMap<String, ARValue>();
		Map<String, Map<String, RValue>> rValues = new HashMap<String, Map<String, RValue>>();
		Map<String, Map<String, TDValue>> tdValues = new HashMap<String, Map<String, TDValue>>();

		
		int numOfConn2Recs = (int) Math.round(percentageOfConn2Recs * numOfRecs);
		int numOfConn2SPs = (int) Math.round(percentageOfConn2SPs * numOfSPs);

		for (int i = 0; i < numOfSPs; i++) {
			String sp = "SP" + i;
			double trust = Math.max(0, Math.min(1, (maxTrust - minTrust) * Math.random() + minTrust));
			double distrust = Math.max(0, Math.min(1, (Math.min(maxDistrust, 1 - trust) - minDistrust) * Math.random() + minDistrust));
			//double distrust = (0.9 + (0.2 * Math.random())) * (1.0 - trust);
			TDValue spValue = new TDValue(trust, distrust, 1);
			spValues.put(sp, spValue);
		}

		for (int i = 0; i < numOfRecs; i++) {
			String recommender = "R" + i;

			// Connect to specific number of random recommenders
			Map<String, RValue> recommenderMap = new HashMap<String, RValue>();
			for (int j = 0; j < numOfConn2Recs; j++) {
				String recommendee = "R" + getRandomNumber(numOfRecs - 1, i);
				double recommendation = Math.max(0, Math.min(1, (maxRecommendation - minRecommendation) * Math.random() + minRecommendation));
				
				// Add direct recommendation
				recommenderMap.put(recommendee, new RValue(recommendation, 1));

				// Update recommendee's AR
				ARValue previousARValue = arValues.getOrDefault(recommendee, new ARValue(0.0, 0));
				arValues.put(recommendee, new ARValue(previousARValue.getAggrRecommendation() + (recommendation * 1.0),
						previousARValue.getNumOfRecs() + 1));
			}
			rValues.put(recommender, recommenderMap);

			// Connect to specific number of random service providers
			Map<String, TDValue> spMap = new HashMap<String, TDValue>();
			for (int j = 0; j < numOfConn2SPs; j++) {
				String sp;
				do {
					sp = "SP" + getRandomNumber(numOfSPs - 1, numOfSPs);
				} while (spMap.containsKey(sp));
				
				double trust = Math.max(0, Math.min(1, spValues.get(sp).getTrust() * (1 + ((2 * trustOffset * Math.random()) - trustOffset))));
				double distrust = Math.max(0, Math.min(1, spValues.get(sp).getDistrust()
						* (1 + ((2 * trustOffset * Math.random()) - trustOffset))));

				spMap.put(sp, new TDValue(trust, distrust, 1));

			}
			tdValues.put(recommender, spMap);
		}
		
		// Check that all recommenders have an AR value
		for (int i = 0; i < numOfRecs; i++) {
			String recommender = "R" + i;
			if (arValues.get(recommender) == null)
				arValues.put(recommender, new ARValue(0.5, 0));	
		}

		dbConnAPI.populateDB(tdValues, rValues, arValues);
	}

}
