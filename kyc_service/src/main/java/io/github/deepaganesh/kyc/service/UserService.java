package io.github.deepaganesh.kyc.service;

import io.github.deepaganesh.kyc.common.KycStatus;
import io.github.deepaganesh.kyc.entity.User;
import io.github.deepaganesh.kyc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        user.setKycStatus(KycStatus.PENDING);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}