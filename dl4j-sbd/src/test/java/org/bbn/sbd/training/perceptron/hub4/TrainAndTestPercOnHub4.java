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
	
	public static void main(String[] args)
	{
		try
		{
            SimplePerceptron<String> perceptron = new SimplePerceptron<String>();	 
			
			Turn turn = ReadRtXml.readAsSingleTurn(args[0]);
			FeatureExtractor featex = new FeatureExtractor();
			
			int wordnumber = 0;
			for(Word word : turn.getWords())
			{
				wordnumber++;
				System.out.println("Training instance number " + wordnumber);
				int truelabel = word.getLabel();
				SparseVector<String> features = featex.getSparseFeatureVector(word, turn, 5);
				perceptron.train(features, truelabel);
			}
			
			
			turn = ReadRtXml.readAsSingleTurn(args[1]);
			featex = new FeatureExtractor();
			List<Integer> truelabels = new ArrayList<Integer>();
			List<Integer> hyp = new ArrayList<Integer>();
			
			for(Word word : turn.getWords())
			{
				truelabels.add(word.getLabel());
				SparseVector<String> features = featex.getSparseFeatureVector(word, turn, 5);
				int prediction = perceptron.simpleDecode(features);
				hyp.add(prediction);
				word.setLabel(prediction);
			}
			
			
			/** hardcoded filepath here */
			/*BufferedWriter bw = new BufferedWriter(new FileWriter("/nfs/raid62/u15/research/SBD/dl4j/deeplearning4j/dl4j-sbd/scores.txt", true));
			bw.write("score\ttruelabel\n");
			for(int i=0; i< truelabels.size(); i++)
			{
				bw.write(hyp.get(i) + "\t" + truelabels.get(i) + "\n");
				bw.flush();
			}
			bw.close();*/
			
			
			Score.score(hyp, truelabels);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
