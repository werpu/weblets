package net.java.dev.weblets;

public class WebletException extends RuntimeException
{
  public WebletException()
  {
    super();
  }

  public WebletException(
    String message)
  {
    super(message);
  }

  public WebletException(
    String    message,
    Throwable cause)
  {
    super(message, cause);
  }

  public WebletException(
    Throwable cause)
  {
    super(cause);
  }

}