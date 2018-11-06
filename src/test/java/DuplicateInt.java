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

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class DuplicateInt {

    public static void main(String[] args) {
        int numbers[] = {1, 2, 3, 4, 1, 2, 3, 4, 5, 1, 2, 3, 4};
        for (int i = 0; i < numbers.length; i++) {
            int number = numbers[i];
            System.out.printf("%d\t",number);
        }
        System.out.println("");

        numbers = java.util.stream.IntStream.of(numbers).distinct().toArray();
        
        for (int i = 0; i < numbers.length; i++) {
            int number = numbers[i];
            System.out.printf("%d\t",number);
        }
    }
}
