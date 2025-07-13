package com.hashjosh.agripro.role;

import com.hashjosh.agripro.role.dtos.RoleCreationResponseDto;
import com.hashjosh.agripro.role.dtos.RoleRequestDto;
import com.hashjosh.agripro.role.dtos.RoleResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleCreationResponseDto> create(
            @RequestBody RoleRequestDto roleRequestDto
    ){
        return new ResponseEntity<>(service.createRole(roleRequestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getRoles(
            @RequestParam(required = false) String q
    ) throws RoleNotFoundException {
        if (q != null) {
            RoleResponseDto role = service.findRole(q);

            return new ResponseEntity<>(role, HttpStatus.OK);
        }

        List<RoleResponseDto> roles  = service.findAll();

        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDto> update(
            @PathVariable Long id, @RequestBody RoleRequestDto dto
    ) throws RoleNotFoundException {
        return new ResponseEntity<>(service.update(id, dto),HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws RoleNotFoundException {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
