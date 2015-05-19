package org.bbn.sbd.datastructures;

import java.util.*;

public class FeatureVector 
{
    private Map<String,Double> features = new HashMap<String,Double>();
    
    
    public Map<String,Double> getFeatures()
    {
    	return features;
    }
    
    public void addFeature(String featureId, Double value)
    {
    	features.put(featureId, value);
    }
    
    public void addFeatures(Map<String,Double> features)
    {
    	for(Map.Entry e : features.entrySet())
    		  if(!this.features.containsKey(e.getKey()))
    		    this.features.put((String)e.getKey(), (Double)e.getValue());
    }
    
    public List<String> createSparseVector()
    {
       // TODO
    	return null;
    }
    
    public List<String> createDenseVector()
    {
    	//TODO
    	return null;
    }
    
}
