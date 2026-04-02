package com.narxoz.rpg.command;

public interface ActionCommand {
    void execute();
    void undo();
    String getDescription();
} 
