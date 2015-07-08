package org.bbn.sbd.training.nn;

import java.util.*;

public class TestReadConfig {
	
	public static void main(String[] args)
	{
		try
		{
			Network<String,String> nn = new Network<String,String>("/nfs/raid62/u15/research/SBD/dl4j/deeplearning4j/dl4j-sbd/src/main/resources/org/bbn"
					+ "/sbd/training/nn/NNConfig.xml");
			
			System.out.println("NETWORK PARAMS: ");
			System.out.println("num hidden layers: " + nn.nHiddenLayers);
			System.out.println("num output nodes: " + nn.nOutputNodes);
			
			System.out.println("OUTPUT NODE IDS ARE:");
			for(String outputNodeId: nn.outputNodeIds)
			{
				System.out.println(outputNodeId);
			}
			
			System.out.println("NUM NODES PER HIDDEN LAYER:");
			for(Integer key : nn.nNodesPerHiddenLayer.keySet())
			{
				System.out.println(key + "\t" + nn.nNodesPerHiddenLayer.get(key));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
