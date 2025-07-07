package com.hashjosh.agripro.rsbsa;

import com.hashjosh.agripro.rsbsa.dto.RsbsaRequestDto;
import com.hashjosh.agripro.rsbsa.dto.RsbsaResponseDto;
import com.hashjosh.agripro.user.models.User;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RsbsaMapper {

    private final PasswordEncoder passwordEncoder;

    public RsbsaMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User rsbsaToUser(RsbsaModel rsbsa) {
        return  User.builder()
                .username(rsbsa.getRsbsaId())
                .email(rsbsa.getEmail())
                .password(passwordEncoder.encode(rsbsa.getDateOfBirth().toString()))
                .fullname(rsbsa.getFirstName() + " " + rsbsa.getLastName())
                .role("FARMER")
                .build();
    }

    public RsbsaModel dtoToRsbsa(RsbsaRequestDto dto) {
        return RsbsaModel.builder()
                .rsbsaId(dto.rsbsaId())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .middleName(dto.middleName())
                .gender(dto.gender())
                .civilStatus(dto.civilStatus())
                .address(dto.address())
                .barangay(dto.barangay())
                .municipality(dto.municipality())
                .province(dto.province())
                .contactNumber(dto.contactNumber())
                .email(dto.email())
                .dateOfBirth(LocalDate.parse(dto.dateOfBirth()))
                .farmingType(dto.farmingType())
                .primaryCrop(dto.primaryCrop())
                .secondaryCrop(dto.secondaryCrop())
                .farmArea(dto.farmArea())
                .farmLocation(String.valueOf(dto.farmLocation()))
                .tenureStatus(dto.tenureStatus())
                .sourceOfIncome(dto.sourceOfIncome())
                .estimatedIncome(dto.estimatedIncome())
                .householdSize(dto.householdSize())
                .educationLevel(String.valueOf(dto.educationLevel()))
                .withDisability(dto.withDisability())
                .build();
    }

    public RsbsaResponseDto rsbsaToReponseDto(RsbsaModel save) {
        return  new RsbsaResponseDto(
                save.getRsbsaId(),save.getFirstName(), save.getLastName(), save.getMiddleName(),
                save.getGender(), save.getCivilStatus(),save.getAddress(), save.getBarangay(),
                save.getMunicipality(),save.getProvince(),save.getContactNumber(),save.getEmail(),
                save.getFarmingType(), save.getPrimaryCrop(), save.getSecondaryCrop(), save.getDateOfBirth().toString() , save.getFarmArea(),
                save.getFarmLocation(), save.getTenureStatus(), save.getSourceOfIncome(), save.getEstimatedIncome(),
                save.getHouseholdSize(), save.getEducationLevel(),save.isWithDisability()
        );
    }
}
