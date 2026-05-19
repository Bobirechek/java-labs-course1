package managers;

import models.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class DatabaseManager {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.getUrl(),
                DatabaseConfig.USER,
                DatabaseConfig.PASSWORD
        );
    }


    public void initSchema() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id            SERIAL PRIMARY KEY,
                    login         VARCHAR(255) UNIQUE NOT NULL,
                    password_hash VARCHAR(255) NOT NULL
                );

                CREATE SEQUENCE IF NOT EXISTS human_being_seq START WITH 1 INCREMENT BY 1;

                CREATE TABLE IF NOT EXISTS human_beings (
                    id               BIGINT PRIMARY KEY DEFAULT nextval('human_being_seq'),
                    name             VARCHAR(255)  NOT NULL,
                    coord_x          BIGINT        NOT NULL,
                    coord_y          BIGINT        NOT NULL,
                    creation_date    TIMESTAMP     NOT NULL,
                    real_hero        BOOLEAN       NOT NULL,
                    has_toothpick    BOOLEAN,
                    impact_speed     DOUBLE PRECISION NOT NULL,
                    soundtrack_name  VARCHAR(255)  NOT NULL,
                    weapon_type      VARCHAR(50),
                    mood             VARCHAR(50),
                    car_name         VARCHAR(255),
                    car_cool         BOOLEAN,
                    owner_login      VARCHAR(255)  NOT NULL REFERENCES users(login)
                );
                """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    
    public boolean registerUser(String login, String passwordHash) throws SQLException {
        String sql = "INSERT INTO users (login, password_hash) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, passwordHash);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean authenticateUser(String login, String passwordHash) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE login = ? AND password_hash = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, passwordHash);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean userExists(String login) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE login = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    
    public List<HumanBeing> loadAll() throws SQLException {
        String sql = "SELECT * FROM human_beings ORDER BY id";
        List<HumanBeing> result = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }
        return result;
    }

    public long addHumanBeing(HumanBeing h, String ownerLogin) throws SQLException {
        String sql = """
                INSERT INTO human_beings
                    (name, coord_x, coord_y, creation_date, real_hero, has_toothpick,
                     impact_speed, soundtrack_name, weapon_type, mood, car_name, car_cool, owner_login)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, h.getName());
            stmt.setLong(2, h.getCoordinates().getX());
            stmt.setLong(3, h.getCoordinates().getY());
            stmt.setTimestamp(4, Timestamp.valueOf(h.getCreationDate()));
            stmt.setBoolean(5, h.isRealHero());

            if (h.getHasToothpick() != null) stmt.setBoolean(6, h.getHasToothpick());
            else stmt.setNull(6, Types.BOOLEAN);

            stmt.setDouble(7, h.getImpactSpeed());
            stmt.setString(8, h.getSoundtrackName());

            stmt.setString(9, h.getWeaponType() != null ? h.getWeaponType().name() : null);
            stmt.setString(10, h.getMood() != null ? h.getMood().name() : null);

            if (h.getCar() != null && !h.getCar().getName().isEmpty()) {
                stmt.setString(11, h.getCar().getName());
                if (h.getCar().getCool() != null) stmt.setBoolean(12, h.getCar().getCool());
                else stmt.setNull(12, Types.BOOLEAN);
            } else {
                stmt.setNull(11, Types.VARCHAR);
                stmt.setNull(12, Types.BOOLEAN);
            }

            stmt.setString(13, ownerLogin);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getLong("id");
            }
        }
    }

    public boolean removeById(long id, String ownerLogin) throws SQLException {
        String sql = "DELETE FROM human_beings WHERE id = ? AND owner_login = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.setString(2, ownerLogin);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean existsById(long id) throws SQLException {
        String sql = "SELECT 1 FROM human_beings WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean updateHumanBeing(HumanBeing h, String ownerLogin) throws SQLException {
        String sql = """
                UPDATE human_beings SET
                    name = ?, coord_x = ?, coord_y = ?, real_hero = ?, has_toothpick = ?,
                    impact_speed = ?, soundtrack_name = ?, weapon_type = ?, mood = ?,
                    car_name = ?, car_cool = ?
                WHERE id = ? AND owner_login = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, h.getName());
            stmt.setLong(2, h.getCoordinates().getX());
            stmt.setLong(3, h.getCoordinates().getY());
            stmt.setBoolean(4, h.isRealHero());

            if (h.getHasToothpick() != null) stmt.setBoolean(5, h.getHasToothpick());
            else stmt.setNull(5, Types.BOOLEAN);

            stmt.setDouble(6, h.getImpactSpeed());
            stmt.setString(7, h.getSoundtrackName());
            stmt.setString(8, h.getWeaponType() != null ? h.getWeaponType().name() : null);
            stmt.setString(9, h.getMood() != null ? h.getMood().name() : null);

            if (h.getCar() != null && !h.getCar().getName().isEmpty()) {
                stmt.setString(10, h.getCar().getName());
                if (h.getCar().getCool() != null) stmt.setBoolean(11, h.getCar().getCool());
                else stmt.setNull(11, Types.BOOLEAN);
            } else {
                stmt.setNull(10, Types.VARCHAR);
                stmt.setNull(11, Types.BOOLEAN);
            }

            stmt.setLong(12, h.getId());
            stmt.setString(13, ownerLogin);

            return stmt.executeUpdate() > 0;
        }
    }

    public int clearByOwner(String ownerLogin) throws SQLException {
        String sql = "DELETE FROM human_beings WHERE owner_login = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ownerLogin);
            return stmt.executeUpdate();
        }
    }

    public int removeGreaterByOwner(HumanBeing ref, String ownerLogin) throws SQLException {
        String sql = """
                DELETE FROM human_beings
                WHERE owner_login = ?
                  AND (coord_x > ? OR (coord_x = ? AND coord_y > ?))
                """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ownerLogin);
            stmt.setLong(2, ref.getCoordinates().getX());
            stmt.setLong(3, ref.getCoordinates().getX());
            stmt.setLong(4, ref.getCoordinates().getY());
            return stmt.executeUpdate();
        }
    }


    private HumanBeing mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        long coordX = rs.getLong("coord_x");
        long coordY = rs.getLong("coord_y");
        LocalDateTime creationDate = rs.getTimestamp("creation_date").toLocalDateTime();
        boolean realHero = rs.getBoolean("real_hero");

        Boolean hasToothpick = rs.getObject("has_toothpick") != null
                ? rs.getBoolean("has_toothpick") : null;

        double impactSpeed = rs.getDouble("impact_speed");
        String soundtrackName = rs.getString("soundtrack_name");

        String weaponTypeStr = rs.getString("weapon_type");
        WeaponType weaponType = weaponTypeStr != null ? WeaponType.valueOf(weaponTypeStr) : null;

        String moodStr = rs.getString("mood");
        Mood mood = moodStr != null ? Mood.valueOf(moodStr) : null;

        String carName = rs.getString("car_name");
        Car car = null;
        if (carName != null) {
            Boolean carCool = rs.getObject("car_cool") != null ? rs.getBoolean("car_cool") : null;
            car = new Car(carName, carCool);
        }

        String ownerLogin = rs.getString("owner_login");

        HumanBeing h = new HumanBeing.Builder()
                .id(id)
                .name(name)
                .coordinates(new Coordinates(coordX, coordY))
                .creationDate(creationDate)
                .realHero(realHero)
                .hasToothpick(hasToothpick)
                .impactSpeed(impactSpeed)
                .soundtrackName(soundtrackName)
                .weaponType(weaponType)
                .mood(mood)
                .car(car)
                .build();

        h.setOwnerLogin(ownerLogin);
        return h;
    }
}