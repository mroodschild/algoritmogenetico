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
package org.gitia.tipo3.mutation;

import java.util.List;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;
import org.gitia.tipo3.population.Individuo;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Mutation {

    static Random r = new Random();

    public static void mutation(List<Individuo> population, double mutation, double elite, double min, double max) {
        int eliteSize = (int) Math.round(population.size() * elite);
        int size = (mutation > 0) ? population.size() - eliteSize : population.size();
        for (int i = 0; i < size; i++) {
            if (r.nextDouble() < mutation) {
                SimpleMatrix v = population.get(i).getDna();
                int idx = r.nextInt(v.numCols());
                double val = r.nextDouble() * (max - min) + min;
                v.set(idx, val);
                population.get(i).setDna(v);
            }
        }
    }

}
