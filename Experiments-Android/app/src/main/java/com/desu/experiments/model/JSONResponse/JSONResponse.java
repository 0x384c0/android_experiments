package com.desu.experiments.model.JSONResponse;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;

import java.util.List;

@Table(name = "JSONResponse", id = "_id")
public class JSONResponse extends Model {
    @Expose
    public List<Point> points;

    public List<Point> getPoints() {
        return getMany(Point.class, "JSONResponse");
    }

    public JSONResponse() {
        super();
    }

}

