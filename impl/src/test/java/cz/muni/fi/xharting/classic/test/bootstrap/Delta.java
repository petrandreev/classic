package cz.muni.fi.xharting.classic.test.bootstrap;

import java.io.Serializable;

public class Delta implements Serializable {
    
    private static final long serialVersionUID = -1326164808701104317L;
    
    private String name;

    public Delta() {
    }

    public Delta(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
