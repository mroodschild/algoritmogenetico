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

import java.util.Comparator;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class IndividuoComparator implements Comparator<Individuo> {

    /**
     * de menor a mayor
     *
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(Individuo o1, Individuo o2) {
        //int value;
        if (o1.getFitnessMean()< o2.getFitnessMean()) {
            return -1;
        } else if (o1.getFitnessMean()> o2.getFitnessMean()) {
            return 1;
        } else {
            return 0;
        }
        //return (int) (o1.getFitness() - o2.getFitness());
        
    }

}
