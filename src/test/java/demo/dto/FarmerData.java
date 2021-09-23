package demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
//@Validated
@Builder
public class FarmerData {
    private String language;
    private String photoUrl;
    private String nationalIdUrl;
    private String administrative;
    private String dependent;
    private String locality;
    private String street;
    private String title;
    private String startedFarming;
    private double latitude;
    private double longitude;
    private int zipcode;


    public FarmerData(String language,  String photoUrl, String nationalIdUrl, String administrative,
                      String dependent, String locality, String street, String title,
                      String startedFarming, double latitude, double longitude, int zipcode) {

    }
}
