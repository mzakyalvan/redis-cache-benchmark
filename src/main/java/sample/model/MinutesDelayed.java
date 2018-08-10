package sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author zakyalvan
 */
@Data
public class MinutesDelayed implements Serializable {
  @JsonProperty("late aircraft")
  private Integer lateAircraft;
  private Integer weather;
  private Integer security;
  @JsonProperty("national aviation system")
  private Integer nationalAviationSystem;
  private Integer carrier;
  private Integer total;
}
