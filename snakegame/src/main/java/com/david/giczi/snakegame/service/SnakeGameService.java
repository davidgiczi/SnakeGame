package com.david.giczi.snakegame.service;

import java.util.List;

import com.david.giczi.snakegame.domain.Component;

public interface SnakeGameService {

	void setGameBoardParam();
	List<Component> createSnake(int length);
	List<Component> addSnakeToBoardComponent(List<Component> snake);
	List<Component> goDirect(List<Component> snake);
	List<Component> turnLeft(List<Component> snake);
	List<Component> turnRight(List<Component> snake);
	boolean canGoDirect(List<Component> snake);
	boolean canTurnLeft(List<Component> snake);
	boolean canTurnRight(List<Component> snake);
	boolean isSnakeBittenByItself(List<Component> snake);
	List<Component> createBarrierComponentStore(List<Component> snake);
	int calcScore(List<Component> snake);
	int getTempo(List<Component> snake);
	String calcLevel(List<Component> snake);
	
}