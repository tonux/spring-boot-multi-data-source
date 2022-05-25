# Spring Boot avec Multiple DataSource 

Spring Boot avec Multiple DataSource 

---
### Spring Boot Setup
A partir de https://start.spring.io/ choisir **web**, **data-jpa**, **lombok**, **Mysql**

Une fois que vous avez généré et téléchargé le fichier zip, vous devriez avoir un fichier POM similaire à celui-ci :
```xml

<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
        <exclusions>
            <exclusion>
                <groupId>org.junit.vintage</groupId>
                <artifactId>junit-vintage-engine</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
</dependencies>

```

Pour cette démo, j'utilise HikariDataSource comme bibliothèque de pool de connexion par défaut par Spring Boot. Nous devons avoir 2 DataSource et EntityManager séparés, un pour les écritures (Master/Primary) et un pour les lectures (Slave/Secondary).

```properties

application.properties
spring.datasource-write.jdbc-url=jdbc:mysql://localhost:3306/db_formation
spring.datasource-write.username=root
spring.datasource-write.password=
spring.datasource-write.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource-write.hikari.idle-timeout= 10000
spring.datasource-write.hikari.maximum-pool-size= 10
spring.datasource-write.hikari.minimum-idle= 5
spring.datasource-write.hikari.pool-name= WriteHikariPool

spring.datasource-read.jdbc-url=jdbc:mysql://localhost:3306/db_formation2
spring.datasource-read.username=root
spring.datasource-read.password=
spring.datasource-read.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource-read.hikari.idle-timeout= 10000
spring.datasource-read.hikari.maximum-pool-size= 10
spring.datasource-read.hikari.minimum-idle= 5
spring.datasource-read.hikari.pool-name= ReadHikariPool
```

comme vous le voyez, j'ai 2 sources de données : datasource-write et datasource-read avec leurs propres informations d'identification.

Configurations de DataSource pour WriteDB :
```java
@Configuration
@ConfigurationProperties("spring.datasource-write")
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryWrite",
        transactionManagerRef = "transactionManagerWrite",
        basePackages = {"com.ca.formation.repository.writeRepository"}
)
public class DataSourceConfigWrite extends HikariConfig {

    public final static String PERSISTENCE_UNIT_NAME = "write";

    @Bean
    public HikariDataSource dataSourceWrite() {
        return new HikariDataSource(this);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryWrite(
            final HikariDataSource dataSourceWrite) {

        return new LocalContainerEntityManagerFactoryBean() {{
            setDataSource(dataSourceWrite);
            setPersistenceProviderClass(HibernatePersistenceProvider.class);
            setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
            setPackagesToScan(MODEL_PACKAGE);
            setJpaProperties(JPA_PROPERTIES);
        }};
    }

    @Bean
    public PlatformTransactionManager transactionManagerWrite(EntityManagerFactory entityManagerFactoryWrite) {
        return new JpaTransactionManager(entityManagerFactoryWrite);
    }
}
```

Configurations de DataSource pour ReadDB :

```java
@Configuration
@ConfigurationProperties("spring.datasource-read")
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryRead",
        transactionManagerRef = "transactionManagerRead",
        basePackages = {"com.ca.formation.repository.readRepository"}
)
public class DataSourceConfigRead extends HikariConfig {

    public final static String PERSISTENCE_UNIT_NAME = "read";


    @Bean
    public HikariDataSource dataSourceRead() {
        return new HikariDataSource(this);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryRead(
            final HikariDataSource dataSourceRead) {

        return new LocalContainerEntityManagerFactoryBean() {{
            setDataSource(dataSourceRead);
            setPersistenceProviderClass(HibernatePersistenceProvider.class);
            setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
            setPackagesToScan(MODEL_PACKAGE);
            setJpaProperties(JPA_PROPERTIES);
        }};
    }

    @Bean
    public PlatformTransactionManager transactionManagerRead(EntityManagerFactory entityManagerFactoryRead) {
        return new JpaTransactionManager(entityManagerFactoryRead);
    }
}
```

Les repositories de lecture et d'écriture doivent être dans des paquets séparés. :

  +  Write: ```com.ca.formation.repository.writeRepository```

  +  Read: ```com.ca.formation.repository.readRepository```

que vous devez également définir :
```java
public final static String MODEL_PACKAGE = "com.ca.formation.model";

public final static Properties JPA_PROPERTIES = new Properties() {{
    put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
    put("hibernate.hbm2ddl.auto", "update");
    put("hibernate.ddl-auto", "update");
    put("show-sql", "true");
}};
```
et la logique réelle est dans la couche service :

```java
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerReadRepository customerReadRepository;
    private final CustomerWriteRepository customerWriteRepository;

    public CustomerServiceImpl(CustomerReadRepository customerReadRepository, CustomerWriteRepository customerWriteRepository) {
        this.customerReadRepository = customerReadRepository;
        this.customerWriteRepository = customerWriteRepository;
    }

    public Optional<Customer> getCustomer(Long id) {
        return customerReadRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {

        return customerWriteRepository.save(customer);
    }

    public Customer updateCustomer(Customer customer) {

        return customerWriteRepository.save(customer);
    }
}
```
Maintenant, si vous exécutez cette ligne, vous créez un client dans la DB1 :
```
curl -H "Content-Type: application/json" --request POST --data '{"name":"Michel"}'   http://localhost:8080/customer
```
OR
```
curl -H "Content-Type: application/json" --request PUT --data '{"id":1 , "name":"Michel"}'   http://localhost:8080/customer
```

Mais si vous exécutez cette ligne, vous obtenez des données de DB2 :
```
 curl --request GET  http://localhost:8080/customer/1
```