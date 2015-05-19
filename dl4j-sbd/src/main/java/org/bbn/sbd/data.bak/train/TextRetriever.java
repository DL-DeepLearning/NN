package org.dl4j.sbd.data.train;

import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVReadProc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextRetriever {

    private Map<String,String> context = new HashMap<>();


    private File csv;



    public TextRetriever(String location) {
        init(location);
    }

    private void init(String location) {
        csv = new File(location);
        CSV csv1 = CSV.separator('\t')
                .ignoreLeadingWhiteSpace().skipLines(1)
                .create();
        //ContextWords Label
        csv1.read(csv,new CSVReadProc() {
            @Override
            public void procRow(int rowIndex, String... values) {
                context.put(values[0],values[1]);
            }
        });
    }

    public List<String> contextWindows() {
    	List<String> labels = new ArrayList<>();
        for(String contextWindow : context.keySet())
            labels.add(contextWindow);
        return labels;
    }

    public List<String> labels() {
        List<String> labels = new ArrayList<>();
        for(String contextWindow : context.keySet())
            labels.add(context.get(contextWindow));
        return labels;
    }

}
