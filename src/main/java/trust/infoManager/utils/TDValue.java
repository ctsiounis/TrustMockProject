package trust.infoManager.utils;

public class TDValue {
	private double trust;
	private double distrust;
	private int count;
	
	public TDValue(double trust, double distrust, int count) {
		super();
		this.trust = trust;
		this.distrust = distrust;
		this.count = count;
	}

	public double getTrust() {
		return trust;
	}

	public double getDistrust() {
		return distrust;
	}

	public int getCount() {
		return count;
	}
	
}
