package org.bbn.sbd.training.perceptron.hub4;

import java.util.ArrayList;
import java.util.List;

import org.bbn.sbd.datastructures.SparseVector;
import org.bbn.sbd.datastructures.Turn;
import org.bbn.sbd.datastructures.Word;
import org.bbn.sbd.featureextractor.FeatureExtractor;
import org.bbn.sbd.io.ReadRtXml;
import org.bbn.sbd.scoring.Score;
import org.bbn.sbd.training.perceptron.SimplePerceptron;

// arg[0] = training xml file
// arg[1] = test xml file
public class TrainAndTestPercOnHub4 {
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		try
		{
            SimplePerceptron<String> perceptron = new SimplePerceptron<String>();	 
			
			Turn training_turn = ReadRtXml.readAsSingleTurn(args[0], false);
			Turn val_turn = ReadRtXml.readAsSingleTurn(args[1], false);
			FeatureExtractor featex = new FeatureExtractor();
			
			double prevSER = 1000.0;
			for(int iter=0; iter<2; iter++)
			{
				System.out.println("Iteration: " + (iter+1));
				for(int i=0; i<training_turn.getWords().size();i++)
				{
					Word word = training_turn.getWords().get(i);
					int truelabel = word.getLabel();
					SparseVector<String> features = featex.getSparseFeatureVector(word, training_turn, 7);
					perceptron.train(features, truelabel);
				}

				featex = new FeatureExtractor();
				List<Integer> truelabels = new ArrayList<Integer>();
				List<Integer> hyp = new ArrayList<Integer>();
				
				for(int i=0; i<val_turn.getWords().size();i++)
				{
					Word word = val_turn.getWords().get(i);
					truelabels.add(word.getLabel());
					SparseVector<String> features = featex.getSparseFeatureVector(word, val_turn, 7);
					int prediction = perceptron.simpleDecode(features);
					hyp.add(prediction);
					word.setLabel(prediction);
				}
				
				double SER = Score.score(hyp, truelabels);
  			    /*if(SER > prevSER)
  			    {
  			    	System.out.println("Best SER = " + prevSER + " on iteration " + iter);
  			    	break;
  			    }
  			    prevSER = SER;*/
						
			}
			System.out.println("=======================================================");
			System.out.println("Now decoding on test..");
			System.out.println("=======================================================");
			featex = new FeatureExtractor();
			List<Integer> truelabels = new ArrayList<Integer>();
			List<Integer> hyp = new ArrayList<Integer>();
			Turn test_turn = ReadRtXml.readAsSingleTurn(args[2], false);
			for(int i=0; i<test_turn.getWords().size();i++)
			{
				Word word = test_turn.getWords().get(i);
				truelabels.add(word.getLabel());
				SparseVector<String> features = featex.getSparseFeatureVector(word, test_turn, 7);
				int prediction = perceptron.simpleDecode(features);
				hyp.add(prediction);
				word.setLabel(prediction);
			}
			double SER = Score.score(hyp, truelabels);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
