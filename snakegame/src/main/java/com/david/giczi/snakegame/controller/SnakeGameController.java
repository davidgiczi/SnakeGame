package com.david.giczi.snakegame.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.david.giczi.snakegame.config.Config;
import com.david.giczi.snakegame.domain.Component;
import com.david.giczi.snakegame.service.SnakeGameService;
import com.david.giczi.snakegame.utils.ResponseType;

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
		List<Component> barrierStore = service.createBarrierComponentStore(snake);
		request.getSession().setAttribute("barrier", barrierStore);
		List<Component> edibleStore = service.createEdibleComponentStore(snake, barrierStore);
		request.getSession().setAttribute("edible", edibleStore);

		model.addAttribute("board_cols", Config.BOARD_COLS);
		model.addAttribute("board_rows", Config.BOARD_ROWS);
		model.addAttribute("board", createBoard(request));
		model.addAttribute("score", service.calcScore(snake));
		model.addAttribute("level", service.calcLevel(snake));

		return "gameboard";
	}

	@RequestMapping("/Snake/ajaxRequest")
	public void ajaxResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String userRequest = request.getParameter("usereq");

		switch (userRequest) {

		case "goDirect":
			goDirect(request, response);
			break;
		case "goWest":
			goWest(request, response);
			break;

		case "goNorth":
			goNorth(request, response);
			break;
		case "goEast":
			goEast(request, response);
			break;
		case "goSouth":
			goSouth(request, response);
			break;

		default:
		}

	}

	private void goDirect(HttpServletRequest request, HttpServletResponse response) throws IOException {

		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");
		@SuppressWarnings("unchecked")
		List<Component> edibleStore = (List<Component>) request.getSession().getAttribute("edible");
		@SuppressWarnings("unchecked")
		List<Component> barrierStore = (List<Component>) request.getSession().getAttribute("barrier");

		if (edibleStore.isEmpty()) {

			createNewEdibleAndBarrierComponentStore(snake, request);
			service.createAndSendResponseString(request, response, ResponseType.FOR_NEW_TABLE);

		} else {

			if (service.canGoDirect(snake) && !service.isSnakeBittenByItself(snake)
					&& !service.isComponentMeeting(snake, barrierStore)) {

				if (service.isComponentMeeting(snake, edibleStore)) {

					snake = service.eating(snake, edibleStore);
				}

				snake = service.goDirect(snake);

				request.getSession().setAttribute("snake", snake);
				request.getSession().setAttribute("edible", edibleStore);
				service.createAndSendResponseString(request, response, ResponseType.FOR_STEPPING);

			} else {

				service.createAndSendResponseString(request, response, ResponseType.FOR_THE_END_OF_THE_GAME);

			}

		}

	}

	private void goNorth(HttpServletRequest request, HttpServletResponse response) throws IOException {

		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");
		@SuppressWarnings("unchecked")
		List<Component> edibleStore = (List<Component>) request.getSession().getAttribute("edible");
		@SuppressWarnings("unchecked")
		List<Component> barrierStore = (List<Component>) request.getSession().getAttribute("barrier");

		if (edibleStore.isEmpty()) {

			createNewEdibleAndBarrierComponentStore(snake, request);
			service.createAndSendResponseString(request, response, ResponseType.FOR_NEW_TABLE);

		} else {

			if (service.canGoNorth(snake) && !service.isSnakeBittenByItself(snake)
					&& !service.isComponentMeeting(snake, barrierStore)) {

				if (service.isComponentMeeting(snake, edibleStore)) {
					service.eating(snake, edibleStore);
				}

				snake = service.goNorth(snake);

				request.getSession().setAttribute("snake", snake);
				request.getSession().setAttribute("edible", edibleStore);
				service.createAndSendResponseString(request, response, ResponseType.FOR_STEPPING);

			} else {
				service.createAndSendResponseString(request, response, ResponseType.FOR_THE_END_OF_THE_GAME);

			}
		}

	}

	private void goEast(HttpServletRequest request, HttpServletResponse response) throws IOException {

		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");
		@SuppressWarnings("unchecked")
		List<Component> edibleStore = (List<Component>) request.getSession().getAttribute("edible");
		@SuppressWarnings("unchecked")
		List<Component> barrierStore = (List<Component>) request.getSession().getAttribute("barrier");

		if (edibleStore.isEmpty()) {

			createNewEdibleAndBarrierComponentStore(snake, request);
			service.createAndSendResponseString(request, response, ResponseType.FOR_NEW_TABLE);

		} else {

			if (service.canGoEast(snake) && !service.isSnakeBittenByItself(snake)
					&& !service.isComponentMeeting(snake, barrierStore)) {

				if (service.isComponentMeeting(snake, edibleStore)) {
					service.eating(snake, edibleStore);
				}

				snake = service.goEast(snake);

				request.getSession().setAttribute("snake", snake);
				request.getSession().setAttribute("edible", edibleStore);
				service.createAndSendResponseString(request, response, ResponseType.FOR_STEPPING);

			} else {
				service.createAndSendResponseString(request, response, ResponseType.FOR_THE_END_OF_THE_GAME);

			}

		}

	}

	private void goSouth(HttpServletRequest request, HttpServletResponse response) throws IOException {

		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");
		@SuppressWarnings("unchecked")
		List<Component> edibleStore = (List<Component>) request.getSession().getAttribute("edible");
		@SuppressWarnings("unchecked")
		List<Component> barrierStore = (List<Component>) request.getSession().getAttribute("barrier");

		if (edibleStore.isEmpty()) {

			createNewEdibleAndBarrierComponentStore(snake, request);
			service.createAndSendResponseString(request, response, ResponseType.FOR_NEW_TABLE);

		} else {

			if (service.canGoSouth(snake) && !service.isSnakeBittenByItself(snake)
					&& !service.isComponentMeeting(snake, barrierStore)) {

				if (service.isComponentMeeting(snake, edibleStore)) {

					service.eating(snake, edibleStore);
				}

				snake = service.goSouth(snake);

				request.getSession().setAttribute("snake", snake);
				request.getSession().setAttribute("edible", edibleStore);
				service.createAndSendResponseString(request, response, ResponseType.FOR_STEPPING);

			} else {
				service.createAndSendResponseString(request, response, ResponseType.FOR_THE_END_OF_THE_GAME);
			}

		}

	}

	private void goWest(HttpServletRequest request, HttpServletResponse response) throws IOException {

		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");
		@SuppressWarnings("unchecked")
		List<Component> edibleStore = (List<Component>) request.getSession().getAttribute("edible");
		@SuppressWarnings("unchecked")
		List<Component> barrierStore = (List<Component>) request.getSession().getAttribute("barrier");

		if (edibleStore.isEmpty()) {

			createNewEdibleAndBarrierComponentStore(snake, request);
			service.createAndSendResponseString(request, response, ResponseType.FOR_NEW_TABLE);

		} else {

			if (service.canGoWest(snake) && !service.isSnakeBittenByItself(snake)
					&& !service.isComponentMeeting(snake, barrierStore)) {

				if (service.isComponentMeeting(snake, edibleStore)) {

					service.eating(snake, edibleStore);

				}

				snake = service.goWest(snake);

				request.getSession().setAttribute("snake", snake);
				request.getSession().setAttribute("edible", edibleStore);
				service.createAndSendResponseString(request, response, ResponseType.FOR_STEPPING);

			} else {
				service.createAndSendResponseString(request, response, ResponseType.FOR_THE_END_OF_THE_GAME);
			}

		}

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

		List<Component> newBarrierStore = service.createBarrierComponentStore(snake);
		List<Component> newEdibleStore = service.createEdibleComponentStore(snake, newBarrierStore);
		request.getSession().setAttribute("edible", newEdibleStore);
		request.getSession().setAttribute("barrier", newBarrierStore);

	}

}
