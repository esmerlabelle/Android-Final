
package com.example.elabelle.cp282final.models;

//import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BaseNote implements Serializable {

    private String title;
    private String content;
    private Long creation;
    private Long lastModification;
    private Boolean archived;
    private Boolean trashed;
    private Category category;
    private Boolean checklist;
    private List<? extends BaseAttachment> attachmentsList = new ArrayList<BaseAttachment>();
    private List<? extends BaseAttachment> attachmentsListOld = new ArrayList<BaseAttachment>();

    public BaseNote() {
        super();
        this.title = "";
        this.content = "";
        this.archived = false;
        this.trashed = false;
        this.checklist = false;
    }


    public BaseNote(Long creation, Long lastModification, String title, String content, Integer archived,
                    Integer trashed, Category category, Integer checklist) {
        super();
        this.title = title;
        this.content = content;
        this.creation = creation;
        this.lastModification = lastModification;
        this.archived = archived == 1;
        this.trashed = trashed == 1;
        this.category = category;
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
        setCategory(baseNote.getCategory());
        setChecklist(baseNote.isChecklist());
        ArrayList<BaseAttachment> list = new ArrayList<BaseAttachment>();
        for (BaseAttachment mAttachment : baseNote.getAttachmentsList()) {
            list.add(mAttachment);
        }
        setAttachmentsList(list);
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

    public Category getCategory() {
        return category;
    }


    public void setCategory(Category category) {
        this.category = category;
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


    public List<? extends BaseAttachment> getAttachmentsList() {
        return attachmentsList;
    }


    public void setAttachmentsList(List<? extends BaseAttachment> attachmentsList) {
        this.attachmentsList = attachmentsList;
    }


    public void backupAttachmentsList() {
        List<BaseAttachment> attachmentsListOld = new ArrayList<BaseAttachment>();
        for (BaseAttachment mAttachment : getAttachmentsList()) {
            attachmentsListOld.add(mAttachment);
        }
        this.attachmentsListOld = attachmentsListOld;
    }


    public List<? extends BaseAttachment> getAttachmentsListOld() {
        return attachmentsListOld;
    }


    public void setAttachmentsListOld(List<? extends BaseAttachment> attachmentsListOld) {
        this.attachmentsListOld = attachmentsListOld;
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
                isTrashed(), getBaseCategory(), isChecklist() };
        Object[] b = {baseNote.getTitle(), baseNote.getContent(), baseNote.getCreation(),
                baseNote.getLastModification(), baseNote.isArchived(), baseNote.isTrashed(),
                baseNote.getBaseCategory(), baseNote.isChecklist()};
        if (EqualityChecker.check(a, b)) {
            res = true;
        }*/

        return res;
    }


    public boolean isChanged(BaseNote note) {
        return !equals(note) || !getAttachmentsList().equals(note.getAttachmentsList());
    }


    public boolean isEmpty() {
        BaseNote emptyNote = new BaseNote();
        // Field to exclude for comparison
        emptyNote.setCreation(getCreation());
        emptyNote.setCategory(getCategory());
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