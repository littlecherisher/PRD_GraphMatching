package Test;

import Dao.DatasetDAO;
import Model.Dataset;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatasetTest {
    private final DatasetDAO datasetDAO = new DatasetDAO();
    private Dataset d;
    private long id;
    private int size;

    @Before
    public void setUp() throws Exception {
        size = datasetDAO.getAll().size();
        d = new Dataset("dataset1", "description du dataset 1");
        datasetDAO.save(d);
        id = d.getId();
    }

    @After
    public void setDown() {
        if (datasetDAO.getAll().size() > size) {
            datasetDAO.remove(d);
        }
    }

    @Test
    public void get() {
        Dataset dTest = datasetDAO.get(id);
        assertEquals("dataset1", dTest.getNom());
        assertEquals("description du dataset 1", dTest.getDescription());
        assertTrue(dTest.getVisible());
    }

    @Test
    public void getAll() {
        List<Dataset> datasets = datasetDAO.getAll();
        assertEquals(size + 1, datasets.size());
    }

    @Test
    public void update() throws Exception {
        d.setVisible();
        d.setNom("dataset1 update");
        d.setDescription("test update");
        d.setDataset(new FileInputStream("F:\\100ZENGKai\\PRD\\sources\\Data\\Datasets\\1\\Dataset.zip"), "Dataset.zip");
        d.setSubset(new FileInputStream("F:\\100ZENGKai\\PRD\\sources\\Data\\Datasets\\1\\Subset.zip"), "Subset.zip");
        datasetDAO.update(d);

        Dataset dTest = datasetDAO.get(id);
        assertEquals("dataset1 update", dTest.getNom());
        assertFalse(dTest.getVisible());
        assertEquals("test update", dTest.getDescription());
        assertEquals("Datasets\\" + id + "\\Dataset", dTest.getDataset());
        assertEquals("Datasets\\" + id + "\\Subset", dTest.getSubset());
        assertEquals(8, dTest.getSubsets().size());
    }

    @Test
    public void delete() {
        datasetDAO.remove(d);
        assertEquals(size, datasetDAO.getAll().size());
    }
}
