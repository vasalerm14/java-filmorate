package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.Mpa.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class MpaDb implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDb(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public MPA getMPAById(Integer id) {
        String sqlQuery = "SELECT * FROM MPA WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMPA, id);
    }

    public Collection<MPA> getAllMpa() {
        String sql = "SELECT id, name FROM MPA";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            MPA mpa = new MPA();
            mpa.setId(rs.getInt("id"));
            mpa.setName(rs.getString("name"));
            return mpa;
        });
    }

    private MPA mapRowToMPA(ResultSet resultSet, int rowNum) throws SQLException {
        MPA mpa = new MPA();
        mpa.setId(resultSet.getInt("id"));
        mpa.setName(resultSet.getString("name"));
        return mpa;
    }
}
