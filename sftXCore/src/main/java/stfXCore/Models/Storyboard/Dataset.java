package stfXCore.Models.Storyboard;

import lombok.Data;

import java.util.ArrayList;

/**
 * Lombok annotation to create all the getters, setters, equals, hash, and toString methods, based on the fields
 */
@Data
/**
 * Helper class for representing the used dataset.
 * This model is not saved in the database.
 */
public class Dataset {

    private ArrayList<ArrayList<ArrayList<Float>>> dataset;

    public Dataset() {}
}