package com.example.order.controller;

import com.example.order.model.member.JoinMemberForm;
import com.example.order.model.member.LoginForm;
import com.example.order.model.member.Member;
import com.example.order.repository.MemberRepository;
import com.example.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("member")
@Controller
public class MemberController {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    /**
     * 회원가입 페이지 이동
     * @param model
     * @return member/joinForm.html
     */
    @GetMapping("join")
    public String joinForm(Model model) {
        model.addAttribute("joinForm", new JoinMemberForm());
        return "member/joinForm";
    }

    /**
     * 회원가입
     * @param joinForm
     * @param bindingResult
     * @return
     */
    @PostMapping("join")
    public String join(@Validated @ModelAttribute("joinForm") JoinMemberForm joinForm, BindingResult bindingResult) {
        log.info("joinForm: {} ", joinForm);

        if (bindingResult.hasErrors()) {
            log.info("bindingResult: {}", bindingResult);
            return "member/joinForm";
        }

        // 동일한 아이디로 가입된 회원정보가 있는지 검색
        Member member = memberRepository.findMemberById(joinForm.getMember_id());
        if (member != null) {
            bindingResult.rejectValue("member_id","idDuplicateError", "이미 사용중인 아이디 입니다.");
            log.info("bindingResult: {}", bindingResult);
            return "member/joinForm";
        }

        // 회원정보를 저장한다.
        memberRepository.saveMember(JoinMemberForm.toMember(joinForm));

        return "redirect:/";
    }

    /**
     * 로그인 페이지 이동
     * @param model
     * @return
     */
    @GetMapping("login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "member/loginForm";
    }

    /**
     * 로그인
     * @param loginForm
     * @param bindingResult
     * @param request
     * @return
     */
    @PostMapping("login")
    public String login(@Validated @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult,
                        HttpServletRequest request) {
        log.info("loginForm: {}", loginForm);

        if (bindingResult.hasErrors()) {
            return "member/loginForm";
        }

        Member member = memberRepository.findMemberById(loginForm.getMember_id());
        if (member == null || !member.getPassword().equals(loginForm.getPassword())) {
            bindingResult.reject("loginError", "아이디가 없거나 패스워드가 다릅니다.");
            return "member/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginMember", member);
        return "redirect:/";
    }

    /**
     * 로그아웃
     * @param request
     * @return
     */
    @GetMapping("logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }

}
