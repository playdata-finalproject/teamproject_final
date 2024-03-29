package com.finalproject.shelter.presentation.controller.view.board;

import com.finalproject.shelter.domain.model.entity.noticationDomain.Board;
import com.finalproject.shelter.business.service.logic.BoardLogicService;
import com.finalproject.shelter.presentation.settingform.SearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/board")
public class BoardPageController {

    private final BoardLogicService boardLogicService;
    private final SearchForm searchForm;

    private final int pageRange = 4;
    private final int pageFirst = 1;

    public BoardPageController(BoardLogicService boardLogicService, SearchForm searchForm) {
        this.boardLogicService = boardLogicService;
        this.searchForm = searchForm;
    }

    @GetMapping("")
    public String readAll(@RequestParam(value = "getId") String getId,
                          @PageableDefault(size = 10) Pageable pageable,
                          Model model
    ) {
        Page<Board> boards = boardLogicService.findCategorys(getId, pageable);
        Long id = Long.valueOf(getId);

        model.addAttribute("boards", boards);
        model.addAttribute("eachboard", boardLogicService.readCategory(getId));
        model.addAttribute("startPage", Math.max(pageFirst, boards.getPageable().getPageNumber() - pageRange));
        model.addAttribute("endPage", Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + pageRange));
        model.addAttribute("weekview", boardLogicService.bestWeekView(id));
        model.addAttribute("mothview", boardLogicService.bestMonthView(id));

        return "pages/list";
    }

    @GetMapping("/search")
    public String readSearchAll(@RequestParam(value = "getId") String getId,
                                @PageableDefault(size = 10) Pageable pageable,
                                @RequestParam(value = "select", required = false, defaultValue = "") String select,
                                @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
                                Model model
    ) {
        searchForm.DataSet(Long.valueOf(getId), searchText, pageable);
        Page<Board> boards = pageSet(select);
        Long id = Long.valueOf(getId);

        model.addAttribute("boards", boards);
        model.addAttribute("eachboard", boardLogicService.readCategory(getId));
        model.addAttribute("startPage", Math.max(pageFirst, boards.getPageable().getPageNumber() - pageRange));
        model.addAttribute("endPage", Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + pageRange));
        model.addAttribute("weekview", boardLogicService.bestWeekView(id));
        model.addAttribute("mothview", boardLogicService.bestMonthView(id));

        return "pages/list";
    }
    private Page<Board> pageSet(String select){
        searchForm.add(boardLogicService);
        return searchForm.getSearch(select);
    }

    @GetMapping("/delete")
    public String delete(@RequestParam(value = "id") String id) {
        boardLogicService.deleteid(id);
        return "redirect:/main";
    }

}