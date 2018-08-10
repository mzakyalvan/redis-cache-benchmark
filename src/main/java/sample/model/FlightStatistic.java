package sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author zakyalvan
 */
@Data
public class FlightStatistic implements Serializable {
  private String cancelled;
  @JsonProperty("on time")
  private Integer onTime;
  private Integer total;
  private Integer delayed;
  private Integer diverted;
}
