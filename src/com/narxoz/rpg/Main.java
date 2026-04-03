package com.narxoz.rpg;

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
import com.narxoz.rpg.tournament.TournamentEngine;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Homework 6 Demo: Chain of Responsibility + Command ===\n");

        ArenaFighter hero = new ArenaFighter("Shadow Knight", 110, 0.30, 20, 7, 20, 2);

        ArenaOpponent opponent = new ArenaOpponent("Blood Reaper", 95, 15);

        // -----------------------------------------------------------------------
        // Part 1 — Command Queue Demo
        // -----------------------------------------------------------------------
        System.out.println("--- Command Queue Demo ---");

        ActionQueue queue = new ActionQueue();
        queue.enqueue(new AttackCommand(opponent, hero.getAttackPower()));
        queue.enqueue(new HealCommand(hero, 25));
        queue.enqueue(new DefendCommand(hero, 0.20));

        System.out.println("Queued actions:");
        for (String desc : queue.getCommandDescriptions()) {
            System.out.println("  " + desc);
        }

        System.out.println("\nUndoing last queued action...");
        queue.undoLast();

        System.out.println("Queue after undo:");
        for (String desc : queue.getCommandDescriptions()) {
            System.out.println("  " + desc);
        }

        queue.enqueue(new DefendCommand(hero, 0.15));
        System.out.println("\nExecuting all queued commands...");
        queue.executeAll();

        System.out.println("Enemy HP: " + opponent.getHealth());
        System.out.println("Hero HP: " + hero.getHealth());

        // -----------------------------------------------------------------------
        // Part 2 — Defense Chain Demo
        // -----------------------------------------------------------------------
        System.out.println("\n--- Defense Chain Demo ---");

        DefenseHandler dodge = new DodgeHandler(0.40, 99L);
        DefenseHandler block = new BlockHandler(0.35);
        DefenseHandler armor = new ArmorHandler(6);
        DefenseHandler hp    = new HpHandler();
        dodge.setNext(block).setNext(armor).setNext(hp);

        System.out.println("Sending 20 incoming damage through the defense chain...");
        System.out.println("Hero HP before: " + hero.getHealth());
        
        dodge.handle(25, hero);
        System.out.println("Hero HP after:  " + hero.getHealth());

        // -----------------------------------------------------------------------
        // Part 3 — Full Tournament Demo
        // -----------------------------------------------------------------------
        System.out.println("\n--- Full Arena Tournament ---");

        ArenaFighter tournamentHero  = new ArenaFighter("Storm Blade", 130, 0.28, 30, 10, 24, 3);
        ArenaOpponent tournamentOpponent = new ArenaOpponent("Dark Gladiator", 105, 18);

        TournamentResult result = new TournamentEngine(tournamentHero, tournamentOpponent)
                .setRandomSeed(42L)
                .runTournament();

        System.out.println("Winner : " + result.getWinner());
        System.out.println("Rounds : " + result.getRounds());
        System.out.println("Battle log:");
        for (String line : result.getLog()) {
            System.out.println("  " + line);
        }

        System.out.println("\n=== Demo Complete ===");
    }
}

//COMMAND PATTERN
//-------------------
//The Command pattern is used to encapsulate actions such as:
// - Attack
// - Heal
// - Defend

//Each action is represented as an object implementing ActionCommand.
//This allows:
// - Storing actions in a queue (ActionQueue)
// - Executing them later
// - Supporting undo functionality

//Classes involved:
// - ActionCommand (interface)
// - AttackCommand, HealCommand, DefendCommand (concrete commands)
// - ActionQueue (invoker)

//CHAIN OF RESPONSIBILITY PATTERN
//----------------------------------
//This pattern is used to process incoming damage through a chain of handlers.

//Damage flow:
//Dodge → Block → Armor → HP

//Each handler:
// - Processes part of the damage
// - Passes remaining damage to the next handler

//Classes involved:
// - DefenseHandler (abstract class)
// - DodgeHandler
// - BlockHandler
// - ArmorHandler
// - HpHandler

//TOURNAMENT ENGINE
//---------------------
//Simulates a battle between a hero and an opponent.

//Features:
// - Uses Command pattern for hero actions
// - Uses Chain of Responsibility for defense
// - Tracks rounds and logs battle progress
// - Determines the winner

//OVERALL ARCHITECTURE
//------------------------
//- Loose coupling between components
//- Clear separation of responsibilities
//- Easily extendable (new commands or handlers can be added)
