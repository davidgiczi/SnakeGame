package com.david.giczi.snakegame.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.david.giczi.snakegame.config.Config;
import com.david.giczi.snakegame.config.GameParam;
import com.david.giczi.snakegame.domain.Component;
import com.david.giczi.snakegame.utils.Direction;
import com.david.giczi.snakegame.utils.SnakeComponentColor;

@Service
public class SnakeGameServiceImpl implements SnakeGameService, SnakeComponentColor {

		
	private GameParam param;
	
	@Autowired
	public void setParam(GameParam param) {
		this.param = param;
	}



	@Override
	public void setGameBoardParam() {
		
		Config.BOARD_ROWS = param.getBoard_rows();
		Config.BOARD_COLS = param.getBoard_cols();
		Config.SNAKE_LENGTH = param.getSnake_length();
		Config.BARRIER_NUMBER = param.getBarrier_number();
		
	}
	
		
	@Override
	public List<Component> createSnake(int length) {

		List<Component> snake = new ArrayList<>();
		
		if (0 > length || length > Config.BOARD_ROWS * Config.BOARD_COLS) {
			length = 0;
		}
					
		for(int i = length - 2;  i >= 0; i--) {
		Component snakeBody = new Component(i);
		snakeBody.setColor(SnakeComponentColor.YELLOW);
		snake.add(snakeBody);
		}
		
		Component snakeHead = new Component(length - 1);
		snakeHead.setActualDirection(Direction.EAST);
		snakeHead.setColor(SnakeComponentColor.RED);
		snake.add(snakeHead);
		
		return snake;
	}

	@Override
	public List<Component> addSnakeToBoardComponent(List<Component> snake) {

		List<Component> componentBoard = new ArrayList<>();

		for (int i = 0; i < Config.BOARD_ROWS * Config.BOARD_COLS; i++) {

			componentBoard.add(new Component(i));
		}

		for (int i = 0; i < componentBoard.size(); i++) {
			for (Component snakeComponent : snake) {

				if (componentBoard.get(i).getLogicBoardIndex() == snakeComponent.getLogicBoardIndex()) {
					componentBoard.set(i, snakeComponent);
				}
			}

		}

		return componentBoard;

	}

	@Override
	public List<Component> goDirect(List<Component> snake) {
		
		List<Component> steppedSnake = new ArrayList<>();
		steppedSnake.add(new Component(snake.get(snake.size() - 1).getLogicBoardIndex()));
		snake.stream().filter(c -> snake.indexOf(c) < snake.size() - 2)
					  .forEach(c -> steppedSnake
					  .add(new Component(c.getLogicBoardIndex())));
		steppedSnake.forEach(c -> c.setColor(SnakeComponentColor.YELLOW));
		steppedSnake.add(stepDirect(snake.get(snake.size() - 1)));
		return steppedSnake;
	}

	@Override
	public List<Component> turnLeft(List<Component> snake) {
		
		List<Component> steppedSnake = new ArrayList<>();
		steppedSnake.add(new Component(snake.get(snake.size() - 1).getLogicBoardIndex()));
		snake.stream().filter(c -> snake.indexOf(c) < snake.size() - 2)
					  .forEach(c -> steppedSnake
					  .add(new Component(c.getLogicBoardIndex())));
		steppedSnake.forEach(c -> c.setColor(SnakeComponentColor.YELLOW));
		steppedSnake.add(stepLeft(snake.get(snake.size() - 1)));
		return steppedSnake;
	}

	@Override
	public List<Component> turnRight(List<Component> snake) {
		List<Component> steppedSnake = new ArrayList<>();
		steppedSnake.add(new Component(snake.get(snake.size() - 1).getLogicBoardIndex()));
		snake.stream().filter(c -> snake.indexOf(c) < snake.size() - 2)
					  .forEach(c -> steppedSnake
					  .add(new Component(c.getLogicBoardIndex())));
		steppedSnake.forEach(c -> c.setColor(SnakeComponentColor.YELLOW));
		steppedSnake.add(stepRight(snake.get(snake.size() - 1)));
		return steppedSnake;
	}
	
	

