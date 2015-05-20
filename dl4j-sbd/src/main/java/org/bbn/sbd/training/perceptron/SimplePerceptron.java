package org.bbn.sbd.training.perceptron;

import org.bbn.sbd.datastructures.*;

import java.util.*;
import java.io.*;
import java.lang.Math;

public class SimplePerceptron<K> 
{
	Map<K,Double> weights;
	
	double bias = Math.random()*0.06;
	
	double learningRate = 0.05;
	
	// constructor
    public SimplePerceptron(double initialBias, double learningRate)
    {
    	this.bias = initialBias;
    	this.learningRate = learningRate;
    }
    
    /**
     * returns 0.0 if unknown feature
     * @param feature
     * @return
     */
    public double getWeight(K feature)
    {
    	if(weights.containsKey(feature))
    		return weights.get(feature);
    	else return 0.0;
    }
    
    /**
     * compute output +1 or -1 given input features
     * @param features
     * @param mode
     * @return
     */
    private double feedForward(SparseVector<K> features, PerceptronMode mode)
    {
    	// return +1 or -1 for averaged perceptron
    	double output = bias;
    	
    	for(K feature : features.keySet())
    	{
    		if(mode == PerceptronMode.TRAIN)
    		{
    			if(!weights.containsKey(feature))
        			weights.put(feature, Math.random()*0.06);
    		}
    		output += features.getFeature(feature)*getWeight(feature);
    	}
    	
    	if(output >= 0) return 1;
    	else return -1;
    }
    
    /**
     * error here is simply difference between true
     * and hypothesized output
     * @param hyp
     * @param truelabel
     * @return
     */
    private double calculateError(double hyp, double truelabel)
    {
    	return truelabel-hyp;
    }
    
    /**
     * update weights to reduce error
     * @param features
     * @param error
     */
    private void updateWeights(SparseVector<K> features, double error)
    {
    	for(K feature : features.keySet())
    	{
    		weights.put(feature, getWeight(feature) + learningRate*error*features.getFeature(feature));
    	}
    }

    /**
     * training consists of three steps
     * feed forward to compute output in current state
     * compute error function
     * update weights to reduce error 
     * @param features
     * @param trueLabel
     */
    public void train(SparseVector<K> features, double trueLabel)
    {
    	double hyp = feedForward(features, PerceptronMode.TRAIN);
    	double error = calculateError(hyp, trueLabel);
    	updateWeights(features,error);
    }
    
    /**
     * simple decode
     * @param features
     * @return
     */
    public double simpleDecode(SparseVector<K> features)
    {
    	return feedForward(features, PerceptronMode.TEST);
    }
    
    
    /**
     * return weights as string
     * tab separated featureId, weight
     * one featureId per line
     * @return
     */
    private String weightsToString() 
    {
        String s = "";
        for (Object key: weights.keySet()) {
            s += key + "\t" + weights.get(key) + "\n";
        }
        return s;
    }
    
    /**
     * save model weights to file system
     * @param modelpath
     */
	public void saveModel(String modelpath)
    {
		try
    	{
    		BufferedWriter bw = new BufferedWriter(new FileWriter(modelpath, true));
    		bw.write(weightsToString());
    		bw.flush();
    		bw.close();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
	
	/**
	 * load model from file
	 * @param modelpath
	 */
	@SuppressWarnings("unchecked")
	public void loadModel(String modelpath)
    {
    	try
    	{
    		BufferedReader br = new BufferedReader(new FileReader(modelpath));
    		String line;
    		while((line = br.readLine()) != null)
    		{
    			String[] temp = line.split("\t");
    			weights.put((K)temp[0], Double.parseDouble(temp[1]));
    		}
    		br.close();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    }

}
