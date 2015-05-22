package org.bbn.sbd.training.perceptron.hub4;

import org.bbn.sbd.training.perceptron.SimplePerceptron;
import org.bbn.sbd.io.*;
import org.bbn.sbd.featureextractor.*;
import org.bbn.sbd.datastructures.*;

// 2 cmdline args.
// first is the input RtXml file
// second is the location to save perc model
public class TrainPerceptronOnHub4 {
	
	public static void main(String[] args)
	{
		try
		{
			SimplePerceptron<String> perceptron = new SimplePerceptron<String>();	    
			Turn turn = ReadRtXml.readAsSingleTurn(args[0]);
			FeatureExtractor featex = new FeatureExtractor();
			for(Word word : turn.getWords())
			{
				int truelabel = word.getLabel();
				SparseVector<String> features = featex.getSparseFeatureVector(word, turn, 7);
				perceptron.train(features, truelabel);
			}
			
			perceptron.saveModel(args[1]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
