package org.bbn.sbd.featureextractor;

import org.bbn.sbd.datastructures.*;

import java.util.*;

public class FeatureExtractor 
{
     public SparseVector<String> getSparseFeatureVector(Word word, Turn turn, int window)
     {
    	 if(!turn.contains(word))
    		 throw new RuntimeException("Word is not in turn. Please try again.");
    	 if(window!=5 && window!=7)
    		 throw new RuntimeException("Window size can only be 5 or 7");
    	 
    	 int wordIndex = turn.indexOf(word);
    	 int turnLength = turn.size();
    			 
    	 SparseVector<String> fv = new SparseVector<String>();
    	 
    	 // add unigram features
    	 fv.addFeatures(getUnigramFeatures(window==7&&wordIndex-3>0&&wordIndex-3<turnLength?turn.get(wordIndex-3):null, 
    			 wordIndex-2>0&&wordIndex-2<turnLength?turn.get(wordIndex-2):null, 
    		     wordIndex-1>0&&wordIndex-1<turnLength?turn.get(wordIndex-1):null,
    			 word,
    			 wordIndex+1>0&&wordIndex+1<turnLength?turn.get(wordIndex+1):null, 
    			 wordIndex+2>0&&wordIndex+2<turnLength?turn.get(wordIndex+2):null,
    			 wordIndex+3>0&&wordIndex+3<turnLength?turn.get(wordIndex+3):null
    					 ));
    	 
    	 // add bigram features
    	 fv.addFeatures(getBigramFeatures(window==7&&wordIndex-3>0&&wordIndex-3<turnLength?turn.get(wordIndex-3):null, 
    			 wordIndex-2>0&&wordIndex-2<turnLength?turn.get(wordIndex-2):null, 
    		     wordIndex-1>0&&wordIndex-1<turnLength?turn.get(wordIndex-1):null,
    			 word,
    			 wordIndex+1>0&&wordIndex+1<turnLength?turn.get(wordIndex+1):null, 
    			 wordIndex+2>0&&wordIndex+2<turnLength?turn.get(wordIndex+2):null,
    			 wordIndex+3>0&&wordIndex+3<turnLength?turn.get(wordIndex+3):null
    					 ));
    	 
    	 // add trigram features
    	 fv.addFeatures(getTrigramFeatures(window==7&&wordIndex-3>0&&wordIndex-3<turnLength?turn.get(wordIndex-3):null, 
    			 wordIndex-2>0&&wordIndex-2<turnLength?turn.get(wordIndex-2):null, 
    		     wordIndex-1>0&&wordIndex-1<turnLength?turn.get(wordIndex-1):null,
    			 word,
    			 wordIndex+1>0&&wordIndex+1<turnLength?turn.get(wordIndex+1):null, 
    			 wordIndex+2>0&&wordIndex+2<turnLength?turn.get(wordIndex+2):null,
    			 wordIndex+3>0&&wordIndex+3<turnLength?turn.get(wordIndex+3):null
    					 ));
    	 
    	 // add pause features
    	 fv.addFeatures(getPauseFeatures(window==7&&wordIndex-3>0&&wordIndex-3<turnLength?turn.get(wordIndex-3):null, 
    			 wordIndex-2>0&&wordIndex-2<turnLength?turn.get(wordIndex-2):null, 
    		     wordIndex-1>0&&wordIndex-1<turnLength?turn.get(wordIndex-1):null,
    			 word,
    			 wordIndex+1>0&&wordIndex+1<turnLength?turn.get(wordIndex+1):null, 
    			 wordIndex+2>0&&wordIndex+2<turnLength?turn.get(wordIndex+2):null,
    			 wordIndex+3>0&&wordIndex+3<turnLength?turn.get(wordIndex+3):null
    					 ));
    	 
    	 return fv;
     }
     
     

	public List<SparseVector<String>> getSparseFeatureVector(Turn turn, int window)
     {
		List<SparseVector<String>> fvectors = new ArrayList<SparseVector<String>>();
		for(Word word : turn.getWords())
		{
			fvectors.add(getSparseFeatureVector(word, turn, window));
		}
		
		return fvectors;
     }
     
	
	// unigram features
     private Map<String, Double> getUnigramFeatures(Word p3word, Word ppword, Word pword, Word word, Word nword, 
    		 Word nnword, Word n3word)
     {
    	 //TODO
    	 return null;
     }
     
     // bigram features
     private Map<String, Double> getBigramFeatures(Word p3word, Word ppword, Word pword, Word word, Word nword, 
    		 Word nnword, Word n3word)
     {
    	 //TODO
    	 return null;
     }
     
     // trigram features
     private Map<String, Double> getTrigramFeatures(Word p3word, Word ppword, Word pword, Word word, Word nword, 
    		 Word nnword, Word n3word)
     {
    	 //TODO
    	 return null;
     }
     
     // pause features
     private Map<String, Double> getPauseFeatures(Word p3word, Word ppword, Word pword, Word word, Word nword, 
    		 Word nnword, Word n3word)
     {
    	 //TODO
    	 return null;
     }
     
}
