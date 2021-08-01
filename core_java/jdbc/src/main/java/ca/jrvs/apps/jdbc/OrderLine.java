package ca.jrvs.apps.jdbc;

public class OrderLine {
  private String productCode;
  private String productName;
  private String productVariety;
  private int productSize;
  private float productPrice;
  private int quantity;

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductVariety() {
    return productVariety;
  }

  public void setProductVariety(String productVariety) {
    this.productVariety = productVariety;
  }

  public int getProductSize() {
    return productSize;
  }

  public void setProductSize(int productSize) {
    this.productSize = productSize;
  }

  public float getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(float productPrice) {
    this.productPrice = productPrice;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return "Product{" +
        ", code='" + productCode + '\'' +
        ", name='" + productName + '\'' +
        ", variety='" + productVariety + '\'' +
        ", size=" + productSize +
        ", price=" + productPrice +
        ", quantity=" + quantity +
        '}';
  }
}
