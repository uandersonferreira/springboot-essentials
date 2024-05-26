package br.com.uanderson.springboot.controller;

import br.com.uanderson.springboot.domain.Anime;
import br.com.uanderson.springboot.service.AnimeService;
import br.com.uanderson.springboot.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("animes")//normalmente declarado no plural
@Log4j2
@RequiredArgsConstructor //Cria um construtor com todos os atributos finais de um class
//@AllArgsConstructor//Cria um construtor com todos os atributos não finais de uma class
public class AnimeController {
    private final DateUtil dateUtil;
    private final AnimeService animeService;

    @GetMapping()
    public ResponseEntity<List<Anime>> list() {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(animeService.listAll(), HttpStatus.OK);
        //Boas práticas: retornar conteúdo extras, tipo status da request,
        // ao invés de somente o conteúdo solicitado, na resquet.
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable Long id) {
        return ResponseEntity.ok(animeService.findById(id));
        /*
          - @GetMapping(path = "/{id}")
           Quando temos mais de 1 method http (GET) é necessário diferenciá-los por um
          'path' caminho que apronta pra um endpoint /animes/{id}. Nesse exemplo
          estamos usando @PathVariable onde o id faz parte da url

         */
    }


}//class
/*
@RestController -> Retorna um corpo contento somente String/Json
@Controller -> Retorna uma pagina html inteira

 */