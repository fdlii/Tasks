package com.yourcompany.task_14;

import com.yourcompany.task_13.DAOs.BookDAO;
import com.yourcompany.task_13.DAOs.ClientDAO;
import com.yourcompany.task_13.DAOs.OrderDAO;
import com.yourcompany.task_13.DAOs.RequestDAO;
import com.yourcompany.task_3_4.BookStore;
import com.yourcompany.task_3_4.FileManager;
import com.yourcompany.task_3_4.interfaces.IBookStore;
import com.yourcompany.task_3_4.interfaces.IFileManager;
import com.yourcompany.task_3_4.mappers.BookMapper;
import com.yourcompany.task_3_4.mappers.ClientMapper;
import com.yourcompany.task_3_4.mappers.OrderMapper;
import com.yourcompany.task_3_4.mappers.RequestMapper;
import com.yourcompany.task_5_1.MenuController;
import com.yourcompany.task_5_1.interfaces.IMenuController;
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
@EnableTransactionManagement
@PropertySource("classpath:database.properties")
@ComponentScan("com.yourcompany")
public class ApplicationConfig {

    @Value("${db.driver}")
    private String driverClass;

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${hibernate.dialect}")
    private String dialect;

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
    public OrderDAO orderDAO() {
        return new OrderDAO();
    }

    @Bean
    public RequestDAO requestDAO() {
        return new RequestDAO();
    }

    @Bean
    public ClientMapper clientMapper() {
        return new ClientMapper();
    }

    @Bean
    public BookMapper bookMapper() {
        return new BookMapper();
    }

    @Bean
    public OrderMapper orderMapper() {
        return new OrderMapper();
    }

    @Bean
    public RequestMapper requestMapper() {
        return new RequestMapper();
    }

    @Bean
    public IBookStore bookStore() {
        return new BookStore();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(driverClass);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sf = new LocalSessionFactoryBean();
        sf.setDataSource(dataSource);

        sf.setPackagesToScan("com.yourcompany.task_13.entities");

        Properties props = new Properties();
        props.put("hibernate.dialect", dialect);
        props.put("hibernate.show_sql", "${hibernate.show_sql:false}");
        props.put("hibernate.format_sql", "${hibernate.format_sql:false}");
        props.put("hibernate.hbm2ddl.auto", "${hibernate.hbm2ddl.auto:none}");

        sf.setHibernateProperties(props);
        return sf;
    }

    @Bean
    public HibernateTransactionManager transactionManager(
            LocalSessionFactoryBean sessionFactory) {
        return new HibernateTransactionManager(sessionFactory.getObject());
    }
}
