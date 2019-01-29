package com.desu.experiments.model.JSONResponse;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

@Table(name = "Vendors", id = "_id")
public class Vendor extends Model {
    @Expose
    @Column(name = "id")
    public String id;

    @Expose
    @Column(name = "title")
    public String title;

    @Expose
    public Network network;

    @Column(name = "Point", onDelete = Column.ForeignKeyAction.CASCADE)
    public Point point;


    public Network getNetwork(){
        return new Select().from(Network.class).where("vendor = '" + this.getId() + "'").executeSingle();
    }
    public Vendor() {
        super();
    }
}
