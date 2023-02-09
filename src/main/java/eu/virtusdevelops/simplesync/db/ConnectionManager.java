package eu.virtusdevelops.simplesync.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import eu.virtusdevelops.simplesync.models.BasicProperties;
import eu.virtusdevelops.simplesync.models.Properties;

public class ConnectionManager {

    public static HikariDataSource openConnecton(Properties<BasicProperties> properties) {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
        config.setPoolName("RepositoryPool");
        config.setMaximumPoolSize(properties.getInt("maxPoolSize"));
        config.setConnectionTimeout(properties.getInt("connectionTimeOut"));
        config.addDataSourceProperty("serverName", properties.getString("address"));
        config.addDataSourceProperty("port", properties.getInt("port"));
        config.addDataSourceProperty("databaseName", properties.getString("database"));
        config.addDataSourceProperty("user", properties.getString("username"));
        config.addDataSourceProperty("password", properties.getString("password"));
        config.addDataSourceProperty("useSSL", properties.getBoolean("useSSL"));

        return new HikariDataSource(config);
    }

}
