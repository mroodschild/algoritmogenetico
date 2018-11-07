/*
 * Copyright 2018 Matías Roodschild <mroodschild@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gitia.ag.population;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Population {

    public static List<Individuo> generate(int populationSize, int dnaSize, double min, double max) {
        List<Individuo> pop = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < populationSize; i++) {
            Individuo indv = new Individuo();
            SimpleMatrix dna = new SimpleMatrix(1, dnaSize);
            for (int j = 0; j < dnaSize; j++) {
                double v = r.nextDouble() * (max - min) + min;
                dna.set(j, v);
            }
            indv.setDna(dna);
            pop.add(indv);
        }
        return pop;
    }

}
