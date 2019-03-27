package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            insertRoles(user);
        } else {
            if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
                return null;
            }
            // Simplest implementation.
            // More complicated : get user roles from DB and compare them with user.roles (assume that roles are changed rarely).
            // If roles are changed, calculate difference in java and delete/insert them.
            deleteRoles(user);
            insertRoles(user);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return setRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return setRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public List<User> getAll() {
        Map<Integer, Set<Role>> map = new HashMap<>();
        jdbcTemplate.query("SELECT * FROM user_roles", resultSet -> {
            map.computeIfAbsent(resultSet.getInt("user_id"), integer ->
                    EnumSet.noneOf(Role.class)).add(Role.valueOf(resultSet.getString("role")));
        });
        List<User> userList = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        userList.forEach(user -> user.setRoles(map.get(user.getId())));
        return userList;
    }

    private User setRoles(User user) {
        if (user != null) {
            List<Role> roles = jdbcTemplate.query("SELECT role FROM user_roles  WHERE user_id=?",
                    (resultSet, i) -> Role.valueOf(resultSet.getString("role")), user.getId());
            user.setRoles(roles);
        }
        return user;
    }

    private void insertRoles(User user) {
        Set<Role> roles = user.getRoles();
        if (!CollectionUtils.isEmpty(roles)) {
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", roles, roles.size(),
                    (preparedStatement, roleUser) -> {
                        preparedStatement.setInt(1, user.getId());
                        preparedStatement.setString(2, roleUser.name());
                    });
        }
    }

    private void deleteRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
    }
}

