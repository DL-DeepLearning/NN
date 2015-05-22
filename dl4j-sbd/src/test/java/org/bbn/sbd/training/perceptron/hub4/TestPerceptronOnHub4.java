package org.bbn.sbd.training.perceptron.hub4;

import org.bbn.sbd.scoring.PRF;
import org.bbn.sbd.training.perceptron.SimplePerceptron;
import org.bbn.sbd.io.*;
import org.bbn.sbd.featureextractor.*;
import org.bbn.sbd.datastructures.*;

import java.util.*;

public class TestPerceptronOnHub4 {
	
	public static void main(String[] args)
	{
		try
		{
			SimplePerceptron<String> perceptron = new SimplePerceptron<String>();
			List<Double> truelabels = new ArrayList<Double>();
			List<Double> hyp = new ArrayList<Double>();
			Turn turn = ReadRtXml.readAsSingleTurn(args[0]);
			FeatureExtractor featex = new FeatureExtractor();
			
			perceptron.loadModel(args[1]);
			
			for(Word word : turn.getWords())
			{
				truelabels.add((double) word.getLabel());
				SparseVector<String> features = featex.getSparseFeatureVector(word, turn, 7);
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
