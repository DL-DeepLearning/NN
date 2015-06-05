package org.bbn.sbd.datastructures;


public class Word 
{
	 /** The string value */
     String wordId;
  
     /** the POS tag */
     String posTag;
     
     /** Label associated with the word*/
     int label;

     
     /** previous word */
     Word prev;
     
     /** next word */
     Word next;
     
     
     /** pause duration */
     double pauseDuration;
     
     /** pause filler after this word? */
     boolean pauseFiller = false;
     
     /** chop boundary after this word? */
     boolean chopBoundary = false;
     
     

    // constructor
    public Word(String id, String posTag, int label, double pause, boolean pausefiller, boolean chopboundary)
    {	
    	this.wordId = id;
    	this.posTag = posTag;
    	
    	this.label = label;
    	this.pauseDuration = pause;
    	this.pauseFiller = pausefiller;
    	this.chopBoundary = chopboundary;
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

	
	public int getLabel()
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

    public boolean getPauseFiller()
    {
    	return this.pauseFiller;
    }
    
    public boolean getChopBoundary()
    {
    	return this.chopBoundary;
    }
    
	
    // setters
	public void setPrev(Word prev) {
		this.prev = prev;
	}



	public void setNext(Word next) {
		this.next = next;
	}
	
	
	public void setLabel(int label)
	{
		this.label =label;
	}
	
	
	public void setPauseFiller(boolean pausefiller)
	{
		this.pauseFiller = pausefiller;
	}
	
	public void setChopBoundary(boolean boundary)
	{
		this.chopBoundary = boundary;
	}
	
}
