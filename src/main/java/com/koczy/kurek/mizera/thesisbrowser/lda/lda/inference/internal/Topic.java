/*
* Copyright 2015 Kohei Yamamoto
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.koczy.kurek.mizera.thesisbrowser.lda.lda.inference.internal;

import java.util.logging.Logger;

class Topic {
    private static final Logger logger = Logger.getLogger(Topic.class.getName());

    private final int id;
    private final int numVocabs;
    private VocabularyCounter counter;
    
    Topic(int id, int numVocabs) {
        if (id < 0 || numVocabs <= 0){
            logger.warning( "Id cannot be lower than 0 or number of vocabs cannot be lower than 1, id: " + id
                    + ", number of vocabs: " + numVocabs);
            this.id = -1;
            this.numVocabs = 0;
            this.counter = new VocabularyCounter(numVocabs);
            return;
        }
        this.id = id;
        this.numVocabs = numVocabs;
        this.counter = new VocabularyCounter(numVocabs);
    }

    int getId() {
        return id;
    }
    
    int getVocabCount(int vocabID) {
        return counter.getVocabCount(vocabID);
    }
    
    int getSumCount() {
        return counter.getSumCount();
    }
    
    void incrementVocabCount(int vocabID) {
        counter.incrementVocabCount(vocabID);
    }
    
    void decrementVocabCount(int vocabID) {
        counter.decrementVocabCount(vocabID);
    }
    
    double getPhi(int vocabID, double beta) {
        if (vocabID <= 0 || beta <= 0){
            logger.warning( "Invalid value of beta or invalid value of vocabId, beta: " + beta + ", vocabId: " + vocabID);
            return -1;
        }
        return (getVocabCount(vocabID) + beta) / (getSumCount() + beta * numVocabs);
    }
}
