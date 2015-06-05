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
					if(turnElement.getAttribute("file").length()>0)
						turn.setFile(turnElement.getAttribute("file"));
					if(turnElement.getAttribute("start").length()>0)
						turn.setStart(Double.parseDouble(turnElement.getAttribute("start")));
					if(turnElement.getAttribute("duration").length()>0)
						turn.setDuration(Double.parseDouble(turnElement.getAttribute("duration")));
					turns.add(turn);
					
					NodeList wordList = turnElement.getElementsByTagName("word");
					
					for(int j=0; j<wordList.getLength(); j++)
					{
						Node wordNode = wordList.item(j);
						Element wordElement = (Element)wordNode;
						
						String wordId = wordElement.getAttribute("id").length()>0?wordElement.getAttribute("id").trim().replaceAll("\\s+", " ")/*.replaceAll("\\d+","<num>")*/:null;
						String posTag = wordElement.getAttribute("pos").length()>0?wordElement.getAttribute("pos"):null;
						
						boolean pausefiller = wordElement.getAttribute("pausefiller").length()>0?true:false;
						boolean chopboundary = wordElement.getAttribute("chop_boundary").length()>0?true:false;
						
						// cheating. Set pause at end of turn to super high
						String pause;
						//if(j==wordList.getLength()-1)
						//{
							//pause = "75";
						//}
						//else
						//{
							pause = wordElement.getAttribute("pause")!=null?wordElement.getAttribute("pause"):"0";
						//}
						
						
						int label = (wordElement.getAttribute("sentence_tag")!="" && 
								wordElement.getAttribute("sentence_tag").equals("comsn"))?1:-1;
						
						try
						{
							turn.addWord(new Word(wordId, posTag, label, Double.parseDouble(pause), pausefiller, chopboundary));
						}
						catch(NumberFormatException e)
						{
							turn.addWord(new Word(wordId, posTag, label, 0, pausefiller, chopboundary));
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
                System.out.println("Num of turns: " + turnList.getLength());
				for(int i=0; i< turnList.getLength(); i++)
				{
					System.out.println("Reading turn " + (i+1));
					Element turnElement = (Element) turnList.item(i);
				    
					if(i==0)
					{
						if(turnElement.getAttribute("file").length()>0)
							turn.setFile(turnElement.getAttribute("file"));
						if(turnElement.getAttribute("start").length()>0)
							turn.setFile(turnElement.getAttribute("start"));
					}
					
					if(turnElement.getAttribute("duration").length()>0)
						duration += Double.parseDouble(turnElement.getAttribute("duration"));
					
					NodeList wordList = turnElement.getElementsByTagName("word");
					
					for(int j=0; j<wordList.getLength(); j++)
					{
						Node wordNode = wordList.item(j);
						Element wordElement = (Element)wordNode;
						
						String wordId = wordElement.getAttribute("id").length()>0?wordElement.getAttribute("id").trim().replaceAll("\\s+", " ")/*.replaceAll("\\d+","<num>")*/:null;
						String posTag = wordElement.getAttribute("pos").length()>0?wordElement.getAttribute("pos"):null;
						//System.out.println("pausefiller: " + wordElement.getAttribute("pausefiller"));
						boolean pausefiller = wordElement.getAttribute("pausefiller").length()>0?true:false;
						boolean chopboundary = wordElement.getAttribute("chop_boundary").length()>0?true:false;
						
						// cheating. Set pause at end of turn to super high
						String pause;
						//if(j==wordList.getLength()-1)
						//{
						//	pause = "75";
						//}
						//else
						//{
							pause = wordElement.getAttribute("pause").length()>0?wordElement.getAttribute("pause"):"0";
						//}
						
						
						int label = (wordElement.getAttribute("sentence_tag").length()>0 && 
								wordElement.getAttribute("sentence_tag").equals("comsn"))?1:-1;
						try
						{
							turn.addWord(new Word(wordId, posTag, label, Double.parseDouble(pause), pausefiller, chopboundary));
						}
						catch(NumberFormatException e)
						{
							turn.addWord(new Word(wordId, posTag, label, 0, pausefiller, chopboundary));
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
