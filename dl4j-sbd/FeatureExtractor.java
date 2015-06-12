package org.bbn.sbd.featureextractor;

import org.bbn.sbd.datastructures.*;

import java.util.*;

public class FeatureExtractor 
{
	 /* static inner class */
	 static class WordAttributes {
		 
		 String wordId;
		 int wordLabel;
		 double wordPause;
		 double wordapause;
		 double wordPauseProsodic;
		 String wordPauseType;
		 boolean wordPauseFiller;
		 boolean wordChopBoundary;
		 
	 }
	 /* end of inner class */
	
	 
     public SparseVector<String> getSparseFeatureVector(Word word, Turn turn, int window)
     {
    	 if(!turn.contains(word))
    		 throw new RuntimeException("Word is not in turn. Please try again.");
    	 if(window!=5 && window!=7)
    		 throw new RuntimeException("Window size can only be 5 or 7");
    	 
    	 int wordIndex = turn.indexOf(word);
    	 int turnLength = turn.size();
    			 
    	 SparseVector<String> fv = new SparseVector<String>();
    	 
    	 WordAttributes p3word = null, ppword = null, pword = null, currword = null,
    			 nword = null, nnword = null, n3word = null;
    	 
    	 // get word attributes
    	 if(wordIndex-3>0&&wordIndex-3<turnLength)
    	    p3word = getWordAttributes(turn.get(wordIndex-3));
    	 if(wordIndex-2>0&&wordIndex-2<turnLength)
     	    ppword = getWordAttributes(turn.get(wordIndex-2));
    	 if(wordIndex-1>0&&wordIndex-1<turnLength)
     	    pword = getWordAttributes(turn.get(wordIndex-1));
    	 if(wordIndex>0&&wordIndex<turnLength)
      	    currword = getWordAttributes(turn.get(wordIndex));
    	 if(wordIndex+1>0&&wordIndex+1<turnLength)
      	    nword = getWordAttributes(turn.get(wordIndex+1));
    	 if(wordIndex+2>0&&wordIndex+2<turnLength)
      	    nnword = getWordAttributes(turn.get(wordIndex+2));
    	 if(wordIndex+3>0&&wordIndex+3<turnLength)
      	    n3word = getWordAttributes(turn.get(wordIndex+3));
    	     	 
    	 
        // all features as in Amit's script
        if(p3word!=null && ppword!=null && pword!=null && currword!=null && p3word.wordId!=null && ppword.wordId!=null && pword.wordId!=null && currword.wordId!=null && window==7)
        	fv.addFeature(String.format("W-3<%s>_W-2<%s>_W-1<%s>_W<%s>", p3word.wordId, ppword.wordId, pword.wordId, currword.wordId), 1.0);
        if(p3word!=null && ppword!=null && pword!=null && p3word.wordId!=null && ppword.wordId!=null && pword.wordId!=null && window==7)
        	fv.addFeature(String.format("W-3<%s>_W-2<%s>_W-1<%s>", p3word.wordId, ppword.wordId, pword.wordId), 1.0);
        if(p3word!=null && currword!=null && p3word.wordId!=null && currword.wordId!=null && window==7)
        	fv.addFeature(String.format("W-3<%s>_W<%s>", p3word.wordId, currword.wordId), 1.0);
        if(p3word!=null && ppword!=null && p3word.wordId!=null && ppword.wordId!=null && window==7)
        	fv.addFeature(String.format("W-3<%s>_W-2<%s>", p3word.wordId, ppword.wordId), 1.0);
        if(ppword!=null && pword!=null && ppword.wordId!=null && pword.wordId!=null)
            fv.addFeature(String.format("W-2<%s>_W-1<%s>", ppword.wordId, pword.wordId), 1.0);

        if(ppword!=null && pword!=null && currword!=null && ppword.wordId!=null && pword.wordId!=null && currword.wordId!=null)
        	fv.addFeature(String.format("W-2<%s>_W-1<%s>_W<%s>", ppword.wordId, pword.wordId, currword.wordId), 1.0);
        if(pword!=null && currword!=null && pword.wordId!=null && currword.wordId!=null)
        	fv.addFeature(String.format("W-1<%s>_W<%s>", pword.wordId, currword.wordId), 1.0);
        if(pword!=null && pword.wordId!=null)
        	fv.addFeature(String.format("W-1<%s>", pword.wordId), 1.0);
        if(pword!=null && nword!=null && pword.wordId!=null && nword.wordId!=null)
        	fv.addFeature(String.format("W-1<%s>_W+1<%s>", pword.wordId, nword.wordId), 1.0);
        if(currword!=null && currword.wordId!=null)
        	fv.addFeature(String.format("W<%s>", currword.wordId),1.0);
        if(pword!=null && currword!=null && nword!=null && pword.wordId!=null && currword.wordId!=null && nword.wordId!=null)
        	fv.addFeature(String.format("W-1<%s>_W<%s>_W+1<%s>", pword.wordId, currword.wordId, nword.wordId), 1.0);
        if(nword!=null && nword.wordId!=null)
        	fv.addFeature(String.format("W+1<%s>", nword.wordId), 1.0);
        if(nword!=null && currword!=null && nword.wordId!=null && currword.wordId!=null)
        	fv.addFeature(String.format("W<%s>_W+1<%s>", currword.wordId, nword.wordId), 1.0);
        if(nword!=null && nnword!=null && nword.wordId!=null && nnword.wordId!=null)
        	fv.addFeature(String.format("W+1<%s>_W+2<%s>", nword.wordId, nnword.wordId), 1.0);
        if(nword!=null && nnword!=null && currword!=null && nnword.wordId!=null && nword.wordId!=null && currword.wordId!=null)
        	fv.addFeature(String.format("W<%s>_W+1<%s>_W+2<%s>", currword.wordId, nword.wordId, nnword.wordId), 1.0);

        if(n3word!=null && nword!=null && nnword!=null && currword!=null && currword.wordId!=null && nword.wordId!=null && nnword.wordId!=null && n3word.wordId!=null && window==7)
        	fv.addFeature(String.format("W<%s>_W+1<%s>_W+2<%s>_W+3<%s>", currword.wordId, nword.wordId, nnword.wordId, n3word.wordId), 1.0);
        if(n3word!=null && nnword!=null && nnword.wordId!=null && n3word.wordId!=null && window==7)
        	fv.addFeature(String.format("W+2<%s>_W+3<%s>", nnword.wordId, n3word.wordId), 1.0);
        if(nword!=null && nnword!=null && n3word!=null && nword.wordId!=null && nnword.wordId!=null && n3word.wordId!=null && window==7)
        	fv.addFeature(String.format("W+1<%s>_W+2<%s>_W+3<%s>", nword.wordId, nnword.wordId, n3word.wordId), 1.0);
        if(currword!=null && n3word!=null && currword.wordId!=null && n3word.wordId!=null && window==7)
        	fv.addFeature(String.format("W<%s>_W+3<%s>", currword.wordId, n3word.wordId), 1.0);

        // affix features
        if(currword!=null && currword.wordId!=null)
      	  fv.addFeatures(getWordAffixFeatures(currword.wordId,""));
        if(nword!=null && nword.wordId != null)
      	  fv.addFeatures(getWordAffixFeatures(nword.wordId,"+1"));
        
        
        if(currword!=null && currword.wordPauseProsodic>0) fv.addFeature(String.format("PAU<%s>", currword.wordPauseProsodic), 1.0);
        if(pword!=null && pword.wordPauseProsodic>0) fv.addFeature(String.format("PAU-1<%s>", pword.wordPauseProsodic), 1.0);
        if(ppword!=null && ppword.wordPauseProsodic>0) fv.addFeature(String.format("PAU-2<%s>", ppword.wordPauseProsodic), 1.0);
        if(nword!=null && nword.wordPauseProsodic>0) fv.addFeature(String.format("PAU+1<%s>", nword.wordPauseProsodic), 1.0);
        if(nnword!=null && nnword.wordPauseProsodic>0) fv.addFeature(String.format("PAU+2<%s>", nnword.wordPauseProsodic), 1.0);
        if(currword!=null && currword.wordPauseFiller)
        	fv.addFeature(String.format("PF<%s>", currword.wordPauseFiller), 1.0);
        if(currword!=null && currword.wordPauseProsodic>0 && currword.wordPauseFiller) fv.addFeature(String.format("PAU<%s>_PF<%s>", currword.wordPauseProsodic, currword.wordPauseFiller), 1.0);
        if(currword!=null && pword!=null && currword.wordPauseProsodic>0 && pword.wordPauseProsodic >0) fv.addFeature(String.format("PAU-1<%s>_PAU<%s>", pword.wordPauseProsodic, currword.wordPauseProsodic), 1.0);
        if(currword!=null && nword!=null && currword.wordPauseProsodic>0 && nword.wordPauseProsodic >0) fv.addFeature(String.format("PAU<%s>_PAU+1<%s>", currword.wordPauseProsodic, nword.wordPauseProsodic),1.0);
        if(currword!=null && nword!=null && nnword!=null && currword.wordPauseProsodic>0 && nword.wordPauseProsodic >0 && nnword.wordPauseProsodic>0)
        	fv.addFeature(String.format("PAU<%s>_PAU+1<%s>_PAU+2<%s>", currword.wordPauseProsodic, nword.wordPauseProsodic, nnword.wordPauseProsodic), 1.0);
        if(currword!=null && currword.wordPauseProsodic>0) fv.addFeature(String.format("PAU<%s>_W<%s>", currword.wordPauseProsodic, currword.wordId), 1.0);
        if(currword!=null && nword!=null && currword.wordPauseProsodic>0 && nword.wordPauseProsodic >0) fv.addFeature(String.format("PAU<%s>_W<%s>_PAU+1<%s>", currword.wordPauseProsodic, currword.wordId, nword.wordPauseProsodic), 1.0);
        if(currword!=null && currword.wordChopBoundary) fv.addFeature(String.format("CB<%s>", currword.wordChopBoundary), 1.0);
        if(pword!=null && pword.wordChopBoundary) fv.addFeature(String.format("CB-1<%s>", pword.wordChopBoundary), 1.0);
        if(ppword!=null && ppword.wordChopBoundary) fv.addFeature(String.format("CB-2<%s>", ppword.wordChopBoundary), 1.0);
        if(nword!=null && nword.wordChopBoundary) fv.addFeature(String.format("CB+1<%s>", nword.wordChopBoundary), 1.0);
        if(currword!=null && currword.wordPauseProsodic>0 && currword.wordChopBoundary) fv.addFeature(String.format("PAU<%s>_CB<%s>", currword.wordPauseProsodic, currword.wordChopBoundary), 1.0);
        if(pword!=null && currword!=null && currword.wordPauseProsodic>0)  fv.addFeature(String.format("W-1<%s>_PAU<%s>", pword.wordId, currword.wordPauseProsodic), 1.0);
        if(currword!=null && nword!=null && currword.wordPauseProsodic>0 && nword.wordPauseProsodic >0) fv.addFeature(String.format("PAU<%s>_W+1<%s>_PAU+1<%s>", currword.wordPauseProsodic, nword.wordId, nword.wordPauseProsodic), 1.0);
        if(currword!=null && nword!=null && currword.wordPauseProsodic>0) fv.addFeature(String.format("PAU<%s>_W+1<%s>", currword.wordPauseProsodic, nword.wordId), 1.0);
        if(pword!=null && ppword!=null && currword!=null && currword.wordPauseProsodic>0 && pword.wordPauseProsodic >0 && ppword.wordPauseProsodic>0)
        	fv.addFeature(String.format("PAU-2<%s>_PAU-1<%s>_PAU<%s>", ppword.wordPauseProsodic, pword.wordPauseProsodic, currword.wordPauseProsodic), 1.0);
        if(currword!=null && currword.wordPauseProsodic>50) fv.addFeature(String.format("PROP<%s>", "SIGNIF_PAUSE"),1.0);
        if(currword!=null && currword.wordPauseProsodic>50) fv.addFeature(String.format("PROP<%s>_W<%s>", "SIGNIF_PAUSE", currword.wordId), 1.0);
        if(nword!=null && currword!=null && currword.wordPauseProsodic>50) fv.addFeature(String.format("PROP<%s>_W+1<%s>", "SIGNIF_PAUSE", nword.wordId), 1.0);

        
        if (currword!=null && currword.wordapause > 0) {
            List<Double> pauseList = new ArrayList<Double>();
            if(window==7)
            	if(p3word!=null) pauseList.add(p3word.wordapause);
            	else pauseList.add((double) -1);
            if(ppword!=null) pauseList.add(ppword.wordapause); 
            else pauseList.add((double) -1);
            if(pword!=null) pauseList.add(pword.wordapause); 
            else pauseList.add((double) -1);
            
            pauseList.add(currword.wordapause); 
            
            if(nword!=null) pauseList.add(nword.wordapause); 
            else pauseList.add((double) -1);
            if(nnword!=null) pauseList.add(nnword.wordapause); 
            else pauseList.add((double) -1);
            if(window==7)
            	if(n3word!=null) pauseList.add(n3word.wordapause);
            	else pauseList.add((double) -1);

            // create comparator for reverse order
            Comparator<Double> cmp = Collections.reverseOrder();
            Collections.sort(pauseList, cmp);
            int rank =1;
            int numpospause = 0;
            for(double pause : pauseList) {
              if(pause>0)
            	  numpospause++;
              if (pause == currword.wordapause) 
            	  {
            	      break;
            	  }
              rank++;
            }
            fv.addFeature(String.format("PAUSERANK<%s>", rank),1.0);
            if(currword!=null && currword.wordPauseProsodic>0) fv.addFeature(String.format("PAUSERANK<%s>_PAU<%s>", rank, currword.wordPauseProsodic),1.0);
            fv.addFeature(String.format("PAUSERANK<%s>_NUMPOSPAUSE<%s>", rank, numpospause),1.0);
            if(currword!=null && currword.wordChopBoundary) fv.addFeature(String.format("PAUSERANK<%s>_CB<%s>", rank, currword.wordChopBoundary),1.0);
          }
             
    	 return fv;
     }
     
     
     private WordAttributes getWordAttributes(Word word)
     {
    	 WordAttributes attributes = new WordAttributes();
    	 attributes.wordId = word.getWordId();
    	 attributes.wordPause = word.getPauseDuration();
    	 attributes.wordapause=0;
    	 attributes.wordPauseProsodic=0;
    	 if(attributes.wordPause!=-1 && attributes.wordPause!=0)
    	 {
    		 attributes.wordapause = Math.floor(attributes.wordPause);
    		 if(attributes.wordPause>100) attributes.wordPauseProsodic = 120;
    		 else attributes.wordPauseProsodic = 10  + (25 * Math.floor(attributes.wordPause/25));
    	 }
    	 attributes.wordPauseType = getPauseType(attributes.wordPause);
    	 attributes.wordPauseFiller = word.getPauseFiller();
    	 attributes.wordChopBoundary = word.getChopBoundary();
    	 
    	 return attributes;
     }
     
     
     private Map<String, Double> getWordAffixFeatures(String word, String tag) 
     {
		Map<String,Double> wordAffixFeatures = new HashMap<String,Double>();
		
		word = word.replaceAll("\\d+", "1");
		
		int wordLen = word.length();
		boolean addPrefix = true, addSuffix = true;
		int maxAffixLen = 4;
		
		// loop for maxAffixLen times
        for(int i=1;i<=maxAffixLen;i++)
        {
        	if(!addPrefix && !addSuffix)
        		break;
        	
        	if(addPrefix && i<wordLen)
        	{
        		String prefix = word.substring(0,i);
        		wordAffixFeatures.put(String.format("PFX%s<%s>", tag, prefix), 1.0);
        	}
        	else
        	{
        		addPrefix = false;
        	}
        	
        	if(addSuffix && i<wordLen)
        	{
        		String suffix = word.substring(wordLen-i);
        		wordAffixFeatures.put(String.format("SFX%s<%s>", tag, suffix), 1.0);
        	}
        	else
        		addSuffix = false;
        }
		
		return wordAffixFeatures;
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
     
	
    // na, high, low, med 
    private static String getPauseType(double pause)
    {
    	if(pause == -1) return "na";
    	else if(pause < 8) return "low";
    	else if(pause>=8 && pause< 45) return "med";
    	else return "high";
    	
    }
     
	
     
}
