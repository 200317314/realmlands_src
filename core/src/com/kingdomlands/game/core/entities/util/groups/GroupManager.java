package com.kingdomlands.game.core.entities.util.groups;

import com.badlogic.gdx.math.Vector2;
import com.kingdomlands.game.core.entities.item.Item;
import com.kingdomlands.game.core.entities.item.ItemManager;
import com.kingdomlands.game.core.entities.monster.Monster;
import com.kingdomlands.game.core.entities.monster.MonsterManager;
import com.kingdomlands.game.core.entities.objects.GameObject;
import com.kingdomlands.game.core.entities.objects.ObjectManager;
import com.kingdomlands.game.core.entities.objects.portal.Realms;
import com.kingdomlands.game.core.entities.util.Methods;
import com.kingdomlands.game.core.stages.StageManager;

import java.util.Objects;

public class GroupManager {
    public static void initGenerator(Realms realm) {
        Vector2[] vector2s = new Vector2[]{new Vector2(1,0), new Vector2(5,5), new Vector2(1, 5), new Vector2(5, 1)};
        Vector2 portalPos = vector2s[Methods.getSecureRandom().nextInt(vector2s.length)];

        for (int i = 1; i < 6; i++) {
            for (int ii = 1; ii < 6; ii++) {
                if (!isCenter(i, ii) && !isPortal(portalPos, i, ii)) {
                    generateGroup(realm.getGroups()[Methods.getSecureRandom().nextInt(realm.getGroups().length)], i, ii);
                }
            }
        }

        StageManager.addActor(ObjectManager.createObjectById(22, 7940, 8200));
    }

    public static void generateGroup(Group group, int x, int y) {
        if (group.getMonsterIds().length != 0) {
            for (int i = 0; i < Methods.random(group.getMinMonsterSpawn(), group.getMaxMonsterSpawn()); i++) {
                Vector2 point = Methods.getRandomPointAway(new Vector2((x*50*64) - 1600, (y*50*64) - 1600), 25);

                Monster genMonster = MonsterManager.generateRngMonster(group.getMonsterIds()[Methods.getSecureRandom().nextInt(group.getMonsterIds().length)]);
                genMonster.setPosition(point.x, point.y);
                StageManager.addActor(genMonster);
            }
        }

        if (group.getItemIds().length != 0) {
            for (int i = 0; i < Methods.random(group.getMinItemSpawn(), group.getMaxItemSpawn()); i++) {
                Vector2 point = Methods.getRandomPointAway(new Vector2((x*50*64) - 1600, (y*50*64) - 1600), 20);
                Item genItem = ItemManager.createItemById(group.getMonsterIds()[Methods.getSecureRandom().nextInt(group.getMonsterIds().length)], Methods.random(group.getMinItemSpawn(), group.getMaxItemSpawn()));
                genItem.setPosition(point.x, point.y);
                StageManager.addActor(genItem);
            }
        }

        if (group.getGameObjectIds().length != 0) {
            for (int i = 0; i < Methods.random(group.getMinGameObjectSpawn(), group.getMaxGameObjectSpawn()); i++) {
                Vector2 point = Methods.getRandomPointAway(new Vector2((x*50*64) - 1600, (y*50*64) - 1600), 20);
                StageManager.addActor(ObjectManager.createObjectById(group
                                .getGameObjectIds()[Methods.getSecureRandom().nextInt(group.getGameObjectIds().length)],
                        (int)point.x, (int)point.y));
            }
        }
    }

    private static boolean isCenter(int i, int ii) {
        return i == 3 && ii == 3;
    }

    private static boolean isPortal(Vector2 vector2, int i, int ii) {
        return (int)vector2.x == i && (int)vector2.y == ii;
    }
}
