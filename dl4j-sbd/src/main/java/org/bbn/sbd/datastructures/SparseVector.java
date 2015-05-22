package org.bbn.sbd.datastructures;

import java.util.*;
import java.lang.Math;

public class SparseVector<K>
{
    private Map<K,Double> features;
    public double norm = 1.0;
	public SparseVector() {
		this.features = new HashMap<K, Double>();
		features.put((K)"biasFeature", 1.0);
	}
    
    
    public Map<K,Double> getFeatures()
    {
    	return features;
    }
    
    public void addFeature(K featureId, Double value)
    {
    	features.put(featureId, value);
    }
    
    public double getFeature(K featureId)
    {
    	if(features.containsKey(featureId))
    		return features.get(featureId);
    	else return 0.0;
    }
    
    @SuppressWarnings("unchecked")
	public void addFeatures(Map<K,Double> features)
    {
    	for(@SuppressWarnings("rawtypes") Map.Entry e : features.entrySet())
    		  if(!this.features.containsKey(e.getKey()))
    		    this.features.put((K)e.getKey(), (Double)e.getValue());
    }
    
    public boolean contains(K key) 
    {
		return features.containsKey(key);
	}
    
    
	public int size() 
	{
		return features.size();
	}
	
	public Set<K> keySet() 
	{
		return features.keySet();
	}
    
	
	public double dot(SparseVector<K> vector) 
	{
        double sum = 0.0;
        if (this.features.size() <= vector.getFeatures().size()) 
        {
            for (K key :  this.features.keySet())
                sum += this.getFeature(key) * vector.getFeature(key);
        }
        else  
        {
            for (K key : vector.features.keySet())
                 sum += this.getFeature(key) * vector.getFeature(key);
        }
        return sum;
    }
	
	
	public double cosineSimiliary(SparseVector<K> vector) 
	{
		double sim = 0.0;
		sim = this.dot(vector);
		  sim = this.dot(vector)/(this.norm()*vector.norm());
		return sim;
	}

	
    // return the 2-norm
	public double norm() 
	{
		return this.norm;
	}
	
	
    public void computeNorm() 
    {
        double sum = 0.0; 
        for (K key :  this.features.keySet()) {
        	double value = this.getFeature(key);
        	sum += value*value;
        }
        this.norm = Math.sqrt(sum);
     
    }

    // return alpha * a
    public SparseVector<K> scale(double alpha) 
    {
        SparseVector<K> scaled = new SparseVector<K>();
        for (K key : this.features.keySet()) scaled.addFeature(key, alpha * this.getFeature(key));
        return scaled;
    }

    // return a + b
    public SparseVector<K> plus(SparseVector<K> vector) 
    {
        SparseVector<K> sum = new SparseVector<K>();
        for (K key : this.features.keySet()) sum.addFeature(key, this.getFeature(key));                // sum = this
        for (K key : vector.getFeatures().keySet()) sum.addFeature(key, vector.getFeature(key) + sum.getFeature(key));     // sum = sum + vector
        return sum;
    }

    // return a string representation
    public String toString() 
    {
        String s = "";
        for (Object key: features.keySet()) {
            s += "(" + key + ", " + features.get(key) + ") \n";
        }
        return s;
    }
    
    
}
