/*
 * This example was written by Bruno Lowagie, author of the book
 * 'iText in Action' by Manning Publications (ISBN: 1932394796).
 * You can use this example as inspiration for your own applications.
 * The following license applies:
 * http://www.1t3xt.com/about/copyright/index.php?page=MIT
 */

package classroom.database;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import classroom.festivaldatabase.FilmTitle;

public class HibernateTest {
	public final static Logger LOGGER = Logger.getLogger(HibernateTest.class.getName());
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Session session = null;
		try {
			session = (Session) MySessionFactory.currentSession();
			Query q = session.createQuery("from FilmTitle order by title");
			List<FilmTitle> results = q.list();
			for (FilmTitle movie : results) {
				System.out.println(movie.getTitle());
			}
		} catch (HibernateException he) {
			LOGGER.warn("HibernateTest - Main: " + he);
		} finally {
			try {
				if (session != null) {
					session.close();
				}
			} catch (HibernateException e) {
				LOGGER.warn("HibernateTest - Closing session: " + e);
			}
		}
	}
}
