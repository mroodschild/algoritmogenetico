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
package org.gitia.ag.mutation;

import org.gitia.ag.mutation.MultiNonUniformMutation;
import org.ejml.simple.SimpleMatrix;
import static org.gitia.ag.mutation.MultiNonUniformMutation.r;
import org.junit.Test;
import org.junit.Ignore;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class MultiNonUniformMutationTest {

    public MultiNonUniformMutationTest() {
    }

    /**
     * Test of mutacion method, of class MultiNonUniformMutation.
     */
//    @Ignore
//    @Test
//    public void testMutacion() {
//        System.out.println("mutacion");
//        List<Individuo> population = null;
//        double mutation = 0.0;
//        double elite = 0.0;
//        double max = 0.0;
//        double min = 0.0;
//        int genAct = 0;
//        int getMax = 0;
//        MultiNonUniformMutation.mutacion(population, mutation, elite, max, min, genAct, getMax);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    @Ignore
    @Test
    public void testSetDir() {
        System.out.println("org.gitia.tipo3.mutation.MultiNonUniformMutationTest.testSetDir()");
        MultiNonUniformMutation nonUniformMutation = new MultiNonUniformMutation();

        SimpleMatrix v = SimpleMatrix.random_DDRM(1, 5, -1, 1, r);
        v.print();
        MultiNonUniformMutation.setDir(v);
        v.print();
    }

    @Ignore
    @Test
    public void testSetMaxChg() {
        System.out.println("org.gitia.tipo3.mutation.MultiNonUniformMutationTest.testSetMaxChg()");
        SimpleMatrix dir = SimpleMatrix.random_DDRM(1, 5, -1, 1, r);
        MultiNonUniformMutation.setDir(dir);
        SimpleMatrix vMaxChg = SimpleMatrix.random_DDRM(1, 5, -1, 1, r);
        vMaxChg.zero();
        SimpleMatrix ind = SimpleMatrix.random_DDRM(1, 5, -1, 1, r);
        double max = 1.5;
        double min = -1;
        System.out.println("max\t" + max + "\tmin\t" + min);
        System.out.println("dir");
        dir.print();
        System.out.println("max change");
        vMaxChg.print();
        System.out.println("ind");
        ind.print();

        MultiNonUniformMutation.setMaxChg(vMaxChg, dir, ind, max, min);

        System.out.println("Mostramos resultados");
        System.out.println("dir");
        dir.print();
        System.out.println("ind");
        ind.print();
        System.out.println("vChange");
        vMaxChg.print();
    }

    @Ignore
    @Test
    public void testStep() {
        System.out.println("org.gitia.tipo3.mutation.MultiNonUniformMutationTest.testStep()");
        SimpleMatrix vStep = SimpleMatrix.random_DDRM(1, 5, 0, 1, r);
        int genAct = 1;
        int genMax = 100;

        vStep.zero();
        vStep.print();
        System.out.println("genActual\t" + genAct + "\tgenMax\t" + genMax);
        MultiNonUniformMutation.step(vStep, genAct, genMax);
        vStep.print();

        genAct = 100;
        System.out.println("genActual\t" + genAct + "\tgenMax\t" + genMax);
        MultiNonUniformMutation.step(vStep, genAct, genMax);
        vStep.print();
    }
    
    @Ignore
    @Test
    public void sol() {
        System.out.println("org.gitia.tipo3.mutation.MultiNonUniformMutationTest.sol()");
        SimpleMatrix ind = SimpleMatrix.random_DDRM(1, 5, -1, 1, r);
        SimpleMatrix dir = SimpleMatrix.random_DDRM(1, 5, 0, 0, r);
        MultiNonUniformMutation.setDir(dir);
        SimpleMatrix maxChange = SimpleMatrix.random_DDRM(1, 5, 0, 0, r);
        MultiNonUniformMutation.setMaxChg(maxChange, dir, ind,1, -1);
        SimpleMatrix step = SimpleMatrix.random_DDRM(1, 5, 0, 0, r);
        MultiNonUniformMutation.step(step, 1, 100);
        
        System.out.println("ind");
        ind.print();
        System.out.println("dir");
        dir.print();
        System.out.println("maxChange");
        maxChange.print();
        System.out.println("step");
        step.print();
        SimpleMatrix sol = MultiNonUniformMutation.sol(ind, dir, step, maxChange);
        System.out.println("Sol");
        sol.print();
    }
}
