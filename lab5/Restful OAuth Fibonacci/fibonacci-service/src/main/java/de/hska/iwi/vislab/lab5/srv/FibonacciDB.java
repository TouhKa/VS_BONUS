package de.hska.iwi.vislab.lab5.srv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FibonacciDB {
    private static FibonacciDB instance = new FibonacciDB();



    private int lastId = 0;

    // a map of all running counters
    private Map<Integer, FibCounter> counters = new HashMap<>();

    public static FibonacciDB getInstance() {
        return instance;
    }

    public CounterList getAllCounters() {

        CounterList counterList = new CounterList();
        List<Integer> counterIds = new ArrayList<>();
        for (Integer id : counters.keySet()) {
            counterIds.add(id);
        }
        counterList.counters = counterIds;
        return counterList;

    }

    public String createCounter() {
        FibCounter newCounter = new FibCounter();
        counters.put(lastId++, newCounter);
        return lastId - 1 + "";
    }

    public int getCounterValue(Integer id) {
        return counters.get(id).getFib();
    }

    public String calculateNext(Integer id) {
        counters.get(id).calculateNext();
        return "Success!";
    }

    public String removeCounter(Integer id) {
        counters.remove(id);
        return "Counter removed!";
    }
}
