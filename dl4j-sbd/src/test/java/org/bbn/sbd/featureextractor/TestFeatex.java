package org.bbn.sbd.featureextractor;

import java.util.List;

import org.bbn.sbd.datastructures.SparseVector;
import org.bbn.sbd.datastructures.Turn;
import org.bbn.sbd.io.ReadRtXml;

public class TestFeatex {
	
	public static void main(String[] args)
	{
		try
		{
			Turn turn = ReadRtXml.readAsSingleTurn(args[0], false);
			
			FeatureExtractor featextractor = new FeatureExtractor();
			List<SparseVector<String>> vectors = featextractor.getSparseFeatureVector(turn, 7);
			
			for(SparseVector<String> vector : vectors)
			{
				System.out.println(vector.toString());
				System.out.println();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
