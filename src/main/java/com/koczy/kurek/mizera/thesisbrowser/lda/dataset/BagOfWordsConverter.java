package com.koczy.kurek.mizera.thesisbrowser.lda.dataset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

import static com.koczy.kurek.mizera.thesisbrowser.model.Constants.LDA_NUM_OF_WORDS_THRESHOLD;

@Component
public class BagOfWordsConverter {

    private static final Logger logger = Logger.getLogger(BagOfWordsConverter.class.getName());

    private Map<String, Integer> wordsIdMap = new HashMap<>();

    @Autowired
    public BagOfWordsConverter(@Value("${lda.vocabs}") String vocabsFilePath) {
        try {
            Path vocabFilePath = Paths.get(vocabsFilePath);
            List<String> lines = Files.readAllLines(vocabFilePath);
            for (String line : lines) {
                wordsIdMap.put(line, lines.indexOf(line)+1);
            }
        } catch (IOException ioe) {
            logger.warning("Couldn't read lines from vocab file, filePath: " + vocabsFilePath);
        }
    }

    public Map<Integer, Integer> convertTxtToBagOfWords(InputStream txtInputStream){
        Map<Integer, Integer> bagOfWords = new HashMap<>();
        Scanner input = new Scanner(txtInputStream);
        while (input.hasNext()) {
            String word = input.next();
            if(wordsIdMap.containsKey(word)){
                if(bagOfWords.containsKey(wordsIdMap.get(word))){
                    bagOfWords.put(wordsIdMap.get(word), bagOfWords.get(wordsIdMap.get(word)) + 1);
                }else{
                    bagOfWords.putIfAbsent(wordsIdMap.get(word), 1);
                }
            }
        }
        for (Iterator<Map.Entry<Integer, Integer>> it = bagOfWords.entrySet().iterator(); it.hasNext();) {
            Integer value = it.next().getValue();
            if (value <= LDA_NUM_OF_WORDS_THRESHOLD) {
                it.remove();
            }
        }
        return bagOfWords;
    }
}
