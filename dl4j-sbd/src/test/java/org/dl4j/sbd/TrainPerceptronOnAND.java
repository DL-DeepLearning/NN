package org.dl4j.sbd;

import java.util.*;

import org.bbn.sbd.training.perceptron.SimplePerceptron;
import org.bbn.sbd.datastructures.*;

public class TrainPerceptronOnAND 
{
    
	
	public static void main(String[] args)
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
		
		SimplePerceptron<String> perceptron = new SimplePerceptron<String>();
		
		for(int iter=0;iter<10000;iter++)
		{
			for(int inputIndex=0;inputIndex<4;inputIndex++)
			{
				SparseVector<String> features = new SparseVector<String>();
				features.addFeatures(inputList.get(inputIndex));
				boolean modified;
				if(inputIndex == 3)
				{
					modified = perceptron.train(features, +1.0);
				}
				else
					modified = perceptron.train(features, -1.0);
				
				System.out.println("Weights changed? " + modified);
			}
			
			
			System.out.println("Iteration number: " + iter + " done.");
			
		}
		
		perceptron.saveModel(args[0]);
	}
	
	
	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min)+1) + min;

	    return randomNum;
	}
}
