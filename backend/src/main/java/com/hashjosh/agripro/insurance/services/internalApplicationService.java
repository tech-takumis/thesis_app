package com.hashjosh.agripro.insurance.services;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.models.InsuranceApplication;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import com.hashjosh.agripro.insurance.repository.ApplicationInsuranceRepository;
import com.hashjosh.agripro.insurance.repository.InsuranceTypeRepository;
import com.hashjosh.agripro.user.models.User;
import com.hashjosh.agripro.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class internalApplicationService {

    private final UserRepository userRepository;
    private final InsuranceTypeRepository insuranceTypeRepository;
    private final ApplicationInsuranceRepository applicationInsuranceRepository;

    public internalApplicationService(UserRepository userRepository, InsuranceTypeRepository insuranceTypeRepository, ApplicationInsuranceRepository applicationInsuranceRepository) {
        this.userRepository = userRepository;
        this.insuranceTypeRepository = insuranceTypeRepository;
        this.applicationInsuranceRepository = applicationInsuranceRepository;
    }

    public User getAuthenticatedUser(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username:::"+ name));
    }

    public InsuranceType findInsuranceTypeById(Long id) {
        return insuranceTypeRepository.findById(id)
                .orElseThrow(() -> new InvalidApplicationException("Invalid application id:::"+ id));
    }

    @Transactional
    public void saveInsuranceApplication(InsuranceApplication application) {
        applicationInsuranceRepository.save(application);
        applicationInsuranceRepository.flush();
    }

    public List<InsuranceApplication> findAllApplicationByUserId(Long id) {
        return applicationInsuranceRepository.findAllByUserId(id);
    }

}
