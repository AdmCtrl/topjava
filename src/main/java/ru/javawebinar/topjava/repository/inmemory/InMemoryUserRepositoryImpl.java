package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.UsersUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {

        UsersUtil.USER_LIST.forEach(this::save);

    }

    @Override
    public boolean delete(int id) {
        log.info("repository size:  " + repository.size() + " id counter = " + counter.get());
        User user = repository.remove(id);
        log.info("delete user " + id);
        log.info("repository size:  " + repository.size() + " id counter = " + counter.get());
        log.info("delete {}", id);
        return user != null;
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            log.info("set id to new user: {}", user.getId());
        }
        log.info("repository size: {} id counter: {}", repository.size(), counter.get());
        return repository.put(user.getId(), user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll users");

        return repository.values()
                .stream()
                .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return repository.values()
                .parallelStream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}
