package trust.infoManager.utils;

public class RValue {
	private double recommendation;
	private int count;
	
	public RValue(double recommendation, int count) {
		super();
		this.recommendation = recommendation;
		this.count = count;
	}
	
	public double getRecommendation() {
		return recommendation;
	}
	
	public int getCount() {
		return count;
	}

}
