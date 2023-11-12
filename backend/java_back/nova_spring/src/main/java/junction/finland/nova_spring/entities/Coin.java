package junction.finland.nova_spring.entities;

import jakarta.persistence.*;
import junction.finland.nova_spring.model.nominative.ReverseResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "coin")
@Getter
@Setter
public class Coin implements Serializable {

    @Serial
    private static final long serialVersionUID = 2405172041950251807L;


    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "coin_seq", sequenceName = "coin_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coin_seq")
    private Long id;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "start_longitude")
    private Double startLongitude;

    @Column(name = "start_latitude")
    private Double startLatitude;

    @Column(name = "stop_longitude")
    private Double stopLongitude;

    @Column(name = "stop_latitude")
    private Double stopLatitude;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "osm_id")
    private Long osmId;

    @Column(name = "completed")
    private Boolean completed = false;

    @Transient
    public boolean isStopped(){
        return ((this.getStopLatitude() != null ) && (this.getStopLongitude() != null));
    }

    public static Coin loadFromResponse(ReverseResponse response, String userId, String startLatitude, String startLongitude) {
        Coin coin = new Coin();
        coin.setStartLatitude(Double.valueOf(startLatitude));
        coin.setStartLongitude(Double.valueOf(startLongitude));
        coin.setLatitude(Double.valueOf(response.getLat()));
        coin.setLongitude(Double.valueOf(response.getLon()));
        coin.setOsmId(Long.valueOf(response.getOsmId()));
        coin.setUserId(Long.valueOf(userId));
        return coin;
    }
}
