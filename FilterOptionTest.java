import org.junit.Test;
import model.FilterOption;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test class for FilterOption methods.
 */
public class FilterOptionTest {


  /**
   * Test for to string method on a FilterOption.
   */
  @Test
  public void testToString() {
    assertEquals("normal", FilterOption.NORM.toString());
    assertEquals("red-component", FilterOption.RED.toString());
    assertEquals("blue-component", FilterOption.BLUE.toString());
    assertEquals("green-component", FilterOption.GREEN.toString());
    assertEquals("darken-value", FilterOption.DARKV.toString());
    assertEquals("brighten-value", FilterOption.BRIGHTV.toString());

    assertEquals("darken-intensity", FilterOption.DARKI.toString());
    assertEquals("brighten-intensity", FilterOption.BRIGHTI.toString());

    assertEquals("darken-luma", FilterOption.DARKL.toString());
    assertEquals("brighten-luma", FilterOption.BRIGHTL.toString());
  }

  /**
   * Test for from string method on a FilterOption.
   */
  @Test
  public void testFromString() {
    assertEquals(FilterOption.NORM.fromString("normal").toString(), "normal");
    assertEquals(FilterOption.NORM.fromString("red-component").toString(), "red-component");
    assertEquals(FilterOption.NORM.fromString("blue-component").toString(),
            "blue-component");
    assertEquals(FilterOption.NORM.fromString("green-component").toString(),

            "green-component");
    assertEquals(FilterOption.NORM.fromString("darken-value").toString(),
            "darken-value");
    assertEquals(FilterOption.NORM.fromString("brighten-value").toString(),
            "brighten-value");

    assertEquals(FilterOption.NORM.fromString("darken-intensity").toString(),
            "darken-intensity");
    assertEquals(FilterOption.NORM.fromString("brighten-intensity").toString(),
            "brighten-intensity");

    assertEquals(FilterOption.NORM.fromString("darken-luma").toString(),
            "darken-luma");
    assertEquals(FilterOption.NORM.fromString("brighten-luma").toString(),
            "brighten-luma");


    try {
      FilterOption.NORM.fromString("wrong-name");
      fail();
    } catch (IllegalArgumentException e) {
      // do nothing
    }
  }
}
