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

package com.koczy.kurek.mizera.thesisbrowser.lda.lda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
class Beta {
    private List<Double> betas;

    @Autowired
    Beta(@Value("${lda.beta}") double beta) {
        this.betas = Arrays.asList(beta);
    }

    double get() {
        return get(0);
    }
    
    double get(int i) {
        return betas.get(i);
    }
    
    void set(int i, double value) {
        betas.set(i, value);
    }

    public List<Double> getBetas() {
        return betas;
    }
}
