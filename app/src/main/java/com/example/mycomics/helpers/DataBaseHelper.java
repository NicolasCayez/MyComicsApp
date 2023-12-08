package com.example.mycomics.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import androidx.annotation.Nullable;

import com.example.mycomics.beans.AuteurBean;
import com.example.mycomics.beans.EditeurBean;
import com.example.mycomics.beans.ProfilActifBean;
import com.example.mycomics.beans.ProfilBean;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.beans.TomeBean;
import com.example.mycomics.beans.TomeSerieBean;
import com.example.mycomics.fragments.ReglagesFragment;

import java.util.ArrayList;
import java.util.List;

//*********************** Convention noms SQLITE : UPPERCASE et underscores

public class DataBaseHelper extends SQLiteOpenHelper {

    /* -------------------------------------- */
    // Constantes via refactor - introduce constant pour les requetes SQL
    /* -------------------------------------- */
    // Table PROFILS
    public static final String PROFILS = "PROFILS";
    public static final String COLUMN_PROFIL_ID = "PROFIL_ID";
    public static final String COLUMN_PROFIL_NOM = "PROFIL_NOM";
    // Table PROFIL_ACTIF
    public static final String PROFIL_ACTIF = "PROFIL_ACTIF";
    public static final String COLUMN_PROFIL_ACTIF_ID = "PROFIL_ACTIF_ID";
    // Table EDITEURS
    public static final String EDITEURS = "EDITEURS";
    public static final String COLUMN_EDITEUR_ID = "EDITEUR_ID";
    public static final String COLUMN_EDITEUR_NOM = "EDITEUR_NOM";
    // Table SERIES
    public static final String SERIES = "SERIES";
    public static final String COLUMN_SERIE_ID = "SERIE_ID";
    public static final String COLUMN_SERIE_NOM = "SERIE_NOM";
    // Table AUTEURS
    public static final String AUTEURS = "AUTEURS";
    public static final String COLUMN_AUTEUR_ID = "AUTEUR_ID";
    public static final String COLUMN_AUTEUR_NOM = "AUTEUR_NOM";
    public static final String COLUMN_AUTEUR_PRENOM = "AUTEUR_PRENOM";
    public static final String COLUMN_AUTEUR_PSEUDO = "AUTEUR_PSEUDO";
    // Table TOMES
    public static final String TOMES = "TOMES";
    public static final String COLUMN_TOME_ID = "TOME_ID";
    public static final String COLUMN_TOME_NUMERO = "TOME_NUMERO";
    public static final String COLUMN_TOME_TITRE = "TOME_TITRE";
    public static final String COLUMN_TOME_PRIX_EDITEUR = "TOME_PRIX_EDITEUR";
    public static final String COLUMN_TOME_VALEUR_CONNUE = "TOME_VALEUR_CONNUE";
    public static final String COLUMN_TOME_DATE_EDITION = "TOME_DATE_EDITION";
    public static final String COLUMN_TOME_ISBN = "TOME_ISBN";
    public static final String COLUMN_TOME_IMAGE = "TOME_IMAGE";
    public static final String COLUMN_TOME_DEDICACE = "TOME_DEDICACE";
    public static final String COLUMN_TOME_EDITION_SPECIALE = "TOME_EDITION_SPECIALE";
    public static final String COLUMN_TOME_EDITION_SPECIALE_LIBELLE = "TOME_EDITION_SPECIALE_LIBELLE";
    public static final String DETENIR = "DETENIR";
    public static final String ECRIRE = "ECRIRE";


    /* -------------------------------------- */
    // Constructeur - Crée la base si inexistante
    /* -------------------------------------- */
    // = CREATE DATABASE IF NOT EXISTS mycomics
    public DataBaseHelper(@Nullable Context context) {
        super(context, "mycomics.db", null, 1);
    }

