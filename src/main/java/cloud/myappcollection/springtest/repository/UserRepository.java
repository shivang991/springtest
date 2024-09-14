package cloud.myappcollection.springtest.repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import cloud.myappcollection.springtest.model.User;

@Repository
public class UserRepository {
    private final JdbcClient jdbcClient;

    UserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<User> getUserByName(String name) {
        return jdbcClient
                .sql("SELECT * FROM USERS WHERE NAME = :name")
                .param("name", name)
                .query(User.class)
                .optional();
    }

    public int count() {
        return jdbcClient
                .sql("SELECT count(*) FROM USERS")
                .query(Integer.class)
                .single()
                .intValue();

    }

    public void insert(User[] users) {
        if (users.length == 0) {
            return;
        }

        ArrayList<String> params = new ArrayList<>();
        ArrayList<String> runnerArrList = new ArrayList<>();

        for (User user : users) {
            params.add(user.name());
            params.add(user.password());
            runnerArrList.add("(?,?)");
        }

        String statement = String.format("INSERT INTO USERS(NAME, PASSWORD) VALUES %s",
                String.join(",", runnerArrList));

        int updated = jdbcClient.sql(statement).params(params).update();
        Assert.state(updated > 0, "Failed to insert users");
    }

}
