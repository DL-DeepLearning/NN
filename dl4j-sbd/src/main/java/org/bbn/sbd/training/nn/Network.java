package org.bbn.sbd.training.nn;

import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.xml.sax.*;
import org.w3c.dom.*;

/**
 * The neural network defined below is composed of a set of scores and weights.
 * The network differentiates between input, hidden and output layers. This is mainly to allow 
 * customizing the type of keys in the input and output layers. In other words,
 * the node indexes for input and output layers may be strings instead of numerical, thus
 * allowing for sparse representation. 
 * @author ssriniva
 *
 * @param <I>
 * @param <H>
 * @param <O>
 */
@SuppressWarnings("unused")
public class Network<I,O> {
	
	// static variables to be set from the config file
	private static int sigmoidTableSize = 1000;
	
	private static Map<Integer, Double> sigmoidTable;
	
	private static double maxExp = 6.0;
	
	private static double learning_rate = 0.1;
	
	// NETWORK PARAMS
	int nHiddenLayers;

	Map<Integer, Integer> nNodesPerHiddenLayer; 

	int nOutputNodes;
	
	List<O> outputNodeIds = new ArrayList<O>();
	
	
	// SCORES	
    // enable storing more than one set of scores to allow for 
	// stochastic batch training
	/** input feature scores */
    List<Map<I,NNScore<I>>> inputScoresList;
	
	/** scores at each hidden layer node in the network.*/
	List<Map<Integer, Map<Integer, NNScore<Integer>>>> hiddenScoresList;
	
	/** actual output scores.*/
	List<Map<O, NNScore<O>>> actualOutputScoresList;
	
	/** expected output scores */
	List<Map<O, NNScore<O>>> expectedOutputScoresList;
	
	
	// WEIGHTS
	/** weights of the NN from input layer to first hidden layer*/
    Map<I, Map<Integer, NNWeight<I, Integer>>> inputWeights;

    /** weights between hidden layers */
    Map<Integer, Map<Integer, Map<Integer, NNWeight<Integer, Integer>>>> hiddenWeights;
   
    /** weights from last hidden layer to output layer */
    Map<Integer, Map<O, NNWeight<Integer, O>>> outputWeights;
    

    // constructor
    public Network(String configFile)
    {
    	// read configuration file and initialize scores and weights maps
    	initNet(configFile);
    	precomputeSigmoid();
    }
    
    
    /**
     * Initialize weight maps with
     * random values
     * @param configFile
     */
    private void initNet(String configFile)
    {
    	inputScoresList = new ArrayList<Map<I,NNScore<I>>>();
    	hiddenScoresList = new ArrayList<Map<Integer, Map<Integer, NNScore<Integer>>>>();
    	actualOutputScoresList = new ArrayList<Map<O, NNScore<O>>>();
    	expectedOutputScoresList = new ArrayList<Map<O, NNScore<O>>>();
    	
    	nNodesPerHiddenLayer = new HashMap<Integer,Integer>();
    	inputWeights = new HashMap<I, Map<Integer, NNWeight<I, Integer>>>();
    	hiddenWeights = new HashMap<Integer, Map<Integer, Map<Integer, NNWeight<Integer, Integer>>>>();
    	outputWeights = new HashMap<Integer, Map<O, NNWeight<Integer, O>>>();
    	
    	// read config file
    	readConfigAndSetNetworkParams(configFile);
    	
    	// initialize weights between hidden layers
    	for(int i=1; i<nHiddenLayers; i++)
    	{
    		if(!hiddenWeights.containsKey(i))
    			hiddenWeights.put(i, new HashMap<Integer, Map<Integer, NNWeight<Integer, Integer>>>());
    		
    		for(int j=0;j<nNodesPerHiddenLayer.get(i); j++)
    		{
    			for(int k=0;k<nNodesPerHiddenLayer.get(i+1);k++)
    			{
    				NNWeight<Integer, Integer> weight = new NNWeight<Integer, Integer>(i, j, k);
    				weight.setWeight(getRandomInitialWeight());
    				Map<Integer, NNWeight<Integer,Integer>> outerHiddenLayerToWeight;
    				if(hiddenWeights.get(i).containsKey(j))
    				{
    					outerHiddenLayerToWeight = hiddenWeights.get(i).get(j);
    				}
    				else
    				{
    					outerHiddenLayerToWeight = new HashMap<Integer, NNWeight<Integer,Integer>>();
    				}
    				outerHiddenLayerToWeight.put(k, weight);
    				hiddenWeights.get(i).put(j, outerHiddenLayerToWeight);
    			}
    		}
    	}
    	
    	
    	// initialize weights from final hidden layer to output layer
    	for(int i=0; i< nNodesPerHiddenLayer.get(nHiddenLayers); i++)
    	{
    		for(int j=0;j<nOutputNodes;j++)
    		{
    			NNWeight<Integer,O> weight = new NNWeight<Integer,O>(nHiddenLayers, i, outputNodeIds.get(j));
    			weight.setWeight(getRandomInitialWeight());
    			Map<O, NNWeight<Integer,O>> outputNodeToWeight;
    			if(outputWeights.containsKey(i))
    			{
    				outputNodeToWeight = outputWeights.get(i);
    			}
    			else
    			{
    				outputNodeToWeight = new HashMap<O, NNWeight<Integer,O>>();
    			}
    			
    			outputNodeToWeight.put(outputNodeIds.get(j), weight);
    			outputWeights.put(i, outputNodeToWeight);
    		}
    	}
    }
    

