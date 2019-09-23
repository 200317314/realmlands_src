package com.kingdomlands.game.core.entities.player;

import com.badlogic.gdx.Gdx;
import com.kingdomlands.game.core.entities.util.*;

import java.util.List;
import java.util.Objects;

public enum PlayerClass {
    KNIGHT("Knight", 5, 30, 1, 0, 0, 0, 0, 5, 0, 0, "knight.png", "KNIGHT_HUD"),
    ARCHER("Archer", 10, 20, 0, 0, 1, 0, 0, 3, 0, 0, "archer.png", "ARCHER_HUD"),
    DRUID("Druid", 15, 10, 0, 1, 0, 0, 0, 1, 0, 0, "druid.png", "DRUID_HUD"),
    WIZARD("Wizard", 15, 10, 0, 1, 0, 0, 0, 1, 0, 0, "wizard.png", "WIZARD_HUD");

    private final String name;
    private final int bonusMana;
    private final int bonusHp;
    private final int bonusStrength;
    private final int bonusIntelligence;
    private final int bonusAgility;
    private final int bonusHpRegen;
    private final int bonusManaRegen;
    private final int bonusManaArmor;
    private final int bonusManaParry;
    private final int bonusManaStamina;
    private final String image;
    private final String avatar;

    PlayerClass(String name, int bonusMana, int bonusHp, int bonusStrength, int bonusIntelligence, int bonusAgility, int bonusHpRegen, int bonusManaRegen, int bonusManaArmor, int bonusManaParry, int bonusManaStamina, String image, String avatar) {
        this.name = name;
        this.bonusMana = bonusMana;
        this.bonusHp = bonusHp;
        this.bonusStrength = bonusStrength;
        this.bonusIntelligence = bonusIntelligence;
        this.bonusAgility = bonusAgility;
        this.bonusHpRegen = bonusHpRegen;
        this.bonusManaRegen = bonusManaRegen;
        this.bonusManaArmor = bonusManaArmor;
        this.bonusManaParry = bonusManaParry;
        this.bonusManaStamina = bonusManaStamina;
        this.image = image;
        this.avatar = avatar;
    }

