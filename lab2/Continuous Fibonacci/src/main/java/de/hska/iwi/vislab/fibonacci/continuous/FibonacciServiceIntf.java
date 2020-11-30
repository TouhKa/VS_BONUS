package de.hska.iwi.vislab.fibonacci.continuous;

import javax.jws.*;
@WebService
public interface FibonacciServiceIntf {
    //returns the next fibonacci number
    int getNextFibonacci();
}
