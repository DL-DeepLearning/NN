package org.bbn.sbd.training.perceptron;

import org.bbn.sbd.datastructures.*;

import java.util.*;

public class ViterbiPerceptron<K> 
{
	
	boolean debug = false;
	
	Map<String, FeatureWeight> weightPointerForAveraging = new HashMap<String, FeatureWeight>();
	
	// static inner class
	static class PossibleState
	{
		int wordIndex;
		int currlabel;
		double score;
		
		PossibleState prev;
		
		// constructor
		public PossibleState(int wordIndex, int currlabel, double score)
		{
			this.wordIndex = wordIndex;
			this.currlabel = currlabel;
			this.score = score;
			prev = null;
		}
		
		// getters
		public int getWordIndex()
		{
			return wordIndex;
		}
		
		public double getScore()
		{
			return score;
		}
		
		public int getLabel()
		{
			return currlabel;
		}
		
		public PossibleState getPrevious()
		{
			return prev;
		}
		
		// setters
		public void setPrevious(PossibleState node)
		{
			prev = node;
		}

		public void setScore(double score) 
		{
			this.score = score;	
		}
	}
	// end of inner class
    
    Map<K,Map<Integer,FeatureWeight>> weights;
	
	double sbBias = Math.random()*0.06;
	double nsbBias = Math.random()*0.06;
	
	double learningRate = 0.1;
	
	// constructors
	public ViterbiPerceptron(double initialSbBias, double initialNsbBias, double learningRate)
	{
		this.sbBias = initialSbBias;
		this.nsbBias = initialNsbBias;
    	this.learningRate = learningRate;
    	
    	weights = new HashMap<K,Map<Integer,FeatureWeight>>();
	}
	
	public ViterbiPerceptron()
    {
		weights = new HashMap<K,Map<Integer,FeatureWeight>>();
    }
	
	
	/**
     * returns null if unknown feature
     * @param feature
     * @return
     */
    public Map<Integer,FeatureWeight> getWeight(K feature)
    {
    	if(weights.containsKey(feature))
    		return weights.get(feature);
    	else return null;
    }
    
    
    /**
     * 
     * @param turn
     * @return
     */
    public void train(Turn turn, List<SparseVector<K>> wordFeatures)
    {
    	List<Integer> truelabels = getTrueLabels(turn);    
    	List<Integer> hypothesis = decode(turn, wordFeatures, PerceptronMode.TRAIN);
    	for(int i=0; i<turn.getWords().size()-1; i++)
    	{
    		if((truelabels.get(i) - hypothesis.get(i)) != 0)
    		{
    			updateWeights(wordFeatures.get(i), hypothesis.get(i), truelabels.get(i));
    		}
    	}
    	
    	// now do weights averaging
    	for(String key : weightPointerForAveraging.keySet())
    	{
    		FeatureWeight featWeight = weightPointerForAveraging.get(key);
    		featWeight.setAvgWeightNumerator(featWeight.getAvgWeightNumerator() + featWeight.getRawWeight());
    		featWeight.setAvgWeightDenominator(featWeight.getAvgWeightDenominator() + 1);
    		
    	}
    				
    	weightPointerForAveraging.clear();
    }
    
    /**
     * 
     * 
     * @param turn
     * @return
     */
	private List<Integer> getTrueLabels(Turn turn) {
		List<Integer> truelabels = new ArrayList<Integer>();
		for(Word word : turn.getWords())
		{
			truelabels.add(word.getLabel());
		}
		
		return truelabels;
	}

	/**
	 * 
	 * @param turn
	 * @return
	 */
	public List<Integer> decode(Turn turn, List<SparseVector<K>> features, PerceptronMode mode)
	{
		List<Integer> hypothesis = new ArrayList<Integer>();
		
		// TODO
		List<PossibleState> prevWordPossibleStates = null;
		List<PossibleState> currWordPossibleStates = new ArrayList<PossibleState>();
		
		for(int i=0; i<turn.getWords().size(); i++)
		{
			
			if(debug)
			{
				System.out.println("------------------------------------------------------------------");
				System.out.println("Word: " + turn.get(i).getWordId());
				System.out.println("------------------------------------------------------------------");
			}
				
			currWordPossibleStates.add(new PossibleState(i, -1, 0));
			currWordPossibleStates.add(new PossibleState(i, 1, 0));
			//currWordPossibleStates.add(new PossibleState(i, -1, 0));
			
			boolean firstcurrstate = true;
			for(PossibleState currWordPossibleState : currWordPossibleStates)
			{
				int state = currWordPossibleState.getLabel();
				Double score = 0.0, maxscore = 0.0;
				PossibleState previous = null;
				
				if(debug)
				{
					if(firstcurrstate)
					{
						System.out.println("------------------------------------------------------------------");
						System.out.println("Curr|Prev" + "\t" + "-1" + "\t" + "1");
						System.out.println("------------------------------------------------------------------");
					}
					
					System.out.print(state + "\t");
				}
				
				if(prevWordPossibleStates!=null && !prevWordPossibleStates.isEmpty())
				{
					for(PossibleState node : prevWordPossibleStates)
					{
						
						if(debug)
						{
							if(node.getLabel()==-1)
								System.out.print(node.getScore() + "\t");
							else
							{
								System.out.print(node.getScore());
								System.out.println();
							}
								
						}
						
						score = node.getScore();
						
						// make on the fly previous label features
						//SparseVector<K> wordfeatures = new SparseVector<K>(); 
						//wordfeatures.addFeature((K)String.format("C-1_%s_C-2_%s", state, node.getLabel()), 1.0);
						//wordfeatures.addFeature((K)String.format("C-2_%s", node.getLabel()), 1.0);
						//wordfeatures.addFeature((K)String.format("C-1_%s", state), 1.0);
						//score += getFeaturesScore(wordfeatures, state, PerceptronMode.TRAIN);
						
						if(previous == null || score > maxscore)
						{
							maxscore = score;
							previous = node;
						}
					}
				}
				else
				{
					if(debug)
					{
						System.out.print("null\tnull");
						System.out.println();
					}
				}

			   SparseVector<K> wordfeatures = features.get(i);
			   double featuresScore = getFeaturesScore(wordfeatures, state, mode);
			   if(debug)
			   {
				   System.out.println("------------------------------------------------------------------");
				   System.out.println("curr state: " + state + " obs score: " + featuresScore + " prev: " + (previous==null?"null":previous.getLabel()));
				   System.out.println("------------------------------------------------------------------");
			   }
			   
			   maxscore += featuresScore;

				currWordPossibleState.setScore(maxscore);
				currWordPossibleState.setPrevious(previous);
				
				if(firstcurrstate)
					firstcurrstate=false;
			}
			
			prevWordPossibleStates = new ArrayList<PossibleState>();
			for(PossibleState node : currWordPossibleStates)
			{
				prevWordPossibleStates.add(node);
			}
			
			currWordPossibleStates.clear();
		}
		
		hypothesis = backtrace(prevWordPossibleStates.get(1));
		return hypothesis;
	}
	
