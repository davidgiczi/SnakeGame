package com.david.giczi.snakegame.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.david.giczi.snakegame.config.Config;
import com.david.giczi.snakegame.domain.Component;
import com.david.giczi.snakegame.service.SnakeGameService;

@Controller
public class SnakeGameController {

	private SnakeGameService service;

	@Autowired
	public void setService(SnakeGameService service) {
		this.service = service;
	}

	@RequestMapping("/Snake")
	public String initGame(Model model, HttpServletRequest request) {

		request.getSession().invalidate();
		service.setGameBoardParam();

		List<Component> snake = service.createSnakeComponentStore(Config.SNAKE_LENGTH);
		request.getSession().setAttribute("snake", snake);
		List<Component> edibleStore = service.createEdibleComponentStore(snake);
		request.getSession().setAttribute("edible", edibleStore);
		List<Component> barrierStore = service.createBarrierComponentStore(snake, edibleStore);
		request.getSession().setAttribute("barrier", barrierStore);

		model.addAttribute("boardcols", Config.BOARD_COLS);
		model.addAttribute("boardrows", Config.BOARD_ROWS);
		model.addAttribute("board", createBoard(request));
		model.addAttribute("level", service.calcLevel(snake));
		model.addAttribute("score", service.calcScore(snake));
		model.addAttribute("tempo", service.getTempo(snake));

		return "gameboard";
	}

	@RequestMapping("/Snake/goDirect")
	public String goDirect(Model model, HttpServletRequest request) {

		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");
		@SuppressWarnings("unchecked")
		List<Component> edibleStore = (List<Component>) request.getSession().getAttribute("edible");
		@SuppressWarnings("unchecked")
		List<Component> barrierStore = (List<Component>) request.getSession().getAttribute("barrier");

		if (service.isComponentMeeting(snake, edibleStore)) {

			service.eating(request);

		} else if (edibleStore.isEmpty()) {

			createNewEdibleAndBarrierComponentStore(snake, request);

		} else {

			if (service.canGoDirect(snake) && !service.isSnakeBittenByItself(snake)
					&& !service.isComponentMeeting(snake, barrierStore)) {

				snake = service.goDirect(snake);
				request.getSession().setAttribute("snake", snake);
			} else {

				model.addAttribute("theEnd", "BIG-BANG! Szeretnél új játékot játszani?");
			}

		}

		model.addAttribute("boardcols", Config.BOARD_COLS);
		model.addAttribute("boardrows", Config.BOARD_ROWS);
		model.addAttribute("board", createBoard(request));
		model.addAttribute("level", service.calcLevel(snake));
		model.addAttribute("score", service.calcScore(snake));
		model.addAttribute("tempo", service.getTempo(snake));

		return "gameboard";
	}

	@RequestMapping("/Snake/turnLeft")
	public String stepLeft(Model model, HttpServletRequest request) {

		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");
		@SuppressWarnings("unchecked")
		List<Component> edibleStore = (List<Component>) request.getSession().getAttribute("edible");
		@SuppressWarnings("unchecked")
		List<Component> barrierStore = (List<Component>) request.getSession().getAttribute("barrier");

		if (service.isComponentMeeting(snake, edibleStore)) {

			service.eating(request);

		} else if (edibleStore.isEmpty()) {

			createNewEdibleAndBarrierComponentStore(snake, request);

		} else {

			if (service.canTurnLeft(snake) && !service.isSnakeBittenByItself(snake)
					&& !service.isComponentMeeting(snake, barrierStore)) {

				snake = service.turnLeft(snake);
				request.getSession().setAttribute("snake", snake);
			} else {
				model.addAttribute("theEnd", "BIG-BANG! Szeretnél új játékot játszani?");
			}
		}
		model.addAttribute("boardcols", Config.BOARD_COLS);
		model.addAttribute("boardrows", Config.BOARD_ROWS);
		model.addAttribute("board", createBoard(request));
		model.addAttribute("level", service.calcLevel(snake));
		model.addAttribute("score", service.calcScore(snake));
		model.addAttribute("tempo", service.getTempo(snake));

		return "gameboard";
	}

	@RequestMapping("/Snake/turnRight")
	public String stepRight(Model model, HttpServletRequest request) {

		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");
		@SuppressWarnings("unchecked")
		List<Component> edibleStore = (List<Component>) request.getSession().getAttribute("edible");
		@SuppressWarnings("unchecked")
		List<Component> barrierStore = (List<Component>) request.getSession().getAttribute("barrier");

		if (service.isComponentMeeting(snake, edibleStore)) {

			service.eating(request);

		} else if (edibleStore.isEmpty()) {

			createNewEdibleAndBarrierComponentStore(snake, request);
			
		} else {

			if (service.canTurnRight(snake) && !service.isSnakeBittenByItself(snake)
					&& !service.isComponentMeeting(snake, barrierStore)) {

				snake = service.turnRight(snake);
				request.getSession().setAttribute("snake", snake);
			} else {
				model.addAttribute("theEnd", "BIG-BANG! Szeretnél új játékot játszani?");
			}

		}

		model.addAttribute("boardcols", Config.BOARD_COLS);
		model.addAttribute("boardrows", Config.BOARD_ROWS);
		model.addAttribute("board", createBoard(request));
		model.addAttribute("level", service.calcLevel(snake));
		model.addAttribute("score", service.calcScore(snake));
		model.addAttribute("tempo", service.getTempo(snake));

		return "gameboard";
	}

	private List<Component> createBoard(HttpServletRequest request) {

		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");
		@SuppressWarnings("unchecked")
		List<Component> edibleStore = (List<Component>) request.getSession().getAttribute("edible");
		@SuppressWarnings("unchecked")
		List<Component> barrierStore = (List<Component>) request.getSession().getAttribute("barrier");

		List<Component> board = service.createBoardComponentStore();
		List<Component> boardWithEdible = service.addComponentStoreToBoardComponentStore(board, edibleStore);
		List<Component> boardWithdEdibleAndBarrier = service.addComponentStoreToBoardComponentStore(boardWithEdible,
				barrierStore);
		List<Component> boardWithEdibleAndBarrierAndSnake = service
				.addComponentStoreToBoardComponentStore(boardWithdEdibleAndBarrier, snake);

		return boardWithEdibleAndBarrierAndSnake;
	}

	private void createNewEdibleAndBarrierComponentStore(List<Component> snake, HttpServletRequest request) {
		
		List<Component> newEdibleStore = service.createEdibleComponentStore(snake);
		List<Component> newBarrierStore = service.createBarrierComponentStore(snake, newEdibleStore);
		request.getSession().setAttribute("edible", newEdibleStore);
		request.getSession().setAttribute("barrier", newBarrierStore);

		
	}
}
