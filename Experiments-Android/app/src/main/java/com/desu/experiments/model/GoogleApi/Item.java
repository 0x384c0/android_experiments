package com.desu.experiments.model.GoogleApi;

import java.io.Serializable;

public class Item implements Serializable {
    public String kind;
    public String title;
    public String displayLink;
    public Image image = new Image();
}
