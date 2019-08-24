package com.kingdomlands.game.core.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kingdomlands.game.core.Constants;
import com.kingdomlands.game.core.entities.Entity;
import com.kingdomlands.game.core.entities.EntityType;
import com.kingdomlands.game.core.entities.monster.Monster;
import com.kingdomlands.game.core.entities.npc.Npc;
import com.kingdomlands.game.core.entities.npc.NpcManager;
import com.kingdomlands.game.core.entities.objects.GameObject;
import com.kingdomlands.game.core.entities.objects.ObjectManager;
import com.kingdomlands.game.core.entities.projectile.Projectile;
import com.kingdomlands.game.core.entities.projectile.Projectiles;
import com.kingdomlands.game.core.entities.util.Methods;
import com.kingdomlands.game.core.entities.util.SoundManager;
import com.kingdomlands.game.core.entities.util.groups.Group;
import com.kingdomlands.game.core.entities.util.groups.GroupManager;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by David K on Mar, 2019
 */
public class StageManager {
    private static Stage currentStage;

    private static ScreenViewport viewPort;

    public static void init() {
        OrthographicCamera camera = new OrthographicCamera(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        viewPort = new ScreenViewport(camera);
    }

    public static void setCurrentStage(Stage stage) {
        currentStage = stage;
        Gdx.input.setInputProcessor(currentStage);

        if (stage == Stages.LOGIN.getStage()) {
            Sound titleMusic = Gdx.audio.newSound(Gdx.files.internal("music/title_screen.mp3"));
            SoundManager.setBackGroundSound(titleMusic);
        }

        if (stage == Stages.TOWN.getStage()) {
            Sound titleMusic = Gdx.audio.newSound(Gdx.files.internal("music/world.mp3"));
            SoundManager.setBackGroundSound(titleMusic);

            //Add Buildings
            StageManager.addActor(ObjectManager.createObjectById(18, 7180, 7860));

            //Add Npcs
            Npc seymour = NpcManager.createNpc(1);
            seymour.setPosition(9285, 16000 - 6783);
            StageManager.addActor(seymour);

            Npc ghorza = NpcManager.createNpc(2);
            ghorza.setPosition(7052, 16000 - 8766);
            StageManager.addActor(ghorza);

            Npc gamel = NpcManager.createNpc(3);
            gamel.setPosition(7367, 16000 - 8853);
            StageManager.addActor(gamel);

            Npc romella = NpcManager.createNpc(4);
            romella.setPosition(7749, 16000 - 8148);
            StageManager.addActor(romella);

            Npc rabaz = NpcManager.createNpc(5);
            rabaz.setPosition(7990, 16000 - 8306);
            StageManager.addActor(rabaz);
        }
    }

    public static Stage getCurrentStage() {
        return currentStage;
    }

    public static void addActor(Actor actor) {
        currentStage.addActor(actor);
    }

    public static void removeActor(Actor actor) {
        actor.remove();
    }

    public static ScreenViewport getViewPort() {
        return viewPort;
    }

    public static List<Entity> getAllEntities() {
        List<Entity> entities = new ArrayList<>();
        Stage currentStage = getCurrentStage();

        if (Objects.nonNull(currentStage)) {
            for (Actor a : currentStage.getActors()) {
                if (Objects.nonNull(a)) {
                    if (a instanceof Entity) {
                        entities.add((Entity) a);
                    }
                }
            }
        }

        return entities;
    }

    public static List<Entity> getAllEntityOfType(EntityType entityType) {
        List<Entity> entities = new ArrayList<>();

        getAllEntities().forEach(e -> {
            if (Objects.nonNull(e)) {
                if (e.getEntityType().equals(entityType)) {
                    entities.add(e);
                }
            }
        });

        return entities;
    }

    public static Entity getFilteredEntity(Predicate<Entity> predicate) {
        Collection<Entity> collection = new ArrayList<>();

        getAllEntities().forEach(e -> {
            if (Objects.nonNull(e)) {
                if (predicate.test(e)) {
                    collection.add(e);
                }
            }
        });

        if (collection.size() != 0) {
            return ((ArrayList<Entity>) collection).get(0);
        } else {
            return null;
        }
    }

    public static List<Entity> getFilteredEntitys(Predicate<Entity> predicate) {
        Collection<Entity> collection = new ArrayList<>();

        getAllEntities().forEach(e -> {
            if (Objects.nonNull(e)) {
                if (predicate.test(e)) {
                    collection.add(e);
                }
            }
        });

        return (List<Entity>) collection;
    }

    public static List<Vector2> getAllEntityPositions(Entity e, Entity clicked) {
        List<Vector2> pos = new ArrayList<>();

        getFilteredEntitys(en -> Objects.nonNull(en) && en.getDistance(e) <= 1600).forEach(entity -> {
            if (Objects.nonNull(entity)) {
                if (Objects.nonNull(clicked)) {
                    if (entity.getEntityType() != EntityType.ITEM && entity.getEntityType() != EntityType.PLAYER && !entity.equals(e) && !entity.equals(clicked)) {
                        Rectangle bounds = getEntityBounds(entity);
                        if (bounds.width > 64) {
                            for (int i = 0; i < (bounds.width/64); i++) {
                                pos.add(new Vector2(entity.getPosition().x/64, entity.getPosition().y/64 + i));
                            }
                        }

                        if (bounds.height > 64) {
                            for (int i = 0; i < (bounds.height/64); i++) {
                                pos.add(new Vector2(entity.getPosition().x/64 + i, entity.getPosition().y/64));
                            }
                        }

                        pos.add(new Vector2(entity.getPosition().x/64, entity.getPosition().y/64));
                    }
                } else {
                    if (entity.getEntityType() != EntityType.ITEM && entity.getEntityType() != EntityType.PLAYER && !entity.equals(e)) {
                        Rectangle bounds = getEntityBounds(entity);

                        if (bounds.width > 64) {
                            for (int i = 0; i < (bounds.width/64); i++) {
                                pos.add(new Vector2(entity.getPosition().x/64, entity.getPosition().y/64 + i));
                            }
                        }

                        if (bounds.height > 64) {
                            for (int i = 0; i < (bounds.height/64); i++) {
                                pos.add(new Vector2(entity.getPosition().x/64 + i, entity.getPosition().y/64));
                            }
                        }

                        pos.add(new Vector2(entity.getPosition().x/64, entity.getPosition().y/64));
                    }
                }
            }
        });


        return pos;
    }

    public static boolean entityOnVector(Vector2 vec) {
        //Broken atm
        Vector2 vecVec = new Vector2((int)(vec.x/64)*64, (int)(vec.y/64)*64);
        Entity entity = getFilteredEntity(e -> Objects.nonNull(e) && e.getEntityType() != EntityType.ITEM &&
                ((int)(e.getX()/64)*64) == vecVec.x && ((int)(e.getY()/64)*64) == vecVec.y);

        if (Objects.nonNull(entity)) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean clickedAnEntity(Vector2 vec) {
        for (Entity e : getAllEntities()) {
            if (Objects.nonNull(e)) {
                Rectangle bounds = new Rectangle();

                if (e.getEntityType() == EntityType.GAMEOBJECT) {
                    if (e instanceof Projectile) {

                    } else {
                        GameObject obj = (GameObject) e;
                        Vector2 objPos = StageManager.getCurrentStage().stageToScreenCoordinates(obj.getPosition());

                        bounds = new Rectangle(objPos.x - 32, 800 - objPos.y - 32, obj.getImage().getImageWidth() + 32, obj.getImage().getImageHeight() + 32);
                    }
                }

                if (e.getEntityType() == EntityType.MONSTER) {
                    Monster obj = (Monster) e;
                    Vector2 objPos = StageManager.getCurrentStage().stageToScreenCoordinates(obj.getPosition());

                    bounds = new Rectangle(objPos.x- 32, 800 - objPos.y - 32, obj.getImage().getImageWidth() + 32, obj.getImage().getImageHeight() - 32);
                }

                if (e.getEntityType() == EntityType.NPC) {
                    Npc obj = (Npc) e;
                    Vector2 objPos = StageManager.getCurrentStage().stageToScreenCoordinates(obj.getPosition());

                    bounds = new Rectangle(objPos.x- 32, 800 - objPos.y - 32, obj.getImage().getImageWidth() + 32, obj.getImage().getImageHeight() - 32);
                }

                if (bounds.contains(vec)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Rectangle getEntityBounds(Entity entity) {
        if (entity.getEntityType() == EntityType.GAMEOBJECT) {
            if (entity instanceof GameObject) {
                GameObject gameObject = (GameObject) entity;

                return new Rectangle(gameObject.getX(), gameObject.getY(), gameObject.getImage().getImageWidth(), gameObject.getImage().getImageHeight());
            }
        } else if (entity.getEntityType() == EntityType.NPC) {
            Npc npc = (Npc) entity;

            return new Rectangle(npc.getX(), npc.getY(), npc.getImage().getImageWidth(), npc.getImage().getImageHeight());
        } else if (entity.getEntityType() == EntityType.MONSTER) {
            Monster monster = (Monster) entity;

            return new Rectangle(monster.getX(), monster.getY(), monster.getImage().getImageWidth(), monster.getImage().getImageHeight());
        }

        return entity.getBounds();
    }
}
