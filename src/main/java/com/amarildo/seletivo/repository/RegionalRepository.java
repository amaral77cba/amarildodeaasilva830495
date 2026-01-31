package com.amarildo.seletivo.repository;

import com.amarildo.seletivo.model.Regional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegionalRepository extends JpaRepository<Regional, Integer> {

    List<Regional> findByAtivoTrueOrderByNomeAsc();

    Optional<Regional> findByIdExternoAndAtivoTrue(Integer idExterno);

    List<Regional> findByIdExternoNotInAndAtivoTrue(List<Integer> idsExternos);
}

