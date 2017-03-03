package com.example.elabelle.cp282final.models;

import java.util.Calendar;

/**
 * Created by elabelle on 3/2/17.
 */

public class BaseNotebook {

    private Long id;
    private String name;
    private String description;
    private String color;
    private Category category;
    private int count;


    public BaseNotebook() {
        this.id = Calendar.getInstance().getTimeInMillis();
    }

    public BaseNotebook(BaseNotebook baseNotebook) {
        this(baseNotebook.getId(), baseNotebook.getName(), baseNotebook.getDescription(),
                baseNotebook.getColor());
    }

    public BaseNotebook(Long id, String title, String description, String color) {
        super();
        this.id = id;
        this.name = title;
        this.description = description;
        this.color = color;
    }

    public BaseNotebook(Long id, String title, String description, String color, Category category) {
        super();
        this.id = id;
        this.name = title;
        this.description = description;
        this.color = color;
        this.category = category;
    }


    public BaseNotebook(Long id, String title, String description, String color, Category category, int count) {
        super();
        this.id = id;
        this.name = title;
        this.description = description;
        this.color = color;
        this.category = category;
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


    public Category getCategory() {
        return category;
    }


    public void setCategory(Category category) {
        this.category = category;
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
            BaseNotebook n = (BaseNotebook) obj;
            return getColor().equals(n.getColor()) && getDescription().equals(n.getDescription()) && getName().equals
                    (n.getName()) && getId().equals(n.getId()) && getCategory().equals(n.getCategory());
        } catch (ClassCastException e) {
            return false;
        }
    }
}
