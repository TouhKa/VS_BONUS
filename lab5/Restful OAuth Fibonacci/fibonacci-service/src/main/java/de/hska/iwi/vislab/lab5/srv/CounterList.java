package de.hska.iwi.vislab.lab5.srv;

import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * used for returning a list of counters
 */
@XmlRootElement
public class CounterList {
    @XmlList
    public List<Integer> counters;
}
