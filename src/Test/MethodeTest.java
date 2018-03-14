package Test;

import Dao.MethodeDAO;
import Model.Methode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class MethodeTest {
    private final MethodeDAO methodeDAO = new MethodeDAO();
    private Methode m;
    private long id;
    private int size = methodeDAO.getAll().size();

    @Before
    public void setUp() throws Exception {
        size = methodeDAO.getAll().size();
        m = new Methode("méthode 1", "description de la méthode 1");
        methodeDAO.save(m);
        id = m.getId();
    }

    @After
    public void setDown() {
        if (methodeDAO.getAll().size() > size) {
        	methodeDAO.remove(m);
        }
    }

    @Test
    public void get() {
    	Methode mTest = methodeDAO.get(id);
        assertEquals("méthode 1", mTest.getNom());
        assertEquals("description de la méthode 1", mTest.getDescription());
        assertTrue(mTest.getVisible());
    }

    @Test
    public void getAll() {
        List<Methode> methodes = methodeDAO.getAll();
        assertEquals(size + 1, methodes.size());
    }

    @Test
    public void update() throws FileNotFoundException {
        m.setVisible();
        m.setNom("méthode 1 update");
        m.setDescription("test update");
        methodeDAO.update(m);
        Methode mTest = methodeDAO.get(id);
        assertEquals("méthode 1 update", mTest.getNom());
        assertFalse(mTest.getVisible());
        assertEquals("test update", mTest.getDescription());
        assertFalse(mTest.getVisible());
    }

    @Test
    public void delete() {
    	methodeDAO.remove(m);
        assertEquals(size, methodeDAO.getAll().size());
    }
}