    /** initial weights */
	private double getRandomInitialWeight() 
    {
		return -0.7+(0.14)*new Random().nextDouble();
		
		// random gaussian weights
		//Random fRandom = new Random();
		
		//return 0.0 + fRandom.nextGaussian() * 1.0;
	}


	@SuppressWarnings("unchecked")
	/** read network configuration params */
	private void readConfigAndSetNetworkParams(String configFile) 
    {
		try
		{
			Document dom;
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			dom = db.parse(configFile);
			
			// set network parameters
			NodeList hidden = dom.getElementsByTagName("hidden");
			nHiddenLayers = hidden.getLength();
			for(int i=0;i<hidden.getLength();i++)
			{
				Element layerElem = (Element)hidden.item(i);
				System.out.println("Num nodes in hidden layer is: " + layerElem.getElementsByTagName("numNodes").item(0).getTextContent());
				nNodesPerHiddenLayer.put(i+1, Integer.parseInt(layerElem.getElementsByTagName("numNodes").item(0).getTextContent()));
			}
			
			Element output = (Element)dom.getElementsByTagName("output").item(0);
			nOutputNodes = Integer.parseInt(output.getElementsByTagName("numNodes").item(0).getTextContent());
			System.out.println("Number of output nodes: " + nOutputNodes);
			NodeList outputIds = output.getElementsByTagName("nodeId");
			for(int i=0;i<outputIds.getLength();i++)
			{
				outputNodeIds.add((O)outputIds.item(i).getTextContent());
			}
		}
		catch(Exception e)
		{
			System.out.println("Failed to read XML configuration file");
			e.printStackTrace();
		}
	}



