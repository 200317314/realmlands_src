package com.kingdomlands.game.core.entities.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.List;
import java.util.Objects;

/**
 * @author Travis T
 */
public class Skill implements Json.Serializable {
    private String name, imageString;
    private int level;
    private double currentExp, maxExp;
    private Image image;

    public Skill(String name, int level, double currentExp, double maxExp, String imageString) {
        this.name = name;
        this.level = level;
        this.currentExp = currentExp;
        this.maxExp = maxExp;
        this.imageString = imageString;
        image = new Image(new Texture(Gdx.files.internal("skills/" + imageString)));
    }

    public Skill() {

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getCurrentExp() {
        return this.currentExp;
    }

    public void setCurrentExp(double currentExp) {
        this.currentExp = currentExp;
    }

    public double getMaxExp() {
        return this.maxExp;
    }

    public void setMaxExp(double maxExp) {
        this.maxExp = maxExp;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public static Skill getSkillFromList(List<Skill> skills, String name) {
        return skills.stream()
                .filter(Objects::nonNull)
                .filter(skill -> skill.name.equals(name))
                .findFirst()
                .orElse(null);
    }

    public static void addExpToSkillFromList(List<Skill> skills, String name, double exp) {
        skills.forEach(skill -> {
            if(skill.name.equals(name))
                skill.currentExp = skill.currentExp + exp;
        });
    }

    public static void removeExpFromSkillFromList(List<Skill> skills, String name, double exp) {
        skills.forEach(skill -> {
            if(skill.name.equals(name))
                skill.currentExp = skill.currentExp - exp;
        });
    }

    @Override
    public void write(Json json) {
        json.writeValue("name", getName());
        json.writeValue("level", getLevel());
        json.writeValue("currentExp", getCurrentExp());
        json.writeValue("maxExp", maxExp);
        json.writeValue("image", getImageString());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        setName(jsonData.getString("name"));
        setLevel(jsonData.getInt("level"));
        setLevel(jsonData.getInt("currentExp"));
        setMaxExp(jsonData.getInt("maxExp"));

        if (jsonData.has("image")) {
            setImageString(jsonData.getString("image"));

            Gdx.app.postRunnable(() -> {
                setImage(new Image(new Texture(Gdx.files.internal("skills/" + getImageString()))));
            });
        }
    }
}