package eu.virtusdevelops.simplesync.db.repositories;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import eu.virtusdevelops.simplesync.models.BasicProperties;
import eu.virtusdevelops.simplesync.models.Properties;

import java.util.*;



public abstract class Repository<T, K> {


	public Repository(Properties<BasicProperties> properties, HikariDataSource dataSource){
		//hikari = openConnecton(properties);
		this.hikari = dataSource;
		create(properties.getString("table_prefix"));
	}

	HikariDataSource hikari;



	abstract Optional<T> save(T obj);

	abstract Optional<T> findById(K key);

	abstract void delete(T obj);

	abstract void create(String prefix);

	public void close(){
		hikari.close();
	}



}
