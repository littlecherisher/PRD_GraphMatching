package Test;

import Dao.DatasetDAO;
import Dao.SubsetDAO;
import Dao.TestDAO;
import Model.Dataset;
import Model.Subset;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestTest {
    private final TestDAO testDAO = new TestDAO();
    private Model.Test t;
    private long id;
    private int size = testDAO.getAll().size();

    @Before
    public void setUp() throws Exception {
        size = testDAO.getAll().size();
        t = new Model.Test("test", "description du test 1", 2);
        testDAO.save(t);
        id = t.getId();
    }

    @After
    public void setDown() {
        if (testDAO.getAll().size() > size) {
            testDAO.remove(t);
        }
    }

    @Test
    public void get() {
        Model.Test tTest = testDAO.get(id);
        assertEquals("test", tTest.getNom());
        assertEquals("description du test 1", tTest.getDescription());
        assertTrue(tTest.isVisible());
    }

    @Test
    public void getAll() {
        List<Model.Test> tests = testDAO.getAll();
        assertEquals(size + 1, tests.size());
    }

    @Test
    public void update() {
        t.setVisible();
        t.setNom("test1 update");
        t.setDescription("test update");
        t.setMode(3);
        t.addParam("param", "test");
        DatasetDAO datasetDAO = new DatasetDAO();
        Dataset d = new Dataset("data", "test");
        datasetDAO.save(d);
        t.addData(String.valueOf(d.getId()));
        SubsetDAO subsetDAO = new SubsetDAO();
        Subset s = new Subset("data\\un sub.gxl", d, "un sub");
        subsetDAO.save(s);
        t.addData(d.getId() + "-" + s.getId());
        testDAO.update(t);

        Model.Test tTest = testDAO.get(id);
        assertEquals("test1 update", tTest.getNom());
        assertFalse(tTest.isVisible());
        assertEquals("test update", tTest.getDescription());
        assertEquals(1, tTest.getSubsets().size());
        assertEquals(1, tTest.getParametres().size());
        assertEquals(1, tTest.getDatasets().size());
    }

    @Test
    public void delete() {
        testDAO.remove(t);
        assertEquals(size, testDAO.getAll().size());
    }
}
