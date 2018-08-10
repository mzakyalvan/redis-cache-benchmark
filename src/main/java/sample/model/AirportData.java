package sample.model;

import java.io.Serializable;
import lombok.Data;

/**
 * @author zakyalvan
 */
@Data
public class AirportData implements Serializable {
  private String code;
  private String name;
}
