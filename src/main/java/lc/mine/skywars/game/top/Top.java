package lc.mine.skywars.game.top;

public class Top {
    private final TopPlayer[] players;
    private final String name;

    public Top(int amountTops, String name) {
        this.players = new TopPlayer[amountTops];
        this.name = name;
    }

    public static void calculatePosition(final TopPlayer player, final Top top) {
        final int score = player.score;
        if (score == 0) {
            return; 
        }
        delete(player.name, top);
        TopPlayer toMove = null;
        int i = 0;
        final int length = top.players.length;

        while (i < length) {
            final TopPlayer playerInTop = top.players[i];
            if (playerInTop == null) {
                top.players[i] = player;
                return;
            }
            if (playerInTop.score >= score) {
                i++;
                continue;
            } 
            toMove = playerInTop;
            break;
        } 
        if (toMove == null) {
            return;
        } 
        moveOne(i, toMove, top);
        top.players[i] = player;
    }
  
    private static void delete(final String playerName, final Top top) {
        for (int i = 0; i < (top.players).length; i++) {
            final TopPlayer playerInTop = top.players[i];
            if (playerInTop == null)
                return; 
            if (playerInTop.name.equals(playerName)) {
                top.players[i] = null;
                return;
            } 
        }
    }
  
    public static void moveOne(final int i, final TopPlayer next, final Top top) {
        final TopPlayer current = top.players[i];
        top.players[i] = next;
        if (current == null || i + 1 == (top.players).length) {
            return;
        }
        moveOne(i + 1, current, top);
    }

    public TopPlayer[] getPlayers() {
        return players;
    }

    public String getName() {
        return name;
    }
}
