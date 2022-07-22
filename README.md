스프링부트 + 리액트<br/>
이것저것 만들어보는 토이프로젝트

ㅁ진행완료단계

-스프링시큐리티 + JWT(10Days Refresh Token)<br/>
-새로고침시 SPA의 세션 유지 (cookie 보관)<br/>
-로그인유지 30일 (cookie 기간 30일)<br/>

-게시글조회, 등록은 회원만 가능<br>
-수정,삭제는 작성자만 가능<br>

-댓글등록, 삭제 (수정 미구현)<br>

-이미지첨부 (google cloud storage 이용)<br>
-게시글 작성시 이미지 첨부,<br>
-게시글 수정지 이미지 수정 가능 (null -> 첨부 , 첨부된 이미지 삭제, 이미지교체)<br>
-첨부된 이미지 다운로드<br>

-새로고침시 에도 front state 유지 (cookies로 재인증처리)<br>
-url 직접입력등의 비정상 적인 접근 차단 (글보기,글작성 비회원불가 및 글수정 작성자만가능 등)<br>

-react gcp app engine deploy 완료<br>
-database gcp mysql<br>
-spiring boot local ngrok deploy(gcp failed...) <br>

-이메일 인증된 유저만 가입가능



