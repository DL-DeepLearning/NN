package org.bbn.sbd.training.perceptron.hub4;

import org.bbn.sbd.scoring.Score;
import org.bbn.sbd.training.perceptron.SimplePerceptron;
import org.bbn.sbd.io.*;
import org.bbn.sbd.featureextractor.*;
import org.bbn.sbd.datastructures.*;

import java.util.*;

public class TestPerceptronOnHub4 {
	
	// 2 cmdline args.
	// first is the test RtXml file
	// second is the location from where to load perc model
	public static void main(String[] args)
	{
		try
		{
			SimplePerceptron<String> perceptron = new SimplePerceptron<String>();
			List<Integer> truelabels = new ArrayList<Integer>();
			List<Integer> hyp = new ArrayList<Integer>();
			Turn turn = ReadRtXml.readAsSingleTurn(args[0], false);
			FeatureExtractor featex = new FeatureExtractor();
			
			perceptron.loadModel(args[1]);
			
			for(Word word : turn.getWords())
			{
				truelabels.add(word.getLabel());
				SparseVector<String> features = featex.getSparseFeatureVector(word, turn, 7);
				hyp.add(perceptron.simpleDecode(features));
			}
			
			Score.score(hyp, truelabels);
	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
