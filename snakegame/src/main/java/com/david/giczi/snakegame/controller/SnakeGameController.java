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
	public String stepDirect(Model model, HttpServletRequest request) {
				
		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");
		
		if(service.canGoDirect(snake) && !service.isSnakeBittenByItself(snake)) {
			snake = service.goDirect(snake);
			request.getSession().setAttribute("snake", snake);
		}
		else {
			model.addAttribute("theEnd", "Vége a játéknak, szeretnél újat játszani?");
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
		
		if(service.canTurnLeft(snake) && !service.isSnakeBittenByItself(snake)) {
			snake = service.turnLeft(snake);
			request.getSession().setAttribute("snake", snake);
		}
		else {
			model.addAttribute("theEnd", "Vége a játéknak, szeretnél újat játszani?");
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
		
		if(service.canTurnRight(snake) && !service.isSnakeBittenByItself(snake)) {
			snake = service.turnRight(snake);
			request.getSession().setAttribute("snake", snake);
		}
		else {
			model.addAttribute("theEnd", "Vége a játéknak, szeretnél újat játszani?");
		}
		
		model.addAttribute("boardcols", Config.BOARD_COLS);
		model.addAttribute("boardrows", Config.BOARD_ROWS);
		model.addAttribute("board", createBoard(request));
		model.addAttribute("level", service.calcLevel(snake));
		model.addAttribute("score", service.calcScore(snake));
		model.addAttribute("tempo", service.getTempo(snake));
			
		return "gameboard";
	}
	
	private List<Component> createBoard(HttpServletRequest request){
		
		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");
		@SuppressWarnings("unchecked")
		List<Component> edibleStore = (List<Component>) request.getSession().getAttribute("edible");
		@SuppressWarnings("unchecked")
		List<Component> barrierStore = (List<Component>) request.getSession().getAttribute("barrier");
		
		List<Component> board = service.createBoardComponentStore();
		List<Component> boardWithSnake = service.addComponentStoreToBoardComponentStore(board, snake);
		List<Component> boardWithSnakeAndEdible = service.addComponentStoreToBoardComponentStore(boardWithSnake, edibleStore);
		List<Component> boardWithSnakeAndEdibleAndBarrier = service.addComponentStoreToBoardComponentStore(boardWithSnakeAndEdible, barrierStore);
		
		return boardWithSnakeAndEdibleAndBarrier;
	}
}
