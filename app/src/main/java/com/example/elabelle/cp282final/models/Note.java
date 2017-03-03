package com.example.elabelle.cp282final.models;

import android.os.Parcel;
import android.os.Parcelable;

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
                Integer trashed, Notebook notebook, Integer checklist) {
        super(creation, lastModification, title, content, archived, trashed, notebook, checklist);
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
        setNotebook((Notebook) in.readParcelable(Notebook.class.getClassLoader()));
        setChecklist(in.readInt());
    }

    public boolean isPasswordChecked() {
        return passwordChecked;
    }


    public void setPasswordChecked(boolean passwordChecked) {
        this.passwordChecked = passwordChecked;
    }


    public Notebook getNotebook() {
        try {
            return (Notebook) super.getNotebook();
        } catch (ClassCastException e) {
            return new Notebook(super.getNotebook());
        }
    }


    public void setNotebook(Notebook notebook) {
        if (notebook != null && notebook.getClass().equals(BaseCategory.class)) {
            setNotebook(new Notebook(notebook));
        }
        super.setNotebook(notebook);
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
        parcel.writeParcelable(getNotebook(), 0);
        parcel.writeInt(isChecklist() ? 1 : 0);
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
