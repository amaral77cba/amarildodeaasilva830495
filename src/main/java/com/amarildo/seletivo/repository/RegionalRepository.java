package com.amarildo.seletivo.repository;

import com.amarildo.seletivo.model.Regional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegionalRepository extends JpaRepository<Regional, Integer> {
    List<Regional> findByIdNotIn(List<Integer> ids);

    List<Regional> findByAtivoTrueOrderByNomeAsc();
}

