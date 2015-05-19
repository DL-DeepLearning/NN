package org.dl4j.sbd.rntn;

import org.deeplearning4j.models.rntn.RNTN;
import org.deeplearning4j.models.rntn.RNTNEval;
import org.deeplearning4j.models.rntn.Tree;
import org.dl4j.sbd.data.train.fetcher.SbdWordVectorDataFetcher;
import org.dl4j.sbd.data.train.sentence.SbdLabelAwareSentenceIterator;
import org.deeplearning4j.text.corpora.treeparser.TreeParser;
import org.deeplearning4j.text.corpora.treeparser.TreeVectorizer;
import org.nd4j.linalg.api.activation.Activations;

import java.util.Arrays;
import java.util.List;

public class RNTNTrain 
{
	 public static void main(String[] args) throws Exception {
	        SbdWordVectorDataFetcher fetcher = new SbdWordVectorDataFetcher("/nfs/mercury-04/u40/deft/dl4j/deeplearning4j/dl4j-sbd/src/main/resources/exampletrain");
	        SbdLabelAwareSentenceIterator trainiter = new SbdLabelAwareSentenceIterator(null, 
	        		"/nfs/mercury-04/u40/deft/dl4j/deeplearning4j/dl4j-sbd/src/main/resources/exampletrain");
	        RNTN t = new RNTN.Builder()
	                .setActivationFunction(Activations.hardTanh()).setFeatureVectors(fetcher.getVec())
	                .setUseTensors(false).build();

	        TreeVectorizer vectorizer = new TreeVectorizer(new TreeParser());

	        RNTNEval evalt = new RNTNEval();
	        
	        while(trainiter.hasNext()) {
	            List<Tree> trees = vectorizer.getTreesWithLabels(trainiter.nextSentence(),trainiter.currentLabel(), Arrays.asList("0","1"));
	            t.fit(trees);
	            
	            evalt.eval(t, trees);
	            System.out.println(evalt.stats());
	        }
	        
	       // System.out.println("Exited while loop");
	        
	        // run predict() method in RNTN on test?
	        SbdLabelAwareSentenceIterator testiter = new SbdLabelAwareSentenceIterator(null, 
	        		"/nfs/mercury-04/u40/deft/dl4j/deeplearning4j/dl4j-sbd/src/main/resources/exampletest");
	        while(testiter.hasNext()) {
	            List<Tree> trees = vectorizer.getTreesWithLabels(testiter.nextSentence(),testiter.currentLabel(), Arrays.asList("0","1"));
	            System.out.println(t.predict(trees).get(0));
                
	        }
	        
	        System.exit(0);
	    }
}
