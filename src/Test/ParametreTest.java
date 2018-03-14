package Test;

import static junit.framework.TestCase.assertEquals;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Dao.ParametreDAO;
import Model.Methode;
import Model.Parametre;

public class ParametreTest {
	private final ParametreDAO parametreDAO = new ParametreDAO();
    private Parametre p;
    private Methode m;
    private long id;
    private int size = parametreDAO.getAll().size();

    @Before
    public void setUp() throws Exception {
        size = parametreDAO.getAll().size();
        p = new Parametre("paramètre 1", m, "int");
        parametreDAO.save(p);
        id = p.getId();
    }

    @After
    public void setDown() {
        if (parametreDAO.getAll().size() > size) {
        	parametreDAO.remove(p);
        }
    }

    @Test
    public void get() {
    	Parametre pTest = parametreDAO.get(id);
        assertEquals("paramètre 1", pTest.getNom());
        assertEquals(m, pTest.getMethode());
        assertEquals("int", pTest.getType());
    }

    @Test
    public void getAll() {
        List<Parametre> parametres = parametreDAO.getAll();
        assertEquals(size + 1, parametres.size());
    }

    @Test
    public void update() throws FileNotFoundException {
        p.setNom("paramètre 1");
        p.setMethode(m);
        p.setType("int");
        parametreDAO.update(p);
        Parametre pTest = parametreDAO.get(id);
        assertEquals("paramètre 1", pTest.getNom());   
        assertEquals(m, pTest.getMethode());
        assertEquals("int", pTest.getType());
    }

    @Test
    public void delete() {
    	parametreDAO.remove(p);
        assertEquals(size, parametreDAO.getAll().size());
    }

}
