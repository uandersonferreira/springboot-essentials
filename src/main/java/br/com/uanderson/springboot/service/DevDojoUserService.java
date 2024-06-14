package br.com.uanderson.springboot.service;

import br.com.uanderson.springboot.domain.DevDojoUserDetails;
import br.com.uanderson.springboot.exception.BadRequestException;
import br.com.uanderson.springboot.mapper.DevDojoUserMapper;
import br.com.uanderson.springboot.repository.DevDojoUserRepository;
import br.com.uanderson.springboot.requests.DevDojoUserPostRequest;
import br.com.uanderson.springboot.requests.DevDojoUserPutRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DevDojoUserService {
    private final DevDojoUserRepository devDojoUserRepository;
    private final PasswordEncoder passwordEncoder;

    public DevDojoUserDetails saveUser(DevDojoUserPostRequest devDojoUserPostRequest) {
        DevDojoUserDetails user = DevDojoUserMapper.INSTANCE.toDevDojoUser(devDojoUserPostRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return devDojoUserRepository.save(user);
    }

    public List<DevDojoUserDetails> listAllNoPageable() {
        return devDojoUserRepository.findAll();
    }

    public DevDojoUserDetails findByIdOrThrowBadRequestException(Long id) {
        return devDojoUserRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    public DevDojoUserDetails replaceUser(DevDojoUserPutRequest devDojoUserPutRequest) {
        DevDojoUserDetails savedUser = findByIdOrThrowBadRequestException(devDojoUserPutRequest.getId());
        DevDojoUserDetails user = DevDojoUserMapper.INSTANCE.toDevDojoUser(devDojoUserPutRequest);
        user.setId(savedUser.getId());
        return devDojoUserRepository.save(user);
    }

    public void deleteUserById(Long id) {
        DevDojoUserDetails userToDelete = findByIdOrThrowBadRequestException(id);
        devDojoUserRepository.deleteById(userToDelete.getId());
    }

}
