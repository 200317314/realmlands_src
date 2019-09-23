package com.kingdomlands.game.core.entities.objects.portal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kingdomlands.game.core.entities.util.groups.Group;
import com.kingdomlands.game.core.stages.Stages;

public enum Realms {
    GRASSLANDS(new Image(new Texture(Gdx.files.internal("portal/earth_icon.png"))), "Grass Lands",
            1 , "A desolated plains filled with animals and wandering monsters.", new Group[]{Group.CREATURE_PIT, Group.SNAKE_PIT, Group.SCORPION_PIT}, Stages.ADVENTURE_MAP),
    WETLANDS(new Image(new Texture(Gdx.files.internal("portal/wetlands_icon.png"))), "Wet Lands",
            5 , "A wet land full of strong monsters to try and ambush you.", new Group[]{Group.GOBLIN_CAMP, Group.GOBLIN_SCOUT, Group.GOBLIN_ENCLOSURE, Group.GOBLIN_SCAVANGE, Group.GOBLIN_WARBAND}, Stages.ADVENTURE_MAP),
    UNDEADLANDS(new Image(new Texture(Gdx.files.internal("portal/undeadlands_icon.png"))), "Undead Forest",
            10 , "A haunted forest filled with enchanted souls who died years ago.", new Group[]{Group.SKELETON_CRYPT, Group.SKELETON_SCOUT, Group.SKELETON_ARMY}, Stages.ADVENTURE_MAP);

    private final Image icon;
    private final String name;
    private final int level;
    private final String description;
    private final Group[] groups;
    private final Stages stages;

    Realms(Image icon, String name, int level, String description, Group[] groups, Stages stages) {
        this.icon = icon;
        this.name = name;
        this.level = level;
        this.description = description;
        this.groups = groups;
        this.stages = stages;
    }

    public Image getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public Group[] getGroups() {
        return groups;
    }

    public Stages getStages() {
        return stages;
    }
}
