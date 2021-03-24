package com.finalproject.shelter.controller.page.board;

import com.finalproject.shelter.model.entity.Answer;
import com.finalproject.shelter.model.entity.Board;
import com.finalproject.shelter.repository.AccountRepository;
import com.finalproject.shelter.service.Logic.AnswerLogicService;
import com.finalproject.shelter.service.Logic.BoardLogicService;
import com.finalproject.shelter.validator.AnswerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/board/view")
public class BoardViewPageController {

    @Autowired
    private BoardLogicService boardLogicService;

    @Autowired
    private AnswerLogicService answerLogicService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AnswerValidator answerValidator;

    @GetMapping("")
    public String listview(
            @RequestParam(value = "id",required = false, defaultValue = "0") String id,
            Model model)
    {

        Board eachboard = boardLogicService.readBoardview(id);
        List<Answer> answerList = answerLogicService.readAnswer(id);
        List<Board> weekview = boardLogicService.bestweekview(String.valueOf(eachboard.getCategory().getId()));
        List<Board> monthview = boardLogicService.bestmonthview(String.valueOf(eachboard.getCategory().getId()));

        Answer answer = answerLogicService.writeuserinfo(eachboard,accountRepository); //Error

        model.addAttribute("eachboard",eachboard);
        model.addAttribute("Answer",answer);
        model.addAttribute("Answers",answerList);
        model.addAttribute("weekview",weekview);
        model.addAttribute("monthview",monthview);

        return "pages/view";
    }

    @PostMapping("/answer")
    public String postanswer(@Valid Answer answer, BindingResult bindingResult, Model model){

        String id = String.valueOf(answer.getBoard().getId());

        if (bindingResult.hasErrors()){
            model.addAttribute("eachboard",answer.getBoard());
            model.addAttribute("Answer",answer);
            model.addAttribute("Answers",answerLogicService.readAnswer(id));

            return "pages/view";
        }

        Answer answer1 = answerLogicService.save(answer);

        return "redirect:/board/view?id=" + answer1.getBoard().getId();
    }
}
