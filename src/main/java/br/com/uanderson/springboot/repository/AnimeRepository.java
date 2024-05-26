package br.com.uanderson.springboot.repository;

import br.com.uanderson.springboot.domain.Anime;

import java.util.List;

public interface AnimeRepository {
    List<Anime> listAll();
}
