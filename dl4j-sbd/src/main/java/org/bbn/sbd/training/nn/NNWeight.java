package org.bbn.sbd.training.nn;

public class NNWeight<M, N> {
	
	int layerIndex;
	
	M startNodeIndex;
	
	N endNodeIndex;
	
	double weight;

	
	// constructor
	public NNWeight(int layerIndex, M startNodeIndex, N endNodeIndex)
	{
		this.layerIndex = layerIndex;
		this.startNodeIndex = startNodeIndex;
		this.endNodeIndex = endNodeIndex;
	}
	
	
	// getters and setters
	public int getLayerIndex() {
		return layerIndex;
	}

	public void setLayerIndex(int layerIndex) {
		this.layerIndex = layerIndex;
	}

	public M getStartNodeIndex() {
		return startNodeIndex;
	}

	public void setStartNodeIndex(M startNodeIndex) {
		this.startNodeIndex = startNodeIndex;
	}

	public N getEndNodeIndex() {
		return endNodeIndex;
	}

	public void setEndNodeIndex(N endNodeIndex) {
		this.endNodeIndex = endNodeIndex;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	
	// other helper methods

}
