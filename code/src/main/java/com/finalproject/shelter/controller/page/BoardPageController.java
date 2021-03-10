package com.finalproject.shelter.controller.page;

import com.finalproject.shelter.model.entity.Board;
import com.finalproject.shelter.service.Logic.BoardLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/board")
public class BoardPageController {

    @Autowired
    private BoardLogicService boardLogicService;

    @GetMapping("")
    public ModelAndView findboardlist(HttpServletRequest request,
                                  @PageableDefault(size = 10) Pageable pageable,
                                      @RequestParam(value = "select",required = false, defaultValue = "") String select,
                                      @RequestParam(value = "searchText",required = false, defaultValue = "") String searchText){

        String id = request.getParameter("id");

        Page<Board> boardlist = boardLogicService.readAll(id,select,searchText,pageable);

        int startPage = Math.max(1,boardlist.getPageable().getPageNumber() -4);
        int endPage = Math.min(boardlist.getTotalPages(),boardlist.getPageable().getPageNumber()+4);

        return new ModelAndView("/pages/list")
                .addObject("boardlist",boardlist)
                .addObject("startPage",startPage)
                .addObject("endPage",endPage)
                .addObject("eachboard",boardLogicService.readCategory(id));
    }

    @GetMapping("/view")
    public ModelAndView listview(HttpServletRequest request){
        String id = request.getParameter("id");

        return new ModelAndView("/pages/view")
                .addObject("eachboard",boardLogicService.readBoard(id));
    }

    @GetMapping("/form")
    public ModelAndView writeview(HttpServletRequest request,@RequestParam("name") String name,
                                  @RequestParam(required = false,defaultValue = "0")String boardid){

        String id = request.getParameter("id");

        Board board = boardLogicService.readBoard(id);

        Board board1 = Board.builder()
                .category(board.getCategory())
                .build();

        if (name.equals("write")){
            return new ModelAndView("pages/form")
                    .addObject("eachboard",board)
                    .addObject("board",board1);
        }else{
            return new ModelAndView("pages/form")
                    .addObject("eachboard",board)
                    .addObject("board",boardLogicService.readBoard(id));
        }
    }

    @PostMapping("/form")
    public String postform(@ModelAttribute Board board){
        Board newboard = boardLogicService.postservice(board);
        return "redirect:/board/view?id=" + newboard.getId();
    }

    @GetMapping("/delete")
    public String deleteboard(HttpServletRequest request){
        String ids = request.getParameter("id");

        String id = boardLogicService.deleteid(ids);

        return "redirect:/board?id="+id+"&page=0";
    }
}