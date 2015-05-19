package org.bbn.sbd.datastructures;

import java.util.List;
import java.util.ArrayList;

public class Turn 
{
	 String id;
	 
	 String file;
	 
	 double start;
	 
	 double duration;
	
     List<Word> words = new ArrayList<Word>();
     
     // getters
     public Turn(String id)
     {
    	 this.id = id;
     }

	public String getId() {
		return id;
	}

	public String getFile() {
		return file;
	}

	public double getStart() {
		return start;
	}

	public double getDuration() {
		return duration;
	}

	public List<Word> getWords() {
		return words;
	}

	
	
	// setters
	
	public void setFile(String file) {
		this.file = file;
	}

	public void setStart(double start) {
		this.start = start;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}
	
	public void addWord(Word word)
	{
		if(words.size() > 0)
		{
			Word currLastWord = words.get(words.size()-1);
			currLastWord.setNext(word);
			word.setPrev(currLastWord);
			word.setNext(null);
		}
		else
		{
			word.setNext(null);
			word.setNext(null);
		}
		
		words.add(word);
	}
     
}