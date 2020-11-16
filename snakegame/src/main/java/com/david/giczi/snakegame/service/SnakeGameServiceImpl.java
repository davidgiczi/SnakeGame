package com.david.giczi.snakegame.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.david.giczi.snakegame.config.Config;
import com.david.giczi.snakegame.config.GameParam;
import com.david.giczi.snakegame.domain.Component;
import com.david.giczi.snakegame.utils.Direction;
import com.david.giczi.snakegame.utils.ComponentColor;

@Service
public class SnakeGameServiceImpl implements SnakeGameService, ComponentColor {

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
		Config.EDIBLE_NUMBER = param.getEdible_number();
		Config.BARRIER_NUMBER = param.getBarrier_number();

	}

	@Override
	public List<Component> createBoardComponentStore() {

		List<Component> board = new ArrayList<>();

		for (int i = 0; i < Config.BOARD_ROWS * Config.BOARD_COLS; i++) {

			board.add(new Component(i));
		}

		return board;
	}

	@Override
	public List<Component> createSnakeComponentStore(int length) {

		List<Component> snake = new ArrayList<>();

		if (0 > length || length > Config.BOARD_ROWS * Config.BOARD_COLS) {
			return snake;
		}

		for (int i = 0; i < length - 1; i++) {
			Component snakeBody = new Component(i);
			snakeBody.setColor(ComponentColor.YELLOW);
			snake.add(snakeBody);
		}

		Component snakeHead = new Component(length - 1);
		snakeHead.setActualDirection(Direction.EAST);
		snakeHead.setColor(ComponentColor.RED);
		snake.add(snakeHead);

		return snake;
	}

	@Override
	public List<Component> addComponentStoreToBoardComponentStore(List<Component> board,
			List<Component> componentStore) {

		for (Component boardComponent : board) {
			for (Component storeComponent : componentStore) {

				if (boardComponent.equals(storeComponent)) {

					boardComponent.setColor(storeComponent.getColor());
				}

			}
		}

		return board;
	}

	@Override
	public List<Component> goDirect(List<Component> snake) {

		List<Component> steppedSnake = new ArrayList<>();
		snake.stream().filter(c -> snake.indexOf(c) > 0)
				.forEach(c -> steppedSnake.add(new Component(c.getLogicBoardIndex())));
		steppedSnake.forEach(c -> c.setColor(ComponentColor.YELLOW));
		steppedSnake.add(stepDirect((snake.get(snake.size() - 1))));

		return steppedSnake;
	}

	@Override
	public List<Component> turnLeft(List<Component> snake) {

		List<Component> steppedSnake = new ArrayList<>();
		snake.stream().filter(c -> snake.indexOf(c) > 0)
				.forEach(c -> steppedSnake.add(new Component(c.getLogicBoardIndex())));
		steppedSnake.forEach(c -> c.setColor(ComponentColor.YELLOW));
		steppedSnake.add(stepLeft(snake.get(snake.size() - 1)));
		return steppedSnake;
	}

	@Override
	public List<Component> turnRight(List<Component> snake) {

		List<Component> steppedSnake = new ArrayList<>();
		snake.stream().filter(c -> snake.indexOf(c) > 0)
				.forEach(c -> steppedSnake.add(new Component(c.getLogicBoardIndex())));
		steppedSnake.forEach(c -> c.setColor(ComponentColor.YELLOW));
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

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x(), snakeComponent.getViewBoard_y() - 1);
			snakeComponent.setActualDirection(Direction.WEST);
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

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x(), snakeComponent.getViewBoard_y() + 1);
			snakeComponent.setActualDirection(Direction.EAST);
		} else if (actual == Direction.WEST) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x() - 1, snakeComponent.getViewBoard_y());
			snakeComponent.setActualDirection(Direction.NORTH);
		}

		return snakeComponent;
	}

	@Override
	public boolean canGoDirect(List<Component> snake) {

		Component snakeHead = snake.get(snake.size() - 1);

		if (snakeHead.getActualDirection() == Direction.NORTH && snakeHead.getViewBoard_x() < 1) {
			return false;

		} else if (snakeHead.getActualDirection() == Direction.EAST
				&& snakeHead.getViewBoard_y() >= Config.BOARD_COLS - 1) {
			return false;

		} else if (snakeHead.getActualDirection() == Direction.SOUTH
				&& snakeHead.getViewBoard_x() >= Config.BOARD_ROWS - 1) {
			return false;

		} else if (snakeHead.getActualDirection() == Direction.WEST && snakeHead.getViewBoard_y() < 1) {
			return false;
		}

		return true;
	}

	@Override
	public boolean canTurnLeft(List<Component> snake) {

		Component snakeHead = snake.get(snake.size() - 1);

		if (snakeHead.getActualDirection() == Direction.NORTH && snakeHead.getViewBoard_y() < 1) {
			return false;

		} else if (snakeHead.getActualDirection() == Direction.EAST && snakeHead.getViewBoard_x() < 1) {
			return false;

		} else if (snakeHead.getActualDirection() == Direction.SOUTH
				&& snakeHead.getViewBoard_y() < 1) {
			return false;

		} else if (snakeHead.getActualDirection() == Direction.WEST
				&& snakeHead.getViewBoard_x() >= Config.BOARD_ROWS - 1) {
			return false;
		}

		return true;
	}

	@Override
	public boolean canTurnRight(List<Component> snake) {

		Component snakeHead = snake.get(snake.size() - 1);

		if (snakeHead.getActualDirection() == Direction.NORTH && snakeHead.getViewBoard_y() >= Config.BOARD_COLS - 1) {
			return false;

		} else if (snakeHead.getActualDirection() == Direction.EAST
				&& snakeHead.getViewBoard_x() >= Config.BOARD_ROWS - 1) {
			return false;

		} else if (snakeHead.getActualDirection() == Direction.SOUTH && snakeHead.getViewBoard_y() >= Config.BOARD_COLS - 1) {
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
		if (alterSnake.contains(snakeHead)) {
			return true;
		}

		return false;
	}

	@Override
	public List<Component> createEdibleComponentStore(List<Component> snake, List<Component> barrierStore) {

		Component snakeHead = snake.get(snake.size() - 1);
		List<Component> store = new ArrayList<>();

		while (store.size() < Config.EDIBLE_NUMBER) {

			int x = (int) (Math.random() * Config.BOARD_ROWS);
			int y = (int) (Math.random() * Config.BOARD_COLS);

			Component component = new Component(x, y);

			if (x != snakeHead.getViewBoard_x() && y != snakeHead.getViewBoard_y() && !snake.contains(component) && !store.contains(component)
					&& !barrierStore.contains(component) && !isEdibleComponentSurroundedByMoreThanOneBarriers(barrierStore, component)) {

				component.setColor(ComponentColor.GREEN);

				store.add(component);
			}

		}

		return store;
	}

	private boolean isEdibleComponentSurroundedByMoreThanOneBarriers(List<Component> barrierStore,
			Component component) {

		int numberOfBarriersNextToEdible = 0;

		if(barrierStore.contains(new Component(component.getViewBoard_x() + 1, component.getViewBoard_y()))) {
			numberOfBarriersNextToEdible++;
		}
		if(barrierStore.contains(new Component(component.getViewBoard_x() - 1, component.getViewBoard_y()))) {
			numberOfBarriersNextToEdible++;
		}
		if(barrierStore.contains(new Component(component.getViewBoard_x(), component.getViewBoard_y() + 1))) {
			numberOfBarriersNextToEdible++;
		}
		if(barrierStore.contains(new Component(component.getViewBoard_x(), component.getViewBoard_y() - 1))) {
			numberOfBarriersNextToEdible++;
		}

		if (numberOfBarriersNextToEdible > 1) {
			return true;
		}

		return false;
	}

	@Override
	public List<Component> createBarrierComponentStore(List<Component> snake) {

		Component snakeHead = snake.get(snake.size() - 1);
		List<Component> store = new ArrayList<>();

		while (store.size() < Config.BARRIER_NUMBER) {

			int x = (int) (Math.random() * Config.BOARD_ROWS);
			int y = (int) (Math.random() * Config.BOARD_COLS);

			Component component = new Component(x, y);

			if (x != snakeHead.getViewBoard_x() && y != snakeHead.getViewBoard_y()
					&& !snake.contains(component) && !store.contains(component)) {

				component.setColor(ComponentColor.BROWN);

				store.add(component);
			}

		}

		return store;
	}

	@Override
	public int calcScore(List<Component> snake) {

		int score = 0;

		for (int i = 0; i < snake.size() - Config.SNAKE_LENGTH; i++) {

			if (i < Config.EDIBLE_NUMBER) {
				score += 10;
			} else if (Config.EDIBLE_NUMBER <= i && i < 2 * Config.EDIBLE_NUMBER) {
				score += 20;
			} else if (2 * Config.EDIBLE_NUMBER <= i && i < 3 * Config.EDIBLE_NUMBER) {
				score += 30;
			} else if (3 * Config.EDIBLE_NUMBER <= i && i < 4 * Config.EDIBLE_NUMBER) {
				score += 40;
			} else if (4 * Config.EDIBLE_NUMBER <= i && i < 5 * Config.EDIBLE_NUMBER) {
				score += 50;
			} else if (5 * Config.EDIBLE_NUMBER <= i && i < 6 * Config.EDIBLE_NUMBER) {
				score += 60;
			} else if (6 * Config.EDIBLE_NUMBER <= i && i < 7 * Config.EDIBLE_NUMBER) {
				score += 70;
			} else if (7 * Config.EDIBLE_NUMBER <= i && i < 8 * Config.EDIBLE_NUMBER) {
				score += 80;
			} else if (8 * Config.EDIBLE_NUMBER <= i && i < 9 * Config.EDIBLE_NUMBER) {
				score += 90;
			} else {
				score += 100;
			}
		}

		return score;
	}

	@Override
	public String calcLevel(List<Component> snake) {

		int netSnakeLength = snake.size() - Config.SNAKE_LENGTH;

		if (0 <= netSnakeLength && netSnakeLength < Config.EDIBLE_NUMBER) {
			return "I.";
		} else if (Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 2 * Config.EDIBLE_NUMBER) {
			return "II.";
		} else if (2 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 3 * Config.EDIBLE_NUMBER) {
			return "III.";
		} else if (3 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 4 * Config.EDIBLE_NUMBER) {
			return "IV.";
		} else if (4 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 5 * Config.EDIBLE_NUMBER) {
			return "V.";
		} else if (5 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 6 * Config.EDIBLE_NUMBER) {
			return "VI.";
		} else if (6 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 7 * Config.EDIBLE_NUMBER) {
			return "VII.";
		} else if (7 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 8 * Config.EDIBLE_NUMBER) {
			return "VIII.";
		} else if (8 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 9 * Config.EDIBLE_NUMBER) {
			return "IX.";
		} else

			return "X.";
	}

	@Override
	public int getTempo(List<Component> snake) {

		int netSnakeLength = snake.size() - Config.SNAKE_LENGTH;

		if (0 <= netSnakeLength && netSnakeLength < Config.EDIBLE_NUMBER) {
			return 1000;
		} else if (Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 2 * Config.EDIBLE_NUMBER) {
			return 900;
		} else if (2 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 3 * Config.EDIBLE_NUMBER) {
			return 800;
		} else if (3 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 4 * Config.EDIBLE_NUMBER) {
			return 700;
		} else if (4 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 5 * Config.EDIBLE_NUMBER) {
			return 600;
		} else if (5 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 6 * Config.EDIBLE_NUMBER) {
			return 500;
		} else if (6 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 7 * Config.EDIBLE_NUMBER) {
			return 400;
		} else if (7 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 8 * Config.EDIBLE_NUMBER) {
			return 300;
		} else if (8 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 9 * Config.EDIBLE_NUMBER) {
			return 200;
		} else

			return 100;
	}

	@Override
	public boolean isComponentMeeting(List<Component> snake, List<Component> componentStore) {

		Component snakeHead = snake.get(snake.size() - 1);

		if (componentStore.contains(snakeHead)) {
			return true;
		}

		return false;
	}

	@Override
	public void eating(HttpServletRequest request) {

		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");
		@SuppressWarnings("unchecked")
		List<Component> edibleStore = (List<Component>) request.getSession().getAttribute("edible");
		@SuppressWarnings("unchecked")
		List<Component> barrierStore = (List<Component>) request.getSession().getAttribute("barrier");

		Component snakeHead = snake.get(snake.size() - 1);
		edibleStore.remove(snakeHead);
		Component snakeTail = snake.get(0);
		int x = snakeTail.getViewBoard_x();
		int y = snakeTail.getViewBoard_y();
		List<Component> store = new ArrayList<>();
		store.add(new Component(x + 1, y));
		store.add(new Component(x - 1, y));
		store.add(new Component(x, y + 1));
		store.add(new Component(x, y - 1));
		store.forEach(t -> t.setColor(ComponentColor.YELLOW));
		Component plusSnakeTail = store.stream()
				.filter(t -> t.getViewBoard_x() >= 0 && t.getViewBoard_y() >= 0
						&& t.getViewBoard_x() < Config.BOARD_ROWS && t.getViewBoard_y() < Config.BOARD_COLS
						&& !snake.contains(t) && !edibleStore.contains(t) && !barrierStore.contains(t))
				.findFirst().get();
		store = new ArrayList<>(snake);
		snake.clear();
		snake.add(plusSnakeTail);
		snake.addAll(store);

		request.getSession().setAttribute("snake", snake);
		request.getSession().setAttribute("edible", edibleStore);

	}

}
