package org.bbn.sbd.io;

import org.bbn.sbd.datastructures.Turn;
import org.bbn.sbd.datastructures.Word;

import java.util.*;

public class TestRtXmlReader {
	
	public static void main(String[] args)
	{
		try
		{
			//Turn turn = ReadRtXml.readAsSingleTurn(args[0]);
			List<Turn> turns = ReadRtXml.readIntoListOfTurns(args[0]);
			
			for(Turn t : turns)
			{
				System.out.println("Turn ID: " + t.getId());
				System.out.println("Turn source: " + t.getFile());
				System.out.println("Turn start: " + t.getStart());
				System.out.println("Turn duration: " + t.getDuration());
				for(Word word: t.getWords())
				{
					System.out.println("Word Id: " + word.getWordId() + "  label: " + word.getLabel() + " pause: " + word.getPauseDuration() 
							/*+ "  prev word: " + word.getPrev()!=null?word.getPrev().getWordId():"" + " next word: " 
					+ (word.getNext()==null?"null":"notnull")*/);
					if(word.getPrev()!=null)
					{
					   System.out.println("prev word: " + word.getPrev().getWordId());
					}
					else
					{
						System.out.println("prev word: null");
					}
					
					if(word.getNext()!=null)
					{
					   System.out.println("next word: " + word.getNext().getWordId());
					}
					else
					{
						System.out.println("next word: null");
					}
				}
			}
			
			/*System.out.println("Turn ID: " + turn.getId());
			System.out.println("Turn source: " + turn.getFile());
			System.out.println("Turn start: " + turn.getStart());
			System.out.println("Turn duration: " + turn.getDuration());
			for(Word word: turn.getWords())
			{
				System.out.println("Word Id: " + word.getWordId() + "  label: " + word.getLabel() + " pause: " + word.getPauseDuration() );
				if(word.getPrev()!=null)
				{
				   System.out.println("prev word: " + word.getPrev().getWordId());
				}
				else
				{
					System.out.println("prev word: null");
				}
				
				if(word.getNext()!=null)
				{
				   System.out.println("next word: " + word.getNext().getWordId());
				}
				else
				{
					System.out.println("next word: null");
				}
			}*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
