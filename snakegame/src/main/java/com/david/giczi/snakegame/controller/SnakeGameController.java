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
		model.addAttribute("boardcols", Config.BOARD_COLS);
		model.addAttribute("boardrows", Config.BOARD_ROWS);
		List<Component> snake = service.createSnake(Config.SNAKE_LENGTH);
		request.getSession().setAttribute("snake", snake);
		model.addAttribute("infotext", "Snake Game");
		model.addAttribute("board", service.addSnakeToBoardComponent(snake));
		model.addAttribute("tempo", service.getTempo(snake));
		
		return "gameboard";
	}
	
	@RequestMapping("/Snake/goDirect")
	public String stepDirect(Model model, HttpServletRequest request) {
			
		model.addAttribute("boardcols", Config.BOARD_COLS);
		model.addAttribute("boardrows", Config.BOARD_ROWS);
		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");
		
		if(service.canGoDirect(snake)) {
			snake = service.goDirect(snake);
			request.getSession().setAttribute("snake", snake);
		}
		else {
			model.addAttribute("theEnd", "Vége a játéknak, szeretnél újat játszani?");
		}	
		
		model.addAttribute("infotext", "Snake Game");
		model.addAttribute("board", service.addSnakeToBoardComponent(snake));
		model.addAttribute("tempo", service.getTempo(snake));
		
		return "gameboard";
	}
	
	@RequestMapping("/Snake/turnLeft")
	public String stepLeft(Model model, HttpServletRequest request) {
			
		model.addAttribute("boardcols", Config.BOARD_COLS);
		model.addAttribute("boardrows", Config.BOARD_ROWS);
		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");

		if(service.canTurnLeft(snake)) {
			snake = service.turnLeft(snake);
			request.getSession().setAttribute("snake", snake);
		}
		else {
			model.addAttribute("theEnd", "Vége a játéknak, szeretnél újat játszani?");
		}	
		
		model.addAttribute("infotext", "Snake Game");
		model.addAttribute("board", service.addSnakeToBoardComponent(snake));
		model.addAttribute("tempo", service.getTempo(snake));
		
		return "gameboard";
	}
	
	
	@RequestMapping("/Snake/turnRight")
	public String stepRight(Model model, HttpServletRequest request) {
			
		model.addAttribute("boardcols", Config.BOARD_COLS);
		model.addAttribute("boardrows", Config.BOARD_ROWS);
		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");
		if(service.canTurnRight(snake)) {
			snake = service.turnRight(snake);
			request.getSession().setAttribute("snake", snake);
		}
		else {
			model.addAttribute("theEnd", "Vége a játéknak, szeretnél újat játszani?");
		}
		
		model.addAttribute("infotext", "Snake Game");
		model.addAttribute("board", service.addSnakeToBoardComponent(snake));
		model.addAttribute("tempo", service.getTempo(snake));
		
		
		return "gameboard";
	}
	
}
