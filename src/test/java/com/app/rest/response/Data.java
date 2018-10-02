package com.app.rest.response;

import com.app.rest.model.User;

public class Data {

  private User data;

  public Data() {  }

  /**
   * @param data
   */
  public Data(User data) {
    this.data = data;
  }

  /**
   * @return the data
   */
  public User getData() {
    return data;
  }

  /**
   * @param data the data to set
   */
  public void setData(User data) {
    this.data = data;
  }  
}
