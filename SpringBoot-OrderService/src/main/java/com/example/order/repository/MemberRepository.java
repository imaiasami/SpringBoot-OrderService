package com.example.order.repository;

import com.example.order.model.member.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberRepository {
    // 회원가입
    void saveMember(Member member);

    // 아이디로 회원정보 검색
    Member findMemberById(String member_id);
}
