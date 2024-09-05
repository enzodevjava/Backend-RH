package com.appRh.appRh.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.appRh.appRh.model.Vaga;

public interface VagaRepository extends CrudRepository<Vaga, String>{
    Vaga findByCodigo(long codigo);
    List<Vaga> findByNome(String nome);
}
