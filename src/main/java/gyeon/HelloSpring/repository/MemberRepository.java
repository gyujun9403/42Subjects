package gyeon.HelloSpring.repository;

import gyeon.HelloSpring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    // 요즘은 NULL을 그대로 반환하는 대신 Optional로 감싸서 반환.
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();
    void clearStore();
}
