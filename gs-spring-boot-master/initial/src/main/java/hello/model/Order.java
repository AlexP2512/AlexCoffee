package hello.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * Order
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-04-26T13:48:15.275Z")

public class Order {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("cellNumber")
  private String cellNumber = null;

  @JsonProperty("address")
  private String address = null;

  @JsonProperty("orderDetails")
  private String orderDetails = null;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCellNumber() {
    return cellNumber;
  }

  public void setCellNumber(String cellNumber) {
    this.cellNumber = cellNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getOrderDetails() {
    return orderDetails;
  }

  public void setOrderDetails(String orderDetails) {
    this.orderDetails = orderDetails;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order order = (Order) o;
    return Objects.equals(this.name, order.name) &&
        Objects.equals(this.cellNumber, order.cellNumber) &&
        Objects.equals(this.address, order.address) &&
        Objects.equals(this.orderDetails, order.orderDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, cellNumber, address, orderDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Order {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    cellNumber: ").append(toIndentedString(cellNumber)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    orderDetails: ").append(toIndentedString(orderDetails)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

