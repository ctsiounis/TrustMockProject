package trust.databaseConnector.concreteImpl;

import trust.databaseConnector.databaseAbstraction.DatabaseConnectorAPI;

public class MySQLDatabaseConnectorAPI extends DatabaseConnectorAPI {
	
	public MySQLDatabaseConnectorAPI() {
		this.queryManager = new MySQLQueryManager();
		this.dbMapper = new MySQLDatabaseMapper();
	}

}
