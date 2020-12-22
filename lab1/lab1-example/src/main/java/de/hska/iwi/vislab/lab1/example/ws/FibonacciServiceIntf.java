package de.hska.iwi.vislab.lab1.example.ws;

import javax.jws.*;
@WebService
public interface FibonacciServiceIntf {
    //takes an integer n as its parameter
    // and returns the nth number of the Fibonacci row as integer result
    int getFibonacci(@WebParam(name = "n") int n);
}
