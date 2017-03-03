package com.example.elabelle.cp282final.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by elabelle on 2/19/17.
 */

public class Notebook extends BaseNotebook implements Parcelable {

    private Notebook(Parcel in) {
        setId(in.readLong());
        setName(in.readString());
        setDescription(in.readString());
        setColor(in.readString());
    }


    public Notebook() {
        super();
    }


    public Notebook(BaseNotebook notebook) {
        super(notebook.getId(), notebook.getName(), notebook.getDescription(), notebook.getColor(), notebook.getCategory());
    }

    public Notebook(Long id, String title, String description, String color) {
        super(id, title, description, color);
    }

    public Notebook(Long id, String title, String description, String color, Category category) {
        super(id, title, description, color, category);
    }


    public Notebook(Long id, String title, String description, String color, Category category, int count) {
        super(id, title, description, color, category, count);
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(getId());
        parcel.writeString(getName());
        parcel.writeString(getDescription());
        parcel.writeString(getColor());
    }


    @Override
    public String toString() {
        return getName();
    }


    /*
     * Parcelable interface must also have a static field called CREATOR, which is an object implementing the
     * Parcelable.Creator interface. Used to un-marshal or de-serialize object from Parcel.
     */
    public static final Parcelable.Creator<Notebook> CREATOR = new Parcelable.Creator<Notebook>() {

        public Notebook createFromParcel(Parcel in) {
            return new Notebook(in);
        }


        public Notebook[] newArray(int size) {
            return new Notebook[size];
        }
    };
}
