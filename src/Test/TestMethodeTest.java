package Test;

import static junit.framework.TestCase.assertEquals;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Dao.TestMethodeDAO;
import Model.TestMethode;

public class TestMethodeTest {
	private final TestMethodeDAO testMethodeDAO = new TestMethodeDAO();
    private Model.TestMethode tm;
    private Model.Test t;
    private Model.Methode m;
    private long id;
    private int size = testMethodeDAO.getAll().size();

    @Before
    public void setUp() throws Exception {
        size = testMethodeDAO.getAll().size();
        tm = new TestMethode(t, m);
        testMethodeDAO.save(tm);
        id = tm.getId();
    }

    @After
    public void setDown() {
        if (testMethodeDAO.getAll().size() > size) {
        	testMethodeDAO.remove(tm);
        }
    }

    @Test
    public void get() {
    	TestMethode tmTest = testMethodeDAO.get(id);
        assertEquals(m, tmTest.getMethode());
        assertEquals(t, tmTest.getTest());
    }

    @Test
    public void getAll() {
        List<TestMethode> testMethodes = testMethodeDAO.getAll();
        assertEquals(size + 1, testMethodes.size());
    }

    @Test
    public void update() throws FileNotFoundException {
    	tm.setMethode(m);
    	tm.setTest(t);
        testMethodeDAO.update(tm);
        TestMethode tmTest = testMethodeDAO.get(id);
        assertEquals(m, tmTest.getMethode());
        assertEquals(t, tmTest.getTest());
    }

    @Test
    public void delete() {
    	testMethodeDAO.remove(tm);
        assertEquals(size, testMethodeDAO.getAll().size());
    }
}
