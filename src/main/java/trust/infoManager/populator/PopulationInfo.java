package trust.infoManager.populator;

public class PopulationInfo {
	private double maxTrust = 1.0;
	private double minTrust = 0.6;
	private double maxDistrust = 0.4;
	private double minDistrust = 0.0;
	
	private double maxRecommendation = 1.0;
	private double minRecommendation = 0.6;

	private double trustOffset = 0.1;
	
	private int numOfRecs = 1000;
	private int numOfSPs = 200;
	private double percentageOfConn2Recs = 0.1;
	private double percentageOfConn2SPs = 0.1;
	
	public PopulationInfo(double maxTrust, double minTrust, double maxDistrust, double minDistrust, 
						  double maxRecommendation, double minRecommendation, double trustOffset, 
						  int numOfRecs, int numOfSPs, double percentageOfConn2Recs, double percentageOfConn2SPs) {
		super();
		this.maxTrust = maxTrust;
		this.minTrust = minTrust;
		this.maxDistrust = maxDistrust;
		this.minDistrust = minDistrust;
		this.maxRecommendation = maxRecommendation;
		this.minRecommendation = minRecommendation;
		this.trustOffset = trustOffset;
		this.numOfRecs = numOfRecs;
		this.numOfSPs = numOfSPs;
		this.percentageOfConn2Recs = percentageOfConn2Recs;
		this.percentageOfConn2SPs = percentageOfConn2SPs;
	}

	public double getMaxTrust() {
		return maxTrust;
	}

	public double getMinTrust() {
		return minTrust;
	}

	public double getMaxDistrust() {
		return maxDistrust;
	}

	public double getMinDistrust() {
		return minDistrust;
	}

	public double getMaxRecommendation() {
		return maxRecommendation;
	}

	public double getMinRecommendation() {
		return minRecommendation;
	}

	public double getTrustOffset() {
		return trustOffset;
	}

	public int getNumOfRecs() {
		return numOfRecs;
	}

	public int getNumOfSPs() {
		return numOfSPs;
	}

	public double getPercentageOfConn2Recs() {
		return percentageOfConn2Recs;
	}

	public double getPercentageOfConn2SPs() {
		return percentageOfConn2SPs;
	}

}
