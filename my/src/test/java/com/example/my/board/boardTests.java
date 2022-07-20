package com.example.my.board;

import com.example.my.domain.dto.BoardDTO;
import com.example.my.domain.ProtocolDTO.Board.page.PageRequestDTO;
import com.example.my.domain.ProtocolDTO.Board.page.PageResultDTO;
import com.example.my.domain.entity.Account;
import com.example.my.domain.entity.Board;
import com.example.my.repository.AccountRepo;
import com.example.my.repository.BoardRepo;
import com.example.my.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class boardTests {

    @Autowired
    private BoardRepo boardRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {

        Optional<Account> optionalAccount = accountRepo.findByUserid("qwer");
        Account account = optionalAccount.get();


        for (int i = 0; i < 100; i++) {
            Board board = Board.builder()
                    .account(account)
                    .title("테스트 "+i)
                    .content("테스트sdsdadsadasdasdasdasd "+i)
                    .isImg(0)
                    .build();

            boardRepo.save(board);
        }
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(17)
                .size(5)
                .build();

        PageResultDTO<BoardDTO, Board> resultDTO = boardService.getList(pageRequestDTO);

        System.out.println(resultDTO.isPrev());
        System.out.println(resultDTO.isNext());
        System.out.println(resultDTO.getTotalPage());


        for(BoardDTO boardDTO : resultDTO.getDtoList()) {
            System.out.println(boardDTO);
        }

        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }

}
