package org.bbn.sbd.training.nn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bbn.sbd.datastructures.SparseVector;
import org.bbn.sbd.scoring.Score;

public class TestNNOnXOR {
	
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
			
			Network<String,String> neuralnet = new Network<String,String>("/nfs/raid62/u15/research/SBD/dl4j/deeplearning4j/dl4j-sbd/src/main/resources/org/bbn"
					+ "/sbd/training/nn/NNConfig_XOR.xml");
			
			double prevSER = 1000.0;
			
			for(int iter=0;iter<Integer.parseInt(args[0]);iter++)
			{
				  neuralnet.inputScoresList.clear();
				  neuralnet.hiddenScoresList.clear();
				  neuralnet.actualOutputScoresList.clear();
				  neuralnet.expectedOutputScoresList.clear();
				
  	  				for(int i=0; i< inputList.size(); i++)
  	  				{
  	  				    // create arguments for feedforward
  	  					Map<String, NNScore<String>> inputFeatures = new HashMap<String, NNScore<String>>();
  	  					Map<String,NNScore<String>> trueScores = new HashMap<String,NNScore<String>>();
  	  	  				for(String inputFeature : inputList.get(i).keySet())
  	  	  				{
  	  	  					NNScore<String> featureScore = new NNScore<String>(0, inputFeature, 1.0);
  	  	  					inputFeatures.put(inputFeature, featureScore);
  	  	  				}
  	  	  				
  	  	  			    if(i==1 || i==2)
	  				    {
	  					    NNScore<String> xorScore = new NNScore<String>(neuralnet.nHiddenLayers+1, "xor", 1.0);
	  					    xorScore.setSigmoidScore(1.0);
	  					    trueScores.put("xor", xorScore);
	  					
	  					    NNScore<String> nxorScore = new NNScore<String>(neuralnet.nHiddenLayers+1, "nxor", 0.0);
	  					    nxorScore.setSigmoidScore(0.0);
	  					    trueScores.put("nxor", nxorScore);
	  				    }
	  				    else
	  				    {
	  				        NNScore<String> xorScore = new NNScore<String>(neuralnet.nHiddenLayers+1, "xor", 0.0);
	  				        xorScore.setSigmoidScore(0.0);
	  					    trueScores.put("xor", xorScore);
	  					
	  					    NNScore<String> nxorScore = new NNScore<String>(neuralnet.nHiddenLayers+1, "nxor", 1.0);
	  					    nxorScore.setSigmoidScore(1.0);
	  					    trueScores.put("nxor", nxorScore);
	  				    }
  	  	  				
  	  	  				neuralnet.forwardPropagate(inputFeatures, trueScores);
  	  	  				neuralnet.backPropagate();
				    }
				
				
				System.out.println("Iteration number: " + iter + " done.");

				
				for(int i=0; i< inputList.size(); i++)
				{
					    // create arguments for feedforward
						Map<String, NNScore<String>> inputFeatures = new HashMap<String, NNScore<String>>();
						Map<String,NNScore<String>> trueScores = new HashMap<String,NNScore<String>>();
		  				for(String inputFeature : inputList.get(i).keySet())
		  				{
		  					NNScore<String> featureScore = new NNScore<String>(0, inputFeature, 1.0);
		  					inputFeatures.put(inputFeature, featureScore);
		  				}
		  				
		  				 if(i==1 || i==2)
		  				    {
		  					    NNScore<String> xorScore = new NNScore<String>(neuralnet.nHiddenLayers+1, "xor", 1.0);
		  					    xorScore.setSigmoidScore(1.0);
		  					    trueScores.put("xor", xorScore);
		  					
		  					    NNScore<String> nxorScore = new NNScore<String>(neuralnet.nHiddenLayers+1, "nxor", 0.0);
		  					    nxorScore.setSigmoidScore(0.0);
		  					    trueScores.put("nxor", nxorScore);
		  				    }
		  				    else
		  				    {
		  				        NNScore<String> xorScore = new NNScore<String>(neuralnet.nHiddenLayers+1, "xor", 0.0);
		  				        xorScore.setSigmoidScore(0.0);
		  					    trueScores.put("xor", xorScore);
		  					
		  					    NNScore<String> nxorScore = new NNScore<String>(neuralnet.nHiddenLayers+1, "nxor", 1.0);
		  					    nxorScore.setSigmoidScore(1.0);
		  					    trueScores.put("nxor", nxorScore);
		  				    }
		  				
		  				neuralnet.forwardPropagate(inputFeatures, trueScores);
				}
		  				
		  				/** compute SER */
		  	  			List<Integer> truelabels = new ArrayList<Integer>();
		  	  			List<Integer> predictedlabels = new ArrayList<Integer>();
		  	  			
		  	  			//TODO: populate the 2 lists initialized above
		  	  			for(Map<String, NNScore<String>> actualOutputScores : neuralnet.actualOutputScoresList)
		  	  			{
		  	  				int bestlabel = -1;
		  	  				double bestscore = 0;
		  	  				int bestindex = 0;
		  	  			    for(int to=0; to<neuralnet.nOutputNodes; to++)
		  	  		        {
		  	  			       if(actualOutputScores.get(neuralnet.outputNodeIds.get(to)).getSigmoidScore() >= bestscore)
		  	  			       {
		  	  				     bestscore = actualOutputScores.get(neuralnet.outputNodeIds.get(to)).getSigmoidScore();
		  	  				     bestindex = to;
		  	  			       }
		  	  		        }
		  	  			    if(neuralnet.outputNodeIds.get(bestindex).equals("xor"))
		  	  			    	bestlabel = 1;
		  	  			    else bestlabel = -1;
		  	  			    
		  	  				predictedlabels.add(bestlabel);
		  	  				System.out.println("Predicted label: " + bestlabel + " Winning score: " + bestscore);
		  	  			}
		  	  			for(Map<String, NNScore<String>> expectedOutputScores : neuralnet.expectedOutputScoresList)
		  	  			{
		  	  			   int bestlabel = -1;
	  	  				   double bestscore = 0;
	  	  				   int bestindex = 0;
	  	  			       for(int to=0; to<neuralnet.nOutputNodes; to++)
	  	  		           {
	  	  			          if(expectedOutputScores.get(neuralnet.outputNodeIds.get(to)).getSigmoidScore() >= bestscore)
	  	  			          {
	  	  				        bestscore = expectedOutputScores.get(neuralnet.outputNodeIds.get(to)).getSigmoidScore();
	  	  				        bestindex = to;
	  	  			          }
	  	  		           }
	  	  			       if(neuralnet.outputNodeIds.get(bestindex).equals("xor"))
	  	  			    	   bestlabel = 1;
	  	  			       else bestlabel = -1;
	  	  			    
	  	  				   truelabels.add(bestlabel);
	  	  				   System.out.println("True label: " + bestlabel + " Winning score: " + bestscore);
		  	  			}
		  	  			
		  	  			for(int i=0; i<predictedlabels.size();i++)
		  	  			{
		  	  				System.out.println("predicted: " + predictedlabels.get(i) + " true: " + truelabels.get(i));
		  	  			}
		  	  			
		  	  			// calculate SER
		  	  			double SER = Score.score(predictedlabels, truelabels);
		  				/*if(SER > prevSER)
		  				    {
		  				    	System.out.println("Best SER = " + prevSER + " on iteration " + iter);
		  				    	break;
		  				    }
		  				prevSER = SER;*/
				}	
		}
		catch(Exception e)
		{
		    e.printStackTrace();	
		}
	}

}
