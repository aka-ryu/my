package com.example.my.user;

import com.example.my.domain.ProtocolDTO.UserListRespDTO;
import com.example.my.domain.dto.AccountDTO;
import com.example.my.domain.entity.Account;
import com.example.my.repository.AccountRepo;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class adminTests {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountRepo accountRepo;

    @Test
    public void test() {

        List<Account> accoutList = accountRepo.findAll();
        System.out.println(accoutList);
        List<AccountDTO> accountDTOList = new ArrayList<>();


        accoutList.forEach(account -> {
            AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
            accountDTOList.add(accountDTO);
        });
        System.out.println("변환");
        System.out.println(accountDTOList);


        UserListRespDTO userListRespDTO = UserListRespDTO.builder()
                .code(200)
                .success(true)
                .data(accountDTOList)
                .build();

        System.out.println(userListRespDTO);
    }


}
