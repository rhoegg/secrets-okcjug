package org.okcjug;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/members")
public class MembersResource {

    @Inject
    RedisMemberService memberService;

    @POST
    public Uni<Member> add(Member member) {
        return memberService.create(member);
    }

    @GET
    public Multi<Member> list() {
        return memberService.list();
    }
}
