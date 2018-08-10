package sample.model;

import java.io.Serializable;
import lombok.Data;

/**
 * @author zakyalvan
 */
@Data
public class AirlineStatistic implements Serializable {
  private AirportData airport;
  private StatisticContainer statistics;
  private TimePeriod time;
  private CarrierData carrier;
}
