package sample.model;

import java.io.Serializable;
import lombok.Data;

/**
 * @author zakyalvan
 */
@Data
public class TimePeriod implements Serializable {
  private String label;
  private Integer year;
  private Integer month;
}
