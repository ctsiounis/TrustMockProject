package trust.infoManager;

import java.util.List;
import java.util.Map;

import trust.databaseConnector.concreteImpl.MySQLDatabaseConnectorAPI;
import trust.databaseConnector.databaseAbstraction.DatabaseConnectorAPI;
import trust.infoManager.utils.ARValue;
import trust.infoManager.utils.RValue;
import trust.infoManager.utils.TDValue;

public class DataProvisioner {
	private DatabaseConnectorAPI dbConnAPI = new MySQLDatabaseConnectorAPI();

	public Map<String, ARValue> getAR(List<String> recommenders) {
		return dbConnAPI.getARValues(recommenders);
	}

	public Map<String, Map<String, RValue>> getR(Map<String, List<String>> combinations) {
		return dbConnAPI.getRValues(combinations);
	}

	public Map<String, Map<String, TDValue>> getTD(Map<String, List<String>> combinations) {
		return dbConnAPI.getTDValues(combinations);
	}
}