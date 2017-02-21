package com.example.elabelle.cp282final.models;

import java.util.Calendar;


public class BaseCategory {

    private Long id;
    private String name;
    private String description;
    private String color;
    private int count;


    public BaseCategory() {
        this.id = Calendar.getInstance().getTimeInMillis();
    }


    public BaseCategory(BaseCategory baseCategory) {
        this(baseCategory.getId(), baseCategory.getName(), baseCategory.getDescription(), baseCategory.getColor());
    }


    public BaseCategory(Long id, String title, String description, String color) {
        super();
        this.id = id;
        this.name = title;
        this.description = description;
        this.color = color;
    }


    public BaseCategory(Long id, String title, String description, String color, int count) {
        super();
        this.id = id;
        this.name = title;
        this.description = description;
        this.color = color;
        this.count = count;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String title) {
        this.name = title;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getColor() {
        return color;
    }


    public void setColor(String color) {
        this.color = color;
    }


    public int getCount() {
        return count;
    }


    public void setCount(int count) {
        this.count = count;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        try {
            BaseCategory c = (BaseCategory) obj;
            return getColor().equals(c.getColor()) && getDescription().equals(c.getDescription()) && getName().equals
                    (c.getName()) && getId().equals(c.getId());
        } catch (ClassCastException e) {
            return false;
        }
    }
}
