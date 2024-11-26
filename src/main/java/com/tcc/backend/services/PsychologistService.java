package com.tcc.backend.services;

import com.tcc.backend.dtos.psychologist.PsychologistRequest;
import com.tcc.backend.dtos.user.UserRequest;
import com.tcc.backend.enums.UserType;
import com.tcc.backend.models.Psychologist;
import com.tcc.backend.models.User;
import com.tcc.backend.repositories.PsychologistRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PsychologistService {

    private final PsychologistRepository repository;
    private final UserService userService;

    @Autowired
    public PsychologistService(PsychologistRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public Psychologist createPsychologist(final PsychologistRequest psychologistRequest) {
        UserRequest userRequest = UserRequest.builder()
                .type(UserType.PSICOLOGO)
                .name(psychologistRequest.getName())
                .email(psychologistRequest.getEmail())
                .password(psychologistRequest.getPassword()) // Senha obrigatória
                .cpf(psychologistRequest.getCpf())
                .phone(psychologistRequest.getPhone())
                .build();

        User createdUser = userService.create(userRequest);

        // Criar psicólogo
        Psychologist psychologist = Psychologist.builder()
                .idUser(createdUser)
                .crp(psychologistRequest.getCrp())
                .build();

        return repository.save(psychologist);
    }

    public Psychologist updatePsychologist(Long idPsychologist, PsychologistRequest psychologistRequest) {
        Psychologist existingPsychologist = getById(idPsychologist);

        UserRequest userRequest = UserRequest.builder()
                .idUser(existingPsychologist.getIdUser().getIdUser())
                .type(UserType.PSICOLOGO)
                .name(psychologistRequest.getName())
                .email(psychologistRequest.getEmail())
                .password(psychologistRequest.getPassword()) // Senha obrigatória
                .cpf(psychologistRequest.getCpf())
                .phone(psychologistRequest.getPhone())
                .build();

        userService.update(existingPsychologist.getIdUser().getIdUser(), userRequest);

        existingPsychologist.setCrp(psychologistRequest.getCrp());

        return repository.save(existingPsychologist);
    }

    public void delete(Long idPsychologist) {
        Psychologist psychologist = repository.findById(idPsychologist).orElseThrow(() ->
                new IllegalArgumentException("Psicólogo não encontrado com o ID: " + idPsychologist));
        userService.delete(psychologist.getIdUser().getIdUser());
        repository.delete(psychologist);
    }

    public Psychologist getById(Long idPsychologist) {
        return repository.findById(idPsychologist).orElseThrow(() ->
                new IllegalArgumentException("Psicólogo não encontrado com o ID: " + idPsychologist));
    }

    public Page<Psychologist> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
