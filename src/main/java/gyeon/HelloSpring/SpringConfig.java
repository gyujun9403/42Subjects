package gyeon.HelloSpring;

import gyeon.HelloSpring.repository.MemoryMemberRepository;
import gyeon.HelloSpring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    @Bean
    // 해당 객체를
    public MemoryMemberRepository memoryMemberRepository() {
        return new MemoryMemberRepository();
    }
    @Bean
    public MemberService memberService() {
        return new MemberService(memoryMemberRepository());
    }
}