	private double getFeaturesScore(SparseVector<K> wordfeatures, int label, PerceptronMode mode) 
	{
		double score = 0.0;
		
		/*if(label==1)
			score+=sbBias;
		else
			score+=nsbBias;*/
		
		for(K feature : wordfeatures.getFeatures().keySet())
		{
			if(weights.containsKey(feature))
			{
				Map<Integer, FeatureWeight> featurescores = weights.get(feature);
				if(featurescores.containsKey(label))
				{
					if(mode == PerceptronMode.TRAIN)
						score += featurescores.get(label).getRawWeight();
					else
					    score += (featurescores.get(label).getAvgWeightNumerator()/featurescores.get(label).getAvgWeightDenominator());
				}
			}
		}
		return score;
	}

	/**
	 * 
	 * @param turn
	 * @param truelabels
	 * @return
	 */
	private void updateWeights(SparseVector<K> features, int hypothesis, int truelabel) {
		for(K feature : features.getFeatures().keySet())
		{
			if(this.weights.containsKey(feature))
			{
				Map<Integer, FeatureWeight> featurescores = this.weights.get(feature);
				if(featurescores.containsKey(hypothesis))
				{
					FeatureWeight featureWeight = featurescores.get(hypothesis);
					featureWeight.setRawWeight(featurescores.get(hypothesis).getRawWeight()-1);
					featurescores.put(hypothesis, featureWeight);
				}
				else
				{
					FeatureWeight featureWeight = new FeatureWeight(-1, 0.0, 0.0);
					featurescores.put(hypothesis, featureWeight);
				}
				
				if(featurescores.containsKey(truelabel))
				{
					FeatureWeight featureWeight = featurescores.get(truelabel);
					featureWeight.setRawWeight(featurescores.get(truelabel).getRawWeight()+1);
					featurescores.put(truelabel, featureWeight);
				}
				else
				{
					FeatureWeight featureWeight = new FeatureWeight(1, 0.0, 0.0);
					featurescores.put(truelabel, featureWeight);
				}
				
				//weights.put(feature, featurescores);
			}
			else
			{
				Map<Integer, FeatureWeight> featurescores = new HashMap<Integer, FeatureWeight>();
				
				FeatureWeight featureWeight = new FeatureWeight(-1, 0.0, 0.0);
				featurescores.put(hypothesis, featureWeight);
				
				featureWeight = new FeatureWeight(1, 0.0, 0.0);
				featurescores.put(truelabel, featureWeight);
				
				this.weights.put(feature, featurescores);
			}

						
			/*if(hypothesis==1)
			{
				sbBias-=1;
				nsbBias+=1;
			}
			else
			{
				sbBias+=1;
				nsbBias-=1;
			}*/
			
			String thisfeat = (String)feature + " " + hypothesis;
			if(!weightPointerForAveraging.containsKey(thisfeat))
				weightPointerForAveraging.put(thisfeat, weights.get(feature).get(hypothesis));
			
			thisfeat = (String)feature + " " + truelabel;
			if(!weightPointerForAveraging.containsKey(thisfeat))
				weightPointerForAveraging.put(thisfeat, weights.get(feature).get(truelabel));
			
		}
	}
	
	
	/**
	 * 
	 */
	private List<Integer> backtrace(PossibleState last)
	{
		List<Integer> hypothesis = new ArrayList<Integer>();
		
		PossibleState curr = last;
		while(curr != null)
		{
			hypothesis.add(curr.getLabel());
			curr = curr.getPrevious();
		}
		
		Collections.reverse(hypothesis);		
		return hypothesis;
	}
	
	/**
     * return weights as string
     * tab separated featureId, weight
     * one featureId per line
     * @return
     */
    @SuppressWarnings("unused")
	private String weightsToString()
    {
    	// TODO
    	return null;
    }
    
    
    /**
     * save model weights to file system
     * @param modelpath
     */
	public void saveModel(String modelpath)
	{
		//TODO
	}
	
	/**
	 * load model from file
	 * @param modelpath
	 */
	public void loadModel(String modelpath)
    {
		//TODO
    }
	
}