    public static void levelUp(Player player) {
        if (Objects.nonNull(player.getPlayerAttributes())) {
            SoundManager.playSoundFx(Gdx.audio.newSound(Gdx.files.internal("sounds/" + "levelup.wav")));

            Attribute.setAttributeValueFromList(player.getPlayerAttributes(),
                    "Level",
                    Attribute.getAttributeFromList(player.getPlayerAttributes(), "Level").getLevel(player.getPlayerAttributes()) + 1);

            Attribute.setAttributeValueFromList(player.getPlayerAttributes(),
                    "Stamina",
                    Attribute.getAttributeValueFromList(player.getPlayerAttributes(), "Stamina") + player.getPlayerClass().bonusManaStamina);

            Attribute.setAttributeValueFromList(player.getPlayerAttributes(),
                    "Armor",
                    Attribute.getAttributeFromList(player.getPlayerAttributes(), "Armor").getArmor(player.getPlayerAttributes()) + player.getPlayerClass().bonusManaArmor);

            Attribute.setAttributeValueFromList(player.getPlayerAttributes(),
                    "Strength",
                    Attribute.getAttributeFromList(player.getPlayerAttributes(), "Strength").getStrength(player.getPlayerAttributes()) + player.getPlayerClass().bonusStrength);

            Attribute.setAttributeValueFromList(player.getPlayerAttributes(),
                    "Intellect",
                    Attribute.getAttributeFromList(player.getPlayerAttributes(), "Intellect").getIntellect(player.getPlayerAttributes()) + player.getPlayerClass().bonusIntelligence);

            Attribute.setAttributeValueFromList(player.getPlayerAttributes(),
                    "Agility",
                    Attribute.getAttributeFromList(player.getPlayerAttributes(), "Agility").getAgility(player.getPlayerAttributes()) + player.getPlayerClass().bonusAgility);

            Attribute.setAttributeValueFromList(player.getPlayerAttributes(),
                    "Parry",
                    Attribute.getAttributeFromList(player.getPlayerAttributes(), "Parry").getParry(player.getPlayerAttributes()) + player.getPlayerClass().bonusManaParry);

            Attribute.setAttributeValueFromList(player.getPlayerAttributes(),
                    "ManaRegen",
                    Attribute.getAttributeFromList(player.getPlayerAttributes(), "ManaRegen").getManaRegen(player.getPlayerAttributes()) + player.getPlayerClass().bonusManaRegen);

            Attribute.setAttributeValueFromList(player.getPlayerAttributes(),
                    "HpRegen",
                    Attribute.getAttributeFromList(player.getPlayerAttributes(), "HpRegen").getHpRegen(player.getPlayerAttributes()) + player.getPlayerClass().bonusHpRegen);

            Attribute.setAttributeValueFromList(player.getPlayerAttributes(),
                    "CurrentExp",
                    Attribute.getAttributeFromList(player.getPlayerAttributes(), "CurrentExp").getCurrExp(player.getPlayerAttributes()) - Attribute.getAttributeFromList(player.getPlayerAttributes(), "MaxExp").getMaxExp(player.getPlayerAttributes()));

            Attribute.setAttributeValueFromList(player.getPlayerAttributes(),
                    "MaxExp",
                    (int) getExpForLevel(Attribute.getAttributeFromList(player.getPlayerAttributes(), "Level").getLevel(player.getPlayerAttributes())));

            Attribute.setAttributeValueFromList(player.getPlayerAttributes(),
                    "MaxHp",
                    Attribute.getAttributeFromList(player.getPlayerAttributes(), "MaxHp").getMaxHp(player.getPlayerAttributes()) + player.getPlayerClass().bonusHp);

            Attribute.setAttributeValueFromList(player.getPlayerAttributes(),
                    "CurrentHp",
                    Attribute.getAttributeFromList(player.getPlayerAttributes(), "MaxHp").getMaxHp(player.getPlayerAttributes()));

            Attribute.setAttributeValueFromList(player.getPlayerAttributes(),
                    "MaxMana",
                    Attribute.getAttributeFromList(player.getPlayerAttributes(), "MaxMana").getMaxMana(player.getPlayerAttributes()) + player.getPlayerClass().bonusMana);

            Attribute.setAttributeValueFromList(player.getPlayerAttributes(),
                    "CurrentMana",
                    Attribute.getAttributeFromList(player.getPlayerAttributes(), "MaxMana").getMaxMana(player.getPlayerAttributes()));

            AlertTextManager.add(new AlertText((int)PlayerManager.getCurrentPlayer().getX() + 32, (int)PlayerManager.getCurrentPlayer().getY(), "Level Up!", DamageType.LEVEL_UP));
        }
    }

    public String getName() {
        return name;
    }

    public int getBonusMana() {
        return bonusMana;
    }

    public int getBonusHp() {
        return bonusHp;
    }

    public int getBonusStrength() {
        return bonusStrength;
    }

    public int getBonusIntelligence() {
        return bonusIntelligence;
    }

    public int getBonusAgility() {
        return bonusAgility;
    }

    public int getBonusHpRegen() {
        return bonusHpRegen;
    }

    public int getBonusManaRegen() {
        return bonusManaRegen;
    }

    public int getBonusManaArmor() {
        return bonusManaArmor;
    }

    public int getBonusManaParry() {
        return bonusManaParry;
    }

    public int getBonusManaStamina() {
        return bonusManaStamina;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getImage() {
        return image;
    }

    private static double getExpForLevel(double level) {
        return (50 * (level) * (level) * (level) - 150 * (level) * (level) + 400 * (level)) / 3;
    }
}
