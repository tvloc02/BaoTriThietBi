package com.hethong.baotri.kho_du_lieu.bao_tri;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CanhBaoLoiRepository extends JpaRepository<Object, Long> {
}