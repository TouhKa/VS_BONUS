package de.hska.iwi.vislab.fibonacci.continuous;

import javax.jws.WebService;

@WebService(endpointInterface = "de.hska.iwi.vislab.fibonacci.continuous.FibonacciServiceIntf")
public class FibonacciServiceImpl implements FibonacciServiceIntf {

    // the next-to-last number
    private int penultimateNumber;
    // the last number
    private int ultimateNumber;

    public FibonacciServiceImpl() {
        penultimateNumber = -1;
        ultimateNumber = 1;
    }

    @Override
    public int getNextFibonacci() {
        int nextFibonacci = penultimateNumber + ultimateNumber;
        this.penultimateNumber = ultimateNumber;
        this.ultimateNumber = nextFibonacci;
        return nextFibonacci;
    }

    @Override
    public String resetSequence() {
        penultimateNumber = -1;
        ultimateNumber = 1;
        return "Sequence reset successful!";
    }

    
}
