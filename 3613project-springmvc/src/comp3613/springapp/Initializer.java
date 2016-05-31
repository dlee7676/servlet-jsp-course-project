package comp3613.springapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import comp3613.springapp.db.OldJDBC;

public class Initializer implements ServletContextListener {
	@Autowired
	private ServletContext sc;
	private static OldJDBC dbAccess;
	private static Properties dbProps;
	private static SessionFactory sessionFactory;
	
	public static OldJDBC getDao() {
		return dbAccess;
	}
	
	public static Properties getProps() {
		return dbProps;
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}
		catch (Throwable ex) {
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
		
		dbAccess = new OldJDBC();
		dbProps = new Properties();
		String propertiesPath = "/WEB-INF/resources/dbprops.properties";
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		/*try {
			FileInputStream fis = new FileInputStream(new File(propertiesPath));
			dbProps.load(fis);
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Class.forName(dbProps.getProperty("com.microsoft.sqlserver.jdbc.SQLServerDriver"));
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}*/
	}
}
