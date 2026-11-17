package simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import characters.Player;
import factory.PlayerFactory;
import factory.PotionFactory;
import factory.WeaponFactory;
import items.PotionType;
import items.Weapon.WeaponType;
import util.GameLog;

/**
 * Headless balance tester. Plays many auto-played runs in parallel and reports win rates.
 * Run from the command line:  java simulator.BatchSimulator [battles]
 */
public class BatchSimulator {

    public record Result(int runs, int wins, double avgTurns, int[] diedOnTier) {
        public double winRate() { return runs == 0 ? 0 : 100.0 * wins / runs; }
    }

    private record Single(boolean won, int turns, int tier) { }

    public static Result run(int runs, int startTier, WeaponType weapon, PotionType potion)
            throws InterruptedException, ExecutionException {

        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            List<Future<Single>> futures = new ArrayList<>();
            for (int i = 0; i < runs; i++) {
                futures.add(pool.submit(() -> playOne(startTier, weapon, potion)));
            }
            int wins = 0;
            long turns = 0;
            int[] died = new int[4];
            for (Future<Single> f : futures) {
                Single s = f.get();
                turns += s.turns();
                if (s.won()) wins++; else died[s.tier()]++;
            }
            return new Result(runs, wins, runs == 0 ? 0 : (double) turns / runs, died);
        } finally {
            pool.shutdownNow();
        }
    }

    private static Single playOne(int startTier, WeaponType weapon, PotionType potion) {
        GameLog.silenceCurrentThread();

        Player p = PlayerFactory.createPlayer("Hero");
        if (weapon != null) p.equipWeapon(WeaponFactory.createWeapon(weapon));
        if (potion != null) p.equipPotion(PotionFactory.createPotion(potion));

        BattleSession session = new BattleSession(p, startTier);
        int turns = 0;
        while (turns < 500) {
            turns++;
            BattleSession.Outcome o = session.playerAutoAct();
            if (o == BattleSession.Outcome.VICTORY) return new Single(true, turns, session.getTier());
            if (o == BattleSession.Outcome.DEFEAT) return new Single(false, turns, session.getTier());
            if (o == BattleSession.Outcome.NEXT_ENEMY) continue;

            o = session.enemyAct();
            if (o == BattleSession.Outcome.DEFEAT) return new Single(false, turns, session.getTier());
        }
        return new Single(false, turns, session.getTier());   // stalled: counts as a loss
    }

    public static void main(String[] args) throws Exception {
        int runs = args.length > 0 ? Integer.parseInt(args[0]) : 2000;

        System.out.printf("%-14s %-16s %8s %8s   died on tier 1/2/3%n", "weapon", "potion", "win %", "turns");
        WeaponType[] weapons = {null, WeaponType.HAND_GUN, WeaponType.SHOT_GUN, WeaponType.MACHINE_GUN};
        PotionType[] potions = {null, PotionType.FREEZE, PotionType.STRENGTH};

        for (WeaponType w : weapons) {
            for (PotionType po : potions) {
                Result r = run(runs, 1, w, po);
                System.out.printf("%-14s %-16s %7.1f%% %8.1f   %d / %d / %d%n",
                        w == null ? "none" : w, po == null ? "none" : po,
                        r.winRate(), r.avgTurns(), r.diedOnTier()[1], r.diedOnTier()[2], r.diedOnTier()[3]);
            }
        }
    }
}
