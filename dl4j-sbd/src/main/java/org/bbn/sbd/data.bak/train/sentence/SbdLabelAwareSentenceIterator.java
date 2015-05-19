package org.dl4j.sbd.data.train.sentence;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.dl4j.sbd.data.train.TextRetriever;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.sentenceiterator.labelaware.LabelAwareSentenceIterator;

public class SbdLabelAwareSentenceIterator implements LabelAwareSentenceIterator {

	private List<String> context;
    private List<String> labels;
	private SentencePreProcessor preProcessor;
	private AtomicInteger currRecord;
	
	public SbdLabelAwareSentenceIterator(SentencePreProcessor preProcessor, String dataFile) {
        this.preProcessor = preProcessor;
        try {
            TextRetriever retriever = new TextRetriever(dataFile);
            this.context = new CopyOnWriteArrayList<>(retriever.contextWindows());
            this.labels = new CopyOnWriteArrayList<>(retriever.labels());
            System.out.println(labels.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        currRecord = new AtomicInteger(0);


    }

    public SbdLabelAwareSentenceIterator() {
       this(null, "/nfs/mercury-04/u40/deft/dl4j/deeplearning4j/dl4j-sbd/src/main/resources/exampletrain");
    }

	
	public boolean hasNext() {
		//System.out.println("currRecord: " + currRecord + " context size: " + context.size());
		return currRecord.get() < context.size();
	}

	public String currentLabel() {
		 return labels.get(currRecord.get() > 0 ? currRecord.get() - 1 : 0);
	}


	public String nextSentence() {
		String ret = context.get(currRecord.get());
        currRecord.incrementAndGet();
        return ret;
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SentencePreProcessor getPreProcessor() {
		return preProcessor;
	}

	@Override
	public void reset() {
		currRecord.set(0);
	}

	@Override
	public void setPreProcessor(SentencePreProcessor preProcessor) {
		this.preProcessor = preProcessor;
		
	}

}
