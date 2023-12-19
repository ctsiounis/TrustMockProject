package trust.databaseConnector.databaseAbstraction;

import java.util.List;
import java.util.Map;

import trust.infoManager.utils.ARValue;
import trust.infoManager.utils.RValue;
import trust.infoManager.utils.TDValue;

public interface IQueryManager {
	public String getQueryToGetTDValues(List<String> from, List<String> to);
	
	public String getQueryToGetRValues(List<String> from, List<String> to);
	
	public String getQueryToGetARValues(List<String> recommenders);
	
	public List<String> getQueriesToPopulate(Map<String, Map<String, TDValue>> tdRecs, Map<String, Map<String, RValue>> rRecs, Map<String, ARValue> arValues);

}
