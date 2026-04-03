package com.narxoz.rpg.tournament;

import com.narxoz.rpg.arena.ArenaFighter;
import com.narxoz.rpg.arena.ArenaOpponent;
import com.narxoz.rpg.arena.TournamentResult;
import com.narxoz.rpg.chain.ArmorHandler;
import com.narxoz.rpg.chain.BlockHandler;
import com.narxoz.rpg.chain.DefenseHandler;
import com.narxoz.rpg.chain.DodgeHandler;
import com.narxoz.rpg.chain.HpHandler;
import com.narxoz.rpg.command.ActionQueue;
import com.narxoz.rpg.command.AttackCommand;
import com.narxoz.rpg.command.DefendCommand;
import com.narxoz.rpg.command.HealCommand;
import java.util.Random;

public class TournamentEngine {
    private final ArenaFighter hero;
    private final ArenaOpponent opponent;
    private Random random = new Random(1L);

    public TournamentEngine(ArenaFighter hero, ArenaOpponent opponent) {
        this.hero = hero;
        this.opponent = opponent;
    }

    public TournamentEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public TournamentResult runTournament() {
        TournamentResult result = new TournamentResult();
        int round = 0;
        final int maxRounds = 20;

        DefenseHandler dodge =new DodgeHandler(hero.getDodgeChance(), 1L);
        DefenseHandler block=new BlockHandler(hero.getBlockRating()/100.0);
        DefenseHandler armor=new ArmorHandler(hero.getArmorValue());
        DefenseHandler hp=new HpHandler();

        dodge.setNext(block).setNext(armor).setNext(hp);
        ActionQueue queue =new ActionQueue();

        while(hero.isAlive() && opponent.isAlive() && round < maxRounds) {
            round++;
            queue.enqueue(new AttackCommand(opponent, hero.getAttackPower()));
            queue.enqueue(new HealCommand(hero, 20));
            queue.enqueue(new DefendCommand(hero, 0.1));
            queue.executeAll();

            if(opponent.isAlive()){
                dodge.handle(opponent.getAttackPower(), hero);
            }
            result.addLine("[Round " +round+ "] Opp HP: " +opponent.getHealth()+" | Hero HP: "+hero.getHealth());
        }

        result.setWinner(hero.isAlive() ? hero.getName() : opponent.getName());
        result.setRounds(round);
        return result;
    }
}
