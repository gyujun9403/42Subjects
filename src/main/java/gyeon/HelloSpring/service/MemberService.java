package gyeon.HelloSpring.service;

import gyeon.HelloSpring.domain.Member;
import gyeon.HelloSpring.repository.MemberRepository;
import gyeon.HelloSpring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Optional;

//@Service
public class MemberService {

    private final MemberRepository memberRepository;

//    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    /*
     * 회원 가입
     */
    public  Long join(Member member) {
        // 규칙 -> 이름이 중복되는 회원은 불가.

        checkValidMember(member);   // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void checkValidMember(Member member) {
        // Optional을 값을 감쌌기 때문에 사용할수 있는 메소드.
        // NULL값이 존재할 수 있다면 Oprional로 감싸준다.
        // 내부에 NULL이 아닌 어떤 값이 있다면, 예외처리를 하는 람다식.
        // get메소드로 바로 꺼낼수는 있는만, 권장하지 않음.
        memberRepository.findByName(member.getName()).ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원.");
        });
    }

    public List<Member> listUpMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
