package org.bbn.sbd.training.perceptron;

public class FeatureWeight {
	
	int rawWeight;
	
	double avgWeightNumerator;
	
	double avgWeightDenominator;
	
	
	public FeatureWeight(int rawWeight, double avgWeightNumerator, double avgWeightDenominator)
	{
		this.rawWeight = rawWeight;
		this.avgWeightNumerator = avgWeightNumerator;
		this.avgWeightDenominator = avgWeightDenominator;
	}

	public int getRawWeight() {
		return rawWeight;
	}

	public void setRawWeight(int rawWeight) {
		this.rawWeight = rawWeight;
	}

	public double getAvgWeightNumerator() {
		return avgWeightNumerator;
	}

	public void setAvgWeightNumerator(double avgWeightNumerator) {
		this.avgWeightNumerator = avgWeightNumerator;
	}

	public double getAvgWeightDenominator() {
		return avgWeightDenominator;
	}

	public void setAvgWeightDenominator(double avgWeightDenominator) {
		this.avgWeightDenominator = avgWeightDenominator;
	}
	

}
