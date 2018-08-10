package sample.model;

import java.io.Serializable;
import lombok.Data;

/**
 * @author zakyalvan
 */
@Data
public class CarrierData implements Serializable {
  private String code;
  private String name;
}
