package com.example.mycomics.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mycomics.beans.AuthorBean;
import com.example.mycomics.beans.BookBean;
import com.example.mycomics.beans.BookSerieBean;
import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.beans.ProfileActiveBean;
import com.example.mycomics.beans.ProfileBean;
import com.example.mycomics.beans.SerieBean;

import java.util.ArrayList;
import java.util.List;

/**************************************************************************************************/
/**  /!\ SQlite NAMING CONVENTION : UPPERCASE AND UNDERSCORES /!\
/**************************************************************************************************/

public class DataBaseHelper extends SQLiteOpenHelper {

/**************************************************************************************************/
/** CONSTANTS FOR SQL QUERIES
/**************************************************************************************************/
    // Table PROFILS
    public static final String PROFILES = "PROFILES";
    public static final String COLUMN_PROFILE_ID = "PROFILE_ID";
    public static final String COLUMN_PROFILE_NAME = "PROFILE_NAME";
    // Table PROFIL_ACTIF
    public static final String PROFILE_ACTIVE = "PROFILE_ACTIVE";
    public static final String COLUMN_PROFILE_ACTIVE_ID = "PROFILE_ACTIVE_ID";
    // Table EDITEURS
    public static final String EDITORS = "EDITORS";
    public static final String COLUMN_EDITOR_ID = "EDITOR_ID";
    public static final String COLUMN_EDITOR_NAME = "EDITOR_NAME";
    // Table SERIES
    public static final String SERIES = "SERIES";
    public static final String COLUMN_SERIE_ID = "SERIE_ID";
    public static final String COLUMN_SERIE_NAME = "SERIE_NAME";
    // Table AUTEURS
    public static final String AUTHORS = "AUTHORS";
    public static final String COLUMN_AUTHOR_ID = "AUTHOR_ID";
    public static final String COLUMN_AUTHOR_LAST_NAME = "AUTHOR_LAST_NAME";
    public static final String COLUMN_AUTHOR_FIRST_NAME = "AUTHOR_FIRST_NAME";
    public static final String COLUMN_AUTHOR_PSEUDONYM = "AUTHOR_PSEUDONYM";
    // Table TOMES
    public static final String BOOKS = "BOOKS";
    public static final String COLUMN_BOOK_ID = "BOOK_ID";
    public static final String COLUMN_BOOK_NUMBER = "BOOK_NUMBER";
    public static final String COLUMN_BOOK_TITLE = "BOOK_TITLE";
    public static final String COLUMN_BOOK_EDITOR_PRICE = "BOOK_EDITOR_PRICE";
    public static final String COLUMN_BOOK_VALUE = "BOOK_VALUE";
    public static final String COLUMN_BOOK_EDITION_DATE = "BOOK_EDITION_DATE";
    public static final String COLUMN_BOOK_ISBN = "BOOK_ISBN";
    public static final String COLUMN_BOOK_PICTURE = "BOOK_PICTURE";
    public static final String COLUMN_BOOK_AUTOGRAPH = "BOOK_AUTOGRAPH";
    public static final String COLUMN_BOOK_SPECIAL_EDITION = "BOOK_SPECIAL_EDITION";
    public static final String COLUMN_BOOK_SPECIAL_EDITION_LABEL = "BOOK_SPECIAL_EDITION_LABEL";
    public static final String DETAINING = "DETAINING";
    public static final String WRITING = "WRITING";

/**************************************************************************************************/
/** Constructor - If database doesn't exist
/** = CREATE DATABASE IF NOT EXISTS mycomics
/**************************************************************************************************/
    public DataBaseHelper(@Nullable Context context) {
        super(context, "mycomics.db", null, 1);
    }

    /* ------------------------------------------------------------------------------------------ */
    // Method Oncreate : called if the database doasn't exist
    /* ------------------------------------------------------------------------------------------ */
        @Override
    public void onCreate(SQLiteDatabase db) {
        // Table PROFILES (profile_id, profile_name) initialized on first profile
        db.execSQL("CREATE TABLE " + PROFILES + " (" + COLUMN_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PROFILE_NAME + " TEXT NOT NULL)");
        db.execSQL("INSERT INTO " + PROFILES + " ("+ COLUMN_PROFILE_NAME + ") " + "VALUES (\"Profil 1\")");


        // Table PROFILE_ACTIVE (profile_active_id, profile_id) only 1 row initialized on first profile
        db.execSQL("CREATE TABLE  " + PROFILE_ACTIVE + "  (" + COLUMN_PROFILE_ACTIVE_ID + " INTEGER PRIMARY KEY, " + COLUMN_PROFILE_ID + " INTEGER)");
        db.execSQL("INSERT INTO " + PROFILE_ACTIVE + " ("+ COLUMN_PROFILE_ACTIVE_ID + ", " + COLUMN_PROFILE_ID + ") " + "VALUES (1,1)");

        // Table EDITORS (editor_id, editor_name)
        db.execSQL("CREATE TABLE " + EDITORS + " (" + COLUMN_EDITOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EDITOR_NAME + " TEXT NOT NULL)");

        // Table SERIES (serie_id, serie_name)
        db.execSQL("CREATE TABLE " + SERIES + " (" + COLUMN_SERIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_SERIE_NAME + " TEXT NOT NULL)");

        // Table AUTHORS (author_id, author_pseudonym, author_first_name, author_last_name)
        db.execSQL("CREATE TABLE " + AUTHORS + " (" + COLUMN_AUTHOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_AUTHOR_PSEUDONYM + " TEXT NOT NULL, " +
                COLUMN_AUTHOR_FIRST_NAME + " TEXT, " + COLUMN_AUTHOR_LAST_NAME + " TEXT)");

        // Table BOOKS (book_id, book_title, book_isbn, book_number, book_picture, book_editor_price, book_value,
        //              book_autograph, book_special_edition, book_special_edition_label, serie_id, editor_id)
        db.execSQL("CREATE TABLE " + BOOKS + " (" +
                COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BOOK_TITLE + " TEXT NOT NULL, " +
                COLUMN_BOOK_NUMBER + " INTEGER, " +
                COLUMN_BOOK_ISBN + " TEXT, " +
                COLUMN_BOOK_PICTURE + " TEXT, " +
                COLUMN_BOOK_EDITOR_PRICE + " DECIMAL(5,2), " +
                COLUMN_BOOK_VALUE + " DECIMAL(5,2), " +
                COLUMN_BOOK_EDITION_DATE + " TEXT, " +
                COLUMN_BOOK_AUTOGRAPH + " BOOLEAN, " +
                COLUMN_BOOK_SPECIAL_EDITION + " BOOLEAN, " +
                COLUMN_BOOK_SPECIAL_EDITION_LABEL + " TEXT, " +
                COLUMN_SERIE_ID + " INTEGER," +
                COLUMN_EDITOR_ID + " INTEGER," +
                " FOREIGN KEY(" + COLUMN_SERIE_ID + ") REFERENCES " + SERIES + "(" + COLUMN_SERIE_ID + ")," +
                " FOREIGN KEY(" + COLUMN_EDITOR_ID + ") REFERENCES " + EDITORS + "(" + COLUMN_EDITOR_ID + "))");

        // Association table DETAINING (book_id, profile_id)
        db.execSQL("CREATE TABLE " + DETAINING + " (" +
                COLUMN_BOOK_ID + " INTEGER, " +
                COLUMN_PROFILE_ID + " INTEGER, " +
                "PRIMARY KEY (" + COLUMN_BOOK_ID + ", " + COLUMN_PROFILE_ID + "), " +
                "FOREIGN KEY(" + COLUMN_BOOK_ID + ") REFERENCES " + BOOKS + "(" + COLUMN_BOOK_ID + ")," +
                "FOREIGN KEY(" + COLUMN_PROFILE_ID + ") REFERENCES " + PROFILES + "(" + COLUMN_PROFILE_ID + "))");


        // Association table WRITING (book_id, author_id)
        db.execSQL("CREATE TABLE " + WRITING + " (" +
                COLUMN_BOOK_ID + " INTEGER, " +
                COLUMN_AUTHOR_ID + " INTEGER, " +
                "PRIMARY KEY(" + COLUMN_BOOK_ID + ", " + COLUMN_AUTHOR_ID + "), " +
                "FOREIGN KEY(" + COLUMN_BOOK_ID + ") REFERENCES " + BOOKS + "(" + COLUMN_BOOK_ID + ")," +
                "FOREIGN KEY(" + COLUMN_AUTHOR_ID + ") REFERENCES " + AUTHORS + "(" + COLUMN_AUTHOR_ID + "))");


        /*TODO ---------------------------------------------------------------------------------- */
        // Method to feed database with test data (at the end of the code page)
        /* -------------------------------------------------------------------------------------- */
        feedDatabase(db);

        }

    /* ------------------------------------------------------------------------------------------ */
    // Method onUpgrade called on database version change
    /* ------------------------------------------------------------------------------------------ */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + PROFILES);
        db.execSQL("DROP TABLE " + PROFILE_ACTIVE);
        db.execSQL("DROP TABLE " + EDITORS);
        db.execSQL("DROP TABLE " + SERIES);
        db.execSQL("DROP TABLE " + AUTHORS);
        db.execSQL("DROP TABLE " + BOOKS);
        db.execSQL("DROP TABLE " + DETAINING);
        db.execSQL("DROP TABLE " + WRITING);
        onCreate(db);
    }


