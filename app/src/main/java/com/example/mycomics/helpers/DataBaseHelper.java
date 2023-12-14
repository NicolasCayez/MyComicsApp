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
    public static final String COLUMN_BOOK_SPECIAL_EDITION_LABEL = "BOOK_SPECIAL_EDITION_LABE";
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


/**************************************************************************************************/
/** Entrées de Test
/**************************************************************************************************/
            // AUTHORS
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

            // EDITORS
            db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Casterman\");\n"); //editor_id 1
            db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Dargaud\");\n"); //editor_id 2
            db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Delcourt\");\n"); //editor_id 3
            db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Glenat\");\n"); //editor_id 4
            db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Pilote\");\n"); //editor_id 5
            db.execSQL("INSERT INTO EDITORS(EDITOR_NAME) VALUES(\"Soleil Editions\");\n"); //editor_id 6

            // SERIES
            db.execSQL("INSERT INTO SERIES(SERIE_NAME) VALUES(\"Asterix\");\n"); //serie_id 1
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

            // BOOKS & DETAINING
            // Lanfeust de Troy serie 5
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'ivoire du Magohamoth\", 1, 5, 6);\n"); //book_id 1
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(1, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Thanos l'incongru\", 2, 5, 6);\n"); //book_id 2
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(2, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Castel Or-Azur\", 3, 5, 6);\n"); //book_id 3
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(3, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le paladin d'Eckmül\", 4, 5, 6);\n"); //book_id 4
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(4, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le frisson de l'haruspice\", 5, 5, 6);\n"); //book_id 5
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(5, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Cixi impératrice\", 6, 5, 6);\n"); //book_id 6
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(6, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les pétaures se cachent pour mourir\", 7, 5, 6);\n"); //book_id 7
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(7, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La bête fabuleuse\", 8, 5, 6);\n"); //book_id 8
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(8, 1);\n");
            // Trolls de Troy serie 10
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Histoires Trolles\", 1, 10, 6);\n"); //book_id 9
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(9, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le scalp du vénérable\", 2, 10, 6);\n"); //book_id 10
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(10, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Comme un vol de pétaures\", 3, 10, 6);\n"); //book_id 11
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(11, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le feu occulte\", 4, 10, 6);\n"); //book_id 12
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(12, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les maléfices de la thaumaturge\", 5, 10, 6);\n"); //book_id 13
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(13, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Trolls dans la brume\", 6, 10, 6);\n"); //book_id 14
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(14, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Plume de sage\", 7, 10, 6);\n"); //book_id 15
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(15, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Rock'n Troll attitude\", 8, 10, 6);\n"); //book_id 16
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(16, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les prisonniers du Darshan I\", 9, 10, 6);\n"); //book_id 17
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(17, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Les prisonniers du Darshan II\", 10, 10, 6);\n"); //book_id 18
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(18, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Trollympiades\", 11, 10, 6);\n"); //book_id 19
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(19, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Sang famille I\", 12, 10, 6);\n"); //book_id 20
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(20, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Boules de poils I\", 15, 10, 6);\n"); //book_id 21
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(21, 1);\n");
            // Les feux d'askell série 11
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"L'onguent admirable\", 1, 11, 6);\n"); //book_id 22
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(22, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Retour à Vocable\", 2, 11, 6);\n"); //book_id 23
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(23, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Corail sanglant\", 3, 11, 6);\n"); //book_id 24
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(24, 1);\n");
            // Sillage série 9
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"A feu et à cendres\", 1, 9, 3);\n"); //book_id 25
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(25, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Collection privée\", 2, 9, 3);\n"); //book_id 26
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(26, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Engrenages\", 3, 9, 3);\n"); //book_id 27
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(27, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le signe des démons\", 4, 9, 3);\n"); //book_id 28
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(28, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"FToRoSs\", 5, 9, 3);\n"); //book_id 29
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(29, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Artifices\", 6, 9, 3);\n"); //book_id 30
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(30, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Q.H.I.\", 7, 9, 3);\n"); //book_id 31
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(31, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Nature humaine\", 8, 9, 3);\n"); //book_id 32
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(32, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Infiltrations\", 9, 9, 3);\n"); //book_id 33
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(33, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_AUTOGRAPH, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Retour de flammes - Edition spéciale\", 10, true, 9, 3);\n"); //book_id 34
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(34, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Monde flottant\", 11, 9, 3);\n"); //book_id 35
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(35, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Zone franche\", 12, 9, 3);\n"); //book_id 36
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(36, 1);\n");
            // Luuna série 12
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La nuit des totems\", 1, 12, 6);\n"); //book_id 37
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(37, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le crépuscule du Lynx\", 2, 12, 6);\n"); //book_id 38
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(38, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Dans les traces d'Oh-mah-ah\", 3, 12, 6);\n"); //book_id 39
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(39, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Pok-ta-pok\", 4, 12, 6);\n"); //book_id 40
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(40, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"Le cercle des miroirs\", 5, 12, 6);\n"); //book_id 41
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(41, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La reine des loups\", 6, 12, 6);\n"); //book_id 42
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(42, 1);\n");
            db.execSQL("INSERT INTO BOOKS(BOOK_TITLE, BOOK_NUMBER, SERIE_ID, EDITOR_ID) VALUES(\"La source du temps\", 7, 12, 6);\n"); //book_id 43
            db.execSQL("INSERT INTO DETAINING(BOOK_ID, PROFILE_ID) VALUES(43, 1);\n");

            // WRITING
            //Lanfeust de Troy Arleston 9 Tarquin 11
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(1,9);\n"); //1
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(1,11);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(2, 9);\n"); //2
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(2, 11);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(3, 9);\n"); //3
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(3, 11);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(4, 9);\n"); //4
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(4, 11);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(5, 9);\n"); //5
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(5, 11);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(6, 9);\n"); //6
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(6, 11);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(7, 9);\n"); //7
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(7, 11);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(8, 9);\n"); //8
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(8, 11);\n");
            //Trolls de Troy Arleston 9 Mourier 10
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(9, 9);\n"); //1
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(9, 10);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(10, 9);\n"); //2
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(10, 10);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(11, 9);\n"); //3
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(11, 10);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(12, 9);\n"); //4
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(12, 10);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(13, 9);\n"); //5
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(13, 10);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(14, 9);\n"); //6
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(14, 10);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(15, 9);\n"); //7
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(15, 10);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(16, 9);\n"); //8
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(16, 10);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(17, 9);\n"); //9
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(17, 10);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(18, 9);\n"); //10
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(18, 10);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(19, 9);\n"); //11
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(19, 10);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(20, 9);\n"); //12
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(20, 10);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(21, 9);\n"); //15
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(21, 10);\n");
            //Les feux d'Askell Arleston 9 Mourier 10
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(22, 9);\n"); //1
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(22, 10);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(23, 9);\n"); //2
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(23, 10);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(24, 9);\n"); //3
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(24, 10);\n");
            //Sillage Buchet 1 Morvan 14 Color Twins 13
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(25, 1);\n"); //1
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(25, 14);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(26, 1);\n"); //2
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(26, 14);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(26, 13);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(27, 1);\n"); //3
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(27, 14);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(28, 1);\n"); //4
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(28, 14);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(29, 1);\n"); //5
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(29, 14);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(30, 1);\n"); //6
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(30, 14);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(31, 1);\n"); //7
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(31, 14);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(32, 1);\n"); //8
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(32, 14);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(33, 1);\n"); //9
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(33, 14);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(34, 1);\n"); //10
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(34, 14);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(35, 1);\n"); //11
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(35, 14);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(36, 1);\n"); //12
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(36, 14);\n");
            // Luuna Crisse 2 Keramidas 15
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(37, 2);\n"); //1
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(37, 15);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(38, 2);\n"); //2
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(38, 15);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(39, 2);\n"); //3
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(39, 15);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(40, 2);\n"); //4
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(40, 15);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(41, 2);\n"); //5
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(41, 15);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(42, 2);\n"); //6
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(42, 15);\n");
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(43, 2);\n"); //7
            db.execSQL("INSERT INTO WRITING(BOOK_ID, AUTHOR_ID) VALUES(43, 15);\n");
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
    public List<ProfileBean> getListProfileBean(String query){
        List<ProfileBean> returnList = new ArrayList<>(); // list to be returned
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
    public List<EditorBean> getListEditorBean(String query){
        List<EditorBean> returnList = new ArrayList<>(); // list to be returned
        SQLiteDatabase db = this.getReadableDatabase(); // Database read access
        Cursor cursor = db.rawQuery(query, null); // cursor = query result data
        if (cursor.moveToFirst()) { // true if there is at least 1 row
            do { // For each tuple in the cursor
                Integer editor_id = cursor.getInt(0); // 1 key-value pair at index 0
                String editor_name = cursor.getString(1); // 1 key-value pair at index 1
                EditorBean editorBean = new EditorBean(editor_id, editor_name); // bean created from this tuple
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
    public List<AuthorBean> getListAuthorBean(String query){
        List<AuthorBean> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Integer author_id = cursor.getInt(0);
                String author_pseudonym = cursor.getString(1);
                String author_first_name = cursor.getString(2);
                String author_last_name = cursor.getString(3);
                AuthorBean authorBean = new AuthorBean(author_id, author_pseudonym, author_last_name, author_first_name);
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
                authorBean = new AuthorBean(author_id, author_pseudonym, author_last_name, author_first_name);
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
    public List<SerieBean> getListSerieBean(String query){
        List<SerieBean> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Integer serie_id = cursor.getInt(0);
                String serie_name = cursor.getString(1);
                SerieBean serieBean = new SerieBean(serie_id, serie_name);
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
    public List<BookBean> getListBookBean(String query){
        List<BookBean> returnList = new ArrayList<BookBean>();
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
                boolean book_autograph = cursor.getInt(8) == 1 ? true: false;
                boolean book_special_edition = cursor.getInt(9) == 1 ? true: false;
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
                double book_editor_price = cursor.getDouble(4);
                double book_value = cursor.getDouble(5);
                String book_edition_date = cursor.getString(6);
                String book_picture = cursor.getString(7);
                boolean book_autograph = cursor.getInt(8) == 1 ? true: false;
                boolean book_special_edition = cursor.getInt(9) == 1 ? true: false;
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
    public List<BookSerieBean> getListBookSerieBean(String query){
        List<BookSerieBean> returnList = new ArrayList<>();
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
                boolean book_autograph = cursor.getInt(8) == 1 ? true: false;
                boolean book_special_edition = cursor.getInt(9) == 1 ? true: false;
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
                boolean tome_dedicace = cursor.getInt(8) == 1 ? true: false;
                boolean tome_edition_speciale = cursor.getInt(9) == 1 ? true: false;
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
    public List<ProfileBean> getProfilesList(){
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
    public List<EditorBean> getEditorsList(){
        String query = "SELECT DISTINCT * FROM " + EDITORS +
                " GROUP BY " + EDITORS + "." + COLUMN_EDITOR_ID +
                " ORDER BY " + EDITORS + "." + COLUMN_EDITOR_NAME;
        return getListEditorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT DISTINCT * FROM EDITORS
    // WHERE EDITORS.EDITOR_NAME LIKE '%filter%'
    // GROUP BY EDITORS.EDITOR_ID
    // ORDER BY EDITORS.EDITOR_NAME
    /* ------------------------------------------------------------------------------------------ */
    public List<EditorBean> getEditorsListByFilter(String filter){
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
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND EDITORS.EDITOR_NAME = editor_name LIMIT 1
    /* ------------------------------------------------------------------------------------------ */
    public EditorBean getEditorByName(String editor_name){
        String query = "SELECT * FROM " + EDITORS +
        " INNER JOIN " + BOOKS + " ON " + BOOKS + "." + COLUMN_EDITOR_ID + " = " + EDITORS + "." + COLUMN_EDITOR_ID +
        " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
        " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
        "\" AND " + EDITORS + "." + COLUMN_EDITOR_NAME + " = \"" + editor_name + "\" LIMIT 1";
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
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND ECRIRE.AUTHOR_ID = author_id
    // GROUP BY EDITORS.EDITOR_ID
    // ORDER BY EDITORS.EDITOR_NAME;
    /* ------------------------------------------------------------------------------------------ */
    public List<EditorBean> getEditorsByAuthorId(int author_id){
        String query = "SELECT * FROM " + EDITORS +
                " INNER JOIN " + BOOKS + " ON " + BOOKS + "." + COLUMN_EDITOR_ID + " = " + EDITORS + "." + COLUMN_EDITOR_ID +
                " INNER JOIN " + WRITING + " ON " + WRITING + "." + COLUMN_BOOK_ID + " = " + BOOKS + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND " + WRITING + "." + COLUMN_AUTHOR_ID + " = \"" + author_id +
                "\" GROUP BY " + EDITORS + "." + COLUMN_EDITOR_ID +
                " ORDER BY " + EDITORS + "." + COLUMN_EDITOR_NAME;
        return getListEditorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM EDITORS
    // JOIN BOOKS ON BOOKS.EDITOR_ID = EDITORS.EDITOR_ID
    // JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND BOOKS.SERIE_ID = serie_id
    /* ------------------------------------------------------------------------------------------ */
    public List<EditorBean> getEditorsBySerieId(int serie_id){
        String query = "SELECT * FROM " + EDITORS +
                " INNER JOIN " + BOOKS + " ON " + BOOKS + "." + COLUMN_EDITOR_ID + " = " + EDITORS + "." + COLUMN_EDITOR_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND " + BOOKS + "." + COLUMN_SERIE_ID + " = \"" + serie_id + "\"";
        return getListEditorBean(query);
    }

    /** AUTHORS **/
    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM AUTHORS
    // GROUP BY AUTHORS.AUTHOR_ID
    // ORDER BY AUTHORS.AUTHOR_PSEUDONYM
    /* ------------------------------------------------------------------------------------------ */
    public List<AuthorBean> getAuthorsList(){
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
    public List<AuthorBean> getAuthorsListByFilter(String filter){
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
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND WRITING.BOOK_ID = book_id
    // GROUP BY AUTHORS.AUTHOR_ID
    // ORDER BY AUTHORS.AUTHOR_PSEUDONYM
    /* ------------------------------------------------------------------------------------------ */
    public List<AuthorBean> getAuthorsListByBookId(int book_id){
        String query = "SELECT * FROM " + AUTHORS +
                " INNER JOIN " + WRITING + " ON " + AUTHORS + "." + COLUMN_AUTHOR_ID + " = " + WRITING + "." + COLUMN_AUTHOR_ID +
                " INNER JOIN " + BOOKS + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + WRITING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND " + WRITING + "." + COLUMN_BOOK_ID + " = \"" + book_id +
                "\" GROUP BY " + AUTHORS + "." + COLUMN_AUTHOR_ID +
                " ORDER BY " + AUTHORS + "." + COLUMN_AUTHOR_PSEUDONYM;
        return getListAuthorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM AUTHORS
    // INNER JOIN WRITING ON AUTHORS.AUTHOR_ID = WRITING.AUTHOR_ID
    // INNER JOIN BOOKS ON WRITING.BOOK_ID = BOOKS.BOOK_ID
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND BOOKS.EDITOR_ID = editor_id
    // GROUP BY AUTHORS.AUTHOR_ID
    // ORDER BY AUTHORS.AUTHOR_PSEUDONYM
    /* ------------------------------------------------------------------------------------------ */
    public List<AuthorBean> getAuthorsListByEditorId(int editor_id){
        String query = "SELECT * FROM " + AUTHORS +
                " INNER JOIN " + WRITING + " ON " + AUTHORS + "." + COLUMN_AUTHOR_ID + " = " + WRITING + "." + COLUMN_AUTHOR_ID +
                " INNER JOIN " + BOOKS + " ON " + WRITING + "." + COLUMN_BOOK_ID + " = " + BOOKS + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND " + BOOKS + "." + COLUMN_EDITOR_ID + " = \"" + editor_id +
                "\" GROUP BY " + AUTHORS + "." + COLUMN_AUTHOR_ID +
                " ORDER BY " + AUTHORS + "." + COLUMN_AUTHOR_PSEUDONYM;
        return getListAuthorBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM AUTHORS
    // INNER JOIN WRITING ON AUTHORS.AUTHOR_ID = WRITING.AUTHOR_ID
    // INNER JOIN BOOKS ON BOOKS.BOOK_ID = WRITING.BOOK_ID
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND BOOKS.SERIE_ID = serie_id
    // GROUP BY AUTHORS.AUTHOR_ID
    // ORDER BY AUTHORS.AUTHOR_PSEUDONYM
    /* ------------------------------------------------------------------------------------------ */
    public List<AuthorBean> getAuthorsListBySerieId(int serie_id){
        String query = "SELECT * FROM " + AUTHORS +
                " INNER JOIN " + WRITING + " ON " + WRITING + "." + COLUMN_AUTHOR_ID + " = " + AUTHORS + "." + COLUMN_AUTHOR_ID +
                " INNER JOIN " + BOOKS + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + WRITING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND " + BOOKS + "." + COLUMN_SERIE_ID + " = \"" + serie_id +
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
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND A1.AUTHOR_ID <> A2.AUTHOR_ID
    // AND A2.AUTHOR_ID = author_id
    /* ------------------------------------------------------------------------------------------ */
    public List<AuthorBean> getAuthorsTeam(int author_id){
        String query = "SELECT DISTINCT A1.* FROM " + AUTHORS + " A1" +
                " INNER JOIN " + WRITING + " W1 ON A1." + COLUMN_AUTHOR_ID + " = W1." + COLUMN_AUTHOR_ID +
                " INNER JOIN " + WRITING + " W2 ON W1." + COLUMN_BOOK_ID + " = W2." + COLUMN_BOOK_ID +
                " INNER JOIN " + AUTHORS + " A2 ON W2." + COLUMN_AUTHOR_ID + "= A2." + COLUMN_AUTHOR_ID +
                " INNER JOIN " + BOOKS + " ON W1." + COLUMN_BOOK_ID + " = " + BOOKS + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND A1." + COLUMN_AUTHOR_ID + " <> A2." + COLUMN_AUTHOR_ID +
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
    public List<SerieBean> getSeriesList(){
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
    public List<SerieBean> getSeriesListByFilter(String filter){
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
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND BOOKS.EDITOR_ID = editor_id
    // GROUP BY SERIES.SERIE_ID
    // ORDER BY SERIES.SERIE_NAME
    /* ------------------------------------------------------------------------------------------ */
    public List<SerieBean> getSeriesListByEditorId(int editor_id){
        String query = "SELECT DISTINCT * FROM " + SERIES +
                " INNER JOIN " + BOOKS + " ON " + SERIES + "." + COLUMN_SERIE_ID + " = " + BOOKS + "." + COLUMN_SERIE_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND " + BOOKS + "." + COLUMN_EDITOR_ID + " = \"" + editor_id +
                "\" GROUP BY " + SERIES + "." + COLUMN_SERIE_ID +
                " ORDER BY " + SERIES + "." + COLUMN_SERIE_NAME;
        return getListSerieBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM SERIES
    // INNER JOIN BOOKS ON BOOKS.SERIE_ID = SERIES.SERIE_ID
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND BOOKS.BOOK_ID = book_id LIMIT 1
    /* ------------------------------------------------------------------------------------------ */
    public SerieBean getSerieByBookId(int book_id){
        String query = "SELECT * FROM " + SERIES +
                " INNER JOIN " + BOOKS + " ON " + SERIES + "." + COLUMN_SERIE_ID + " = " + BOOKS + "." + COLUMN_SERIE_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND " + BOOKS + "." + COLUMN_BOOK_ID + " = \"" + book_id + "\" LIMIT 1";
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
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND WRITING.AUTHOR_ID = author_id
    // GROUP BY SERIES.SERIE_ID
    // ORDER BY SERIES.SERIE_NAME
    /* ------------------------------------------------------------------------------------------ */
    public List<SerieBean> getSeriesListByAuthorId(int author_id){
        String query = "SELECT DISTINCT * FROM " + SERIES +
                " INNER JOIN " + BOOKS + " ON " + BOOKS + "." + COLUMN_SERIE_ID + " = " + SERIES + "." + COLUMN_SERIE_ID +
                " INNER JOIN " + WRITING + " ON " + WRITING + "." + COLUMN_BOOK_ID + " = " + BOOKS + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND " + WRITING + "." + COLUMN_AUTHOR_ID + " = \"" + author_id +
                "\" GROUP BY " + SERIES + "." + COLUMN_SERIE_ID +
                " ORDER BY " + SERIES + "." + COLUMN_SERIE_NAME;
        return getListSerieBean(query);
    }

    /** BOOKS **/
    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM BOOKS
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // GROUP BY BOOKS.SERIE_ID
    // ORDER BY BOOKS.BOOK_NUMBER, BOOKS.BOOK_TITLE
    /* ------------------------------------------------------------------------------------------ */
    public List<BookBean> getBooksList(){
        String query = "SELECT * FROM " + BOOKS +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" GROUP BY " + BOOKS + "." + COLUMN_SERIE_ID +
                " ORDER BY " + BOOKS + "." + COLUMN_BOOK_NUMBER + ", " + BOOKS + "." + COLUMN_BOOK_TITLE;
        return getListBookBean(query); // résultat de la requête
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM BOOKS
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND (BOOKS.BOOK_TITLE LIKE '%filter%'
    // OR BOOKS.BOOK_NUMBER LIKE '%filter%')
    // ORDER BY BOOKS.BOOK_NUMBER, BOOKS.BOOK_TITLE
    /* ------------------------------------------------------------------------------------------ */
    public List<BookBean> getBooksListByFilter(String filter){
        String query = "SELECT * FROM " + BOOKS +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND (" + BOOKS + "." + COLUMN_BOOK_TITLE + " LIKE \'%" + filter +
                "%\' OR " + BOOKS + "." + COLUMN_BOOK_NUMBER + " LIKE \'%" + filter +
                "%\') ORDER BY " + BOOKS + "." + COLUMN_BOOK_NUMBER + ", " + BOOKS + "." + COLUMN_BOOK_TITLE;
        return getListBookBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM BOOKS
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND BOOKS.BOOK_TITLE = book_title
    /* ------------------------------------------------------------------------------------------ */
    public boolean checkBookDuplicate(String book_title){
        String query = "SELECT COUNT(*) FROM " + BOOKS +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND " + BOOKS + "." + COLUMN_BOOK_TITLE + " = \"" + book_title + "\"";
        return checkDuplicate(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT DISTINCT BOOKS.*, SERIE_NAME FROM BOOKS, SERIES
    // LEFT JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // LEFT JOIN PROFILE_ACTIVE ON PROFILE_ACTIVE.PROFILE_ID = DETAINING.PROFILE_ID
    // WHERE BOOKS.SERIE_ID = SERIES.SERIE_ID
    // AND DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // UNION
    // SELECT DISTINCT BOOKS.*, NULL AS SERIE_NAME FROM BOOKS
    // LEFT JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // LEFT JOIN PROFILE_ACTIVE ON PROFILE_ACTIVE.PROFILE_ID = DETAINING.PROFILE_ID
    // WHERE (BOOKS.SERIE_ID IS NULL
    // OR BOOKS.SERIE_ID = -1)
    // AND DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // ORDER BY SERIES.SERIE_NAME, BOOKS.BOOK_NUMBER, BOOKS.BOOK_TITLE
    /* ------------------------------------------------------------------------------------------ */

    public List<BookSerieBean> getBooksAndBooksSeriesList(){
        String query = "SELECT DISTINCT " + BOOKS + ".*, " + COLUMN_SERIE_NAME + " FROM " + BOOKS + ", " + SERIES +
                " LEFT JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " LEFT JOIN " + PROFILE_ACTIVE + " ON " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID + " = " + DETAINING + "." + COLUMN_PROFILE_ID +
                " WHERE " + BOOKS + "." + COLUMN_SERIE_ID + " = " + SERIES + "." + COLUMN_SERIE_ID +
                 " AND " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" UNION " +
                "SELECT DISTINCT " + BOOKS + ".*, NULL AS " + COLUMN_SERIE_NAME + " FROM " + BOOKS +
                " LEFT JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " LEFT JOIN " + PROFILE_ACTIVE + " ON " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID + " = " + DETAINING + "." + COLUMN_PROFILE_ID +
                " WHERE (" + BOOKS + "." + COLUMN_SERIE_ID + " IS NULL" +
                " OR " + BOOKS + "." + COLUMN_SERIE_ID + " = -1" +
                ") AND " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" ORDER BY " + SERIES + "." + COLUMN_SERIE_NAME + ", " + BOOKS + "." + COLUMN_BOOK_NUMBER + ", " + BOOKS + "." + COLUMN_BOOK_TITLE;
        return getListBookSerieBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT BOOKS.*, SERIES.SERIE_NAME FROM BOOKS , SERIES
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // INNER JOIN PROFILE_ACTIVE ON PROFILE_ACTIVE.PROFILE_ID = DETAINING.PROFILE_ID
    // WHERE BOOKS.SERIE_ID = SERIES.SERIE_ID
    // AND DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND (BOOKS.BOOK_TITLE LIKE '%filter%'
    // OR BOOKS.BOOK_NUMBER LIKE '%filter%'
    // OR SERIES.SERIE_NAME LIKE '%filter%')
    // ORDER BY SERIES.SERIE_NAME, BOOKS.BOOK_NUMBER, BOOKS.BOOK_TITLE
    /* ------------------------------------------------------------------------------------------ */
    public List<BookSerieBean> getBooksAndSeriesListByFilter(String filtre){
        String query = "SELECT " + BOOKS + ".*, " + SERIES + "." + COLUMN_SERIE_NAME + " FROM " + BOOKS + ", " + SERIES +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + PROFILE_ACTIVE + " ON " + PROFILE_ACTIVE + "." + COLUMN_PROFILE_ID + " = " + DETAINING + "." + COLUMN_PROFILE_ID +
                " WHERE " + BOOKS + "." + COLUMN_SERIE_ID + " = " + SERIES + "." + COLUMN_SERIE_ID +
                " AND " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND (" + BOOKS + "." + COLUMN_BOOK_TITLE + " LIKE \'%" + filtre +
                "%\' OR " + BOOKS + "." + COLUMN_BOOK_NUMBER + " LIKE \'%" + filtre +
                "%\' OR " + SERIES + "." + COLUMN_SERIE_NAME + " LIKE \'%" + filtre +
                "%\') ORDER BY " + SERIES + "." + COLUMN_SERIE_NAME + ", " + BOOKS + "." + COLUMN_BOOK_NUMBER + ", " + BOOKS + "." + COLUMN_BOOK_TITLE;
        return getListBookSerieBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM BOOKS
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND BOOKS.EDITOR_ID = editor_id
    // AND BOOKS.SERIE_ID IS NULL
    /* ------------------------------------------------------------------------------------------ */
    public List<BookBean> getBooksListByEditorIdNoSerie(int editor_id){
        String query = "SELECT * FROM " + BOOKS +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND " + BOOKS + "." + COLUMN_EDITOR_ID + " = \"" + editor_id +
                "\" AND " + BOOKS + "." + COLUMN_SERIE_ID + " IS NULL";
        return getListBookBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT * FROM BOOKS
    // INNER JOIN WRITING ON BOOKS.BOOK_ID = WRITING.BOOK_ID
    // INNER JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND WRITING.AUTEUR_ID = author_id
    // AND BOOKS.SERIE_ID IS NULL
    /* ------------------------------------------------------------------------------------------ */
    public List<BookBean> getBooksListByAuthorIdNoSerie(int author_id){
        String query = "SELECT * FROM " + BOOKS +
                " INNER JOIN " + WRITING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + WRITING + "." + COLUMN_BOOK_ID +
                " INNER JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND " + WRITING + "." + COLUMN_AUTHOR_ID + " = \"" + author_id +
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
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND BOOKS.SERIE_ID = serie_id
    /* ------------------------------------------------------------------------------------------ */
    public List<BookBean> getBooksListBySerieId(int serie_id){
        String query = "SELECT * FROM " + BOOKS +
                " JOIN " + DETAINING +
                " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND " + BOOKS + "." + COLUMN_SERIE_ID + " = \"" + serie_id + "\"";
        return getListBookBean(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT COUNT (*) FROM BOOKS
    // JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND BOOKS.SERIE_ID = serie_id
    /* ------------------------------------------------------------------------------------------ */
    public int getNbBooksBySerieId(int serie_id){
        String query = "SELECT * FROM " + BOOKS +
                " JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND " + BOOKS + "." + COLUMN_SERIE_ID + " = \"" + serie_id + "\"";
        return getNbBooks(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT COUNT (*) FROM BOOKS
    // JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // JOIN WRITING ON BOOKS.BOOK_ID = WRITING.BOOK_ID
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND WRITING.AUTHOR_ID = author_id
    /* ------------------------------------------------------------------------------------------ */
    public int getNbBooksByAuthorId(int author_id){
        String query = "SELECT * FROM " + BOOKS +
                " JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " JOIN " + WRITING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + WRITING + "." + COLUMN_AUTHOR_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND " + WRITING + "." + COLUMN_AUTHOR_ID + " = \"" + author_id + "\"";
        return getNbBooks(query);
    }

    /* ------------------------------------------------------------------------------------------ */
    // SELECT COUNT (*) FROM BOOKS
    // JOIN DETAINING ON BOOKS.BOOK_ID = DETAINING.BOOK_ID
    // WHERE DETAINING.PROFILE_ID = getActiveProfile().getProfile_id()
    // AND BOOKS.EDITOR_ID = editor_id
    /* ------------------------------------------------------------------------------------------ */
    public int getNbBooksByEditorId(int editor_id) {
        String query = "SELECT * FROM " + BOOKS +
                " JOIN " + DETAINING + " ON " + BOOKS + "." + COLUMN_BOOK_ID + " = " + DETAINING + "." + COLUMN_BOOK_ID +
                " WHERE " + DETAINING + "." + COLUMN_PROFILE_ID + " = \"" + getActiveProfile().getProfile_id() +
                "\" AND " + BOOKS + "." + COLUMN_EDITOR_ID + " = \"" + editor_id + "\"";
        return getNbBooks(query);
    }
}