    //Methode Oncreate appelée si la base de donnée est inexistante
        @Override
    public void onCreate(SQLiteDatabase db) {
        //Table PROFILS (profil_id, profil_nom)
        db.execSQL("CREATE TABLE " + PROFILS + " (" + COLUMN_PROFIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PROFIL_NOM + " TEXT NOT NULL)");
        db.execSQL("INSERT INTO " + PROFILS + " ("+ COLUMN_PROFIL_NOM+ ") " + "VALUES (\"Profil 1\")");


            //Table PROFIL_ACTIF 1 ligne initialisée (profil_actif_id, profil_id)
        db.execSQL("CREATE TABLE  " + PROFIL_ACTIF + "  (" + COLUMN_PROFIL_ACTIF_ID + " INTEGER PRIMARY KEY, " + COLUMN_PROFIL_ID + " INTEGER)");
        db.execSQL("INSERT INTO " + PROFIL_ACTIF + " ("+ COLUMN_PROFIL_ACTIF_ID + ", " + COLUMN_PROFIL_ID+ ") " + "VALUES (1,1)");

        //Table EDITEURS (editeur_id, editeur_nom)
        db.execSQL("CREATE TABLE " + EDITEURS + " (" + COLUMN_EDITEUR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EDITEUR_NOM + " TEXT NOT NULL)");

        //Table SERIES (serie_id, serie_nom)
        db.execSQL("CREATE TABLE " + SERIES + " (" + COLUMN_SERIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_SERIE_NOM + " TEXT NOT NULL)");

        //Table AUTEURS (auteur_id, auteur_pseudo, auteur_prenom, auteur_nom)
        db.execSQL("CREATE TABLE " + AUTEURS + " (" + COLUMN_AUTEUR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_AUTEUR_PSEUDO + " TEXT NOT NULL, " +
                COLUMN_AUTEUR_PRENOM + " TEXT, " + COLUMN_AUTEUR_NOM + " TEXT)");

        //Table TOMES (tome_id, tome_titre, tome_isbn, tome_numero, tome_image, tome_prix_editeur, tome_valeur_connue,
        //              tome_dedicace, tome_edition_speciale, tome_edition_speciale_libelle, serie_id)
        db.execSQL("CREATE TABLE " + TOMES + " (" +
                COLUMN_TOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TOME_TITRE + " TEXT NOT NULL, " +
                COLUMN_TOME_NUMERO + " INTEGER, " +
                COLUMN_TOME_ISBN + " TEXT, " +
                COLUMN_TOME_IMAGE + " TEXT, " +
                COLUMN_TOME_PRIX_EDITEUR + " DECIMAL(5,2), " +
                COLUMN_TOME_VALEUR_CONNUE + " DECIMAL(5,2), " +
                COLUMN_TOME_DATE_EDITION + " TEXT, " +
                COLUMN_TOME_DEDICACE + " BOOLEAN, " +
                COLUMN_TOME_EDITION_SPECIALE + " BOOLEAN, " +
                COLUMN_TOME_EDITION_SPECIALE_LIBELLE + " TEXT, " +
                COLUMN_SERIE_ID + " INTEGER," +
                COLUMN_EDITEUR_ID + " INTEGER," +
                " FOREIGN KEY(" + COLUMN_SERIE_ID + ") REFERENCES " + SERIES + "(" + COLUMN_SERIE_ID + ")," +
                " FOREIGN KEY(" + COLUMN_EDITEUR_ID + ") REFERENCES " + EDITEURS + "(" + COLUMN_EDITEUR_ID + "))");

        //Table d'association DETENIR (tome_id, profil_id)
        db.execSQL("CREATE TABLE " + DETENIR + " (" +
                COLUMN_TOME_ID + " INTEGER, " +
                COLUMN_PROFIL_ID + " INTEGER, " +
                "PRIMARY KEY (" + COLUMN_TOME_ID + ", " + COLUMN_PROFIL_ID + "), " +
                "FOREIGN KEY(" + COLUMN_TOME_ID + ") REFERENCES " + TOMES + "(" + COLUMN_TOME_ID + ")," +
                "FOREIGN KEY(" + COLUMN_PROFIL_ID + ") REFERENCES " + PROFILS + "(" + COLUMN_PROFIL_ID + "))");


        //Table d'association ECRIRE (tome_id, auteur_id)
        db.execSQL("CREATE TABLE " + ECRIRE + " (" +
                COLUMN_TOME_ID + " INTEGER, " +
                COLUMN_AUTEUR_ID + " INTEGER, " +
                "PRIMARY KEY(" + COLUMN_TOME_ID + ", " + COLUMN_AUTEUR_ID + "), " +
                "FOREIGN KEY(" + COLUMN_TOME_ID + ") REFERENCES " + TOMES + "(" + COLUMN_TOME_ID + ")," +
                "FOREIGN KEY(" + COLUMN_AUTEUR_ID + ") REFERENCES " + AUTEURS + "(" + COLUMN_AUTEUR_ID + "))");


/**************************************************************************************************/
/** Entrées de Test
/**************************************************************************************************/
            // AUTEURS
            db.execSQL("INSERT INTO AUTEURS(AUTEUR_PSEUDO) VALUES(\"Buchet\");\n"); //auteur_id 1
            db.execSQL("INSERT INTO AUTEURS(AUTEUR_PSEUDO) VALUES(\"Crisse\");\n"); //auteur_id 2
            db.execSQL("INSERT INTO AUTEURS(AUTEUR_PSEUDO) VALUES(\"Duval\");\n"); //auteur_id 3
            db.execSQL("INSERT INTO AUTEURS(AUTEUR_PSEUDO) VALUES(\"Eiichiro Oda\");\n"); //auteur_id 4
            db.execSQL("INSERT INTO AUTEURS(AUTEUR_PSEUDO) VALUES(\"Goscinny\");\n"); //auteur_id 5
            db.execSQL("INSERT INTO AUTEURS(AUTEUR_PSEUDO) VALUES(\"Gotlib\");\n"); //auteur_id 6
            db.execSQL("INSERT INTO AUTEURS(AUTEUR_PSEUDO) VALUES(\"Hugo Pratt\");\n"); //auteur_id 7
            db.execSQL("INSERT INTO AUTEURS(AUTEUR_PSEUDO) VALUES(\"Uderzo\");\n"); //auteur_id 8
            db.execSQL("INSERT INTO AUTEURS(AUTEUR_PSEUDO) VALUES(\"Arleston\");\n"); //auteur_id 9
            db.execSQL("INSERT INTO AUTEURS(AUTEUR_PSEUDO) VALUES(\"Mourier\");\n"); //auteur_id 10
            db.execSQL("INSERT INTO AUTEURS(AUTEUR_PSEUDO) VALUES(\"Tarquin\");\n"); //auteur_id 11
            db.execSQL("INSERT INTO AUTEURS(AUTEUR_PSEUDO) VALUES(\"Vatine\");\n"); //auteur_id 12
            db.execSQL("INSERT INTO AUTEURS(AUTEUR_PSEUDO) VALUES(\"Color Twins\");\n"); //auteur_id 13
            db.execSQL("INSERT INTO AUTEURS(AUTEUR_PSEUDO) VALUES(\"Morvan\");\n"); //auteur_id 14
            db.execSQL("INSERT INTO AUTEURS(AUTEUR_PSEUDO) VALUES(\"Keramidas\");\n"); //auteur_id 15


            // EDITEURS
            db.execSQL("INSERT INTO EDITEURS(EDITEUR_NOM) VALUES(\"Casterman\");\n"); //editeur_id 1
            db.execSQL("INSERT INTO EDITEURS(EDITEUR_NOM) VALUES(\"Dargaud\");\n"); //editeur_id 2
            db.execSQL("INSERT INTO EDITEURS(EDITEUR_NOM) VALUES(\"Delcourt\");\n"); //editeur_id 3
            db.execSQL("INSERT INTO EDITEURS(EDITEUR_NOM) VALUES(\"Glenat\");\n"); //editeur_id 4
            db.execSQL("INSERT INTO EDITEURS(EDITEUR_NOM) VALUES(\"Pilote\");\n"); //editeur_id 5
            db.execSQL("INSERT INTO EDITEURS(EDITEUR_NOM) VALUES(\"Soleil Editions\");\n"); //editeur_id 6

            // SERIES
            db.execSQL("INSERT INTO SERIES(SERIE_NOM) VALUES(\"Asterix\");\n"); //serie_id 1
            db.execSQL("INSERT INTO SERIES(SERIE_NOM) VALUES(\"Carmen Mc Callum\");\n"); //serie_id 2
            db.execSQL("INSERT INTO SERIES(SERIE_NOM) VALUES(\"Clifton\");\n"); //serie_id 3
            db.execSQL("INSERT INTO SERIES(SERIE_NOM) VALUES(\"Kookaburra\");\n"); //serie_id 4
            db.execSQL("INSERT INTO SERIES(SERIE_NOM) VALUES(\"Lanfeust de Troy\");\n"); //serie_id 5
            db.execSQL("INSERT INTO SERIES(SERIE_NOM) VALUES(\"Lanfeust des étoiles\");\n"); //serie_id 6
            db.execSQL("INSERT INTO SERIES(SERIE_NOM) VALUES(\"Lucky Luke\");\n"); //serie_id 7
            db.execSQL("INSERT INTO SERIES(SERIE_NOM) VALUES(\"One Piece\");\n"); //serie_id 8
            db.execSQL("INSERT INTO SERIES(SERIE_NOM) VALUES(\"Sillage\");\n"); //serie_id 9
            db.execSQL("INSERT INTO SERIES(SERIE_NOM) VALUES(\"Trolls de Troy\");\n"); //serie_id 10
            db.execSQL("INSERT INTO SERIES(SERIE_NOM) VALUES(\"Les feux d'Askell\");\n"); //serie_id 11
            db.execSQL("INSERT INTO SERIES(SERIE_NOM) VALUES(\"Luuna\");\n"); //serie_id 12

            // TOMES et DETENIR id numero serie
            //Lanfeust de Troy serie 5
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"L'ivoire du Magohamoth\", 1, 5, 6);\n"); //tome_id 1
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(1, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Thanos l'incongru\", 2, 5, 6);\n"); //tome_id 2
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(2, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Castel Or-Azur\", 3, 5, 6);\n"); //tome_id 3
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(3, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Le paladin d'Eckmül\", 4, 5, 6);\n"); //tome_id 4
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(4, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Le frisson de l'haruspice\", 5, 5, 6);\n"); //tome_id 5
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(5, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Cixi impératrice\", 6, 5, 6);\n"); //tome_id 6
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(6, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Les pétaures se cachent pour mourir\", 7, 5, 6);\n"); //tome_id 7
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(7, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"La bête fabuleuse\", 8, 5, 6);\n"); //tome_id 8
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(8, 1);\n");
            //Trolls de Troy serie 10
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Histoires Trolles\", 1, 10, 6);\n"); //tome_id 9
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(9, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Le scalp du vénérable\", 2, 10, 6);\n"); //tome_id 10
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(10, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Comme un vol de pétaures\", 3, 10, 6);\n"); //tome_id 11
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(11, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Le feu occulte\", 4, 10, 6);\n"); //tome_id 12
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(12, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Les maléfices de la thaumaturge\", 5, 10, 6);\n"); //tome_id 13
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(13, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Trolls dans la brume\", 6, 10, 6);\n"); //tome_id 14
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(14, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Plume de sage\", 7, 10, 6);\n"); //tome_id 15
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(15, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Rock'n Troll attitude\", 8, 10, 6);\n"); //tome_id 16
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(16, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Les prisonniers du Darshan I\", 9, 10, 6);\n"); //tome_id 17
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(17, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Les prisonniers du Darshan II\", 10, 10, 6);\n"); //tome_id 18
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(18, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Trollympiades\", 11, 10, 6);\n"); //tome_id 19
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(19, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Sang famille I\", 12, 10, 6);\n"); //tome_id 20
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(20, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Boules de poils I\", 15, 10, 6);\n"); //tome_id 21
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(21, 1);\n");
            //Les feux d'askell série 11
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"L'onguent admirable\", 1, 11, 6);\n"); //tome_id 22
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(22, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Retour à Vocable\", 2, 11, 6);\n"); //tome_id 23
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(23, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Corail sanglant\", 3, 11, 6);\n"); //tome_id 24
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(24, 1);\n");
            //Sillage série 9
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"A feu et à cendres\", 1, 9, 3);\n"); //tome_id 25
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(25, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Collection privée\", 2, 9, 3);\n"); //tome_id 26
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(26, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Engrenages\", 3, 9, 3);\n"); //tome_id 27
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(27, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Le signe des démons\", 4, 9, 3);\n"); //tome_id 28
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(28, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"FToRoSs\", 5, 9, 3);\n"); //tome_id 29
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(29, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Artifices\", 6, 9, 3);\n"); //tome_id 30
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(30, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Q.H.I.\", 7, 9, 3);\n"); //tome_id 31
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(31, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Nature humaine\", 8, 9, 3);\n"); //tome_id 32
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(32, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Infiltrations\", 9, 9, 3);\n"); //tome_id 33
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(33, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, TOME_DEDICACE, SERIE_ID, EDITEUR_ID) VALUES(\"Retour de flammes - Edition spéciale\", 10, true, 9, 3);\n"); //tome_id 34
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(34, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Monde flottant\", 11, 9, 3);\n"); //tome_id 35
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(35, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Zone franche\", 12, 9, 3);\n"); //tome_id 36
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(36, 1);\n");
            //Luuna série 12
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"La nuit des totems\", 1, 12, 6);\n"); //tome_id 37
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(37, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Le crépuscule du Lynx\", 2, 12, 6);\n"); //tome_id 38
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(38, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Dans les traces d'Oh-mah-ah\", 3, 12, 6);\n"); //tome_id 39
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(39, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Pok-ta-pok\", 4, 12, 6);\n"); //tome_id 40
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(40, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"Le cercle des miroirs\", 5, 12, 6);\n"); //tome_id 41
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(41, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"La reine des loups\", 6, 12, 6);\n"); //tome_id 42
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(42, 1);\n");
            db.execSQL("INSERT INTO TOMES(TOME_TITRE, TOME_NUMERO, SERIE_ID, EDITEUR_ID) VALUES(\"La source du temps\", 7, 12, 6);\n"); //tome_id 43
            db.execSQL("INSERT INTO DETENIR(TOME_ID, PROFIL_ID) VALUES(43, 1);\n");



            // ECRIRE
            //Lanfeust de Troy Arleston 9 Tarquin 11
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(1,9);\n"); //1
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(1,11);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(2, 9);\n"); //2
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(2, 11);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(3, 9);\n"); //3
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(3, 11);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(4, 9);\n"); //4
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(4, 11);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(5, 9);\n"); //5
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(5, 11);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(6, 9);\n"); //6
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(6, 11);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(7, 9);\n"); //7
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(7, 11);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(8, 9);\n"); //8
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(8, 11);\n");
            //Trolls de Troy Arleston 9 Mourier 10
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(9, 9);\n"); //1
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(9, 10);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(10, 9);\n"); //2
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(10, 10);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(11, 9);\n"); //3
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(11, 10);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(12, 9);\n"); //4
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(12, 10);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(13, 9);\n"); //5
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(13, 10);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(14, 9);\n"); //6
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(14, 10);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(15, 9);\n"); //7
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(15, 10);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(16, 9);\n"); //8
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(16, 10);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(17, 9);\n"); //9
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(17, 10);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(18, 9);\n"); //10
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(18, 10);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(19, 9);\n"); //11
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(19, 10);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(20, 9);\n"); //12
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(20, 10);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(21, 9);\n"); //15
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(21, 10);\n");
            //Les feux d'Askell Arleston 9 Mourier 10
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(22, 9);\n"); //1
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(22, 10);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(23, 9);\n"); //2
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(23, 10);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(24, 9);\n"); //3
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(24, 10);\n");
            //Sillage Buchet 1 Morvan 14 Color Twins 13
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(25, 1);\n"); //1
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(25, 14);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(26, 1);\n"); //2
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(26, 14);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(26, 13);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(27, 1);\n"); //3
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(27, 14);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(28, 1);\n"); //4
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(28, 14);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(29, 1);\n"); //5
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(29, 14);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(30, 1);\n"); //6
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(30, 14);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(31, 1);\n"); //7
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(31, 14);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(32, 1);\n"); //8
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(32, 14);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(33, 1);\n"); //9
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(33, 14);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(34, 1);\n"); //10
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(34, 14);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(35, 1);\n"); //11
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(35, 14);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(36, 1);\n"); //12
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(36, 14);\n");
            // Luuna Crisse 2 Keramidas 15
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(37, 2);\n"); //1
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(37, 15);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(38, 2);\n"); //2
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(38, 15);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(39, 2);\n"); //3
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(39, 15);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(40, 2);\n"); //4
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(40, 15);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(41, 2);\n"); //5
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(41, 15);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(42, 2);\n"); //6
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(42, 15);\n");
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(43, 2);\n"); //7
            db.execSQL("INSERT INTO ECRIRE(TOME_ID, AUTEUR_ID) VALUES(43, 15);\n");






        }


    //onUpgrade appelé si la version de la BDD change. Pour éviter conflits
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + PROFILS); // pour chaque table
        db.execSQL("DROP TABLE " + PROFIL_ACTIF); // pour chaque table
        db.execSQL("DROP TABLE " + EDITEURS); // pour chaque table
        db.execSQL("DROP TABLE " + SERIES); // pour chaque table
        db.execSQL("DROP TABLE " + AUTEURS); // pour chaque table
        db.execSQL("DROP TABLE " + TOMES); // pour chaque table
        db.execSQL("DROP TABLE " + DETENIR); // pour chaque table
        db.execSQL("DROP TABLE " + ECRIRE); // pour chaque table
        onCreate(db);
    }


