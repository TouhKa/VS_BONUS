package de.hska.iwi.vislab.lab2.fibonacci;

public class FibCounter {

        // the last number
        private int lastNumber;

        private int currentNumber;

        //0, 1, 1, 2, 3, 5, 8, 13, ...
        public FibCounter() {
            lastNumber = 1;
            currentNumber = 0;
        }

        public int getFib() {
            return currentNumber;
        }

        public void calculateNext() {
            int currentNumberOfLastStep = currentNumber;
            currentNumber = currentNumber + lastNumber;
            lastNumber = currentNumberOfLastStep;

        }

    }