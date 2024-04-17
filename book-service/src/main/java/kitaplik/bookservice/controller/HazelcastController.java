package kitaplik.bookservice.controller;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/hazelcast")
public class HazelcastController {

    private final HazelcastInstance hazelcastInstance;

    public HazelcastController(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @PostMapping
    public String addHazelcastMap(@RequestParam("key") String key, @RequestBody Object value) {
        Map<Object, Object> hazelcastMap = hazelcastInstance.getMap("hazelcastMap");
        hazelcastMap.put(key, value);
        return "Added key = " + key;
    }

    @GetMapping
    public Object getHazelcastMap(@RequestParam("key") String key) {
        Map<Object, Object> hazelcastMap = hazelcastInstance.getMap("hazelcastMap");
        return hazelcastMap.get(key);
    }

    @DeleteMapping
    public String removeHazelcastMap(@RequestParam("key") String key) {
        Map<String, Object> hazelcastMap = hazelcastInstance.getMap("hazelcastMap");
        hazelcastMap.remove(key);
        return "Remove key = " + key;
    }
}