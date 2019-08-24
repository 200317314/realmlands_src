package com.kingdomlands.game.core.entities.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.List;
import java.util.Objects;

public class Attribute implements Json.Serializable {
    private String name;
    private double value;
    private Image icon;

    public Attribute(String name, double value) {
        this.name = name;
        this.value = value;
        this.icon = new Image(new Texture(Gdx.files.internal("attributes/" + name.toLowerCase() + ".png")));
    }

    public Attribute() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Image getIcon() {
        if (Objects.isNull(icon)) {
            this.icon = new Image(new Texture(Gdx.files.internal("attributes/" + getName().toLowerCase() + ".png")));
        }

        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public static Attribute getAttributeFromList(List<Attribute> list, String attribute) {
        Attribute val = null;

        for(Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals(attribute)) {
                    return a;
                }
            }
        }

        return val;
    }

    public static void setAttributeValueFromList(List<Attribute> list, String attribute, double value) {
        for(Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals(attribute)) {
                    a.value = value;
                }
            }
        }
    }

    public static double getAttributeValueFromList(List<Attribute> list, String attribute) {
        double val = -1;

        if (Objects.nonNull(list)) {
            for(Attribute a : list) {
                if (Objects.nonNull(a)) {
                    if (a.name.equals(attribute)) {
                        return a.value;
                    }
                }
            }
        }

        return val;
    }

    public static double getAttackPower(List<Attribute> list) {
        double attackPowerFound = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("AttackPower")) {
                    attackPowerFound = a.value;
                }
            }
        }

        return Methods.random((int)(attackPowerFound + getStrength(list)), (int)((attackPowerFound + getStrength(list)) * 1.4));
    }

    public double getStamina(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("Stamina")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public static double getStrength(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("Strength")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public static double getAgility(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("Agility")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public static double getIntellect(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("Intellect")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getCrit(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("Crit")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getHaste(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("Haste")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getExpBoost(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("ExpBoost")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public static double getBonusArmor(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("BonusArmor")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getLifeSteal(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("LifeSteal")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getSpellPower(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("SpellPower")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getCastingSpeed(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("CastingSpeed")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getManaRegen(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("ManaRegen")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getHpRegen(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("HpRegen")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public static double getArmor(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("Armor")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getDodge(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("Dodge")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getParry(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("Parry")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public static double getBlock(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("Block")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public static double getResist(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("Resist")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getMovementSpeed(List<Attribute> list) {
        double var = -1;
        double modifier = (100.00 + getHaste(list)) / 100.00;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("MovementSpeed")) {
                    return a.value;
                }
            }
        }

        return var * modifier;
    }

    public double getCurrentHp(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("CurrentHp")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getMaxHp(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("MaxHp")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getCurrExp(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("CurrentExp")) {
                    return (int)a.value;
                }
            }
        }

        return var;
    }

    public double getMaxExp(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("MaxExp")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getLevel(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("Level")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getCurrMana(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("CurrentMana")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getMaxMana(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("MaxMana")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getAttackSpeed(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("AttackSpeed")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getCurrentExp(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("CurrentExp")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public double getSlowness(List<Attribute> list) {
        double var = -1;

        for (Attribute a : list) {
            if (Objects.nonNull(a)) {
                if (a.name.equals("Slowness")) {
                    return a.value;
                }
            }
        }

        return var;
    }

    public static double getPhysicalDamage(List<Attribute> list) {
        double damage = 0;
        damage += getAttackPower(list);
        damage += getStrength(list) * 1.5;

        damage = Methods.random((int)damage, (int)(damage * 1.2));
        return damage;
    }

    public static double getProjectileDamage(List<Attribute> list) {
        double damage = 0;
        damage += getAttackPower(list);
        damage += getAgility(list) * 1.5;

        damage = Methods.random((int)damage, (int)(damage * 1.2));
        return damage;
    }

    public static double getMagicalDamage(List<Attribute> list) {
        double damage = 0;
        damage += getAttackPower(list);
        damage += getIntellect(list) * 1.5;

        damage = Methods.random((int)damage, (int)(damage * 1.2));
        return damage;
    }

    public static double getNegatedDamage(double damage, List<Attribute> list) {
        double armor = getArmor(list) + getBonusArmor(list);
        armor = armor/10;
        double res = (100.00 - getResist(list)) / 100.00;

        damage -= getBlock(list);
        damage -= armor;
        damage = damage * res;


        if (damage > 0) {
            return damage;
        }
        return 0;
    }

    @Override
    public String toString() {
        return name + " " + value;
    }

    @Override
    public void write(Json json) {
        json.writeValue("name", getName());
        json.writeValue("value", getValue());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        if (jsonData.has("name")) {
            setName(jsonData.getString("name"));
        }

        if (jsonData.has("value")) {
            setValue(jsonData.getDouble("value"));
        }
    }
}
