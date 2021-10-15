package gyeon.HelloSpring.service;

import gyeon.HelloSpring.domain.Member;
import gyeon.HelloSpring.repository.MemberRepository;
import gyeon.HelloSpring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// 서비스를 통해 회원가입이 잘 되는지 테스트
class MemberServiceTest {

    // afterEach에서 레포지토리에 접근하여 clear할수 있게 하기 위해 생성자로 받아주게 함.
    MemberService memberService;
    MemoryMemberRepository memoryMemberRepository;

    @BeforeEach
    public void beforeEach() {
        // service입장에서는 외부에서 저장소를 넣어준다
        // 이를 defendency injection, DI라고 한다.?
        // ->하나의 객체가 다른 객체의 의존성을 제공하는 테크닉.
        memoryMemberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memoryMemberRepository);
    }

    @AfterEach
    public void afterEach() {
        memoryMemberRepository.clearStore();
    }

    @Test
    // 테스트코드는 실제코드에 포함되지 않으므로, 직관적으로 한글로적을수도 있음.
    // given, when, then -> 테스트 코드가 길어지면, 테스트에 대해 파악하기 쉬워짐
    void 회원가입() {
        // given -> 주어지는 데이터
        Member member = new Member();
        member.setName("Hello");

        // when -> 설정 상황(조건)
        Long saveId = memberService.join(member);

        // then -> 결과
        Member findedMember = memberService.findOne(saveId).get();
        org.assertj.core.api.Assertions.assertThat(member.getName()).isEqualTo(findedMember.getName());
    }

    @Test
    public void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        Member member2 = new Member();
        member1.setName("foo");
        member2.setName("foo");

        // when
        memberService.join(member1);
        // try catch 넣을수도 있지만 너무 길어짐...
        // assertThrows를 통해 발특정 내용을 수행했을 때, 특정 예외가 발생하는지 확인할 수 있다.
        // assertThrows(발생해야할예외.class, 특정명령)
        // 특정 명령시 발생해야할예외가 발생했다 -> 테스트를 통과한다.
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        // 위에서 받은 에러가 같은 예측되는 경고문을 출력하는지 확인.
        org.assertj.core.api.Assertions
                .assertThat(e.getMessage())
                .isEqualTo("이미 존재하는 회원.");
        // then -> 예외가 터졌으면 잡기.
    }

    @Test
    void listUpMembers() {
    }

    @Test
    void findOne() {
    }
}