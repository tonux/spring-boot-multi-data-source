package com.ca.formation.configurations;

import com.zaxxer.hikari.HikariConfig;

import java.util.Properties;

public class HikariConfigWrite extends HikariConfig {

  protected final HikariWriteProperties hikariWriteProperties;

  protected final String PERSISTENCE_UNIT_NAME = "write";

  protected final Properties JPA_WRITE_PROPERTIES = new Properties() {
    {
      put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
      put("hibernate.hbm2ddl.auto", "update");
      put("hibernate.ddl-auto", "update");
      put("show-sql", "true");
    }
  };

  protected HikariConfigWrite(HikariWriteProperties hikariWriteProperties) {
    this.hikariWriteProperties = hikariWriteProperties;
    setPoolName(this.hikariWriteProperties.getPoolName());
    setMinimumIdle(this.hikariWriteProperties.getMinimumIdle());
    setMaximumPoolSize(this.hikariWriteProperties.getMaximumPoolSize());
    setIdleTimeout(this.hikariWriteProperties.getIdleTimeout());
  }
}
