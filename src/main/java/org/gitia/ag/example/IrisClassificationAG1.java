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
package org.gitia.ag.example;

import org.ejml.simple.SimpleMatrix;
import org.gitia.ag.AG1;
import org.gitia.froog.Feedforward;
import org.gitia.froog.transferfunction.TransferFunction;
import org.gitia.jdataanalysis.CSV;
import org.gitia.jdataanalysis.data.stats.STD;
import org.gitia.ag.fitness.Fitness;
import org.gitia.ag.fitness.FitnessClassification;
import org.gitia.froog.layer.Dense;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class IrisClassificationAG1 {

    public static void main(String[] args) {

        SimpleMatrix in = CSV.open("src/main/resources/iris/iris-in.csv");
        SimpleMatrix out = CSV.open("src/main/resources/iris/iris-out.csv");

        STD std = new STD();
        std.fit(in);
        in = std.eval(in);

        in = in.transpose();
        out = out.transpose();

        Feedforward net = new Feedforward();
        net.addLayer(new Dense(4, 5, TransferFunction.TANSIG));
        net.addLayer(new Dense(5, 3, TransferFunction.SOFTMAX));

        Fitness fitness = new FitnessClassification();
        ((FitnessClassification) fitness).setInput(in);
        ((FitnessClassification) fitness).setOutput(out);
        ((FitnessClassification) fitness).setNet(net);

        AG1 ag = new AG1(150, 100, net.getParameters().getNumElements(), 0.6, 0.02, 0.15, 5, -1, 1);
        //AG1 ag = new AG1(100, 100, net.getParameters().getNumElements(), 0.6, 0.1, 0.02, 10);
        ag.setFitness(fitness);
        ag.run();
    }

}
