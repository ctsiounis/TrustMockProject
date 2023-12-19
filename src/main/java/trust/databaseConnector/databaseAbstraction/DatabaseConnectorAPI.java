package trust.databaseConnector.databaseAbstraction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trust.infoManager.utils.ARValue;
import trust.infoManager.utils.RValue;
import trust.infoManager.utils.TDValue;

abstract public class DatabaseConnectorAPI {
	protected IDatabaseMapper dbMapper;
	protected IQueryManager queryManager;
	
	// TODO Think about result if update fails

	public Map<String, Map<String, TDValue>> getTDValues(Map<String, List<String>> combinations) {
		String query = queryManager.getQueryToGetTDValues(combinations.get("from"), combinations.get("to"));
		Map<String, Map<String, TDValue>> result = dbMapper.executeTDQuery(query);
		return result;
	}
	
	public Map<String, Map<String, RValue>> getRValues(Map<String, List<String>> combinations) {
		String query = queryManager.getQueryToGetRValues(combinations.get("from"), combinations.get("to"));
		Map<String, Map<String, RValue>> result = dbMapper.executeRQuery(query);
		return result;
	}
	
	public Map<String, ARValue> getARValues(List<String> recommenders) {
		String query = queryManager.getQueryToGetARValues(recommenders);
		Map<String, ARValue> result = dbMapper.executeARQuery(query);
		return result;
	}
	
	public boolean populateDB(Map<String, Map<String, TDValue>> tdRecs,
			Map<String, Map<String, RValue>> rRecs, Map<String, ARValue> arValues) {
		
		List<String> queries = queryManager.getQueriesToPopulate(tdRecs, rRecs, arValues);
		
		return dbMapper.populateDatabase(queries);
	}
    
}