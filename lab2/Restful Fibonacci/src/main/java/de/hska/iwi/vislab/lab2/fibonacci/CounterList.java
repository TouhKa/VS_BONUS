package de.hska.iwi.vislab.lab2.fibonacci;

import java.util.List;

import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * used for returning a list of counters
 */
@XmlRootElement
public class CounterList {
        @XmlList
        public List<Integer> counters;
    }
