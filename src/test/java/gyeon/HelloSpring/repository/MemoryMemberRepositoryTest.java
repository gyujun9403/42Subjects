package gyeon.HelloSpring.repository;

import gyeon.HelloSpring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

// 관례적으로, 테스트가 포한되는 패키지의 이름은 동일하고,
// 테스트하고자하는 클래스(인터페이스?)의 이름뒤에 Test를 붙이는 방식으로 작성한다.
class MemoryMemberRepositoryTest {

    MemberRepository repository = new MemoryMemberRepository();
    // MemoryMemberRepositoryTest옆의 ▶를 누르면, 내부의 모든 테스트를 실행할 수 있다.

    @AfterEach
    // 각 테스트(메소드?)가 끝날때마다 실핼한 메소드를 나타내는 어노테이션.
    public void afterEach() {
        // 아래의 각 테스트 진행시, 이전 테스트때문에 남아있는 데이터가 이후 테스트에 영향을 미칠수 잇다.
        // 따라서 각 테스트를 독립적으로 만들어주기 위해 테스트할 저장소를 테스트이후 매번 초기화한다.
        repository.clearStore();
    }

    @Test
    // @Test어노테이션을 사용하면, main이 아니더라도 해당 메서드만 실행할 수 잇다.
    // 보면 메서드 옆에 ▶가 생겼다.
    public void save() {
        Member member = new Member();
        member.setName("spring");

        repository.save(member);
        // findByName은 Optional로 반환한다.
        // Optional의 get() 메서드로 바로 값을 얻을수 있다(테스트에서만 자주 쓰는 방식).
        Member result = repository.findByName(member.getName()).get();
        // 아래처럼 테스트해도 되지만 매번 텍스트로 확인할순 없다(?)
        // System.out.println("result = " + (result == member));
        // 그래서 Assertions라는 기능을 제공한다.

        // org.junit.jupiter.api
        Assertions.assertEquals(member, result);
        // org.assertj.core.api, 요즘 많이쓰는 방식이라고 함...
        org.assertj.core.api.Assertions.assertThat(member).isEqualTo(result);
        // 이러한 테스트를 빌드툴 안에 넣어서
        // 이러한 테스트를 통과 못하면 이후 빌드과정이 안되게 막아버리게 할수도 있음
    }

    @Test
    public void findByName() {
        Member member1 = new Member();
        member1.setName("Spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("Spring2");
        repository.save(member2);

        Member result = repository.findByName("Spring1").get();
        org.assertj.core.api.Assertions.assertThat(result).isEqualTo(member1);
        // org.assertj.core.api.Assertions.assertThat(result).isEqualTo(member2);
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();
        org.assertj.core.api.Assertions.assertThat(result.size()).isEqualTo(2);
    }
}
