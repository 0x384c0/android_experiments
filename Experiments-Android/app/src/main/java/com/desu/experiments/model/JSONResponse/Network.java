package com.desu.experiments.model.JSONResponse;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;

@Table(name = "Networks", id = "_id")
public class Network extends Model {
    @Expose
    @Column(name = "id")
    public String id;

    @Expose
    @Column(name = "title")
    public String title;

    @Column(name = "Vendor", onDelete = Column.ForeignKeyAction.CASCADE)
    public Vendor vendor;

    public Network() {
        super();
    }
}