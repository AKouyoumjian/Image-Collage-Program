

import java.io.IOException;
import view.IView;

import model.IProject;
import view.CollageTextView;

/**
 * A view representation meant entirely for testing. Always throws an IOException in its methods.
 */
public class CollageExceptionThrowerView extends CollageTextView implements IView {
  public CollageExceptionThrowerView(IProject model, Appendable dest) {
    super(model, dest);
  }

  /**
   * This implementation of renderMessage always throws an IOException.
   *
   * @param message is the message to be rendered
   * @throws IOException is thrown whenever this method is called
   */
  @Override
  public void renderMessage(String message) throws IOException {
    throw new IOException("Could not transmit to the destination.");
  }
}