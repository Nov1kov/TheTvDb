package com.example;

import java.io.Console;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {

    public static void main(String[] args){
        Schema schema = new Schema(1, "ru.novikov.novikovthetvdb.Model.DataBase.GreenDao");
        Entity note= schema.addEntity("FavoriteItem");
        note.addIdProperty().primaryKey().autoincrement();
        note.addStringProperty("owner");
        note.addLongProperty("seriesId");
        note.addStringProperty("seriesName");
        note.addStringProperty("genre");
        try {
            new DaoGenerator().generateAll(schema, "app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
