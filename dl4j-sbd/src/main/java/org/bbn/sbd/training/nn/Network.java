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
	
	// NETWORK PARAMS
	int nHiddenLayers;

	Map<Integer, Integer> nNodesPerHiddenLayer; 
	
	int nInputNodes;
	
	int nOutputNodes;
	
	List<O> outputNodeIds = new ArrayList<O>();
	
	// SCORES
	/** scores associated with input nodes of the network (aka feature values).
	 * This does not change during the course of the training */
	Map<I, NNScore<I>> inputFeatures;
	
	/** scores at each hidden layer node in the network.
	 * Reset for every training instance. */
	Map<Integer, Map<Integer, NNScore<Integer>>> hiddenScores;
	
	/** output scores.
	 * Reset for every training instance.*/
	Map<O, NNScore<O>> outputScores;
	
	// WEIGHTS
	/** weights of the NN from input layer to first hidden layer*/
    Map<I, Map<Integer, NNWeight<I, Integer>>> inputWeights;

    /** weights between hidden layers */
    Map<Integer, Map<Integer, Map<Integer, NNWeight<Integer, Integer>>>> hiddenWeights;
   
    /** weights from last hidden layer to output layer */
    Map<Integer, Map<O, NNWeight<Integer, O>>> outputWeights;
    
    Map<Integer, Double> precomputedSigmoid;
    
    
    // constructor
    public Network(String configFile, Map<I, NNScore<I>> inputFeatures)
    {
    	// read configuration file and initialize scores and weights maps
    	this.inputFeatures = inputFeatures;
    	nInputNodes = inputFeatures.size();
    	
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
    	readConfigAndSetNetworkParams(configFile);
    	
    	// initialize weights in the input layer to first hidden layer
    	for(I inputFeature : inputFeatures.keySet())
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
    			inputWeights.put(inputFeature, hiddenNodeToWeight);
    		}
    	}
    	
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
    
    
    private double getRandomInitialWeight() 
    {
		return 0.1+(0.7-0.1)*new Random().nextDouble();
	}


	@SuppressWarnings("unchecked")
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
				nNodesPerHiddenLayer.put(i+1, Integer.parseInt(layerElem.getElementsByTagName("numNodes").item(0).getTextContent()));
			}
			
			Element output = (Element)dom.getElementsByTagName("output").item(0);
			nOutputNodes = Integer.parseInt(output.getElementsByTagName("numNodes").item(0).getTextContent());
			
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
     * Functionality as in word2vec
     * 
     */
    private void precomputeSigmoid()
    {
    	
    }
    
    
    /**
     * Make a forward pass through the network with current values
     * of weights. Final output values stored in the output
     * score maps.
     */
    public void forwardPropagate()
    {
    	
    }
    
    
    /**
     * Make weight updates throughout the network
     * using the standard stochastic backpropagation
     * algorithm.  
     */
    public void backPropagate()
    {
    	//TODO: Make any smaller helper methods as needed
    	// once you start developing
    }
    
}