/**************************************************************************************************/
/** REQUETES UPDATE
/**************************************************************************************************/

    /* -------------------------------------- */
    // UPDATE PROFIL_ACTIF SET PROFIL_ID = profil_id
    /* -------------------------------------- */
    public boolean updateProfilActif(DataBaseHelper dataBaseHelper, long nouvelId){
        SQLiteDatabase db = this.getWritableDatabase(); // accès lecture BDD
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PROFIL_ID, nouvelId);
        long update =  db.update(PROFIL_ACTIF,cv,null,null);
        if (update == -1){ // Test si insertion ok
            System.out.println("update request : non");
            return false;
        } else {
            System.out.println("update request : OK");
            System.out.println("Id profil actif: " + dataBaseHelper.selectProfilActif().toString());
            return true;
        }
    }

    /* -------------------------------------- */
    // UPDATE PROFIL SET PROFIL_NOM = profil_nom WHERE PROFIL_ID = profil_id
    /* -------------------------------------- */
    public boolean updateProfil(DataBaseHelper dataBaseHelper, ProfilBean profil){
        SQLiteDatabase db = this.getWritableDatabase(); // accès lecture BDD
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PROFIL_NOM, profil.getProfil_nom());
        long update =  db.update(PROFILS,cv,"PROFIL_ID = ?",new String[]{String.valueOf(profil.getProfil_id())});
        if (update == -1){ // Test si insertion ok
            System.out.println("update request : non");
            return false;
        } else {
            System.out.println("update request : OK");
            System.out.println("Id profil actif: " + dataBaseHelper.selectProfilActif().toString());
            return true;
        }
    }

    /* -------------------------------------- */
    // UPDATE TOME WHERE TOME_ID = tome_id
    /* -------------------------------------- */
    public boolean updateTome(DataBaseHelper dataBaseHelper, TomeBean tome){
        SQLiteDatabase db = this.getWritableDatabase(); // accès lecture BDD
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TOME_TITRE, tome.getTome_titre());
        cv.put(COLUMN_TOME_NUMERO, tome.getTome_numero());
        cv.put(COLUMN_TOME_ISBN, tome.getTome_isbn());
        cv.put(COLUMN_TOME_PRIX_EDITEUR, tome.getTome_prix_editeur());
        cv.put(COLUMN_TOME_VALEUR_CONNUE, tome.getTome_valeur_connue());
        cv.put(COLUMN_TOME_DATE_EDITION, tome.getTome_date_edition());
        cv.put(COLUMN_TOME_IMAGE, tome.getTome_image());
        cv.put(COLUMN_TOME_DEDICACE, tome.isTome_dedicace());
        cv.put(COLUMN_TOME_EDITION_SPECIALE, tome.isTome_edition_speciale());
        cv.put(COLUMN_TOME_EDITION_SPECIALE_LIBELLE, tome.getTome_edition_speciale_libelle());
        cv.put(COLUMN_SERIE_ID, tome.getSerie_id());
        long update =  db.update(TOMES,cv,"TOME_ID = ?",new String[]{String.valueOf(tome.getTome_id())});
        if (update == -1){ // Test si insertion ok
            System.out.println("update request : non");
            return false;
        } else {
            System.out.println("update request : OK");
            System.out.println("Id profil actif: " + dataBaseHelper.selectProfilActif().toString());
            return true;
        }
    }

    /* -------------------------------------- */
    // UPDATE TOMES SET SERIE_ID = serie_id WHERE TOME_ID = tome_id
    /* -------------------------------------- */
    public boolean updateSerieDuTome(DataBaseHelper dataBaseHelper, SerieBean serieBean, Integer tome_id){
        SQLiteDatabase db = this.getWritableDatabase(); // accès lecture BDD
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SERIE_ID, serieBean.getSerie_id());
        long update = db.update(TOMES,cv,"TOME_ID = ?",new String[]{String.valueOf(tome_id)});
        if (update == -1){ // Test si insertion ok
            System.out.println("update request : non");
            return false;
        } else {
            System.out.println("update request : OK");
            return true;
        }
    }

    /* -------------------------------------- */
    // UPDATE TOMES SET EDITEUR_ID = editeur_id WHERE TOME_ID = tome_id
    /* -------------------------------------- */
    public boolean updateEditeurDuTome(DataBaseHelper dataBaseHelper, EditeurBean editeurBean, Integer tome_id){
        SQLiteDatabase db = this.getWritableDatabase(); // accès lecture BDD
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EDITEUR_ID, editeurBean.getEditeur_id());
        long update = db.update(TOMES,cv,"TOME_ID = ?",new String[]{String.valueOf(tome_id)});
        if (update == -1){ // Test si insertion ok
            System.out.println("update request : non");
            return false;
        } else {
            System.out.println("update request : OK");
            return true;
        }
    }

