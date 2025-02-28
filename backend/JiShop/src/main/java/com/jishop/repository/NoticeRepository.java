package com.jishop.repository;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    default Notice findOne(Long id){
        return findById(id).orElseThrow(()->new DomainException(ErrorType.NOTICE_NOT_FOUND));
    }
}
