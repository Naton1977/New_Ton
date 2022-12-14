package com.new_ton.dao;

import com.new_ton.domain.entities.RawEntity;

import java.util.List;
import java.util.Optional;

public interface RawTableDao {
    List<RawEntity> findAllByIdProd(int idProd);

    Optional<RawEntity> findCode8Stage1(int idProd);

    Optional<RawEntity> findCode8Stage2(int idProd);
}
