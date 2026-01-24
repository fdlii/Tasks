package com.task_14;

import com.task_13.DAOs.BookDAO;
import com.task_13.DAOs.ClientDAO;
import com.task_13.DAOs.OrderDAO;
import com.task_13.DAOs.RequestDAO;
import com.task_13.HibernateConnector;
import com.task_3_4.BookStore;
import com.task_3_4.FileManager;
import com.task_5_1.MenuController;
import com.task_8_2.interfaces.IBookStore;
import com.task_8_2.interfaces.IFileManager;
import com.task_8_2.interfaces.IMenuController;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.hibernate.HibernateTransactionManager;
import org.springframework.orm.jpa.hibernate.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:DatabaseConfig.properties")
@EnableTransactionManagement
@ComponentScan("com")
public class AppConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource(
            @Value("${db.url}") String url,
            @Value("${db.username}") String username,
            @Value("${db.password}") String password,
            @Value("${db.driver}") String driver) {

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driver);
        return ds;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.task_13.entities");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

    @Bean
    public HibernateConnector hibernateConnector(SessionFactory sessionFactory) {
        return new HibernateConnector(sessionFactory);
    }

    private Properties hibernateProperties() {
        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        return props;
    }

    @Bean
    public IMenuController menuController() {
        return new MenuController();
    }

    @Bean
    public IFileManager fileManager() {
        return new FileManager();
    }

    @Bean
    public ClientDAO clientDAO() {
        return new ClientDAO();
    }

    @Bean
    public BookDAO bookDAO() {
        return new BookDAO();
    }

    @Bean
    public OrderDAO orderDAO(ClientDAO clientDAO) {
        return new OrderDAO(clientDAO);
    }

    @Bean
    public RequestDAO requestDAO(BookDAO bookDAO) {
        return new RequestDAO(bookDAO);
    }

    @Bean
    public IBookStore bookStore(
            BookDAO bookDAO,
            OrderDAO orderDAO,
            ClientDAO clientDAO,
            RequestDAO requestDAO,
            IFileManager fileManager
    ) {
        return new BookStore(bookDAO, orderDAO, clientDAO, requestDAO, fileManager);
    }
}
