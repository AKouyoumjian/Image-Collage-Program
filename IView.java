package view;

import java.io.IOException;

/**
 * Interface which details the methods to display a visual project with layers.
 */
public interface IView {

  /**
   * Renders a message in the view.
   *
   * @param message is the message to be rendered
   * @throws IOException if the view is unable to transmit a rendering of the message
   */
  void renderMessage(String message) throws IOException;
}