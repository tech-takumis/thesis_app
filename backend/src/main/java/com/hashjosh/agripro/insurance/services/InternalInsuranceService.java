package com.hashjosh.agripro.insurance.services;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.models.InsuranceApplication;
import com.hashjosh.agripro.insurance.models.InsuranceField;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import com.hashjosh.agripro.insurance.repository.ApplicationInsuranceRepository;
import com.hashjosh.agripro.insurance.repository.InsuranceFieldRepository;
import com.hashjosh.agripro.insurance.repository.InsuranceTypeRepository;
import com.hashjosh.agripro.user.models.User;
import com.hashjosh.agripro.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternalInsuranceService {
    private final ApplicationInsuranceRepository repository;
    private  final InsuranceTypeRepository insuranceTypeRepository;
    private final InsuranceFieldRepository insuranceFieldRepository;
    private final UserRepository userRepository;

    public InternalInsuranceService(ApplicationInsuranceRepository repository,
                                    InsuranceTypeRepository insuranceTypeRepository,
                                    InsuranceFieldRepository insuranceFieldRepository, UserRepository userRepository) {
        this.repository = repository;
        this.insuranceTypeRepository = insuranceTypeRepository;
        this.insuranceFieldRepository = insuranceFieldRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public  InsuranceType saveInsuranceType(InsuranceType type) {
        return insuranceTypeRepository.save(type);
    }
    @Transactional
    public void saveInsuranceFields(List<InsuranceField> fields) {
        insuranceFieldRepository.saveAll(fields);
    }

    @Transactional
    public void saveInsuranceApplication(InsuranceApplication application) {
        repository.save(application);
        repository.flush();
    }


    public InsuranceType findInsuranceTypeById(Long id) {
        return insuranceTypeRepository.findById(id)
                .orElseThrow(() -> new InvalidApplicationException("Invalid application id:::"+ id));
    }

    public User getAuthenticatedUser(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username:::"+ name));
    }

    public void deleteInsurance(Long id) {
        insuranceTypeRepository.deleteById(id);
    }

    public InsuranceApplication findApplicationInsuranceById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new InvalidApplicationException("Insurance application not found id:: "+id));

    }

    public List<InsuranceApplication> findAllApplicationByUserId(Long id) {
        return repository.findAllByUserId(id);
    }
}
