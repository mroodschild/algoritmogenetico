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
package org.gitia.algoritmogenetico;

import java.util.List;
import org.gitia.tipo3.population.Individuo;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
class Ruleta {

    /**
     * 
     * @param offspring para saber cuantos cruzamientos se requieren
     * @param population para poder seleccionar la elite en el cruzamiento
     * @return los indices de p1 y p2
     */
    static double[][] getParing(int offspring, List<Individuo> population) {
        int cant = (int)(offspring/2);
        double[][] indices = new double[cant][2];
        return indices;
    }
    
}
