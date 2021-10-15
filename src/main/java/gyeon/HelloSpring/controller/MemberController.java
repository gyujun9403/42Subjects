package gyeon.HelloSpring.controller;

import gyeon.HelloSpring.domain.Member;
import gyeon.HelloSpring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
// @Controller어노테이션을 넣으면, 스프링은 해당 클래스의 객체를 만들어서 따로 관리를 한다.
// 이렇게 스프링 컨테이너에서 따로 관리되는 객체를 빈이라고 한다.
// 빈에는 컨트롤러 뿐만이 아니라 service, repository등이 있다.
public class MemberController {

    private final MemberService memberService;

    @Autowired
    // defendency injection. 빈으로 관리되는 MemberService객체에 연결시켜준다(의존관계 주입).
    // @Autowired어노테이션이 있는 메소드에 연결되는 객체는 '빈'이여야 한다.
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 멤버를 등록하는 폼으로 이동시켜주는 메소드.
    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);

        // 회원 정보 입력 후 홈 화면으로 리다이렉트
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.listUpMembers();
        model.addAttribute("members", members);
        return "members/memberlist";
    }
}
