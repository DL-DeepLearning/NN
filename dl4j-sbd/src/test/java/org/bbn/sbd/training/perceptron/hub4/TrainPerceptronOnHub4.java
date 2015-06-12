package org.bbn.sbd.training.perceptron.hub4;

import org.bbn.sbd.training.perceptron.SimplePerceptron;
import org.bbn.sbd.io.*;
import org.bbn.sbd.featureextractor.*;
import org.bbn.sbd.datastructures.*;

// 2 cmdline args.
// first is the training RtXml file
// second is the location to save perc model
public class TrainPerceptronOnHub4 {
	
	public static void main(String[] args)
	{
		try
		{
			SimplePerceptron<String> perceptron = new SimplePerceptron<String>();	 
			
			System.out.println("before read");
			Turn turn = ReadRtXml.readAsSingleTurn(args[0], false);
			System.out.println("after read");
			
			FeatureExtractor featex = new FeatureExtractor();
			
			int wordnumber = 0;
			for(Word word : turn.getWords())
			{
				wordnumber++;
				System.out.println("Training instance number " + wordnumber);
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
