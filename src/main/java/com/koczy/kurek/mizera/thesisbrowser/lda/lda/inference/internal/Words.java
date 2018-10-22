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

import com.koczy.kurek.mizera.thesisbrowser.lda.dataset.Vocabulary;

import java.util.Collections;
import java.util.List;

class Words {
    private List<Vocabulary> words;
    
    Words(List<Vocabulary> words) {
        if (words == null) throw new NullPointerException();
        this.words = words; 
    }

    int getNumWords() {
        return words.size();
    }
    
    Vocabulary get(int id) {
        if (id < 0 || words.size() <= id) {
            throw new IllegalArgumentException();
        }
        return words.get(id);
    }
    
    List<Vocabulary> getWords() {
        return Collections.unmodifiableList(words);
    }
}
