package org.dl4j.sbd.data.visualization;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.plot.Tsne;
import org.dl4j.sbd.data.train.sentence.SbdLabelAwareSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


/**
 * Created by agibsonccc on 10/19/14.
 */
public class Visualization {


    public static void main(String[] args) throws Exception {

    	SbdLabelAwareSentenceIterator trainiter = new SbdLabelAwareSentenceIterator(null, 
        		"/nfs/mercury-04/u40/deft/dl4j/deeplearning4j/dl4j-sbd/src/main/resources/exampletrain");
        TokenizerFactory factory = new DefaultTokenizerFactory();
        Word2Vec  vec = new Word2Vec.Builder().iterate(trainiter)
                .tokenizerFactory(factory).batchSize(10000)
                .learningRate(2.5e-2).sampling(5).learningRateDecayWords(10000)
                .iterations(3).minWordFrequency(1)
                .layerSize(300).windowSize(5).build();
        vec.fit();
        FileUtils.writeLines(new File("vocab.csv"),vec.getCache().words());


        String word = "UNK";
        String otherWord = "sentence";
        System.out.println("Words nearest  " + word +  " " + vec.wordsNearest(word,2));
        System.out.println("Words nearest  " + otherWord +  " " + vec.wordsNearest(otherWord,2));

        System.exit(0);

        /*Tsne t = new Tsne.Builder()
                .setMaxIter(100).stopLyingIteration(20).build();


        vec.getCache().plotVocab(t);*/

    }

}
