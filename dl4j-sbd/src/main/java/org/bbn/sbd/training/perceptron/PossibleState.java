package org.bbn.sbd.training.perceptron;

public class PossibleState 
{
	
	
	int wordIndex;
	
	int currlabel;
	
	double score;
	
	PossibleState prev;
	
	
	// constructor
	public PossibleState(int wordIndex, int currlabel, double score)
	{
		this.wordIndex = wordIndex;
		this.currlabel = currlabel;
		this.score = score;
		prev = null;
	}
	
	
	// getters
	public int getWordIndex()
	{
		return wordIndex;
	}
	
	public double getScore()
	{
		return score;
	}
	
	public int getLabel()
	{
		return currlabel;
	}
	
	public PossibleState getPrevious()
	{
		return prev;
	}
	
	
	// setters
	public void setPrevious(PossibleState node)
	{
		prev = node;
	}

	public void setScore(double score) 
	{
		this.score = score;	
	}

}
