package br.com.uanderson.springboot.controller;

import br.com.uanderson.springboot.domain.Anime;
import br.com.uanderson.springboot.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("anime")
@Log4j2
@RequiredArgsConstructor //Cria um construtor com todos os atributos finais de um class
//@AllArgsConstructor//Cria um construtor com todos os atributos não finais de uma class
public class AnimeController {
    private final DateUtil dateUtil;

    @GetMapping(path = "/list")
    public List<Anime> list(){
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return List.of(
                new Anime("Naruto"),
                new Anime("Boruto"),
                new Anime("Boku no Hero Academy")
        );

    }
}
/*
@RestController -> Retorna um corpo contento somente String/Json
@Controller -> Retorna uma pagina html inteira

 */