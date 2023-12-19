package trust.databaseConnector.concreteImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.Map.Entry;

import trust.databaseConnector.databaseAbstraction.IQueryManager;
import trust.infoManager.utils.ARValue;
import trust.infoManager.utils.RValue;
import trust.infoManager.utils.TDValue;

public class MySQLQueryManager implements IQueryManager {

	@Override
	public String getQueryToGetTDValues(List<String> from, List<String> to) {
		// If the from and to lists are empty we return all available services
		if (from.isEmpty() && to.isEmpty())
			return "SELECT * FROM rec2sp;";
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM rec2sp WHERE ");
		if (!from.isEmpty()) {
			query.append("(recommender IN (");

			for (String recommender : from) {
				query.append("'" + recommender + "',");
			}
			query.deleteCharAt(query.length() - 1);
			
			if (!to.isEmpty())
				query.append(")) AND ");
		}
		
		if (!to.isEmpty()) {
			query.append("(recommendee IN (");

			for (String recommendee : to) {
				query.append("'" + recommendee + "',");
			}
			query.deleteCharAt(query.length() - 1);
		}

		query.append("));");
		return query.toString();
	}

	@Override
	public String getQueryToGetRValues(List<String> from, List<String> to) {
		// If the from list is empty we return all available services
		if (from.isEmpty())
			return "SELECT * FROM rec2rec;";
				
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM rec2rec WHERE (recommender IN (");

		for (String recommender : from) {
			query.append("'" + recommender + "',");
		}
		query.deleteCharAt(query.length() - 1);

		if (!to.isEmpty()) {
			query.append(")) AND (recommendee IN (");

			for (String recommendee : to) {
				query.append("'" + recommendee + "',");
			}
			query.deleteCharAt(query.length() - 1);
		}

		query.append("));");
		return query.toString();
	}

	@Override
	public String getQueryToGetARValues(List<String> recommenders) {
		if (recommenders.isEmpty())
			return "SELECT * FROM ar";

		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM ar WHERE recommender IN (");

		for (String recommender : recommenders) {
			query.append("'" + recommender + "',");
		}
		query.deleteCharAt(query.length() - 1);

		query.append(");");
		return query.toString();
	}

	@Override
	public List<String> getQueriesToPopulate(Map<String, Map<String, TDValue>> tdRecs,
			Map<String, Map<String, RValue>> rRecs, Map<String, ARValue> arValues) {
		
		List<String> queries = new ArrayList<String>();
		
		StringJoiner tdQuery = new StringJoiner(",", "INSERT INTO rec2sp(recommender, recommendee, trust, distrust, count) VALUES ", ";");
		StringJoiner rQuery = new StringJoiner(",", "INSERT INTO rec2rec(recommender, recommendee, value, count) VALUES ", ";");
		StringJoiner arQuery = new StringJoiner(",", "INSERT INTO ar(recommender, aggregate, count) VALUES ", ";");
		
		for (Entry<String, Map<String, TDValue>> recommenderEntry: tdRecs.entrySet()) {
			String recommender = recommenderEntry.getKey();
			for (Entry<String, TDValue> recommendeeEntry: recommenderEntry.getValue().entrySet()) {
				String recommendee = recommendeeEntry.getKey();
				TDValue recommendation = recommendeeEntry.getValue();
				
				tdQuery.add(String.format("('%s', '%s', %f, %f, %d)", recommender, recommendee, recommendation.getTrust(), recommendation.getDistrust(),
							recommendation.getCount()));
			}
		}
		
		queries.add(tdQuery.toString());
		
		for (Entry<String, Map<String, RValue>> recommenderEntry: rRecs.entrySet()) {
			String recommender = recommenderEntry.getKey();
			for (Entry<String, RValue> recommendeeEntry: recommenderEntry.getValue().entrySet()) {
				String recommendee = recommendeeEntry.getKey();
				RValue recommendation = recommendeeEntry.getValue();
				
				rQuery.add(String.format("('%s', '%s', %f, %d)",
						recommender, recommendee, recommendation.getRecommendation(), recommendation.getCount()));
			}
		}
		
		queries.add(rQuery.toString());
		
		for (Entry<String, ARValue> entry: arValues.entrySet()) {
			String recommender = entry.getKey();
			ARValue arValue = entry.getValue();
			
			arQuery.add(String.format("('%s', %f, %d)",
					recommender, arValue.getAggrRecommendation(), arValue.getNumOfRecs()));
		}

		queries.add(arQuery.toString());
		
		return queries;
	}

}
