package org.bbn.sbd.scoring;

import java.util.*;

public class SER {
	
	public static void score(List<Double> hyp, List<Double> truelabels)
	{
		if(hyp.size() != truelabels.size())
		{
			System.out.println("Please ensure that true labels and hypothesis lists are of equal size.");
			System.exit(1);
		}
	}

}
