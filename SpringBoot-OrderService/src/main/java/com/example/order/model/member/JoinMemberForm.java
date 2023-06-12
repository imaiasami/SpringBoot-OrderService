package com.example.order.model.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class JoinMemberForm {
    @Size(min = 4, max = 20)
    private String member_id;
    @Size(min = 4, max = 20)
    private String password;
    @NotBlank
    private String name;

    public static Member toMember(JoinMemberForm joinMemberForm) {
        Member member = new Member();
        member.setMember_id(joinMemberForm.getMember_id());
        member.setPassword(joinMemberForm.getPassword());
        member.setName(joinMemberForm.getName());
        return member;
    }
}
