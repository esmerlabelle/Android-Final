
package com.example.elabelle.cp282final.models;

//import com.google.gson.Gson;

import java.io.Serializable;


public class BaseNote implements Serializable {

    private String title;
    private String content;
    private Long creation;
    private Long lastModification;
    private Boolean archived;
    private Boolean trashed;
    private Notebook notebook;
    private Boolean checklist;
    public BaseNote() {
        super();
        this.title = "";
        this.content = "";
        this.archived = false;
        this.trashed = false;
        this.checklist = false;
    }


    public BaseNote(Long creation, Long lastModification, String title, String content, Integer archived,
                    Integer trashed, Notebook notebook, Integer checklist) {
        super();
        this.title = title;
        this.content = content;
        this.creation = creation;
        this.lastModification = lastModification;
        this.archived = archived == 1;
        this.trashed = trashed == 1;
        this.notebook = notebook;
        this.checklist = checklist == 1;
    }


    public BaseNote(BaseNote baseNote) {
        super();
        buildFromNote(baseNote);
    }


    private void buildFromNote(BaseNote baseNote) {
        setTitle(baseNote.getTitle());
        setContent(baseNote.getContent());
        setCreation(baseNote.getCreation());
        setLastModification(baseNote.getLastModification());
        setArchived(baseNote.isArchived());
        setTrashed(baseNote.isTrashed());
        setNotebook(baseNote.getNotebook());
        setChecklist(baseNote.isChecklist());
    }


    /*public void buildFromJson(String jsonNote) {
        Gson gson = new Gson();
        BaseNote noteFromJson = gson.fromJson(jsonNote, this.getClass());
        buildFromNote(noteFromJson);
    }*/


    public void set_id(Long _id) {
        this.creation = _id;
    }


    public Long get_id() {
        return creation;
    }


    public String getTitle() {
        if (title == null) return "";
        return title;
    }


    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }


    public String getContent() {
        if (content == null) return "";
        return content;
    }


    public void setContent(String content) {
        this.content = content == null ? "" : content;
    }


    public Long getCreation() {
        return creation;
    }


    public void setCreation(Long creation) {
        this.creation = creation;
    }


    public void setCreation(String creation) {
        Long creationLong;
        try {
            creationLong = Long.parseLong(creation);
        } catch (NumberFormatException e) {
            creationLong = null;
        }
        this.creation = creationLong;
    }


    public Long getLastModification() {
        return lastModification;
    }


    public void setLastModification(Long lastModification) {
        this.lastModification = lastModification;
    }


    public void setLastModification(String lastModification) {
        Long lastModificationLong;
        try {
            lastModificationLong = Long.parseLong(lastModification);
        } catch (NumberFormatException e) {
            lastModificationLong = null;
        }
        this.lastModification = lastModificationLong;
    }


    public Boolean isArchived() {
        return !(archived == null || !archived);
    }


    public void setArchived(Boolean archived) {
        this.archived = archived;
    }


    public void setArchived(int archived) {
        this.archived = archived == 1;
    }


    public Boolean isTrashed() {
        return !(trashed == null || !trashed);
    }


    public void setTrashed(Boolean trashed) {
        this.trashed = trashed;
    }


    public void setTrashed(int trashed) {
        this.trashed = trashed == 1;
    }


    public Notebook getNotebook() {
        return notebook;
    }


    public void setNotebook(Notebook notebook) {
        this.notebook = notebook;
    }


    public Boolean isChecklist() {
        return !(checklist == null || !checklist);
    }


    public void setChecklist(Boolean checklist) {
        this.checklist = checklist;
    }


    public void setChecklist(int checklist) {
        this.checklist = checklist == 1;
    }


    @Override
    public boolean equals(Object o) {
        boolean res = false;
        BaseNote baseNote;
        try {
            baseNote = (BaseNote) o;
        } catch (Exception e) {
            return res;
        }

        /*Object[] a = {getTitle(), getContent(), getCreation(), getLastModification(), isArchived(),
                isTrashed(), getCategory(), isChecklist() };
        Object[] b = {baseNote.getTitle(), baseNote.getContent(), baseNote.getCreation(),
                baseNote.getLastModification(), baseNote.isArchived(), baseNote.isTrashed(),
                baseNote.getCategory(), baseNote.isChecklist()};
        if (EqualityChecker.check(a, b)) {
            res = true;
        }*/

        return res;
    }


    public boolean isChanged(BaseNote note) {
        return !equals(note);
    }


    public boolean isEmpty() {
        BaseNote emptyNote = new BaseNote();
        // Field to exclude for comparison
        emptyNote.setCreation(getCreation());
        emptyNote.setNotebook(getNotebook());
        // Check
        return !isChanged(emptyNote);
    }


    public String toString() {
        return getTitle();
    }


    /*public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }*/
}