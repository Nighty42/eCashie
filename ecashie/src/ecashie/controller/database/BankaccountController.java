package ecashie.controller.database;

import org.hibernate.Session;

import ecashie.model.database.Bankaccount;
import ecashie.model.database.BankaccountType;

public class BankaccountController
{
	protected void exit()
	{
		DatabaseController.getInstance().getSession().getTransaction().commit();
		DatabaseController.getInstance().getSession().close();
	}

	protected void create()
	{
		Bankaccount bankaccount = new Bankaccount(BankaccountType.getList().get(0), "Test Account");

		Session session = DatabaseController.getInstance().getSessionFactory().openSession();
		session.beginTransaction();

		session.save(bankaccount);

		session.getTransaction().commit();
		session.close();
	}

	protected void read()
	{
		Session session = DatabaseController.getInstance().getSessionFactory().openSession();

		int bankaccountId = 1;
		Bankaccount bankaccount = session.get(Bankaccount.class, bankaccountId);

		System.out.println("Name: " + bankaccount.getName());
		System.out.println("Type: " + bankaccount.getBankaccountType().getName());

		session.close();
	}

	protected void update()
	{
		Bankaccount bankaccount = new Bankaccount(BankaccountType.getList().get(1), "Test Account 1");
		bankaccount.setId(1);

		Session session = DatabaseController.getInstance().getSessionFactory().openSession();
		session.beginTransaction();

		session.update(bankaccount);

		session.getTransaction().commit();
		session.close();
	}

	protected void delete()
	{
		Bankaccount bankaccount = new Bankaccount();
		bankaccount.setId(1);

		Session session = DatabaseController.getInstance().getSessionFactory().openSession();
		session.beginTransaction();

		session.delete(bankaccount);

		session.getTransaction().commit();
		session.close();
	}
}
