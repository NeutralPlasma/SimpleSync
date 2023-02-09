package eu.virtusdevelops.simplesync.db.repositories;

import com.zaxxer.hikari.HikariDataSource;
import eu.virtusdevelops.simplesync.db.entity.LinkedPlayer;
import eu.virtusdevelops.simplesync.models.BasicProperties;
import eu.virtusdevelops.simplesync.models.Properties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


public class LinkedPlayerRepository extends Repository<LinkedPlayer, Long> {

    String prefix;
    public LinkedPlayerRepository(Properties<BasicProperties> properties, HikariDataSource hikariDataSource) {
        super(properties, hikariDataSource);
        this.prefix = properties.getString("table_prefix");
    }

    @Override
    public Optional<LinkedPlayer> save(LinkedPlayer linkedPlayer) {
        Objects.requireNonNull(linkedPlayer, "LinkCode cannot be null");

        try(Connection connection = hikari.getConnection()){
            if(linkedPlayer.getID() == -1){
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO " + this.prefix + "LinkedPlayer(discordUser, uuid, username) VALUES(?, ?, ?)"
                );
                statement.setLong(1, linkedPlayer.getDiscordUser());
                statement.setString(2, linkedPlayer.getUserID().toString());
                statement.setString(3, linkedPlayer.getUsername());
                statement.executeUpdate();

                ResultSet set = statement.getGeneratedKeys();
                while(set.next()){
                    linkedPlayer.setID(set.getLong("ID"));
                }
                return Optional.of(linkedPlayer);
            }else{
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE " + this.prefix + "LinkedPlayer SET discordUser=?, uuid=?, username=? WHERE ID=?"
                );
                statement.setLong(1, linkedPlayer.getDiscordUser());
                statement.setString(2, linkedPlayer.getUserID().toString());
                statement.setString(3, linkedPlayer.getUsername());
                statement.setLong(4, linkedPlayer.getID());
                statement.execute();
                return Optional.of(linkedPlayer);
            }


        }catch (SQLException e){
            return Optional.empty();
        }


    }

    @Override
    public Optional<LinkedPlayer> findById(Long key) {
        try(Connection connection = hikari.getConnection()){

                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM " + this.prefix + "LinkedPlayer WHERE ID = ?"
                );
                statement.setLong(1, key);
                ResultSet set = statement.executeQuery();
                LinkedPlayer code = new LinkedPlayer();
                if(set.next()){
                    code.setID(set.getLong("ID"));
                    code.setDiscordUser(set.getLong("discordUser"));
                    code.setUsername(set.getString("username"));
                    code.setUserID(UUID.fromString(set.getString("uuid")));
                    return Optional.of(code);
                }
        }catch (SQLException e){
            return Optional.empty();
        }
        return Optional.empty();
    }


    public Optional<LinkedPlayer> findByDiscordUser(Long userID) {
        try(Connection connection = hikari.getConnection()){

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM " + this.prefix + "LinkedPlayer WHERE discordUser = ?"
            );
            statement.setLong(1, userID);
            ResultSet set = statement.executeQuery();
            LinkedPlayer code = new LinkedPlayer();
            if(set.next()){
                code.setID(set.getLong("ID"));
                code.setDiscordUser(set.getLong("discordUser"));
                code.setUsername(set.getString("username"));
                code.setUserID(UUID.fromString(set.getString("uuid")));
                return Optional.of(code);
            }
        }catch (SQLException e){
            return Optional.empty();
        }
        return Optional.empty();
    }

    public Optional<LinkedPlayer> findByMinecraftID(UUID userID) {
        try(Connection connection = hikari.getConnection()){

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM " + this.prefix + "LinkedPlayer WHERE uuid = ?"
            );
            statement.setString(1, userID.toString());
            ResultSet set = statement.executeQuery();
            LinkedPlayer code = new LinkedPlayer();
            if(set.next()){
                code.setID(set.getLong("ID"));
                code.setDiscordUser(set.getLong("discordUser"));
                code.setUsername(set.getString("username"));
                code.setUserID(UUID.fromString(set.getString("uuid")));
                return Optional.of(code);
            }
        }catch (SQLException e){
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public void delete(LinkedPlayer obj) {
        try(Connection connection = hikari.getConnection()){

            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM " + this.prefix + "LinkedPlayer WHERE ID = ?"
            );
            statement.setLong(1, obj.getID());
            statement.execute();
        }catch (SQLException e){
            // ERROR
            e.printStackTrace();
        }
    }


    @Override
    void create(String prefix) {
        try(Connection connection = hikari.getConnection()){

            PreparedStatement statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS " + prefix +"LinkedPlayer( " +
                            "ID bigint PRIMARY KEY AUTO_INCREMENT, " +
                            "discordUser LONG NOT NULL, " +
                            "uuid VARCHAR(36) NOT NULL, " +
                            "username VARCHAR(48) NOT NULL );"
            );
            statement.execute();
        }catch (SQLException e){
            // ERROR
            e.printStackTrace();
        }
    }
}