	private Component stepDirect(Component snakeComponent) {

		Direction actual = snakeComponent.getActualDirection();

		if (actual == Direction.NORTH) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x() - 1, snakeComponent.getViewBoard_y());

		} else if (actual == Direction.EAST) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x(), snakeComponent.getViewBoard_y() + 1);

		} else if (actual == Direction.SOUTH) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x() + 1, snakeComponent.getViewBoard_y());

		} else if (actual == Direction.WEST) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x(), snakeComponent.getViewBoard_y() - 1);
		}

		return snakeComponent;
	}

	private Component stepLeft(Component snakeComponent) {

		Direction actual = snakeComponent.getActualDirection();

		if (actual == Direction.NORTH) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x(), snakeComponent.getViewBoard_y() - 1);
			snakeComponent.setActualDirection(Direction.WEST);

		} else if (actual == Direction.EAST) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x() - 1, snakeComponent.getViewBoard_y());
			snakeComponent.setActualDirection(Direction.NORTH);
		} else if (actual == Direction.SOUTH) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x(), snakeComponent.getViewBoard_y() + 1);
			snakeComponent.setActualDirection(Direction.EAST);
		} else if (actual == Direction.WEST) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x() + 1, snakeComponent.getViewBoard_y());
			snakeComponent.setActualDirection(Direction.SOUTH);
		}

		return snakeComponent;
	}

	private Component stepRight(Component snakeComponent) {

		Direction actual = snakeComponent.getActualDirection();

		if (actual == Direction.NORTH) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x(), snakeComponent.getViewBoard_y() + 1);
			snakeComponent.setActualDirection(Direction.EAST);
		} else if (actual == Direction.EAST) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x() + 1, snakeComponent.getViewBoard_y());
			snakeComponent.setActualDirection(Direction.SOUTH);
		} else if (actual == Direction.SOUTH) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x(), snakeComponent.getViewBoard_y() - 1);
			snakeComponent.setActualDirection(Direction.WEST);
		} else if (actual == Direction.WEST) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x() - 1, snakeComponent.getViewBoard_y());
			snakeComponent.setActualDirection(Direction.NORTH);
		}

		return snakeComponent;
	}



	@Override
	public boolean canGoDirect(List<Component> snake) {
		
		Component snakeHead = snake.get(snake.size() - 1);
		
		if (snakeHead.getActualDirection() == Direction.NORTH && snakeHead.getViewBoard_x()  <  1) {
			return false;

		} else if (snakeHead.getActualDirection() == Direction.EAST && snakeHead.getViewBoard_y() >= Config.BOARD_COLS - 1) {
			return false;
			
		} else if (snakeHead.getActualDirection() == Direction.SOUTH && snakeHead.getViewBoard_x() >= Config.BOARD_ROWS - 1) {
			return false;
			
		} else if (snakeHead.getActualDirection() == Direction.WEST && snakeHead.getViewBoard_y() < 1) {
			return false;
		}
	
		return true;
	}



	@Override
	public boolean canTurnLeft(List<Component> snake) {
		
			Component snakeHead = snake.get(snake.size() - 1);
		
		if (snakeHead.getActualDirection() == Direction.NORTH && snakeHead.getViewBoard_y()  < 1) {
			return false;

		} else if (snakeHead.getActualDirection() == Direction.EAST && snakeHead.getViewBoard_x() < 1) {
			return false;
			
		} else if (snakeHead.getActualDirection() == Direction.SOUTH && snakeHead.getViewBoard_y() >=  Config.BOARD_COLS - 1) {
			return false;
			
		} else if (snakeHead.getActualDirection() == Direction.WEST && snakeHead.getViewBoard_x() >= Config.BOARD_ROWS - 1) {
			return false;
		}
		
		
		return true;
	}



	@Override
	public boolean canTurnRight(List<Component> snake) {

		
		Component snakeHead = snake.get(snake.size() - 1);
		
		if (snakeHead.getActualDirection() == Direction.NORTH && snakeHead.getViewBoard_y()  >= Config.BOARD_COLS - 1) {
			return false;

		} else if (snakeHead.getActualDirection() == Direction.EAST && snakeHead.getViewBoard_x() >= Config.BOARD_ROWS - 1) {
			return false;
			
		} else if (snakeHead.getActualDirection() == Direction.SOUTH && snakeHead.getViewBoard_y() < 1) {
			return false;
			
		} else if (snakeHead.getActualDirection() == Direction.WEST && snakeHead.getViewBoard_x() < 1) {
			return false;
		}
		

		return true;
	}



	@Override
	public boolean isSnakeBittenByItself(List<Component> snake) {
		
		List<Component> alterSnake = new ArrayList<>(snake);
		
		Component snakeHead = alterSnake.get(snake.size() - 1);
		alterSnake.remove(snake.size() - 1);
		if(alterSnake.contains(snakeHead)) {
			return true;
		}
			
		return false;
	}



	@Override
	public List<Component> createBarrierComponentStore(List<Component> snake) {
		
		return null;
	}



	@Override
	public int calcScore(List<Component> snake) {
		
		return 0;
	}



	@Override
	public String calcLevel(List<Component> snake) {
		
		return null;
	}



	@Override
	public int getTempo(List<Component> snake) {
		
		return 1000;
	}

	
}
