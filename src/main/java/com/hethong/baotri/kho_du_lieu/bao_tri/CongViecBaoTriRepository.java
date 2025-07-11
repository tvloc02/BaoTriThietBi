package com.hethong.baotri.kho_du_lieu.bao_tri;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CongViecBaoTriRepository extends JpaRepository<Object, Long> {
    // TODO: Define maintenance task repository methods
}