package cloud.myappcollection.springtest.runner;

import static java.util.Map.entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class RunnerRepository {
    private final JdbcClient jdbcClient;

    RunnerRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    List<Runner> findAll() {
        return jdbcClient.sql("SELECT * FROM RUNNERS").query(Runner.class).list();
    }

    /**
     * Find a runner by id
     */
    Runner findOne(int id) {
        return jdbcClient
                .sql("SELECT * FROM RUNNERS WHERE ID = :id")
                .param("id", id)
                .query(Runner.class)
                .optional()
                .orElseThrow(RunnerNotFoundException::new);
    }

    /**
     * Insert a single runner into the database
     */
    void create(Runner runner) {
        int updated = jdbcClient
                .sql("INSERT INTO RUNNERS(ID, NAME, DATE) VALUES(?,?,?)")
                .params(List.of(runner.id(), runner.name(), runner.date()))
                .update();
        Assert.state(updated == 1, "Failed to create runner");
    }

    /**
     * Update a given runner specified by id
     * 
     * @param id     id of the runner to update
     * @param runner New runner data
     */
    void update(int id, Runner runner) {
        int updated = jdbcClient.sql("UPDATE RUNNERS SET NAME = :name, DATE = :date WHERE ID = :id")
                .params(Map.ofEntries(
                        entry("name", runner.name()),
                        entry("date", runner.date()),
                        entry("id", id)))
                .update();
        Assert.state(updated == 1, "Failed to update runner");
    }

    /**
     * Delete a runner from database by id
     */
    void delete(int id) {
        int updated = jdbcClient.sql("DELETE FROM RUNNERS WHERE ID = ?").param(id).update();
        Assert.state(updated == 1, "Failed to delete runner");
    }

    /**
     * Insert multiple runners into the database via array
     */
    void insert(Runner[] runners) {
        if (runners.length == 0) {
            return;
        }

        ArrayList<String> params = new ArrayList<>();
        ArrayList<String> runnerArrList = new ArrayList<>();

        for (Runner runner : runners) {
            params.add(runner.name());
            params.add(runner.date().toString());
            runnerArrList.add("(?,?)");
        }

        String statement = String.format("INSERT INTO RUNNERS(NAME, DATE) VALUES %s",
                String.join(",", runnerArrList));

        int updated = jdbcClient.sql(statement).params(params).update();
        Assert.state(updated > 0, "Failed to insert runners");

    }

    /**
     * Get count of total number of runners in the database
     */
    int count() {
        return jdbcClient
                .sql("SELECT count(*) FROM RUNNERS")
                .query(Integer.class)
                .single()
                .intValue();
    }
}
