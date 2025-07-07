package com.hashjosh.agripro.rsbsa;

import com.hashjosh.agripro.rsbsa.dto.RsbsaRequestDto;
import com.hashjosh.agripro.rsbsa.dto.RsbsaResponseDto;
import com.hashjosh.agripro.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RsbsaService {

    private final RsbsaRepository repository;
    private final UserRepository userRepository;
    private final RsbsaMapper mapper;

    public RsbsaService(RsbsaRepository repository, UserRepository userRepository,
                        RsbsaMapper mapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.mapper = mapper;

    }


    public RsbsaResponseDto save(RsbsaRequestDto dto) {
        RsbsaModel rsbsa = mapper.dtoToRsbsa(dto);
        return  mapper.rsbsaToReponseDto(repository.save(rsbsa));
    }

    public List<RsbsaResponseDto> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::rsbsaToReponseDto)
                .collect(Collectors.toList());
    }
}
