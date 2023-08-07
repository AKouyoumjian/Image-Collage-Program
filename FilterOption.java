package model;

/**
 * Enumeration to represent different types of filters applied to layers.
 */
public enum FilterOption implements IFilterOption {
  NORM("normal"),
  RED("red-component"),
  BLUE("blue-component"),
  GREEN("green-component"),
  BRIGHTV("brighten-value"),
  DARKV("darken-value"),
  BRIGHTI("brighten-intensity"),
  DARKI("darken-intensity"),
  BRIGHTL("brighten-luma"),
  DARKL("darken-luma"),
  DIFFERENCE("difference"),
  MULTIPLY("multiply"),
  SCREEN("screen"),
  ERROR("unsupported");
  // ERROR is only used for testing exceptions

  private final String descriptor;

  FilterOption(String descriptor) {
    this.descriptor = descriptor;
  }

  /**
   * ToString method converting fo into a string.
   * @return the descriptor of this IFilterOption
   */
  @Override
  public String toString() {
    return this.descriptor;
  }

  /**
   * From string method converts a string to a FilterOption.
   * @param str String
   * @return the FilterOption corresponding with the string.
   */
  public FilterOption fromString(String str) throws IllegalArgumentException {

    switch (str) {
      case "normal":
        return FilterOption.NORM;
      case "red-component":
        return FilterOption.RED;
      case "blue-component":
        return FilterOption.BLUE;
      case "green-component":
        return FilterOption.GREEN;
      case "brighten-value":
        return FilterOption.BRIGHTV;
      case "darken-value":
        return FilterOption.DARKV;
      case "brighten-intensity":
        return FilterOption.BRIGHTI;
      case "darken-intensity":
        return FilterOption.DARKI;
      case "brighten-luma":
        return FilterOption.BRIGHTL;
      case "darken-luma":
        return FilterOption.DARKL;
      case "multiply":
        return FilterOption.MULTIPLY;
      case "difference":
        return FilterOption.DIFFERENCE;
      case "screen":
        return FilterOption.SCREEN;
      default:
        throw new IllegalArgumentException("Filter option with that name does not exist.");
    }
  }
}