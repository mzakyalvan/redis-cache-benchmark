package sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author zakyalvan
 */
@Data
public class StatisticContainer implements Serializable {
  private FlightStatistic flights;

  @JsonProperty("# of delays")
  private DelayCount delayCount;

  @JsonProperty("minutes delayed")
  private MinutesDelayed minutesDelayed;
}
