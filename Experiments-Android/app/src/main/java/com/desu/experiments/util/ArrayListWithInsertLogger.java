package com.desu.experiments.util;

import java.util.ArrayList;

public class ArrayListWithInsertLogger<E> {
    boolean wasInserted = false;
    int positionStart;
    int itemCount;

    public ArrayList<E> arrayList;

    public ArrayListWithInsertLogger(ArrayList<E> arrayList){
        setArrayList(arrayList);
    }

    public ArrayList<E> getArrayList() {
        return arrayList;
    }
    public void setArrayList(ArrayList<E> arrayList) {
        wasInserted = false;
        positionStart = 0;
        itemCount = arrayList.size();
        this.arrayList = arrayList;
    }
    public void addAllToArrayList(ArrayList<E> arrayList) {
        wasInserted = true;
        positionStart = this.arrayList.size();
        itemCount = arrayList.size();
        this.arrayList.addAll(arrayList);
    }
    public boolean isItemsWasInserted() {
        return wasInserted;
    }


    public void resetLog(){
        wasInserted = false;
        positionStart = arrayList.size();
        itemCount = 0;
    }


    public int getPositionStart() {
        return positionStart;
    }
    public int getItemCount() {
        return itemCount;
    }
}