/**************************************************************************************************/
/** UPDATE QUERIES
/**************************************************************************************************/

    /* ------------------------------------------------------------------------------------------ */
    // UPDATE PROFILE_ACTIVE SET PROFILE_ID = new_active_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean updateActiveProfile(DataBaseHelper dataBaseHelper, long new_active_id){
        SQLiteDatabase db = this.getWritableDatabase(); // Database write access
        ContentValues cv = new ContentValues(); // Data bundle to be stored in the database
        cv.put(COLUMN_PROFILE_ID, new_active_id); // Key-value pair put into bundle
        long update =  db.update(PROFILE_ACTIVE,cv,null,null); // query execution
        // log update success
        if (update == -1){ // Test update : if not ok. -1 is a default from SQlite
            System.out.println("update request : non");
            db.close(); // close database
            return false;
        } else { // Test update : if ok
            System.out.println("update request : OK");
            db.close(); // close database
            return true;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // UPDATE PROFILES SET PROFILE_NAME = profile_name WHERE PROFILE_ID = profile_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean updateProfile(DataBaseHelper dataBaseHelper, ProfileBean profileBean){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PROFILE_NAME, profileBean.getProfile_name());
        // query execution with WHERE clause and arguments
        long update =  db.update(PROFILES,cv,"PROFILE_ID = ?",new String[]{String.valueOf(profileBean.getProfile_id())});
        if (update == -1){
            System.out.println("update request : error");
            db.close();
            return false;
        } else {
            System.out.println("update request : OK");
            db.close();
            return true;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // UPDATE SERIES WHERE SERIE_ID = serie_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean updateSerie(DataBaseHelper dataBaseHelper, SerieBean serieBean){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SERIE_NAME, serieBean.getSerie_name());
        long update =  db.update(SERIES,cv,"SERIE_ID = ?",new String[]{String.valueOf(serieBean.getSerie_id())});
        if (update == -1){
            System.out.println("update request : error");
            db.close();
            return false;
        } else {
            System.out.println("update request : OK");
            db.close();
            return true;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // UPDATE BOOKS WHERE BOOK_ID = book_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean updateBook(DataBaseHelper dataBaseHelper, BookBean bookBean){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BOOK_TITLE, bookBean.getBook_title());
        cv.put(COLUMN_BOOK_NUMBER, bookBean.getBook_number());
        cv.put(COLUMN_BOOK_ISBN, bookBean.getBook_isbn());
        cv.put(COLUMN_BOOK_EDITOR_PRICE, bookBean.getBook_editor_price());
        cv.put(COLUMN_BOOK_VALUE, bookBean.getBook_value());
        cv.put(COLUMN_BOOK_EDITION_DATE, bookBean.getBook_edition_date());
        cv.put(COLUMN_BOOK_PICTURE, bookBean.getBook_picture());
        cv.put(COLUMN_BOOK_AUTOGRAPH, bookBean.isBook_autograph());
        cv.put(COLUMN_BOOK_SPECIAL_EDITION, bookBean.isBook_special_edition());
        cv.put(COLUMN_BOOK_SPECIAL_EDITION_LABEL, bookBean.getBook_special_edition_label());
        cv.put(COLUMN_SERIE_ID, bookBean.getSerie_id());
        long update =  db.update(BOOKS,cv,"BOOK_ID = ?",new String[]{String.valueOf(bookBean.getBook_id())});
        if (update == -1){
            System.out.println("update request : error");
            db.close();
            return false;
        } else {
            System.out.println("update request : OK");
            db.close();
            return true;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // UPDATE AUTHORS WHERE AUTHOR_ID = author_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean updateAuthor(DataBaseHelper dataBaseHelper, AuthorBean authorBean){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_AUTHOR_PSEUDONYM, authorBean.getAuthor_pseudonym());
        cv.put(COLUMN_AUTHOR_LAST_NAME, authorBean.getAuthor_last_name());
        cv.put(COLUMN_AUTHOR_FIRST_NAME, authorBean.getAuthor_first_name());
        long update =  db.update(AUTHORS,cv,"AUTHOR_ID = ?",new String[]{String.valueOf(authorBean.getAuthor_id())});
        if (update == -1){
            System.out.println("update request : error");
            db.close();
            return false;
        } else {
            System.out.println("update request : OK");
            db.close();
            return true;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // UPDATE EDITORS WHERE EDITOR_ID = editor_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean updateEditor(DataBaseHelper dataBaseHelper, EditorBean editorBean){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EDITOR_NAME, editorBean.getEditor_name());
        long update =  db.update(EDITORS,cv,"EDITOR_ID = ?",new String[]{String.valueOf(editorBean.getEditor_id())});
        if (update == -1){
            System.out.println("update request : error");
            db.close();
            return false;
        } else {
            System.out.println("update request : OK");
            db.close();
            return true;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // UPDATE TOMES SET SERIE_ID = serie_id WHERE TOME_ID = tome_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean updateBookSerie(DataBaseHelper dataBaseHelper, SerieBean serieBean, Integer book_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SERIE_ID, serieBean.getSerie_id());
        long update = db.update(BOOKS,cv,"BOOK_ID = ?",new String[]{String.valueOf(book_id)});
        if (update == -1){
            System.out.println("update request : error");
            db.close();
            return false;
        } else {
            System.out.println("update request : OK");
            db.close();
            return true;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // UPDATE TOMES SET EDITEUR_ID = editeur_id WHERE TOME_ID = tome_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean updateBookEditor(DataBaseHelper dataBaseHelper, EditorBean editorBean, Integer book_id){
        SQLiteDatabase db = this.getWritableDatabase(); // accès lecture BDD
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EDITOR_ID, editorBean.getEditor_id());
        long update = db.update(BOOKS,cv,"BOOK_ID = ?",new String[]{String.valueOf(book_id)});
        if (update == -1){ // Test si insertion ok
            System.out.println("update request : error");
            db.close();
            return false;
        } else {
            System.out.println("update request : OK");
            db.close();
            return true;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // UPDATE BOOKS SET BOOK_PICTURE = book_picture WHERE BOOK_ID = book_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean updateBookPicture(DataBaseHelper dataBaseHelper, Integer book_id, String book_picture){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BOOK_PICTURE, book_picture);
        long update = db.update(BOOKS,cv,"BOOK_ID = ?",new String[]{String.valueOf(book_id)});
        if (update == -1){
            System.out.println("update request : error");
            db.close();
            return false;
        } else {
            System.out.println("update request : OK");
            db.close();
            return true;
        }
    }

/**************************************************************************************************/
    /** INSERT QUERIES, only the NAMEs/PSEUDONYM, the IDs are auto-incremented.
    /** for association Tables, the IDs are stored
    /** The other data will be stored with update queries
/**************************************************************************************************/

    /* ------------------------------------------------------------------------------------------ */
    // INSERT INTO PROFILES (profile_name)
    /* ------------------------------------------------------------------------------------------ */
    public boolean insertIntoProfiles(ProfileBean profileBean){
        SQLiteDatabase db = this.getWritableDatabase(); // Database write access
        ContentValues cv = new ContentValues(); // Data bundle to be stored in the database
        cv.put(COLUMN_PROFILE_NAME, profileBean.getProfile_name()); // Key-value pair put into bundle
        long insert = db.insert(PROFILES, null, cv); // query execution
        // log update success
        if (insert == -1){ // Test insert : if not ok
            db.close(); // close database
            return false;
        } else { // Test update : if ok
            db.close(); // close database
            return true;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // INSERT INTO EDITORS (editor_name)
    /* ------------------------------------------------------------------------------------------ */
    public boolean insertIntoEditors(EditorBean editorBean){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EDITOR_NAME, editorBean.getEditor_name());
        long insert = db.insert(EDITORS, null, cv);
        if (insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // INSERT INTO AUTHORS (author_pseudonym)
    /* ------------------------------------------------------------------------------------------ */
    public boolean insertIntoAuthors(AuthorBean authorBean){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_AUTHOR_PSEUDONYM, authorBean.getAuthor_pseudonym());
        long insert = db.insert(AUTHORS, null, cv);
        if (insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // INSERT INTO WRITING (book_id, author_id)
    /* ------------------------------------------------------------------------------------------ */
    public boolean insertIntoWriting(Integer book_id, Integer author_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BOOK_ID, book_id);
        cv.put(COLUMN_AUTHOR_ID, author_id);
        long insert = db.insert(WRITING, null, cv);
        if (insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // INSERT INTO SERIES (serie_name)
    /* ------------------------------------------------------------------------------------------ */
    public boolean insertIntoSeries(SerieBean serieBean){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SERIE_NAME, serieBean.getSerie_name());
        long insert = db.insert(SERIES, null, cv);
        if (insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // INSERT INTO BOOKS (book_title)
    /* ------------------------------------------------------------------------------------------ */
    public boolean insertIntoBooks(BookBean bookBean){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BOOK_TITLE, bookBean.getBook_title());
        long insert = db.insert(BOOKS, null, cv);
        if (insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // INSERT INTO DETAINING (book_id, profile_id)
    /* ------------------------------------------------------------------------------------------ */
    public boolean insertIntoDetaining(BookBean bookBean){
        int profile_id = this.getActiveProfile().getProfile_id(); // fetching active profile id
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BOOK_ID, bookBean.getBook_id());
        cv.put(COLUMN_PROFILE_ID, profile_id);
        long insert = db.insert(DETAINING, null, cv);
        if (insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

/**************************************************************************************************/
/** DELETE QUERIES (use of update queries for books serie_id & editor_id)
/**************************************************************************************************/

    /* ------------------------------------------------------------------------------------------ */
    // UPDATE BOOKS SET SERIE_ID = NULL WHERE BOOK_ID = book_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean deleteBookSerie(DataBaseHelper dataBaseHelper, Integer book_id){
    SQLiteDatabase db = this.getWritableDatabase(); // Database write access
    ContentValues cv = new ContentValues(); // Data bundle to be stored in the database
    cv.putNull(COLUMN_SERIE_ID); // Key-value pair put into bundle, value = null
        // query execution with WHERE clause and arguments
        long update = db.update(BOOKS,cv,"BOOK_ID = ?",new String[]{String.valueOf(book_id)});
    if (update == -1){ // Test update : if not ok. -1 is a default from SQlite
        System.out.println("update request : error");
        db.close(); // close database
        return false;
    } else { // Test update : if ok
        System.out.println("update request : OK");
        db.close(); // close database
        return true;
    }
}

    /* ------------------------------------------------------------------------------------------ */
    // UPDATE BOOKS SET EDITOR_ID = NULL WHERE BOOK_ID = book_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean deleteBookEditor(DataBaseHelper dataBaseHelper, Integer book_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.putNull(COLUMN_EDITOR_ID);
        long update = db.update(BOOKS,cv,"BOOK_ID = ?",new String[]{String.valueOf(book_id)});
        if (update == -1){
            System.out.println("update request : error");
            db.close();
            return false;
        } else {
            System.out.println("update request : OK");
            db.close();
            return true;
        }
    }


    /* ------------------------------------------------------------------------------------------ */
    // DELETE * FROM WRITING
    // WHERE BOOK_ID = book_id
    // AND AUTHOR_ID = author_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean deleteBookAuthor(DataBaseHelper dataBaseHelper, Integer author_id, Integer book_id){
        SQLiteDatabase db = this.getWritableDatabase(); // Database write access
        String whereClause = COLUMN_BOOK_ID + " = ? AND " + COLUMN_AUTHOR_ID + " = ?"; // Whrere clause creation
        String[] whereArgs = new String[]{String.valueOf(book_id), String.valueOf(author_id)}; // Where arguments (array)
        // query execution with WHERE clause and arguments
        int numRowsDeleted = db.delete(WRITING, whereClause, whereArgs);
        if (numRowsDeleted >= 1) { // Test if at least 1 row got deleted -> OK
            db.close(); // close database
            return true;
        } else { // nothing deleted
            db.close(); // close database
            return false;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // DELETE * FROM BOOKS
    // WHERE BOOK_ID = book_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean deleteBook(DataBaseHelper dataBaseHelper, Integer book_id){
        SQLiteDatabase db = this.getWritableDatabase();
        // Whrere clause creation, used three times
        String whereClause = COLUMN_BOOK_ID + " = ?";
        // Delete constraint in WRITING
        String[] whereArgs = new String[]{String.valueOf(book_id)};
        int numRowsDeletedWriting = db.delete(WRITING, whereClause, whereArgs);
        // Delete constraint in DETAINING
        whereArgs = new String[]{String.valueOf(book_id)};
        int numRowsDeletedDetaining = db.delete(DETAINING, whereClause, whereArgs);
        // At last Delete from BOOKS
        whereArgs = new String[]{String.valueOf(book_id)};
        int numRowsDeletedBooks = db.delete(BOOKS, whereClause, whereArgs);
        db.close();
        if (numRowsDeletedBooks >= 1) { // Test if at least 1 row from Books got deleted -> OK
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // DELETE * FROM SERIES
    // WHERE SERIE_ID = serie_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean deleteSerie(DataBaseHelper dataBaseHelper, Integer serie_id){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete CONSTRAINT in BOOKS
        // SET serie_id  to NULL in Table BOOKS where the serie_id matches
        String query = "UPDATE " + BOOKS +
                " SET " + COLUMN_SERIE_ID + " = NULL" +
                " WHERE " + COLUMN_SERIE_ID + " = \"" + serie_id + "\"";
        db.execSQL(query);

        // Whrere clause
        String whereClause = COLUMN_SERIE_ID + " = ?";
        // At last Delete from BOOKS
        String[] whereArgs = new String[]{String.valueOf(serie_id)};
        int numRowsDeletedSeries = db.delete(SERIES, whereClause, whereArgs);
        db.close();
        if (numRowsDeletedSeries >= 1) { // Test if at least 1 row from SERIES got deleted -> OK
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // DELETE * FROM AUTHORS
    // WHERE AUTHOR_ID = author_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean deleteAuthor(DataBaseHelper dataBaseHelper, Integer author_id){
        SQLiteDatabase db = this.getWritableDatabase();
        // Whrere clause creation, used twice
        String whereClause = COLUMN_AUTHOR_ID + " = ?";
        // Delete constraint in WRITING
        String[] whereArgs = new String[]{String.valueOf(author_id)};
        int numRowsDeletedWriting = db.delete(WRITING, whereClause, whereArgs);
        // At last Delete from BOOKS
        whereArgs = new String[]{String.valueOf(author_id)};
        int numRowsDeletedAuthors = db.delete(AUTHORS, whereClause, whereArgs);
        db.close();
        if (numRowsDeletedAuthors >= 1) { // Test if at least 1 row from AUTHORS got deleted -> OK
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // DELETE * FROM EDITORS
    // WHERE EDITOR_ID = editor_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean deleteEditor(DataBaseHelper dataBaseHelper, Integer editor_id){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete CONSTRAINT in BOOKS
        // SET serie_id  to NULL in Table BOOKS where the serie_id matches
        String query = "UPDATE " + BOOKS +
                " SET " + COLUMN_EDITOR_ID + " = NULL" +
                " WHERE " + COLUMN_EDITOR_ID + " = \"" + editor_id + "\"";
        db.execSQL(query);

        // Whrere clause
        String whereClause = COLUMN_EDITOR_ID + " = ?";
        // At last Delete from EDITORS
        String[] whereArgs = new String[]{String.valueOf(editor_id)};
        int numRowsDeletedEditors = db.delete(EDITORS, whereClause, whereArgs);
        db.close();
        if (numRowsDeletedEditors >= 1) { // Test if at least 1 row from SERIES got deleted -> OK
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }


/**************************************************************************************************/
/** get methods for SELECT queries with the query as argument
/** returns beans or list of beans
/** get count method used to check if there are duplicates
/**************************************************************************************************/


    /* ------------------------------------------------------------------------------------------ */
    // get : List<ProfileBean>
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<ProfileBean> getListProfileBean(String query){
        ArrayList<ProfileBean> returnList = new ArrayList<>(); // list to be returned
        SQLiteDatabase db = this.getReadableDatabase(); // Database read access
        Cursor cursor = db.rawQuery(query, null); // cursor = query result data
        if (cursor.moveToFirst()) { // true if there is at least 1 row
            do { // For each tuple in the cursor
                int profile_id = cursor.getInt(0); // 1 key-value pair at index 0
                String profile_name = cursor.getString(1); // 1 key-value pair at index 1
                ProfileBean profileBean = new ProfileBean(profile_id, profile_name); // bean created from this tuple
                returnList.add(profileBean); // to put the bean in the list to be returned
            } while (cursor.moveToNext()); // go to next tuple if there is more results and repeat
        } else {
            // false, no results -> do nothing
        }
        cursor.close(); // close cursor
        db.close(); // close database
        return returnList; // the list is returned
    }

    /* -------------------------------------- */
    // get : ProfiBean
    // Only 1 result thanks to LIMIT 1 in queries
    /* -------------------------------------- */
    public ProfileBean getProfileBean(String query){
        ProfileBean profileBean = null; // bean to be returned
        SQLiteDatabase db = this.getReadableDatabase(); // Database read access
        Cursor cursor = db.rawQuery(query, null); // cursor = query result data
        if (cursor.moveToFirst()) { // true if there is at least 1 row
            do { // For each tuple in the cursor (in this case 1 maxinum)
                int profil_id = cursor.getInt(0); // 1 key-value pair at index 0
                String profil_nom = cursor.getString(1); // 1 key-value pair at index 1
                profileBean = new ProfileBean(profil_id, profil_nom); // bean created from this tuple
            } while (cursor.moveToNext()); // go to next tuple if there is more results and repeat

        } else {
            // false, no results -> do nothing
        }
        cursor.close(); // close cursor
        db.close(); // close database
        return profileBean; // the bean is returned
    }

    /* ------------------------------------------------------------------------------------------ */
    // get : List<EditorBean>
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<EditorBean> getListEditorBean(String query){
        ArrayList<EditorBean> returnList = new ArrayList<>(); // list to be returned
        SQLiteDatabase db = this.getReadableDatabase(); // Database read access
        Cursor cursor = db.rawQuery(query, null); // cursor = query result data
        if (cursor.moveToFirst()) { // true if there is at least 1 row
            do { // For each tuple in the cursor
                Integer editor_id = cursor.getInt(0); // 1 key-value pair at index 0
                String editor_name = cursor.getString(1); // 1 key-value pair at index 1
                EditorBean editorBean = new EditorBean(editor_id, editor_name, getNbBooksByEditorId(editor_id)); // bean created from this tuple
                returnList.add(editorBean); // to put the bean in the list to be returned
            } while (cursor.moveToNext()); // go to next tuple if there is more results and repeat
        } else {
            // false, no results -> do nothing
        }
        cursor.close(); // close cursor
        db.close(); // close database
        return returnList; // the list is returned
    }

    /* ------------------------------------------------------------------------------------------ */
    // get : EditorBean
    // Only 1 result thanks to LIMIT 1 in queries
    /* ------------------------------------------------------------------------------------------ */
    public EditorBean getEditorBean(String query){
        EditorBean editorBean = new EditorBean();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Integer editor_id = cursor.getInt(0);
                String editor_name = cursor.getString(1);
                editorBean = new EditorBean(editor_id, editor_name);
            } while (cursor.moveToNext());
        } else {
            // do nothing
        }
        cursor.close();
        db.close();
        return editorBean;
    }

    /* ------------------------------------------------------------------------------------------ */
    // get : List<AuthorBean>
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<AuthorBean> getListAuthorBean(String query){
        ArrayList<AuthorBean> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Integer author_id = cursor.getInt(0);
                String author_pseudonym = cursor.getString(1);
                String author_first_name = cursor.getString(2);
                String author_last_name = cursor.getString(3);
                AuthorBean authorBean = new AuthorBean(author_id, author_pseudonym, author_last_name, author_first_name, getNbBooksByAuthorId(author_id));
                returnList.add(authorBean);
            } while (cursor.moveToNext());
        } else {
            // do nothing
        }
        cursor.close();
        db.close();
        return returnList;
    }

    /* ------------------------------------------------------------------------------------------ */
    // get : AuthorBean
    // Only 1 result thanks to LIMIT 1 in queries
    /* ------------------------------------------------------------------------------------------ */
    public AuthorBean getAuthorBean(String query){
        AuthorBean authorBean = new AuthorBean();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Integer author_id = cursor.getInt(0);
                String author_pseudonym = cursor.getString(1);
                String author_first_name = cursor.getString(2);
                String author_last_name = cursor.getString(3);
                authorBean = new AuthorBean(author_id, author_pseudonym, author_last_name, author_first_name, getNbBooksByAuthorId(author_id));
            } while (cursor.moveToNext());
        } else {
            // do nothing
        }
        cursor.close();
        db.close();
        return authorBean;
    }

    /* ------------------------------------------------------------------------------------------ */
    // get : List<SerieBean>
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<SerieBean> getListSerieBean(String query){
        ArrayList<SerieBean> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Integer serie_id = cursor.getInt(0);
                String serie_name = cursor.getString(1);
                SerieBean serieBean = new SerieBean(serie_id, serie_name, getNbBooksBySerieId(serie_id));
                returnList.add(serieBean);
            } while (cursor.moveToNext());
        } else {
            // do nothing
        }
        cursor.close();
        db.close();
        return returnList;
    }

    /* ------------------------------------------------------------------------------------------ */
    // get : SerieBean
    // Only 1 result thanks to LIMIT 1 in queries
    /* ------------------------------------------------------------------------------------------ */
    public SerieBean getSerieBean(String query){
        SerieBean serieBean = new SerieBean();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Integer serie_id = cursor.getInt(0);
                String serie_name = cursor.getString(1);
                serieBean = new SerieBean(serie_id, serie_name);
            } while (cursor.moveToNext());
        } else {
            // do nothing
        }
        cursor.close();
        db.close();
        return serieBean;
    }

    /* ------------------------------------------------------------------------------------------ */
    // get : List<BookBean>
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<BookBean> getListBookBean(String query){
        ArrayList<BookBean> returnList = new ArrayList<BookBean>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Integer book_id = cursor.getInt(0);
                String book_title = cursor.getString(1);
                Integer book_number = cursor.getInt(2);
                String book_isbn = cursor.getString(3);
                double book_editor_price = cursor.getDouble(4);
                double book_value = cursor.getDouble(5);
                String book_edition_date = cursor.getString(6);
                String book_picture = cursor.getString(7);
                boolean book_autograph = cursor.getInt(8) == 1;
                boolean book_special_edition = cursor.getInt(9) == 1;
                String book_special_edition_label = cursor.getString(10);
                Integer serie_id = cursor.getInt(11);
                Integer editor_id = cursor.getInt(12);
                BookBean bookBean = new BookBean(book_id, book_title, book_number, book_isbn,
                        book_picture, book_editor_price, book_value, book_edition_date, book_autograph,
                        book_special_edition, book_special_edition_label, serie_id, editor_id);
                returnList.add(bookBean);
            } while (cursor.moveToNext());
        }
        else {
            // do nothing
        }
        cursor.close();
        db.close();
        return returnList;
    }

    /* ------------------------------------------------------------------------------------------ */
    // get : BookBean
    // Only 1 result thanks to LIMIT 1 in queries
    /* ------------------------------------------------------------------------------------------ */
    public BookBean getTomeBean(String query){
        BookBean bookBean = new BookBean();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Integer book_id = cursor.getInt(0);
                String book_title = cursor.getString(1);
                Integer book_number = cursor.getInt(2);
                String book_isbn = cursor.getString(3);
                String book_picture = cursor.getString(4);
                double book_editor_price = cursor.getDouble(5);
                double book_value = cursor.getDouble(6);
                String book_edition_date = cursor.getString(6);
                boolean book_autograph = cursor.getInt(8) == 1;
                boolean book_special_edition = cursor.getInt(9) == 1;
                String book_special_edition_label = cursor.getString(10);
                Integer serie_id = cursor.getInt(11);
                Integer editor_id = cursor.getInt(12);
                bookBean = new BookBean(book_id, book_title, book_number, book_isbn, book_picture,
                        book_editor_price, book_value, book_edition_date, book_autograph,
                        book_special_edition, book_special_edition_label, serie_id, editor_id);
            } while (cursor.moveToNext());
        } else {
            // do nothing
        }
        cursor.close();
        db.close();
        return bookBean;
    }


    /* ------------------------------------------------------------------------------------------ */
    // get : List<BookSerieBean>
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<BookSerieBean> getListBookSerieBean(String query){
        ArrayList<BookSerieBean> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Integer book_id = cursor.getInt(0);
                String book_title = cursor.getString(1);
                Integer book_number = cursor.getInt(2);
                String book_isbn = cursor.getString(3);
                double book_editor_price = cursor.getDouble(4);
                double book_value = cursor.getDouble(5);
                String book_edition_date = cursor.getString(6);
                String book_picture = cursor.getString(7);
                boolean book_autograph = cursor.getInt(8) == 1;
                boolean book_special_edition = cursor.getInt(9) == 1;
                String book_special_edition_label = cursor.getString(10);
                Integer serie_id = cursor.getInt(11);
                Integer editor_id = cursor.getInt(12);
                String serie_name = cursor.getString(13);
                BookSerieBean bookSerieBean = new BookSerieBean(book_id, book_title, book_number,
                        book_isbn, book_picture, book_editor_price, book_value, book_edition_date,
                        book_autograph, book_special_edition, book_special_edition_label, serie_id,
                        editor_id, serie_name);
                returnList.add(bookSerieBean);
            } while (cursor.moveToNext());
        } else {
            // do nothing
        }
        cursor.close();
        db.close();
        return returnList;
    }

    /* ------------------------------------------------------------------------------------------ */
    // get count : boolean
    /* ------------------------------------------------------------------------------------------ */
    public boolean checkDuplicate(String query){
        int nbResult = 0; // integer to check number of duplicates
        SQLiteDatabase db = this.getReadableDatabase(); // Database read access
        Cursor cursor = db.rawQuery(query, null); // cursor = query result data
        if (cursor.moveToFirst()) { // true if there is at least 1 row (in this case 1 maxinum)
            nbResult = cursor.getInt(0); // 1 if there is 1 matching result
        } else {
            // false, no results -> do nothing, nbResult = 0 by default
        }
        cursor.close(); // close cursor
        db.close(); // close database
        if (nbResult == 1) { // if there is at least 1 duplicate, return true
            return true;
        } else { // if there is no duplicate, return false
            return false;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // get id : int
    /* ------------------------------------------------------------------------------------------ */
    public int getId(String query){
        int id = -1; // Result initialized
        SQLiteDatabase db = this.getReadableDatabase(); // Database read access
        Cursor cursor = db.rawQuery(query, null); // cursor = query result data
        if (cursor.moveToFirst()) { // true if there is at least 1 row
            id = cursor.getInt(0);
        } else {
            // false, no results -> do nothing, id = -1 by default
        }
        cursor.close(); // close cursor
        db.close(); // close database
        return id;
    }

    /* ------------------------------------------------------------------------------------------ */
    // get count : int
    /* ------------------------------------------------------------------------------------------ */
    public int getNbBooks(String query){
        List<BookBean> returnList = new ArrayList<>(); // list to be returned
        int nbBooks = 0; // Result initialized
        SQLiteDatabase db = this.getReadableDatabase(); // Database read access
        Cursor cursor = db.rawQuery(query, null); // cursor = query result data
        if (cursor.moveToFirst()) { // true if there is at least 1 row
            do { // For each tuple in the cursor
                Integer tome_id = cursor.getInt(0); // 1 key-value pair at index 0
                String tome_titre = cursor.getString(1); // 1 key-value pair at index 1
                int tome_numero = cursor.getInt(2); // ...
                String tome_isbn = cursor.getString(3);
                String tome_image = cursor.getString(4);
                double tome_prix_achat = cursor.getDouble(5);
                double tome_valeur_connue = cursor.getDouble(6);
                String tome_date_edition = cursor.getString(7);
                // ternary operator to convert Integer from database to boolean
                boolean tome_dedicace = cursor.getInt(8) == 1;
                boolean tome_edition_speciale = cursor.getInt(9) == 1;
                String tome_edition_speciale_libelle = cursor.getString(10);
                Integer serie_id = cursor.getInt(11);
                Integer editeur_id = cursor.getInt(12);
                // bean created from this tuple
                BookBean bookBean = new BookBean(tome_id, tome_titre, tome_numero, tome_isbn,
                        tome_image, tome_prix_achat, tome_valeur_connue, tome_date_edition,
                        tome_dedicace, tome_edition_speciale, tome_edition_speciale_libelle,
                        serie_id, editeur_id);
                returnList.add(bookBean); // to put the bean in the list to be returned
            } while (cursor.moveToNext()); // go to next tuple if there is more results and repeat
        } else {
            // false, no results -> do nothing
        }
        cursor.close(); // close cursor
        db.close(); // close database
        return returnList.size(); // the list length is returned
    }

/**************************************************************************************************/
/** SELECT QUERIES
/**************************************************************************************************/

    /** ACTIVE PROFILE **/
    /* -------------------------------------- */
    // SELECT * FROM PROFILE_ACTIVE, only 1 row
    /* This method is unique and doesn't fit in the get methods
    /* -------------------------------------- */
    public ProfileActiveBean getActiveProfile(){
        ProfileActiveBean profileActiveBean = new ProfileActiveBean(); // bean to be returned
        String query = "SELECT * FROM " + PROFILE_ACTIVE; // query
        SQLiteDatabase db = this.getReadableDatabase(); // Database read access
        Cursor cursor = db.rawQuery(query, null); // cursor = query result data
        if (cursor.moveToFirst()) { // true if there is at least 1 row
            int profil_id = cursor.getInt(0); // 1 key-value pair at index 0
            profileActiveBean = new ProfileActiveBean(profil_id); // bean created from this tuple
        } else {
            // false, no results -> do nothing
        }
        // fermeture db et cursor
        cursor.close(); // close cursor
        db.close(); // close database
        return profileActiveBean; // the bean is returned
    }

    /** PROFILES **/
    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM PROFILES
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<ProfileBean> getProfilesList(){
        String query = "SELECT * FROM " + PROFILES +
                " ORDER BY " + PROFILES + "." + COLUMN_PROFILE_NAME; // query
        return getListProfileBean(query); // the list is returned
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT COUNT (*) FROM PROFILES
    // WHERE PROFILE_NAME = profile_name
    /* ------------------------------------------------------------------------------------------ */
    public boolean checkProfileDuplicate(String profile_name){
        String query = "SELECT COUNT(*) FROM " + PROFILES +
                " WHERE " + COLUMN_PROFILE_NAME + " = \"" + profile_name + "\"";
        return checkDuplicate(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM PROFILES
    // INNER JOIN PROFILE_ACTIVE ON PROFILES.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE PROFILES.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    /* ------------------------------------------------------------------------------------------ */
    public ProfileBean getProfileByActiveProfile(){
        String query = "SELECT * FROM " + PROFILES +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + PROFILES + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID + " " +
                "WHERE " + PROFILES + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID;
        return getProfileBean(query);
    }

    /** EDITORS **/
    /* ------------------------------------------------------------------------------------------ */
    // SELECT DISTINCT * FROM EDITORS
    // GROUP BY EDITORS.EDITOR_ID
    // ORDER BY EDITORS.EDITOR_NAME
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<EditorBean> getEditorsList(){
        String query = "SELECT DISTINCT * FROM " + EDITORS +
                " GROUP BY " + EDITORS + "." + COLUMN_EDITOR_ID +
                " ORDER BY " + EDITORS + "." + COLUMN_EDITOR_NAME;
        return getListEditorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT DISTINCT * FROM EDITORS
    // INNER JOIN BOOKS ON BOOKS.EDITOR_ID = EDITORS.EDITOR_ID
    // WHERE BOOKS.SERIE_ID = serie_id
    // GROUP BY EDITORS.EDITOR_ID
    // ORDER BY EDITORS.EDITOR_NAME
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<EditorBean> getEditorsListBySerieId(int serie_id){
        String query = "SELECT DISTINCT * FROM " + EDITORS +
                " INNER JOIN " + BOOKS + " ON " + BOOKS + "." + COLUMN_EDITOR_ID + " = " + EDITORS + "." + COLUMN_EDITOR_ID +
                " WHERE " + BOOKS + "." + COLUMN_SERIE_ID + " = \"" + serie_id +
                "\" GROUP BY " + EDITORS + "." + COLUMN_EDITOR_ID +
                " ORDER BY " + EDITORS + "." + COLUMN_EDITOR_NAME;
        return getListEditorBean(query);
    }
    /* ------------------------------------------------------------------------------------------ */
    // SELECT DISTINCT * FROM EDITORS
    // WHERE EDITORS.EDITOR_NAME LIKE '%filter%'
    // GROUP BY EDITORS.EDITOR_ID
    // ORDER BY EDITORS.EDITOR_NAME
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<EditorBean> getEditorsListByFilter(String filter){
        String query = "SELECT DISTINCT * FROM " + EDITORS +
                " WHERE " + EDITORS + "." + COLUMN_EDITOR_NAME + " LIKE \'%" + filter +
                "%\' GROUP BY " + EDITORS + "." + COLUMN_EDITOR_ID +
                " ORDER BY " + EDITORS + "." + COLUMN_EDITOR_NAME;
        return getListEditorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM EDITORS
    // WHERE EDITORS.EDITOR_ID = editor_id LIMIT 1
    /* ------------------------------------------------------------------------------------------ */
    public EditorBean getEditorById(int editor_id){
        String query = "SELECT * FROM " + EDITORS +
                " WHERE " + EDITORS + "." + COLUMN_EDITOR_ID + " = \"" + editor_id + "\" LIMIT 1";
        return getEditorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT COUNT (*) FROM EDITORS
    // WHERE EDITOR_NAME = editor_name
    /* ------------------------------------------------------------------------------------------ */
    public boolean checkEditorDuplicate(String editor_name){
        String query = "SELECT COUNT(*) FROM " + EDITORS +
                " WHERE " + COLUMN_EDITOR_NAME + " = \"" + editor_name + "\"";
        return checkDuplicate(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM EDITORS
    // INNER JOIN BOOKS ON BOOKS.EDITOR_ID = EDITORS.EDITOR_ID
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE EDITORS.EDITOR_NAME = editor_name LIMIT 1
    /* ------------------------------------------------------------------------------------------ */
    public EditorBean getEditorByName(String editor_name){
        String query = "SELECT * FROM " + EDITORS +
        " INNER JOIN " + BOOKS + " ON " + BOOKS + "." + COLUMN_EDITOR_ID + " = " + EDITORS + "." + COLUMN_EDITOR_ID +
        " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
        " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
        " WHERE " + EDITORS + "." + COLUMN_EDITOR_NAME + " = \"" + editor_name + "\" LIMIT 1";
        return getEditorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM EDITORS
    // INNER JOIN BOOKS ON EDITORS.EDITOR_ID = BOOKS.EDITOR_ID
    // WHERE BOOKS.BOOK_ID = book_id LIMIT 1
    /* ------------------------------------------------------------------------------------------ */
    public EditorBean getEditorByBookId(int book_id){
        String query = "SELECT * FROM " + EDITORS +
                " INNER JOIN " + BOOKS + " ON " + EDITORS + "." + COLUMN_EDITOR_ID + " = " + BOOKS + "." + COLUMN_EDITOR_ID +
                " WHERE " + BOOKS + "." + COLUMN_BOOK_ID + " = \"" + book_id + "\" LIMIT 1";
        return getEditorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT EDITOR_ID FROM EDITORS
    // WHERE EDITOR_NAME = editorBean.getEditor_name()
    // ORDER BY EDITOR_ID DESC LIMIT 1
    /* ------------------------------------------------------------------------------------------ */
    public EditorBean getEditorLatest(EditorBean editorBean){
        String query = "SELECT " + COLUMN_EDITOR_ID + " FROM " + EDITORS +
                " WHERE " + COLUMN_EDITOR_NAME + " = \"" + editorBean.getEditor_name() +
                "\" ORDER BY " + COLUMN_EDITOR_ID +" DESC LIMIT 1";
        return getEditorById(getId(query));
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM EDITORS
    // INNER JOIN BOOKS ON BOOKS.EDITOR_ID = EDITORS.EDITOR_ID
    // INNER JOIN WRITING ON WRITING.BOOK_ID = BOOKS.BOOK_ID
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE ECRIRE.AUTHOR_ID = author_id
    // GROUP BY EDITORS.EDITOR_ID
    // ORDER BY EDITORS.EDITOR_NAME;
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<EditorBean> getEditorsByAuthorId(int author_id){
        String query = "SELECT * FROM " + EDITORS +
                " INNER JOIN " + BOOKS + " ON " + BOOKS + "." + COLUMN_EDITOR_ID + " = " + EDITORS + "." + COLUMN_EDITOR_ID +
                " INNER JOIN " + WRITING + " ON " + WRITING + "." + COLUMN_BOOK_ID + " = " + BOOKS + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + WRITING + "." + COLUMN_AUTHOR_ID + " = \"" + author_id +
                "\" GROUP BY " + EDITORS + "." + COLUMN_EDITOR_ID +
                " ORDER BY " + EDITORS + "." + COLUMN_EDITOR_NAME;
        return getListEditorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM EDITORS
    // JOIN BOOKS ON BOOKS.EDITOR_ID = EDITORS.EDITOR_ID
    // JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE BOOKS.SERIE_ID = serie_id
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<EditorBean> getEditorsBySerieId(int serie_id){
        String query = "SELECT * FROM " + EDITORS +
                " INNER JOIN " + BOOKS + " ON " + BOOKS + "." + COLUMN_EDITOR_ID + " = " + EDITORS + "." + COLUMN_EDITOR_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + BOOKS + "." + COLUMN_SERIE_ID + " = \"" + serie_id + "\"";
        return getListEditorBean(query);
    }

    /** AUTHORS **/
    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM AUTHORS
    // GROUP BY AUTHORS.AUTHOR_ID
    // ORDER BY AUTHORS.AUTHOR_PSEUDONYM
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<AuthorBean> getAuthorsList(){
        String query = "SELECT * FROM " + AUTHORS +
                " GROUP BY " + AUTHORS + "." + COLUMN_AUTHOR_ID +
                " ORDER BY " + AUTHORS + "." + COLUMN_AUTHOR_PSEUDONYM;
        return getListAuthorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM AUTHORS
    // WHERE AUTHORS.AUTHOR_PSEUDONYM LIKE '%filter%'
    // GROUP BY AUTHORS.AUTHOR_ID
    // ORDER BY AUTHORS.AUTHOR_PSEUDONYM
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<AuthorBean> getAuthorsListByFilter(String filter){
        String query = "SELECT * FROM " + AUTHORS +
                " WHERE " + AUTHORS + "." + COLUMN_AUTHOR_PSEUDONYM + " LIKE \'%" + filter +
                "%\' GROUP BY " + AUTHORS + "." + COLUMN_AUTHOR_ID +
                " ORDER BY " + AUTHORS + "." + COLUMN_AUTHOR_PSEUDONYM;
        return getListAuthorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM AUTHORS
    // WHERE AUTHORS.AUTHOR_ID = author_id  LIMIT 1
    /* ------------------------------------------------------------------------------------------ */
    public AuthorBean getAuthorById(int author_id){
        String query = "SELECT * FROM " + AUTHORS +
                " WHERE " + AUTHORS + "." + COLUMN_AUTHOR_ID + " = \"" + author_id + "\" LIMIT 1";
        return getAuthorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM AUTHORS
    // WHERE AUTHOR_PSEUDONYM = author_pseudonym LIMIT 1
    /* ------------------------------------------------------------------------------------------ */
    public AuthorBean getAuthorByPseudonym(String author_pseudonym){
        String query = "SELECT * FROM " + AUTHORS +
                " WHERE " + AUTHORS + "." + COLUMN_AUTHOR_PSEUDONYM + " = \"" + author_pseudonym + "\" LIMIT 1";
        return getAuthorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT COUNT (*) FROM AUTHORS
    // WHERE AUTHOR_PSEUDONYM = author_pseudonym
    /* ------------------------------------------------------------------------------------------ */
    public boolean checkAuthorDuplicate(String author_pseudonym){
        String query = "SELECT COUNT(*) FROM " + AUTHORS +
                " WHERE " + COLUMN_AUTHOR_PSEUDONYM + " = \"" + author_pseudonym + "\"";
        return checkDuplicate(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM AUTHORS
    // INNER JOIN WRITING ON AUTHORS.AUTHOR_ID = WRITING.AUTHOR_ID
    // INNER JOIN BOOKS ON BOOKS.BOOK_ID = WRITING.BOOK_ID
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID +
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE WRITING.BOOK_ID = book_id
    // GROUP BY AUTHORS.AUTHOR_ID
    // ORDER BY AUTHORS.AUTHOR_PSEUDONYM
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<AuthorBean> getAuthorsListByBookId(int book_id){
        String query = "SELECT * FROM " + AUTHORS +
                " INNER JOIN " + WRITING + " ON " + AUTHORS + "." + COLUMN_AUTHOR_ID + " = " + WRITING + "." + COLUMN_AUTHOR_ID +
                " INNER JOIN " + BOOKS + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + WRITING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + WRITING + "." + COLUMN_BOOK_ID + " = \"" + book_id +
                "\" GROUP BY " + AUTHORS + "." + COLUMN_AUTHOR_ID +
                " ORDER BY " + AUTHORS + "." + COLUMN_AUTHOR_PSEUDONYM;
        return getListAuthorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM AUTHORS
    // INNER JOIN WRITING ON AUTHORS.AUTHOR_ID = WRITING.AUTHOR_ID
    // INNER JOIN BOOKS ON WRITING.BOOK_ID = BOOKS.BOOK_ID
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE BOOKS.EDITOR_ID = editor_id
    // GROUP BY AUTHORS.AUTHOR_ID
    // ORDER BY AUTHORS.AUTHOR_PSEUDONYM
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<AuthorBean> getAuthorsListByEditorId(int editor_id){
        String query = "SELECT * FROM " + AUTHORS +
                " INNER JOIN " + WRITING + " ON " + AUTHORS + "." + COLUMN_AUTHOR_ID + " = " + WRITING + "." + COLUMN_AUTHOR_ID +
                " INNER JOIN " + BOOKS + " ON " + WRITING + "." + COLUMN_BOOK_ID + " = " + BOOKS + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + BOOKS + "." + COLUMN_EDITOR_ID + " = \"" + editor_id +
                "\" GROUP BY " + AUTHORS + "." + COLUMN_AUTHOR_ID +
                " ORDER BY " + AUTHORS + "." + COLUMN_AUTHOR_PSEUDONYM;
        return getListAuthorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM AUTHORS
    // INNER JOIN WRITING ON AUTHORS.AUTHOR_ID = WRITING.AUTHOR_ID
    // INNER JOIN BOOKS ON BOOKS.BOOK_ID = WRITING.BOOK_ID
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE BOOKS.SERIE_ID = serie_id
    // GROUP BY AUTHORS.AUTHOR_ID
    // ORDER BY AUTHORS.AUTHOR_PSEUDONYM
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<AuthorBean> getAuthorsListBySerieId(int serie_id){
        String query = "SELECT * FROM " + AUTHORS +
                " INNER JOIN " + WRITING + " ON " + WRITING + "." + COLUMN_AUTHOR_ID + " = " + AUTHORS + "." + COLUMN_AUTHOR_ID +
                " INNER JOIN " + BOOKS + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + WRITING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + BOOKS + "." + COLUMN_SERIE_ID + " = \"" + serie_id +
                "\" GROUP BY " + AUTHORS + "." + COLUMN_AUTHOR_ID +
                " ORDER BY " + AUTHORS + "." + COLUMN_AUTHOR_PSEUDONYM;
        return getListAuthorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT DISTINCT A1.* FROM AUTHORS A1
    // INNER JOIN WRITING W1 ON A1.AUTHOR_ID = W1.AUTHOR_ID
    // INNER JOIN WRITING W2 ON W1.BOOK_ID = W2.BOOK_ID
    // INNER JOIN AUTHORS A2 ON W2.AUTHOR_ID = A2.AUTHOR_ID
    // INNER JOIN BOOKS ON W1.BOOK_ID = BOOKS.BOOK_ID
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE A1.AUTHOR_ID <> A2.AUTHOR_ID
    // AND A2.AUTHOR_ID = author_id
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<AuthorBean> getAuthorsTeam(int author_id){
        String query = "SELECT DISTINCT A1.* FROM " + AUTHORS + " A1" +
                " INNER JOIN " + WRITING + " W1 ON A1." + COLUMN_AUTHOR_ID + " = W1." + COLUMN_AUTHOR_ID +
                " INNER JOIN " + WRITING + " W2 ON W1." + COLUMN_BOOK_ID + " = W2." + COLUMN_BOOK_ID +
                " INNER JOIN " + AUTHORS + " A2 ON W2." + COLUMN_AUTHOR_ID + "= A2." + COLUMN_AUTHOR_ID +
                " INNER JOIN " + BOOKS + " ON W1." + COLUMN_BOOK_ID + " = " + BOOKS + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE A1." + COLUMN_AUTHOR_ID + " <> A2." + COLUMN_AUTHOR_ID +
                " AND A2." + COLUMN_AUTHOR_ID + " =\"" + author_id + "\"";
        return getListAuthorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT AUTHOR_ID FROM AUTHORS
    // WHERE AUTHOR_PSEUDONYM = authorBean.getAuthor_pseudonym()
    // ORDER BY AUTHOR_ID DESC LIMIT 1
    /* ------------------------------------------------------------------------------------------ */
    public AuthorBean getAuthorLatest(AuthorBean authorBean){
        String query = "SELECT " + COLUMN_AUTHOR_ID + " FROM " + AUTHORS +
                " WHERE " + COLUMN_AUTHOR_PSEUDONYM + " = \"" + authorBean.getAuthor_pseudonym() +
                "\" ORDER BY " + COLUMN_AUTHOR_ID +" DESC LIMIT 1";
        return getAuthorById(getId(query));
    }

    /** SERIES **/
    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM SERIES
    // GROUP BY SERIES.SERIE_ID
    // ORDER BY SERIES.SERIE_NAME
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<SerieBean> getSeriesList(){
        String query = "SELECT * FROM " + SERIES +
                " GROUP BY " + SERIES + "." + COLUMN_SERIE_ID +
                " ORDER BY " + SERIES + "." + COLUMN_SERIE_NAME;
        return getListSerieBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM SERIES
    // WHERE SERIES.SERIE_NAME LIKE '%filter%'
    // GROUP BY SERIES.SERIE_ID
    // ORDER BY SERIES.SERIE_NAME
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<SerieBean> getSeriesListByFilter(String filter){
        String query = "SELECT * FROM " + SERIES +
                " WHERE " + SERIES + "." + COLUMN_SERIE_NAME + " LIKE \'%" + filter +
                "%\' GROUP BY " + SERIES + "." + COLUMN_SERIE_ID +
                " ORDER BY " + SERIES + "." + COLUMN_SERIE_NAME;
        return getListSerieBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM SERIES
    // WHERE SERIES.SERIE_ID = serie_id LIMIT 1
    /* ------------------------------------------------------------------------------------------ */
    public SerieBean getSerieById(int serie_id){
        String query = "SELECT * FROM " + SERIES +
                " WHERE " + SERIES + "." + COLUMN_SERIE_ID + " = \"" + serie_id + "\" LIMIT 1";
        return getSerieBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM SERIES
    // INNER JOIN BOOKS ON BOOKS.SERIE_ID = SERIES.SERIE_ID
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE BOOKS.EDITOR_ID = editor_id
    // GROUP BY SERIES.SERIE_ID
    // ORDER BY SERIES.SERIE_NAME
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<SerieBean> getSeriesListByEditorId(int editor_id){
        String query = "SELECT DISTINCT * FROM " + SERIES +
                " INNER JOIN " + BOOKS + " ON " + SERIES + "." + COLUMN_SERIE_ID + " = " + BOOKS + "." + COLUMN_SERIE_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + BOOKS + "." + COLUMN_EDITOR_ID + " = \"" + editor_id +
                "\" GROUP BY " + SERIES + "." + COLUMN_SERIE_ID +
                " ORDER BY " + SERIES + "." + COLUMN_SERIE_NAME;
        return getListSerieBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM SERIES
    // INNER JOIN BOOKS ON BOOKS.SERIE_ID = SERIES.SERIE_ID
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE BOOKS.BOOK_ID = book_id LIMIT 1
    /* ------------------------------------------------------------------------------------------ */
    public SerieBean getSerieByBookId(int book_id){
        String query = "SELECT * FROM " + SERIES +
                " INNER JOIN " + BOOKS + " ON " + SERIES + "." + COLUMN_SERIE_ID + " = " + BOOKS + "." + COLUMN_SERIE_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + BOOKS + "." + COLUMN_BOOK_ID + " = \"" + book_id + "\" LIMIT 1";
        return getSerieBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT COUNT (*) FROM SERIES
    // WHERE COLUMN_SERIE_NAME = serie_name
    /* ------------------------------------------------------------------------------------------ */
    public boolean checkSerieDuplicate(String serie_name){
        String query = "SELECT COUNT(*) FROM " + SERIES +
                " WHERE " + COLUMN_SERIE_NAME + " = \"" + serie_name + "\"";
        return checkDuplicate(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT SERIE_ID FROM SERIES
    // WHERE SERIE_NAME = serieBean.getSerie_name()
    // ORDER BY SERIE_ID DESC LIMIT 1
    /* ------------------------------------------------------------------------------------------ */
    public SerieBean getSerieLatest(SerieBean serieBean){
        String query = "SELECT " + COLUMN_SERIE_ID + " FROM " + SERIES +
                " WHERE " + COLUMN_SERIE_NAME + " = \"" + serieBean.getSerie_name() +
                "\" ORDER BY " + COLUMN_SERIE_ID +" DESC LIMIT 1";
        return getSerieById(getId(query));
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM SERIES
    // INNER JOIN BOOKS ON BOOKS.SERIE_ID = SERIES.SERIE_ID
    // INNER JOIN WRITING ON WRITING.BOOK_ID = BOOKS.BOOK_ID
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE WRITING.AUTHOR_ID = author_id
    // GROUP BY SERIES.SERIE_ID
    // ORDER BY SERIES.SERIE_NAME
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<SerieBean> getSeriesListByAuthorId(int author_id){
        String query = "SELECT DISTINCT * FROM " + SERIES +
                " INNER JOIN " + BOOKS + " ON " + BOOKS + "." + COLUMN_SERIE_ID + " = " + SERIES + "." + COLUMN_SERIE_ID +
                " INNER JOIN " + WRITING + " ON " + WRITING + "." + COLUMN_BOOK_ID + " = " + BOOKS + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + WRITING + "." + COLUMN_AUTHOR_ID + " = \"" + author_id +
                "\" GROUP BY " + SERIES + "." + COLUMN_SERIE_ID +
                " ORDER BY " + SERIES + "." + COLUMN_SERIE_NAME;
        return getListSerieBean(query);
    }

    /** BOOKS **/
    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM BOOKS
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // GROUP BY BOOKS.SERIE_ID
    // ORDER BY BOOKS.BOOK_NUMBER, BOOKS.BOOK_TITLE
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<BookBean> getBooksList(){
        String query = "SELECT * FROM " + BOOKS +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " GROUP BY " + BOOKS + "." + COLUMN_SERIE_ID +
                " ORDER BY " + BOOKS + "." + COLUMN_BOOK_NUMBER + ", " + BOOKS + "." + COLUMN_BOOK_TITLE;
        return getListBookBean(query); // résultat de la requête
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM BOOKS
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE (BOOKS.BOOK_TITLE LIKE '%filter%'
    // OR BOOKS.BOOK_NUMBER LIKE '%filter%')
    // ORDER BY BOOKS.BOOK_NUMBER, BOOKS.BOOK_TITLE
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<BookBean> getBooksListByFilter(String filter){
        String query = "SELECT * FROM " + BOOKS +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE (" + BOOKS + "." + COLUMN_BOOK_TITLE + " LIKE \'%" + filter +
                "%\' OR " + BOOKS + "." + COLUMN_BOOK_NUMBER + " LIKE \'%" + filter +
                "%\') ORDER BY " + BOOKS + "." + COLUMN_BOOK_NUMBER + ", " + BOOKS + "." + COLUMN_BOOK_TITLE;
        return getListBookBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM BOOKS
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE BOOKS.BOOK_TITLE = book_title
    /* ------------------------------------------------------------------------------------------ */
    public boolean checkBookDuplicate(String book_title){
        String query = "SELECT COUNT(*) FROM " + BOOKS +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + BOOKS + "." + COLUMN_BOOK_TITLE + " = \"" + book_title + "\"";
        return checkDuplicate(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT DISTINCT BOOKS.*, SERIE_NAME FROM BOOKS, SERIES
    // LEFT JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE BOOKS.SERIE_ID = SERIES.SERIE_ID
    // UNION
    // SELECT DISTINCT BOOKS.*, NULL AS SERIE_NAME FROM BOOKS
    // LEFT JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE (BOOKS.SERIE_ID IS NULL
    // OR BOOKS.SERIE_ID = -1)
    // ORDER BY SERIES.SERIE_NAME, BOOKS.BOOK_NUMBER, BOOKS.BOOK_TITLE
    /* ------------------------------------------------------------------------------------------ */

    public ArrayList<BookSerieBean> getBooksAndBooksSeriesList(){
        String query = "SELECT DISTINCT " + BOOKS + ".*, " + COLUMN_SERIE_NAME + " FROM " + BOOKS + ", " + SERIES +
                " LEFT JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + BOOKS + "." + COLUMN_SERIE_ID + " = " + SERIES + "." + COLUMN_SERIE_ID +
                 " UNION " +
                "SELECT DISTINCT " + BOOKS + ".*, NULL AS " + COLUMN_SERIE_NAME + " FROM " + BOOKS +
                " LEFT JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE (" + BOOKS + "." + COLUMN_SERIE_ID + " IS NULL" +
                " OR " + BOOKS + "." + COLUMN_SERIE_ID + " = -1)" +
                " ORDER BY " + SERIES + "." + COLUMN_SERIE_NAME + ", " + BOOKS + "." + COLUMN_BOOK_NUMBER + ", " + BOOKS + "." + COLUMN_BOOK_TITLE;
        return getListBookSerieBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT BOOKS.*, SERIES.SERIE_NAME FROM BOOKS , SERIES
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE BOOKS.SERIE_ID = SERIES.SERIE_ID
    // AND (BOOKS.BOOK_TITLE LIKE '%filter%'
    // OR BOOKS.BOOK_NUMBER LIKE '%filter%'
    // OR SERIES.SERIE_NAME LIKE '%filter%')
    // ORDER BY SERIES.SERIE_NAME, BOOKS.BOOK_NUMBER, BOOKS.BOOK_TITLE
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<BookSerieBean> getBooksAndSeriesListByFilter(String filtre){
        String query = "SELECT " + BOOKS + ".*, " + SERIES + "." + COLUMN_SERIE_NAME + " FROM " + BOOKS + ", " + SERIES +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + BOOKS + "." + COLUMN_SERIE_ID + " = " + SERIES + "." + COLUMN_SERIE_ID +
                " AND (" + BOOKS + "." + COLUMN_BOOK_TITLE + " LIKE \'%" + filtre +
                "%\' OR " + BOOKS + "." + COLUMN_BOOK_NUMBER + " LIKE \'%" + filtre +
                "%\' OR " + SERIES + "." + COLUMN_SERIE_NAME + " LIKE \'%" + filtre +
                "%\') ORDER BY " + SERIES + "." + COLUMN_SERIE_NAME + ", " + BOOKS + "." + COLUMN_BOOK_NUMBER + ", " + BOOKS + "." + COLUMN_BOOK_TITLE;
        return getListBookSerieBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM BOOKS
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE BOOKS.EDITOR_ID = editor_id
    // AND BOOKS.SERIE_ID IS NULL
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<BookBean> getBooksListByEditorIdNoSerie(int editor_id){
        String query = "SELECT * FROM " + BOOKS +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + BOOKS + "." + COLUMN_EDITOR_ID + " = \"" + editor_id +
                "\" AND " + BOOKS + "." + COLUMN_SERIE_ID + " IS NULL";
        return getListBookBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM BOOKS
    // INNER JOIN WRITING ON BOOKS.BOOK_ID = WRITING.BOOK_ID
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE WRITING.AUTEUR_ID = author_id
    // AND BOOKS.SERIE_ID IS NULL
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<BookBean> getBooksListByAuthorIdNoSerie(int author_id){
        String query = "SELECT * FROM " + BOOKS +
                " INNER JOIN " + WRITING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + WRITING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + WRITING + "." + COLUMN_AUTHOR_ID + " = \"" + author_id +
                "\" AND " + BOOKS + "." + COLUMN_SERIE_ID + " IS NULL";
        return getListBookBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT BOOK_ID FROM BOOKS
    // WHERE BOOK_TITLE = bookBean.getBook_title()
    // ORDER BY BOOK_ID DESC LIMIT 1
    /* ------------------------------------------------------------------------------------------ */
    public BookBean getBookLatest(BookBean bookBean){
        String query = "SELECT " + COLUMN_BOOK_ID + " FROM " + BOOKS +
                " WHERE " + COLUMN_BOOK_TITLE + " = \"" + bookBean.getBook_title() +
                "\" ORDER BY " + COLUMN_BOOK_ID + " DESC LIMIT 1";
        return getBookById(getId(query));
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT COUNT(*) FROM DETAINING
    // WHERE BOOK_ID = getBookLatest(bookBean)
    /* ------------------------------------------------------------------------------------------ */
    public boolean checkDetainingLatestBookOk(BookBean bookBean){
        String query = "SELECT COUNT(*) FROM " + DETAINING + " " +
                "WHERE " + COLUMN_BOOK_ID + " = \"" + getBookLatest(bookBean) + "\"";
        return checkDuplicate(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT COUNT (*) FROM WRITING
    // WHERE BOOK_ID = book_id
    // AND AUTHOR_ID = author_id
    /* ------------------------------------------------------------------------------------------ */
    public boolean checkDetainingBookAuthorPairDuplicate(int book_id, int author_id){
        String query = "SELECT COUNT(*) FROM " + WRITING +
                " WHERE " + COLUMN_BOOK_ID + " = \"" + book_id +
                "\" AND " + COLUMN_AUTHOR_ID + " = \"" + author_id + "\"";
        return checkDuplicate(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM BOOKS
    // WHERE BOOK_ID = book_id LIMIT 1
    /* ------------------------------------------------------------------------------------------ */
    public BookBean getBookById(Integer book_id){
        String query = "SELECT * FROM " + BOOKS +
                " WHERE " + BOOKS + "." + COLUMN_BOOK_ID + " = \"" + book_id + "\" LIMIT 1";
        return getTomeBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM BOOKS
    // JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE BOOKS.SERIE_ID = serie_id
    /* ------------------------------------------------------------------------------------------ */
    public ArrayList<BookBean> getBooksListBySerieId(int serie_id){
        String query = "SELECT * FROM " + BOOKS +
                " JOIN " + DETAINING +
                " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + BOOKS + "." + COLUMN_SERIE_ID + " = \"" + serie_id + "\"";
        return getListBookBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT COUNT (*) FROM BOOKS
    // JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE BOOKS.SERIE_ID = serie_id
    /* ------------------------------------------------------------------------------------------ */
    public int getNbBooksBySerieId(int serie_id){
        String query = "SELECT * FROM " + BOOKS +
                " JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + BOOKS + "." + COLUMN_SERIE_ID + " = \"" + serie_id + "\"";
        return getNbBooks(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT COUNT (*) FROM BOOKS
    // JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // JOIN WRITING ON BOOKS.BOOK_ID = WRITING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE WRITING.AUTHOR_ID = author_id
    /* ------------------------------------------------------------------------------------------ */
    public int getNbBooksByAuthorId(int author_id){
        String query = "SELECT * FROM " + BOOKS +
                " JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " JOIN " + WRITING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + WRITING + "." + COLUMN_AUTHOR_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + WRITING + "." + COLUMN_AUTHOR_ID + " = \"" + author_id + "\"";
        return getNbBooks(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT COUNT (*) FROM BOOKS
    // JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON DETAINING.PROFILE_ID = PROFILE_ACTIVE.PROFILE_ID
    // WHERE BOOKS.EDITOR_ID = editor_id
    /* ------------------------------------------------------------------------------------------ */
    public int getNbBooksByEditorId(int editor_id) {
        String query = "SELECT * FROM " + BOOKS +
                " JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + DETAINING + "." + COLUMN_PROFILE_ID + " = " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID +
                " WHERE " + BOOKS + "." + COLUMN_EDITOR_ID + " = \"" + editor_id + "\"";
        return getNbBooks(query);
    }

    public void feedDatabase(SQLiteDatabase db){
        /** AUTHORS */
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Buchet\");\n"); //author_id 1
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Crisse\");\n"); //author_id 2
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Duval\");\n"); //author_id 3
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Eiichiro Oda\");\n"); //author_id 4
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Goscinny\");\n"); //author_id 5
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Gotlib\");\n"); //author_id 6
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Hugo Pratt\");\n"); //author_id 7
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Uderzo\");\n"); //author_id 8
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Arleston\");\n"); //author_id 9
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Mourier\");\n"); //author_id 10
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Tarquin\");\n"); //author_id 11
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Vatine\");\n"); //author_id 12
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Color Twins\");\n"); //author_id 13
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Morvan\");\n"); //author_id 14
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Keramidas\");\n"); //author_id 15
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Carrere\");\n"); //author_id 16
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Nicoloff\");\n"); //author_id 17
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Bar2\");\n"); //author_id 18
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"'Fane\");\n"); //author_id 19
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Jenfevre\");\n"); //author_id 20
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Perna\");\n"); //author_id 21
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Turk\");\n"); //author_id 22
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"De Groot\");\n"); //author_id 23
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Bedu\");\n"); //author_id 24
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Tome\");\n"); //author_id 25
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Janry\");\n"); //author_id 26
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Franquin\");\n"); //author_id 27
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Peyo\");\n"); //author_id 28
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Tota\");\n"); //author_id 29
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Pecqueur\");\n"); //author_id 30
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Henriet\");\n"); //author_id 31
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Schelle\");\n"); //author_id 32
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Rosa\");\n"); //author_id 33
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Usagi\");\n"); //author_id 34
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Cassegrain\");\n"); //author_id 35
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Gess\");\n"); //author_id 36
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Rabarot\");\n"); //author_id 37
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Breton\");\n"); //author_id 38
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Vatine\");\n"); //author_id 39
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Blanchard\");\n"); //author_id 40
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Emem\");\n"); //author_id 41
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Alex Alice\");\n"); //author_id 42
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Mitric\");\n"); //author_id 43
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Lamirand\");\n"); //author_id 44
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Ange\");\n"); //author_id 45
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Paty\");\n"); //author_id 46
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Gaudin\");\n"); //author_id 47
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Briones\");\n"); //author_id 48
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Tackian\");\n"); //author_id 49
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Miquel\");\n"); //author_id 50
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Ludolullabi\");\n"); //author_id 51
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Péru\");\n"); //author_id 52
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Runberg\");\n"); //author_id 53
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Ocaña\");\n"); //author_id 54
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Louis\");\n"); //author_id 55
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Mariolle\");\n"); //author_id 56
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"aurore\");\n"); //author_id 57
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Paille\");\n"); //author_id 58
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Campoy\");\n"); //author_id 59
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Alliel\");\n"); //author_id 60
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Morris\");\n"); //author_id 61
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Melanÿa\");\n"); //author_id 62
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Floch\");\n"); //author_id 63
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Pellet\");\n"); //author_id 64
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Dorison\");\n"); //author_id 65
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Diaz Canales\");\n"); //author_id 66
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Pellejero\");\n"); //author_id 67
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Olivier Delcroix\");\n"); //author_id 68
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Bajram\");\n"); //author_id 69
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Sokal\");\n"); //author_id 70
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Tardi\");\n"); //author_id 71
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Lob\");\n"); //author_id 72
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Rochette\");\n"); //author_id 73
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Legrand\");\n"); //author_id 74
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Koren Shadmi\");\n"); //author_id 75
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"W. Vance\");\n"); //author_id 76
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"J. Van Hamme\");\n"); //author_id 77
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"J. Giraud\");\n"); //author_id 78
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Francq\");\n"); //author_id 79
        db.execSQL("INSERT INTO AUTHORS(AUTHOR_PSEUDONYM) VALUES(\"Bourgeon\");\n"); //author_id 80

        /** EDITORS */
        db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Casterman\");\n"); //editor_id 1
        db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Dargaud\");\n"); //editor_id 2
        db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Delcourt\");\n"); //editor_id 3
        db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Glenat\");\n"); //editor_id 4
        db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Pilote\");\n"); //editor_id 5
        db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Soleil Editions\");\n"); //editor_id 6
        db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Vent d'Ouest\");\n"); //editor_id 7
        db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Le Lombard\");\n"); //editor_id 8
        db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Dupuis\");\n"); //editor_id 9
        db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Albert René\");\n"); //editor_id 10
        db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Rue de sèvres\");\n"); //editor_id 11
        db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Pepperland\");\n"); //editor_id 12
        db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Ici même\");\n"); //editor_id 13
        db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"12Bis\");\n"); //editor_id 14

        /** SERIES */
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Astérix\");\n"); //serie_id 1
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Carmen Mc Callum\");\n"); //serie_id 2
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Clifton\");\n"); //serie_id 3
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Kookaburra\");\n"); //serie_id 4
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Lanfeust de Troy\");\n"); //serie_id 5
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Lanfeust des étoiles\");\n"); //serie_id 6
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Lucky Luke\");\n"); //serie_id 7
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"One Piece\");\n"); //serie_id 8
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Sillage\");\n"); //serie_id 9
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Trolls de Troy\");\n"); //serie_id 10
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Les feux d'Askell\");\n"); //serie_id 11
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Luuna\");\n"); //serie_id 12
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Leo Loden\");\n"); //serie_id 13
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Joe Bar Team\");\n"); //serie_id 14
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Spirou et Fantasio\");\n"); //serie_id 15
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Le petit Spirou\");\n"); //serie_id 16
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Les Schtroumpfs\");\n"); //serie_id 17
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Les Conquérants de Troy\");\n"); //serie_id 18
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Golden cup\");\n"); //serie_id 19
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Code Mc Callum\");\n"); //serie_id 20
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Siegfried\");\n"); //serie_id 21
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Kookaburra Universe\");\n"); //serie_id 22
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Lanfeust Odyssey\");\n"); //serie_id 23
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"cixi de Troy\");\n"); //serie_id 24
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Tykko des sables\");\n"); //serie_id 25
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Les naufragés d'Ythaq\");\n"); //serie_id 26
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Les forêts d'Opale\");\n"); //serie_id 27
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Streamliner\");\n"); //serie_id 28
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Le troisième testament\");\n"); //serie_id 29
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Corto Maltese\");\n"); //serie_id 30
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Universal War One\");\n"); //serie_id 31
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Canardo\");\n"); //serie_id 32
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Les aventures extraordinaires d'Adèle Blanc-Sec\");\n"); //serie_id 33
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"XIII\");\n"); //serie_id 34
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Largo Winch\");\n"); //serie_id 35
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Private Ghost\");\n"); //serie_id 36
        db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Les passagers du vent\");\n"); //serie_id 37

        /** BOOKS & DETAINING */
        // Lanfeust de Troy serie 5
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'ivoire du Magohamoth\", 1, 5, 6);\n"); //book_id 1
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Thanos l'incongru\", 2, 5, 6);\n"); //book_id 2
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Castel Or-Azur\", 3, 5, 6);\n"); //book_id 3
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le paladin d'Eckmül\", 4, 5, 6);\n"); //book_id 4
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le frisson de l'haruspice\", 5, 5, 6);\n"); //book_id 5
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Cixi impératrice\", 6, 5, 6);\n"); //book_id 6
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les pétaures se cachent pour mourir\", 7, 5, 6);\n"); //book_id 7
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La bête fabuleuse\", 8, 5, 6);\n"); //book_id 8
        // Trolls de Troy serie 10
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Histoires Trolles\", 1, 10, 6);\n"); //book_id 9
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le scalp du vénérable\", 2, 10, 6);\n"); //book_id 10
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Comme un vol de pétaures\", 3, 10, 6);\n"); //book_id 11
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le feu occulte\", 4, 10, 6);\n"); //book_id 12
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les maléfices de la thaumaturge\", 5, 10, 6);\n"); //book_id 13
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Trolls dans la brume\", 6, 10, 6);\n"); //book_id 14
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Plume de sage\", 7, 10, 6);\n"); //book_id 15
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Rock'n Troll attitude\", 8, 10, 6);\n"); //book_id 16
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les prisonniers du Darshan I\", 9, 10, 6);\n"); //book_id 17
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les prisonniers du Darshan II\", 10, 10, 6);\n"); //book_id 18
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Trollympiades\", 11, 10, 6);\n"); //book_id 19
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Sang famille I\", 12, 10, 6);\n"); //book_id 20
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Boules de poils I\", 15, 10, 6);\n"); //book_id 21
        // Les feux d'askell série 11
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'onguent admirable\", 1, 11, 6);\n"); //book_id 22
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Retour à Vocable\", 2, 11, 6);\n"); //book_id 23
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Corail sanglant\", 3, 11, 6);\n"); //book_id 24
        // Sillage série 9
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"A feu et à cendres\", 1, 9, 3);\n"); //book_id 25
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Collection privée\", 2, 9, 3);\n"); //book_id 26
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Engrenages\", 3, 9, 3);\n"); //book_id 27
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le signe des démons\", 4, 9, 3);\n"); //book_id 28
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"FToRoSs\", 5, 9, 3);\n"); //book_id 29
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Artifices\", 6, 9, 3);\n"); //book_id 30
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Q.H.I.\", 7, 9, 3);\n"); //book_id 31
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Nature humaine\", 8, 9, 3);\n"); //book_id 32
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Infiltrations\", 9, 9, 3);\n"); //book_id 33
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_SPECIAL_EDITION, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Retour de flammes - Edition spéciale\", 10, true, 9, 3);\n"); //book_id 34
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Monde flottant\", 11, 9, 3);\n"); //book_id 35
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Zone franche\", 12, 9, 3);\n"); //book_id 36
        // Luuna série 12
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La nuit des totems\", 1, 12, 6);\n"); //book_id 37
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le crépuscule du Lynx\", 2, 12, 6);\n"); //book_id 38
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Dans les traces d'Oh-mah-ah\", 3, 12, 6);\n"); //book_id 39
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Pok-ta-pok\", 4, 12, 6);\n"); //book_id 40
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le cercle des miroirs\", 5, 12, 6);\n"); //book_id 41
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La reine des loups\", 6, 12, 6);\n"); //book_id 42
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La source du temps\", 7, 12, 6);\n"); //book_id 43
        // Leo Loden série 13
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Terminus Cannebière\", 1, 13, 6);\n"); //book_id 44
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les sirènes du vieux-port\", 2, 13, 6);\n"); //book_id 45
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Adieu ma Joliette\", 3, 13, 6);\n"); //book_id 46
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Grillade Provençale\", 4, 13, 6);\n"); //book_id 47
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Kabbale dans les Traboules\", 5, 13, 6);\n"); //book_id 48
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Langoustines Breizhées\", 6, 13, 6);\n"); //book_id 49
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Fugue en rave mineure\", 7, 13, 6);\n"); //book_id 50
        // Leo Loden série 14
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Joe Bar Team 1\", 1, 14, 7);\n"); //book_id 51
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Joe Bar Team 2\", 2, 14, 7);\n"); //book_id 52
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Joe Bar Team 3\", 3, 14, 7);\n"); //book_id 53
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Joe Bar Team 4\", 4, 14, 7);\n"); //book_id 54
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Joe Bar Team 5\", 5, 14, 7);\n"); //book_id 55
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Joe Bar Team 6\", 6, 14, 7);\n"); //book_id 56
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Joe Bar Team 7\", 7, 14, 7);\n"); //book_id 57
        // Clifton série 3
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le voleur qui ris\", 2, 3, 8);\n"); //book_id 58
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"7 jours pour mourir\", 3, 3, 8);\n"); //book_id 59
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Alias Lord X\", 4, 3, 8);\n"); //book_id 60
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Une panthère pour le Colonel\", 6, 3, 8);\n"); //book_id 61
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Sir Jason\", 7, 3, 8);\n"); //book_id 62
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Week-end à tuer\", 8, 3, 8);\n"); //book_id 63
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Kidnapping\", 9, 3, 8);\n"); //book_id 64
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La mémoire brisée\", 11, 3, 8);\n"); //book_id 65
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Dernière séance\", 12, 3, 8);\n"); //book_id 66
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Matoutou-Falaise\", 13, 3, 8);\n"); //book_id 67
        // Spirou et Fantasio série 15
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Spirou et les héritiers\", 4, 15, 9);\n"); //book_id 68
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Z comme Zorglub\", 15, 15, 9);\n"); //book_id 69
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'horloger de la comète\", 36, 15, 9);\n"); //book_id 70
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le réveil du Z\", 37, 15, 9);\n"); //book_id 71
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Spirou à New-York\", 39, 15, 9);\n"); //book_id 72
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le rayon noir\", 44, 15, 9);\n"); //book_id 73
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Luna fatale\", 45, 15, 9);\n"); //book_id 74
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Machine qui rêve\", 46, 15, 9);\n"); //book_id 75
        // Le petit Spirou série 16
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\'\"Merci\" qui?\', 5, 16, 9);\n"); //book_id 76
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"N'oublie pas ta capuche\", 6, 16, 9);\n"); //book_id 77
        // Les Schtroumpfs série 17
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les Schtroumpfs noirs\", 1, 17, 9);\n"); //book_id 78
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La Schtroumpfette\", 3, 17, 9);\n"); //book_id 79
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'oeuf et les Schtroumpfs\", 4, 17, 9);\n"); //book_id 80
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les Schtroumpfs et le cracoucass\", 5, 17, 9);\n"); //book_id 81
        // Les Conquérants de Troy série 18
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Exil à Port-Fleuri\", 1, 18, 6);\n"); //book_id 82
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Eckmûl le bûcheron\", 2, 18, 6);\n"); //book_id 83
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La bataille du Port-Fleuri\", 3, 18, 6);\n"); //book_id 84
        // Astérix série 1
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le combat des chefs\", 7, 1, 2);\n"); //book_id 85
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Astérix légionnaire\", 10, 1, 2);\n"); //book_id 86
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le bouclier Arverne\", 11, 1, 2);\n"); //book_id 87
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le devin\", 19, 1, 2);\n"); //book_id 88
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le cadeau de César\", 21, 1, 2);\n"); //book_id 89
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le grand fossé\", 25, 1, 10);\n"); //book_id 90
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La galère d'Obélix\", 30, 1, 10);\n"); //book_id 91
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Astérix et Latraviata\", 31, 1, 10);\n"); //book_id 92
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le papyrus de César\", 36, 1, 10);\n"); //book_id 93
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"L'eau du ciel\", 1, 10);\n"); //book_id 94
        // Golden cup série 19
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Daytona\", 1, 19, 3);\n"); //book_id 95
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"500 mille chevaux\", 2, 19, 3);\n"); //book_id 96
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Des loups dans la spéciale\", 3, 19, 3);\n"); //book_id 97
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La fille de la toundra\", 4, 19, 3);\n"); //book_id 98
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le baiser du dragon\", 5, 19, 3);\n"); //book_id 99
        // Code Mc Callum série 20
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Londres\", 1, 20, 3);\n"); //book_id 100
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Spectre\", 2, 20, 3);\n"); //book_id 101
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Exil\", 3, 20, 3);\n"); //book_id 102
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Jungles\", 4, 20, 3);\n"); //book_id 103
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Mercenaire\", 5, 20, 3);\n"); //book_id 104
        // Carmen Mc Callum série 2
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_SPECIAL_EDITION, BOOK_SPECIAL_EDITION_LABEL, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Jukurpa\", true, \"Coffret L'affaire Sonoda\", 1, 2, 3);\n"); //book_id 105
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_SPECIAL_EDITION, BOOK_SPECIAL_EDITION_LABEL, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Mare tranquillitatis\", true, \"Coffret L'affaire Sonoda\", 2, 2, 3);\n"); //book_id 106
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_SPECIAL_EDITION, BOOK_SPECIAL_EDITION_LABEL, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Intrusions\", true, \"Coffret L'affaire Sonoda\", 3, 2, 3);\n"); //book_id 107
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Samuel Earp\", 4, 2, 3);\n"); //book_id 108
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Deus ex machina\", 5, 2, 3);\n"); //book_id 109
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_SPECIAL_EDITION, BOOK_SPECIAL_EDITION_LABEL, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le sixième doigt du Penjab\", true, \"Edition spéciale\", 6, 2, 3);\n"); //book_id 110
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_SPECIAL_EDITION, BOOK_SPECIAL_EDITION_LABEL, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le sixième doigt du Penjab\", true, \"Coffret La question E.G.M\", 6, 2, 3);\n"); //book_id 111
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_SPECIAL_EDITION, BOOK_SPECIAL_EDITION_LABEL, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'appel de Baïkonour\", true, \"Coffret La question E.G.M\", 7, 2, 3);\n"); //book_id 112
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_SPECIAL_EDITION, BOOK_SPECIAL_EDITION_LABEL, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Dans le vide de Kirkwood\", true, \"Coffret La question E.G.M\", 8, 2, 3);\n"); //book_id 113
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Vendetta\", 9, 2, 3);\n"); //book_id 114
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Mazzere\", 10, 2, 3);\n"); //book_id 115
        // Siegfried série 21
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_SPECIAL_EDITION, BOOK_SPECIAL_EDITION_LABEL, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Siegfried\", true, \"Edition spéciale\", 1, 21, 2);\n"); //book_id 116
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Siegfried II\", 2, 21, 2);\n"); //book_id 117
        // Kookaburra série 4
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_SPECIAL_EDITION, BOOK_SPECIAL_EDITION_LABEL, SERIE_ID, EDITOR_ID) VALUES(\"Kookaburra 1-3\", true, \"Relié tomes 1 à 3\", 4, 6);\n"); //book_id 118
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_SPECIAL_EDITION, BOOK_SPECIAL_EDITION_LABEL, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Système Ragnarok\", true, \"Collection 2B\", 4, 4, 6);\n"); //book_id 119
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Système Ragnarok\", 4, 4, 6);\n"); //book_id 120
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Retour à Terradoes\", 5, 4, 6);\n"); //book_id 121
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'héritier des âmes\", 6, 4, 6);\n"); //book_id 122
        // Kookaburra Universe série 22
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le secret du sniper\", 1, 22, 6);\n"); //book_id 123
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Taman Kha\", 2, 22, 6);\n"); //book_id 124
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Mano Kha\", 3, 22, 6);\n"); //book_id 125
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Skullface\", 4, 22, 6);\n"); //book_id 126
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les larmes de Gosharad\", 5, 22, 6);\n"); //book_id 127
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le serment Dakoïd\", 6, 22, 6);\n"); //book_id 128
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le sourire de Myra\", 7, 22, 6);\n"); //book_id 129
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le lamentin noir\", 9, 22, 6);\n"); //book_id 130
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les prêtresses d'Isis\", 10, 22, 6);\n"); //book_id 131
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'île des amantes religieuses\", 11, 22, 6);\n"); //book_id 132
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'honneur du sniper\", 12, 22, 6);\n"); //book_id 133
        // Lucky luke série 7
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les Dalton se rachètent\", 26, 7, 9);\n"); //book_id 134
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Canyon apache\", 37, 7, 2);\n"); //book_id 135
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La guérison des Dalton\", 44, 7, 2);\n"); //book_id 136
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'empereur Smith\", 45, 7, 2);\n"); //book_id 137
        // angela (pas de série)
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, EDITOR_ID) VALUES(\"Angela\", 3);\n"); //book_id 138
        // Lanfeust des étoiles série 6
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Un, deux, Troy\", 1, 6, 6);\n"); //book_id 139
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les tours de Meirrion\", 2, 6, 6);\n"); //book_id 140
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les sables d'Abraxar\", 3, 6, 6);\n"); //book_id 141
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les buveurs de mondes\", 4, 6, 6);\n"); //book_id 142
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La chevauchée des bactéries\", 5, 6, 6);\n"); //book_id 143
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le râle du flibustier\", 6, 6, 6);\n"); //book_id 144
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_SPECIAL_EDITION, BOOK_SPECIAL_EDITION_LABEL, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le secret des Dolphantes\", true, \"29 février 2008\", 7, 4, 6);\n"); //book_id 145
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le secret des Dolphantes\", 7, 6, 6);\n"); //book_id 146
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le sang des comètes\", 8, 6, 6);\n"); //book_id 147
        // Lanfeust Odyssey série 23
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'énigme Or-Azur\", 1, 23, 6);\n"); //book_id 148
        // Cixi de Troy série 24
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le secret de Cixi (I)\", 1, 24, 6);\n"); //book_id 149
        // Tykko des sables série 25
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les chevaucheurs des vents\", 1, 25, 6);\n"); //book_id 150
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La cité engloutie\", 2, 25, 6);\n"); //book_id 151
        // Les naufragés d'Ythaq 26
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Terra incognita\", 1, 26, 6);\n"); //book_id 152
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Ophyde la géminée\", 2, 26, 6);\n"); //book_id 153
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le soupir des étoiles\", 3, 26, 6);\n"); //book_id 154
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'ombre de Khengis\", 4, 26, 6);\n"); //book_id 155
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'ultime arcane\", 5, 26, 6);\n"); //book_id 156
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La révolte des pions\", 6, 26, 6);\n"); //book_id 157
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La marque des Ythes\", 7, 26, 6);\n"); //book_id 158
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'impossible vérité\", 9, 26, 6);\n"); //book_id 159
        // Les forêts d'Opale série 27
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le bracelet de Cohars\", 1, 27, 6);\n"); //book_id 160
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'envers du grimoire\", 2, 27, 6);\n"); //book_id 161
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La cicatrice verte\", 3, 27, 6);\n"); //book_id 162
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les geoles de Nénuphe\", 4, 27, 6);\n"); //book_id 163
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Onze racines\", 5, 27, 6);\n"); //book_id 164
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le sortilège du pontife\", 6, 27, 6);\n"); //book_id 165
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les dents de pierre\", 7, 27, 6);\n"); //book_id 166
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les hordes de la nuit\", 8, 27, 6);\n"); //book_id 167
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"Le codex d'Opale\", 27, 6);\n"); //book_id 168
        // Streamliner série 28
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Bye-bye Lisa Dora\", 1, 28, 11);\n"); //book_id 169
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"All-in day\", 2, 28, 11);\n"); //book_id 170
        // Le troisième testament série 29
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_AUTOGRAPH, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Marc, ou le réveil du lion\", true, 1, 29, 4);\n"); //book_id 171
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_AUTOGRAPH, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Mathieu, ou le visage de l'Ange\", true, 2, 29, 4);\n"); //book_id 172
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_AUTOGRAPH, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Luc, ou le souffle du taureau\", true, 3, 29, 4);\n"); //book_id 173
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Jean, ou le jour du corbeau\", 4, 29, 4);\n"); //book_id 174
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_AUTOGRAPH, BOOK_SPECIAL_EDITION, BOOK_SPECIAL_EDITION_LABEL, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Jean, ou le jour du corbeau\", true, true, \"Encré Noir & Blanc\", 4, 29, 4);\n"); //book_id 175
        // Corto Maltese série 30
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"La Ballade de la mer salée - Couleur\", 30, 1);\n"); //book_id 176
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"Corto Maltese en sibérie\", 30, 1);\n"); //book_id 177
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"Fable de Venise\", 30, 1);\n"); //book_id 178
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"La maison dorée de Samarkand\", 30, 1);\n"); //book_id 179
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"Tango\", 30, 1);\n"); //book_id 180
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"Les helvétiques\", 30, 1);\n"); //book_id 181
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"Mû\", 30, 1);\n"); //book_id 182
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"Suite Caraïbéénne\", 30, 1);\n"); //book_id 183
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"Sous le drapeau des pirates\", 30, 1);\n"); //book_id 184
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"La lagune des mystères\", 30, 1);\n"); //book_id 185
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"Sous le soleil de Minuit\", 30, 1);\n"); //book_id 186
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"Equatoria\", 30, 1);\n"); //book_id 187
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_SPECIAL_EDITION, BOOK_SPECIAL_EDITION_LABEL, SERIE_ID, EDITOR_ID) VALUES(\"Equatoria\", true, \"Encré Noir & Blanc\", 30, 1);\n"); //book_id 188
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"Le jour de Tarowean\", 30, 1);\n"); //book_id 189
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"La lune\", 30, 1);\n"); //book_id 190
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, SERIE_ID, EDITOR_ID) VALUES(\"La cour secrête des arcanes\", 30, 1);\n"); //book_id 191
        // Universal War One série 31
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La genèse\", 1, 31, 6);\n"); //book_id 192
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le fruit de la connaissance\", 2, 31, 6);\n"); //book_id 193
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Caïn et Abel\", 3, 31, 6);\n"); //book_id 194
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le déluge\", 4, 31, 6);\n"); //book_id 195
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Babel\", 5, 31, 6);\n"); //book_id 196
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le patriarche\", 6, 31, 6);\n"); //book_id 197
        // Canardo 32
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Canardo - Premières enquêtes\", 0, 32, 12);\n"); //book_id 198
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Premières enquêtes\", 0, 32, 1);\n"); //book_id 199
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le chien debout\", 1, 32, 1);\n"); //book_id 200
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La marque de Raspoutine\", 2, 32, 1);\n"); //book_id 201
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La mort douce\", 3, 32, 1);\n"); //book_id 202
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Noces de brume\", 4, 32, 1);\n"); //book_id 203
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'amerzone\", 5, 32, 1);\n"); //book_id 204
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'île noyée\", 7, 32, 1);\n"); //book_id 205
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le canal de l'angoisse\", 8, 32, 1);\n"); //book_id 206
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La fille qui rêvait d'horizon\", 10, 32, 1);\n"); //book_id 207
        // Les aventures extraordinaires d'Adèle Blanc-Sec Série 33
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Adèle et la bête\", 1, 33, 1);\n"); //book_id 208
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le démon de la tour Eiffel\", 2, 33, 1);\n"); //book_id 209
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le savant fou\", 3, 33, 1);\n"); //book_id 210
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Momies en folie\", 4, 33, 1);\n"); //book_id 211
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le secret de la salamandre\", 5, 33, 1);\n"); //book_id 212
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le noyé à deux têtes\", 6, 33, 1);\n"); //book_id 213
        // Le transperceneige
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, EDITOR_ID) VALUES(\"Le transperceneige - Intégrale\", 1);\n"); //book_id 214
        // Le Voyageur
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, EDITOR_ID) VALUES(\"Le Voyageur\", 13);\n"); //book_id 215
        // XIII
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le jour du soleil noir\", 1, 34, 2);\n"); //book_id 216
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Là où va l'indien...\", 2, 34, 2);\n"); //book_id 217
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Toutes les larmes de l'enfer\", 3, 34, 2);\n"); //book_id 218
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"SPADS\", 4, 34, 2);\n"); //book_id 219
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Rouge total\", 5, 34, 2);\n"); //book_id 220
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le dossier Jason Fly\", 6, 34, 2);\n"); //book_id 221
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La nuit du 3 Août\", 7, 34, 2);\n"); //book_id 222
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Treize contre un\", 8, 34, 2);\n"); //book_id 223
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Pour Maria\", 9, 34, 2);\n"); //book_id 224
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"El Cascador\", 10, 34, 2);\n"); //book_id 225
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Trois montres d'argent\", 11, 34, 2);\n"); //book_id 226
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le jugement\", 12, 34, 2);\n"); //book_id 227
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"The XIII mystrey - L'enquête\", 13, 34, 2);\n"); //book_id 228
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Secret défense\", 14, 34, 2);\n"); //book_id 229
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Lâchez les chiens!\", 15, 34, 2);\n"); //book_id 230
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Opération Montecristo\", 16, 34, 2);\n"); //book_id 231
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'or de Maximilien\", 17, 34, 2);\n"); //book_id 232
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La version Irlandaise\", 18, 34, 2);\n"); //book_id 233
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le dernier Round\", 19, 34, 2);\n"); //book_id 234
        // Largo Winch
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'héritier\", 1, 35, 9);\n"); //book_id 235
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le groupe W\", 2, 35, 9);\n"); //book_id 236
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"O.P.A\", 3, 35, 9);\n"); //book_id 237
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Business blues\", 4, 35, 9);\n"); //book_id 238
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"H\", 5, 35, 9);\n"); //book_id 239
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Dutch connection\", 6, 35, 9);\n"); //book_id 240
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La forteresse de Makiling\", 7, 35, 9);\n"); //book_id 241
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'heure du tigre\", 8, 35, 9);\n"); //book_id 242
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Voir Venise...\", 9, 35, 9);\n"); //book_id 243
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"...Et mourir\", 10, 35, 9);\n"); //book_id 244
        // Private ghost
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_AUTOGRAPH, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Red label voodoo - 1ère édition\", true, 1, 36, 6);\n"); //book_id 245
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_AUTOGRAPH, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Red label voodoo\", true, 1, 36, 6);\n"); //book_id 246
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_AUTOGRAPH, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"White bloody Mary\", true, 2, 36, 6);\n"); //book_id 247
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Hot carribean rainbow\", 3, 36, 6);\n"); //book_id 248
        // Les passagers du vent
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La fille sous la dunette\", 1, 37, 1);\n"); //book_id 249
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le ponton\", 2, 37, 1);\n"); //book_id 250
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le comptoir de Juda\", 3, 37, 1);\n"); //book_id 251
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'heure du serpent\", 4, 37, 1);\n"); //book_id 252
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le bois d'ébène\", 5, 37, 1);\n"); //book_id 253
        db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Livre 1 - La petite fille bois-caïman\", 6, 37, 14);\n"); //book_id 254









        /** DETAINING i -> dernier book_id */
        for (int bookID = 1; bookID <= 254; bookID++) {
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(" + bookID + ", 1);\n");
        }

        /** WRITING */
        //Lanfeust de Troy - Book_id 1-8
        for (int bookID = 1; bookID <= 8; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",9);\n"); // Arleston 9
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",11);\n"); // Tarquin 11
        }
        /*------------------------------------------------------------------------------------*/
        //Trolls de Troy - Book_id 9-21
        for (int bookID = 9; bookID <= 21; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",9);\n"); // Arleston 9
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",10);\n"); // Mourier 10
        }
        /*------------------------------------------------------------------------------------*/
        //Les feux d'Askell - Book_id 22-24
        for (int bookID = 22; bookID <= 24; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",9);\n"); // Arleston 9
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",10);\n"); // Mourier 10
        }
        /*------------------------------------------------------------------------------------*/
        //Sillage - Book_id 25-36
        for (int bookID = 25; bookID <= 36; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",1);\n"); // Buchet 1
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",14);\n"); // Morvan 14
        }
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(26, 13);\n"); // Color Twins 13
        /*------------------------------------------------------------------------------------*/
        // Luuna - Book_id 37-43
        for (int bookID = 37; bookID <= 43; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",2);\n"); // Crisse 2
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",15);\n"); // Keramidas 15
        }
        /*------------------------------------------------------------------------------------*/
        // Leo Loden - Book_id 44-50
        for (int bookID = 44; bookID <= 49; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",9);\n"); // Arleston 9
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",16);\n"); // Carrere 16
        }
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(49,17);\n"); // Nicoloff 17
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(50,16);\n"); // Carrere 16
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(50,17);\n"); // Nicoloff 17
        /*------------------------------------------------------------------------------------*/
        // Joe Bar Team - Book_id 51-57
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(51,18);\n"); // Bar2
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(52,19);\n"); // 'Fane
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(53,19);\n"); // 'Fane
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(54,19);\n"); // 'Fane
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(55,18);\n"); // Bar2
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(56,19);\n"); // 'Fane
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(57,20);\n"); // Jenfevre
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(57,21);\n"); // Perna
        /*------------------------------------------------------------------------------------*/
        // Clifton - Book_id 58-67
        for (int bookID = 58; bookID <= 64; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",22);\n"); // Turk 22
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",23);\n"); // De Groot 23
        }
        for (int bookID = 65; bookID <= 67; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",23);\n"); // De Groot 23
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",24);\n"); // Bedu 24
        }
        /*------------------------------------------------------------------------------------*/
        // Spirou et Fantasio - Book_id 68-75
        for (int bookID = 68; bookID <= 69; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",27);\n"); // Franquin 27
        }
        for (int bookID = 70; bookID <= 75; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",25);\n"); // Tome 25
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",26);\n"); // Janry 26
        }
        /*------------------------------------------------------------------------------------*/
        // Le petit Spirou - Book_id 76-77
        for (int bookID = 76; bookID <= 77; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",25);\n"); // Tome 25
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",26);\n"); // Janry 26
        }
        /*------------------------------------------------------------------------------------*/
        // Les Schtroumpfs - Book_id 78-81
        for (int bookID = 78; bookID <= 81; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",28);\n"); // Peyo 28
        }
        /*------------------------------------------------------------------------------------*/
        // Les Conquérants de Troy - Book_id 82-84
        for (int bookID = 82; bookID <= 84; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",9);\n"); // Arleston 9
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",29);\n"); // Tota 29
        }
        /*------------------------------------------------------------------------------------*/
        // Astérix - Book_id 85-94
        for (int bookID = 85; bookID <= 89; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",6);\n"); // Goscinny 6
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",8);\n"); // Uderzo 8
        }
        for (int bookID = 90; bookID <= 94; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",8);\n"); // Uderzo 8
        }
        /*------------------------------------------------------------------------------------*/
        // Golden cup - Book_id 95-99
        for (int bookID = 95; bookID <= 96; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",30);\n"); // Pecqueur 30
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",31);\n"); // Henriet 31
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",32);\n"); // Schelle 32
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",33);\n"); // Rosa 33
        }
        for (int bookID = 97; bookID <= 98; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",30);\n"); // Pecqueur 30
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",31);\n"); // Henriet 31
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",32);\n"); // Schelle 32
        }
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(99,30);\n"); // Pecqueur 30
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(99,31);\n"); // Henriet 31
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(99,32);\n"); // Schelle 32
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(99,34);\n"); // Usagi 34
        /*------------------------------------------------------------------------------------*/
        // Code Mc Callum - Book_id 100-104
        for (int bookID = 100; bookID <= 104; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",3);\n"); // Duval 3
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",35);\n"); // Cassegrain 35
        }
        /*------------------------------------------------------------------------------------*/
        // Carmen Mc Callum - Book_id 105-115
        //105
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(105,33);\n"); // Guess 36
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(105,3);\n"); // Duval 3
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(105,38);\n"); // Breton 38
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(105,39);\n"); // Vatine 39
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(105,40);\n"); // Blanchard 40
        //106
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(106,33);\n"); // Guess 36
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(106,3);\n"); // Duval 3
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(106,38);\n"); // Breton 38
        for (int bookID = 107; bookID <= 109; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",36);\n"); // Guess 36
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",3);\n"); // Duval 3
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",37);\n"); // Rabarot 37
        }
        //110
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(110,33);\n"); // Guess 36
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(110,3);\n"); // Duval 3
        for (int bookID = 111; bookID <= 113; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",36);\n"); // Guess 36
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",3);\n"); // Duval 3
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",37);\n"); // Rabarot 37
        }
        for (int bookID = 114; bookID <= 115; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",3);\n"); // Duval 3
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",41);\n"); // Emem 41
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",32);\n"); // Schelle 32
        }
        /*------------------------------------------------------------------------------------*/
        // Siegfried - Book_id 116-117
        for (int bookID = 116; bookID <= 117; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",42);\n"); // Alex Alice 42
        }
        /*------------------------------------------------------------------------------------*/
        // Kookaburra - Book_id 118-122
        //118
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(118,2);\n"); // Crisse 2
        for (int bookID = 119; bookID <= 121; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",2);\n"); // Crisse 2
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",43);\n"); // Mitric 43
        }
        //122
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(122,43);\n"); // Mitric 43
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(122,44);\n"); // Lamirand 44
        /*------------------------------------------------------------------------------------*/
        // Kookaburra Universe - Book_id 123-133
        //123 T1
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(123,2);\n"); // Crisse 2
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(123,43);\n"); // Mitric 43
        //124-125 T2 T3
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(124,2);\n"); // Crisse 2
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(124,45);\n"); // Ange 45
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(124,46);\n"); // Paty 46
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(125,2);\n"); // Crisse 2
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(125,45);\n"); // Ange 45
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(125,46);\n"); // Paty 46
        //126 T4
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(126,2);\n"); // Crisse 2
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(126,47);\n"); // Gaudin 47
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(126,48);\n"); // Briones 48
        //127 T5
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(127,2);\n"); // Crisse 2
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(127,49);\n"); // Tackian 49
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(127,50);\n"); // Miquel 50
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(127,51);\n"); // Ludolullabi 51
        //128 T6
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(128,2);\n"); // Crisse 2
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(128,43);\n"); // Mitric 43
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(128,52);\n"); // Péru 52
        //129 T7
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(129,2);\n"); // Crisse 2
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(129,53);\n"); // Runberg 53
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(129,54);\n"); // Ocaña 54
        //130 T9
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(130,2);\n"); // Crisse 2
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(130,43);\n"); // Mitric 43
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(130,55);\n"); // Louis 55
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(130,44);\n"); // Lamirand 44
        //131 T10
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(131,2);\n"); // Crisse 2
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(131,43);\n"); // Mitric 43
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(131,56);\n"); // Mariolle 56
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(131,57);\n"); // Aurore 57
        //132 T11
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(132,2);\n"); // Crisse 2
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(132,43);\n"); // Mitric 43
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(132,58);\n"); // Paille 58
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(132,59);\n"); // Campoy 59
        //133 T12
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(133,2);\n"); // Crisse 2
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(133,43);\n"); // Mitric 43
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(133,60);\n"); // Alliel 60
        /*------------------------------------------------------------------------------------*/
        // Lucky Luke - Book_id 134-137
        for (int bookID = 134; bookID <= 137; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",5);\n"); // Goscinny 5
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",61);\n"); // Morris 61
        }
        /*------------------------------------------------------------------------------------*/
        // angela - Book_id 138
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(138,39);\n"); // Vatine 39
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(138,30);\n"); // Pecqueur 30
        /*------------------------------------------------------------------------------------*/
        // Lanfeust des étoiles - Book_id 138-147
        for (int bookID = 138; bookID <= 147; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",9);\n"); // Arleston 9
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",11);\n"); // Tarquin 11
        }
        /*------------------------------------------------------------------------------------*/
        // Lanfeust Odyssey - Book_id 148
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(148,9);\n"); // Arleston 9
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(148,11);\n"); // Tarquin 11
        /*------------------------------------------------------------------------------------*/
        // Cixi de Troy - Book_id 149
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(149,9);\n"); // Arleston 9
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(149,39);\n"); // Vatine 39
        /*------------------------------------------------------------------------------------*/
        // Tykko des sables - Book_id 150-151
        for (int bookID = 150; bookID <= 151; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",9);\n"); // Arleston 9
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",62);\n"); // Melanÿa 62
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",15);\n"); // Keramidas 15
        }
        /*------------------------------------------------------------------------------------*/
        // Les naufragés d'Ythaq - Book_id 152-159
        for (int bookID = 152; bookID <= 159; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",9);\n"); // Arleston 9
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",63);\n"); // Floch 63
        }
        /*------------------------------------------------------------------------------------*/
        // Les forêts d'Opale - Book_id 160-168
        for (int bookID = 160; bookID <= 168; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",9);\n"); // Arleston 9
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",64);\n"); // Pellet 64
        }
        /*------------------------------------------------------------------------------------*/
        // Streamliner - Book_id 169-170
        for (int bookID = 169; bookID <= 170; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",19);\n"); // 'Fane 19
        }
        /*------------------------------------------------------------------------------------*/
        // Le troisième testament - Book_id 171-175
        for (int bookID = 171; bookID <= 175; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",42);\n"); // Alex Alice 42
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",65);\n"); // dorison 65
        }
        /*------------------------------------------------------------------------------------*/
        // Corto Maltese - Book_id 176-191
        for (int bookID = 176; bookID <= 185; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",7);\n"); // Hugo Pratt 7
        }
        for (int bookID = 186; bookID <= 189; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",66);\n"); // Diaz Canales 66
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",67);\n"); // Pellejero 67
        }
        //190
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(190,7);\n"); // Hugo Pratt 7
        //191
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(191,68);\n"); // Olivier Delcroix
        /*------------------------------------------------------------------------------------*/
        // Universal War One - Book_id 192-197
        for (int bookID = 192; bookID <= 197; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",69);\n"); // Bajram 69
        }
        /*------------------------------------------------------------------------------------*/
        // Canardo - Book_id 198-207
        for (int bookID = 197; bookID <= 207; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",70);\n"); // Sokal 70
        }
        /*------------------------------------------------------------------------------------*/
        // Les aventures extraordinaires d'Adèle Blanc-Sec - Book_id 208-213
        for (int bookID = 208; bookID <= 213; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",71);\n"); // Tardi 71
        }
        /*------------------------------------------------------------------------------------*/
        // Le transperceneige - Book_id 214
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(214,72);\n"); // Lob 72
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(214,73);\n"); // Rochette 73
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(214,74);\n"); // Legrand 74
        /*------------------------------------------------------------------------------------*/
        // Le Voyageur - Book_id 215
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(215,72);\n"); // Koren Shadmi 75
        /*------------------------------------------------------------------------------------*/
        // XIII - Book_id 216-234
        for (int bookID = 216; bookID <= 232; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",76);\n"); // W. Vance 76
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",77);\n"); // J. Van Hamme 77
        }
        //233
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(233,77);\n"); // J. Van Hamme 77
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(233,78);\n"); // J. Giraud 78
        //234
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(234,76);\n"); // W. Vance 76
        db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(234,77);\n"); // J. Van Hamme 77
        /*------------------------------------------------------------------------------------*/
        // Largo Winch - Book_id 235-244
        for (int bookID = 235; bookID <= 244; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",79);\n"); // Francq 79
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",77);\n"); // J. Van Hamme 77
        }
        /*------------------------------------------------------------------------------------*/
        // Largo Winch - Book_id 245-248
        for (int bookID = 245; bookID <= 248; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",2);\n"); // Crisse 2
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",16);\n"); // Carrere 16
        }
        /*------------------------------------------------------------------------------------*/
        // Largo Winch - Book_id 249-254
        for (int bookID = 249; bookID <= 254; bookID++) {
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(" + bookID + ",80);\n"); // Bourgeon 80
        }







    }
}
