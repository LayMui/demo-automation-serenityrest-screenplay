package demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class FarmerDto {
    private String yaraUserId;
    private String firstName;
    private String lastName;
    private String nationalId;
    private String primaryPhoneNumber;
    private String gender;
    private String dateOfBirth;
    private String country;
    private String email;
    private FarmerData farmerData;

    public FarmerDto(String yaraUserId, String firstName, String lastName, String nationalId,
                     String primaryPhoneNumber, String gender,
                     String dateOfBirth, String country, String email,
                     FarmerData farmerData) {
        this.yaraUserId = yaraUserId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationalId = nationalId;
        this.primaryPhoneNumber = primaryPhoneNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.email = email;
        this.farmerData = farmerData;
    }
}

