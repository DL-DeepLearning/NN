package org.bbn.sbd.datastructures;

public class Word 
{
	 /** The string value */
     String wordId;
  
     /** the POS tag */
     String posTag;
     
     /** punctuation label following this word 
      *  0 for period, 1 for none.
      * */
     String label;
     
     /** previous word */
     Word prev;
     
     /** next word */
     Word next;
     
     /** pause duration */
     double pauseDuration;

    // constructor
    public Word(String id, String posTag, String label, Word prev, Word next, double pause)
    {
    	this.wordId = id;
    	this.posTag = posTag;
    	this.label = label;
    	this.prev = prev;
    	this.next = next;
    	this.pauseDuration = pause;
    }
     
   

	/** getters */
	public String getWordId() 
	{
		return wordId;
	}

	public String getPosTag() 
	{
		return posTag;
	}

	public String getLabel() 
	{
		return label;
	}

	public Word getPrev() 
	{
		return prev;
	}

	public Word getNext() 
	{
		return next;
	}
	 
	public double getPauseDuration() 
	{
		return pauseDuration;
	}


    // setters
	public void setPrev(Word prev) {
		this.prev = prev;
	}



	public void setNext(Word next) {
		this.next = next;
	}
	
	
}
