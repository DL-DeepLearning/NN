package org.bbn.sbd.training.nn;

import java.util.*;

import org.bbn.sbd.datastructures.SparseVector;
import org.bbn.sbd.datastructures.Turn;
import org.bbn.sbd.datastructures.Word;
import org.bbn.sbd.featureextractor.FeatureExtractor;
import org.bbn.sbd.io.ReadRtXml;
import org.bbn.sbd.scoring.Score;

public class NNApp {
	
	public static void main(String[] args)
	{
		try
		{
			Network<String,String> neuralnet = new Network<String,String>("/nfs/raid62/u15/research/SBD/dl4j/deeplearning4j/dl4j-sbd/src/main/resources/org/bbn"
					+ "/sbd/training/nn/NNConfig.xml");
			
            
  			double prevSER = 1000.0;
  			int window = 7;
  			if(args.length>3)
  				window = Integer.parseInt(args[3]);
  			
  			if(window !=5 && window != 7)
  			{
  				System.out.println("Invalid window size! Only one of 5 or 7 allowed.");
  				System.exit(1);
  			}
  			
  			FeatureExtractor featex = new FeatureExtractor();
  			List<Turn> training_turns = ReadRtXml.readIntoListOfTurns(args[0]);
  			List<Turn> test_turns = ReadRtXml.readIntoListOfTurns(args[1]);
  			
  			
  			for(int iter=0;iter<10;iter++)
  			{
  				/** training */
  	  			for(Turn training_turn : training_turns)
  	  			{
  	     			List<SparseVector<String>> features = featex.getSparseFeatureVector(training_turn, window);
  	  				
  	  				for(int i=0; i< features.size(); i++)
  	  				{
  	  					SparseVector<String> wordFeatures = features.get(i);
  	  					int trueLabel = training_turn.getWords().get(i).getLabel();
  	  					
  	  				    // create arguments for feedforward
  	  					Map<String, NNScore<String>> inputFeatures = new HashMap<String, NNScore<String>>();
  	  					Map<String,NNScore<String>> trueScores = new HashMap<String,NNScore<String>>();
  	  	  				for(String wordFeature : wordFeatures.getFeatures().keySet())
  	  	  				{
  	  	  					NNScore<String> featureScore = new NNScore<String>(0, wordFeature, 1.0);
  	  	  					inputFeatures.put(wordFeature, featureScore);
  	  	  				}
  	  	  				
  	  	  				if(trueLabel == 1)
  	  	  				{
  	  	  					NNScore<String> sbdScore = new NNScore<String>(neuralnet.nHiddenLayers+1, "sbd", 1.0);
  	  	  				    NNScore<String> nsbdScore = new NNScore<String>(neuralnet.nHiddenLayers+1, "nsbd", 0.0);
  	  	  					trueScores.put("sbd", sbdScore);
  	  	  					trueScores.put("nsbd", nsbdScore);
  	  	  				}
  	  	  				else
  	  	  				{
  	  	  				    NNScore<String> sbdScore = new NNScore<String>(neuralnet.nHiddenLayers+1, "sbd", 0.0);
  		  				    NNScore<String> nsbdScore = new NNScore<String>(neuralnet.nHiddenLayers+1, "nsbd", 1.0);
  		  					trueScores.put("sbd", sbdScore);
  		  					trueScores.put("nsbd", nsbdScore);
  	  	  				}
  	  	  				
  	  	  				neuralnet.forwardPropagate(inputFeatures, trueScores);
  	  				}
  	  				
  	  				// run backpropagation
  	  				neuralnet.backPropagate();
  	  			}
  	  			
  	  			/** decode */
  	  			for(Turn test_turn : test_turns)
  	  			{
  	  				List<SparseVector<String>> features = featex.getSparseFeatureVector(test_turn, window);
  	  				
  	  				// decode turn by turn and compute SER
  	  				for(int i=0; i<features.size();i++)
  	  				{
  	  					SparseVector<String> wordFeatures = features.get(i);
  	  					int trueLabel = test_turn.getWords().get(i).getLabel();
  	  					
  	  				    // create arguments for feedforward
  	  					Map<String, NNScore<String>> inputFeatures = new HashMap<String, NNScore<String>>();
  	  					Map<String,NNScore<String>> trueScores = new HashMap<String,NNScore<String>>();
  	  	  				for(String wordFeature : wordFeatures.getFeatures().keySet())
  	  	  				{
  	  	  					NNScore<String> featureScore = new NNScore<String>(0, wordFeature, 1.0);
  	  	  					inputFeatures.put(wordFeature, featureScore);
  	  	  				}
  	  	  				
  	  	  				if(trueLabel == 1)
  	  	  				{
  	  	  					NNScore<String> sbdScore = new NNScore<String>(neuralnet.nHiddenLayers+1, "sbd", 1.0);
  	  	  				    NNScore<String> nsbdScore = new NNScore<String>(neuralnet.nHiddenLayers+1, "nsbd", 0.0);
  	  	  					trueScores.put("sbd", sbdScore);
  	  	  					trueScores.put("nsbd", nsbdScore);
  	  	  				}
  	  	  				else
  	  	  				{
  	  	  				    NNScore<String> sbdScore = new NNScore<String>(neuralnet.nHiddenLayers+1, "sbd", 0.0);
  		  				    NNScore<String> nsbdScore = new NNScore<String>(neuralnet.nHiddenLayers+1, "nsbd", 1.0);
  		  					trueScores.put("sbd", sbdScore);
  		  					trueScores.put("nsbd", nsbdScore);
  	  	  				}
  	  	  				
  	  	  				neuralnet.forwardPropagate(inputFeatures, trueScores);
  	  				}
  	  			}
  	  			

  	  			/** compute SER */
  	  			List<Integer> truelabels = new ArrayList<Integer>();
  	  			List<Integer> predictedlabels = new ArrayList<Integer>();
  	  			
  	  			//TODO: populate the 2 lists initialized above
  	  			for(Map<String, NNScore<String>> actualOutputScores : neuralnet.actualOutputScoresList)
  	  			{
  	  				int bestlabel = -1;
  	  				double bestscore = 0;
  	  				for(String key : actualOutputScores.keySet())
  	  				{
  	  					if(actualOutputScores.get(key).getScore() > bestscore)
  	  					{
  	  						bestscore = actualOutputScores.get(key).getScore();
  	  						if(key.equals("sbd"))
  	  							bestlabel=1;
  	  						else bestlabel = -1;
  	  					}
  	  				}
  	  				
  	  				predictedlabels.add(bestlabel);
  	  			}
  	  			for(Map<String, NNScore<String>> expectedOutputScores : neuralnet.expectedOutputScoresList)
  	  			{
  	  			    int bestlabel = -1;
	  				double bestscore = 0;
	  				for(String key : expectedOutputScores.keySet())
	  				{
	  					if(expectedOutputScores.get(key).getScore() > bestscore)
	  					{
	  						bestscore = expectedOutputScores.get(key).getScore();
	  						if(key.equals("sbd"))
	  							bestlabel=1;
	  						else bestlabel = -1;
	  					}
	  				}
	  				
	  				truelabels.add(bestlabel);
  	  			}
  	  			
  	  			// calculate SER
  	  			double SER = Score.score(predictedlabels, truelabels);
  				if(SER > prevSER)
  				    {
  				    	System.out.println("Best SER = " + prevSER + " on iteration " + iter);
  				    	break;
  				    }
  				prevSER = SER;
  			}
  			
  			
	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	

}
