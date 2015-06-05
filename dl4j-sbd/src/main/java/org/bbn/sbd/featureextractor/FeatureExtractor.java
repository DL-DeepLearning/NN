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
    	 
    	 // get word attributes
    	 String p3wordId = wordIndex-3>0&&wordIndex-3<turnLength?turn.get(wordIndex-3).getWordId():null;
    	 int p3wordLabel = wordIndex-3>0&&wordIndex-3<turnLength?turn.get(wordIndex-3).getLabel():0;
    	 double p3wordPause = wordIndex-3>0&&wordIndex-3<turnLength?turn.get(wordIndex-3).getPauseDuration():-1;
    	 double p3wordapause=0, p3wordPauseProsodic=0;
    	 if(p3wordPause!=-1 && p3wordPause!=0)
    	 {
    		 p3wordapause = Math.floor(p3wordPause);
    		 if(p3wordPause>100) p3wordPauseProsodic = 120;
    		 else p3wordPauseProsodic = 10  + (25 * Math.floor(p3wordPause/25));
    	 }
    	 
    	 String p3wordPauseType = getPauseType(p3wordPause);
    	 boolean p3wordPauseFiller = wordIndex-3>0&&wordIndex-3<turnLength&&turn.get(wordIndex-3).getPauseFiller();
    	 boolean p3wordChopBoundary = wordIndex-3>0&&wordIndex-3<turnLength&&turn.get(wordIndex-3).getChopBoundary();
    	 
    	 String ppwordId = wordIndex-2>0&&wordIndex-2<turnLength?turn.get(wordIndex-2).getWordId():null;
    	 int ppwordLabel = wordIndex-2>0&&wordIndex-2<turnLength?turn.get(wordIndex-2).getLabel():0;
    	 double ppwordPause = wordIndex-2>0&&wordIndex-2<turnLength?turn.get(wordIndex-2).getPauseDuration():-1;
    	 double ppwordapause=0, ppwordPauseProsodic=0;
    	 if(ppwordPause!=-1 && ppwordPause!=0)
    	 {
    		 ppwordapause = Math.floor(ppwordPause);
    		 if(ppwordPause>100) ppwordPauseProsodic = 120;
    		 else ppwordPauseProsodic = 10  + (25 * Math.floor(ppwordPause/25));
    	 }
    	 
    	 String ppwordPauseType = getPauseType(ppwordPause);
    	 boolean ppwordPauseFiller = wordIndex-2>0&&wordIndex-2<turnLength&&turn.get(wordIndex-2).getPauseFiller();
    	 boolean ppwordChopBoundary = wordIndex-2>0&&wordIndex-2<turnLength&&turn.get(wordIndex-2).getChopBoundary();
    	 
    	 String pwordId = wordIndex-1>0&&wordIndex-1<turnLength?turn.get(wordIndex-1).getWordId():null;
    	 int pwordLabel = wordIndex-1>0&&wordIndex-1<turnLength?turn.get(wordIndex-1).getLabel():0;
    	 double pwordPause = wordIndex-1>0&&wordIndex-1<turnLength?turn.get(wordIndex-1).getPauseDuration():-1;
    	 double pwordapause=0, pwordPauseProsodic=0;
    	 if(pwordPause!=-1 && pwordPause!=0)
    	 {
    		 pwordapause = Math.floor(pwordPause);
    		 if(pwordPause>100) pwordPauseProsodic = 120;
    		 else pwordPauseProsodic = 10  + (25 * Math.floor(pwordPause/25));
    	 }
    	 String pwordPauseType = getPauseType(pwordPause);
    	 boolean pwordPauseFiller = wordIndex-1>0&&wordIndex-1<turnLength&&turn.get(wordIndex-1).getPauseFiller();
    	 boolean pwordChopBoundary = wordIndex-1>0&&wordIndex-1<turnLength&&turn.get(wordIndex-1).getChopBoundary();
    	 
    	 String wordId = word!=null?word.getWordId():null;
    	 double wordPause = word!=null?word.getPauseDuration():-1;
    	 double wordapause=0, wordPauseProsodic=0;
    	 if(wordPause!=-1 && wordPause!=0)
    	 {
    		 wordapause = Math.floor(wordPause);
    		 if(wordPause>100) wordPauseProsodic = 120;
    		 else wordPauseProsodic = 10  + (25 * Math.floor(wordPause/25));
    	 }
    	 String wordPauseType = getPauseType(wordPause);
    	 boolean wordPauseFiller = word!=null&&word.getPauseFiller();
    	 boolean wordChopBoundary = word!=null&&word.getChopBoundary();
    	 
    	 String nwordId = wordIndex+1>0&&wordIndex+1<turnLength?turn.get(wordIndex+1).getWordId():null;
    	 double nwordPause = wordIndex+1>0&&wordIndex+1<turnLength?turn.get(wordIndex+1).getPauseDuration():-1;
    	 double nwordapause=0, nwordPauseProsodic=0;
    	 if(nwordPause!=-1 && nwordPause!=0)
    	 {
    		 nwordapause = Math.floor(nwordPause);
    		 if(nwordPause>100) nwordPauseProsodic = 120;
    		 else nwordPauseProsodic = 10  + (25 * Math.floor(nwordPause/25));
    	 }
    	 String nwordPauseType = getPauseType(nwordPause);
    	 boolean nwordPauseFiller = wordIndex+1>0&&wordIndex+1<turnLength&&turn.get(wordIndex+1).getPauseFiller();
    	 boolean nwordChopBoundary = wordIndex+1>0&&wordIndex+1<turnLength&&turn.get(wordIndex+1).getChopBoundary();
    	 
    	 String nnwordId = wordIndex+2>0&&wordIndex+2<turnLength?turn.get(wordIndex+2).getWordId():null;
    	 double nnwordPause = wordIndex+2>0&&wordIndex+2<turnLength?turn.get(wordIndex+2).getPauseDuration():-1;
    	 double nnwordapause=0, nnwordPauseProsodic=0;
    	 if(nnwordPause!=-1 && nnwordPause!=0)
    	 {
    		 nnwordapause = Math.floor(nnwordPause);
    		 if(nnwordPause>100) nnwordPauseProsodic = 120;
    		 else nnwordPauseProsodic = 10  + (25 * Math.floor(nnwordPause/25));
    	 }
    	 String nnwordPauseType = getPauseType(nnwordPause);
    	 boolean nnwordPauseFiller = wordIndex+2>0&&wordIndex+2<turnLength&&turn.get(wordIndex+2).getPauseFiller();
    	 boolean nnwordChopBoundary = wordIndex+2>0&&wordIndex+2<turnLength&&turn.get(wordIndex+2).getChopBoundary();
    	 
    	 String n3wordId = wordIndex+3>0&&wordIndex+3<turnLength?turn.get(wordIndex+3).getWordId():null;
    	 double n3wordPause = wordIndex+3>0&&wordIndex+3<turnLength?turn.get(wordIndex+3).getPauseDuration():-1;
    	 double n3wordapause=0, n3wordPauseProsodic=0;
    	 if(n3wordPause!=-1 && n3wordPause!=0)
    	 {
    		 n3wordapause = Math.floor(n3wordPause);
    		 if(n3wordPause>100) n3wordPauseProsodic = 120;
    		 else n3wordPauseProsodic = 10  + (25 * Math.floor(n3wordPause/25));
    	 }
    	 String n3wordPauseType = getPauseType(n3wordPause);
    	 boolean n3wordPauseFiller = wordIndex+3>0&&wordIndex+3<turnLength&&turn.get(wordIndex+3).getPauseFiller();
    	 boolean n3wordChopBoundary = wordIndex+3>0&&wordIndex+3<turnLength&&turn.get(wordIndex+3).getChopBoundary();
    	 
    	 // add features
    	 
    	 /** previous word labels */
    	// fv.addFeature(String.format("C-3<%s>", p3wordLabel), 1.0);
    	 //fv.addFeature(String.format("C-2<%s>", ppwordLabel), 1.0);
    	 //fv.addFeature(String.format("C-1<%s>", pwordLabel), 1.0);
    	 //fv.addFeature(String.format("C-3<%s>_C-2<%s>", p3wordLabel, ppwordLabel), 1.0);
 		 //fv.addFeature(String.format("C-2<%s>_C-1<%s>", ppwordLabel, pwordLabel), 1.0);
 		 //fv.addFeature(String.format("C-3<%s>_C-1<%s>", p3wordLabel, pwordLabel), 1.0);
 		 //fv.addFeature(String.format("C-3<%s>_C-2<%s>_C-1<%s>", p3wordLabel, ppwordLabel, pwordLabel), 1.0);
 		 //fv.addFeature(String.format("C-4<%s>_C-3<%s>_C-2<%s>_C-1<%s>", p4wordLabel, p3wordLabel, ppwordLabel, pwordLabel), 1.0);
 		
 		 /** word-label combinations */
 		//fv.addFeature(String.format("W-1<%s>_C-1<%s>", pwordId, pwordLabel), 1.0);
 		//fv.addFeature(String.format("W-1<%s>_C-1<%s>_W<%s>", pwordId, pwordLabel, word), 1.0);
 		//fv.addFeature(String.format("C-1<%s>_W+1<%s>", pwordLabel, nwordId), 1.0);
 		//fv.addFeature(String.format("W-2<%s>_W-1<%s>_C-1<%s>_W<%s>", ppwordId, pwordId, pwordLabel, wordId), 1.0);
 		//fv.addFeature(String.format("W<%s>_W+1<%s>_W+2<%s>_C-1<%s>", wordId, nwordId, nnwordId, pwordLabel), 1.0);
 		 
    	 /** pause-label combinations */
 		//fv.addFeature(String.format("C-1<%s>_PAU+1<%s>", pwordLabel, nwordPauseType), 1.0);
		//fv.addFeature(String.format("C-2<%s>_C-1<%s>_PAU+1<%s>", ppwordLabel, pwordLabel, nwordPauseType), 1.0);
		//fv.addFeature(String.format("C-1<%s>_PAU+1<%s>_PAU+2<%s>", pwordLabel, nwordPauseType, nnwordPauseType), 1.0);
		//fv.addFeature(String.format("C-1<%s>_PAU<%s>", pwordLabel, wordPauseType), 1.0);
		//fv.addFeature(String.format("C-1<%s>_PAU+2<%s>", pwordLabel, nnwordPauseType), 1.0);
		//fv.addFeature(String.format("C-3<%s>_C-2<%s>C-1<%s>_PAU+1<%s>", p3wordLabel, ppwordLabel, pwordLabel, nwordPauseType), 1.0);
		//fv.addFeature(String.format("C-1<%s>_PAU<%s>_PAU+1<%s>_PAU+2<%s>_PAU+3<%s>", pwordLabel, wordPauseType, nwordPauseType, nnwordPauseType, n3wordPauseType), 1.0);
   	    
 		 /** pause-word combinations */
 		//fv.addFeature(String.format("PAU<%s>_W+1<%s>", wordPauseType, nwordId), 1.0);
 		 
 		/** word features */
 	    //fv.addFeature(String.format("w-3<%s>_w-2<%s>_w-1<%s>_w<%s>_w+1<%s>_w+2<%s>_w+3<%s>", p3wordId, ppwordId, pwordId, wordId, nwordId, nnwordId, n3wordId), 1.0);
    	//fv.addFeature(String.format("w-1<%s>_w<%s>_w+1<%s>",pwordId, wordId, nwordId), 1.0);
    	//fv.addFeature(String.format("w<%s>", wordId), 1.0);
    	//fv.addFeature(String.format("w-2<%s>_w-1<%s>_w<%s>_w+1<%s>_w+2", ppwordId, pwordId, wordId, nwordId, nnwordId), 1.0);
    	//fv.addFeature(String.format("w-3<%s>_w-1<%s>_w+1<%s>_w+3<%s>", p3wordId, pwordId, nwordId, n3wordId), 1.0);
    	//fv.addFeature(String.format("w-1<%s>_w<%s>", pwordId, wordId), 1.0);
    	//fv.addFeature(String.format("w<%s>_w+1<%s>", wordId, nwordId), 1.0);
 
 		/** pause features */		
 	    //fv.addFeature(String.format("PAU-3<%s>", p3wordPauseType), 1.0);
        //fv.addFeature(String.format("PAU-2<%s>", ppwordPauseType), 1.0);
        //fv.addFeature(String.format("PAU-1<%s>", pwordPauseType), 1.0);
        //fv.addFeature(String.format("PAU<%s>", wordPauseType), 1.0);
        //fv.addFeature(String.format("PAU+1<%s>", nwordPauseType), 1.0);
        //fv.addFeature(String.format("PAU+2<%s>", nnwordPauseType), 1.0);
        //fv.addFeature(String.format("PAU+3<%s>", n3wordPauseType), 1.0);
        /*fv.addFeature(String.format("PAU-3<%s>_PAU-2<%s>", p3wordPauseType, ppwordPauseType), 1.0);
        fv.addFeature(String.format("PAU-2<%s>_PAU-1<%s>", ppwordPauseType, pwordPauseType), 1.0);
        fv.addFeature(String.format("PAU-1<%s>_PAU<%s>", pwordPauseType, wordPauseType), 1.0);
        fv.addFeature(String.format("PAU<%s>_PAU+1<%s>", wordPauseType, nwordPauseType), 1.0);
        fv.addFeature(String.format("PAU+1<%s>_PAU+2<%s>", nwordPauseType, nnwordPauseType), 1.0);
        fv.addFeature(String.format("PAU+2<%s>_PAU+3<%s>", nnwordPauseType, n3wordPauseType), 1.0);
        fv.addFeature(String.format("PAU-3<%s>_PAU-2<%s>_PAU-1<%s>", p3wordPauseType, ppwordPauseType, pwordPauseType), 1.0);
        fv.addFeature(String.format("PAU-2<%s>_PAU-1<%s>_PAU<%s>", ppwordPauseType, pwordPauseType, wordPauseType), 1.0);
        fv.addFeature(String.format("PAU-1<%s>_PAU<%s>_PAU+1<%s>", pwordPauseType, wordPauseType, nwordPauseType), 1.0);
        fv.addFeature(String.format("PAU<%s>_PAU+1<%s>_PAU+2<%s>", wordPauseType, nwordPauseType, nnwordPauseType), 1.0);
        fv.addFeature(String.format("PAU+1<%s>_PAU+2<%s>_PAU+3<%s>", nwordPauseType, nnwordPauseType, n3wordPauseType), 1.0);*/
        
 		
        // all features as in Amit's script
        if(p3wordId!=null && ppwordId!=null && pwordId!=null && wordId!=null && window==7)
        	fv.addFeature(String.format("W-3<%s>_W-2<%s>_W-1<%s>_W<%s>", p3wordId, ppwordId, pwordId, wordId), 1.0);
        if(p3wordId!=null && ppwordId!=null && pwordId!=null && window==7)
        	fv.addFeature(String.format("W-3<%s>_W-2<%s>_W-1<%s>", p3wordId, ppwordId, pwordId), 1.0);
        if(p3wordId!=null && wordId!=null && window==7)
        	fv.addFeature(String.format("W-3<%s>_W<%s>", p3wordId, wordId), 1.0);
        if(p3wordId!=null && ppwordId!=null && window==7)
        	fv.addFeature(String.format("W-3<%s>_W-2<%s>", p3wordId, ppwordId), 1.0);
        if(ppwordId!=null && pwordId!=null)
            fv.addFeature(String.format("W-2<%s>_W-1<%s>", ppwordId, pwordId), 1.0);

        if(ppwordId!=null && pwordId!=null && wordId!=null)
        	fv.addFeature(String.format("W-2<%s>_W-1<%s>_W<%s>", ppwordId, pwordId, wordId), 1.0);
        if(pwordId!=null && wordId!=null)
        	fv.addFeature(String.format("W-1<%s>_W<%s>", pwordId, wordId), 1.0);
        if(pwordId!=null)
        	fv.addFeature(String.format("W-1<%s>", pwordId), 1.0);
        if(pwordId!=null && nwordId!=null)
        	fv.addFeature(String.format("W-1<%s>_W+1<%s>", pwordId, nwordId), 1.0);
        if(wordId!=null)
        	fv.addFeature(String.format("W<%s>", wordId),1.0);
        if(pwordId!=null && wordId!=null && nwordId!=null)
        	fv.addFeature(String.format("W-1<%s>_W<%s>_W+1<%s>", pwordId, wordId, nwordId), 1.0);
        if(nwordId!=null)
        	fv.addFeature(String.format("W+1<%s>", nwordId), 1.0);
        if(nwordId!=null && wordId!=null)
        	fv.addFeature(String.format("W<%s>_W+1<%s>", wordId, nwordId), 1.0);
        if(nwordId!=null && nnwordId!=null)
        	fv.addFeature(String.format("W+1<%s>_W+2<%s>", nwordId, nnwordId), 1.0);
        if(nnwordId!=null && nwordId!=null && wordId!=null)
        	fv.addFeature(String.format("W<%s>_W+1<%s>_W+2<%s>", wordId, nwordId, nnwordId), 1.0);

        if(wordId!=null && nwordId!=null && nnwordId!=null && n3wordId!=null && window==7)
        	fv.addFeature(String.format("W<%s>_W+1<%s>_W+2<%s>_W+3<%s>", wordId, nwordId, nnwordId, n3wordId), 1.0);
        if(nnwordId!=null && n3wordId!=null && window==7)
        	fv.addFeature(String.format("W+2<%s>_W+3<%s>", nnwordId, n3wordId), 1.0);
        if(nwordId!=null && nnwordId!=null && n3wordId!=null && window==7)
        	fv.addFeature(String.format("W+1<%s>_W+2<%s>_W+3<%s>", nwordId, nnwordId, n3wordId), 1.0);
        if(wordId!=null && n3wordId!=null && window==7)
        	fv.addFeature(String.format("W<%s>_W+3<%s>", wordId, n3wordId), 1.0);
        
        //fv.addFeature(String.format("PT<%s>", wordPauseType), 1.0);
        //fv.addFeature(String.format("PT-1<%s>", pwordPauseType), 1.0);
        //fv.addFeature(String.format("PT-2<%s>", ppwordPauseType), 1.0);
        //fv.addFeature(String.format("PT+1<%s>", nwordPauseType), 1.0);
        //fv.addFeature(String.format("PT+2<%s>", nnwordPauseType), 1.0);
        /*fv.addFeature(String.format("PF<%s>", wordPauseFiller), 1.0);
        fv.addFeature(String.format("PAU<%s>_PF<%s>", wordPauseType, wordPauseFiller), 1.0);
        fv.addFeature(String.format("PAU-1<%s>_PAU<%s>", pwordPauseType, wordPauseType), 1.0);
        fv.addFeature(String.format("PAU<%s>_PAU+1<%s>", wordPauseType, nwordPauseType),1.0);
        fv.addFeature(String.format("PAU<%s>_PAU+1<%s>_PAU+2<%s>", wordPauseType, nwordPauseType, nnwordPauseType), 1.0);
        fv.addFeature(String.format("PAU<%s>_W<%s>", wordPauseType, wordId), 1.0);
        fv.addFeature(String.format("PAU<%s>_W<%s>_PAU+1<%s>", wordPauseType, wordId, nwordPauseType), 1.0);
        fv.addFeature(String.format("CB<%s>", wordChopBoundary), 1.0);
        fv.addFeature(String.format("CB-1<%s>", pwordChopBoundary), 1.0);
        fv.addFeature(String.format("CB-2<%s>", ppwordChopBoundary), 1.0);
        fv.addFeature(String.format("CB+1<%s>", nwordChopBoundary), 1.0);
        fv.addFeature(String.format("PAU<%s>_CB<%s>", wordPauseType, wordChopBoundary), 1.0);
        fv.addFeature(String.format("W-1<%s>_PAU<%s>", pwordId, wordPauseType), 1.0);
        //fv.addFeature(String.format("P<%s>_PAU<%s>"), 1.0);
        //fv.addFeature(String.format("P-1<%s>_P<%s>_PAU<%s>"), 1.0);
        //fv.addFeature(String.format("P<%s>_PF<%s>"), 1.0);
        fv.addFeature(String.format("PAU<%s>_W+1<%s>_PAU+1<%s>", wordPauseType, nwordId, nwordPauseType), 1.0);
        fv.addFeature(String.format("PAU<%s>_W+1<%s>", wordPauseType, nwordId), 1.0);
        fv.addFeature(String.format("PAU-2<%s>_PAU-1<%s>_PAU<%s>", ppwordPauseType, pwordPauseType, wordPauseType), 1.0);
        //fv.addFeature(String.format("PROP<%s>", "SIGNIF_PAUSE"),1.0);
        //fv.addFeature(String.format("PROP<%s>_W<%s>", "SIGNIF_PAUSE"), 1.0);
        //fv.addFeature(String.format("PROP<%s>_W+1<%s>", "SIGNIF_PAUSE"), 1.0);
        //fv.addFeature(String.format("PROP<%s>_P<%s>", "SIGNIF_PAUSE"), 1.0);
        */
        
        // affix features
        if(wordId!=null)
      	  fv.addFeatures(getWordAffixFeatures(wordId,""));
        if(nwordId != null)
      	  fv.addFeatures(getWordAffixFeatures(nwordId,"+1"));
        
        
        if(wordPauseProsodic>0) fv.addFeature(String.format("PAU<%s>", wordPauseProsodic), 1.0);
        if(pwordPauseProsodic>0) fv.addFeature(String.format("PAU-1<%s>", pwordPauseProsodic), 1.0);
        if(ppwordPauseProsodic>0) fv.addFeature(String.format("PAU-2<%s>", ppwordPauseProsodic), 1.0);
        if(nwordPauseProsodic>0) fv.addFeature(String.format("PAU+1<%s>", nwordPauseProsodic), 1.0);
        if(nnwordPauseProsodic>0) fv.addFeature(String.format("PAU+2<%s>", nnwordPauseProsodic), 1.0);
        if(wordPauseFiller)
        	fv.addFeature(String.format("PF<%s>", wordPauseFiller), 1.0);
        if(wordPauseProsodic>0 && wordPauseFiller) fv.addFeature(String.format("PAU<%s>_PF<%s>", wordPauseProsodic, wordPauseFiller), 1.0);
        if(wordPauseProsodic>0 && pwordPauseProsodic >0) fv.addFeature(String.format("PAU-1<%s>_PAU<%s>", pwordPauseProsodic, wordPauseProsodic), 1.0);
        if(wordPauseProsodic>0 && nwordPauseProsodic >0) fv.addFeature(String.format("PAU<%s>_PAU+1<%s>", wordPauseProsodic, nwordPauseProsodic),1.0);
        if(wordPauseProsodic>0 && nwordPauseProsodic >0 && nnwordPauseProsodic>0)
        	fv.addFeature(String.format("PAU<%s>_PAU+1<%s>_PAU+2<%s>", wordPauseProsodic, nwordPauseProsodic, nnwordPauseProsodic), 1.0);
        if(wordPauseProsodic>0) fv.addFeature(String.format("PAU<%s>_W<%s>", wordPauseProsodic, wordId), 1.0);
        if(wordPauseProsodic>0 && nwordPauseProsodic >0) fv.addFeature(String.format("PAU<%s>_W<%s>_PAU+1<%s>", wordPauseProsodic, wordId, nwordPauseProsodic), 1.0);
        if(wordChopBoundary) fv.addFeature(String.format("CB<%s>", wordChopBoundary), 1.0);
        if(pwordChopBoundary) fv.addFeature(String.format("CB-1<%s>", pwordChopBoundary), 1.0);
        if(ppwordChopBoundary) fv.addFeature(String.format("CB-2<%s>", ppwordChopBoundary), 1.0);
        if(nwordChopBoundary) fv.addFeature(String.format("CB+1<%s>", nwordChopBoundary), 1.0);
        if(wordPauseProsodic>0 && wordChopBoundary) fv.addFeature(String.format("PAU<%s>_CB<%s>", wordPauseProsodic, wordChopBoundary), 1.0);
        if(wordPauseProsodic>0)  fv.addFeature(String.format("W-1<%s>_PAU<%s>", pwordId, wordPauseProsodic), 1.0);
        if(wordPauseProsodic>0 && nwordPauseProsodic >0) fv.addFeature(String.format("PAU<%s>_W+1<%s>_PAU+1<%s>", wordPauseProsodic, nwordId, nwordPauseProsodic), 1.0);
        if(wordPauseProsodic>0) fv.addFeature(String.format("PAU<%s>_W+1<%s>", wordPauseProsodic, nwordId), 1.0);
        if(wordPauseProsodic>0 && pwordPauseProsodic >0 && ppwordPauseProsodic>0)
        	fv.addFeature(String.format("PAU-2<%s>_PAU-1<%s>_PAU<%s>", ppwordPauseProsodic, pwordPauseProsodic, wordPauseProsodic), 1.0);
        if(wordPauseProsodic>50) fv.addFeature(String.format("PROP<%s>", "SIGNIF_PAUSE"),1.0);
        if(wordPauseProsodic>50) fv.addFeature(String.format("PROP<%s>_W<%s>", "SIGNIF_PAUSE", wordId), 1.0);
        if(wordPauseProsodic>50) fv.addFeature(String.format("PROP<%s>_W+1<%s>", "SIGNIF_PAUSE", nwordId), 1.0);

        
        if (wordapause > 0) {
            List<Double> pauseList = new ArrayList<Double>();
            if(window==7)
            	pauseList.add(p3wordapause);
            pauseList.add(ppwordapause);
            pauseList.add(pwordapause);
            pauseList.add(wordapause);
            pauseList.add(nwordapause);
            pauseList.add(nnwordapause);
            if(window==7)
            	pauseList.add(n3wordapause);

            // create comparator for reverse order
            Comparator<Double> cmp = Collections.reverseOrder();
            Collections.sort(pauseList, cmp);
            int rank =1;
            int numpospause = 0;
            for(double pause : pauseList) {
              if(pause>0)
            	  numpospause++;
              if (pause == wordapause) 
            	  {
            	      break;
            	  }
              rank++;
            }
            fv.addFeature(String.format("PAUSERANK<%s>", rank),1.0);
            if(wordPauseProsodic>0) fv.addFeature(String.format("PAUSERANK<%s>_PAU<%s>", rank, wordPauseProsodic),1.0);
            fv.addFeature(String.format("PAUSERANK<%s>_NUMPOSPAUSE<%s>", rank, numpospause),1.0);
            if(wordChopBoundary) fv.addFeature(String.format("PAUSERANK<%s>_CB<%s>", rank, wordChopBoundary),1.0);
          }
             
    	 return fv;
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
