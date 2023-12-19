package trust.infoManager.utils;

public class ARValue {
	private double aggrRecommendation;
	private int numOfRecs;
	
	public ARValue(double aggrRecommendation, int numOfRecs) {
		super();
		this.aggrRecommendation = aggrRecommendation;
		this.numOfRecs = numOfRecs;
	}
	
	public double getAggrRecommendation() {
		return aggrRecommendation;
	}
	
	public int getNumOfRecs() {
		return numOfRecs;
	}
	
	public double getAR() {
		if (numOfRecs == 0)
			return aggrRecommendation;
		else
			return aggrRecommendation/numOfRecs;
	}

}
