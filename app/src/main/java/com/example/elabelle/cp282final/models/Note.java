package com.example.elabelle.cp282final.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elabelle on 2/19/17.
 */

public class Note extends BaseNote implements Parcelable {
    // Not saved in DB
    private boolean passwordChecked = false;


    public Note() {
        super();
    }


    public Note(Long creation, Long lastModification, String title, String content, Integer archived,
                Integer trashed, Category category, Integer checklist) {
        super(creation, lastModification, title, content, archived, trashed, category, checklist);
    }


    public Note(Note note) {
        super(note);
        setPasswordChecked(note.isPasswordChecked());
    }


    private Note(Parcel in) {
        setCreation(in.readString());
        setLastModification(in.readString());
        setTitle(in.readString());
        setContent(in.readString());
        setArchived(in.readInt());
        setTrashed(in.readInt());
        setCategory((Category) in.readParcelable(Category.class.getClassLoader()));
        setChecklist(in.readInt());
        in.readList(getAttachmentsList(), Attachment.class.getClassLoader());
    }


    public List<Attachment> getAttachmentsList() {
        return (List<Attachment>) super.getAttachmentsList();
    }


    public void setAttachmentsList(ArrayList<Attachment> attachmentsList) {
        super.setAttachmentsList(attachmentsList);
    }


    public void addAttachment(Attachment attachment) {
        List<Attachment> attachmentsList = ((List<Attachment>) super.getAttachmentsList());
        attachmentsList.add(attachment);
        setAttachmentsList(attachmentsList);
    }


    public void removeAttachment(Attachment attachment) {
        List<Attachment> attachmentsList = ((List<Attachment>) super.getAttachmentsList());
        attachmentsList.remove(attachment);
        setAttachmentsList(attachmentsList);
    }


    public List<Attachment> getAttachmentsListOld() {
        return (List<Attachment>) super.getAttachmentsListOld();
    }


    public void setAttachmentsListOld(ArrayList<Attachment> attachmentsListOld) {
        super.setAttachmentsListOld(attachmentsListOld);
    }


    public boolean isPasswordChecked() {
        return passwordChecked;
    }


    public void setPasswordChecked(boolean passwordChecked) {
        this.passwordChecked = passwordChecked;
    }


    public Category getCategory() {
        try {
            return (Category) super.getCategory();
        } catch (ClassCastException e) {
            return new Category(super.getCategory());
        }
    }


    public void setCategory(Category category) {
        if (category != null && category.getClass().equals(BaseCategory.class)) {
            setCategory(new Category(category));
        }
        super.setCategory(category);
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(String.valueOf(getCreation()));
        parcel.writeString(String.valueOf(getLastModification()));
        parcel.writeString(getTitle());
        parcel.writeString(getContent());
        parcel.writeInt(isArchived() ? 1 : 0);
        parcel.writeInt(isTrashed() ? 1 : 0);
        parcel.writeParcelable(getCategory(), 0);
        parcel.writeInt(isChecklist() ? 1 : 0);
        parcel.writeList(getAttachmentsList());
    }


    /*
     * Parcelable interface must also have a static field called CREATOR, which is an object implementing the
     * Parcelable.Creator interface. Used to un-marshal or de-serialize object from Parcel.
     */
    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {

        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }


        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
