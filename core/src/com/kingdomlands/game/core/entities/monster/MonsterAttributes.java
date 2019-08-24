package com.kingdomlands.game.core.entities.monster;

import com.kingdomlands.game.core.entities.util.Methods;

/**
 * Created by David K on Mar, 2019
 */
public class MonsterAttributes {
    private double stamina, strength, agility, intellect, crit, haste, expBoost, bonusArmor, lifeSteal, attackPower,
            spellPower, castingSpeed, manaRegen, hpRegen, armor, dodge, parry, block, resist, movementSpeed,
            currentHp, maxHp, currExp, maxExp, level, currMana, maxMana, attackSpeed;

    public MonsterAttributes(double stamina, double strength, double agility, double intellect, double crit, double haste, double expBoost, double bonusArmor, double lifeSteal, double attackPower, double spellPower, double castingSpeed, double manaRegen, double hpRegen, double armor, double dodge, double parry, double block, double resist, double movementSpeed, double currentHp, double maxHp, double currExp, double maxExp, double level, double currMana, double maxMana, double attackSpeed) {
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

    public MonsterAttributes() {

    }

    public double getStamina() {
        return stamina;
    }

    public void setStamina(double stamina) {
        this.stamina = stamina;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public double getAgility() {
        return agility;
    }

    public void setAgility(double agility) {
        this.agility = agility;
    }

    public double getIntellect() {
        return intellect;
    }

    public void setIntellect(double intellect) {
        this.intellect = intellect;
    }

    public double getCrit() {
        return crit;
    }

    public void setCrit(double crit) {
        this.crit = crit;
    }

    public double getHaste() {
        return haste;
    }

    public void setHaste(double haste) {
        this.haste = haste;
    }

    public double getExpBoost() {
        return expBoost;
    }

    public void setExpBoost(double expBoost) {
        this.expBoost = expBoost;
    }

    public double getBonusArmor() {
        return bonusArmor;
    }

    public void setBonusArmor(double bonusArmor) {
        this.bonusArmor = bonusArmor;
    }

    public double getLifeSteal() {
        return lifeSteal;
    }

    public void setLifeSteal(double lifeSteal) {
        this.lifeSteal = lifeSteal;
    }

    public double getAttackPower() {
        return Methods.random((int)(attackPower + strength), (int)((attackPower + strength) * 1.4));
    }

    public void setAttackPower(double attackPower) {
        this.attackPower = attackPower;
    }

    public double getSpellPower() {
        return spellPower;
    }

    public void setSpellPower(double spellPower) {
        this.spellPower = spellPower;
    }

    public double getCastingSpeed() {
        return castingSpeed;
    }

    public void setCastingSpeed(double castingSpeed) {
        this.castingSpeed = castingSpeed;
    }

    public double getManaRegen() {
        return manaRegen;
    }

    public void setManaRegen(double manaRegen) {
        this.manaRegen = manaRegen;
    }

    public double getHpRegen() {
        return hpRegen;
    }

    public void setHpRegen(double hpRegen) {
        this.hpRegen = hpRegen;
    }

    public double getArmor() {
        return armor;
    }

    public void setArmor(double armor) {
        this.armor = armor;
    }

    public double getDodge() {
        return dodge;
    }

    public void setDodge(double dodge) {
        this.dodge = dodge;
    }

    public double getParry() {
        return parry;
    }

    public void setParry(double parry) {
        this.parry = parry;
    }

    public double getBlock() {
        return block;
    }

    public void setBlock(double block) {
        this.block = block;
    }

    public double getResist() {
        return resist;
    }

    public void setResist(double resist) {
        this.resist = resist;
    }

    public double getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(double movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public double getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(double currentHp) {
        this.currentHp = currentHp;
    }

    public double getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(double maxHp) {
        this.maxHp = maxHp;
    }

    public double getCurrExp() {
        return currExp;
    }

    public void setCurrExp(double currExp) {
        this.currExp = currExp;
    }

    public double getMaxExp() {
        return maxExp;
    }

    public void setMaxExp(double maxExp) {
        this.maxExp = maxExp;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public double getCurrMana() {
        return currMana;
    }

    public void setCurrMana(double currMana) {
        this.currMana = currMana;
    }

    public double getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(double maxMana) {
        this.maxMana = maxMana;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
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
