package com.desu.experiments.model.JSONResponse;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;

@Table(name = "Coordinates", id = "_id")
public class Coordinate extends Model {
    @Expose
    @Column(name = "lat")
    public String lat;

    @Expose
    @Column(name = "lng")
    public String lng;

    @Column(name = "Point", onDelete = Column.ForeignKeyAction.CASCADE)
    public Point point;

    public Coordinate() {
        super();
    }
}
