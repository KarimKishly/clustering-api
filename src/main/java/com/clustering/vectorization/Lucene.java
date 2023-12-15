package com.clustering.vectorization;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.tartarus.snowball.ext.PorterStemmer;

import java.io.IOException;
import java.io.StringReader;

public class Lucene {
    public static String removeStopwordsAndStem(String text) throws IOException {
        EnglishAnalyzer analyzer = new EnglishAnalyzer();

        CharArraySet stopwords = analyzer.getStopwordSet();

        StringReader reader = new StringReader(text);
        org.apache.lucene.analysis.TokenStream tokenStream = analyzer.tokenStream("", reader);

        PorterStemmer stemmer = new PorterStemmer();

        org.apache.lucene.analysis.tokenattributes.CharTermAttribute charTermAttribute =
                tokenStream.addAttribute(org.apache.lucene.analysis.tokenattributes.CharTermAttribute.class);
        tokenStream.reset();
        StringBuilder filteredText = new StringBuilder();
        while (tokenStream.incrementToken()) {
            String term = charTermAttribute.toString();
            if (!stopwords.contains(term)) {
                stemmer.setCurrent(term);
                stemmer.stem();
                String stemmedTerm = stemmer.getCurrent();
                filteredText.append(stemmedTerm).append(" ");
            }
        }
        tokenStream.close();

        analyzer.close();

        return filteredText.toString().trim();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(Lucene.removeStopwordsAndStem("playing games, having fun"));
    }
}
