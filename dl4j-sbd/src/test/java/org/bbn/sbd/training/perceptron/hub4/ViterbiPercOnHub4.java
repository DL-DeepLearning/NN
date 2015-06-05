package org.bbn.sbd.training.perceptron.hub4;

import java.util.ArrayList;
import java.util.List;

import org.bbn.sbd.datastructures.SparseVector;
import org.bbn.sbd.datastructures.Turn;
import org.bbn.sbd.featureextractor.FeatureExtractor;
import org.bbn.sbd.io.ReadRtXml;
import org.bbn.sbd.scoring.Score;
import org.bbn.sbd.training.perceptron.PerceptronMode;
import org.bbn.sbd.training.perceptron.ViterbiPerceptron;

// arg[0] = training xml file
// arg[1] = test xml file
public class ViterbiPercOnHub4 
{
      public static void main(String[] args)
      {
    	  try
  		{
            ViterbiPerceptron<String> perceptron = new ViterbiPerceptron<String>();	 
  			FeatureExtractor featex = new FeatureExtractor();
  			
  			int turnindex;
  			double prevSER = 1000.0;
  			int window = 7;
  			if(args.length>2)
  				window = Integer.parseInt(args[2]);
  			
  			List<Turn> training_turns = ReadRtXml.readIntoListOfTurns(args[0]);
  			List<Turn> test_turns = ReadRtXml.readIntoListOfTurns(args[1]);
  			
  			for(int iter=0; iter<10;iter++)
  			{
  				
  				System.out.println("Iteration number: " + (iter+1));
  				turnindex = 0;
  	  			for(Turn turn : training_turns)
  	  			{
  	  				turnindex++;
  	  				
  	  				if(turnindex/4000 > 0)
  	  				{
  	  				   System.out.println("....");
  	  				   turnindex = 0;
  	  				}
  	  				
  	  				//System.out.println("Training turn number " + turnindex + " number of words: " + turn.getWords().size());
  	  				List<SparseVector<String>> features = featex.getSparseFeatureVector(turn, window);
  	  				perceptron.train(turn, features);
  	  			}
  	  			
  	  		    
  			    List<Integer> truelabels = new ArrayList<Integer>();
  			    List<Integer> hyp = new ArrayList<Integer>();
  			    turnindex=0;
  			    for(Turn turn : test_turns)
  			    {
  				   turnindex++;
  				   for(int i=0; i<turn.getWords().size(); i++)
  				   {
  				      truelabels.add(turn.get(i).getLabel());
  				   }
  				
  				   //System.out.println("decoding turn number " + turnindex);
  				   List<SparseVector<String>> features = featex.getSparseFeatureVector(turn, window);
			       hyp.addAll(perceptron.decode(turn, features, PerceptronMode.TEST));
			       //hyp.remove(hyp.size()-1);
  			    }
  			
  			
  			    double SER = Score.score(hyp, truelabels);
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
