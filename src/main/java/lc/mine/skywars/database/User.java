package lc.mine.skywars.database;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lc.mine.skywars.kit.Kit;

public class User {

    public final UUID uuid;
    public final String name;

    public int kills = 0;
    public int deaths = 0;

    public Set<String> kits = new HashSet<>();

    public int wins = 0;

    public int chestRefillVoteIndex = -1;
    public Kit selectedKit;

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public boolean isNew() {
        return false;
    }

    public static final class New extends User {
        public New(UUID uuid, String name) {
            super(uuid, name);
        }
        @Override
        public boolean isNew() {
            return true;
        }
    }
}