/**************************************************************************************************/
/** REQUETES INSERT INTO
/**************************************************************************************************/

    /* -------------------------------------- */
    // INSERT INTO PROFILS (profil_nom)
    /* -------------------------------------- */
    public boolean insertIntoProfils(ProfilBean profilBean){
        SQLiteDatabase db = this.getWritableDatabase(); // accès écriture BDD
        ContentValues cv = new ContentValues(); //stocke des paires clé-valeur
        cv.put(COLUMN_PROFIL_NOM, profilBean.getProfil_nom()); //ajout dans la pile
        long insert = db.insert(PROFILS, null, cv); //insertion en base
        if (insert == -1){ // Test si insertion ok
            return false;
        } else {
            return true;
        }
    }

    /* -------------------------------------- */
    // INSERT INTO EDITEURS (editeur_nom)
    /* -------------------------------------- */
    public boolean insertIntoEditeurs(EditeurBean editeurBean){
        SQLiteDatabase db = this.getWritableDatabase(); // accès écriture BDD
        ContentValues cv = new ContentValues(); //stocke des paires clé-valeur
        cv.put(COLUMN_EDITEUR_NOM, editeurBean.getEditeur_nom()); //ajout dans la pile
        long insert = db.insert(EDITEURS, null, cv); //insertion en base
        if (insert == -1){ // Test si insertion ok
            return false;
        } else {
            return true;
        }
    }

    /* -------------------------------------- */
    // INSERT INTO AUTEURS (auteur_pseudo)
    /* -------------------------------------- */
    public boolean insertIntoAuteurs(AuteurBean auteurBean){
        SQLiteDatabase db = this.getWritableDatabase(); // accès écriture BDD
        ContentValues cv = new ContentValues(); //stocke des paires clé-valeur
        cv.put(COLUMN_AUTEUR_PSEUDO, auteurBean.getAuteur_pseudo()); //ajout dans la pile
        long insert = db.insert(AUTEURS, null, cv); //insertion en base
        if (insert == -1){ // Test si insertion ok
            return false;
        } else {
            return true;
        }
    }

    /* -------------------------------------- */
    // INSERT INTO ECRIRE (tome_id, auteur_id)
    /* -------------------------------------- */
    public boolean insertIntoEcrire(int tome_id, int auteur_id){
        SQLiteDatabase db = this.getWritableDatabase(); // accès écriture BDD
        ContentValues cv = new ContentValues(); //stocke des paires clé-valeur
        cv.put(COLUMN_TOME_ID, tome_id); //ajout dans la pile
        cv.put(COLUMN_AUTEUR_ID, auteur_id); //ajout dans la pile
        long insert = db.insert(ECRIRE, null, cv); //insertion en base
        if (insert == -1){ // Test si insertion ok
            return false;
        } else {
            return true;
        }
    }

    /* -------------------------------------- */
    // INSERT INTO SERIES (serie_nom)
    /* -------------------------------------- */
    public boolean insertIntoSeries(SerieBean serieBean){
        SQLiteDatabase db = this.getWritableDatabase(); // accès écriture BDD
        ContentValues cv = new ContentValues(); //stocke des paires clé-valeur
        cv.put(COLUMN_SERIE_NOM, serieBean.getSerie_nom()); //ajout dans la pile
        long insert = db.insert(SERIES, null, cv); //insertion en base
        if (insert == -1){ // Test si insertion ok
            return false;
        } else {
            return true;
        }
    }

    /* -------------------------------------- */
    // INSERT INTO TOMES (tome_titre)
    /* -------------------------------------- */
    public boolean insertIntoTomes(TomeBean tomeBean){
        //Table Tomes
        SQLiteDatabase db = this.getWritableDatabase(); // accès écriture BDD
        ContentValues cv = new ContentValues(); //stocke des paires clé-valeur
        cv.put(COLUMN_TOME_TITRE, tomeBean.getTome_titre()); //ajout dans la pile
        long insert = db.insert(TOMES, null, cv); //insertion en base
        if (insert == -1){ // Test si insertion ok
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    /* -------------------------------------- */
    // INSERT INTO DETENIR (tome_id, profil_id)
    // a faire sur le profil actif
    /* -------------------------------------- */
    public boolean insertIntoDetenir(TomeBean tomeBean){
        int profil_id = this.selectProfilActif().getProfil_id();
        SQLiteDatabase db = this.getWritableDatabase(); // accès écriture BDD
        ContentValues cv = new ContentValues(); //stocke des paires clé-valeur
        cv.put(COLUMN_TOME_ID, tomeBean.getTome_id()); //ajout dans la pile
        cv.put(COLUMN_PROFIL_ID, profil_id);
        long insert = db.insert(DETENIR, null, cv); //insertion en base
        if (insert == -1){ // Test si insertion ok
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

/**************************************************************************************************/
/** REQUETES DELETE & UPDATE (suppression)
 /**************************************************************************************************/

    /* -------------------------------------- */
    // UPDATE TOMES SET SERIE_ID = NULL WHERE TOME_ID = tome_id
    /* -------------------------------------- */
    public boolean deleteSerieDuTome(DataBaseHelper dataBaseHelper, Integer tome_id){
    SQLiteDatabase db = this.getWritableDatabase(); // accès lecture BDD
    ContentValues cv = new ContentValues();
    cv.putNull(COLUMN_SERIE_ID);
    long update = db.update(TOMES,cv,"TOME_ID = ?",new String[]{String.valueOf(tome_id)});
    if (update == -1){ // Test si insertion ok
        System.out.println("update request : non");
        return false;
    } else {
        System.out.println("update request : OK");
        return true;
    }
}

    /* -------------------------------------- */
    // UPDATE TOMES SET EDITEUR_ID = NULL WHERE TOME_ID = tome_id
    /* -------------------------------------- */
    public boolean deleteEditeurDuTome(DataBaseHelper dataBaseHelper, Integer tome_id){
        SQLiteDatabase db = this.getWritableDatabase(); // accès lecture BDD
        ContentValues cv = new ContentValues();
        cv.putNull(COLUMN_EDITEUR_ID);
        long update = db.update(TOMES,cv,"TOME_ID = ?",new String[]{String.valueOf(tome_id)});
        if (update == -1){ // Test si insertion ok
            System.out.println("update request : non");
            return false;
        } else {
            System.out.println("update request : OK");
            return true;
        }
    }

    /* -------------------------------------- */
    // DELETE * FROM ECRIRE
    // WHERE TOME_ID = tome_id
    // AND AUTEUR_ID = auteur_id
    //
    /* -------------------------------------- */
    public boolean deleteAuteurDuTome(DataBaseHelper dataBaseHelper, Integer auteur_id, Integer tome_id){
        SQLiteDatabase db = this.getWritableDatabase(); // accès écriture BDD

        String whereClause = COLUMN_TOME_ID + " = ? AND " + COLUMN_AUTEUR_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(tome_id), String.valueOf(auteur_id)};
        int numRowsDeleted = db.delete(ECRIRE, whereClause, whereArgs);
        db.close();
        if (numRowsDeleted >= 1) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    /* -------------------------------------- */
    // DELETE * FROM TOMES
    // WHERE TOME_ID = tome_id
    /* -------------------------------------- */
    public boolean deleteTome(DataBaseHelper dataBaseHelper, Integer tome_id){
        SQLiteDatabase db = this.getWritableDatabase(); // accès écriture BDD
        String whereClause = COLUMN_TOME_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(tome_id)};
        int numRowsDeletedEcrire = db.delete(ECRIRE, whereClause, whereArgs);
        whereClause = COLUMN_TOME_ID + " = ?";
        whereArgs = new String[]{String.valueOf(tome_id)};
        int numRowsDeletedDetenir = db.delete(DETENIR, whereClause, whereArgs);
        whereClause = COLUMN_TOME_ID + " = ?";
        whereArgs = new String[]{String.valueOf(tome_id)};
        int numRowsDeletedTomes = db.delete(TOMES, whereClause, whereArgs);
        db.close();
        if (numRowsDeletedEcrire >= 1 && numRowsDeletedDetenir >= 1 && numRowsDeletedTomes >= 1) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }


/**************************************************************************************************/
/** Méthodes "get..." pour les requêtes SELECT hors profil et profil actif
/**************************************************************************************************/
    /* -------------------------------------- */
    // get : list<List<EditeurBean>
    /* -------------------------------------- */
    public List<EditeurBean> getListEditeurBean(String requete){
        List<EditeurBean> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(); // accès lecture BDD
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        if (cursor.moveToFirst()) { // true si il y a des résultats
            do { // pour chaque tuple
                Integer editeur_id = cursor.getInt(0);
                String editeur_nom = cursor.getString(1);
                EditeurBean editeurBean = new EditeurBean(editeur_id, editeur_nom);
                returnList.add(editeurBean);
            } while (cursor.moveToNext()); //on passe au tuple suivant
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        db.close();
        return returnList;
    }

    /* -------------------------------------- */
    // get : EditeurBean
    /* -------------------------------------- */
    public EditeurBean getEditeurBean(String requete){
        EditeurBean editeurBean = new EditeurBean();
        SQLiteDatabase db = this.getReadableDatabase(); // accès lecture BDD
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        if (cursor.moveToFirst()) { // true si il y a des résultats
            do { // pour chaque tuple
                Integer editeur_id = cursor.getInt(0);
                String editeur_nom = cursor.getString(1);
                editeurBean = new EditeurBean(editeur_id, editeur_nom);
            } while (cursor.moveToNext()); //on passe au tuple suivant
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        db.close();
        return editeurBean;
    }

    /* -------------------------------------- */
    // get : list<AuteurBean>
    /* -------------------------------------- */
    public List<AuteurBean> getListAuteurBean(String requete){
        List<AuteurBean> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(); // accès lecture BDD
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        if (cursor.moveToFirst()) { // true si il y a des résultats
            do { // pour chaque tuple
                Integer auteur_id = cursor.getInt(0);
                String auteur_pseudo = cursor.getString(1);
                String auteur_prenom = cursor.getString(2);
                String auteur_nom = cursor.getString(3);
                AuteurBean auteurBean = new AuteurBean(auteur_id, auteur_nom, auteur_prenom, auteur_pseudo);
                returnList.add(auteurBean);
            } while (cursor.moveToNext()); //on passe au tuple suivant
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        db.close();
        return returnList;
    }

    /* -------------------------------------- */
    // get : AuteurBean
    /* -------------------------------------- */
    public AuteurBean getAuteurBean(String requete){
        AuteurBean auteurBean = new AuteurBean();
        SQLiteDatabase db = this.getReadableDatabase(); // accès lecture BDD
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        if (cursor.moveToFirst()) { // true si il y a des résultats
            do { // pour chaque tuple
                Integer auteur_id = cursor.getInt(0);
                String auteur_pseudo = cursor.getString(1);
                String auteur_prenom = cursor.getString(2);
                String auteur_nom = cursor.getString(3);
                auteurBean = new AuteurBean(auteur_id, auteur_pseudo, auteur_prenom, auteur_nom);
            } while (cursor.moveToNext()); //on passe au tuple suivant
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        db.close();
        return auteurBean;
    }

    /* -------------------------------------- */
    // get : list<SerieBean>
    /* -------------------------------------- */
    public List<SerieBean> getListSerieBean(String requete){
        List<SerieBean> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(); // accès lecture BDD
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        if (cursor.moveToFirst()) { // true si il y a des résultats
            do { // pour chaque tuple
                Integer serie_id = cursor.getInt(0);
                String serie_nom = cursor.getString(1);
                SerieBean serieBean = new SerieBean(serie_id, serie_nom);
                returnList.add(serieBean);
            } while (cursor.moveToNext()); //on passe au tuple suivant
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        db.close();
        return returnList;
    }

    /* -------------------------------------- */
    // get : SerieBean
    /* -------------------------------------- */
    public SerieBean getSerieBean(String requete){
        SerieBean serieBean = new SerieBean();
        SQLiteDatabase db = this.getReadableDatabase(); // accès lecture BDD
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        if (cursor.moveToFirst()) { // true si il y a des résultats
            do { // pour chaque tuple
                Integer serie_id = cursor.getInt(0);
                String serie_nom = cursor.getString(1);
                serieBean = new SerieBean(serie_id, serie_nom);
            } while (cursor.moveToNext()); //on passe au tuple suivant
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        db.close();
        return serieBean;
    }

    /* -------------------------------------- */
    // get : list<TomeBean>
    /* -------------------------------------- */
    public List<TomeBean> getListTomeBean(String requete){
        List<TomeBean> returnList = new ArrayList<TomeBean>();
        SQLiteDatabase db = this.getReadableDatabase(); // accès lecture BDD
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        if (cursor.moveToFirst()) { // true si il y a des résultats
            do { // pour chaque tuple
                Integer tome_id = cursor.getInt(0);
                String tome_titre = cursor.getString(1);
                Integer tome_numero = cursor.getInt(2);
                String tome_isbn = cursor.getString(3);
                double tome_prix_achat = cursor.getDouble(4);
                double tome_valeur_connue = cursor.getDouble(5);
                String tome_date_edition = cursor.getString(6);
                String tome_image = cursor.getString(7);
                boolean tome_dedicace = cursor.getInt(8) == 1 ? true: false;
                boolean tome_edition_speciale = cursor.getInt(9) == 1 ? true: false;
                String tome_edition_speciale_libelle = cursor.getString(10);
                Integer serie_id = cursor.getInt(11);
                Integer editeur_id = cursor.getInt(12);
                TomeBean tomeBean = new TomeBean(tome_id, tome_titre, tome_numero, tome_isbn, tome_image,
                        tome_prix_achat, tome_valeur_connue, tome_date_edition, tome_dedicace,
                        tome_edition_speciale, tome_edition_speciale_libelle, serie_id, editeur_id);
                returnList.add(tomeBean);
            } while (cursor.moveToNext()); //on passe au tuple suivant
        }
        else {
            // pas de résultats on ne fait rien
        }
        // fermeture base de données et cursor
        cursor.close();
        db.close();
        return returnList; // résultat de la requête
    }

    /* -------------------------------------- */
    // get : TomeBean
    /* -------------------------------------- */
    public TomeBean getTomeBean(String requete){
        TomeBean tomeBean = new TomeBean();
        SQLiteDatabase db = this.getReadableDatabase(); // accès lecture BDD
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        if (cursor.moveToFirst()) { // true si il y a des résultats
            do { // pour chaque tuple
                Integer tome_id = cursor.getInt(0);
                String tome_titre = cursor.getString(1);
                Integer tome_numero = cursor.getInt(2);
                String tome_isbn = cursor.getString(3);
                double tome_prix_achat = cursor.getDouble(4);
                double tome_valeur_connue = cursor.getDouble(5);
                String tome_date_edition = cursor.getString(6);
                String tome_image = cursor.getString(7);
                boolean tome_dedicace = cursor.getInt(8) == 1 ? true: false;
                boolean tome_edition_speciale = cursor.getInt(9) == 1 ? true: false;
                String tome_edition_speciale_libelle = cursor.getString(10);
                Integer serie_id = cursor.getInt(11);
                Integer editeur_id = cursor.getInt(12);
                tomeBean = new TomeBean(tome_id, tome_titre, tome_numero, tome_isbn,  tome_image, tome_prix_achat, tome_valeur_connue, tome_date_edition, tome_dedicace, tome_edition_speciale, tome_edition_speciale_libelle, serie_id, editeur_id);
            } while (cursor.moveToNext()); //on passe au tuple suivant
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        db.close();
        return tomeBean;
    }


    /* -------------------------------------- */
    // get : list<TomeSerieBean>
    /* -------------------------------------- */
    public List<TomeSerieBean> getListTomeSerieBean(String requete){
        List<TomeSerieBean> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(); // accès lecture BDD
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        if (cursor.moveToFirst()) { // true si il y a des résultats
            do { // pour chaque tuple
                Integer tome_id = cursor.getInt(0);
                String tome_titre = cursor.getString(1);
                int tome_numero = cursor.getInt(2);
                Integer serie_id = cursor.getInt(11);
                Integer editeur_id = cursor.getInt(12);
                String serie_nom = cursor.getString(13);
                TomeSerieBean tomeSerieBean = new TomeSerieBean(tome_id, tome_titre, tome_numero, null,
                        null, 0, 0, null, false,
                        false, null, serie_id, editeur_id, serie_nom);
                returnList.add(tomeSerieBean);
            } while (cursor.moveToNext()); //on passe au tuple suivant
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        db.close();
        return returnList;
    }




/**************************************************************************************************/
/** REQUETES SELECT
/**************************************************************************************************/

    /* -------------------------------------- */
    // SELECT * FROM PROFILS
    /* -------------------------------------- */
    public List<ProfilBean> listeProfils(){
        List<ProfilBean> returnList = new ArrayList<>();
        String requete = "SELECT * FROM " + PROFILS +
                " ORDER BY " + PROFILS + "." + COLUMN_PROFIL_NOM;
        SQLiteDatabase db = this.getReadableDatabase(); // accès lecture BDD
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        if (cursor.moveToFirst()) { // true si il y a des résultats
            do { // pour chaque tuple
                int profil_id = cursor.getInt(0);
                String profil_nom = cursor.getString(1);
                ProfilBean profilBean = new ProfilBean(profil_id, profil_nom);
                returnList.add(profilBean);
            } while (cursor.moveToNext()); //on passe au tuple suivant
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        db.close();
        return returnList;
    }

    /* -------------------------------------- */
    // SELECT COUNT (*) FROM PROFILS
    // WHERE COLUMN_PROFIL_NOM = profil_nom
    /* -------------------------------------- */
    public boolean verifDoublonProfil(String profil_nom){
        SQLiteDatabase db = this.getWritableDatabase(); // accès écriture BDD
        ContentValues cv = new ContentValues(); //stocke des paires clé-valeur
        String requete = "SELECT COUNT(*) FROM " + PROFILS + " WHERE " + COLUMN_PROFIL_NOM + " = \"" + profil_nom + "\"";
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        int nbResult = 0;
        if (cursor.moveToFirst()) { // true si il y a des résultats
            nbResult = cursor.getInt(0);
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        db.close();
        if (nbResult == 1) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    /* -------------------------------------- */
    // SELECT * FROM PROFIL_ACTIF, une seule entrée
    /* -------------------------------------- */
    public ProfilActifBean selectProfilActif(){
        ProfilActifBean profilActifBean = null;
//        String requete = "SELECT * FROM " + PROFIL_ACTIF + " WHERE " + COLUMN_PROFIL_ACTIF_ID + " = 0";
        String requete = "SELECT * FROM " + PROFIL_ACTIF;
        SQLiteDatabase db = this.getReadableDatabase(); // accès lecture BDD
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        if (cursor.moveToFirst()) { // true si il y a un résultat
            int profil_id = cursor.getInt(0);
            profilActifBean = new ProfilActifBean(profil_id);
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
//        db.close();
        return profilActifBean;
    }
    /* -------------------------------------- */
    // SELECT * FROM PROFILS
    // INNER JOIN PROFIL_ACTIF ON PROFILS.PROFIL_ID = PROFIL_ACTIF.PROFIL_ID
    // WHERE PROFILS.PROFIL_ID = PROFIL_ACTIF.PROFIL_ID
    /* -------------------------------------- */
    public ProfilBean selectProfilSelonProfilActif(){
        ProfilBean profilBean = null;
        String requete = "SELECT * FROM " + PROFILS +
                " INNER JOIN " + PROFIL_ACTIF + " ON " + PROFILS + "." + COLUMN_PROFIL_ID + " = " + PROFIL_ACTIF + "." + COLUMN_PROFIL_ID + " " +
                "WHERE " + PROFILS + "." + COLUMN_PROFIL_ID + " = " + PROFIL_ACTIF + "." + COLUMN_PROFIL_ID;
        SQLiteDatabase db = this.getReadableDatabase(); // accès lecture BDD
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        if (cursor.moveToFirst()) { // true si il y a des résultats
            int profil_id = cursor.getInt(0);
            String profil_nom = cursor.getString(1);
            profilBean = new ProfilBean(profil_id, profil_nom);
            System.out.println("select : " + profilBean.getProfil_nom());
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        db.close();
        return profilBean;
    }

    /* -------------------------------------- */
    // SELECT DISTINCT * FROM EDITEURS
    // GROUP BY TOMES.EDITEUR_ID
    // ORDER BY EDITEURS.EDITEUR_NOM
    /* -------------------------------------- */
    public List<EditeurBean> listeEditeurs(){
        String requete = "SELECT DISTINCT * FROM " + EDITEURS +
                " GROUP BY " + EDITEURS + "." + COLUMN_EDITEUR_ID +
                " ORDER BY " + EDITEURS + "." + COLUMN_EDITEUR_NOM;
        return getListEditeurBean(requete);
    }

    /* -------------------------------------- */
    // SELECT DISTINCT * FROM EDITEURS
    // INNER JOIN TOMES ON TOMES.EDITEUR_ID = EDITEURS.EDITEUR_ID
    // INNER JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // WHERE DETENIR.PROFIL_ID = selectProfilActif()
    // AND EDITEURS.EDITEUR_NOM LIKE '%filtre%'
    // GROUP BY TOMES.EDITEUR_ID
    // ORDER BY EDITEURS.EDITEUR_NOM
    /* -------------------------------------- */
    public List<EditeurBean> listeEditeursFiltre(String filtre){
        String requete = "SELECT DISTINCT * FROM " + EDITEURS +
                " WHERE " + EDITEURS + "." + COLUMN_EDITEUR_NOM + " LIKE \'%" + filtre +
                "%\' GROUP BY " + EDITEURS + "." + COLUMN_EDITEUR_ID +
                " ORDER BY " + EDITEURS + "." + COLUMN_EDITEUR_NOM;
        return getListEditeurBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM EDITEURS
    // WHERE EDITEUR_ID = editeur_id LIMIT 1
    /* -------------------------------------- */
    public EditeurBean selectEditeurSelonEditeurId(int editeur_id_voulu){
        String requete = "SELECT * FROM " + EDITEURS +
                " WHERE " + EDITEURS + "." + COLUMN_EDITEUR_ID + " = \"" + editeur_id_voulu + "\" LIMIT 1";
        return getEditeurBean(requete);
    }

    /* -------------------------------------- */
    // SELECT COUNT (*) FROM EDITEUR
    // WHERE COLUMN_EDITEUR_NOM = editeur_nom
    /* -------------------------------------- */
    public boolean verifDoublonEditeur(String editeur_nom){
        SQLiteDatabase db = this.getWritableDatabase(); // accès écriture BDD
        ContentValues cv = new ContentValues(); //stocke des paires clé-valeur
        String requete = "SELECT COUNT(*) FROM " + EDITEURS +
                " WHERE " + COLUMN_EDITEUR_NOM + " = \"" + editeur_nom + "\"";
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        int nbResult = 0;
        if (cursor.moveToFirst()) { // true si il y a des résultats
            nbResult = cursor.getInt(0);
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        if (nbResult == 1) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    /* -------------------------------------- */
    // SELECT * FROM EDITEURS
    // INNER JOIN TOMES ON TOMES.EDITEUR_ID = EDITEURS.EDITEUR_ID
    // INNER JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // WHERE DETENIR.PROFIL_ID = selectProfilActif()
    // AND EDITEURS.EDITEUR_NOM = editeur_nom_voulu LIMIT 1
    /* -------------------------------------- */
    public EditeurBean selectEditeurSelonEditeurNom(String editeur_nom_voulu){
        String requete = "SELECT * FROM " + EDITEURS +
        " INNER JOIN " + TOMES + " ON " + TOMES + "." + COLUMN_EDITEUR_ID + " = " + EDITEURS + "." + COLUMN_EDITEUR_ID +
        " INNER JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
        " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID  + " = \"" + selectProfilActif() +
        "\" AND " + EDITEURS + "." + COLUMN_EDITEUR_NOM + " = \"" + editeur_nom_voulu+ "\" LIMIT 1";
        return getEditeurBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM EDITEURS
    // INNER JOIN TOMES ON EDITEURS.EDITEUR_ID = TOMES.EDITEUR_ID
    // WHERE TOMES.TOME_ID = tome_id LIMIT 1
    /* -------------------------------------- */
    public EditeurBean selectEditeurSelonTomeId(int tome_id){
        String requete = "SELECT * FROM " + EDITEURS +
                " INNER JOIN " + TOMES + " ON " + EDITEURS + "." + COLUMN_EDITEUR_ID + " = " + TOMES + "." + COLUMN_EDITEUR_ID +
                " WHERE " + TOMES + "." + COLUMN_TOME_ID + " = \"" + tome_id + "\" LIMIT 1";
        return getEditeurBean(requete);
    }

    /* -------------------------------------- */
    // SELECT EDITEUR_ID FROM EDITEURS
    // WHERE EDITEUR_NOM = editeurBean.getEditeur_nom()
    // ORDER BY EDITEUR_ID DESC LIMIT 1"
    /* -------------------------------------- */
    public EditeurBean selectDernierEditeurAjoute(EditeurBean editeurBean){
        String requete = "SELECT " + COLUMN_EDITEUR_ID + " FROM " + EDITEURS +
                " WHERE " + COLUMN_EDITEUR_NOM + " = \"" + editeurBean.getEditeur_nom() +
                "\" ORDER BY " + COLUMN_EDITEUR_ID +" DESC LIMIT 1";
        return getEditeurBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM EDITEURS
    // INNER JOIN TOMES ON TOMES.EDITEUR_ID = EDITEURS.EDITEUR_ID
    // INNER JOIN ECRIRE ON ECRIRE.TOME_ID = TOMES.TOME_ID
    // JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID +
    // WHERE DETENIR.PROFIL_ID = selectProfilActif() +
    // AND ECRIRE.AUTEUR_ID = auteur_id
    // GROUP BY EDITEURS.EDITEUR_ID
    // ORDER BY EDITEURS.EDITEUR_NOM;

    /* -------------------------------------- */
    public List<EditeurBean> listeEditeursSelonAuteurId(int auteur_id){
        String requete = "SELECT * FROM " + EDITEURS +
                " INNER JOIN " + TOMES + " ON " + TOMES + "." + COLUMN_EDITEUR_ID + " = " + EDITEURS + "." + COLUMN_EDITEUR_ID +
                " INNER JOIN " + ECRIRE + " ON " + ECRIRE + "." + COLUMN_TOME_ID + " = " + TOMES + "." + COLUMN_TOME_ID +
                " INNER JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID  + " = \"" + selectProfilActif() +
                "\" AND " + ECRIRE + "." + COLUMN_AUTEUR_ID + " = \"" + auteur_id +
                "\" GROUP BY " + EDITEURS + "." + COLUMN_EDITEUR_ID +
                " ORDER BY " + EDITEURS + "." + COLUMN_EDITEUR_NOM;
        return getListEditeurBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM EDITEURS +
    // JOIN TOMES ON TOMES.EDITEUR_ID = EDITEURS.EDITEUR_ID +
    // JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID +
    // WHERE DETENIR.PROFIL_ID = selectProfilActif() +
    // AND TOMES.SERIE_ID = serie_id_voulu
    /* -------------------------------------- */
    public List<EditeurBean> listeEditeursSelonSerieId(int serie_id_voulu){
        String requete = "SELECT * FROM " + EDITEURS +
                " INNER JOIN " + TOMES + " ON " + TOMES + "." + COLUMN_EDITEUR_ID + " = " + EDITEURS + "." + COLUMN_EDITEUR_ID +
                " INNER JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID  + " = \"" + selectProfilActif() +
                "\" AND " + TOMES + "." + COLUMN_SERIE_ID + " = \"" + serie_id_voulu + "\"";
        return getListEditeurBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM AUTEURS
    // GROUP BY AUTEURS.AUTEUR_ID
    // ORDER BY AUTEURS.AUTEUR_PSEUDO
    /* -------------------------------------- */
    public List<AuteurBean> listeAuteurs(){
        String requete = "SELECT * FROM " + AUTEURS +
                " GROUP BY " + AUTEURS + "." + COLUMN_AUTEUR_ID +
                " ORDER BY " + AUTEURS + "." + COLUMN_AUTEUR_PSEUDO;
        return getListAuteurBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM AUTEURS
    // WHERE AUTEURS.AUTEURS_PSEUDO LIKE '%filtre%'
    // GROUP BY AUTEURS.AUTEUR_ID
    // ORDER BY AUTEURS.AUTEUR_PSEUDO
    /* -------------------------------------- */
    public List<AuteurBean> listeAuteursFiltre(String filtre){
        String requete = "SELECT * FROM " + AUTEURS +
                " WHERE " + AUTEURS + "." + COLUMN_AUTEUR_PSEUDO + " LIKE \'%" + filtre +
                "%\' GROUP BY " + AUTEURS + "." + COLUMN_AUTEUR_ID +
                " ORDER BY " + AUTEURS + "." + COLUMN_AUTEUR_PSEUDO;
        return getListAuteurBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM AUTEURS
    // WHERE AUTEUR_ID = auteur_id
    /* -------------------------------------- */
    public AuteurBean selectAuteurSelonAuteurId(int auteur_id_voulu){
        String requete = "SELECT * FROM " + AUTEURS +
                " WHERE " + AUTEURS + "." + COLUMN_AUTEUR_ID + " = \"" + auteur_id_voulu + "\" LIMIT 1";

        return getAuteurBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM AUTEURS
    // WHERE AUTEUR_PSEUDO = auteur_pseudo
    /* -------------------------------------- */
    public AuteurBean selectAuteurSelonAuteurPseudo(String auteur_pseudo_voulu){
        String requete = "SELECT * FROM " + AUTEURS +
                " WHERE " + AUTEURS + "." + COLUMN_AUTEUR_PSEUDO + " = \"" + auteur_pseudo_voulu + "\" LIMIT 1";
        return getAuteurBean(requete);
    }

    /* -------------------------------------- */
    // SELECT COUNT (*) FROM AUTEURS
    // WHERE COLUMN_AUTEUR_PSEUDO = auteur_pseudo
    /* -------------------------------------- */
    public boolean verifDoublonAuteur(String auteur_pseudo){
        SQLiteDatabase db = this.getWritableDatabase(); // accès écriture BDD
        String requete = "SELECT COUNT(*) FROM " + AUTEURS +
                " WHERE " + COLUMN_AUTEUR_PSEUDO + " = \"" + auteur_pseudo + "\"";
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        int nbResult = 0;
        if (cursor.moveToFirst()) { // true si il y a des résultats
            nbResult = cursor.getInt(0);
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        if (nbResult == 1) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    /* -------------------------------------- */
    // SELECT * FROM AUTEURS
    // INNER JOIN ECRIRE ON AUTEUR.AUTEUR_ID = ECRIRE.AUTEUR_ID
    // INNER JOIN TOMES ON TOMES.TOME_ID = ECRIRE.TOME_ID
    // INNER JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID +
    // WHERE DETENIR.PROFIL_ID = selectProfilActif()
    // AND ECRIRE.TOME_ID = tome_id
    // GROUP BY AUTEURS.AUTEUR_ID
    // ORDER BY AUTEURS.AUTEUR_PSEUDO
    /* -------------------------------------- */
    public List<AuteurBean> listeAuteursSelonTomeId(int tome_id){
        String requete = "SELECT * FROM " + AUTEURS +
                " INNER JOIN " + ECRIRE + " ON " + AUTEURS + "." + COLUMN_AUTEUR_ID + " = " + ECRIRE + "." + COLUMN_AUTEUR_ID +
                " INNER JOIN " + TOMES + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + ECRIRE + "." + COLUMN_TOME_ID +
                " INNER JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID  + " = \"" + selectProfilActif() +
                "\" AND " + ECRIRE + "." + COLUMN_TOME_ID + " = \"" + tome_id +
                "\" GROUP BY " + AUTEURS + "." + COLUMN_AUTEUR_ID +
                " ORDER BY " + AUTEURS + "." + COLUMN_AUTEUR_PSEUDO;
        return getListAuteurBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM AUTEURS
    // INNER JOIN ECRIRE ON AUTEUR.AUTEUR_ID = ECRIRE.AUTEUR_ID
    // INNER JOIN TOMES ON ECRIRE.TOME_ID = TOMES.TOME_ID
    // INNER JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // WHERE DETENIR.PROFIL_ID = selectProfilActif()
    // AND TOMES.EDITEUR_ID = editeur_id_voulu
    // GROUP BY AUTEURS.AUTEUR_ID
    // ORDER BY AUTEURS.AUTEUR_PSEUDO
    /* -------------------------------------- */
    public List<AuteurBean> listeAuteursSelonEditeurId(int editeur_id_voulu){
        String requete = "SELECT * FROM " + AUTEURS +
                " INNER JOIN " + ECRIRE + " ON " + AUTEURS + "." + COLUMN_AUTEUR_ID + " = " + ECRIRE + "." + COLUMN_AUTEUR_ID +
                " INNER JOIN " + TOMES + " ON " + ECRIRE + "." + COLUMN_TOME_ID + " = " + TOMES + "." + COLUMN_TOME_ID +
                " INNER JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID  + " = \"" + selectProfilActif() +
                "\" AND " + TOMES + "." + COLUMN_EDITEUR_ID + " = \"" + editeur_id_voulu +
                "\" GROUP BY " + AUTEURS + "." + COLUMN_AUTEUR_ID +
                " ORDER BY " + AUTEURS + "." + COLUMN_AUTEUR_PSEUDO;
        return getListAuteurBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM AUTEURS
    // INNER JOIN ECRIRE ON AUTEUR.AUTEUR_ID = ECRIRE.AUTEUR_ID
    // INNER JOIN TOMES ON ECRIRE.TOME_ID = TOMES.TOME_ID
    // INNER JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // WHERE DETENIR.PROFIL_ID = selectProfilActif()
    // AND TOMES.SERIE_ID = serie_id_voulu
    // GROUP BY AUTEURS.AUTEUR_ID
    // ORDER BY AUTEURS.AUTEUR_PSEUDO
    /* -------------------------------------- */
    public List<AuteurBean> listeAuteursSelonSerieId(int serie_id_voulu){
        String requete = "SELECT * FROM " + AUTEURS +
                " JOIN " + ECRIRE + " ON " + ECRIRE + "." + COLUMN_AUTEUR_ID + " = " + AUTEURS + "." + COLUMN_AUTEUR_ID +
                " JOIN " + TOMES + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + ECRIRE + "." + COLUMN_TOME_ID +
                " JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID  + " = \"" + selectProfilActif() +
                "\" AND " + TOMES + "." + COLUMN_SERIE_ID + " = \"" + serie_id_voulu +
                "\" GROUP BY " + AUTEURS + "." + COLUMN_AUTEUR_ID +
                " ORDER BY " + AUTEURS + "." + COLUMN_AUTEUR_PSEUDO;
        return getListAuteurBean(requete);
    }

    /* -------------------------------------- */
    // SELECT DISTINCT A1.* FROM AUTEURS A1
    // INNER JOIN ECRIRE E1 ON A1.AUTEUR_ID = E1.AUTEUR_ID
    // INNER JOIN ECRIRE E2 ON E1.TOME_ID = E2.TOME_ID
    // INNER JOIN AUTEURS A2 ON E2.AUTEUR_ID = A2.AUTEUR_ID
    // INNER JOIN TOMES ON E1.TOME_ID = TOMES.TOME_ID
    // INNER JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // WHERE DETENIR.PROFIL_ID = selectProfilActif()
    // AND A1.AUTEUR_ID <> A2.AUTEUR_ID
    // AND A2.AUTEUR_ID = auteur_id_voulu
    /* -------------------------------------- */
    public List<AuteurBean> listeAuteursPartenaires(int auteur_id_voulu){
        String requete = "SELECT DISTINCT A1.* FROM " + AUTEURS + " A1" +
                " INNER JOIN " + ECRIRE + " E1 ON A1." + COLUMN_AUTEUR_ID + " = E1." + COLUMN_AUTEUR_ID +
                " INNER JOIN " + ECRIRE + " E2 ON E1." + COLUMN_TOME_ID + " = E2." + COLUMN_TOME_ID +
                " INNER JOIN " + AUTEURS + " A2 ON E2." + COLUMN_AUTEUR_ID + "= A2." + COLUMN_AUTEUR_ID +
                " INNER JOIN " + TOMES + " ON E1." + COLUMN_TOME_ID + " = " + TOMES + "." + COLUMN_TOME_ID +
                " INNER JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID  + " = \"" + selectProfilActif() +
                "\" AND A1." + COLUMN_AUTEUR_ID + " <> A2." + COLUMN_AUTEUR_ID +
                " AND A2." + COLUMN_AUTEUR_ID + " =\"" + auteur_id_voulu + "\"";
        return getListAuteurBean(requete);
    }

    /* -------------------------------------- */
    // "SELECT COLUMN_TOME_ID FROM TOMES
    // WHERE COLUMN_TOME_TITRE = tomeBean.getTome_titre()
    // ORDER BY COLUMN_TOME_ID DESC LIMIT 1"
    /* -------------------------------------- */
    public AuteurBean selectDernierAuteurAjoute(AuteurBean auteurBean){
        String requete = "SELECT " + COLUMN_AUTEUR_ID + " FROM " + AUTEURS +
                " WHERE " + COLUMN_AUTEUR_PSEUDO + " = \"" + auteurBean.getAuteur_pseudo() +
                "\" ORDER BY " + COLUMN_AUTEUR_ID +" DESC LIMIT 1";
        return getAuteurBean(requete);
    }


    /* -------------------------------------- */
    // SELECT * FROM SERIES
    // GROUP BY SERIES.SERIE_ID
    // ORDER BY SERIES.SERIE_NOM
    /* -------------------------------------- */
    public List<SerieBean> listeSeries(){
        String requete = "SELECT * FROM " + SERIES +
                " GROUP BY " + SERIES + "." + COLUMN_SERIE_ID +
                " ORDER BY " + SERIES + "." + COLUMN_SERIE_NOM;
        return getListSerieBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM SERIES
    // AND SERIES.SERIE_NOM LIKE '%filtre%'
    // GROUP BY SERIES.SERIE_ID
    // ORDER BY SERIES.SERIE_NOM
    /* -------------------------------------- */
    public List<SerieBean> listeSeriesFiltre(String filtre){
        String requete = "SELECT * FROM " + SERIES +
                " WHERE " + SERIES + "." + COLUMN_SERIE_NOM + " LIKE \'%" + filtre +
                "%\' GROUP BY " + SERIES + "." + COLUMN_SERIE_ID +
                " ORDER BY " + SERIES + "." + COLUMN_SERIE_NOM;
        return getListSerieBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM SERIES
    // WHERE SERIE_ID = serie_id LIMIT 1
    /* -------------------------------------- */
    public SerieBean selectSerieSelonSerieId(int serie_id_voulu){
        String requete = "SELECT * FROM " + SERIES +
                " WHERE " + SERIES + "." + COLUMN_SERIE_ID + " = \"" + serie_id_voulu + "\" LIMIT 1";
        return getSerieBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM SERIES
    // INNER JOIN TOMES ON TOMES.SERIE_ID = SERIES.SERIE_ID
    // INNER JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // WHERE DETENIR.PROFIL_ID = selectProfilActif()
    // AND TOMES.EDITEUR_ID = editeur_id_voulu
    // GROUP BY SERIES.SERIE_ID
    // ORDER BY SERIES.SERIE_NOM
    /* -------------------------------------- */
    public List<SerieBean> listeSeriesSelonEditeurId(int editeur_id_voulu){
        String requete = "SELECT DISTINCT * FROM " + SERIES +
                " INNER JOIN " + TOMES + " ON " + SERIES + "." + COLUMN_SERIE_ID + " = " + TOMES + "." + COLUMN_SERIE_ID +
                " INNER JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID  + " = \"" + selectProfilActif() +
                "\" AND " + TOMES + "." + COLUMN_EDITEUR_ID + " = \"" + editeur_id_voulu +
                "\" GROUP BY " + SERIES + "." + COLUMN_SERIE_ID +
                " ORDER BY " + SERIES + "." + COLUMN_SERIE_NOM;
        return getListSerieBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM SERIES
    // INNER JOIN TOMES ON SERIES.SERIE_ID = TOMES.SERIE_ID
    // INNER JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // WHERE DETENIR.PROFIL_ID = selectProfilActif()
    // AND TOMES.TOME_ID = tome_id LIMIT 1
    /* -------------------------------------- */
    public SerieBean selectSerieSelonTomeId(int tome_id){
        String requete = "SELECT * FROM " + SERIES +
                " INNER JOIN " + TOMES + " ON " + SERIES + "." + COLUMN_SERIE_ID + " = " + TOMES + "." + COLUMN_SERIE_ID +
                " INNER JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID  + " = \"" + selectProfilActif() +
                "\" AND " + TOMES + "." + COLUMN_TOME_ID + " = \"" + tome_id + "\" LIMIT 1";
        return getSerieBean(requete);
    }

    /* -------------------------------------- */
    // SELECT COUNT (*) FROM SERIES
    // WHERE COLUMN_SERIE_NOM = serie_nom
    /* -------------------------------------- */
    public boolean verifDoublonSerie(String serie_nom){
        SQLiteDatabase db = this.getReadableDatabase(); // accès écriture BDD
        String requete = "SELECT COUNT(*) FROM " + SERIES +
                " WHERE " + COLUMN_SERIE_NOM + " = \"" + serie_nom + "\"";
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        int nbResult = 0;
        if (cursor.moveToFirst()) { // true si il y a des résultats
            nbResult = cursor.getInt(0);
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        if (nbResult == 1) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    /* -------------------------------------- */
    // "SELECT SERIE_ID FROM SERIES
    // WHERE SERIE_NOM = serieBean.getSerie_nom() +
    // ORDER BY SERIE_ID DESC LIMIT 1"
    /* -------------------------------------- */
    public SerieBean DerniereSerieAjoutee(SerieBean serieBean){
        String requete = "SELECT " + COLUMN_SERIE_ID + " FROM " + EDITEURS +
                " WHERE " + COLUMN_SERIE_NOM + " = \"" + serieBean.getSerie_nom() +
                "\" ORDER BY " + COLUMN_SERIE_ID +" DESC LIMIT 1";
        return getSerieBean(requete);
    }

    /* -------------------------------------- */
    // SELECT DISTINCT * FROM SERIES
    // INNER JOIN TOMES ON SERIES.SERIE_ID = TOMES.SERIE_ID
    // INNER JOIN ECRIRE ON ECRIRE.TOME_ID= TOME.TOME_ID
    // INNER JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // WHERE DETENIR.PROFIL_ID = selectProfilActif()
    // AND ECRIRE.AUTEUR_ID = auteur_id
    // GROUP BY SERIES.SERIE_ID
    // ORDER BY SERIES.SERIE_NOM
    /* -------------------------------------- */
    public List<SerieBean> listeSeriesSelonAuteurId(int auteur_id){
        String requete = "SELECT DISTINCT * FROM " + SERIES +
                " INNER JOIN " + TOMES + " ON " + SERIES + "." + COLUMN_SERIE_ID + " = " + TOMES + "." + COLUMN_SERIE_ID +
                " INNER JOIN " + ECRIRE + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + ECRIRE + "." + COLUMN_TOME_ID +
                " INNER JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID  + " = \"" + selectProfilActif() +
                "\" AND " + ECRIRE + "." + COLUMN_AUTEUR_ID + " = \"" + auteur_id +
                "\" GROUP BY " + SERIES + "." + COLUMN_SERIE_ID +
                " ORDER BY " + SERIES + "." + COLUMN_SERIE_NOM;
        return getListSerieBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM TOMES
    // INNER JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // WHERE DETENIR.PROFIL_ID = selectProfilActif()
    // GROUP BY TOMES.SERIE_ID
    // ORDER BY TOMES.TOME_NUMERO, TOMES.TOME_TITRE
    /* -------------------------------------- */
    public List<TomeBean> listeTomes(){
        String requete = "SELECT * FROM " + TOMES +
                " INNER JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID  + " = \"" + selectProfilActif() +
                "\" GROUP BY " + TOMES + "." + COLUMN_SERIE_ID +
                " ORDER BY " + TOMES + "." + COLUMN_TOME_NUMERO + ", " + TOMES + "." + COLUMN_TOME_TITRE;
        return getListTomeBean(requete); // résultat de la requête
    }

    /* -------------------------------------- */
    // SELECT * FROM TOMES
    // INNER JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // INNER JOIN PROFIL_ACTIF ON PROFIL_ACTIF.PROFIL_ID = DETENIR.PROFIL_ID
    // AND DETENIR.PROFIL_ID = selectProfilActif().getProfil_id()
    // AND TOMES.TOME_TITRE LIKE '%filtre%'
    // ORDER BY TOMES.TOME_NUMERO, TOMES.TOME_TITRE
    /* -------------------------------------- */
    public List<TomeBean> listeTomesFiltre(String filtre){
        String requete = "SELECT * FROM " + TOMES +
                " INNER JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " INNER JOIN " + PROFIL_ACTIF + " ON " + PROFIL_ACTIF + "." + COLUMN_PROFIL_ID + " = " + DETENIR + "." + COLUMN_PROFIL_ID +
                " AND " + DETENIR + "." + COLUMN_PROFIL_ID + " = \"" + selectProfilActif().getProfil_id() +
                "\" AND (" + TOMES + "." + COLUMN_TOME_TITRE + " LIKE \'%" + filtre +
                "%\' OR " + TOMES + "." + COLUMN_TOME_NUMERO + " LIKE \'%" + filtre +
                "%\') ORDER BY " + TOMES + "." + COLUMN_TOME_NUMERO + ", " + TOMES + "." + COLUMN_TOME_TITRE;
        return getListTomeBean(requete);
    }

    /* -------------------------------------- */
    // SELECT COUNT (*) FROM TOMES
    // INNER JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // WHERE DETENIR.PROFIL_ID = selectProfilActif()
    // AND COLUMN_TOME_TITRE = tome_titre
    /* -------------------------------------- */
    public boolean verifDoublonTome(String tome_titre){
        SQLiteDatabase db = this.getReadableDatabase(); // accès lecture BDD
        String requete = "SELECT COUNT(*) FROM " + TOMES +
                " INNER JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID  + " = \"" + selectProfilActif() +
                "\" AND " + COLUMN_TOME_TITRE + " = \"" + tome_titre + "\"";
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        int nbResult = 0;
        if (cursor.moveToFirst()) { // true si il y a des résultats
            nbResult = cursor.getInt(0);
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        if (nbResult == 1) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    /* -------------------------------------- */
    // SELECT DISTINCT TOMES.*, SERIE_NOM FROM TOMES, SERIES
    // LEFT JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // LEFT JOIN PROFIL_ACTIF ON PROFIL_ACTIF.PROFIL_ID = DETENIR.PROFIL_ID
    // WHERE TOMES.SERIE_ID = SERIES.SERIE_ID
    // AND DETENIR.PROFIL_ID = selectProfilActif().getProfil_id()
    // UNION
    // SELECT DISTINCT TOMES.*, NULL AS SERIE_NOM FROM TOMES
    // LEFT JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // LEFT JOIN PROFIL_ACTIF ON PROFIL_ACTIF.PROFIL_ID = DETENIR.PROFIL_ID
    // WHERE (TOMES.SERIE_ID IS NULL OR TOMES.SERIE_ID = -1)
    // AND DETENIR.PROFIL_ID = selectProfilActif().getProfil_id()
    /* -------------------------------------- */

    public List<TomeSerieBean> listeTomesEtSerieSelonProfilId(){
        String requete = "SELECT DISTINCT " + TOMES + ".*, " + COLUMN_SERIE_NOM + " FROM " + TOMES + ", " + SERIES +
                " LEFT JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " LEFT JOIN " + PROFIL_ACTIF + " ON " + PROFIL_ACTIF + "." + COLUMN_PROFIL_ID + " = " + DETENIR + "." + COLUMN_PROFIL_ID +
                " WHERE " + TOMES + "." + COLUMN_SERIE_ID + " = " + SERIES + "." + COLUMN_SERIE_ID +
                 " AND " + DETENIR + "." + COLUMN_PROFIL_ID + " = \"" + selectProfilActif().getProfil_id() +
                "\" UNION " +
                "SELECT DISTINCT " + TOMES + ".*, NULL AS " + COLUMN_SERIE_NOM + " FROM " + TOMES +
                " LEFT JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " LEFT JOIN " + PROFIL_ACTIF + " ON " + PROFIL_ACTIF + "." + COLUMN_PROFIL_ID + " = " + DETENIR + "." + COLUMN_PROFIL_ID +
                " WHERE (" + TOMES + "." + COLUMN_SERIE_ID + " IS NULL" +
                " OR " + TOMES + "." + COLUMN_SERIE_ID + " = -1" +
                ") AND " + DETENIR + "." + COLUMN_PROFIL_ID + " = \"" + selectProfilActif().getProfil_id() +
                "\" ORDER BY " + SERIES + "." + COLUMN_SERIE_NOM + ", " + TOMES + "." + COLUMN_TOME_NUMERO + ", " + TOMES + "." + COLUMN_TOME_TITRE;
        return getListTomeSerieBean(requete);
    }

    /* -------------------------------------- */
    // SELECT TOMES.*, SERIES.SERIE_NOM FROM TOMES , SERIES
    // INNER JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // INNER JOIN PROFIL_ACTIF ON PROFIL_ACTIF.PROFIL_ID = DETENIR.PROFIL_ID
    // WHERE TOMES.SERIE_ID = SERIES.SERIE_ID
    // AND DETENIR.PROFIL_ID = selectProfilActif().getProfil_id()
    // AND TOMES.TOME_TITRE LIKE '%filtre%'
    // GROUP BY TOMES.SERIE_ID
    // ORDER BY TOMES.TOME_NUMERO, TOMES.TOME_TITRE
    /* -------------------------------------- */
    public List<TomeSerieBean> listeTomesEtSerieSelonProfilIdFiltre(String filtre){
        String requete = "SELECT " + TOMES + ".*, " + SERIES + "." + COLUMN_SERIE_NOM + " FROM " + TOMES + ", " + SERIES +
                " INNER JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " INNER JOIN " + PROFIL_ACTIF + " ON " + PROFIL_ACTIF + "." + COLUMN_PROFIL_ID + " = " + DETENIR + "." + COLUMN_PROFIL_ID +
                " WHERE " + TOMES + "." + COLUMN_SERIE_ID + " = " + SERIES + "." + COLUMN_SERIE_ID + /*********************************************************************/
                " AND " + DETENIR + "." + COLUMN_PROFIL_ID + " = \"" + selectProfilActif().getProfil_id() +
                "\" AND (" + TOMES + "." + COLUMN_TOME_TITRE + " LIKE \'%" + filtre +
                "%\' OR " + TOMES + "." + COLUMN_TOME_NUMERO + " LIKE \'%" + filtre +
                "%\' OR " + SERIES + "." + COLUMN_SERIE_NOM + " LIKE \'%" + filtre +
                "%\') ORDER BY " + SERIES + "." + COLUMN_SERIE_NOM + ", " + TOMES + "." + COLUMN_TOME_NUMERO + ", " + TOMES + "." + COLUMN_TOME_TITRE;
        return getListTomeSerieBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM TOMES
    // INNER JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // INNER JOIN PROFIL_ACTIF ON PROFIL_ACTIF.PROFIL_ID = DETENIR.PROFIL_ID
    // AND DETENIR.PROFIL_ID = selectProfilActif().getProfil_id()
    // AND TOMES.EDITEUR_ID = editeur_id_voulu
    // AND TOMES.SERIE_ID IS NULL
    /* -------------------------------------- */
    public List<TomeBean> listeTomesSelonEditeurIdSansSerie(int editeur_id_voulu){
        String requete = "SELECT * FROM " + TOMES +
                " INNER JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " INNER JOIN " + PROFIL_ACTIF + " ON " + PROFIL_ACTIF + "." + COLUMN_PROFIL_ID + " = " + DETENIR + "." + COLUMN_PROFIL_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID + " = \"" + selectProfilActif().getProfil_id() +
                "\" AND " + TOMES + "." + COLUMN_EDITEUR_ID + " = \"" + editeur_id_voulu +
                "\" AND " + TOMES + "." + COLUMN_SERIE_ID + " IS NULL";
        return getListTomeBean(requete);
    }


    /* -------------------------------------- */
    // SELECT * FROM TOMES
    // INNER JOIN ECRIRE ON TOMES.TOME_ID = ECRIRE.TOME_ID
    // INNER JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // INNER JOIN PROFIL_ACTIF ON PROFIL_ACTIF.PROFIL_ID = DETENIR.PROFIL_ID
    // AND DETENIR.PROFIL_ID = selectProfilActif().getProfil_id()
    // AND ECRIRE.AUTEUR_ID = auteur_id
    // AND TOMES.SERIE_ID IS NULL
    /* -------------------------------------- */
    public List<TomeBean> listeTomesSelonAuteurIdSansSerie(int auteur_id){
        String requete = "SELECT * FROM " + TOMES +
                " INNER JOIN " + ECRIRE + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + ECRIRE + "." + COLUMN_TOME_ID +
                " INNER JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " INNER JOIN " + PROFIL_ACTIF + " ON " + PROFIL_ACTIF + "." + COLUMN_PROFIL_ID + " = " + DETENIR + "." + COLUMN_PROFIL_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID + " = \"" + selectProfilActif().getProfil_id() +
                "\" AND " + ECRIRE + "." + COLUMN_AUTEUR_ID + " = \"" + auteur_id +
                "\" AND " + TOMES + "." + COLUMN_SERIE_ID + " IS NULL";
        return getListTomeBean(requete);
    }

    /* -------------------------------------- */
    // SELECT TOME_ID FROM TOMES
    // WHERE TOME_TITRE = tomeBean.getTome_titre()
    // ORDER BY TOME_ID DESC LIMIT 1
    /* -------------------------------------- */
    public TomeBean selectDernierTomeAjoute(TomeBean tomeBean){
        String requete = "SELECT " + COLUMN_TOME_ID + " FROM " + TOMES +
                " WHERE " + COLUMN_TOME_TITRE + " = \"" + tomeBean.getTome_titre() +
                "\" ORDER BY " + COLUMN_TOME_ID +" DESC LIMIT 1";
        return getTomeBean(requete);
    }

    /* -------------------------------------- */
    // SELECT COUNT(*) FROM DETENIR
    // WHERE COLUMN_TOME_ID = selectTOMEIDFromTomesDernierAjout(tomeBean)
    /* -------------------------------------- */
    public boolean selectDetenirIsDernierTomeOk(TomeBean tomeBean){
        SQLiteDatabase db = this.getWritableDatabase(); // accès écriture BDD
        ContentValues cv = new ContentValues(); //stocke des paires clé-valeur
        String requete = "SELECT COUNT(*) FROM " + DETENIR + " WHERE " + COLUMN_TOME_ID + " = \"" + selectDernierTomeAjoute(tomeBean) + "\"";
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        int nbResult = 0;
        if (cursor.moveToFirst()) { // true si il y a des résultats
            nbResult = cursor.getInt(0);
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        db.close();
        if (nbResult == 1) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    /* -------------------------------------- */
    // SELECT COUNT (*) FROM ECRIRE
    // WHERE COLUMN_TOME_ID = tome_id
    // AND COLUMN_AUTEUR_ID = auteur_id
    /* -------------------------------------- */
    public boolean verifDoublonTomeAuteur(int tome_id, int auteur_id){
        SQLiteDatabase db = this.getWritableDatabase(); // accès écriture BDD
        ContentValues cv = new ContentValues(); //stocke des paires clé-valeur
        String requete = "SELECT COUNT(*) FROM " + ECRIRE +
                " WHERE " + COLUMN_TOME_ID + " = \"" + tome_id +
                "\" AND " + COLUMN_AUTEUR_ID + " = \"" + auteur_id + "\"";
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        int nbResult = 0;
        if (cursor.moveToFirst()) { // true si il y a des résultats
            nbResult = cursor.getInt(0);
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        if (nbResult == 1) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    /* -------------------------------------- */
    // SELECT * FROM TOMES
    // WHERE TOME_ID = tome_id LIMIT 1
    /* -------------------------------------- */
    public TomeBean selectTomeSelonTomeId(Integer tome_id_voulu){
        String requete = "SELECT * FROM " + TOMES +
                " WHERE " + TOMES + "." + COLUMN_TOME_ID + " = \"" + tome_id_voulu + "\" LIMIT 1";
        return getTomeBean(requete);
    }

    /* -------------------------------------- */
    // SELECT * FROM TOMES
    // JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // WHERE DETENIR.PROFIL_ID = selectProfilActif()
    // AND TOMES.SERIE_ID = serie_id_voulu
    /* -------------------------------------- */
    public List<TomeBean> listeTomesSelonSerieId(int serie_id_voulu){
        String requete = "SELECT * FROM " + TOMES +
                " JOIN " + DETENIR +
                " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID  + " = \"" + selectProfilActif() +
                "\" AND " + TOMES + "." + COLUMN_SERIE_ID + " = \"" + serie_id_voulu + "\"";
        return getListTomeBean(requete);
    }

    /* -------------------------------------- */
    // SELECT COUNT (*) FROM TOMES
    // JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // WHERE DETENIR.PROFIL_ID = selectProfilActif()
    // AND TOMES.SERIE_ID = serie_id_voulu
    /* -------------------------------------- */
    public int nbTomesSelonSerieId(int serie_id_voulu){
        List<TomeBean> returnList = new ArrayList<>();
        TomeBean tomeBean = new TomeBean();
        int nbTomes = 0;
        String requete = "SELECT * FROM " + TOMES +
                " JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID  + " = \"" + selectProfilActif() +
                "\" AND " + TOMES + "." + COLUMN_SERIE_ID + " = \"" + serie_id_voulu + "\"";
        SQLiteDatabase db = this.getReadableDatabase(); // accès lecture BDD
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        if (cursor.moveToFirst()) { // true si il y a des résultats
            do { // pour chaque tuple
                Integer tome_id = cursor.getInt(0);
                String tome_titre = cursor.getString(1);
                int tome_numero = cursor.getInt(2);
                String tome_isbn = cursor.getString(3);
                String tome_image = cursor.getString(4);
                double tome_prix_achat = cursor.getDouble(5);
                double tome_valeur_connue = cursor.getDouble(6);
                String tome_date_edition = cursor.getString(7);
                boolean tome_dedicace = cursor.getInt(8) == 1 ? true: false;
                boolean tome_edition_speciale = cursor.getInt(9) == 1 ? true: false;
                String tome_edition_speciale_libelle = cursor.getString(10);
                Integer serie_id = cursor.getInt(11);
                Integer editeur_id = cursor.getInt(12);
                tomeBean = new TomeBean(tome_id, tome_titre, tome_numero, tome_isbn,  tome_image, tome_prix_achat, tome_valeur_connue, tome_date_edition, tome_dedicace, tome_edition_speciale, tome_edition_speciale_libelle, serie_id, editeur_id);
                returnList.add(tomeBean);
            } while (cursor.moveToNext()); //on passe au tuple suivant
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        db.close();
        return returnList.size();
    }

    /* -------------------------------------- */
    // SELECT COUNT (*) FROM TOMES
    // JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // INNER JOIN ECRIRE ON TOMES.TOME_ID = ECRIRE.TOME_ID
    // WHERE DETENIR.PROFIL_ID = selectProfilActif()
    // AND ECRIRE.AUTEUR_ID = auteur_id_voulu
    /* -------------------------------------- */
    public int nbTomesSelonAuteurId(int auteur_id_voulu){
        List<TomeBean> returnList = new ArrayList<>();
        TomeBean tomeBean = new TomeBean();
        int nbTomes = 0;
        String requete = "SELECT * FROM " + TOMES +
                " JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " JOIN " + ECRIRE + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + ECRIRE + "." + COLUMN_AUTEUR_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID  + " = \"" + selectProfilActif() +
                "\" AND " + ECRIRE + "." + COLUMN_AUTEUR_ID + " = \"" + auteur_id_voulu + "\"";
        SQLiteDatabase db = this.getReadableDatabase(); // accès lecture BDD
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        if (cursor.moveToFirst()) { // true si il y a des résultats
            do { // pour chaque tuple
                Integer tome_id = cursor.getInt(0);
                String tome_titre = cursor.getString(1);
                int tome_numero = cursor.getInt(2);
                String tome_isbn = cursor.getString(3);
                String tome_image = cursor.getString(4);
                double tome_prix_achat = cursor.getDouble(5);
                double tome_valeur_connue = cursor.getDouble(6);
                String tome_date_edition = cursor.getString(7);
                boolean tome_dedicace = cursor.getInt(8) == 1 ? true: false;
                boolean tome_edition_speciale = cursor.getInt(9) == 1 ? true: false;
                String tome_edition_speciale_libelle = cursor.getString(10);
                Integer serie_id = cursor.getInt(11);
                Integer editeur_id = cursor.getInt(12);
                tomeBean = new TomeBean(tome_id, tome_titre, tome_numero, tome_isbn,  tome_image, tome_prix_achat, tome_valeur_connue, tome_date_edition, tome_dedicace, tome_edition_speciale, tome_edition_speciale_libelle, serie_id, editeur_id);
                returnList.add(tomeBean);
            } while (cursor.moveToNext()); //on passe au tuple suivant
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        db.close();
        return returnList.size();
    }

    /* -------------------------------------- */
    // SELECT COUNT (*) FROM TOMES
    // JOIN DETENIR ON TOMES.TOME_ID = DETENIR.TOME_ID
    // WHERE DETENIR.PROFIL_ID = selectProfilActif()
    // AND ECRIRE.AUTEUR_ID = auteur_id_voulu
    /* -------------------------------------- */
    public int nbTomesSelonEditeurId(int editeur_id_voulu){
        List<TomeBean> returnList = new ArrayList<>();
        TomeBean tomeBean = new TomeBean();
        int nbTomes = 0;
        String requete = "SELECT * FROM " + TOMES +
                " JOIN " + DETENIR + " ON " + TOMES + "." + COLUMN_TOME_ID + " = " + DETENIR + "." + COLUMN_TOME_ID +
                " WHERE " + DETENIR + "." + COLUMN_PROFIL_ID  + " = \"" + selectProfilActif() +
                "\" AND " + TOMES + "." + COLUMN_EDITEUR_ID + " = \"" + editeur_id_voulu + "\"";
        SQLiteDatabase db = this.getReadableDatabase(); // accès lecture BDD
        Cursor cursor = db.rawQuery(requete, null); //cursor = résultat de la requête
        if (cursor.moveToFirst()) { // true si il y a des résultats
            do { // pour chaque tuple
                Integer tome_id = cursor.getInt(0);
                String tome_titre = cursor.getString(1);
                int tome_numero = cursor.getInt(2);
                String tome_isbn = cursor.getString(3);
                String tome_image = cursor.getString(4);
                double tome_prix_achat = cursor.getDouble(5);
                double tome_valeur_connue = cursor.getDouble(6);
                String tome_date_edition = cursor.getString(7);
                boolean tome_dedicace = cursor.getInt(8) == 1 ? true: false;
                boolean tome_edition_speciale = cursor.getInt(9) == 1 ? true: false;
                String tome_edition_speciale_libelle = cursor.getString(10);
                Integer serie_id = cursor.getInt(11);
                Integer editeur_id = cursor.getInt(12);
                tomeBean = new TomeBean(tome_id, tome_titre, tome_numero, tome_isbn,  tome_image, tome_prix_achat, tome_valeur_connue, tome_date_edition, tome_dedicace, tome_edition_speciale, tome_edition_speciale_libelle, serie_id, editeur_id);
                returnList.add(tomeBean);
            } while (cursor.moveToNext()); //on passe au tuple suivant
        } else {
            // pas de résultats on ne fait rien
        }
        // fermeture db et cursor
        cursor.close();
        db.close();
        return returnList.size();
    }
}
