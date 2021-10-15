package gyeon.HelloSpring.repository;

import gyeon.HelloSpring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

// MemberRepository의 구현 클래스
//@Repository
public class MemoryMemberRepository implements MemberRepository{

    // 실무에서는 동시성 문제 때문에 ConcurrentMap을 사용하지만, 간단한 예제이므로 HaspMap을 사용한다.
    private static Map<Long, Member> store = new HashMap<>();
    // 실무에서는 동시성 문제 때문에 AtomicLong을 사용하지만, 간단한 예제이므로 그냥 Long을 사용한다.
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        // id에 적합한 이름이 없으면 null을 반환한다.
        // 이때 null이 반환될 가능성이 있으므로 Optional.ofNullable()로 감싸준다면,
        // 클라이언트에서 뭔갈 할수 있다고 한다...?
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        // 람다식. 반복문을 돌면서 파라미터를 통해 넘어온 name과 같은 객체가 있다면 반환.
        // 없으면 optional에 null이 포함되서 반환돤다.
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        // 자바 실무에서 리스트 자주사용.
        // Map형태인 store에 저장된 데이터들을 ArrayList에 담아서 반환.
        return new ArrayList<>(store.values());
    }

    @Override
    public void clearStore() {
        store.clear();
    }
}
