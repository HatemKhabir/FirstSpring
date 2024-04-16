package org.mik.first.spring.controller.web;


import lombok.extern.log4j.Log4j2;
import org.mik.first.spring.domain.Person;
import org.mik.first.spring.service.CountryService;
import org.mik.first.spring.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;

    private final CountryService countryService;

    @Autowired
    public PersonController(PersonService personService,
                            CountryService countryService) {
        this.countryService=countryService;
        this.personService=personService;
    }

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam("page")Optional<Integer> page,
                       @RequestParam("size")Optional<Integer> size){
        int currentPage=page.orElse(1);
        int pageSize=size.orElse(5);
        Page<Person> result = personService.getAll(PageRequest.of(currentPage-1,pageSize));
        int totalPages=result.getTotalPages();
        if (totalPages>0){
            List<Integer> pageNumber= IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumber", pageNumber);
        }
        model.addAttribute("person", result);
        return "person-list";

    }
}
