package com.jishop.repository;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    default Notice findOne(Long id){
        return findById(id).orElseThrow(()->new DomainException(ErrorType.NOTICE_NOT_FOUND));
    }

    // 공지사항 전체 목록 시 중요도에 따라 정렬
        // 중요도는 긴급(EMERGENCY) > 중요(IMPORTANT) > 일반(NORMAL)순서로 정렬
        // 긴급은 작성 날짜와 관계없이 일반, 중요 보다 항상 상단에 고정
        // 동일한 중요도 내에서는 최신 작성 순으로 정렬
    @Query("SELECT n FROM Notice n " +
            "ORDER BY " +
            "CASE n.priority " +
            "    WHEN 'EMERGENCY' THEN 3 " +
            "    WHEN 'IMPORTANT' THEN 2 " +
            "    WHEN 'NORMAL' THEN 1 " +
            "    ELSE 0 " +
            "END DESC, " +
            "n.createdAt DESC")
    Page<Notice> findAll(Pageable pageable);
}
