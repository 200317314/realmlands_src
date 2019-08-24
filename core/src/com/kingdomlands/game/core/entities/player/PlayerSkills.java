package com.kingdomlands.game.core.entities.player;

/**
 * @author Travis T
 */
public class PlayerSkills {

    private int swordProficiency, axeProficiency, maceProficiency, staffProficiency, bowProficiency, woodcutting, fishing, mining, smithing;

    public PlayerSkills(int swordProficiency, int axeProficiency, int maceProficiency, int staffProficiency, int bowProficiency, int woodcutting, int fishing, int mining, int smithing) {
        this.swordProficiency = swordProficiency;
        this.axeProficiency = axeProficiency;
        this.maceProficiency = maceProficiency;
        this.staffProficiency = staffProficiency;
        this.bowProficiency = bowProficiency;
        this.woodcutting = woodcutting;
        this.fishing = fishing;
        this.mining = mining;
        this.smithing = smithing;
    }

    public int getSwordProficiency() {
        return swordProficiency;
    }

    public void setSwordProficiency(int swordProficiency) {
        this.swordProficiency = swordProficiency;
    }

    public int getAxeProficiency() {
        return axeProficiency;
    }

    public void setAxeProficiency(int axeProficiency) {
        this.axeProficiency = axeProficiency;
    }

    public int getMaceProficiency() {
        return maceProficiency;
    }

    public void setMaceProficiency(int maceProficiency) {
        this.maceProficiency = maceProficiency;
    }

    public int getStaffProficiency() {
        return staffProficiency;
    }

    public void setStaffProficiency(int staffProficiency) {
        this.staffProficiency = staffProficiency;
    }

    public int getBowProficiency() {
        return bowProficiency;
    }

    public void setBowProficiency(int bowProficiency) {
        this.bowProficiency = bowProficiency;
    }

    public int getWoodcutting() {
        return woodcutting;
    }

    public void setWoodcutting(int woodcutting) {
        this.woodcutting = woodcutting;
    }

    public int getFishing() {
        return fishing;
    }

    public void setFishing(int fishing) {
        this.fishing = fishing;
    }

    public int getMining() {
        return mining;
    }

    public void setMining(int mining) {
        this.mining = mining;
    }

    public int getSmithing() {
        return smithing;
    }

    public void setSmithing(int smithing) {
        this.smithing = smithing;
    }
}
