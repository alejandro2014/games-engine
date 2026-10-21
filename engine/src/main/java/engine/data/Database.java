package engine.data;

import java.lang.System.Logger.Level;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Database implements AutoCloseable {
    private static final System.Logger LOG = System.getLogger(Database.class.getName());

    private final Connection connection;

    public Database(String url) throws SQLException {
        connection = DriverManager.getConnection(url);
        LOG.log(Level.DEBUG, "Connected to {0}", url);
    }

    public static Database sqliteResource(String resourcePath) throws SQLException {
        return new Database("jdbc:sqlite::resource:" + resourcePath);
    }

    public <T> List<T> query(String sql, RowMapper<T> mapper, Object... params) throws SQLException {
        try (var statement = prepare(sql, params); var result = statement.executeQuery()) {
            var rows = new ArrayList<T>();
            while (result.next()) {
                rows.add(mapper.map(result));
            }
            return rows;
        }
    }

    public <T> Optional<T> queryFirst(String sql, RowMapper<T> mapper, Object... params) throws SQLException {
        try (var statement = prepare(sql, params); var result = statement.executeQuery()) {
            return result.next() ? Optional.ofNullable(mapper.map(result)) : Optional.empty();
        }
    }

    public int queryInt(String sql, Object... params) throws SQLException {
        return queryFirst(sql, row -> row.getInt(1), params)
                .orElseThrow(() -> new SQLException("No result for: " + sql));
    }

    public int update(String sql, Object... params) throws SQLException {
        try (var statement = prepare(sql, params)) {
            return statement.executeUpdate();
        }
    }

    private PreparedStatement prepare(String sql, Object... params) throws SQLException {
        LOG.log(Level.DEBUG, "{0} {1}", sql, List.of(params));

        var statement = connection.prepareStatement(sql);
        for (var i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
        return statement;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
