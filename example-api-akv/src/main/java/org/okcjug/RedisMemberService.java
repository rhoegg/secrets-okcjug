package org.okcjug;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.hash.ReactiveHashCommands;
import io.quarkus.redis.datasource.set.ReactiveSetCommands;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Date;

@ApplicationScoped
public class RedisMemberService {
    public static final String MEMBERID_KEY = "memberid";
    public static final String MEMBER_KEYPREFIX = "member:";
    public static final String MEMBERNAME_KEY = "name";
    public static final String MEMBERJUG_KEY = "firstjug";

    private final ReactiveSetCommands<String, Long> longSetCommands;
    private final ReactiveHashCommands<String, String, Long> longFieldCommands;
    private final ReactiveHashCommands<String, String, String> stringFieldCommands;
    private final ReactiveHashCommands<String, String, Date> dateFieldCommands;
    private final ReactiveValueCommands<String, Long> counterCommands;

    @ConfigProperty(name = "redis_password")
    String secret;

    public RedisMemberService(ReactiveRedisDataSource ds) {
        longSetCommands = ds.set(Long.class);
        longFieldCommands = ds.hash(Long.class);
        stringFieldCommands = ds.hash(String.class);
        dateFieldCommands = ds.hash(Date.class);
        counterCommands = ds.value(Long.class);
    }

    public Uni<Member> create(Member member) {
        return counterCommands.incrby(MEMBERID_KEY, 1)
                .onItem().transformToUni(id -> create(id, member));
    }

    public Multi<Member> list() {
        return longSetCommands.smembers(memberIdListKey())
                .onItem().transformToMulti(ids -> Multi.createFrom().iterable(ids))
                .onItem().transformToUniAndConcatenate(this::getMember);
    }

    private Uni<Member> create(long id, Member member) {
        final String key = memberKey(id);
        member.id = id;
        return longSetCommands.sadd(memberIdListKey(), id)
                .chain(last -> longFieldCommands.hset(key, MEMBERID_KEY, id))
                .chain(last -> stringFieldCommands.hset(key, MEMBERNAME_KEY, member.name))
                .chain(last -> dateFieldCommands.hset(key, MEMBERJUG_KEY, member.firstJug))
                .onItem().transform(last -> member);
    }

    private Uni<Member> getMember(long id) {
        final String key = memberKey(id);
        Member m = new Member();
        m.id = id;
        return stringFieldCommands.hget(key, MEMBERNAME_KEY)
                .chain(name -> {
                    m.name = name;
                    return dateFieldCommands.hget(key, MEMBERJUG_KEY);
                })
                .onItem().transform(jugDate -> {
                    m.firstJug = jugDate;
                    return m;
                });
    }

    private String memberKey(long id) {
        return MEMBER_KEYPREFIX + id;
    }
    private String memberIdListKey() {
        return "ids:" + MEMBERID_KEY;
    }
}
