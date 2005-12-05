package net.java.dev.weblets;

public interface WebletRequest
{
  public String getWebletName();
  public String getWebletPath();
  public String getContextPath();
  public String getPathInfo();

  public long getIfModifiedSince();
}