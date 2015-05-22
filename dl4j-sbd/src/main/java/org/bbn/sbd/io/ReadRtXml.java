package org.bbn.sbd.io;

import java.util.*;

import org.bbn.sbd.datastructures.*;

import java.io.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class ReadRtXml 
{
    
	
	public static List<Turn> readIntoListOfTurns(String filepath)
    {
		List<Turn> turns = new ArrayList<Turn>(); 
		
		try
		   {
			    File fXmlFile = new File(filepath);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
			 
				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();
				
				NodeList turnList = doc.getElementsByTagName("turn");
				for(int i=0; i< turnList.getLength(); i++)
				{
					Element turnElement = (Element) turnList.item(i);
				    
					Turn turn = new Turn(Integer.toString(i));
					if(turnElement.getAttribute("file")!=null)
						turn.setFile(turnElement.getAttribute("file"));
					if(turnElement.getAttribute("start")!=null)
						turn.setStart(Double.parseDouble(turnElement.getAttribute("start")));
					if(turnElement.getAttribute("duration")!=null)
						turn.setDuration(Double.parseDouble(turnElement.getAttribute("duration")));
					turns.add(turn);
					
					NodeList wordList = turnElement.getElementsByTagName("word");
					
					for(int j=0; j<wordList.getLength(); j++)
					{
						Node wordNode = wordList.item(j);
						Element wordElement = (Element)wordNode;
						
						String wordId = wordElement.getAttribute("id")!=null?wordElement.getAttribute("id"):null;
						String posTag = wordElement.getAttribute("pos")!=null?wordElement.getAttribute("pos"):null;
						String pause = wordElement.getAttribute("pause")!=null?wordElement.getAttribute("pause"):null;
						int label = (wordElement.getAttribute("sentence_tag")!=null && 
								wordElement.getAttribute("sentence_tag").equals("comsn"))?1:-1;
						
						try
						{
							turn.addWord(new Word(wordId, posTag, label, Double.parseDouble(pause)));
						}
						catch(NumberFormatException e)
						{
							turn.addWord(new Word(wordId, posTag, label, 0));
						}
					} 
				}
				
		   }
		   catch(Exception e)
		   {
			   e.printStackTrace();
		   }
		
		return turns;
    }
    
    
    
    public static Turn readAsSingleTurn(String filepath)
    {
        Turn turn = null; 
		
		try
		   {
			    File fXmlFile = new File(filepath);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
			 
				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();
				
				turn = new Turn(Integer.toString(0));
				double duration = 0;
				
				NodeList turnList = doc.getElementsByTagName("turn");
				for(int i=0; i< turnList.getLength(); i++)
				{
					Element turnElement = (Element) turnList.item(i);
				    
					if(i==0)
					{
						if(turnElement.getAttribute("file")!=null)
							turn.setFile(turnElement.getAttribute("file"));
						if(turnElement.getAttribute("start")!=null)
							turn.setFile(turnElement.getAttribute("start"));
					}
					
					if(turnElement.getAttribute("duration")!=null)
						duration += Double.parseDouble(turnElement.getAttribute("duration"));
					
					NodeList wordList = turnElement.getElementsByTagName("word");
					
					for(int j=0; j<wordList.getLength(); j++)
					{
						Node wordNode = wordList.item(j);
						Element wordElement = (Element)wordNode;
						
						String wordId = wordElement.getAttribute("id")!=null?wordElement.getAttribute("id"):null;
						String posTag = wordElement.getAttribute("pos")!=null?wordElement.getAttribute("pos"):null;
						String pause = wordElement.getAttribute("pause")!=null?wordElement.getAttribute("pause"):null;
						int label = (wordElement.getAttribute("sentence_tag")!=null && 
								wordElement.getAttribute("sentence_tag").equals("comsn"))?1:-1;
						try
						{
							turn.addWord(new Word(wordId, posTag, label, Double.parseDouble(pause)));
						}
						catch(NumberFormatException e)
						{
							turn.addWord(new Word(wordId, posTag, label, 0));
						}
						
					} 
				}
				
				turn.setDuration(duration);
				
		   }
		   catch(Exception e)
		   {
			   e.printStackTrace();
		   }
		
		return turn; 
    }
}