	/**
     * Functionality as in word2vec for
     * the next 2 sigmoid related methods
     * 
     */
    private void precomputeSigmoid()
    {
    	sigmoidTable = new HashMap<Integer, Double>();
    	for(int i=0; i<sigmoidTableSize; i++)
    	{
    		double exp = Math.exp(((double)i/sigmoidTableSize * 2.0 - 1.0)*6.0);
    		//System.out.println("exp: " + exp);
    		//System.out.println("sigmoid: " + exp/(exp+1));
    		sigmoidTable.put(i, exp/(exp+1));
    	}
    	
    	// debug
    	/*for(int i=0; i<sigmoidTableSize; i++)
    	{
    		System.out.println("key: " + i + "  value: " + sigmoidTable.get(i));
    	}*/
    	
    }
    
    
    private double getSigmoid(double input)
    {
    	int key = (int)((input + maxExp) * (sigmoidTableSize/maxExp/2));
    	if(sigmoidTable.containsKey(key))
    	{
    		return sigmoidTable.get(key);
    	}
    	else return 0.0;	
    }
    
    
    /**
     * Make a forward pass through the network with current values
     * of weights. Final output values stored in the output
     * score maps.
     */
    public void forwardPropagate( Map<I, NNScore<I>> inputFeatures, Map<O,NNScore<O>> expectedOutputScores)
    {	
    	/** scores at each hidden layer node in the network.*/
    	Map<Integer, Map<Integer, NNScore<Integer>>> hiddenScores = new HashMap<Integer, Map<Integer, NNScore<Integer>>>();
    	
    	/** actual output scores.*/
    	Map<O, NNScore<O>> actualOutputScores = new HashMap<O, NNScore<O>>();
    	
    	// initialize score DS
    	for(/* layer number*/ int i=1; i<=nHiddenLayers; i++)
    	{
    		if(!hiddenScores.containsKey(i))
    			hiddenScores.put(i, new HashMap<Integer,NNScore<Integer>>());
    		
    		for(/* node number*/ int j=0;j<nNodesPerHiddenLayer.get(i);j++)
    		{
    			hiddenScores.get(i).put(j, new NNScore<Integer>(i, j, 0.0));
    		}
    	}
    	
    	for(O outputNodeId : outputNodeIds)
    	{
    		if(!actualOutputScores.containsKey(outputNodeId))
    			actualOutputScores.put(outputNodeId, new NNScore<O>(nHiddenLayers+1, outputNodeId, 0.0));
    	}
    	
    	
    	// initialize weights in the input layer to first hidden layer
    	// if not already initialized
    	for(I inputFeature : inputFeatures.keySet())
    	{
    		if(!inputWeights.containsKey(inputFeature))
    		{
    			for(int i=0; i<nNodesPerHiddenLayer.get(1); i++)
        		{
        			NNWeight<I,Integer> weight = new NNWeight<I,Integer>(0, inputFeature, i);
        			weight.setWeight(getRandomInitialWeight());
        			Map<Integer, NNWeight<I,Integer>> hiddenNodeToWeight;
        			if(inputWeights.containsKey(inputFeature))
        			{
        				hiddenNodeToWeight = inputWeights.get(inputFeature);
        			}
        			else
        			{
        				hiddenNodeToWeight = new HashMap<Integer, NNWeight<I,Integer>>();
        			}
        			
        			hiddenNodeToWeight.put(i, weight);
        			//System.out.println("Setting weight between input feature " + inputFeature + " and hidden layer node " + i + ", weight value = " + weight.getWeight());
        			inputWeights.put(inputFeature, hiddenNodeToWeight);
        		}
    		}
    	}
    	
    	// compute scores for first hidden layer
    	for(int i=0; i<nNodesPerHiddenLayer.get(1); i++)
    	{
    		double nodescore = 0.0;
    		//if(hiddenScores.get(1).containsKey(i)) nodescore = hiddenScores.get(1).get(i).getScore();
    		for(I inputFeature : inputFeatures.keySet())
        	{
        		nodescore += inputFeatures.get(inputFeature).getScore() * inputWeights.get(inputFeature).get(i).getWeight();
        	}
    		
    		if(getSigmoid(nodescore) > 0)
    		{
    			//System.out.println("Setting score: " + nodescore);
      		    hiddenScores.get(1).get(i).setScore(nodescore);
    		}
    		  
    	}
    	
    	
    	// compute scores for all subsequent hidden layers
    	for(int i=2; i<=nHiddenLayers; i++)
    	{
    		for(int to=0; to<nNodesPerHiddenLayer.get(i); to++)
    		{
    			double nodescore = 0.0;
    			//if(hiddenScores.get(i).containsKey(to)) nodescore = hiddenScores.get(i).get(to).getScore();
    			for(int from=0; from<nNodesPerHiddenLayer.get(i-1); from++)
    			{
    				nodescore += getSigmoid(hiddenScores.get(i-1).get(from).getScore()) * hiddenWeights.get(i-1).get(from).get(to).getWeight();
    			}
    			if(getSigmoid(nodescore) > 0)
    			{
    				//System.out.println("Setting score: " + nodescore);
    				hiddenScores.get(i).get(to).setScore(nodescore);
    			}
    			  
    		}
    	}
    	
    	// compute output layer scores
    	Map<O,Double> outputs = new HashMap<O,Double>();
    	for(int i=0; i<nOutputNodes; i++)
    	{
    		double nodescore = 0.0;
    		//if(actualOutputScores.containsKey(outputNodeIds.get(i))) nodescore = actualOutputScores.get(outputNodeIds.get(i)).getScore();
    		for(int from=0; from<nNodesPerHiddenLayer.get(nHiddenLayers); from++)
    		{
    			nodescore += getSigmoid(hiddenScores.get(nHiddenLayers).get(from).getScore()) * outputWeights.get(from).get(outputNodeIds.get(i)).getWeight();
    		}
    		
    		if(getSigmoid(nodescore) > 0)
    		{
    			//System.out.println("Setting score: " + nodescore);
    			actualOutputScores.get(outputNodeIds.get(i)).setScore(nodescore);
    			actualOutputScores.get(outputNodeIds.get(i)).setSigmoidScore(getSigmoid(nodescore));
    		}
    			
    	}
    	
    	inputScoresList.add(inputFeatures);
    	hiddenScoresList.add(hiddenScores);
    	expectedOutputScoresList.add(expectedOutputScores);
    	actualOutputScoresList.add(actualOutputScores);
    }
    
    
    /**
     * Make weight updates throughout the network
     * using the standard stochastic backpropagation
     * algorithm.  
     */
    public void backPropagate()
    {
    	/** handle output to last hidden layer separately*/
    	// for each entry in expected or actual output scores list
    	   // for each node in prev layer
    	      // error = (error_on_prev_layer_node==null)?outputlayercomputederror:error_on_prev_layer_node; reset error_on_prev_layer_node
    	      // for each node in present layer
    	          // error_on_prev_layer_node += learning_rate * error * dsigmoid(input_to_sigmoid) * weight_b/w_current_2_nodes
    	          // weight_b/w_current_2_nodes  += learning_rate * error * dsigmoid(input_to_sigmoid) * input_coming_in_to_output
    	
    	double error_on_prev_layer;
    	for(int index=0; index < actualOutputScoresList.size(); index++)
    	{
    		Map<O, NNScore<O>> actualOutputScores = actualOutputScoresList.get(index);
    		Map<O, NNScore<O>> expectedOutputScores = expectedOutputScoresList.get(index);
    		Map<I,NNScore<I>> inputScores = inputScoresList.get(index);
    		Map<Integer, Map<Integer, NNScore<Integer>>> hiddenScores = hiddenScoresList.get(index);
    		
    		Map<Integer, Map<Integer, Double>> errors = new HashMap<Integer, Map<Integer, Double>>();
    		
    		int actualOutputIndex = getMaxIndex(actualOutputScores);
			int expectedOutputIndex = getMaxIndex(expectedOutputScores);
			if(actualOutputIndex == expectedOutputIndex && nOutputNodes > 1)
			{
				//System.out.println("Actual: " + outputNodeIds.get(actualOutputIndex) + " Expected: " + outputNodeIds.get(expectedOutputIndex) + " No error");
				continue;
			}
			/*if(nOutputNodes == 1)
			{
				if(expectedOutputScores.get(outputNodeIds.get(expectedOutputIndex)).getSigmoidScore()==1.0 && actualOutputScores.get(outputNodeIds.get(actualOutputIndex)).getSigmoidScore()>=0.5)
					continue;
				if(expectedOutputScores.get(outputNodeIds.get(expectedOutputIndex)).getSigmoidScore()==-1.0 && actualOutputScores.get(outputNodeIds.get(actualOutputIndex)).getSigmoidScore()<0.5)
					continue;
			}*/

			
    		// output to final hidden layer
    		for(int from=0; from<nNodesPerHiddenLayer.get(nHiddenLayers); from++)
    		{
    			double error_to_be_propagated = 0.0;

    			for(int to=0; to<nOutputNodes; to++)
    			{
                    double toNodeScore = actualOutputScores.get(outputNodeIds.get(to)).getScore();
    				double weight = outputWeights.get(from).get(outputNodeIds.get(to)).getWeight();
    				//double error = expectedOutputScores.get(outputNodeIds.get(to)).getScore() - getSigmoid(toNodeScore);
    				
    				// mean squared error: dc/d(activation) = (hyp - actual)
    				double error = 0.0;
    				
    				/*
    				 *  if there is more than one output node, the error associated with the correct node is -2,
    				 *  and error associated with all other nodes is +2. 
    				 */
    				if(nOutputNodes > 1)
    				{
    					if(to == expectedOutputIndex)
        					error = -(expectedOutputScores.get(outputNodeIds.get(to)).getSigmoidScore()-actualOutputScores.get(outputNodeIds.get(to)).getSigmoidScore());
        				else error = -(expectedOutputScores.get(outputNodeIds.get(to)).getSigmoidScore()-actualOutputScores.get(outputNodeIds.get(to)).getSigmoidScore());
    				}
    				/*
    				 * if there is only one output node
    				 */
    				/*else
    				{
    					if(expectedOutputScores.get(outputNodeIds.get(to)).getSigmoidScore()==1.0 && actualOutputScores.get(outputNodeIds.get(to)).getSigmoidScore()<0.5)
    						error = -1;
    					else error = 1;
    				}*/
    				
    				
    				error_to_be_propagated += learning_rate * error * getSigmoid(toNodeScore) * (1- getSigmoid(toNodeScore)) * weight;
    				// weight update
    				outputWeights.get(from).get(outputNodeIds.get(to)).setWeight(weight += (learning_rate * error * getSigmoid(toNodeScore) * (1- getSigmoid(toNodeScore)) * toNodeScore));
    			}
    			
    			if(!errors.containsKey(nHiddenLayers))
    				errors.put(nHiddenLayers, new HashMap<Integer,Double>());
				errors.get(nHiddenLayers).put(from, error_to_be_propagated);
    		}
    		
    		// between hidden layers
    		int currLayer = nHiddenLayers;
    		while(currLayer > 1)
    		{
    			for(int from=0; from<nNodesPerHiddenLayer.get(currLayer-1); from++)
        		{
        			double error_to_be_propagated = 0.0;
        			for(int to=0; to<nNodesPerHiddenLayer.get(currLayer); to++)
        			{
        				double toNodeScore = hiddenScores.get(currLayer).get(to).getScore();
        				double weight = hiddenWeights.get(currLayer).get(from).get(to).getWeight();
        				double error = errors.get(currLayer).get(to);
        				error_to_be_propagated += learning_rate * error * getSigmoid(toNodeScore) * (1- getSigmoid(toNodeScore)) * weight;
        				// weight update
        				hiddenWeights.get(currLayer).get(from).get(to).setWeight(weight += (learning_rate * error * getSigmoid(toNodeScore) * (1- getSigmoid(toNodeScore)) * toNodeScore));
        			}
        			
        			if(!errors.containsKey(currLayer-1))
        				errors.put(currLayer-1, new HashMap<Integer,Double>());
    				errors.get(currLayer-1).put(from, error_to_be_propagated);
        		}
    			currLayer--;
    		}
    		
    		// debug
    		/*System.out.println("errors map entries");
    		for(int layer: errors.keySet())
    		{
    			System.out.println("Layer: " + layer);
    			for(int i=0; i< nNodesPerHiddenLayer.get(layer); i++)
    			{
    				for(Integer node : errors.get(layer).keySet())
    				{
    					System.out.println("Node: " + node + "  Error: " + errors.get(layer).get(node));
    				}
    			}
    			
    		}*/
    		
    		// hidden to input layer
    		for(I inputFeature : inputScores.keySet())
    		{
    			for(int to=0;to<nNodesPerHiddenLayer.get(1);to++)
    			{
    				double toNodeScore = hiddenScores.get(1).get(to).getScore();
    				double weight = inputWeights.get(inputFeature).get(to).getWeight();
    				//System.out.println("to: " + to);
    				double error = errors.get(1).get(to);

    				// weight update
    				inputWeights.get(inputFeature).get(to).setWeight(weight += (learning_rate * error * getSigmoid(toNodeScore) * (1- getSigmoid(toNodeScore)) * toNodeScore));
    			}
    		}
    		
    	}
    	
    	/** reset score lists after every backprop pass */
    	inputScoresList.clear();
    	hiddenScoresList.clear();
    	expectedOutputScoresList.clear();
    	actualOutputScoresList.clear();
    }


	private int getMaxIndex(Map<O, NNScore<O>> outputScores) {
		double max = 0.0;
		int maxIndex = 0;
		for(int to=0; to<nOutputNodes; to++)
		{
			if(outputScores.get(outputNodeIds.get(to)).getSigmoidScore() >= max)
			{
				max = outputScores.get(outputNodeIds.get(to)).getSigmoidScore();
				maxIndex = to;
			}
		}
		
		return maxIndex;
	}
    
}
