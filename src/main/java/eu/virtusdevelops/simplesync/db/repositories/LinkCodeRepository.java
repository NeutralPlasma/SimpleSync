package eu.virtusdevelops.simplesync.db.repositories;

import eu.virtusdevelops.simplesync.db.entity.LinkCode;
import eu.virtusdevelops.simplesync.models.BasicProperties;
import eu.virtusdevelops.simplesync.models.Properties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;


public class LinkCodeRepository extends Repository<LinkCode, Long> {

    String prefix;

     public LinkCodeRepository(Properties<BasicProperties> properties, String prefix) {
         super(properties, prefix);
         this.prefix = properties.getString("table_prefix");
    }

    @Override
    public Optional<LinkCode> save(LinkCode linkCode) {
        Objects.requireNonNull(linkCode, "LinkCode cannot be null");

        try(Connection connection = hikari.getConnection()){
            if(linkCode.getID() == -1){
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO " + this.prefix + "LinkCode(discordUser, code, expireTimeStamp) VALUES(?, ?, ?)"
                );
                statement.setLong(1, linkCode.getDiscordUser());
                statement.setString(2, linkCode.getCode());
                statement.setLong(3, linkCode.getExpireTimeStamp());
                statement.executeUpdate();

                ResultSet set = statement.getGeneratedKeys();
                while(set.next()){
                    linkCode.setID(set.getLong("ID"));
                }
                return Optional.of(linkCode);
            }else{
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE " + this.prefix + " LinkCode SET discordUser=?, code=?, expireTimeStamp=? WHERE ID=?"
                );
                statement.setLong(1, linkCode.getDiscordUser());
                statement.setString(2, linkCode.getCode());
                statement.setLong(3, linkCode.getExpireTimeStamp());
                statement.setLong(4, linkCode.getID());
                statement.execute();
                return Optional.of(linkCode);
            }


        }catch (SQLException e){
            return Optional.empty();
        }


    }

    @Override
    public Optional<LinkCode> findById(Long key) {
        try(Connection connection = hikari.getConnection()){

                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM " + this.prefix + "LinkCode WHERE ID = ?"
                );
                statement.setLong(1, key);
                ResultSet set = statement.executeQuery();
                LinkCode code = new LinkCode();
                if(set.next()){
                    code.setID(set.getLong("ID"));
                    code.setCode(set.getString("code"));
                    code.setDiscordUser(set.getLong("discordUser"));
                    code.setExpireTimeStamp(set.getLong("expireTimeStamp"));
                    return Optional.of(code);
                }
        }catch (SQLException e){
            return Optional.empty();
        }
        return Optional.empty();
    }


    public Optional<LinkCode> findByDiscordUser(Long userID) {
        try(Connection connection = hikari.getConnection()){

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM " + this.prefix + "LinkCode WHERE discordUser = ?"
            );
            statement.setLong(1, userID);
            ResultSet set = statement.executeQuery();
            LinkCode code = new LinkCode();
            if(set.next()){
                code.setID(set.getLong("ID"));
                code.setCode(set.getString("code"));
                code.setDiscordUser(set.getLong("discordUser"));
                code.setExpireTimeStamp(set.getLong("expireTimeStamp"));
                return Optional.of(code);
            }
        }catch (SQLException e){
            return Optional.empty();
        }
        return Optional.empty();

    }

    public Optional<LinkCode> findByCode(String code) {
        try(Connection connection = hikari.getConnection()){

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM " + this.prefix + "LinkCode WHERE code = ?"
            );
            statement.setString(1, code);
            ResultSet set = statement.executeQuery();
            LinkCode linkCode = new LinkCode();
            if(set.next()){
                linkCode.setID(set.getLong("ID"));
                linkCode.setCode(set.getString("code"));
                linkCode.setDiscordUser(set.getLong("discordUser"));
                linkCode.setExpireTimeStamp(set.getLong("expireTimeStamp"));
                return Optional.of(linkCode);
            }
        }catch (SQLException e){
            return Optional.empty();
        }
        return Optional.empty();

    }


    @Override
    public void delete(LinkCode obj) {
        try(Connection connection = hikari.getConnection()){

            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM " + this.prefix + "LinkCode WHERE ID = ?"
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
                    "CREATE TABLE IF NOT EXISTS " + prefix + "LinkCode( " +
                            "ID bigint PRIMARY KEY AUTO_INCREMENT, " +
                            "discordUser LONG NOT NULL, " +
                            "code VARCHAR(10) NOT NULL, " +
                            "expireTimeStamp LONG NOT NULL );"
            );
            statement.execute();
        }catch (SQLException e){
            // ERROR
            e.printStackTrace();
        }
    }
}
