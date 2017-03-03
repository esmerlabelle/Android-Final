package com.example.elabelle.cp282final.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteReadOnlyDatabaseException;
import android.util.Log;

import com.example.elabelle.cp282final.CP282Final;
import com.example.elabelle.cp282final.models.Category;
import com.example.elabelle.cp282final.models.Note;
import com.example.elabelle.cp282final.models.Notebook;
import com.example.elabelle.cp282final.models.Tag;
import com.example.elabelle.cp282final.utils.AssetUtils;
import com.example.elabelle.cp282final.utils.Constants;
import com.example.elabelle.cp282final.utils.Navigation;
import com.example.elabelle.cp282final.utils.SQLParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * Created by elabelle on 2/26/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    // Database name
    private static final String DATABASE_NAME = "CP282Final";
    // Database version aligned if possible to software version
    private static final int DATABASE_VERSION = 2;
    // Sql query file directory
    private static final String SQL_DIR = "sql";

    // Notes table name
    public static final String TABLE_NOTES = "notes";
    // Notes table columns
    public static final String KEY_ID = "note_id";
    public static final String KEY_CREATION = "creation";
    public static final String KEY_LAST_MODIFICATION = "last_modification";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_ARCHIVED = "archived";
    public static final String KEY_TRASHED = "trashed";
    public static final String KEY_NOTEBOOK = "notebook_id";
    public static final String KEY_CHECKLIST = "checklist";

    // Notebook table name
    public static final String TABLE_NOTEBOOK = "notebooks";
    // Notebook table columns
    public static final String KEY_NOTEBOOK_ID = "notebook_id";
    public static final String KEY_NOTEBOOK_NAME = "name";
    public static final String KEY_NOTEBOOK_DESCRIPTION = "description";
    public static final String KEY_NOTEBOOK_COLOR = "color";
    public static final String KEY_NOTEBOOK_CATEGORY_ID = "category_id";

    // Categories table name
    public static final String TABLE_CATEGORY = "categories";
    // Categories table columns
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_CATEGORY_NAME = "name";
    public static final String KEY_CATEGORY_DESCRIPTION = "description";
    public static final String KEY_CATEGORY_COLOR = "color";

    // Tags table name
    public static final String TABLE_TAG = "tags";
    // Tags table columns
    public static final String KEY_TAG_ID = "tag_id";
    public static final String KEY_TAG_NAME = "name";

    // Tagmap table name
    public static final String TABLE_TAGMAP = "tagmap";
    // Tagmap table columns
    public static final String KEY_TAGMAP_ID = "tagmap_id";
    public static final String KEY_TAGMAP_NOTE_ID = "note_id";
    public static final String KEY_TAGMAP_TAG_ID = "tag_id";

    // Queries
    private static final String CREATE_QUERY = "create.sql";
    private static final String UPGRADE_QUERY_PREFIX = "upgrade-";
    private static final String UPGRADE_QUERY_SUFFIX = ".sql";


    private final Context mContext;
    private final SharedPreferences prefs;

    private static DbHelper instance = null;
    private SQLiteDatabase db;


    public static synchronized DbHelper getInstance() {
        return getInstance(CP282Final.getAppContext());
    }

    public static synchronized DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context);
        }
        return instance;
    }


    private DbHelper(Context mContext) {
        super(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = mContext;
        this.prefs = mContext.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_MULTI_PROCESS);
    }


    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public SQLiteDatabase getDatabase() {
        return getDatabase(false);
    }


    public SQLiteDatabase getDatabase(boolean forceWritable) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            if (forceWritable && db.isReadOnly()) {
                throw new SQLiteReadOnlyDatabaseException("Required writable database, obtained read-only");
            }
            return db;
        } catch (IllegalStateException e) {
            return this.db;
        }
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.i(Constants.TAG, "Database creation");
            execSqlFile(CREATE_QUERY, db);
        } catch (IOException exception) {
            throw new RuntimeException("Database creation failed", exception);
        }
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.db = db;
        Log.i(Constants.TAG, "Upgrading database version from " + oldVersion + " to " + newVersion);

        UpgradeProcessor.process(oldVersion, newVersion);

        try {
            for (String sqlFile : AssetUtils.list(SQL_DIR, mContext.getAssets())) {
                if (sqlFile.startsWith(UPGRADE_QUERY_PREFIX)) {
                    int fileVersion = Integer.parseInt(sqlFile.substring(UPGRADE_QUERY_PREFIX.length(),
                            sqlFile.length() - UPGRADE_QUERY_SUFFIX.length()));
                    if (fileVersion > oldVersion && fileVersion <= newVersion) {
                        execSqlFile(sqlFile, db);
                    }
                }
            }
            Log.i(Constants.TAG, "Database upgrade successful");

        } catch (IOException e) {
            throw new RuntimeException("Database upgrade failed", e);
        }
    }


    // Inserting or updating single note
    public Note updateNote(Note note, boolean updateLastModification) {
        SQLiteDatabase db = getDatabase(true);

        String content = note.getContent();

        // To ensure note and attachments insertions are atomical and boost performances transaction are used
        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, content);
        values.put(KEY_CREATION, note.getCreation() != null ? note.getCreation() : Calendar.getInstance()
                .getTimeInMillis());
        values.put(KEY_LAST_MODIFICATION, updateLastModification ? Calendar
                .getInstance().getTimeInMillis() : (note.getLastModification() != null ? note.getLastModification() :
                Calendar
                        .getInstance().getTimeInMillis()));
        values.put(KEY_ARCHIVED, note.isArchived());
        values.put(KEY_TRASHED, note.isTrashed());
        values.put(KEY_NOTEBOOK, note.getNotebook() != null ? note.getNotebook().getId() : null);
        boolean checklist = note.isChecklist() != null ? note.isChecklist() : false;
        values.put(KEY_CHECKLIST, checklist);

        db.insertWithOnConflict(TABLE_NOTES, KEY_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(Constants.TAG, "Updated note titled '" + note.getTitle() + "'");


        db.setTransactionSuccessful();
        db.endTransaction();

        // Fill the note with correct data before returning it
        note.setCreation(note.getCreation() != null ? note.getCreation() : values.getAsLong(KEY_CREATION));
        note.setLastModification(values.getAsLong(KEY_LAST_MODIFICATION));

        return note;
    }


    protected void execSqlFile(String sqlFile, SQLiteDatabase db) throws SQLException, IOException {
        Log.i(Constants.TAG, "  exec sql file: {}" + sqlFile);
        for (String sqlInstruction : SQLParser.parseSqlFile(SQL_DIR + "/" + sqlFile, mContext.getAssets())) {
            Log.v(Constants.TAG, "    sql: {}" + sqlInstruction);
            try {
                db.execSQL(sqlInstruction);
            } catch (Exception e) {
                Log.e(Constants.TAG, "Error executing command: " + sqlInstruction, e);
            }
        }
    }


    /**
     * Getting single note
     */
    public Note getNote(long id) {

        String whereCondition = " WHERE "
                + KEY_ID + " = " + id;

        List<Note> notes = getNotes(whereCondition, true);
        Note note;
        if (notes.size() > 0) {
            note = notes.get(0);
        } else {
            note = null;
        }
        return note;
    }


    /**
     * Getting All notes
     *
     * @param checkNavigation Tells if navigation status (notes, archived) must be kept in
     *                        consideration or if all notes have to be retrieved
     * @return Notes list
     */
    public List<Note> getAllNotes(Boolean checkNavigation) {
        String whereCondition = "";
        if (checkNavigation) {
            int navigation = Navigation.getNavigation();
            switch (navigation) {
                case Navigation.HOME:
                    return getNotesActive();
                case Navigation.ARCHIVE:
                    return getNotesArchived();
                case Navigation.TRASH:
                    return getNotesTrashed();
                case Navigation.UNCATEGORIZED:
                    return getNotesUncategorized();
                case Navigation.CATEGORY:
                    return getNotesByCategory(Navigation.getCategory());
                default:
                    return getNotes(whereCondition, true);
            }
        } else {
            return getNotes(whereCondition, true);
        }

    }


    public List<Note> getNotesActive() {
        String whereCondition = " WHERE " + KEY_ARCHIVED + " IS NOT 1 AND " + KEY_TRASHED + " IS NOT 1 ";
        return getNotes(whereCondition, true);
    }


    public List<Note> getNotesArchived() {
        String whereCondition = " WHERE " + KEY_ARCHIVED + " = 1 AND " + KEY_TRASHED + " IS NOT 1 ";
        return getNotes(whereCondition, true);
    }


    public List<Note> getNotesTrashed() {
        String whereCondition = " WHERE " + KEY_TRASHED + " = 1 ";
        return getNotes(whereCondition, true);
    }


    public List<Note> getNotesUncategorized() {
        String whereCondition = " WHERE "
                + "(" + KEY_CATEGORY_ID + " IS NULL OR " + KEY_CATEGORY_ID + " == 0) "
                + "AND " + KEY_TRASHED + " IS NOT 1";
        return getNotes(whereCondition, true);
    }


    /**
     * Counts words in a note
     */
    public int getWords(Note note) {
        int count = 0;
        String[] fields = {note.getTitle(), note.getContent()};
        for (String field : fields) {
            boolean word = false;
            int endOfLine = field.length() - 1;
            for (int i = 0; i < field.length(); i++) {
                // if the char is a letter, word = true.
                if (Character.isLetter(field.charAt(i)) && i != endOfLine) {
                    word = true;
                    // if char isn't a letter and there have been letters before,
                    // counter goes up.
                } else if (!Character.isLetter(field.charAt(i)) && word) {
                    count++;
                    word = false;
                    // last word of String; if it doesn't end with a non letter, it
                    // wouldn't count without this.
                } else if (Character.isLetter(field.charAt(i)) && i == endOfLine) {
                    count++;
                }
            }
        }
        return count;
    }


    /**
     * Counts chars in a note
     */
    public int getChars(Note note) {
        int count = 0;
        count += note.getTitle().length();
        count += note.getContent().length();
        return count;
    }


    /**
     * Common method for notes retrieval. It accepts a query to perform and returns matching records.
     */
    public List<Note> getNotes(String whereCondition, boolean order) {
        List<Note> noteList = new ArrayList<>();

        String sort_column, sort_order = "";

        // Getting sorting criteria from preferences. Reminder screen forces sorting.
        sort_column = prefs.getString(Constants.PREF_SORTING_COLUMN, KEY_TITLE);
        if (order) {
            sort_order = KEY_TITLE.equals(sort_column) ? " ASC " : " DESC ";
        }

        // In case of title sorting criteria it must be handled empty title by concatenating content
        sort_column = KEY_TITLE.equals(sort_column) ? KEY_TITLE + "||" + KEY_CONTENT : sort_column;

        // Generic query to be specialized with conditions passed as parameter
        String noteQuery = "SELECT "
                + KEY_CREATION + ","
                + KEY_LAST_MODIFICATION + ","
                + KEY_TITLE + ","
                + KEY_CONTENT + ","
                + KEY_ARCHIVED + ","
                + KEY_TRASHED + ","
                + KEY_CHECKLIST + ","
                + KEY_NOTEBOOK + ","
                + KEY_NOTEBOOK_NAME + ","
                + KEY_NOTEBOOK_DESCRIPTION + ","
                + KEY_NOTEBOOK_COLOR
                + " FROM " + TABLE_NOTES
                + " LEFT JOIN " + TABLE_NOTEBOOK + " USING( " + KEY_NOTEBOOK + ") "
                + whereCondition
                + (order ? " ORDER BY " + sort_column + sort_order : "");

        Log.v(Constants.TAG, "Query: " + noteQuery);

        Cursor cursor = null;
        try {
            cursor = getDatabase().rawQuery(noteQuery, null);

            // Looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    int i = 0;
                    Note note = new Note();
                    note.setCreation(cursor.getLong(i++));
                    note.setLastModification(cursor.getLong(i++));
                    note.setTitle(cursor.getString(i++));
                    note.setContent(cursor.getString(i++));
                    note.setArchived("1".equals(cursor.getString(i++)));
                    note.setTrashed("1".equals(cursor.getString(i++)));
                    note.setChecklist("1".equals(cursor.getString(i++)));

                    // Set notebook
                    long notebookId = cursor.getLong(i++);
                    if (notebookId != 0) {
                        Notebook notebook = new Notebook(notebookId, cursor.getString(i++),
                                cursor.getString(i++), cursor.getString(i++));
                        note.setNotebook(notebook);
                    }

                    // Adding note to list
                    noteList.add(note);

                } while (cursor.moveToNext());
            }

        } finally {
            if (cursor != null)
                cursor.close();
        }

        Log.v(Constants.TAG, "Query: Retrieval finished!");
        return noteList;
    }


    /**
     * Archives/restore single note
     */
    public void archiveNote(Note note, boolean archive) {
        note.setArchived(archive);
        updateNote(note, false);
    }


    /**
     * Trashes/restore single note
     */
    public void trashNote(Note note, boolean trash) {
        note.setTrashed(trash);
        updateNote(note, false);
    }


    /**
     * Deleting single note
     */
    public boolean deleteNote(Note note) {
        return deleteNote(note, false);
    }


    /**
     * Deleting single note but keeping attachments
     */
    public boolean deleteNote(Note note, boolean keepAttachments) {
        int deletedNotes;
        boolean result = true;
        SQLiteDatabase db = getDatabase(true);
        // Delete notes
        deletedNotes = db.delete(TABLE_NOTES, KEY_ID + " = ?", new String[]{String.valueOf(note.get_id())});
        // Check on correct and complete deletion
        result = result && deletedNotes == 1;
        return result;
    }


    /**
     * Empties trash deleting all trashed notes
     */
    public void emptyTrash() {
        for (Note note : getNotesTrashed()) {
            deleteNote(note);
        }
    }


    public List<Note> getChecklists() {
        String whereCondition = " WHERE " + KEY_CHECKLIST + " = 1";
        return getNotes(whereCondition, false);
    }


    /**
     * Retrieves all notes related to Category it passed as parameter
     *
     * @param categoryId Category integer identifier
     * @return List of notes with requested category
     */
    public List<Note> getNotesByCategory(Long categoryId) {
        List<Note> notes;
        boolean filterArchived = prefs.getBoolean(Constants.PREF_FILTER_ARCHIVED_IN_CATEGORIES + categoryId, false);
        try {
            String whereCondition = " WHERE "
                    + KEY_CATEGORY_ID + " = " + categoryId
                    + " AND " + KEY_TRASHED + " IS NOT 1"
                    + (filterArchived ? " AND " + KEY_ARCHIVED + " IS NOT 1" : "");
            notes = getNotes(whereCondition, true);
        } catch (NumberFormatException e) {
            notes = getAllNotes(true);
        }
        return notes;
    }


    /**
     * Retrieves all tags
     */
    public List<Tag> getTags() {
        return getTags(null);
    }


    /**
     * Retrieves all tags of a specified note
     */
    public List<Tag> getTags(Note note) {
        List<Tag> tags = new ArrayList<>();
        HashMap<String, Integer> tagsMap = new HashMap<>();

        String whereCondition = " WHERE "
                + (note != null ? KEY_ID + " = " + note.get_id() + " AND " : "")
                + "(" + KEY_CONTENT + " LIKE '%#%' OR " + KEY_TITLE + " LIKE '%#%' " + ")"
                + " AND " + KEY_TRASHED + " IS " + (Navigation.checkNavigation(Navigation.TRASH) ? "" : " NOT ") + " 1";
        List<Note> notesRetrieved = getNotes(whereCondition, true);

        for (Note noteRetrieved : notesRetrieved) {
            HashMap<String, Integer> tagsRetrieved = TagsHelper.retrieveTags(noteRetrieved);
            for (String s : tagsRetrieved.keySet()) {
                int count = tagsMap.get(s) == null ? 0 : tagsMap.get(s);
                tagsMap.put(s, ++count);
            }
        }

        for (String s : tagsMap.keySet()) {
            Tag tag = new Tag(s, tagsMap.get(s));
            tags.add(tag);
        }

        Collections.sort(tags, (tag1, tag2) -> tag1.getText().compareToIgnoreCase(tag2.getText()));
        return tags;
    }


    /**
     * Retrieves categories list from database
     *
     * @return List of categories
     */
    public ArrayList<Category> getCategories() {
        ArrayList<Category> categoriesList = new ArrayList<>();
        String sql = "SELECT "
                + KEY_CATEGORY_ID + ","
                + KEY_CATEGORY_NAME + ","
                + KEY_CATEGORY_DESCRIPTION + ","
                + KEY_CATEGORY_COLOR + ","
                + " COUNT(" + KEY_NOTEBOOK_ID + ") count"
                + " FROM " + TABLE_CATEGORY
                + " LEFT JOIN ("
                + " SELECT " + KEY_NOTEBOOK_ID + ", " + KEY_NOTEBOOK_CATEGORY_ID
                + " FROM " + TABLE_NOTEBOOK
                + ") USING( " + KEY_NOTEBOOK_CATEGORY_ID + ") "
                + " GROUP BY "
                + KEY_CATEGORY_ID + ","
                + KEY_CATEGORY_NAME + ","
                + KEY_CATEGORY_DESCRIPTION + ","
                + KEY_CATEGORY_COLOR
                + " ORDER BY IFNULL(NULLIF(" + KEY_CATEGORY_NAME + ", ''),'zzzzzzzz') ";

        Cursor cursor = null;
        try {
            cursor = getDatabase().rawQuery(sql, null);
            // Looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    categoriesList.add(new Category(cursor.getLong(0),
                            cursor.getString(1), cursor.getString(2), cursor
                            .getString(3), cursor.getInt(4)));
                } while (cursor.moveToNext());
            }

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return categoriesList;
    }


    /**
     * Updates or insert a new a category
     *
     * @param category Category to be updated or inserted
     * @return Rows affected or new inserted category id
     */
    public Category updateCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_ID, category.getId() != null ? category.getId() : Calendar.getInstance()
                .getTimeInMillis());
        values.put(KEY_CATEGORY_NAME, category.getName());
        values.put(KEY_CATEGORY_DESCRIPTION, category.getDescription());
        values.put(KEY_CATEGORY_COLOR, category.getColor());
        getDatabase(true).insertWithOnConflict(TABLE_CATEGORY, KEY_CATEGORY_ID, values, SQLiteDatabase
                .CONFLICT_REPLACE);
        return category;
    }


    /**
     * Deletion of  a category
     *
     * @param category Category to be deleted
     * @return Number 1 if category's record has been deleted, 0 otherwise
     */
    public long deleteCategory(Category category) {
        long deleted;

        SQLiteDatabase db = getDatabase(true);
        // Un-categorize notes associated with this category
        ContentValues values = new ContentValues();
        values.put(KEY_NOTEBOOK_CATEGORY_ID, "");

        // Updating row
        db.update(TABLE_NOTEBOOK, values, KEY_NOTEBOOK_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(category.getId())});

        // Delete category
        deleted = db.delete(TABLE_CATEGORY, KEY_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(category.getId())});
        return deleted;
    }


    /**
     * Get note Category
     */
    public Category getCategory(Long id) {
        Category category = null;
        String sql = "SELECT "
                + KEY_CATEGORY_ID + ","
                + KEY_CATEGORY_NAME + ","
                + KEY_CATEGORY_DESCRIPTION + ","
                + KEY_CATEGORY_COLOR
                + " FROM " + TABLE_CATEGORY
                + " WHERE " + KEY_CATEGORY_ID + " = " + id;

        Cursor cursor = null;
        try {
            cursor = getDatabase().rawQuery(sql, null);

            // Looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                category = new Category(cursor.getLong(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));
            }

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return category;
    }


    public int getCategorizedCount(Category category) {
        int count = 0;
        String sql = "SELECT COUNT(*)"
                + " FROM " + TABLE_NOTEBOOK
                + " WHERE " + KEY_NOTEBOOK_CATEGORY_ID + " = " + category.getId();

        Cursor cursor = null;
        try {
            cursor = getDatabase().rawQuery(sql, null);

            // Looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return count;
    }


    /**
     * Get note Category
     */
    public Notebook getNotebook(Long id) {
        Notebook notebook = null;
        String sql = "SELECT "
                + KEY_NOTEBOOK_ID + ","
                + KEY_NOTEBOOK_NAME + ","
                + KEY_NOTEBOOK_DESCRIPTION + ","
                + KEY_NOTEBOOK_COLOR
                + " FROM " + TABLE_NOTEBOOK
                + " WHERE " + KEY_NOTEBOOK_ID + " = " + id;

        Cursor cursor = null;
        try {
            cursor = getDatabase().rawQuery(sql, null);

            // Looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                notebook = new Notebook(cursor.getLong(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));
            }

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return notebook;
    }


    /**
     * Deletion of  a notebook
     *
     * @param notebook Notebook to be deleted
     * @return Number 1 if notebook's record has been deleted, 0 otherwise
     */
    public long deleteNotebook(Notebook notebook) {
        long deleted;

        SQLiteDatabase db = getDatabase(true);

        // Delete category
        deleted = db.delete(TABLE_NOTEBOOK, KEY_NOTEBOOK_ID + " = ?",
                new String[]{String.valueOf(notebook.getId())});
        return deleted;
    }

    public void getDefaultNotebook() {
        String name = "Personal";
        String description = "Add some personal notes";
        String color = "#FFFFFF";
        SQLiteDatabase db = getDatabase(true);
        ContentValues row = new ContentValues();
        row.put(KEY_NOTEBOOK_NAME, name);
        row.put(KEY_NOTEBOOK_DESCRIPTION, description);
        row.put(KEY_NOTEBOOK_COLOR, color);
        db.insert(TABLE_NOTEBOOK, null, row);
    }

    public void getDefaultNote() {
        Notebook notebook = new Notebook();
        SQLiteDatabase db = getDatabase();
        String selectQuery = "SELECT * FROM" + TABLE_NOTEBOOK;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            notebook.setId((long) cursor.getInt(0));
            notebook.setName(cursor.getString(1));
            notebook.setDescription(cursor.getString(2));
            notebook.setColor(cursor.getString(3));
        }
        cursor.close();
        String title = "To Do";
        String content = "What needs done?";
        int archived = 0;
        int trashed = 0;
        long notebookId = notebook.getId();
        int checklist = 0;

    }
}