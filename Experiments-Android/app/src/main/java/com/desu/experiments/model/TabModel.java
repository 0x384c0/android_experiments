package com.desu.experiments.model;

import java.io.Serializable;
import java.util.ArrayList;

public class TabModel implements Serializable {
    public ArrayList<String> testActivityTitle = new ArrayList<>();
    public ArrayList<String> testActivitySubTitle = new ArrayList<>();
    public ArrayList<Class> activities = new ArrayList<>();
}
