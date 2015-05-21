package org.bbn.sbd.training.perceptron;

import java.util.*;

import org.bbn.sbd.scoring.PRF;
import org.bbn.sbd.training.perceptron.SimplePerceptron;
import org.bbn.sbd.datastructures.*;

public class TestPerceptronOnAND 
{
    public static void main(String[] args)
    {
    	try
    	{
    		List<Map<String,Double>> inputList = new ArrayList<Map<String,Double>>();
    		
    		//0,0
    		Map<String,Double> _00inputs = new HashMap<String,Double>();
    		_00inputs.put("x0", 1.0);
    		_00inputs.put("y0", 1.0);
    		inputList.add(_00inputs);
    		
    		//0,1
    		Map<String,Double> _01inputs = new HashMap<String,Double>();
    		_01inputs.put("x0", 1.0);
    		_01inputs.put("y1", 1.0);
    		inputList.add(_01inputs);
    		
    		//1,0
    		Map<String,Double> _10inputs = new HashMap<String,Double>();
    		_10inputs.put("x1", 1.0);
    		_10inputs.put("y0", 1.0);
    		inputList.add(_10inputs);
    		
    		//1,1
    		Map<String,Double> _11inputs = new HashMap<String,Double>();
    		_11inputs.put("x1", 1.0);
    		_11inputs.put("y1", 1.0);
    		inputList.add(_11inputs);
    		
    		List<Double> truelabels = Arrays.asList(new Double[]{-1.0,-1.0,-1.0,+1.0});
    		
    		SimplePerceptron<String> perceptron = new SimplePerceptron<String>();
    		perceptron.loadModel(args[0]);
    		
    		List<Double> hyp = new ArrayList<Double>();
    		for(int i=0;i<inputList.size();i++)
    		{
    			SparseVector<String> features = new SparseVector<String>();
    			features.addFeatures(inputList.get(i));
    			
    			hyp.add(perceptron.simpleDecode(features));
    		}
    		
    		PRF.score(hyp, truelabels);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}
