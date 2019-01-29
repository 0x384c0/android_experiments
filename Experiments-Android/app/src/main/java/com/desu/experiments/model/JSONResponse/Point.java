package com.desu.experiments.model.JSONResponse;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.List;

@Table(name = "Points", id = "_id")
public class Point extends Model {

    @Expose
    @Column(name = "id")
    public String id;

    @Expose
    @Column(name = "title")
    public String title;

    @Expose
    public Coordinate coordinate;

    @Expose
    @Column(name = "phone")
    public String phone;

    @Expose
    @Column(name = "address")
    public String address;

    @Expose
    public Vendor vendor;

    @Expose
    public List<Features> features;

    @Column(name = "JSONResponse", onDelete = Column.ForeignKeyAction.CASCADE)
    public JSONResponse jsonResponse;


    public Coordinate getCoordinate(){
        return new Select().from(Coordinate.class).where(Cache.getTableName(Coordinate.class) + ".Point = '" + this.getId() + "'").executeSingle();
    }

    public Vendor getVendor(){
        return new Select().from(Vendor.class).where(Cache.getTableName(Vendor.class) + ".Point = '" + this.getId() + "'").executeSingle();
    }

    public List<Features> getFeaturesList(){
        return getMany(Features.class, "Point");//new Select().from(Features.class).where("Features.Point = '" + this.getId() + "'").execute();
    }

    public Point() {
        super();
    }
}
