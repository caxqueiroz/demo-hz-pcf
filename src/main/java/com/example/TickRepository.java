package com.example;

import com.example.model.Tick;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.PagingPredicate;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cq on 11/2/17.
 */
@Component
public class TickRepository {

    private HazelcastInstance hazelcastInstance;

    @Autowired
    public TickRepository(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    public void save(Tick tick){
        ticks().put(tick.getTime(),tick);
    }

    public long count(){
        return ticks().size();
    }

    public Tick findOne(Long id){
        return ticks().get(id);
    }

    private IMap<Long,Tick> ticks(){
        return hazelcastInstance.getMap("ticks");
    }

    public List<Tick> findAll(String instrument, int pageSize, int pageNumber){
        Predicate<Long,Tick> predicate = Predicates.equal("instrument",instrument);
        PagingPredicate<Long,Tick> pagingPredicate = new PagingPredicate<>(predicate,pageSize);
        List<Tick> ticks = new ArrayList<>();
        for (Tick tick : ticks().values(pagingPredicate)) {
            ticks.add(tick);
        }
        return ticks;


    }

}
