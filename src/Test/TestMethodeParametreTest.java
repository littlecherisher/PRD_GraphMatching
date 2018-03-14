package Test;

import static junit.framework.TestCase.assertEquals;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Dao.TestMethodeParametreDAO;
import Model.TestMethodeParametre;

public class TestMethodeParametreTest {
	private final TestMethodeParametreDAO testMethodeParametreDAO = new TestMethodeParametreDAO();
    private Model.TestMethodeParametre tmp;
    private Model.TestMethode tm;
    private Model.Parametre p;
    private long id;
    private String v;
    private int size = testMethodeParametreDAO.getAll().size();

    @Before
    public void setUp() throws Exception {
        size = testMethodeParametreDAO.getAll().size();
        tmp = new TestMethodeParametre(tm, p, v);
        testMethodeParametreDAO.save(tmp);
        id = tmp.getId();
    }

    @After
    public void setDown() {
        if (testMethodeParametreDAO.getAll().size() > size) {
        	testMethodeParametreDAO.remove(tmp);
        }
    }

    @Test
    public void get() {
    	TestMethodeParametre tmpTest = testMethodeParametreDAO.get(id);
        assertEquals(p, tmpTest.getParametre());
        assertEquals(tm, tmpTest.getTestMethode());
        assertEquals(v, tmpTest.getValeur());
    }

    @Test
    public void getAll() {
        List<TestMethodeParametre> testMethodeParametres = testMethodeParametreDAO.getAll();
        assertEquals(size + 1, testMethodeParametres.size());
    }

    @Test
    public void update() throws FileNotFoundException {
    	tmp.setTestMethode(tm);
    	tmp.setParametre(p);
        testMethodeParametreDAO.update(tmp);
        TestMethodeParametre tmpTest = testMethodeParametreDAO.get(id);
        assertEquals(tm, tmpTest.getTestMethode());
        assertEquals(p, tmpTest.getParametre());
        assertEquals(v, tmpTest.getValeur());
    }

    @Test
    public void delete() {
    	testMethodeParametreDAO.remove(tmp);
        assertEquals(size, testMethodeParametreDAO.getAll().size());
    }

}
