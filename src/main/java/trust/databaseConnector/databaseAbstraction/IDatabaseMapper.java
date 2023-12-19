package trust.databaseConnector.databaseAbstraction;

import java.util.List;
import java.util.Map;

import trust.infoManager.utils.ARValue;
import trust.infoManager.utils.RValue;
import trust.infoManager.utils.TDValue;

public interface IDatabaseMapper {
	Map<String, Map<String, TDValue>> executeTDQuery(String query);

	Map<String, Map<String, RValue>> executeRQuery(String query);
	
	Map<String, ARValue> executeARQuery(String query);
	
	boolean executeUpdate(List<String> queries);
	
	public boolean populateDatabase(List<String> queries);

}
