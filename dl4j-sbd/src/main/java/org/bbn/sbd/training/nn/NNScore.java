package org.bbn.sbd.training.nn;

public class NNScore<T> {
	
	T nodeIndex;
	
	int layerIndex;
	
	double score;
	
	double sigmoid;
	
	
	// constructors
	public NNScore(int layerIndex, T nodeIndex, double score)
	{
		this.layerIndex = layerIndex;
		this.nodeIndex = nodeIndex;
		this.score = score;
	}
	
	public NNScore(int layerIndex, T nodeIndex)
	{
		this.layerIndex = layerIndex;
		this.nodeIndex = nodeIndex;
	}
	
	
	// getters and setters
	public T getNodeIndex() {
		return nodeIndex;
	}

	public void setNodeIndex(T nodeIndex) {
		this.nodeIndex = nodeIndex;
	}

	public int getLayerIndex() {
		return layerIndex;
	}

	public void setLayerIndex(int layerIndex) {
		this.layerIndex = layerIndex;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	public void setSigmoidScore(double sigmoid) {
		this.sigmoid = sigmoid;
	}
	
	public double getSigmoidScore() {
		return this.sigmoid;
	}

	
	// other helper methods

}
