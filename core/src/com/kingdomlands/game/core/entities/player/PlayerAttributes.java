package com.kingdomlands.game.core.entities.player;

import com.kingdomlands.game.core.entities.util.Methods;

/**
 * Created by David K on Mar, 2019
 */
public class PlayerAttributes {
    private double stamina, strength, agility, intellect, crit, haste, expBoost, bonusArmor, lifeSteal, attackPower,
            spellPower, castingSpeed, manaRegen, hpRegen, armor, dodge, parry, block, resist, movementSpeed,
            currentHp, maxHp, currExp, maxExp, level, currMana, maxMana, attackSpeed, slowness;

    public PlayerAttributes(double stamina, double strength, double agility, double intellect, double crit, double haste, double expBoost, double bonusArmor, double lifeSteal, double attackPower, double spellPower, double castingSpeed, double manaRegen, double hpRegen, double armor, double dodge, double parry, double block, double resist, double movementSpeed, double currentHp, double maxHp, double currExp, double maxExp, double level, double currMana, double maxMana, double attackSpeed, double slowness) {
        this.stamina = stamina;
        this.strength = strength;
        this.agility = agility;
        this.intellect = intellect;
        this.crit = crit;
        this.haste = haste;
        this.expBoost = expBoost;
        this.bonusArmor = bonusArmor;
        this.lifeSteal = lifeSteal;
        this.attackPower = attackPower;
        this.spellPower = spellPower;
        this.castingSpeed = castingSpeed;
        this.manaRegen = manaRegen;
        this.hpRegen = hpRegen;
        this.armor = armor;
        this.dodge = dodge;
        this.parry = parry;
        this.block = block;
        this.resist = resist;
        this.movementSpeed = movementSpeed;
        this.currentHp = currentHp;
        this.maxHp = maxHp;
        this.currExp = currExp;
        this.maxExp = maxExp;
        this.level = level;
        this.currMana = currMana;
        this.maxMana = maxMana;
        this.attackSpeed = attackSpeed;
    }

    public double getAttackPower() {
        return attackPower;
    }

    public double getStamina() {
        return stamina;
    }

    public double getStrength() {
        return strength;
    }

    public double getAgility() {
        return agility;
    }

    public double getIntellect() {
        return intellect;
    }

    public double getCrit() {
        return crit;
    }

    public double getHaste() {
        return haste;
    }

    public double getExpBoost() {
        return expBoost;
    }

    public double getBonusArmor() {
        return bonusArmor;
    }

    public double getLifeSteal() {
        return lifeSteal;
    }

    public double getSpellPower() {
        return spellPower;
    }

    public double getCastingSpeed() {
        return castingSpeed;
    }

    public double getManaRegen() {
        return manaRegen;
    }

    public double getHpRegen() {
        return hpRegen;
    }

    public double getArmor() {
        return armor;
    }

    public double getDodge() {
        return dodge;
    }

    public double getParry() {
        return parry;
    }

    public double getBlock() {
        return block;
    }

    public double getResist() {
        return resist;
    }

    public double getMovementSpeed() {
        double modifier = (100.00 + haste) / 100.00;

        return movementSpeed * modifier;
    }

    public double getCurrentHp() {
        return currentHp;
    }

    public double getMaxHp() {
        return maxHp;
    }

    public double getCurrExp() {
        return currExp;
    }

    public double getMaxExp() {
        return maxExp;
    }

    public double getLevel() {
        return level;
    }

    public double getCurrMana() {
        return currMana;
    }

    public double getMaxMana() {
        return maxMana;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public double getSlowness() {
        return this.slowness;
    }

    public double getPhysicalDamage() {
        double damage = 0;
        damage += attackPower;
        damage += strength * 3;

        damage = Methods.random((int)damage, (int)(damage * 1.2));
        return damage;
    }

    public double getProjectileDamage() {
        double damage = 0;
        damage += attackPower;
        damage += agility * 3;

        damage = Methods.random((int)damage, (int)(damage * 1.2));
        return damage;
    }

    public double getMagicalDamage() {
        double damage = 0;
        damage += attackPower;
        damage += intellect * 3;

        damage = Methods.random((int)damage, (int)(damage * 1.2));
        return damage;
    }

    public double getNegatedDamage(double damage) {
        double armor = getArmor() + bonusArmor;
        armor = armor/10;
        double res = (100.00 - resist) / 100.00;

        damage -= block;
        damage -= armor;
        damage = damage * res;


        if (damage > 0) {
            return damage;
        }
        return 0;
    }
}
