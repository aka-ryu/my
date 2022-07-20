package com.example.my.service;

import com.example.my.domain.ProtocolDTO.UserListRespDTO;
import com.example.my.domain.dto.AccountDTO;
import com.example.my.domain.entity.Account;
import com.example.my.repository.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class AdminService {

    @Autowired
    private  AccountRepo accountRepo;
    @Autowired
    private  ModelMapper modelMapper;

    public UserListRespDTO userList(){
        log.info("어드민리스트 진입");

        List<Account> accoutList = accountRepo.findAll();
        System.out.println(accoutList);
        List<AccountDTO> accountDTOList = new ArrayList<>();


        accoutList.forEach(account -> {
            AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
            accountDTOList.add(accountDTO);
        });
        System.out.println(accountDTOList);


        UserListRespDTO userListRespDTO = UserListRespDTO.builder()
                .code(200)
                .success(true)
                .data(accountDTOList)
                .build();

        return userListRespDTO;
    }
}
