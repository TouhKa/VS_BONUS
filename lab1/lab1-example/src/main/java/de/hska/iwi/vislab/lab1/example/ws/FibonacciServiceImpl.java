package de.hska.iwi.vislab.lab1.example.ws;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(endpointInterface = "de.hska.iwi.vislab.lab1.example.ws.FibonacciServiceIntf")
public class FibonacciServiceImpl implements FibonacciServiceIntf {
    static int n1=0,n2=1,n3=0;
    //TOOD @Override interface methods
    @Override
    public int getFibonacci(@WebParam(name = "n") int n){
        return calculateNthFibonacci(n);
    }

    private int calculateNthFibonacci(int n) {
        if(n>0){
               this.n3 = this.n1 + this.n2;
               this.n1 = this.n2;
               this.n2 = this.n3;
           }
        return this.n3;
    }
}
