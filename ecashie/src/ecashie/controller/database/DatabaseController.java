package ecashie.controller.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DatabaseController
{
	private static DatabaseController instance;

	static
	{
		instance = new DatabaseController();
	}

	public static DatabaseController getInstance()
	{
		return instance;
	}

	private SessionFactory sessionFactory;
	private Session session;

	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	
	public Session getSession()
	{
		return session;
	}

	public void setSession(Session session)
	{
		this.session = session;
	}
	
	protected void setup()
	{
		final StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
		try
		{
			sessionFactory = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();

			session = sessionFactory.openSession();
			session.beginTransaction();
		}
		catch (Exception e)
		{
			StandardServiceRegistryBuilder.destroy(serviceRegistry);
		}
	}
}
